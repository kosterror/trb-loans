package ru.hits.trb.trbloans.client.core.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class AccountDto {

    private UUID id;

    private AccountType type;

    private long balance;

    private String clientFullName;

    private String externalClientId;

    private Date creationDate;

    private Date closingDate;

    private Boolean isClosed;

}