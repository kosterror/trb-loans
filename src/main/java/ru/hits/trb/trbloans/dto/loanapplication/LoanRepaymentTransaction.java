package ru.hits.trb.trbloans.dto.loanapplication;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LoanRepaymentTransaction {

    private UUID accountId;

    private long amount;

}
