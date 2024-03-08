package ru.hits.trb.trbloans.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hits.trb.trbloans.entity.LoanPayment;

import java.util.UUID;

public interface LoanPaymentRepository extends JpaRepository<LoanPayment, UUID> {

}
