package ru.hits.trb.trbloans.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.hits.trb.trbloans.entity.LoanEntity;
import ru.hits.trb.trbloans.entity.enumeration.LoanState;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface LoanRepository extends JpaRepository<LoanEntity, UUID>, JpaSpecificationExecutor<LoanEntity> {

    List<LoanEntity> findLoanEntitiesByClientId(UUID clientId);

    Page<LoanEntity> findAllLoanEntitiesByState(Pageable pageable, LoanState loanState);

    long countByClientIdAndState(UUID clientId, LoanState state);

    List<LoanEntity> findLoanEntityByStateAndIssuedDateLessThanEqual(LoanState state, Date issuedDate);
}
