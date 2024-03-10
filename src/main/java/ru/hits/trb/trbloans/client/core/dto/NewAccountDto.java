package ru.hits.trb.trbloans.client.core.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class NewAccountDto {

    private AccountType type;

    private String clientFullName;

    private UUID externalClientId;

}
