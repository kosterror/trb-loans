package ru.hits.trb.trbloans.dto.loanapplication;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class NewLoanApplicationDto {

    @NotNull
    @Schema(description = "Идентификатор клиента", requiredMode = REQUIRED)
    private UUID clientId;

    @NotNull
    @Schema(description = "Идентификатор кредитной программы", requiredMode = REQUIRED)
    private UUID tariffId;

    @Min(1)
    @Max(365)
    @Schema(description = "Длительность кредита в днях", requiredMode = REQUIRED)
    private int loanTermInDays;

    @Min(10000)
    @Max(100000000000L)
    @Schema(description = "Запрашиваемая сумма для кредита в копейках", requiredMode = REQUIRED)
    private long issuedAmount;

}
