package com.brightwheel.requests;

import com.brightwheel.entities.Email;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@SuppressWarnings("unused")
public class SnailgunRequest implements SendEmailRequest {

    @JsonProperty("from_email")
    private String fromEmail;

    @JsonProperty("from_name")
    private String fromName;

    @JsonProperty("to_email")
    private String toEmail;

    @JsonProperty("to_name")
    private String toName;

    @JsonProperty
    private String subject;

    @JsonProperty
    private String body;

    /**
     * Constructor based on email
     * @param email Input message for email
     */
    public SnailgunRequest(Email email) {
        this.fromEmail = email.getFrom();
        this.fromName = email.getFromName();
        this.toEmail = email.getTo();
        this.toName = email.getToName();
        this.subject = email.getSubject();
        this.body = email.getBody();
    }

    /**
     * Gets the sender email
     * @return the sender email
     */
    public String getFromEmail() {
        return fromEmail;
    }

    /**
     * Sets the sender email address
     * @param fromEmail the sender email address
     */
    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    /**
     * Gets the sender name
     * @return the sender name
     */
    public String getFromName() {
        return fromName;
    }

    /**
     * Sets the sender name
     * @param fromName the sender name
     */
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    /**
     * Gets the recipient email address
     * @return the recipient email address
     */
    public String getToEmail() {
        return toEmail;
    }

    /**
     * Sets the recipient email address
     * @param toEmail the recipient email address
     */
    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    /**
     * Gets the recipient name
     * @return the recipient name
     */
    public String getToName() {
        return toName;
    }

    /**
     * Sets the recipient name
     * @param toName  the recipient name
     */
    public void setToName(String toName) {
        this.toName = toName;
    }

    /**
     * Gets the email subject
     * @return the email subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the email subject
     * @param subject the email subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Gets the email body
     * @return the email subject
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets the email body
     * @param body the email subject
     */
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SnailgunRequest that = (SnailgunRequest) o;
        return Objects.equals(fromEmail, that.fromEmail) && Objects.equals(fromName, that.fromName) && Objects.equals(toEmail, that.toEmail) && Objects.equals(toName, that.toName) && Objects.equals(subject, that.subject) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromEmail, fromName, toEmail, toName, subject, body);
    }

    @Override
    public String toString() {
        return "SnailgunRequest{" +
                "fromEmail='" + fromEmail + '\'' +
                ", from_name='" + fromName + '\'' +
                ", toEmail='" + toEmail + '\'' +
                ", toName='" + toName + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
