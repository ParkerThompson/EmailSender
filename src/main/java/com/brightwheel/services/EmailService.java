package com.brightwheel.services;

import com.brightwheel.entities.Email;
import com.brightwheel.exceptions.InvalidParameterException;
import com.brightwheel.requests.SendEmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;


public abstract class EmailService {
    @Value(value = "${api.key}")
    private String apiKey;

    @Value(value = "${url}")
    protected String url;

    @Autowired
    protected RestTemplate restTemplate;

    /**
     * Sends an email using the configured email service
     * @param email The email to be sent
     * @return An http response
     */
    public ResponseEntity<String> send(Email email) {
        email.validate();
        HttpEntity<SendEmailRequest> entity = createSendEmailEntity(email);
        return this.restTemplate.postForEntity(url, entity, String.class);
    }

    public abstract ResponseEntity<String> getStatus(String id);

    public abstract HttpEntity<SendEmailRequest> createSendEmailEntity(Email email);

    /**
     * Returns http headers
     * @return http headers
     */
    @SuppressWarnings({"UastIncorrectHttpHeaderInspection"})
    protected HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("X-Api-Key", apiKey);
        return headers;
    }
}
