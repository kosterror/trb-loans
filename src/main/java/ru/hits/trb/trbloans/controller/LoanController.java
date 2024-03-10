package ru.hits.trb.trbloans.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.hits.trb.trbloans.dto.PageDto;
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
    public Page<ShortLoanDto> getLoans(@Valid @RequestBody PageDto pageDto) {

        return loanService.getLoans(pageDto);
    }

    @Operation(summary = "Получить детальную информацию по кредиту.")
    @GetMapping("/loan/{loanId}")
    public LoanDto getLoan(@Valid @PathVariable UUID loanId) {

        return loanService.getLoan(loanId);
    }

}
