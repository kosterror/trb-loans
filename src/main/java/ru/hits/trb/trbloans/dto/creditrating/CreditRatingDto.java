package ru.hits.trb.trbloans.dto.creditrating;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class CreditRatingDto {

    @Schema(description = "Идентификатор кредитного рейтинга", requiredMode = REQUIRED)
    private UUID id;

    @Schema(description = "Идентификатор клиента", requiredMode = REQUIRED)
    private UUID clientId;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Schema(description = "Дата, когда вычислили этот кредитный рейтинг",
            requiredMode = REQUIRED,
            type = "number")
    private Date calculationDate;

    @Schema(description = "Значение кредитного рейтинга, от 0 до 999.",
            requiredMode = REQUIRED)
    private int rating;

}
