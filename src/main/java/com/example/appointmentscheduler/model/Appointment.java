package com.example.appointmentscheduler.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int contactID;
    private int userID;
    private int customerID;

    private final ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    public Appointment(){}

    public Appointment(String title, String description, String location, String type, LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime createdDate, String createdBy, int contactID, int userID, int customerID) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.contactID = contactID;
        this.userID = userID;
        this.customerID = customerID;
    }

    public Appointment(int id, String title, String description, String location, String type, LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime createdDate, String createdBy, int contactID, int userID, int customerID) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.contactID = contactID;
        this.userID = userID;
        this.customerID = customerID;
    }

    public Appointment(int id, String title, String type, String description, String location, LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime createdDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int contactID, int userID, int customerID) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.location = location;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.contactID = contactID;
        this.userID = userID;
        this.customerID = customerID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }


    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public void addList(Appointment appointment ){
        appointmentList.add(appointment);
    }

    public void updateList(Appointment updatedAppointment) {
        int index = -1;
        for (Appointment appointment : appointmentList) {
            if (appointment.getId() == updatedAppointment.getId()) {
                index = appointmentList.indexOf(appointment);
                break;
            }
        }

        if (index != -1) {
            // Remove the old appointment from the list
            appointmentList.remove(index);

            // Add the updated appointment to the list
            appointmentList.add(index, updatedAppointment);
        }
    }

    public ObservableList<Appointment> getAppointmentList() {
        return appointmentList;
    }
}
