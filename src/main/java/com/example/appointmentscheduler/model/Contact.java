package com.example.appointmentscheduler.model;

import java.time.LocalDateTime;

public class Contact {
    private int contactId;
    private String title;
    private String type;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int customerId;

    public Contact() {
    }

    public Contact(int contactId, String title, String type, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, int customerId) {
        this.contactId = contactId;
        this.title = title;
        this.type = type;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.customerId = customerId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
