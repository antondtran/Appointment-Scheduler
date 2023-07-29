package com.example.appointmentscheduler.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

public class Customer {
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private int divisionID;
    private String divisionName;
    private String postalCode;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;

    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    public Customer(){}

    public Customer(String name, String phoneNumber, int divisionID, String postalCode) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.divisionID = divisionID;
        this.postalCode = postalCode;
    }

    public Customer(int id, String name, String address, String phoneNumber, int divisionID, String postalCode, LocalDateTime createdDate, String createdBy) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.divisionID = divisionID;
        this.postalCode = postalCode;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
    }

    public Customer(int id, String name, String address, String phoneNumber, int divisionID, String divisionName, String postalCode, LocalDateTime createdDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.postalCode = postalCode;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Customer(int id, String name, String address, String phoneNumber, int divisionID, String postalCode) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.divisionID = divisionID;
        this.postalCode = postalCode;
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

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void addList(Customer customer){
        customerList.add(customer);
    }

    public ObservableList<Customer> getCustomerList() {
        return customerList;
    }

    public void updateList(Customer updatedCustomer) {
        int index = -1;
        for (Customer customer : customerList) {
            if (customer.getId() == updatedCustomer.getId()) {
                index = customerList.indexOf(customer);
                break;
            }
        }

        if (index != -1) {
            // Remove the old appointment from the list
            customerList.remove(index);

            // Add the updated appointment to the list
            customerList.add(index, updatedCustomer);
        }
    }
}
