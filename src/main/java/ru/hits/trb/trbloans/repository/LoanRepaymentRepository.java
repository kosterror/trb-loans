package ru.hits.trb.trbloans.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.hits.trb.trbloans.entity.LoanRepaymentEntity;
import ru.hits.trb.trbloans.entity.enumeration.LoanRepaymentState;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface LoanRepaymentRepository extends JpaRepository<LoanRepaymentEntity, UUID>,
        JpaSpecificationExecutor<LoanRepaymentEntity> {

    List<LoanRepaymentEntity> findAllByStateInAndDateLessThanEqual(List<LoanRepaymentState> states, Date toDate);

    List<LoanRepaymentEntity> findAllByDateOfLastTransactionIsBeforeAndState(Date toDate, LoanRepaymentState state);

    List<LoanRepaymentEntity> findAllByDateBeforeAndStateIn(Date toDate, List<LoanRepaymentState> states);

}
