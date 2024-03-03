package ru.hits.trb.trbloans.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hits.trb.trbloans.entity.enumeration.LoanApplicationState;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "loan_application")
public class LoanApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Date creationDate;

    private Date updatedDateFinal;

    private int loanTermInDays;

    private long issuedAmount;

    private UUID clientId;

    private UUID officerId;

    @Enumerated(EnumType.ORDINAL)
    private LoanApplicationState state;

    @ManyToOne
    private TariffEntity tariff;

    @OneToOne(mappedBy = "loanApplication")
    private LoanEntity loan;

}
