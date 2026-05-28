package com.example.managerworkactivites.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class IntervalOverlapException extends RuntimeException {

    public IntervalOverlapException(String message) {
        super(message);
    }

    public IntervalOverlapException(String message, Throwable cause) {
        super(message, cause);
    }
}
