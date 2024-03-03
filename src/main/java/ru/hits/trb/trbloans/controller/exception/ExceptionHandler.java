package ru.hits.trb.trbloans.controller.exception;

import ru.hits.trb.trbloans.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request,
                                                         Exception exception
    ) {
        logException(request, exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildResponse(ErrorCodes.INTERNAL_ERROR, "Internal service error"));
    }

    private void logException(HttpServletRequest request, Exception exception) {
        log.error("Error during: {} {}", request.getMethod(), request.getRequestURI(), exception);
    }

    private ErrorResponse buildResponse(int code, String message) {
        return ErrorResponse.Builder()
                .code(code)
                .message(message)
                .build();
    }


}
