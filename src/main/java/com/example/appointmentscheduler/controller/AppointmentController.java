package com.example.appointmentscheduler.controller;

import com.example.appointmentscheduler.model.Appointment;
import com.example.appointmentscheduler.model.Inventory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentController {

    @FXML
    TextField idTextField;

    @FXML
    TextField titleTextField;

    @FXML
    TextField typeTextField;

    @FXML
    TextField descriptionTextField;

    @FXML
    TextField locationTextField;

    @FXML
    DatePicker startDatePicker;

    @FXML
    DatePicker endDatePicker;

    @FXML
    ComboBox<LocalTime> startTimePicker;

    @FXML
    ComboBox<LocalTime> endTimePicker;

    @FXML
    TextField contactTextField;

    @FXML
    TextField userTextField;

    @FXML
    TextField customerTextField;






    private Inventory inventory = new Inventory();
    private MainSchedulerController mainSchedulerController;



    public void saveBtn(ActionEvent event){


        String startTimeString = startTimePicker.getSelectionModel().getSelectedItem().toString();
        String endTimeString = endTimePicker.getSelectionModel().getSelectedItem().toString();

        // Get the selected date from the DatePicker (startDatePicker) and convert it to a LocalDate
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        // Parse the selected time strings to LocalTime
        LocalTime startTime = LocalTime.parse(startTimeString);
        LocalTime endTime = LocalTime.parse(endTimeString);

        // Combine the selected date (startDate) with the selected time (startTime) to create LocalDateTime
        LocalDateTime combinedStartDateTime = startDate.atTime(startTime);
        LocalDateTime combinedEndDateTime = endDate.atTime(endTime);


        int id = Integer.parseInt(idTextField.getText());
        String title = titleTextField.getText();
        String type = typeTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        int userID = Integer.parseInt(userTextField.getText());
        int customerID = Integer.parseInt(customerTextField.getText());
        int contactID = Integer.parseInt(contactTextField.getText());

        Appointment appointment = new Appointment(id, title, type, description, location, combinedStartDateTime, combinedEndDateTime, userID, customerID, contactID);

        mainSchedulerController.getInventory().addList(appointment);

        Stage stage = (Stage) idTextField.getScene().getWindow();
        stage.close();
        mainSchedulerController.refreshAppointmentTable();

    }

    public void setMainSchedulerController(MainSchedulerController mainSchedulerController){
        this.mainSchedulerController = mainSchedulerController;
    }

    public void cancelBtn(ActionEvent event) {
        Stage stage = (Stage) idTextField.getScene().getWindow();
        stage.close();

    }
}
