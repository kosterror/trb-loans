package ru.hits.trb.trbloans.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.hits.trb.trbloans.entity.CreditRatingEntity;
import ru.hits.trb.trbloans.entity.CreditRatingEntity_;

import java.util.UUID;

@UtilityClass
public class CreditRatingSpecification {

    public static Specification<CreditRatingEntity> byClientId(UUID clientId) {
        return (root, _, builder) ->
                builder.equal(root.get(CreditRatingEntity_.CLIENT_ID), clientId);
    }

    public static Specification<CreditRatingEntity> orderByCalculatedDate(
            Specification<CreditRatingEntity> specification
    ) {
        return (root, query, builder) -> {
            query.orderBy(builder.desc(root.get(CreditRatingEntity_.CALCULATION_DATE)));
            return specification.toPredicate(root, query, builder);
        };
    }

}
