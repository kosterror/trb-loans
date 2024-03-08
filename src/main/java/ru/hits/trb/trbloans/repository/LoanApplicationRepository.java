package ru.hits.trb.trbloans.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hits.trb.trbloans.entity.LoanApplicationEntity;
import ru.hits.trb.trbloans.entity.enumeration.LoanApplicationState;

import java.util.List;
import java.util.UUID;

public interface LoanApplicationRepository extends JpaRepository<LoanApplicationEntity, UUID> {

   List<LoanApplicationEntity> getAllByClientIdAndState(UUID clientId, LoanApplicationState loanApplicationState);
   List<LoanApplicationEntity> getAllByState(LoanApplicationState loanApplicationState);
}
