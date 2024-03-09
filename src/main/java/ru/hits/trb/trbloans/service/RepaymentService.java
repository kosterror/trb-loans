package ru.hits.trb.trbloans.service;

import ru.hits.trb.trbloans.dto.transaction.TransactionState;

import java.util.UUID;

public interface RepaymentService {

    void processRepaymentCallback(UUID repaymentId, TransactionState state);

}
