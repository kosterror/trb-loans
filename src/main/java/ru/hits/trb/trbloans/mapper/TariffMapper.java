package ru.hits.trb.trbloans.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.hits.trb.trbloans.dto.tariff.NewTariffDto;
import ru.hits.trb.trbloans.dto.tariff.TariffDto;
import ru.hits.trb.trbloans.entity.TariffEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TariffMapper {

    @Mapping(target = "additionDate", expression = "java(new java.util.Date())")
    TariffEntity newDtoToEntity(NewTariffDto dto);

    TariffDto entityToDto(TariffEntity tariff);
}
