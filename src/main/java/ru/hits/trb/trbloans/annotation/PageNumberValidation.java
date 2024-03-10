package ru.hits.trb.trbloans.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.hits.trb.trbloans.validator.PageNumberValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Валидирующая аннотация. Она нужна для проверки на данных для пагинации.
 */
@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PageNumberValidator.class)
public @interface PageNumberValidation {
    String message() default "Некорректный номер страницы";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
