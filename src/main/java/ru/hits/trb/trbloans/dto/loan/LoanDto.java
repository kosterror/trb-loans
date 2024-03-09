package ru.hits.trb.trbloans.dto.loan;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.hits.trb.trbloans.dto.tariff.TariffDto;
import ru.hits.trb.trbloans.entity.enumeration.LoanState;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Data
public class LoanDto {

    private UUID id;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Schema(type = "number")
    private Date issuedDate;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Schema(type = "number")
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
