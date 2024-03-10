package ru.hits.trb.trbloans.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hits.trb.trbloans.dto.loan.LoanDto;
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
    public List<LoanDto> getClientLoans(@Valid @RequestParam UUID clientId) {

        return loanService.getLoans(clientId);
    }


}
