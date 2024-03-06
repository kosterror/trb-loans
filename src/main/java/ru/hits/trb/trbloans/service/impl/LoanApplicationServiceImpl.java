package ru.hits.trb.trbloans.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.trb.trbloans.dto.loan.LoanDto;
import ru.hits.trb.trbloans.dto.loanapplication.LoanApplicationDto;
import ru.hits.trb.trbloans.dto.loanapplication.NewLoanApplicationDto;
import ru.hits.trb.trbloans.entity.LoanApplicationEntity;
import ru.hits.trb.trbloans.exception.NotFoundException;
import ru.hits.trb.trbloans.mapper.LoanApplicationMapper;
import ru.hits.trb.trbloans.repository.LoanApplicationRepository;
import ru.hits.trb.trbloans.repository.LoanRepository;
import ru.hits.trb.trbloans.service.LoanApplicationService;
import ru.hits.trb.trbloans.service.TariffService;

import java.util.UUID;

import static ru.hits.trb.trbloans.entity.enumeration.LoanApplicationState.APPROVED;
import static ru.hits.trb.trbloans.entity.enumeration.LoanApplicationState.REJECTED;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanRepository loanRepository;
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

    @Override
    @Transactional
    public LoanDto approveLoanApplication(UUID loanApplicationId) {

        LoanApplicationEntity loanApplication = findLoanApplication(loanApplicationId);
        loanApplication.setState(APPROVED);
        //Todo создать счет
        //Todo создать кредит
        //Todo создать платежи по кредиту

        return null;
    }

    @Override
    @Transactional
    public LoanApplicationDto rejectLoanApplication(UUID loanApplicationId) {

        LoanApplicationEntity loanApplication = findLoanApplication(loanApplicationId);
        loanApplication.setState(REJECTED);
       // loanApplication.setUpdatedDateFinal(LocalDateTime.now());

        return loanApplicationMapper.mapEntityToDto(loanApplicationRepository.save(loanApplication));
    }

    private LoanApplicationEntity findLoanApplication(UUID id){
        return loanApplicationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("LoanApplication with id '" + id + "' not found"));
    }

//    private createLoan(LoanApplicationEntity loanApplication){
//
//
//    }



}
