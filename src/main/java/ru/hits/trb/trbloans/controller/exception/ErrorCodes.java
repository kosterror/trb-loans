package ru.hits.trb.trbloans.controller.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
class ErrorCodes {
    static final int INTERNAL_ERROR = 1;
    static final int VALIDATION_ERROR = 2;
    static final int BAD_REQUEST = 3;
    static final int NOT_FOUND = 4;
}
