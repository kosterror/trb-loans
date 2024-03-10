package ru.hits.trb.trbloans.dto.loan;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
public class ShortLoanDto {

    private UUID id;

    private Date issuedDate;

    private Date repaymentDate;

    private long amountDebt;

    private BigDecimal interestRate;
}
