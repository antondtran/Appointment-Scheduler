package com.example.appointmentscheduler.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    public Inventory(){

    }

    public void addList(Appointment appointment ){
        appointmentList.add(appointment);
    }

    public void addList(Customer customer){
        customerList.add(customer);
    }

    public ObservableList<Appointment> getAppointmentList() {
        return appointmentList;
    }
    public ObservableList<Customer> getCustomerList() {
        return customerList;
    }
}
