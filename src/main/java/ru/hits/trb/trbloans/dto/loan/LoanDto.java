package ru.hits.trb.trbloans.dto.loan;

import lombok.Data;
import ru.hits.trb.trbloans.dto.tariff.TariffDto;
import ru.hits.trb.trbloans.entity.enumeration.LoanState;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Data
public class LoanDto {

    private UUID id;

    private Date issuedDate;

    private Date repaymentDate;

    private long issuedAmount;

    private long amountLoan;

    private long amountDebt;

    private long accruedPenny;

    private int loanTermInDays;

    private UUID clientId;

    private UUID accountId;

    private LoanState state;

    private TariffDto tariff;

    private List<LoanRepaymentDto> repayments;

}
