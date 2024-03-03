package ru.hits.trb.trbloans.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tariff")
public class TariffEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Date additionDate;

    private String name;

    private String description;

    private BigDecimal interestRate;

    @OneToMany(mappedBy = "tariff")
    private List<LoanEntity> issuedLoans;

    @OneToMany(mappedBy = "tariff")
    private List<LoanApplicationEntity> applications;

}
