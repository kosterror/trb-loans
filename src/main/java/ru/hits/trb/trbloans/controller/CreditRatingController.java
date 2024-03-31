package ru.hits.trb.trbloans.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.hits.trb.trbloans.dto.PaginationResponse;
import ru.hits.trb.trbloans.dto.creditrating.CreditRatingDto;
import ru.hits.trb.trbloans.service.CreditRatingService;

import java.util.UUID;

@Slf4j
@Tag(name = "API для кредитного рейтинга")
@RestController
@RequestMapping("/api/v1/credit-ratings")
@RequiredArgsConstructor
public class CreditRatingController {

    private final CreditRatingService service;

    @GetMapping
    @Operation(summary = "Получить кредитные рейтинги клиента")
    public PaginationResponse<CreditRatingDto> getCreditRatings(
            @RequestParam UUID clientId,
            @RequestParam @Min(0) int pageNumber,
            @RequestParam @Min(1) @Max(200) int pageSize
    ) {
        return service.getCreditRatings(clientId, pageNumber, pageSize);
    }

    @GetMapping("/last")
    @Operation(summary = "Получить последний вычисленный кредитный клиента",
            description = "Может прийти null, если кредитный рейтинг для пользователя никогда не вычислялся")
    public CreditRatingDto getLastCreditRating(@RequestParam UUID clientId) {
        return service.getLastCreditRating(clientId);
    }

    @PostMapping
    @Operation(summary = "Посчитать кредитный рейтинг для клиента")
    public CreditRatingDto calculateCreditRating(@RequestParam UUID clientId) {
        return service.calculateCreditRating(clientId);
    }

}
