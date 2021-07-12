package com.brightwheel.services;

import com.brightwheel.entities.Email;
import com.brightwheel.exceptions.InvalidParameterException;
import com.brightwheel.requests.SendEmailRequest;
import com.brightwheel.requests.SnailgunRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SnailgunEmailService extends EmailService {

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
        return new HttpEntity<>(new SnailgunRequest(email), getHeaders());
    }

    /**
     * Queries the status of a previously sent email
     * @param id the service provided identifier for the email
     * @return An http response
     */
    public ResponseEntity<String> getStatus(String id) {
        if(id == null) {
            throw new InvalidParameterException("Id cannot be null");
        }
        return restTemplate.exchange(
                url + "/" + id,
                HttpMethod.GET,
                new HttpEntity<SendEmailRequest>(getHeaders()),
                String.class
        );
    }
}
