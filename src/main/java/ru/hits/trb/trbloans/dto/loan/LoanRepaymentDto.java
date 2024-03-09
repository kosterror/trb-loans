package ru.hits.trb.trbloans.dto.loan;


import lombok.Data;
import ru.hits.trb.trbloans.entity.enumeration.LoanRepaymentState;

import java.util.Date;
import java.util.UUID;

@Data
public class LoanRepaymentDto {

    private UUID id;

    private Date date;

    private long amount;

    private LoanRepaymentState state;

}
