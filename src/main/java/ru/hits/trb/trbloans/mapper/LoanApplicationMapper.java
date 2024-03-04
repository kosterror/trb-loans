package ru.hits.trb.trbloans.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.hits.trb.trbloans.dto.loanapplication.LoanApplicationDto;
import ru.hits.trb.trbloans.dto.loanapplication.NewLoanApplicationDto;
import ru.hits.trb.trbloans.entity.LoanApplicationEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LoanApplicationMapper {

    @Mapping(target = "creationDate", expression = "java(new java.util.Date())")
    @Mapping(
            target = "state",
            expression = "java(ru.hits.trb.trbloans.entity.enumeration.LoanApplicationState.UNDER_CONSIDERATION)"
    )
    LoanApplicationEntity newDtoToEntity(NewLoanApplicationDto newDto);

    LoanApplicationDto mapEntityToDto(LoanApplicationEntity entity);
}
