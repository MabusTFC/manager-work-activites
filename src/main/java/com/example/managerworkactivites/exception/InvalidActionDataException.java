package com.example.managerworkactivites.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidActionDataException extends RuntimeException {

    public InvalidActionDataException(String message) {
        super(message);
    }

    public InvalidActionDataException(String message, Throwable cause) {
        super(message, cause);
    }
}