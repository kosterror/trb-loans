package ru.hits.trb.trbloans.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hits.trb.trbloans.entity.LoanEntity;

import java.util.UUID;

public interface LoanRepository extends JpaRepository<LoanEntity, UUID> {
}
