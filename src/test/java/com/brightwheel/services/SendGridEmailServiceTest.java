package com.brightwheel.services;

import com.brightwheel.entities.Email;
import com.brightwheel.exceptions.EndpointNotAllowedException;
import com.brightwheel.exceptions.InvalidParameterException;
import com.brightwheel.requests.SendEmailRequest;
import com.brightwheel.requests.SendGridRequest;
import org.junit.Test;
import org.springframework.http.HttpEntity;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SendGridEmailServiceTest {
    SendGridEmailService sendGridEmailService = new SendGridEmailService();

    @Test
    public void testCreateEmailEntity() {
        Email email = new Email();
        email.setTo("test@gmail.com");
        email.setToName("Mr. Test");
        email.setFrom("firstname.lastname@gmail.com");
        email.setFromName("Mr. Lastname");
        email.setSubject("Test email please ignore");
        email.setBody("Please ignore");
        HttpEntity<SendEmailRequest> httpEntity = sendGridEmailService.createSendEmailEntity(email);
        assertThat(httpEntity.getHeaders().size()).isEqualTo(2);
        assertThat(httpEntity.getHeaders().get("Accept")).isEqualTo(Collections.singletonList("application/json"));
        assertThat(httpEntity.getHeaders().containsKey("X-Api-Key"));
        SendGridRequest sendGridRequest = (SendGridRequest) httpEntity.getBody();
        assertThat(sendGridRequest.getSender()).isEqualTo("Mr. Test <test@gmail.com>");
        assertThat(sendGridRequest.getRecipient()).isEqualTo("Mr. Lastname <firstname.lastname@gmail.com>");
        assertThat(sendGridRequest.getBody()).isEqualTo("Please ignore");
        assertThat(sendGridRequest.getSubject()).isEqualTo("Test email please ignore");
    }

    @Test
    public void testCreateNullEntity() {
        Throwable exception = assertThrows(InvalidParameterException.class, () ->
                sendGridEmailService.createSendEmailEntity(null));
        assertEquals("Email for entity cannot be null", exception.getMessage());
    }

    @Test
    public void testCreateEmailEntityWithNoFields() {
        HttpEntity<SendEmailRequest> httpEntity = sendGridEmailService.createSendEmailEntity(new Email());
        assertThat(httpEntity.getHeaders().size()).isEqualTo(2);
        assertThat(httpEntity.getHeaders().get("Accept")).isEqualTo(Collections.singletonList("application/json"));
        assertThat(httpEntity.getHeaders().containsKey("X-Api-Key"));
        SendGridRequest sendGridRequest = (SendGridRequest) httpEntity.getBody();
        assertThat(sendGridRequest.getSender()).isEqualTo("null <null>");
        assertThat(sendGridRequest.getRecipient()).isEqualTo("null <null>");
        assertThat(sendGridRequest.getBody()).isNull();
        assertThat(sendGridRequest.getSubject()).isNull();
    }

    @Test
    public void testGetStatus() {
        Throwable exception = assertThrows(EndpointNotAllowedException.class, () ->
                sendGridEmailService.getStatus(null));
        assertEquals("405 METHOD_NOT_ALLOWED \"Endpoint 'Status' is not supported for current email processor\"", exception.getMessage());
    }
}
