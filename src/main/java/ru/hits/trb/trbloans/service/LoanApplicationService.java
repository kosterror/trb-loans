package ru.hits.trb.trbloans.service;

import ru.hits.trb.trbloans.dto.loanapplication.LoanApplicationDto;
import ru.hits.trb.trbloans.dto.loanapplication.NewLoanApplicationDto;

public interface LoanApplicationService {

    LoanApplicationDto createLoanApplication(NewLoanApplicationDto dto);
}
