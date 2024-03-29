package ru.hits.trb.trbloans.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.trb.trbloans.dto.transaction.InitTransactionDto;
import ru.hits.trb.trbloans.dto.transaction.TransactionState;
import ru.hits.trb.trbloans.dto.transaction.TransactionType;
import ru.hits.trb.trbloans.entity.LoanEntity;
import ru.hits.trb.trbloans.entity.LoanRepaymentEntity;
import ru.hits.trb.trbloans.entity.enumeration.LoanRepaymentState;
import ru.hits.trb.trbloans.entity.enumeration.LoanState;
import ru.hits.trb.trbloans.exception.InternalServiceException;
import ru.hits.trb.trbloans.producer.TransactionProducer;
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

    private static final List<LoanRepaymentState> STATES_FOR_REPAYMENT = List.of(
            LoanRepaymentState.OPEN,
            LoanRepaymentState.REJECTED
    );

    private final LoanRepaymentRepository repaymentRepository;
    private final TransactionProducer transactionProducer;
    private final LoanRepository loanRepository;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public void processRepaymentCallback(UUID internalTransactionId, TransactionState state) {
        var loanRepayment = findLoanRepaymentByInternalTransactionId(internalTransactionId);

        if (state == TransactionState.DONE) {
            processSuccessRepaymentCallback(loanRepayment);
        } else {
            processFailedRepaymentCallback(loanRepayment);
        }
    }

    @SuppressWarnings("SpringTransactionalMethodCallsInspection")
    @Override
    public void sendRepaymentTransactions() {
        var toDate = DateUtil.getStartNextDate();
        var repayments = repaymentRepository.findAllByStateInAndDateLessThanEqual(STATES_FOR_REPAYMENT, toDate);

        for (var repayment : repayments) {
            processRepayment(repayment);
        }
    }

    @Transactional
    protected void processRepayment(LoanRepaymentEntity repayment) {
        var internalTransactionId = UUID.randomUUID();

        var updatedRepayment = updateRepayment(repayment, internalTransactionId);

        var transaction = InitTransactionDto.builder()
                .payerAccountId(updatedRepayment.getLoan().getAccountId())
                .amount(updatedRepayment.getAmount())
                .type(TransactionType.LOAN_REPAYMENT)
                .currency(repayment.getCurrency())
                .build();

        transactionProducer.sendMessage(internalTransactionId, transaction);
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
                        .multiply(rejectedRepayment.getAmount().divide(BigDecimal.valueOf(100), RoundingMode.DOWN))
                        .longValue();
                log.info("current penny: {}", penny);
            }

            var futureRepaymentCount = repayments.stream()
                    .filter(r -> r.getState() != LoanRepaymentState.DONE
                            && r.getState() != LoanRepaymentState.IN_PROGRESS)
                    .count();

            var appendixToNotClosedRepayment = penny / futureRepaymentCount;
            log.info("appendix {}", appendixToNotClosedRepayment);

            loan.setAmountDebt(loan.getAmountDebt()
                    .add(BigDecimal.valueOf(appendixToNotClosedRepayment * futureRepaymentCount))
            );
            loan.setAmountLoan(loan.getIssuedAmount().add(loan.getAmountDebt()));
            loan.setAccruedPenny(loan.getAccruedPenny().add(BigDecimal.valueOf(penny)));

            var repaymentsToRecalculateAmount = repayments.stream()
                    .filter(r -> r.getState() != LoanRepaymentState.DONE
                            && r.getState() != LoanRepaymentState.IN_PROGRESS)
                    .toList();

            for (var repayment : repaymentsToRecalculateAmount) {
                repayment.setAmount(repayment.getAmount().add(BigDecimal.valueOf(appendixToNotClosedRepayment)));
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

    private LoanRepaymentEntity updateRepayment(LoanRepaymentEntity repayment, UUID internalTransactionId) {
        var freshRepayment = repaymentRepository
                .findById(repayment.getId())
                .orElseThrow(() -> new InternalServiceException(
                                STR."LoanRepaymentEntity with id = \{repayment.getId()} not found"
                        )
                );

        freshRepayment.setDateOfLastTransaction(new Date());
        freshRepayment.setState(LoanRepaymentState.IN_PROGRESS);
        freshRepayment.getInternalTransactionIds().add(internalTransactionId);

        return repaymentRepository.save(freshRepayment);
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

        loan.setAmountDebt(loan.getAmountDebt().subtract(loanRepayment.getAmount()));
        loan.setAmountLoan(loan.getIssuedAmount().add(loan.getAmountDebt()));

        repaymentRepository.save(loanRepayment);
    }

    private void processFailedRepaymentCallback(LoanRepaymentEntity loanRepayment) {
        log.info("Loan repayment {} failed", loanRepayment.getId());
        loanRepayment.setState(LoanRepaymentState.REJECTED);
        loanRepayment.setDateOfLastTransaction(new Date());
        repaymentRepository.save(loanRepayment);
    }

    private LoanRepaymentEntity findLoanRepaymentByInternalTransactionId(UUID internalTransactionId) {
        return repaymentRepository
                .findOne(getLoanRepaymentByInternalTransactionIdSpecification(internalTransactionId))
                .orElseThrow(() -> new InternalServiceException(
                                STR."Repayment with internalTransactionId = \{internalTransactionId} not found"
                        )
                );
    }

    private Specification<LoanRepaymentEntity> getLoanRepaymentByInternalTransactionIdSpecification(UUID internalTransactionId) {
        return (root, _, criteriaBuilder) -> {
            Join<LoanRepaymentEntity, UUID> internalTransactionIdsJoin = root.join("internalTransactionIds");
            return criteriaBuilder.equal(internalTransactionIdsJoin, internalTransactionId);
        };
    }

}
