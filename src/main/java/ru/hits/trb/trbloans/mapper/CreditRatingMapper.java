package ru.hits.trb.trbloans.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.hits.trb.trbloans.dto.creditrating.CreditRatingDto;
import ru.hits.trb.trbloans.entity.CreditRatingEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CreditRatingMapper {

    CreditRatingDto entityToDto(CreditRatingEntity entity);

}
