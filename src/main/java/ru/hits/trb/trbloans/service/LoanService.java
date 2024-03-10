package ru.hits.trb.trbloans.service;

import ru.hits.trb.trbloans.dto.loan.LoanDto;
import ru.hits.trb.trbloans.dto.loan.ShortLoanDto;

import java.util.List;
import java.util.UUID;

public interface LoanService {

    List<ShortLoanDto> getLoans(UUID clientId);
    LoanDto getLoan(UUID loanId);
}
