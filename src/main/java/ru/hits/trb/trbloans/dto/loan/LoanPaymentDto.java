package ru.hits.trb.trbloans.dto.loan;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import ru.hits.trb.trbloans.entity.enumeration.LoanPaymentState;

import java.util.Date;
import java.util.UUID;

@Data
public class LoanPaymentDto {

    private UUID id;

    private Date date;

    private long amount;

    @Enumerated(EnumType.ORDINAL)
    private LoanPaymentState state;

}
