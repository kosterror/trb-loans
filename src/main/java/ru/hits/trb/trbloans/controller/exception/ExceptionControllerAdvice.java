package ru.hits.trb.trbloans.controller.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.hits.trb.trbloans.dto.ErrorResponse;
import ru.hits.trb.trbloans.exception.BadRequestException;
import ru.hits.trb.trbloans.exception.ConflictException;
import ru.hits.trb.trbloans.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request,
                                                         Exception exception
    ) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildResponse(ErrorCodes.INTERNAL_ERROR, "Internal service error"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(HttpServletRequest request,
                                                                   MethodArgumentNotValidException exception) {
        logException(request, exception);

        var errors = new HashMap<String, List<String>>();

        exception.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();

                    if (errors.containsKey(fieldName)) {
                        var messages = errors.get(fieldName);
                        messages.add(errorMessage);
                    } else {
                        var messages = new ArrayList<String>();
                        messages.add(errorMessage);
                        errors.put(fieldName, messages);
                    }
                });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .code(ErrorCodes.VALIDATION_ERROR)
                        .message("Validation error")
                        .requestValidationMessages(errors)
                        .build()
                );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(NoResourceFoundException exception)
            throws NoResourceFoundException {
        throw exception;
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(HttpServletRequest request,
                                                                   BadRequestException exception) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildResponse(ErrorCodes.BAD_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(HttpServletRequest request,
                                                                 NotFoundException exception) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildResponse(ErrorCodes.NOT_FOUND, exception.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(HttpServletRequest request,
                                                                 ConflictException exception) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildResponse(ErrorCodes.INVALID_ACTION, exception.getMessage()));
    }

    private void logException(HttpServletRequest request, Exception exception) {
        log.error("Error during: {} {}", request.getMethod(), request.getRequestURI(), exception);
    }

    private ErrorResponse buildResponse(int code, String message) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .build();
    }


}
