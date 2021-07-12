package com.brightwheel.requests;

import com.brightwheel.entities.Email;

@SuppressWarnings("unused")
public class SendGridRequest implements SendEmailRequest {
    private String sender;

    private String recipient;

    private String subject;

    private String body;

    /**
     * Constructor based on email
     * @param email Input message for email
     */
    public SendGridRequest(Email email) {
        this.sender = email.getToName() + " <" + email.getTo() + ">";
        this.recipient = email.getFromName() + " <" + email.getFrom() + ">";
        this.subject = email.getSubject();
        this.body = email.getBody();
    }

    /**
     * Returns a string with the sender name and email address
     * @return a string with the sender name and email address
     */
    public String getSender() {
        return sender;
    }

    /**
     * sets a string with the sender name and email address
     * @param sender a string with the sender name and email address
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * returns a string with the recipient name and email address
     * @return a string with the recipient name and email address
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * sets a string with the recipient name and email address
     * @param recipient a string with the recipient name and email address
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    /**
     * returns the email subject
     * @return  the email subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * sets the email subject
     * @param subject the email subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * gets the email body
     * @return the email body
     */
    public String getBody() {
        return body;
    }

    /**
     * sets the email body
     * @param body the email body
     */
    public void setBody(String body) {
        this.body = body;
    }
}
