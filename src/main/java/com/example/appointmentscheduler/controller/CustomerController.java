package com.example.appointmentscheduler.controller;

import com.example.appointmentscheduler.model.Customer;
import com.example.appointmentscheduler.model.Inventory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CustomerController {

    @FXML
    TextField idTextField;

    @FXML
    TextField nameTextField;

    @FXML
    TextField addressTextField;

    @FXML
    TextField phoneNumberTextField;

    @FXML
    TextField stateTextField;

    @FXML
    TextField postalCodeTextField;


    private Inventory inventory = new Inventory();
    private MainSchedulerController mainSchedulerController;


    public void cancelBtn(ActionEvent event) {
        Stage stage = (Stage) idTextField.getScene().getWindow();
        stage.close();

    }

    public void saveBtn(ActionEvent event) {

        int id = Integer.parseInt(idTextField.getText());
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        int phoneNumber = Integer.parseInt(phoneNumberTextField.getText());
        String state = stateTextField.getText();
        int postalCode = Integer.parseInt(postalCodeTextField.getText());

        Customer customer = new Customer(id, name, address, phoneNumber, state, postalCode);

        mainSchedulerController.getInventory().addList(customer);
        Stage stage = (Stage) idTextField.getScene().getWindow();
        stage.close();
        mainSchedulerController.refreshCustomerTable();

    }

    public void setMainSchedulerController(MainSchedulerController mainSchedulerController){
        this.mainSchedulerController = mainSchedulerController;
    }
}
