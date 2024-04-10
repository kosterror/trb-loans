package ru.hits.trb.trbloans.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.trb.trbloans.dto.transaction.TransactionState;
import ru.hits.trb.trbloans.entity.LoanEntity;
import ru.hits.trb.trbloans.entity.LoanRepaymentEntity;
import ru.hits.trb.trbloans.entity.enumeration.LoanRepaymentState;
import ru.hits.trb.trbloans.entity.enumeration.LoanState;
import ru.hits.trb.trbloans.repository.LoanRepaymentRepository;
import ru.hits.trb.trbloans.repository.LoanRepository;
import ru.hits.trb.trbloans.service.LoanPaymentService;
import ru.hits.trb.trbloans.service.LoanService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanPaymentServiceImpl implements LoanPaymentService {

    private final LoanService loanService;
    private final LoanRepository loanRepository;
    private final LoanRepaymentRepository loanRepaymentRepository;

    @Override
    @Transactional
    public void processPaymentCallback(UUID loanId, TransactionState transactionState) {
        var loanEntity = loanService.getLoanEntity(loanId);

        if (transactionState == TransactionState.DONE) {
            log.info("Processing success payment for loan {}...", loanId);
            processSuccessPayment(loanEntity);
        } else {
            log.info("Processing failed payment for loan {}...", loanId);
            processFailedPayment(loanEntity);
        }

        log.info("Processing of payment transaction finished for loan {}", loanId);

    }

    private void processSuccessPayment(LoanEntity loanEntity) {
        var repayments = buildRepayments(loanEntity);
        loanEntity.setRepayments(repayments);

        loanRepository.save(loanEntity);
        loanRepaymentRepository.saveAll(repayments);
    }

    private void processFailedPayment(LoanEntity loanEntity) {
        log.info("Change loan state from {} to {}", loanEntity.getState(), LoanState.FAILED);
        loanEntity.setState(LoanState.FAILED);
        loanRepository.save(loanEntity);
    }

    private List<LoanRepaymentEntity> buildRepayments(LoanEntity loan) {
        var loanRepayments = new ArrayList<LoanRepaymentEntity>();
        var repaymentAmount = loan.getAmountLoan()
                .divide(BigDecimal.valueOf(loan.getLoanTermInDays()), RoundingMode.DOWN);

        var calendar = Calendar.getInstance();
        calendar.setTime(loan.getIssuedDate());

        for (int i = 0; i < loan.getLoanTermInDays(); i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            var repaymentDate = calendar.getTime();
            var loanRepayment = LoanRepaymentEntity.builder()
                    .date(repaymentDate)
                    .amount(repaymentAmount)
                    .currency(loan.getCurrency())
                    .state(LoanRepaymentState.OPEN)
                    .loan(loan)
                    .build();

            loanRepayments.add(loanRepayment);
        }

        return loanRepayments;
    }

}
