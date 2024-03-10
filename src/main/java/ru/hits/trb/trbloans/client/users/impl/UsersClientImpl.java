package ru.hits.trb.trbloans.client.users.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.hits.trb.trbloans.client.users.UsersClient;
import ru.hits.trb.trbloans.client.users.dto.ClientDto;
import ru.hits.trb.trbloans.exception.InternalServiceException;
import ru.hits.trb.trbloans.exception.NotFoundException;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class UsersClientImpl implements UsersClient, RestClient.ResponseSpec.ErrorHandler {

    private final RestClient restClient;

    public UsersClientImpl(@Qualifier("usersRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public ClientDto getClient(UUID clientId) {
        var responseEntityClientDto = restClient.get()
                .uri(builder -> builder.path(Paths.GET_CLIENT_INFO)
                        .queryParam(Params.CLIENT_ID, clientId)
                        .build()
                ).retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this)
                .toEntity(ClientDto.class);

        return responseEntityClientDto.getBody();
    }

    @Override
    public void handle(HttpRequest request, ClientHttpResponse response) throws IOException {
        var statusCode = response.getStatusCode();

        if (statusCode.value() == 404) {
            throw new NotFoundException("Client not found");
        }

        log.error("Failed to get client info. Status code: {}, response: {}",
                response.getStatusCode(),
                response.getStatusText()
        );

        throw new InternalServiceException("Failed to get client info");
    }
}
