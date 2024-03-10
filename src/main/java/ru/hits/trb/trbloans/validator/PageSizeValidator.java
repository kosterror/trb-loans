package ru.hits.trb.trbloans.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.hits.trb.trbloans.annotation.PageSizeValidation;

@RequiredArgsConstructor
public class PageSizeValidator implements ConstraintValidator<PageSizeValidation, Integer> {


    /**
     * Этот метод проверяет, является ли указанный номер страницы допустимым.
     *
     * @param pageSize  размер страницы для проверки
     * @param context    контекст, в котором выполняется проверка
     * @return true, если размер страницы больше нуля, в противном случае - false
     */
    @Override
    public boolean isValid(Integer pageSize, ConstraintValidatorContext context) {
        return pageSize > 0;
    }
}
