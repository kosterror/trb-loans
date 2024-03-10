package ru.hits.trb.trbloans.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hits.trb.trbloans.entity.LoanEntity;
import ru.hits.trb.trbloans.entity.enumeration.LoanState;

import java.util.List;
import java.util.UUID;

public interface LoanRepository extends JpaRepository<LoanEntity, UUID> {

    List<LoanEntity> findLoanEntitiesByClientId(UUID clientId);
    Page<LoanEntity> findAllLoanEntitiesByState(Pageable pageable, LoanState loanState);
}
