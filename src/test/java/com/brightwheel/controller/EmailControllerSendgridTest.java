package com.brightwheel.controller;

import com.brightwheel.application.Application;
import com.brightwheel.entities.Email;
import com.brightwheel.exceptions.EndpointNotAllowedException;
import com.brightwheel.exceptions.InvalidParameterException;
import com.brightwheel.responses.SendGridResponse;
import com.brightwheel.services.EmailService;
import com.brightwheel.services.SendGridEmailService;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@ActiveProfiles("test-sendgrid")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(
        locations = "classpath:test-sendgrid.properties")
public class EmailControllerSendgridTest {

    @Autowired
    private EmailController emailController;

    private Gson gson = new Gson();

    private List<String> validTestEmails = Arrays.asList("email@example.com",
            "firstname.lastname@example.com", "email@subdomain.example.com",
            "firstname+lastname@example.com", "email@[123.123.123.123]",
            "1234567890@example.com", "email@example-one.com",
            "_______@example.com", "email@example.name",
            "email@example.museum", "email@example.co.jp",
            "firstname-lastname@example.com", "much.”more\\ unusual”@example.com");

    private String testHtml = "<!DOCTYPE html><html<body><h1>My First Heading</h1><p>My first paragraph.</p>" +
            "This line has<script type=\"text/javascript\">function doSomeBadStuffThatShouldGetParsedOut(){}" +
            "</script>a break<br>in the middle</body></html>";

    @Test
    public void sendEmailTest() {
        validTestEmails.forEach(emailAddress -> {
            Email email = new Email();
            email.setTo(emailAddress);
            email.setToName("Mr. Test");
            email.setFrom("firstname.lastname@gmail.com");
            email.setFromName("Mr. Lastname");
            email.setSubject( "Test email please ignore");
            email.setBody(testHtml);
            ResponseEntity<String> responseEntity = emailController.send(email);
            SendGridResponse sendGridResponse = gson.fromJson(responseEntity.getBody(), SendGridResponse.class);
            assertThat(sendGridResponse.getId()).isNotNull();
            assertThat(sendGridResponse.getCreatedAt()).isNotNull();
            assertThat(sendGridResponse.getSender()).isEqualTo(String.format("Mr. Test <%s>", emailAddress));
            assertThat(sendGridResponse.getRecipient()).isEqualTo("Mr. Lastname <firstname.lastname@gmail.com>");
            assertThat(sendGridResponse.getSubject()).isEqualTo("Test email please ignore");
            assertThat(sendGridResponse.getBody()).isEqualTo("My First Heading My first paragraph. This line has a break in the middle");
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        });

    }

    @Test
    public void sendEmailBadToTest() {
        Email email = new Email();
        email.setTo("Abc..123@example.com");
        email.setToName("Mr. Test");
        email.setFrom("firstname.lastname@gmail.com");
        email.setFromName("Mr. Lastname");
        email.setSubject( "Test email please ignore");
        email.setBody(testHtml);
        Throwable exception = assertThrows(InvalidParameterException.class, () -> emailController.send(email));
        assertEquals("To 'Abc..123@example.com' is not a valid email", exception.getMessage());
    }

    @Test
    public void sendEmailBadToNameTest() {
        Email email = new Email();
        email.setTo("test@example.com");
        email.setToName(null);
        email.setFrom("firstname.lastname@gmail.com");
        email.setFromName("Mr. Lastname");
        email.setSubject( "Test email please ignore");
        email.setBody(testHtml);
        Throwable exception = assertThrows(InvalidParameterException.class, () -> emailController.send(email));
        assertEquals("toName cannot be null", exception.getMessage());
    }

    @Test
    public void sendEmailBadFromTest() {
        Email email = new Email();
        email.setTo("test@example.com");
        email.setToName("Mr. Example");
        email.setFrom("this\\ is\"really\"not\\allowed@example.com");
        email.setFromName("Mr. Lastname");
        email.setSubject( "Test email please ignore");
        email.setBody(testHtml);
        Throwable exception = assertThrows(InvalidParameterException.class, () -> emailController.send(email));
        assertEquals("From 'this\\ is\"really\"not\\allowed@example.com' is not a valid email", exception.getMessage());
    }

    @Test
    public void sendEmailBadFromNameTest() {
        Email email = new Email();
        email.setTo("test@example.com");
        email.setToName("Mr. Example");
        email.setFrom("firstname.lastname@gmail.com");
        email.setFromName(null);
        email.setSubject( "Test email please ignore");
        email.setBody(testHtml);
        Throwable exception = assertThrows(InvalidParameterException.class, () -> emailController.send(email));
        assertEquals("fromName cannot be null", exception.getMessage());
    }

    @Test
    public void sendEmailBadSubjectTest() {
        Email email = new Email();
        email.setTo("test@example.com");
        email.setToName("Mr. Example");
        email.setFrom("firstname.lastname@gmail.com");
        email.setFromName("Mr. Lastname");
        email.setSubject(null);
        email.setBody(testHtml);
        Throwable exception = assertThrows(InvalidParameterException.class, () -> emailController.send(email));
        assertEquals("subject cannot be null", exception.getMessage());
    }

    @Test
    public void sendEmailBadBodyTest() {
        Email email = new Email();
        email.setTo("test@example.com");
        email.setToName("Mr. Example");
        email.setFrom("firstname.lastname@gmail.com");
        email.setFromName("Mr. Lastname");
        email.setSubject( "Test email please ignore");
        Throwable exception = assertThrows(InvalidParameterException.class, () -> {
            email.setBody(null);
            email.validate();

        });
        assertEquals("body cannot be null", exception.getMessage());
    }

    @Test
    public void sendNullEmailTest() {
        Throwable exception = assertThrows(InvalidParameterException.class, () -> {
            emailController.send(null);
        });
        assertEquals("Request body cannot be null", exception.getMessage());
    }

    @Test
    public void testGetStatus() {
        Throwable exception = assertThrows(EndpointNotAllowedException.class, () -> {
            emailController.status("1234");
        });
        assertEquals("405 METHOD_NOT_ALLOWED \"Endpoint 'Status' is not supported for current email processor\"", exception.getMessage());
    }

    @Profile("test-sendgrid")
    @Configuration
    private static class testConfiguration {

        public testConfiguration() {
        }

        @Bean
        public EmailController emailController() {
            return new EmailController();
        }
        @Bean
        public EmailService emailService() {
            return new SendGridEmailService();
        }
    }

}
