package ru.hits.trb.trbloans.service;

import ru.hits.trb.trbloans.dto.tariff.NewTariffDto;
import ru.hits.trb.trbloans.dto.tariff.TariffDto;

public interface TariffService {

    TariffDto createTariff(NewTariffDto dto);

}
