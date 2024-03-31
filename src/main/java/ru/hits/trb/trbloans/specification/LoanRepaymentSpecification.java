package ru.hits.trb.trbloans.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.hits.trb.trbloans.entity.LoanEntity;
import ru.hits.trb.trbloans.entity.LoanEntity_;
import ru.hits.trb.trbloans.entity.LoanRepaymentEntity;
import ru.hits.trb.trbloans.entity.LoanRepaymentEntity_;
import ru.hits.trb.trbloans.entity.enumeration.LoanRepaymentState;

import java.util.UUID;

@UtilityClass
public class LoanRepaymentSpecification {

    public static Specification<LoanRepaymentEntity> expiredRepayments(UUID clientId) {
        return (root, _, criteriaBuilder) -> {
            Join<LoanRepaymentEntity, LoanEntity> loanJoin = root.join(LoanRepaymentEntity_.LOAN, JoinType.LEFT);

            Predicate clientIdPredicate =
                    criteriaBuilder.equal(loanJoin.get(LoanEntity_.CLIENT_ID), clientId);
            Predicate statePredicate =
                    criteriaBuilder.equal(root.get(LoanRepaymentEntity_.STATE), LoanRepaymentState.REJECTED);

            return criteriaBuilder.and(clientIdPredicate, statePredicate);
        };
    }

}
