package ru.hits.trb.trbloans.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hits.trb.trbloans.dto.tariff.NewTariffDto;
import ru.hits.trb.trbloans.dto.tariff.TariffDto;
import ru.hits.trb.trbloans.service.TariffService;

@RestController
@RequestMapping("/api/v1/tariff")
@RequiredArgsConstructor
public class TariffController {

    private final TariffService service;

    @PostMapping
    public TariffDto createTariff(@Valid @RequestBody NewTariffDto dto) {
        return service.createTariff(dto);
    }

}
