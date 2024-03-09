package ru.hits.trb.trbloans.dto.loan;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.hits.trb.trbloans.entity.enumeration.LoanRepaymentState;

import java.util.Date;
import java.util.UUID;

@Data
public class LoanRepaymentDto {

    private UUID id;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Schema(type = "number")
    private Date date;

    private long amount;

    private LoanRepaymentState state;

}
