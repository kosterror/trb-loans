package ru.hits.trb.trbloans.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.hits.trb.trbloans.dto.loan.LoanDto;
import ru.hits.trb.trbloans.dto.loan.ShortLoanDto;
import ru.hits.trb.trbloans.entity.LoanEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LoanMapper {

    LoanDto entityToDto(LoanEntity entity);
    @Mapping(target = "interestRate", source = "tariff.interestRate")
    ShortLoanDto entityToShortDto(LoanEntity entity);

}
