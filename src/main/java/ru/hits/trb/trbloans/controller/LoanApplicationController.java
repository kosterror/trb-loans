package ru.hits.trb.trbloans.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hits.trb.trbloans.dto.loan.LoanDto;
import ru.hits.trb.trbloans.dto.loanapplication.LoanApplicationDto;
import ru.hits.trb.trbloans.dto.loanapplication.NewLoanApplicationDto;
import ru.hits.trb.trbloans.entity.enumeration.LoanApplicationState;
import ru.hits.trb.trbloans.service.LoanApplicationService;

import java.util.List;
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

    @PostMapping("{id}/approve")
    public LoanDto approveLoanApplication(@PathVariable UUID id) {
        return service.approveLoanApplication(id);
    }

    @PostMapping("{id}/reject")
    public LoanApplicationDto rejectLoanApplication(@PathVariable UUID id) {
        return service.rejectLoanApplication(id);
    }

    @GetMapping("/by-client")
    public List<LoanApplicationDto> getClientLoanApplications(@RequestParam UUID clientId, @RequestParam LoanApplicationState loanApplicationState){
        return service.getClientLoanApplications(clientId, loanApplicationState);
    }

   @GetMapping
    public List<LoanApplicationDto> getLoanApplications(@RequestParam LoanApplicationState loanApplicationState){
        return service.getLoanApplications(loanApplicationState);
    }

}
