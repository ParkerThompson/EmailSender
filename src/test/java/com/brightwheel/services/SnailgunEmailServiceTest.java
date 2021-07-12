package com.brightwheel.services;

import com.brightwheel.entities.Email;
import com.brightwheel.exceptions.EndpointNotAllowedException;
import com.brightwheel.exceptions.InvalidParameterException;
import com.brightwheel.requests.SendEmailRequest;
import com.brightwheel.requests.SendGridRequest;
import com.brightwheel.requests.SnailgunRequest;
import org.junit.Test;
import org.springframework.http.HttpEntity;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SnailgunEmailServiceTest {
    SnailgunEmailService emailService = new SnailgunEmailService();

    @Test
    public void testCreateEmailEntity() {
        Email email = new Email();
        email.setTo("test@gmail.com");
        email.setToName("Mr. Test");
        email.setFrom("firstname.lastname@gmail.com");
        email.setFromName("Mr. Lastname");
        email.setSubject("Test email please ignore");
        email.setBody("Please ignore");
        HttpEntity<SendEmailRequest> httpEntity = emailService.createSendEmailEntity(email);
        assertThat(httpEntity.getHeaders().size()).isEqualTo(2);
        assertThat(httpEntity.getHeaders().get("Accept")).isEqualTo(Collections.singletonList("application/json"));
        assertThat(httpEntity.getHeaders().containsKey("X-Api-Key"));
        SnailgunRequest requestBody = (SnailgunRequest) httpEntity.getBody();
        assertThat(requestBody.getToEmail()).isEqualTo("test@gmail.com");
        assertThat(requestBody.getToName()).isEqualTo("Mr. Test");
        assertThat(requestBody.getFromEmail()).isEqualTo("firstname.lastname@gmail.com");
        assertThat(requestBody.getFromName()).isEqualTo("Mr. Lastname");
        assertThat(requestBody.getBody()).isEqualTo("Please ignore");
        assertThat(requestBody.getSubject()).isEqualTo("Test email please ignore");
    }

    @Test
    public void testCreateNullEntity() {
        Throwable exception = assertThrows(InvalidParameterException.class, () ->
                emailService.createSendEmailEntity(null));
        assertEquals("Email for entity cannot be null", exception.getMessage());
    }

    @Test
    public void testCreateEmailEntityWithNoFields() {
        HttpEntity<SendEmailRequest> httpEntity = emailService.createSendEmailEntity(new Email());
        assertThat(httpEntity.getHeaders().size()).isEqualTo(2);
        assertThat(httpEntity.getHeaders().get("Accept")).isEqualTo(Collections.singletonList("application/json"));
        SnailgunRequest requestBody = (SnailgunRequest) httpEntity.getBody();
        assertThat(httpEntity.getHeaders().containsKey("X-Api-Key"));
        assertThat(requestBody.getToName()).isNull();
        assertThat(requestBody.getToName()).isNull();
        assertThat(requestBody.getFromName()).isNull();
        assertThat(requestBody.getFromEmail()).isNull();
        assertThat(requestBody.getBody()).isNull();
        assertThat(requestBody.getSubject()).isNull();
    }

    @Test
    public void testGetStatus() {
        Throwable exception = assertThrows(InvalidParameterException.class, () ->
                emailService.getStatus(null));
        assertEquals("Id cannot be null", exception.getMessage());
    }
}
