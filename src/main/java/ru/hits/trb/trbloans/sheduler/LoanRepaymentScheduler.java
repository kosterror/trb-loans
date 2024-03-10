package ru.hits.trb.trbloans.sheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbloans.service.RepaymentService;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanRepaymentScheduler {

    private final RepaymentService repaymentService;

    @Scheduled(cron = "${scheduler.loan-repayment-send-transaction}")
    public void sendRepaymentTransactions() {
        log.info("Send repayment transaction scheduler started...");
        repaymentService.sendRepaymentTransactions();
        log.info("Send repayment transaction scheduler finished");
    }

    @Scheduled(cron = "${scheduler.loan-repayment-update-status}")
    public void updateRepaymentsStatus() {
        log.info("Loan repayment update status scheduler started...");
        repaymentService.updateRepaymentsStatus();
        log.info("Loan repayment update status scheduler finished");
    }

}
