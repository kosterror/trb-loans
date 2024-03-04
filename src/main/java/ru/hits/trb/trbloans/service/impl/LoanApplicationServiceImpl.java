package ru.hits.trb.trbloans.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.trb.trbloans.dto.loanapplication.LoanApplicationDto;
import ru.hits.trb.trbloans.dto.loanapplication.NewLoanApplicationDto;
import ru.hits.trb.trbloans.mapper.LoanApplicationMapper;
import ru.hits.trb.trbloans.repository.LoanApplicationRepository;
import ru.hits.trb.trbloans.service.LoanApplicationService;
import ru.hits.trb.trbloans.service.TariffService;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanApplicationMapper loanApplicationMapper;
    private final TariffService tariffService;

    @Override
    @Transactional
    public LoanApplicationDto createLoanApplication(NewLoanApplicationDto dto) {
        var tariff = tariffService.findTariff(dto.getTariffId());
        var loanApplication = loanApplicationMapper.newDtoToEntity(dto);
        loanApplication.setTariff(tariff);

        return loanApplicationMapper.mapEntityToDto(loanApplicationRepository.save(loanApplication));
    }

}
