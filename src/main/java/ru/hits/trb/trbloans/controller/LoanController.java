package ru.hits.trb.trbloans.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.hits.trb.trbloans.dto.loan.LoanDto;
import ru.hits.trb.trbloans.dto.loan.ShortLoanDto;
import ru.hits.trb.trbloans.service.LoanService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @Operation(summary = "Получить кредиты клиента")
    @GetMapping("/client-loans")
    public List<ShortLoanDto> getClientLoans(@Valid @RequestParam UUID clientId) {
        return loanService.getClientLoans(clientId);
    }

    @Operation(summary = "Получить все незакрытые кредиты")
    @GetMapping("/loan")
    public Page<ShortLoanDto> getLoans(@RequestParam @Min(0) int pageNumber,
                                       @RequestParam @Min(1) @Max(200) int pageSize) {
        return loanService.getLoans(pageNumber, pageSize);
    }

    @Operation(summary = "Получить детальную информацию по кредиту.")
    @GetMapping("/loan/{loanId}")
    public LoanDto getLoan(@Valid @PathVariable UUID loanId) {
        return loanService.getLoan(loanId);
    }

}
