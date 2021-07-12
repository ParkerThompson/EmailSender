package com.brightwheel.controller;

import com.brightwheel.application.Application;
import com.brightwheel.entities.Email;
import com.brightwheel.exceptions.InvalidParameterException;
import com.brightwheel.responses.SnailgunResponse;
import com.brightwheel.services.EmailService;
import com.brightwheel.services.SnailgunEmailService;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@ActiveProfiles("test-snailgun")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(
        locations = "classpath:test-snailgun.properties")
public class EmailControllerSnailgunTest {

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
            SnailgunResponse snailgunSendEmailResponse = gson.fromJson(responseEntity.getBody(), SnailgunResponse.class);
            assertThat(snailgunSendEmailResponse.getId()).isNotNull();
            assertThat(snailgunSendEmailResponse.getToEmail()).isEqualTo(emailAddress);
            assertThat(snailgunSendEmailResponse.getToName()).isEqualTo("Mr. Test");
            assertThat(snailgunSendEmailResponse.getFromEmail()).isEqualTo("firstname.lastname@gmail.com");
            assertThat(snailgunSendEmailResponse.getFromName()).isEqualTo("Mr. Lastname");
            assertThat(snailgunSendEmailResponse.getSubject()).isEqualTo("Test email please ignore");
            assertThat(snailgunSendEmailResponse.getBody()).isEqualTo("My First Heading My first paragraph. This line has a break in the middle");
            assertThat(snailgunSendEmailResponse.getStatus()).isEqualTo("queued");
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

            ResponseEntity<String> statusResponse = emailController.status(snailgunSendEmailResponse.getId());
            SnailgunResponse snailgunStatusResponse = gson.fromJson(statusResponse.getBody(), SnailgunResponse.class);
            assertThat(snailgunStatusResponse.getId()).isNotNull();
            assertThat(snailgunStatusResponse.getToEmail()).isEqualTo(emailAddress);
            assertThat(snailgunStatusResponse.getToName()).isEqualTo("Mr. Test");
            assertThat(snailgunStatusResponse.getFromEmail()).isEqualTo("firstname.lastname@gmail.com");
            assertThat(snailgunStatusResponse.getFromName()).isEqualTo("Mr. Lastname");
            assertThat(snailgunStatusResponse.getSubject()).isEqualTo("Test email please ignore");
            assertThat(snailgunStatusResponse.getBody()).isEqualTo("My First Heading My first paragraph. This line has a break in the middle");
            assertThat(snailgunStatusResponse.getStatus()).isEqualTo("queued");
            assertThat(statusResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

            long timeout = 10000;
            long startTime = System.currentTimeMillis();

            //Wait for status change or timeout
            while(snailgunStatusResponse.getStatus().equals(snailgunSendEmailResponse.getStatus())
                    && System.currentTimeMillis()-startTime < timeout) {
                //Try every half a second
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
                statusResponse = emailController.status(snailgunSendEmailResponse.getId());
                snailgunStatusResponse = gson.fromJson(statusResponse.getBody(), SnailgunResponse.class);
            }
            assertThat(snailgunStatusResponse.getStatus()).isEqualTo("sent");
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
    public void testGetStatusNullId() {
        Throwable exception = assertThrows(InvalidParameterException.class, () -> {
            emailController.status(null);
        });
        assertEquals("Id cannot be null", exception.getMessage());
    }

    @Test
    public void testGetStatusEmptyId() {
        Throwable exception = assertThrows(InvalidParameterException.class, () -> {
            emailController.status("");
        });
        assertEquals("Id cannot be empty", exception.getMessage());
    }

    @Test
    public void testGetStatusBadId() {
        Throwable exception = assertThrows(HttpServerErrorException.InternalServerError.class, () -> {
                    emailController.status("fwadwadwa");
                });
        assertEquals("500 Internal Server Error: [{\"error\":\"undefined method `[]' for nil:NilClass\"}]", exception.getMessage());
    }

    @Profile("test-snailgun")
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
            return new SnailgunEmailService();
        }
    }

}
