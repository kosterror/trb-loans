package ru.hits.trb.trbloans.service;

import ru.hits.trb.trbloans.dto.loanapplication.DecideForApplicationDto;
import ru.hits.trb.trbloans.dto.loanapplication.LoanApplicationDto;
import ru.hits.trb.trbloans.dto.loanapplication.NewLoanApplicationDto;
import ru.hits.trb.trbloans.entity.enumeration.LoanApplicationState;

import java.util.List;
import java.util.UUID;

public interface LoanApplicationService {

    LoanApplicationDto createLoanApplication(NewLoanApplicationDto dto);

    LoanApplicationDto approveLoanApplication(DecideForApplicationDto decideForApplicationDto);

    LoanApplicationDto rejectLoanApplication(DecideForApplicationDto decideForApplicationDto);

    List<LoanApplicationDto> getClientLoanApplications(UUID clientId, LoanApplicationState loanApplicationState);

    List<LoanApplicationDto> getLoanApplications(LoanApplicationState loanApplicationState);
    LoanApplicationDto getLoanApplication(UUID loanApplicationId);
}
