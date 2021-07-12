package com.brightwheel.entities;

import com.brightwheel.application.Application;
import com.brightwheel.controller.EmailController;
import com.brightwheel.exceptions.InvalidParameterException;
import com.brightwheel.services.EmailService;
import com.brightwheel.services.SendGridEmailService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class EmailTest {
    private List<String> validTestEmails = Arrays.asList("email@example.com",
            "firstname.lastname@example.com", "email@subdomain.example.com",
            "firstname+lastname@example.com", "email@[123.123.123.123]",
            "1234567890@example.com", "email@example-one.com",
            "_______@example.com", "email@example.name",
            "email@example.museum", "email@example.co.jp",
            "firstname-lastname@example.com", "much.”more\\ unusual”@example.com");
    private List<String> invalidTestEmails = Arrays.asList(
            "#@%^%#$@#$@#.com",
            "@example.com",
            "Joe Smith <email@example.com>",
            "email.example.com",
            "email@example@example.com",
            ".email@example.com",
            "email.@example.com",
            "email..email@example.com",
            "email@example.com (Joe Smith)",
            "email@example",
            "email@-example.com",
            "email@example.web",
            "email@111.222.333.44444",
            "email@example..com",
            "Abc..123@example.com",
            "”(),:;<>[\\]@example.com",
            " just”not”right@example.com",
            "this\\ is\"really\"not\\allowed@example.com"
            );

    private String testHtml = "<!DOCTYPE html><html<body><h1>My First Heading</h1><p>My first paragraph.</p>" +
            "This line has<script type=\"text/javascript\">function doSomeBadStuffThatShouldGetParsedOut(){}" +
            "</script>a break<br>in the middle</body></html>";


    @Test
    public void testValidEmailNoArgsConstructor() {
        Email email = new Email();
        email.setTo("test@gmail.com");
        email.setToName("Mr. Test");
        email.setFrom("firstname.lastname@gmail.com");
        email.setFromName("Mr. Lastname");
        email.setSubject("Test email please ignore");
        email.setBody("Please ignore");
        assertThat(email.getTo()).isEqualTo("test@gmail.com");
        assertThat(email.getToName()).isEqualTo("Mr. Test");
        assertThat(email.getFrom()).isEqualTo("firstname.lastname@gmail.com");
        assertThat(email.getFromName()).isEqualTo("Mr. Lastname");
        assertThat(email.getSubject()).isEqualTo("Test email please ignore");
        assertThat("Please ignore");
    }

    @Test
    public void testValidEmailsOnTo() {
        validTestEmails.forEach(emailAddress -> {
            Email email = new Email();
            email.setTo(emailAddress);
            email.setFrom(emailAddress);

            assertThat(email.getTo()).isEqualTo(emailAddress);
            assertThat(email.getFrom()).isEqualTo(emailAddress);
        });
    }

    @Test
    public void testInValidEmails() {
        invalidTestEmails.forEach(emailAddress -> {

            Throwable exception = assertThrows(InvalidParameterException.class, () -> {
                Email email = new Email();
                email.setToName("Mr. Test");
                email.setFrom("firstname.lastname@gmail.com");
                email.setFromName("Mr. Lastname");
                email.setSubject("Test email please ignore");
                email.setBody("Please ignore");
                email.setTo(emailAddress);
                email.validate();
            });
            assertEquals(String.format("To '%s' is not a valid email", emailAddress), exception.getMessage());

            exception = assertThrows(InvalidParameterException.class, () -> {
                Email email = new Email();
                email.setTo("test@gmail.com");
                email.setToName("Mr. Test");
                email.setFromName("Mr. Lastname");
                email.setSubject("Test email please ignore");
                email.setBody("Please ignore");
                email.setFrom(emailAddress);
                email.validate();
            });
            assertEquals(String.format("From '%s' is not a valid email", emailAddress), exception.getMessage());
        });
    }

    @Test
    public void testHtmlBody() {
        Email email = new Email();
        email.setTo("test@gmail.com");
        email.setToName("Mr. Test");
        email.setFrom("firstname.lastname@gmail.com");
        email.setFromName("Mr. Lastname");
        email.setSubject("subject");
        email.setBody(testHtml);
        email.validate();
        assertThat(email.getBody()).isEqualTo("My First Heading My first paragraph. This line has a break in the middle");
    }

    @Test
    public void testNullTo() {
        Throwable exception = assertThrows(InvalidParameterException.class, () -> {
            Email email = new Email();
            email.setTo(null);
            email.setToName("Mr. Test");
            email.setFrom("firstname.lastname@gmail.com");
            email.setFromName("Mr. Lastname");
            email.setSubject("Test email please ignore");
            email.setBody("Please ignore");
            email.validate();
        });
        assertEquals(String.format("To 'null' is not a valid email", "null"), exception.getMessage());
    }

    @Test
    public void testEmptyTo(){
        Throwable exception = assertThrows(InvalidParameterException.class, () -> {
            Email email = new Email();
            email.setToName("Mr. Test");
            email.setFrom("firstname.lastname@gmail.com");
            email.setFromName("Mr. Lastname");
            email.setSubject("Test email please ignore");
            email.setBody("Please ignore");
            email.setTo("");
            email.validate();
        });
        assertEquals("To '' is not a valid email", exception.getMessage());
    }

    @Test
    public void testNullToName() {
        Throwable exception = assertThrows(InvalidParameterException.class, () -> {
            Email email = new Email();
            email.setToName(null);
            email.setTo("test@gmail.com");
            email.setFrom("firstname.lastname@gmail.com");
            email.setFromName("Mr. Lastname");
            email.setSubject("Test email please ignore");
            email.setBody("Please ignore");
            email.validate();
        });
        assertEquals("toName cannot be null", exception.getMessage());
    }

    @Test
    public void testEmptyToName(){
        Throwable exception = assertThrows(InvalidParameterException.class, () -> {
            Email email = new Email();
            email.setTo("test@gmail.com");
            email.setFrom("firstname.lastname@gmail.com");
            email.setFromName("Mr. Lastname");
            email.setSubject("Test email please ignore");
            email.setBody("Please ignore");
            email.setToName("");
            email.validate();
        });
        assertEquals("toName cannot be empty", exception.getMessage());
    }

    @Test
    public void testNullFrom() {
        Throwable exception = assertThrows(InvalidParameterException.class, () -> {
            Email email = new Email();
            email.setTo("test@gmail.com");
            email.setToName("Mr. Test");
            email.setFromName("Mr. Lastname");
            email.setSubject("Test email please ignore");
            email.setBody("Please ignore");
            email.setFrom(null);
            email.validate();
        });
        assertEquals("From 'null' is not a valid email", exception.getMessage());
    }

    @Test
    public void testEmptyFrom(){
        Throwable exception = assertThrows(InvalidParameterException.class, () -> {
            Email email = new Email();
            email.setTo("test@gmail.com");
            email.setToName("Mr. Test");
            email.setFromName("Mr. Lastname");
            email.setSubject("Test email please ignore");
            email.setBody("Please ignore");
            email.setFrom("");
            email.validate();
        });
        assertEquals("From '' is not a valid email", exception.getMessage());
    }

    @Test
    public void testNullFromName() {
        Throwable exception = assertThrows(InvalidParameterException.class, () -> {
            Email email = new Email();
            email.setTo("test@gmail.com");
            email.setToName("Mr. Test");
            email.setFrom("firstname.lastname@gmail.com");
            email.setSubject("Test email please ignore");
            email.setBody("Please ignore");
            email.setFromName(null);
            email.validate();
        });
        assertEquals("fromName cannot be null", exception.getMessage());
    }

    @Test
    public void testEmptyFromName(){
        Throwable exception = assertThrows(InvalidParameterException.class, () -> {
            Email email = new Email();
            email.setTo("test@gmail.com");
            email.setToName("Mr. Test");
            email.setFrom("firstname.lastname@gmail.com");
            email.setSubject("Test email please ignore");
            email.setBody("Please ignore");
            email.setFromName("");
            email.validate();
        });
        assertEquals("fromName cannot be empty", exception.getMessage());
    }

    @Test
    public void testNullSubject() {
        Throwable exception = assertThrows(InvalidParameterException.class, () -> {
            Email email = new Email();
            email.setTo("test@gmail.com");
            email.setToName("Mr. Test");
            email.setFrom("firstname.lastname@gmail.com");
            email.setFromName("Mr. Lastname");
            email.setBody("Please ignore");
            email.setSubject(null);
            email.validate();
        });
        assertEquals("subject cannot be null", exception.getMessage());
    }

    @Test
    public void testEmptySubject(){
        Throwable exception = assertThrows(InvalidParameterException.class, () -> {
            Email email = new Email();
            email.setTo("test@gmail.com");
            email.setToName("Mr. Test");
            email.setFrom("firstname.lastname@gmail.com");
            email.setFromName("Mr. Lastname");
            email.setBody("Please ignore");
            email.setSubject("");
            email.validate();
        });
        assertEquals("subject cannot be empty", exception.getMessage());
    }

    @Test
    public void testNullBody() {
        Throwable exception = assertThrows(InvalidParameterException.class, () -> {
            Email email = new Email();
            email.setTo("test@gmail.com");
            email.setToName("Mr. Test");
            email.setFrom("firstname.lastname@gmail.com");
            email.setFromName("Mr. Lastname");
            email.setSubject("subject");
            email.setBody(null);
            email.validate();
        });
        assertEquals("body cannot be null", exception.getMessage());
    }

    @Test
    public void testEmptyBody(){
        Throwable exception = assertThrows(InvalidParameterException.class, () -> {
            Email email = new Email();
            email.setTo("test@gmail.com");
            email.setToName("Mr. Test");
            email.setFrom("firstname.lastname@gmail.com");
            email.setFromName("Mr. Lastname");
            email.setSubject("subject");
            email.setBody("");
            email.validate();
        });
        assertEquals("body cannot be empty", exception.getMessage());
    }
}
