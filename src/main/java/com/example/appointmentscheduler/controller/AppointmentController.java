package com.example.appointmentscheduler.controller;

import com.example.appointmentscheduler.dao.AppointmentDAO;
import com.example.appointmentscheduler.dao.CustomerDAO;
import com.example.appointmentscheduler.model.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;



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
    ComboBox<String> contactPicker;

    @FXML
    ComboBox<String> userPicker;

    @FXML
    TextField customerTextField;

    private static int assignedAppointmentID;

    private MainSchedulerController mainSchedulerController;
    private Map<String, Integer> contactMap;
    private Map<String, Integer> userMap;
    private CustomerDAO customerDAO;


    public AppointmentController(){}

    public void setAppointmentController(Appointment existingAppointment) {
        // Populate the form fields with the details of the existing appointment
        idTextField.setText(String.valueOf(existingAppointment.getId()));
        titleTextField.setText(existingAppointment.getTitle());
        typeTextField.setText(existingAppointment.getType());
        descriptionTextField.setText(existingAppointment.getDescription());
        locationTextField.setText(existingAppointment.getLocation());

        LocalDate startDate = existingAppointment.getStartDateTime().toLocalDate();
        LocalDate endDate = existingAppointment.getEndDateTime().toLocalDate();
        startTimePicker.setValue(existingAppointment.getStartDateTime().toLocalTime());
        endTimePicker.setValue(existingAppointment.getEndDateTime().toLocalTime());
        startDatePicker.setValue(startDate);
        endDatePicker.setValue(endDate);
        customerTextField.setText(String.valueOf(Integer.valueOf(existingAppointment.getCustomerID())));



        int userID = existingAppointment.getUserID();
        String selectedUserName = null;
        for (Map.Entry<String, Integer> entry : userMap.entrySet()) {
            if (entry.getValue() == userID) {
                selectedUserName = entry.getKey();
                break;
            }
        }

        userPicker.setValue(selectedUserName);


        // Set the contactPicker ComboBox to display the contact name associated with the existing appointment
        int contactID = existingAppointment.getContactID();

        // Use the contactMap to look up the contact name associated with the contact ID
        String selectedContactName = null;
        for (Map.Entry<String, Integer> entry : contactMap.entrySet()) {
            if (entry.getValue() == contactID) {
                selectedContactName = entry.getKey();
                break;
            }
        }

        // Set the selected contact name in the contactPicker ComboBox
        contactPicker.setValue(selectedContactName);

    }


    public void saveBtn(ActionEvent event) {

        LocalDateTime currentDateTime = LocalDateTime.now();






        // Get the form data from the input fields
        int id;
        try {
            id = Integer.parseInt(idTextField.getText());
        } catch (NumberFormatException e) {
            // If idTextField is empty or contains non-numeric characters, consider it as a new appointment
            id = 0; // Generate a new appointment ID
        }
        String title = titleTextField.getText();
        String type = typeTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String selectedUserName = String.valueOf(userPicker.getValue());
        int userID = userMap.get(selectedUserName);
        int customerID = Integer.parseInt(customerTextField.getText());
        String selectedContactName = String.valueOf(contactPicker.getValue());
        int contactID = contactMap.get(selectedContactName);


        String startTimeString = startTimePicker.getSelectionModel().getSelectedItem().toString();
        String endTimeString = endTimePicker.getSelectionModel().getSelectedItem().toString();

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        LocalTime startTime = LocalTime.parse(startTimeString);
        LocalTime endTime = LocalTime.parse(endTimeString);

        // Combine the selected date (startDate) with the selected time (startTime) to create LocalDateTime
        LocalDateTime combinedStartDateTime = startDate.atTime(startTime);
        LocalDateTime combinedEndDateTime = endDate.atTime(endTime);
        LocalDateTime createDate = LocalDateTime.now();
        LocalDateTime lastUpdate = LocalDateTime.now();
        String createdBy = "script";
        String lastUpdatedBy = "script";

        if (!isWithinBusinessHours(combinedStartDateTime) || !isWithinBusinessHours(combinedEndDateTime)) {
            // Show an error message if the appointment time is outside business hours or on weekends
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Appointment Time");
            alert.setContentText("Appointments must be scheduled between 8:00 a.m. and 10:00 p.m. ET on weekdays.");
            alert.showAndWait();
            return;
        }




        Appointment existingAppointment = AppointmentDAO.getAppointmentById(id);


        if (existingAppointment != null) {
            // This is an existing appointment being updated
            existingAppointment.setTitle(title);
            existingAppointment.setType(type);
            existingAppointment.setDescription(description);
            existingAppointment.setLocation(location);
            existingAppointment.setStartDateTime(combinedStartDateTime);
            existingAppointment.setEndDateTime(combinedEndDateTime);
            existingAppointment.setCreatedBy(createdBy);
            existingAppointment.setLastUpdate(lastUpdate);
            existingAppointment.setLastUpdatedBy(lastUpdatedBy);
            existingAppointment.setUserID(userID);
            existingAppointment.setCustomerID(customerID);
            existingAppointment.setContactID(contactID);

            try {
                // Use the AppointmentDAO to update the appointment in the database

                if (CustomerDAO.doesCustomerExist(existingAppointment.getCustomerID())){

                    if (existingAppointment.getStartDateTime().toLocalDate().isBefore(existingAppointment.getEndDateTime().toLocalDate()) || existingAppointment.getStartDateTime().toLocalDate().isEqual(existingAppointment.getEndDateTime().toLocalDate()) && existingAppointment.getStartDateTime().toLocalTime().isBefore(existingAppointment.getEndDateTime().toLocalTime())){

                            AppointmentDAO.updateAppointment(existingAppointment);

                            // Refresh the main controller's appointment list and table view
                            mainSchedulerController.getAppointment().updateList(existingAppointment);
                            mainSchedulerController.refreshAppointmentTable();

                            // Close the stage
                            Stage stage = (Stage) idTextField.getScene().getWindow();
                            stage.close();


                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid Date or Time.");
                        alert.setContentText("Please ensure the appointment date and time is before the end date and time");
                        alert.showAndWait();
                    }

                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Customer ID");
                    alert.showAndWait();

                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // This is a new appointment being added
            int newAppointmentId = id == 0 ? AppointmentDAO.getLastUsedAppointmentId() + 1 : id; // Use the provided ID if available, otherwise generate a new ID
            Appointment newAppointment = new Appointment(newAppointmentId, title, type, description, location, combinedStartDateTime, combinedEndDateTime, createDate, createdBy, contactID, userID, customerID);

            try {

                if (CustomerDAO.doesCustomerExist(newAppointment.getCustomerID())){

                    if (newAppointment.getStartDateTime().toLocalDate().isBefore(newAppointment.getEndDateTime().toLocalDate()) || newAppointment.getStartDateTime().toLocalDate().isEqual(newAppointment.getEndDateTime().toLocalDate()) && newAppointment.getStartDateTime().toLocalTime().isBefore(newAppointment.getEndDateTime().toLocalTime())){
                        // Use the AppointmentDAO to insert the appointment into the database
                        AppointmentDAO.insertAppointment(newAppointment);


                        // Update the main controller's appointment list
                        mainSchedulerController.getAppointment().addList(newAppointment);
                        mainSchedulerController.refreshAppointmentTable();

                        // Close the stage
                        Stage stage = (Stage) idTextField.getScene().getWindow();
                        stage.close();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid Date or Time.");
                        alert.setContentText("Please ensure the appointment date and time is before the end date and time");
                        alert.showAndWait();
                    }

                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Customer ID");
                    alert.showAndWait();

                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }




    public void setMainSchedulerController(MainSchedulerController mainSchedulerController){
        this.mainSchedulerController = mainSchedulerController;
    }

    public void cancelBtn(ActionEvent event) {
        Stage stage = (Stage) idTextField.getScene().getWindow();
        stage.close();

    }

    public static int generateAppointmentID(){

        return ++assignedAppointmentID;
    }


    public void setContactMap(Map<String, Integer> contactMap) {
        this.contactMap = contactMap;
    }

    public void setUserMap(Map<String, Integer> userMap) {
        this.userMap = userMap;
    }

    public boolean isWithinBusinessHours(LocalDateTime dateTime) {
        LocalTime startTime = dateTime.toLocalTime();
        int startHour = startTime.getHour();
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();

        // Check if the time is within business hours (8:00 a.m. to 10:00 p.m.) and not on weekends (Saturday or Sunday)
        return (startHour >= 8 && startHour <= 22) &&
                (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY);
    }




}
