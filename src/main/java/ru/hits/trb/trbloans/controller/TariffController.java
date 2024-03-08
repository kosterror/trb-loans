package ru.hits.trb.trbloans.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hits.trb.trbloans.dto.tariff.NewTariffDto;
import ru.hits.trb.trbloans.dto.tariff.TariffDto;
import ru.hits.trb.trbloans.service.TariffService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tariff")
@RequiredArgsConstructor
public class TariffController {

    private final TariffService service;

    @Operation(summary = "Создать кредитный тариф")
    @PostMapping
    public TariffDto createTariff(@Valid @RequestBody NewTariffDto dto) {
        return service.createTariff(dto);
    }

    @Operation(summary = "Получить кредитные тарифы")
    @GetMapping
    public  List<TariffDto> getTariffs(){
        return service.getTariffs();
    }

}
