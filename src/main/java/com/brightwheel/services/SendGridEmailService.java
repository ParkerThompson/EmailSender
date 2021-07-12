package com.brightwheel.services;

import com.brightwheel.entities.Email;
import com.brightwheel.exceptions.EndpointNotAllowedException;
import com.brightwheel.exceptions.InvalidParameterException;
import com.brightwheel.requests.SendEmailRequest;
import com.brightwheel.requests.SendGridRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SendGridEmailService extends EmailService {

    /**
     * Queries the status of a previously sent email
     * @param id the service provided identifier for the email
     * @return an HTTP response
     */
    @Override
    public ResponseEntity<String> getStatus(String id) {
        throw new EndpointNotAllowedException("Endpoint 'Status' is not supported for current email processor");
    }

    /**
     * Creates an http entity with headers for the given email
     * @param email The given email
     * @return an http entity with headers for the given email
     */
    @Override
    public HttpEntity<SendEmailRequest> createSendEmailEntity(Email email) {
        if(email == null) {
            throw new InvalidParameterException("Email for entity cannot be null");
        }
        return new HttpEntity<>(new SendGridRequest(email), getHeaders());
    }

}
