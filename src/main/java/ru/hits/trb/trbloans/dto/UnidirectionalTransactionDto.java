package ru.hits.trb.trbloans.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UnidirectionalTransactionDto {

    private UUID accountId;

    private long amount;

}
