package ru.hits.trb.trbloans.mapper.impl;


import ru.hits.trb.trbloans.dto.loan.LoanDto;
import ru.hits.trb.trbloans.entity.LoanApplicationEntity;
import ru.hits.trb.trbloans.entity.LoanEntity;
import ru.hits.trb.trbloans.entity.enumeration.LoanState;

import java.util.UUID;

public class LoanMapper {

    public LoanDto mapEntityToDto(LoanEntity entity) {
        return null;
    }

    LoanEntity mapLoanEntity(LoanApplicationEntity loanApplication, long amountLoan, UUID accountId) {

        return LoanEntity
                .builder()
                .amountLoan(amountLoan)
                .amountDebt(amountLoan)
                .accruedPenny(0)
                .state(LoanState.OPEN)
                .accountId(accountId)
                .loanTermInDays(loanApplication.getLoanTermInDays())
                .clientId(loanApplication.getClientId())
                .issuedAmount(loanApplication.getIssuedAmount())
                .tariff(loanApplication.getTariff())
                .issuedDate(loanApplication.getCreationDate())
                .build();
    }

}
