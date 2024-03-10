package ru.hits.trb.trbloans.client.users;

import ru.hits.trb.trbloans.client.users.dto.ClientDto;

import java.util.UUID;

public interface UsersClient {

    ClientDto getClient(UUID clientId);

}
