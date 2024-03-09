package ru.hits.trb.trbloans.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hits.trb.trbloans.entity.LoanRepaymentEntity;

import java.util.UUID;

public interface LoanRepaymentRepository extends JpaRepository<LoanRepaymentEntity, UUID> {

}
