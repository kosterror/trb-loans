package ru.hits.trb.trbloans.sheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbloans.service.RepaymentService;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanPennyScheduler {

    private final RepaymentService repaymentService;

    @Scheduled(cron = "${scheduler.loan-repayment-calculate-penny}")
    public void calculatePenny() {
        log.info("Calculating penny...");
        repaymentService.calculatePenny();
        log.info("Calculating penny is finished");
    }

}
