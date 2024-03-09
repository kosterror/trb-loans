package ru.hits.trb.trbloans.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.trb.trbloans.dto.transaction.TransactionState;
import ru.hits.trb.trbloans.entity.LoanRepaymentEntity;
import ru.hits.trb.trbloans.entity.enumeration.LoanRepaymentState;
import ru.hits.trb.trbloans.entity.enumeration.LoanState;
import ru.hits.trb.trbloans.exception.ConflictException;
import ru.hits.trb.trbloans.exception.NotFoundException;
import ru.hits.trb.trbloans.repository.LoanRepaymentRepository;
import ru.hits.trb.trbloans.service.RepaymentService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepaymentServiceImpl implements RepaymentService {

    private final LoanRepaymentRepository loanRepaymentRepository;

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

    private void processSuccessRepaymentCallback(LoanRepaymentEntity loanRepayment) {
        loanRepayment.setState(LoanRepaymentState.DONE);

        var loan = loanRepayment.getLoan();
        var repayments = loan.getRepayments();

        var openRepaymentsCount = repayments.stream()
                .filter(repayment -> repayment.getState() != LoanRepaymentState.DONE)
                .count();

        if (openRepaymentsCount == 0) {
            loan.setState(LoanState.CLOSED);
            log.info("Closing the loan {}", loan.getId());
        }

        loanRepaymentRepository.save(loanRepayment);
    }

    private void processFailedRepaymentCallback(LoanRepaymentEntity loanRepayment) {
        log.info("Loan repayment {} failed", loanRepayment.getId());
        loanRepayment.setState(LoanRepaymentState.REJECTED);
        loanRepaymentRepository.save(loanRepayment);
    }

    private LoanRepaymentEntity getLoanRepayment(UUID repaymentId) {
        var loanRepayment = loanRepaymentRepository.findById(repaymentId)
                .orElseThrow(() -> new NotFoundException("Loan repayment with id '" + repaymentId + "' not found"));

        if (loanRepayment.getState() == LoanRepaymentState.DONE) {
            throw new ConflictException("Repayment with id '" + repaymentId + "' is done");
        }

        return loanRepayment;
    }

}
