package ru.hits.trb.trbloans.dto.tariff;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class NewTariffDto {

    @NotBlank
    @Schema(requiredMode = REQUIRED, description = "Название кредитной программы")
    private String name;

    @NotBlank
    @Length(max = 2048)
    @Schema(requiredMode = REQUIRED, description = "Описание кредитной программы")
    private String description;

    @DecimalMin(value = "0.01")
    @DecimalMax(value = "999.99")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal interestRate;

    @NotNull
    private UUID officerId;

}
