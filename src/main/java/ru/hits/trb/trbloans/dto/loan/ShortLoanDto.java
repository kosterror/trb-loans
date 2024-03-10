package ru.hits.trb.trbloans.dto.loan;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
public class ShortLoanDto {

    private UUID id;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Schema(type = "number")
    private Date issuedDate;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Schema(type = "number")
    private Date repaymentDate;

    private long amountDebt;

    private BigDecimal interestRate;
}
