package ru.hits.trb.trbloans.mapper;


import ru.hits.trb.trbloans.dto.loan.LoanDto;
import ru.hits.trb.trbloans.entity.LoanApplicationEntity;
import ru.hits.trb.trbloans.entity.LoanEntity;

public class LoanMapper {

    LoanDto mapEntityToDto(LoanEntity entity){
        return  null;
    }
     LoanEntity mapLoanEntity(LoanApplicationEntity loanApplication){
         return LoanEntity
                 .builder()
                 .amountLoan(calculateAmountLoan())
                 .loanTermInDays(loanApplication.getLoanTermInDays())
                 .clientId(loanApplication.getClientId())
                 .issuedAmount(loanApplication.getIssuedAmount())
                 .tariff(loanApplication.getTariff())
                 .issuedDate(loanApplication.getCreationDate())
                 .build();
    }

    private long calculateAmountLoan(){
        return 0;
    }

}
