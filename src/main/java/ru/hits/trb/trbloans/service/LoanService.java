package ru.hits.trb.trbloans.service;

import org.springframework.data.domain.Page;
import ru.hits.trb.trbloans.dto.loan.LoanDto;
import ru.hits.trb.trbloans.dto.loan.ShortLoanDto;
import ru.hits.trb.trbloans.entity.LoanEntity;

import java.util.List;
import java.util.UUID;

public interface LoanService {

    List<ShortLoanDto> getClientLoans(UUID clientId);

    LoanDto getLoan(UUID loanId);

    LoanEntity getLoanEntity(UUID loanId);

    Page<ShortLoanDto> getLoans(int pageNumber, int pageSize);

    void updateLoanStates();

}
