package ru.hits.trb.trbloans.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Builder
public class ErrorResponse {

    @Schema(requiredMode = REQUIRED, description = "Задекларированный код ошибки")
    private int code;

    @Schema(requiredMode = REQUIRED, description = "Текст ошибки")
    private String message;

    @Schema(requiredMode = NOT_REQUIRED,
            description = "Информация о нарушении валидации, " +
                    "ключ - параметр тела запроса, значение - описание нарушения")
    private Map<String, List<String>> requestValidationMessages;

}
