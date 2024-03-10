package ru.hits.trb.trbloans.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.trb.trbloans.dto.UnidirectionalTransactionDto;
import ru.hits.trb.trbloans.dto.transaction.TransactionState;
import ru.hits.trb.trbloans.entity.LoanEntity;
import ru.hits.trb.trbloans.entity.LoanRepaymentEntity;
import ru.hits.trb.trbloans.entity.enumeration.LoanRepaymentState;
import ru.hits.trb.trbloans.entity.enumeration.LoanState;
import ru.hits.trb.trbloans.exception.ConflictException;
import ru.hits.trb.trbloans.exception.NotFoundException;
import ru.hits.trb.trbloans.producer.LoanRepaymentProducer;
import ru.hits.trb.trbloans.repository.LoanRepaymentRepository;
import ru.hits.trb.trbloans.repository.LoanRepository;
import ru.hits.trb.trbloans.service.RepaymentService;
import ru.hits.trb.trbloans.util.DateUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepaymentServiceImpl implements RepaymentService {

    private final LoanRepaymentRepository repaymentRepository;
    private final LoanRepaymentProducer loanRepaymentProducer;
    private final LoanRepository loanRepository;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void processRepaymentCallback(UUID repaymentId, TransactionState state) {
        var loanRepayment = getLoanRepayment(repaymentId);

        if (state == TransactionState.DONE) {
            processSuccessRepaymentCallback(loanRepayment);
        } else {
            processFailedRepaymentCallback(loanRepayment);
        }
    }

    @Override
    public void sendRepaymentTransactions() {
        var toDate = DateUtil.getStartNextDate();
        var repayments = repaymentRepository.findOpenAndRejectedRepaymentsToDate(toDate);

        for (var repayment : repayments) {
            updateRepayment(repayment);
        }
    }

    @Override
    public void updateRepaymentsStatus() {
        var toDate = DateUtil.getDateSeveralHoursAgo(1);
        var repayments = repaymentRepository.findAllByDateOfLastTransactionIsBeforeAndState(
                toDate,
                LoanRepaymentState.IN_PROGRESS
        );

        repayments.forEach(repayment -> repayment.setState(LoanRepaymentState.REJECTED));

        repaymentRepository.saveAll(repayments);
    }

    @Override
    @Transactional
    public void calculatePenny() {
        List<LoanEntity> loans = getLoansToCalculatePenny();

        for (var loan : loans) {
            var repayments = loan.getRepayments();
            var penny = 0L;
            var interestRate = loan.getTariff().getInterestRate();
            var repaymentsForPenny = repayments.stream()
                    .filter(repayment -> repayment.getDate().before(DateUtil.getStartCurrentDay())
                            && repayment.getState() == LoanRepaymentState.REJECTED)
                    .toList();

            for (var rejectedRepayment : repaymentsForPenny) {
                penny += interestRate
                        .multiply(BigDecimal.valueOf(rejectedRepayment.getAmount())
                                .divide(BigDecimal.valueOf(100), RoundingMode.DOWN)
                        ).longValue();
                log.info("current penny: {}", penny);
            }

            var futureRepaymentCount = repayments.stream()
                    .filter(r -> r.getState() != LoanRepaymentState.DONE
                            && r.getState() != LoanRepaymentState.IN_PROGRESS)
                    .count();

            var appendixToNotClosedRepayment = penny / futureRepaymentCount;
            log.info("appendix {}", appendixToNotClosedRepayment);

            loan.setAmountDebt(loan.getAmountDebt() + appendixToNotClosedRepayment * futureRepaymentCount);
            loan.setAmountLoan(loan.getIssuedAmount() + loan.getAmountDebt());
            loan.setAccruedPenny(loan.getAccruedPenny() + penny);

            var repaymentsToRecalculateAmount = repayments.stream()
                    .filter(r -> r.getState() != LoanRepaymentState.DONE
                            && r.getState() != LoanRepaymentState.IN_PROGRESS)
                    .toList();

            for (var repayment : repaymentsToRecalculateAmount) {
                repayment.setAmount(repayment.getAmount() + appendixToNotClosedRepayment);
            }
        }

        loanRepository.saveAll(loans);
    }

    private List<LoanEntity> getLoansToCalculatePenny() {
        var startCurrentDay = DateUtil.getStartCurrentDay();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<LoanEntity> criteriaQuery = criteriaBuilder.createQuery(LoanEntity.class);
        Root<LoanEntity> root = criteriaQuery.from(LoanEntity.class);

        Join<LoanEntity, LoanRepaymentEntity> repaymentsJoin = root.join("repayments", JoinType.INNER);

        Predicate datePredicate = criteriaBuilder.lessThanOrEqualTo(repaymentsJoin.get("date"), startCurrentDay);
        Predicate statePredicate = criteriaBuilder.equal(repaymentsJoin.get("state"), LoanRepaymentState.REJECTED);
        Predicate finalPredicate = criteriaBuilder.and(datePredicate, statePredicate);

        criteriaQuery.select(root)
                .distinct(true)
                .where(finalPredicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private void updateRepayment(LoanRepaymentEntity repayment) {
        var transaction = UnidirectionalTransactionDto.builder()
                .accountId(repayment.getLoan().getAccountId())
                .amount(repayment.getAmount())
                .build();

        repayment.setDateOfLastTransaction(new Date());
        repayment.setState(LoanRepaymentState.IN_PROGRESS);

        repaymentRepository.save(repayment);
        loanRepaymentProducer.sendMessage(repayment.getId(), transaction);
    }

    private void processSuccessRepaymentCallback(LoanRepaymentEntity loanRepayment) {
        loanRepayment.setState(LoanRepaymentState.DONE);

        var loan = loanRepayment.getLoan();
        var repayments = loan.getRepayments();

        var openRepaymentsCount = repayments.stream()
                .filter(repayment -> repayment.getState() != LoanRepaymentState.DONE)
                .count();

        if (openRepaymentsCount == 0) {
            loan.setState(LoanState.CLOSED);
            log.info("Loan {} closed", loan.getId());
        }

        loan.setAmountDebt(loan.getAmountDebt() - loanRepayment.getAmount());
        loan.setAmountLoan(loan.getIssuedAmount() + loan.getAmountDebt());

        repaymentRepository.save(loanRepayment);
    }

    private void processFailedRepaymentCallback(LoanRepaymentEntity loanRepayment) {
        log.info("Loan repayment {} failed", loanRepayment.getId());
        loanRepayment.setState(LoanRepaymentState.REJECTED);
        loanRepayment.setDateOfLastTransaction(new Date());
        repaymentRepository.save(loanRepayment);
    }

    private LoanRepaymentEntity getLoanRepayment(UUID repaymentId) {
        var loanRepayment = repaymentRepository.findById(repaymentId)
                .orElseThrow(() -> new NotFoundException("Loan repayment with id '" + repaymentId + "' not found"));

        if (loanRepayment.getState() == LoanRepaymentState.DONE) {
            throw new ConflictException("Repayment with id '" + repaymentId + "' is done");
        }

        return loanRepayment;
    }

}
