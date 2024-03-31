package ru.hits.trb.trbloans.client.core.dto;

import lombok.Builder;
import lombok.Data;
import ru.hits.trb.trbloans.entity.enumeration.Currency;

import java.util.UUID;

@Data
@Builder
public class NewAccountDto {

    private AccountType type;

    private Currency currency;

    private String clientFullName;

    private UUID externalClientId;

}
