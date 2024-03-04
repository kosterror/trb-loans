package ru.hits.trb.trbloans.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbloans.dto.tariff.NewTariffDto;
import ru.hits.trb.trbloans.dto.tariff.TariffDto;
import ru.hits.trb.trbloans.mapper.TariffMapper;
import ru.hits.trb.trbloans.repository.TariffRepository;
import ru.hits.trb.trbloans.service.TariffService;

@Slf4j
@Service
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {

    private final TariffMapper tariffMapper;
    private final TariffRepository tariffRepository;

    @Override
    public TariffDto createTariff(NewTariffDto dto) {
        var tariff = tariffMapper.newDtoToEntity(dto);
        tariff = tariffRepository.save(tariff);
        log.info("Tariff created with id {}", tariff.getId());
        return tariffMapper.entityToDto(tariff);
    }

}
