package ru.hits.trb.trbloans.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hits.trb.trbloans.entity.enumeration.LoanState;

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

    private long issuedAmount;

    private long amountLoan;

    private long amountDebt;

    private long accruedPenny;

    private int loanTermInDays;

    @Enumerated(EnumType.ORDINAL)
    private LoanState state;

    @ManyToOne
    private TariffEntity tariff;

    @OneToMany(mappedBy = "loan")
    private List<LoanPayment> payments;

    @OneToOne
    @JoinColumn(name = "loan_application_id", referencedColumnName = "id")
    private LoanApplicationEntity loanApplication;
}
