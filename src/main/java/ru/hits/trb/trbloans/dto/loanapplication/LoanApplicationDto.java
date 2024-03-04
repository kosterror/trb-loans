package ru.hits.trb.trbloans.dto.loanapplication;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.hits.trb.trbloans.dto.tariff.TariffDto;
import ru.hits.trb.trbloans.entity.enumeration.LoanApplicationState;

import java.util.Date;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class LoanApplicationDto {

    @Schema(description = "Идентификатор заявки", requiredMode = REQUIRED)
    private UUID id;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Schema(description = "Дата создания заявки",
            requiredMode = REQUIRED,
            type = "string",
            example = "1709372199882")
    private Date creationDate;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Schema(description = "Дата, когда рассмотрели заявку",
            requiredMode = NOT_REQUIRED,
            type = "string",
            example = "1709372199882")
    private Date updatedDateFinal;

    @Schema(description = "Длительность кредита в днях", requiredMode = REQUIRED)
    private int loanTermInDays;

    @Schema(description = "Запрошенная сумма кредита в копейках", requiredMode = REQUIRED)
    private long issuedAmount;

    @Schema(description = "Идентификатор клиента", requiredMode = REQUIRED)
    private UUID clientId;

    @Schema(description = "Сотрудник, рассмотревший заявку", requiredMode = NOT_REQUIRED)
    private UUID officerId;

    @Schema(description = "Состояние заявки", requiredMode = REQUIRED)
    private LoanApplicationState state;

    @Schema(description = "Кредитная программа, по которой создана заявка", requiredMode = REQUIRED)
    private TariffDto tariff;

}
