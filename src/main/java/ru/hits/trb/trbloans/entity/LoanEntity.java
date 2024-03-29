package ru.hits.trb.trbloans.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hits.trb.trbloans.entity.enumeration.Currency;
import ru.hits.trb.trbloans.entity.enumeration.LoanState;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "loan")
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Date issuedDate;

    private Date repaymentDate;

    private BigDecimal issuedAmount;

    private BigDecimal amountLoan;

    private BigDecimal amountDebt;

    private BigDecimal accruedPenny;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private int loanTermInDays;

    private UUID clientId;

    private UUID accountId;

    @Enumerated(EnumType.STRING)
    private LoanState state;

    @ManyToOne
    private TariffEntity tariff;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.PERSIST)
    private List<LoanRepaymentEntity> repayments;

    @OneToOne
    @JoinColumn(name = "loan_application_id", referencedColumnName = "id")
    private LoanApplicationEntity loanApplication;
}
