package com.example.appointmentscheduler.controller;

import com.example.appointmentscheduler.model.Appointment;
import com.example.appointmentscheduler.model.Customer;
import com.example.appointmentscheduler.model.Inventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MainSchedulerController {

    @FXML
    TableColumn<Appointment, Integer> idColumn;

    @FXML
    TableColumn<Appointment, String> titleColumn;

    @FXML
    TableColumn<Appointment, String> descriptionColumn;

    @FXML
    TableColumn<Appointment, String> typeColumn;

    @FXML
    TableColumn<Appointment, String> locationColumn;

    @FXML
    TableColumn<Appointment, LocalDateTime> startDateTimeColumn;

    @FXML
    TableColumn<Appointment, LocalDateTime> endDateTimeColumn;

//    @FXML
//    TableColumn<Appointment, LocalDateTime> startTimeColumn;
//
//    @FXML
//    TableColumn<Appointment, LocalDateTime> endTimeColumn;

    @FXML
    TableColumn<Appointment, Integer> contactColumn;

    @FXML
    TableColumn<Appointment, Integer> customerColumn;

    @FXML
    TableColumn<Appointment, Integer> userColumn;

    @FXML
    TableView<Appointment> appointmentTableView;

    @FXML
    TableView<Customer> customerTableView;

    @FXML
    TableColumn<Customer, Integer> customerIdColumn;

    @FXML
    TableColumn<Customer, String> customerNameColumn;

    @FXML
    TableColumn<Customer, String> customerAddColumn;

    @FXML
    TableColumn<Customer, Integer> customerPhoneColumn;

    @FXML
    TableColumn<Customer, String> customerStateColumn;

    @FXML
    TableColumn<Customer, Integer> customerCodeColumn;







    private AppointmentController appointmentController;
    private CustomerController customerController;
    private Inventory inventory;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");


    public void initialize(){

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));


        startDateTimeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime dateTime, boolean empty) {
                super.updateItem(dateTime, empty);

                if (empty || dateTime == null) {
                    setText(null);
                } else {
                    setText(dateTime.format(dateTimeFormatter));
                }
            }
        });


        endDateTimeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime dateTime, boolean empty) {
                super.updateItem(dateTime, empty);

                if (empty || dateTime == null) {
                    setText(null);
                } else {
                    setText(dateTime.format(dateTimeFormatter));
                }
            }
        });
//        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
//        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        inventory = new Inventory();
        appointmentTableView.setItems(inventory.getAppointmentList());


        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerStateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        customerCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerTableView.setItems(inventory.getCustomerList());

    }


    public void addAppointmentBtn(ActionEvent event) throws IOException {

        ObservableList<LocalTime> timeOptions = FXCollections.observableArrayList(
                LocalTime.of(9, 0),
                LocalTime.of(12, 30),
                LocalTime.of(15, 45),
                LocalTime.of(19, 15)
                // Add more time options as needed
        );



        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/appointmentscheduler/AppointmentForm.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Add Form");
        stage.setScene(scene);
        stage.show();

        ComboBox<LocalTime> startTimePicker = (ComboBox<LocalTime>) loader.getNamespace().get("startTimePicker");
        startTimePicker.setItems(timeOptions);

        ComboBox<LocalTime> endTimePicker = (ComboBox<LocalTime>) loader.getNamespace().get("endTimePicker");
        endTimePicker.setItems(timeOptions);

        // Get the instance of AddAppointmentController
        appointmentController = loader.getController();


        // Set the MainSchedulerController instance
        appointmentController.setMainSchedulerController(this);
        appointmentTableView.refresh();


    }

    public void refreshAppointmentTable() {
        appointmentTableView.refresh();
    }
    public void refreshCustomerTable() {
        customerTableView.refresh();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void addCustomerBtn(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/appointmentscheduler/CustomerForm.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Add Form");
        stage.setScene(scene);
        stage.show();

        customerController = loader.getController();
        customerController.setMainSchedulerController(this);
        customerTableView.refresh();

    }
}