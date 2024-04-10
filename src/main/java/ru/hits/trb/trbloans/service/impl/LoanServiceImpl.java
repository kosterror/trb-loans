package ru.hits.trb.trbloans.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.trb.trbloans.dto.loan.LoanDto;
import ru.hits.trb.trbloans.dto.loan.ShortLoanDto;
import ru.hits.trb.trbloans.entity.LoanEntity;
import ru.hits.trb.trbloans.entity.enumeration.LoanState;
import ru.hits.trb.trbloans.exception.NotFoundException;
import ru.hits.trb.trbloans.mapper.LoanMapper;
import ru.hits.trb.trbloans.repository.LoanRepository;
import ru.hits.trb.trbloans.service.LoanService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;

    @Override
    public List<ShortLoanDto> getClientLoans(UUID clientId) {
        return loanRepository.findLoanEntitiesByClientId(clientId)
                .stream()
                .map(loanMapper::entityToShortDto)
                .toList();
    }

    @Override
    public Page<ShortLoanDto> getLoans(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return loanRepository.findAllLoanEntitiesByState(pageable, LoanState.OPEN)
                .map(loanMapper::entityToShortDto);
    }

    @Override
    public LoanDto getLoan(UUID loanId) {
        LoanEntity loanEntity = loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException(STR."Loan with id '\{loanId}' not found"));

        return loanMapper.entityToDto(loanEntity);
    }

    @Override
    public LoanEntity getLoanEntity(UUID loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException(STR."Loan with id '\{loanId}' not found"));
    }

    @Override
    @Transactional
    public void updateLoanStates() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, -1);
        var issuedDate = calendar.getTime();

        var loans = loanRepository.findLoanEntityByStateAndIssuedDateLessThanEqual(LoanState.PENDING, issuedDate);

        loans.forEach(loan -> {
            log.info("Updating state of loan {} from {} to {}", loan.getId(), loan.getState(), LoanState.FAILED);
            loan.setState(LoanState.FAILED);
        });

        loanRepository.saveAll(loans);
    }

}