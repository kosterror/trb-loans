package ru.hits.trb.trbloans.dto.loanapplication;

import lombok.Data;

import java.util.UUID;

@Data
public class DecideForApplicationDto {

    private UUID officerId;

    private UUID loanApplicationId;
}
