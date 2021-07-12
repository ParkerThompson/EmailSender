package com.brightwheel.responses;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Objects;

@SuppressWarnings("unused")
public class SendGridResponse {
    private String id;
    private String sender;
    private String recipient;
    private String subject;
    private String body;
    @SerializedName("created_at")
    private Date createdAt;

    /**
     * Constructor for SendGridResponse
     */
    public SendGridResponse() {
    }

    /**
     * Returns the service provided identifier for the email
     * @return the service provided identifier for the email
     */
    public String getId() {
        return id;
    }

    /**
     * sets the service provided identifier for the email
     * @param id the service provided identifier for the email
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * gets the email sender
     * @return the email sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * sets the email sender
     * @param sender the email sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * gets the email sender
     * @return the email sender
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * sets the email sender
     * @param recipient the email sender
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    /**
     * gets the email subject
     * @return the email subject
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

    /**
     * gets the date the request was send at
     * @return the date the request was send at
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * sets the date the request was send at
     * @param createdAt the date the request was send at
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendGridResponse that = (SendGridResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(sender, that.sender) && Objects.equals(recipient, that.recipient) && Objects.equals(subject, that.subject) && Objects.equals(body, that.body) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sender, recipient, subject, body, createdAt);
    }

    @Override
    public String toString() {
        return "SendGridResponse{" +
                "id='" + id + '\'' +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
