package com.brightwheel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EndpointNotAllowedException extends ResponseStatusException {

    /**
     * Constructor for EndpointNotAllowedException
     * @param reason human readable error string
     */
    public EndpointNotAllowedException(String reason) {
        super(HttpStatus.METHOD_NOT_ALLOWED, reason);
    }
}
