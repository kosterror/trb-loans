package ru.hits.trb.trbloans.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.hits.trb.trbloans.entity.CreditRatingEntity;

import java.util.UUID;

public interface CreditRatingRepository extends JpaRepository<CreditRatingEntity, UUID>, JpaSpecificationExecutor<CreditRatingEntity> {
}
