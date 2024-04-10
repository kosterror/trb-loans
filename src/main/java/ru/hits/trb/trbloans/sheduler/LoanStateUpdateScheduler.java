package ru.hits.trb.trbloans.sheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbloans.service.LoanService;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanStateUpdateScheduler {

    private final LoanService loanService;

    @Scheduled(cron = "${scheduler.loan-state-update}")
    public void updateLoanStates() {
        log.info("Updating loan states started...");
        loanService.updateLoanStates();
        log.info("Updating loan states finished");
    }

}
