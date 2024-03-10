package ru.hits.trb.trbloans.client.users.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ClientDto {

    private UUID id;

    private String firstName;

    private String lastName;

    private String patronymic;

}
