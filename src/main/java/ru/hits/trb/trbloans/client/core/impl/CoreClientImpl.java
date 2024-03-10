package ru.hits.trb.trbloans.client.core.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.hits.trb.trbloans.client.core.CoreClient;
import ru.hits.trb.trbloans.client.core.dto.AccountDto;
import ru.hits.trb.trbloans.client.core.dto.AccountType;
import ru.hits.trb.trbloans.client.core.dto.NewAccountDto;

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
                                        String clientPatronymic
    ) {
        var body = NewAccountDto.builder()
                .type(AccountType.LOAN)
                .externalClientId(clientId)
                .clientFullName(clientName + StringUtils.SPACE + clientFullName + StringUtils.SPACE + clientPatronymic)
                .build();

        log.info("Creating loan account...");

        var account = restClient.post()
                .uri(builder -> builder.path(Paths.CREATE_ACCOUNT)
                        .build()
                ).body(body)
                .retrieve()
                .toEntity(AccountDto.class)
                .getBody();

        log.info("Loan account created with id {}", account.getId());

        return account;
    }

}
