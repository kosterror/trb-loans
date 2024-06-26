package ru.hits.trb.trbloans.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hits.trb.trbloans.dto.loanapplication.LoanApplicationDecisionDto;
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

    @Operation(summary = "Создать заявку на кредит")
    @PostMapping
    public LoanApplicationDto createLoanApplication(@Valid @RequestBody NewLoanApplicationDto dto) {
        return service.createLoanApplication(dto);
    }

    @Operation(summary = "Одобрить заявку на кредит")
    @PostMapping("/approve")
    public LoanApplicationDto approveLoanApplication(
            @Valid @RequestBody LoanApplicationDecisionDto loanApplicationDecisionDto
    ) {
        return service.approveLoanApplication(loanApplicationDecisionDto);
    }

    @Operation(summary = "Отклонить заявку на кредит")
    @PostMapping("/reject")
    public LoanApplicationDto rejectLoanApplication(
            @Valid @RequestBody LoanApplicationDecisionDto loanApplicationDecisionDto
    ) {
        return service.rejectLoanApplication(loanApplicationDecisionDto);
    }

    @Operation(summary = "Получить заявки клиента на кредит")
    @GetMapping("/by-client")
    public List<LoanApplicationDto> getClientLoanApplications(@RequestParam UUID clientId,
                                                              @RequestParam LoanApplicationState loanApplicationState
    ) {
        return service.getClientLoanApplications(clientId, loanApplicationState);
    }

    @Operation(summary = "Получить все заявки на кредит")
    @GetMapping
    public List<LoanApplicationDto> getLoanApplications(@RequestParam LoanApplicationState loanApplicationState) {
        return service.getLoanApplications(loanApplicationState);
    }

    @Operation(summary = "Получить информацию по заявке")
    @GetMapping("{loanApplicationId}")
    public LoanApplicationDto getLoanApplications(@PathVariable UUID loanApplicationId) {
        return service.getLoanApplication(loanApplicationId);
    }

}
