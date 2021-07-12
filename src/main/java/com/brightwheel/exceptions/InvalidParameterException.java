package com.brightwheel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidParameterException extends RuntimeException {

    /**
     * Constructor for InvalidParameterException
     * @param message human readable error string
     */
    public InvalidParameterException(String message) {
        super(message);
    }
}