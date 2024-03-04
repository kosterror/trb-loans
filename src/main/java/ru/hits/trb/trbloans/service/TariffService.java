package ru.hits.trb.trbloans.service;

import ru.hits.trb.trbloans.dto.tariff.NewTariffDto;
import ru.hits.trb.trbloans.dto.tariff.TariffDto;
import ru.hits.trb.trbloans.entity.TariffEntity;

import java.util.UUID;

public interface TariffService {

    TariffDto createTariff(NewTariffDto dto);

    TariffEntity findTariff(UUID id);
}
