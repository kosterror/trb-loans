package ru.hits.trb.trbloans.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbloans.dto.loan.LoanDto;
import ru.hits.trb.trbloans.mapper.LoanMapper;
import ru.hits.trb.trbloans.repository.LoanRepository;
import ru.hits.trb.trbloans.service.LoanService;

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
        return loanRepository.findLoanEntitiesByClientId(clientId)
                .stream()
                .map(loanMapper::entityToDto)
                .toList();
    }

}