package ru.hits.trb.trbloans.client.core.dto;

import lombok.Data;
import ru.hits.trb.trbloans.entity.enumeration.Currency;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
public class AccountDto {

    private UUID id;

    private AccountType type;

    private UUID loanId;

    private BigDecimal balance;

    private Currency currency;

    private String clientFullName;

    private String externalClientId;

    private Date creationDate;

    private Date closingDate;

    private Boolean isClosed;

}