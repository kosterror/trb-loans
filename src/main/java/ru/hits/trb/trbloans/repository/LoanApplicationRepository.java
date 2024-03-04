package ru.hits.trb.trbloans.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hits.trb.trbloans.entity.LoanApplicationEntity;

import java.util.UUID;

public interface LoanApplicationRepository extends JpaRepository<LoanApplicationEntity, UUID> {
}
