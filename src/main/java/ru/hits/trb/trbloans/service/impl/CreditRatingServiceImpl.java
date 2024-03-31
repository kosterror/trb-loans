package ru.hits.trb.trbloans.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbloans.dto.PaginationResponse;
import ru.hits.trb.trbloans.dto.creditrating.CreditRatingDto;
import ru.hits.trb.trbloans.entity.CreditRatingEntity;
import ru.hits.trb.trbloans.entity.enumeration.LoanRepaymentState;
import ru.hits.trb.trbloans.entity.enumeration.LoanState;
import ru.hits.trb.trbloans.mapper.CreditRatingMapper;
import ru.hits.trb.trbloans.repository.CreditRatingRepository;
import ru.hits.trb.trbloans.repository.LoanRepaymentRepository;
import ru.hits.trb.trbloans.repository.LoanRepository;
import ru.hits.trb.trbloans.service.CreditRatingService;
import ru.hits.trb.trbloans.specification.CreditRatingSpecification;
import ru.hits.trb.trbloans.specification.LoanRepaymentSpecification;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditRatingServiceImpl implements CreditRatingService {

    private final CreditRatingRepository creditRatingRepository;
    private final CreditRatingMapper creditRatingMapper;
    private final LoanRepository loanRepository;
    private final LoanRepaymentRepository loanRepaymentRepository;

    @Override
    public PaginationResponse<CreditRatingDto> getCreditRatings(UUID clientId, int pageNumber, int pageSize) {
        var specification = CreditRatingSpecification.byClientId(clientId);
        specification = CreditRatingSpecification.orderByCalculatedDate(specification);

        var creditRatingPage = creditRatingRepository.findAll(specification, PageRequest.of(pageNumber, pageSize));

        return PaginationResponse.<CreditRatingDto>builder()
                .pageNumber(creditRatingPage.getNumber())
                .pageSize(creditRatingPage.getSize())
                .elements(creditRatingPage
                        .getContent()
                        .stream()
                        .map(creditRatingMapper::entityToDto)
                        .toList()
                ).build();
    }

    @Override
    public CreditRatingDto getLastCreditRating(UUID clientId) {
        var specification = CreditRatingSpecification.byClientId(clientId);
        specification = CreditRatingSpecification.orderByCalculatedDate(specification);

        var creditRatingPage = creditRatingRepository.findAll(specification, PageRequest.of(0, 1));
        var creditRatingsList = creditRatingPage.getContent();

        if (creditRatingsList.isEmpty()) {
            log.warn("Credit rating for client {} is null", clientId);
            return null;
        }

        return creditRatingMapper.entityToDto(creditRatingsList.getFirst());
    }

    @Override
    public CreditRatingDto calculateCreditRating(UUID clientId) {
        int rating = getRating(clientId);

        var creditRating = CreditRatingEntity.builder()
                .clientId(clientId)
                .rating(rating)
                .calculationDate(new Date())
                .build();

        creditRating = creditRatingRepository.save(creditRating);
        return creditRatingMapper.entityToDto(creditRating);
    }

    private int getRating(UUID clientId) {
        var openedLoans = loanRepository.countByClientIdAndState(clientId, LoanState.OPEN);
        var closedLoans = loanRepository.countByClientIdAndState(clientId, LoanState.CLOSED);

        var specificationExpiredRepayments = LoanRepaymentSpecification.expiredRepayments(clientId);
        var expiredRepayments = loanRepaymentRepository.count(specificationExpiredRepayments);

        var calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        var yearAgo = calendar.getTime();
        var beforeExpiredRepayments = loanRepaymentRepository.countExpiredRepayments(clientId,
                LoanRepaymentState.DONE,
                yearAgo
        );

        return (int) ((500
                + closedLoans * 100
                - openedLoans * 250
                - expiredRepayments * 500
                - beforeExpiredRepayments * 50) % 1000);
    }
}
