package com.example.appointmentscheduler.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
