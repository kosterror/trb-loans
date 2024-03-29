package ru.hits.trb.trbloans.client.core;

import ru.hits.trb.trbloans.client.core.dto.AccountDto;
import ru.hits.trb.trbloans.entity.enumeration.Currency;

import java.util.UUID;

public interface CoreClient {

    AccountDto createLoanAccount(UUID clientId,
                                 String clientName,
                                 String clientFullName,
                                 String clientPatronymic,
                                 Currency currency
    );

}
