package ru.hits.trb.trbloans.dto.transaction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.hits.trb.trbloans.entity.enumeration.Currency;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class InitTransactionDto {

    private UUID payerAccountId;

    private UUID payeeAccountId;

    @NotNull
    @Min(1)
    private BigDecimal amount;

    @NotNull
    private Currency currency;

    @NotNull
    private TransactionType type;

}
