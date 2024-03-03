package ru.hits.trb.trbloans.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hits.trb.trbloans.entity.enumeration.LoanPaymentState;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "loan_payment")
public class LoanPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Date date;

    private long amount;

    @Enumerated(EnumType.ORDINAL)
    private LoanPaymentState state;

    @ManyToOne
    private LoanEntity loan;

}
