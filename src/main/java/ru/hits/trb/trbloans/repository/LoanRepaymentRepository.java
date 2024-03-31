package ru.hits.trb.trbloans.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.hits.trb.trbloans.entity.LoanRepaymentEntity;
import ru.hits.trb.trbloans.entity.enumeration.LoanRepaymentState;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface LoanRepaymentRepository extends JpaRepository<LoanRepaymentEntity, UUID>,
        JpaSpecificationExecutor<LoanRepaymentEntity> {

    List<LoanRepaymentEntity> findAllByStateInAndDateLessThanEqual(List<LoanRepaymentState> states, Date toDate);

    List<LoanRepaymentEntity> findAllByDateOfLastTransactionIsBeforeAndState(Date toDate, LoanRepaymentState state);

    @Query(value = """
            select count (t.id) from (
                select lr.id AS id
                from LoanRepaymentEntity lr
                left join lr.loan l
                left join lr.internalTransactionIds iti
                where l.clientId = :clientId
                and lr.state = :loanRepaymentState
                and lr.date > :dateFrom
                group by lr.id
                having count(lr.id) > 1
            ) as t
            """)
    long countExpiredRepayments(UUID clientId, LoanRepaymentState loanRepaymentState, Date dateFrom);

}
