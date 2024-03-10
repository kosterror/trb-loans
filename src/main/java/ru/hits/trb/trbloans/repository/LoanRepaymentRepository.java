package ru.hits.trb.trbloans.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hits.trb.trbloans.entity.LoanRepaymentEntity;
import ru.hits.trb.trbloans.entity.enumeration.LoanRepaymentState;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface LoanRepaymentRepository extends JpaRepository<LoanRepaymentEntity, UUID> {

    @Query(value = """
                    select *
                    from loan_repayment t
                    where t.date <= ?1
                      and t.state in (0, 3)
            """,
            nativeQuery = true)
    List<LoanRepaymentEntity> findOpenAndRejectedRepaymentsToDate(Date toDate);

    List<LoanRepaymentEntity> findAllByDateOfLastTransactionIsBeforeAndState(Date toDate, LoanRepaymentState state);

    List<LoanRepaymentEntity> findAllByDateBeforeAndStateIn(Date toDate, List<LoanRepaymentState> states);

}
