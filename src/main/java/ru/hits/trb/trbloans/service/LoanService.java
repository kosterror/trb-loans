package ru.hits.trb.trbloans.service;

import ru.hits.trb.trbloans.dto.loan.LoanDto;

import java.util.List;
import java.util.UUID;

public interface LoanService {

    List<LoanDto> getLoans(UUID clientId);
}
