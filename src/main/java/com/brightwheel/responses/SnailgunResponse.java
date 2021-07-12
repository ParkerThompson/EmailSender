package com.brightwheel.responses;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SnailgunResponse {
    private String id;
    @SerializedName("from_email")
    private String fromEmail;
    @SerializedName("from_name")
    private String fromName;
    @SerializedName("to_email")
    private String toEmail;
    @SerializedName("to_name")
    private String toName;
    private String subject;
    private String body;
    private String status;

    /**
     * Gets the service provided identifier for the email
     * @return the service provided identifier for the email
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the service provided identifier for the email
     * @param id the service provided identifier for the email
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the email address of the sender
     * @return the email address of the sender
     */
    public String getFromEmail() {
        return fromEmail;
    }

    /**
     * Sets the email of the sender
     * @param fromEmail the email of the sender
     */
    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    /**
     * Gets the name of the recipient
     * @return the name of the recipient
     */
    public String getFromName() {
        return fromName;
    }

    /**
     * Sets the name of the recipient
     * @param fromName the name of the recipient
     */
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    /**
     * Gets the email address of the sender
     * @return the email address of the sender
     */
    public String getToEmail() {
        return toEmail;
    }

    /**
     * Sets the email address of the sender
     * @param toEmail the email address of the sender
     */
    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    /**
     * Gets the name of the sender
     * @return the name of the sender
     */
    public String getToName() {
        return toName;
    }

    /**
     * Sets the name of the sender
     * @param toName the name of the sender
     */
    public void setToName(String toName) {
        this.toName = toName;
    }

    /**
     * Gets the subject of the email
     * @return the subject of the email
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject of the email
     * @param subject the subject of the email
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Gets the body of the email
     * @return the body of the email
     */
    public String getBody() {
        return body;
    }

    /**
     * Sets the body of the email
     * @param body the body of the email
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Gets the status of the email(queued, sent, failed)
     * @return the status of the email(queued, sent, failed)
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the email(queued, sent, failed)
     * @param status the status of the email(queued, sent, failed)
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
