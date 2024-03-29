package ru.hits.trb.trbloans.client.core.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.hits.trb.trbloans.client.core.CoreClient;
import ru.hits.trb.trbloans.client.core.dto.AccountDto;
import ru.hits.trb.trbloans.client.core.dto.AccountType;
import ru.hits.trb.trbloans.client.core.dto.NewAccountDto;
import ru.hits.trb.trbloans.entity.enumeration.Currency;
import ru.hits.trb.trbloans.exception.InternalServiceException;

import java.util.UUID;

@Slf4j
@Service
public class CoreClientImpl implements CoreClient {

    private final RestClient restClient;

    public CoreClientImpl(@Qualifier("coreRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public AccountDto createLoanAccount(UUID clientId,
                                        String clientName,
                                        String clientFullName,
                                        String clientPatronymic,
                                        Currency currency
    ) {
        var body = NewAccountDto.builder()
                .type(AccountType.LOAN)
                .currency(currency)
                .clientFullName(STR."\{clientName} \{clientFullName} \{clientPatronymic}")
                .externalClientId(clientId)
                .build();

        log.info("Creating loan account...");

        var account = restClient.post()
                .uri(builder -> builder.path(Paths.CREATE_ACCOUNT)
                        .build()
                ).body(body)
                .retrieve()
                .toEntity(AccountDto.class)
                .getBody();

        if (account == null) {
            throw new InternalServiceException("trb-core returned null on creation account");
        }

        log.info("Loan account created with id {}", account.getId());

        return account;
    }

}
