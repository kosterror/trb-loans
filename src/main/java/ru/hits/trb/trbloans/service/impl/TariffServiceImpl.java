package ru.hits.trb.trbloans.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbloans.dto.tariff.NewTariffDto;
import ru.hits.trb.trbloans.dto.tariff.TariffDto;
import ru.hits.trb.trbloans.entity.TariffEntity;
import ru.hits.trb.trbloans.exception.NotFoundException;
import ru.hits.trb.trbloans.mapper.TariffMapper;
import ru.hits.trb.trbloans.repository.TariffRepository;
import ru.hits.trb.trbloans.service.TariffService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Override
    public TariffEntity findTariff(UUID id) {
        return tariffRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Tariff with id '" + id + "' not found"));
    }

    public List<TariffDto> getTariffs() {

        List<TariffEntity> tariffEntities = tariffRepository.findAll();

        List<TariffDto> tariffDtos = new ArrayList<>();

        for (TariffEntity tariff : tariffEntities) {
            tariffDtos.add(tariffMapper.entityToDto(tariff));
        }
        return tariffDtos;
    }

}
