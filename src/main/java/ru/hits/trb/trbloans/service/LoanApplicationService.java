package ru.hits.trb.trbloans.service;

import ru.hits.trb.trbloans.dto.loan.LoanDto;
import ru.hits.trb.trbloans.dto.loanapplication.LoanApplicationDto;
import ru.hits.trb.trbloans.dto.loanapplication.NewLoanApplicationDto;

import java.util.UUID;

public interface LoanApplicationService {

    LoanApplicationDto createLoanApplication(NewLoanApplicationDto dto);

    LoanDto approveLoanApplication(UUID loanApplicationId);
    LoanApplicationDto rejectLoanApplication(UUID loanApplicationId);
}
