package ru.hits.trb.trbloans.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hits.trb.trbloans.entity.TariffEntity;

import java.util.UUID;

public interface TariffRepository extends JpaRepository<TariffEntity, UUID> {
}
