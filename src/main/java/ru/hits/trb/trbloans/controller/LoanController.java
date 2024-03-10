package ru.hits.trb.trbloans.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hits.trb.trbloans.dto.loan.LoanDto;
import ru.hits.trb.trbloans.dto.loan.ShortLoanDto;
import ru.hits.trb.trbloans.service.LoanService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/loan")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @Operation(summary = "Получить кредиты клиента")
    @GetMapping
    public List<ShortLoanDto> getClientLoans(@Valid @RequestParam UUID clientId) {

        return loanService.getLoans(clientId);
    }

    @Operation(summary = "Получить детальную информацию по кредиту.")
    @GetMapping("/{loanId}")
    public LoanDto getLoan(@Valid @PathVariable UUID loanId) {

        return loanService.getLoan(loanId);
    }

}
