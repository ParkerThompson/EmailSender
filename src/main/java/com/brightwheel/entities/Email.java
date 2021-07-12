package com.brightwheel.entities;

import com.brightwheel.exceptions.InvalidParameterException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.validator.routines.EmailValidator;
import org.jsoup.Jsoup;

import java.util.Objects;

@ApiModel
public class Email {
    @ApiModelProperty(value = "Recipient email address", example = "example@example.com")
    private String to;
    @ApiModelProperty(value = "Recipient name", example = "Mr. Example")
    private String toName;
    @ApiModelProperty(value = "Sender email address", example = "firstname.lastname@example.com")
    private String from;
    @ApiModelProperty(value = "Sender name", example = "Mr. Lastname")
    private String fromName;
    @ApiModelProperty(value = "Email subject", example = "Fwd: Fwd: Re: Urgent")
    private String subject;
    @ApiModelProperty(value = "Email body", example = "This message is important")
    private String body;

    private final EmailValidator emailValidator = EmailValidator.getInstance();

    /**
     * Get The email address to send to
     * @return The email address to send to
     */
    public String getTo() {
        return to;
    }

    /**
     * set The email address to send to
     * @param to The email address to send to
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Get The name to accompany the email
     * @return The name to accompany the email
     */
    public String getToName() {
        return toName;
    }

    /**
     * Set The name to accompany the email
     * @param toName The name to accompany the email
     */
    public void setToName(String toName) {
        this.toName = toName;
    }

    /**
     * Get The email address in the from and reply fields
     * @return The email address in the from and reply fields
     */
    public String getFrom() {
        return from;
    }

    /**
     * Set The email address in the from and reply fields
     * @param from The email address in the from and reply fields
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * get The name to accompany the from/reply emails
     * @return The name to accompany the from/reply emails
     */
    public String getFromName() {
        return fromName;
    }

    /**
     * Set The name to accompany the from/reply emails
     * @param fromName The name to accompany the from/reply emails
     */
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    /**
     * Get The subject line of the email
     * @return The subject line of the email
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Set The subject line of the email
     * @param subject The subject line of the email
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Get The HTML body of the email
     * @return The HTML body of the email
     */
    public String getBody() {
        return body;
    }

    /**
     * Set The HTML body of the email
     * @param body The HTML body of the email
     */
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(to, email.to) && Objects.equals(toName, email.toName) && Objects.equals(from, email.from)
                && Objects.equals(subject, email.subject) && Objects.equals(body, email.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(to, toName, from, subject, body);
    }

    @Override
    public String toString() {
        return "Email{" +
                "to='" + to + '\'' +
                ", toName='" + toName + '\'' +
                ", from='" + from + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    /**
     * Verifies recipient email address is valid
     */
    private void validateTo() {
        if(!emailValidator.isValid(to)) {
            throw new InvalidParameterException(String.format("To '%s' is not a valid email", to));
        }
    }

    /**
     * Verifies sender email address is valid
     */
    private void validateFrom() {
        if(!emailValidator.isValid(from)) {
            throw new InvalidParameterException(String.format("From '%s' is not a valid email", from));
        }
    }

    /**
     * Verifies string field is not null or empty
     */
    private void validateString(String fieldName, String s) {
        if(s == null) {
            throw new InvalidParameterException(String.format("%s cannot be null", fieldName));
        }
        if(s.isEmpty()) {
            throw new InvalidParameterException(String.format("%s cannot be empty", fieldName));
        }
    }

    /**
     * Verifies whether fields contain valid values and throws exception if they don't
     */
    public void validate() {
        validateString("toName", toName);
        validateString("fromName", fromName);
        validateString("subject", subject);
        validateString("body", body);
        body = Jsoup.parse(body).text();
        validateTo();
        validateFrom();
    }

}
