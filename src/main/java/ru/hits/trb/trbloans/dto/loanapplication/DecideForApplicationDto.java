package ru.hits.trb.trbloans.dto.loanapplication;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class DecideForApplicationDto {

    @NotNull
    private UUID officerId;

    @NotNull
    private UUID loanApplicationId;
}
