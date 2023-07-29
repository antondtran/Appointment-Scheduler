package com.example.appointmentscheduler.controller;

import com.example.appointmentscheduler.dao.CustomerDAO;
import com.example.appointmentscheduler.dao.DivisionDAO;
import com.example.appointmentscheduler.model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

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
    ComboBox<String> stateComboField;

    @FXML
    TextField postalCodeTextField;

    private static int assignedCustomerID;


    private MainSchedulerController mainSchedulerController;
    private DivisionDAO divisionDAO;
    private Map<String, Integer> divisionMap;

    public CustomerController() throws SQLException {
        divisionDAO = new DivisionDAO();
        divisionMap = divisionDAO.fetchDivisionFromDb();

    }


    public void cancelBtn(ActionEvent event) {
        Stage stage = (Stage) idTextField.getScene().getWindow();
        stage.close();

    }



    public void saveBtn(ActionEvent event) {
        // Get the form data from the input fields
        int id;
        try {
            id = Integer.parseInt(idTextField.getText());
        } catch (NumberFormatException e) {
            // If idTextField is empty or contains non-numeric characters, consider it as a new customer
            id = 0;
        }
        String name = nameTextField.getText();
        String address = addressTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String divisionName = stateComboField.getSelectionModel().getSelectedItem();
        String postalCode = postalCodeTextField.getText();

        LocalDateTime createDate = LocalDateTime.now();
        LocalDateTime lastUpdate = LocalDateTime.now();
        String createdBy = "script";
        String lastUpdatedBy = "script";

        if (divisionMap.containsKey(divisionName)) {
            int divisionId = divisionMap.get(divisionName);

            if (id == 0) {
                // This is a new customer being added
                id = generateCustomerID(); // Generate a new ID for the new customer
                Customer newCustomer = new Customer(id, name, address, phoneNumber, divisionId, postalCode, createDate, createdBy);
                newCustomer.setDivisionName(divisionName);

                try {
                    // Use the CustomerDAO to insert the customer into the database
                    CustomerDAO.insertCustomer(newCustomer);

                    // Update the main controller's customer list
                    mainSchedulerController.getCustomer().addList(newCustomer);
                    mainSchedulerController.refreshCustomerTable();

                    // Close the stage
                    Stage stage = (Stage) idTextField.getScene().getWindow();
                    stage.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                // This is an existing customer being updated
                Customer existingCustomer = CustomerDAO.getCustomerById(id);

                if (existingCustomer != null) {
                    existingCustomer.setName(name);
                    existingCustomer.setAddress(address);
                    existingCustomer.setPhoneNumber(phoneNumber);
                    existingCustomer.setDivisionID(divisionId);
                    existingCustomer.setDivisionName(divisionName);
                    existingCustomer.setPostalCode(postalCode);
                    existingCustomer.setLastUpdate(lastUpdate);
                    existingCustomer.setLastUpdatedBy(lastUpdatedBy);

                    try {
                        // Use the CustomerDAO to update the customer in the database
                        CustomerDAO.updateCustomer(existingCustomer);

                        // Refresh the main controller's customer list and table view
                        mainSchedulerController.getCustomer().updateList(existingCustomer);
                        mainSchedulerController.refreshCustomerTable();

                        // Close the stage
                        Stage stage = (Stage) idTextField.getScene().getWindow();
                        stage.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle the case when the selected ID does not correspond to an existing customer
                    // This could be an error or validation check, depending on your use case
                }
            }
        } else {
            // Handle the case when the selected division is not found in the divisionMap
            // This could be an error or validation check, depending on your use case
        }
    }

    public int generateCustomerID() {
        // Retrieve the last used customer ID from the database
        int lastUsedId = CustomerDAO.getLastUsedCustomerId();

        // Increment the last used ID to generate the next ID
        int nextId = lastUsedId + 1;

        return nextId;
    }




    public void setCustomerController(Customer existingCustomer) {
        // Populate the form fields with the details of the existing appointment
        idTextField.setText(String.valueOf(existingCustomer.getId()));
        nameTextField.setText(existingCustomer.getName());
        addressTextField.setText(existingCustomer.getAddress());
        postalCodeTextField.setText(existingCustomer.getPostalCode());
        phoneNumberTextField.setText(existingCustomer.getPhoneNumber());
        stateComboField.setValue(existingCustomer.getDivisionName());
;
    }



    public void setMainSchedulerController(MainSchedulerController mainSchedulerController){
        this.mainSchedulerController = mainSchedulerController;
    }


    public int getCustomerID(){
        return assignedCustomerID;
    }


}
