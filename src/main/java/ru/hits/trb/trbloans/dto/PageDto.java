package ru.hits.trb.trbloans.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hits.trb.trbloans.annotation.PageNumberValidation;
import ru.hits.trb.trbloans.annotation.PageSizeValidation;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageDto {

    @PageNumberValidation
    private int pageNumber;

    @PageSizeValidation
    private int pageSize;
}
