package ru.hits.trb.trbloans.service;

import ru.hits.trb.trbloans.dto.PaginationResponse;
import ru.hits.trb.trbloans.dto.creditrating.CreditRatingDto;

import java.util.UUID;

public interface CreditRatingService {

    PaginationResponse<CreditRatingDto> getCreditRatings(UUID clientId, int pageNumber, int pageSize);

    CreditRatingDto getLastCreditRating(UUID clientId);

    CreditRatingDto calculateCreditRating(UUID clientId);

}
