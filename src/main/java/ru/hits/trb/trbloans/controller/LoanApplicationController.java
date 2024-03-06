package ru.hits.trb.trbloans.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hits.trb.trbloans.dto.loan.LoanDto;
import ru.hits.trb.trbloans.dto.loanapplication.LoanApplicationDto;
import ru.hits.trb.trbloans.dto.loanapplication.NewLoanApplicationDto;
import ru.hits.trb.trbloans.service.LoanApplicationService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/loan-applications")
@RequiredArgsConstructor
public class LoanApplicationController {

    private final LoanApplicationService service;

    @PostMapping
    public LoanApplicationDto createLoanApplication(@Valid @RequestBody NewLoanApplicationDto dto) {
        return service.createLoanApplication(dto);
    }

    @PostMapping
    public LoanDto approveLoanApplication(@RequestParam UUID loanApplicationId) {
        return service.approveLoanApplication(loanApplicationId);
    }

    @PostMapping
    public LoanApplicationDto rejectLoanApplication(@RequestParam UUID loanApplicationId) {
        return service.rejectLoanApplication(loanApplicationId);
    }
}
