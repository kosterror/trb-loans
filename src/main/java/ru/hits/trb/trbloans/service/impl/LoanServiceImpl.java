package ru.hits.trb.trbloans.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbloans.dto.loan.LoanDto;
import ru.hits.trb.trbloans.entity.LoanEntity;
import ru.hits.trb.trbloans.mapper.LoanMapper;
import ru.hits.trb.trbloans.repository.LoanRepository;
import ru.hits.trb.trbloans.service.LoanService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;

    private final LoanMapper loanMapper;

    @Override
    public List<LoanDto> getLoans(UUID clientId) {

        List<LoanEntity> loanEntityList = loanRepository.findLoanEntitiesByClientId(clientId);

        List<LoanDto> loanDtos = new ArrayList<>();

        for (LoanEntity loan : loanEntityList) {
            loanDtos.add(loanMapper.mapEntityToDto(loan));
        }

        return loanDtos;
    }

}
