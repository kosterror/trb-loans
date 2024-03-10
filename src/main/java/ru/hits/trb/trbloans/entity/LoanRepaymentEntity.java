package ru.hits.trb.trbloans.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hits.trb.trbloans.entity.enumeration.LoanRepaymentState;

import java.util.Date;
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

    private long amount;

    @Enumerated(EnumType.ORDINAL)
    private LoanRepaymentState state;

    @ManyToOne
    private LoanEntity loan;

}
