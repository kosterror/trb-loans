package ru.hits.trb.trbloans.dto.tariff;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class TariffDto {

    @Schema(description = "Идентификатор тарифа", requiredMode = REQUIRED)
    private UUID id;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Schema(description = "Дата создания тарифа",
            requiredMode = REQUIRED,
            example = "1709372199882",
            type = "string")
    private Date additionDate;

    @Schema(description = "Название тарифа", requiredMode = REQUIRED)
    private String name;

    @Schema(description = "Описание тарифа", requiredMode = REQUIRED)
    private String description;

    @Schema(description = "Кредитная ставка", requiredMode = REQUIRED)
    private BigDecimal interestRate;

    @Schema(description = "Идентификатор сотрудника, который добавил тариф", requiredMode = REQUIRED)
    private UUID officerId;

}
