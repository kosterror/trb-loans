package ru.hits.trb.trbloans.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hits.trb.trbloans.entity.enumeration.Currency;
import ru.hits.trb.trbloans.entity.enumeration.LoanRepaymentState;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "loan_repayment")
public class LoanRepaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Date date;

    private Date dateOfLastTransaction;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private LoanRepaymentState state;

    @ManyToOne
    private LoanEntity loan;

    @Column(name = "transaction_id")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "internal_transaction_ids", joinColumns = @JoinColumn(name = "loan_repayment_id"))
    private List<UUID> internalTransactionIds;

}
