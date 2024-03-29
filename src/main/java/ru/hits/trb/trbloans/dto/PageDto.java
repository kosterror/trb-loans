package ru.hits.trb.trbloans.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PageDto {

    @Min(value = 0)
    private int pageNumber;

    @Min(value = 1)
    @Min(value = 200)
    private int pageSize;
}
