package com.example.appointmentscheduler.controller;

import com.example.appointmentscheduler.dao.*;
import com.example.appointmentscheduler.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;


import java.io.IOException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    @FXML
    RadioButton currentWeekBtn;

    @FXML
    RadioButton currentMonthBtn;

    @FXML
    RadioButton currentAppointmentBtn;

    private ToggleGroup toggleGroup;

    @FXML
    private Button logoutBtn;



    private AppointmentController appointmentController;
    private CustomerController customerController;
    private Customer customer;
    private Appointment appointment;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
    private AppointmentDAO appointmentDAO;
    private CustomerDAO customerDAO;
    private DivisionDAO divisionDAO;
    private ContactDAO contactDAO;


    public MainSchedulerController() throws IOException {
        // Initialize the AppointmentDAO instance here
        appointmentDAO = new AppointmentDAO();
        customerDAO = new CustomerDAO();
        divisionDAO = new DivisionDAO();
        contactDAO = new ContactDAO();
    }



    public void initialize() throws SQLException {

        toggleGroup = new ToggleGroup();
        currentWeekBtn.setToggleGroup(toggleGroup);
        currentMonthBtn.setToggleGroup(toggleGroup);
        currentAppointmentBtn.setToggleGroup(toggleGroup);
        currentAppointmentBtn.setSelected(true);
        checkAndShowAppointmentAlerts();




        appointment = new Appointment();
        customer = new Customer();


        appointmentDAO.fetchAppointmentsFromDatabase(appointment.getAppointmentList(), appointmentTableView);
        customerDAO.fetchCustomersFromDatabase(customer.getCustomerList(), customerTableView);


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


        appointmentTableView.setItems(appointment.getAppointmentList());


        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerStateColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        customerCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerTableView.setItems(customer.getCustomerList());

    }






    public void addAppointmentBtn(ActionEvent event) throws IOException {

        ObservableList<LocalTime> timeOptions = FXCollections.observableArrayList(
                LocalTime.of(8, 0),
                LocalTime.of(8, 30),
                LocalTime.of(9, 0),
                LocalTime.of(9, 30),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                LocalTime.of(11, 0),
                LocalTime.of(11, 30),
                LocalTime.of(12, 0),
                LocalTime.of(12, 30),
                LocalTime.of(13, 0),
                LocalTime.of(13, 30),
                LocalTime.of(14, 0),
                LocalTime.of(14, 30),
                LocalTime.of(15, 0),
                LocalTime.of(15, 30),
                LocalTime.of(16, 0),
                LocalTime.of(16, 30),
                LocalTime.of(17, 0),
                LocalTime.of(17, 30),
                LocalTime.of(18, 0),
                LocalTime.of(18, 30),
                LocalTime.of(19, 0),
                LocalTime.of(19, 30),
                LocalTime.of(20, 0),
                LocalTime.of(20, 30),
                LocalTime.of(21, 0),
                LocalTime.of(21, 30),
                LocalTime.of(22, 0)
        );



        // Populate the contactPicker ComboBox with names and their corresponding contact IDs
        Map<String, Integer> contactMap = new HashMap<>();
        contactMap.put("Anika Costa", 1);
        contactMap.put("Daniel Garcia", 2);
        contactMap.put("Li Lee", 3);

        Map<String, Integer> userMap = new HashMap<>();
        userMap.put("test", 1);
        userMap.put("admin", 2);






        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/appointmentscheduler/AppointmentForm.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Add Form");
        stage.setScene(scene);
        stage.show();


        TextField appointmentIDText = (TextField) loader.getNamespace().get("idTextField");
        appointmentIDText.setEditable(false);
        appointmentIDText.setPromptText("Auto Generated ID");

        ComboBox<LocalTime> startTimePicker = (ComboBox<LocalTime>) loader.getNamespace().get("startTimePicker");
        startTimePicker.setItems(timeOptions);

        ComboBox<LocalTime> endTimePicker = (ComboBox<LocalTime>) loader.getNamespace().get("endTimePicker");
        endTimePicker.setItems(timeOptions);

        ComboBox<String> contactPicker = (ComboBox) loader.getNamespace().get("contactPicker");
        contactPicker.setItems(FXCollections.observableArrayList(contactMap.keySet()));

        ComboBox<String> userPicker = (ComboBox) loader.getNamespace().get("userPicker");
        userPicker.setItems(FXCollections.observableArrayList(userMap.keySet()));


        // Get the instance of AddAppointmentController
        appointmentController = loader.getController();
        appointmentController.setContactMap(contactMap);
        appointmentController.setUserMap(userMap);

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

    public Appointment getAppointment() {
        return appointment;
    }

    public Customer getCustomer(){
        return customer;
    }

    public void addCustomerBtn(ActionEvent event) throws IOException, SQLException {

        ObservableList<String> states = FXCollections.observableArrayList(divisionDAO.fetchStateFromDatabase());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/appointmentscheduler/CustomerForm.fxml"));
        Parent root = loader.load();

        ComboBox<String> stateComboField = (ComboBox<String>) loader.getNamespace().get("stateComboField");
        stateComboField.setItems(states);
        TextField customerIDText = (TextField) loader.getNamespace().get("idTextField");
        customerIDText.setEditable(false);
        customerIDText.setPromptText("Auto Generated ID");

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Add Form");
        stage.setScene(scene);
        stage.show();

        customerController = loader.getController();
        customerController.setMainSchedulerController(this);
        customerTableView.refresh();

    }


    public void updateAppointmentBtn(ActionEvent event) throws IOException {
        ObservableList<LocalTime> timeOptions = FXCollections.observableArrayList(
                LocalTime.of(8, 0),
                LocalTime.of(8, 30),
                LocalTime.of(9, 0),
                LocalTime.of(9, 30),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                LocalTime.of(11, 0),
                LocalTime.of(11, 30),
                LocalTime.of(12, 0),
                LocalTime.of(12, 30),
                LocalTime.of(13, 0),
                LocalTime.of(13, 30),
                LocalTime.of(14, 0),
                LocalTime.of(14, 30),
                LocalTime.of(15, 0),
                LocalTime.of(15, 30),
                LocalTime.of(16, 0),
                LocalTime.of(16, 30),
                LocalTime.of(17, 0),
                LocalTime.of(17, 30),
                LocalTime.of(18, 0),
                LocalTime.of(18, 30),
                LocalTime.of(19, 0),
                LocalTime.of(19, 30),
                LocalTime.of(20, 0),
                LocalTime.of(20, 30),
                LocalTime.of(21, 0),
                LocalTime.of(21, 30),
                LocalTime.of(22, 0)
        );

        Map<String, Integer> contactMap = new HashMap<>();
        contactMap.put("Anika Costa", 1);
        contactMap.put("Daniel Garcia", 2);
        contactMap.put("Li Lee", 3);

        Map<String, Integer> userMap = new HashMap<>();
        userMap.put("test", 1);
        userMap.put("admin", 2);





        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            // Handle the case when no product is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Appointment Selected");
            alert.setHeaderText("Please select an appointment to update.");
            alert.showAndWait();
            return;
        }
        int selectedContactID = selectedAppointment.getContactID();
        // Load the FXML file for the appointment form
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/appointmentscheduler/AppointmentForm.fxml"));
        Parent root = loader.load();



        TextField appointmentIDText = (TextField) loader.getNamespace().get("idTextField");
        appointmentIDText.setEditable(false);

        ComboBox<LocalTime> startTimePicker = (ComboBox<LocalTime>) loader.getNamespace().get("startTimePicker");
        startTimePicker.setItems(timeOptions);

        ComboBox<LocalTime> endTimePicker = (ComboBox<LocalTime>) loader.getNamespace().get("endTimePicker");
        endTimePicker.setItems(timeOptions);

        ComboBox<String> contactPicker = (ComboBox) loader.getNamespace().get("contactPicker");
        contactPicker.setItems(FXCollections.observableArrayList(contactMap.keySet()));

        ComboBox<String> userPicker = (ComboBox) loader.getNamespace().get("userPicker");
        userPicker.setItems(FXCollections.observableArrayList(userMap.keySet()));



        // Get the controller for the appointment form and pass the selected appointment
        AppointmentController appointmentController = loader.getController();
        appointmentController.setMainSchedulerController(this);
        appointmentController.setContactMap(contactMap);
        appointmentController.setUserMap(userMap);
        appointmentController.setAppointmentController(selectedAppointment); // Pass the existing appointment to populate the form fields

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();


    }



    public void deleteAppointmentBtn(ActionEvent event) throws SQLException {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            // Handle the case when no product is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Appointment Selected");
            alert.setHeaderText("Please select an appointment to update.");
            alert.showAndWait();
            return;
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Do you want to proceed?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (selectedAppointment != null) {
                        try {
                            AppointmentDAO.deleteAppointment(selectedAppointment.getId());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        appointment.getAppointmentList().remove(selectedAppointment);
                        appointmentTableView.refresh();
                    }
                } else {
                    // User clicked Cancel, do nothing or handle the cancellation
                    System.out.println("Cancelled");
                }
            });
            // Delete the selected appointment from the database and the appointmentList

        }
    }


    public void deleteCustomerBtn(ActionEvent event) throws SQLException {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            // Handle the case when no product is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Customer Selected");
            alert.setHeaderText("Please select a customer to delete.");
            alert.showAndWait();
            return;
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Do you want to proceed?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (selectedCustomer != null) {

                        boolean hasAppointments = false;

                        for (Appointment appointment : appointment.getAppointmentList()){
                            if (appointment.getCustomerID() == selectedCustomer.getId()) {
                                hasAppointments = true;
                                break;
                            }
                        }

                        if (hasAppointments) {
                            Alert alert1 = new Alert(Alert.AlertType.WARNING);
                            alert1.setTitle("Cannot Delete Customer");
                            alert1.setHeaderText("The selected customer has associated appointments.");
                            alert1.setContentText("Please delete the associated appointments first.");
                            alert1.showAndWait();
                        } else {
                            try {
                                CustomerDAO.deleteCustomer(selectedCustomer.getId());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            customer.getCustomerList().remove(selectedCustomer);
                            customerTableView.refresh();
                        }
                    }
                } else {
                    // User clicked Cancel, do nothing or handle the cancellation
                    System.out.println("Cancelled");
                }
            });

        }
            // Delete the selected appointment from the database and the appointmentList






        }


    public void updateCustomerBtn(ActionEvent event) throws IOException, SQLException {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            // Handle the case when no product is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Appointment Selected");
            alert.setHeaderText("Please select an appointment to update.");
            alert.showAndWait();
            return;
        }

        ObservableList<String> states = FXCollections.observableArrayList(divisionDAO.fetchStateFromDatabase());

        // Load the FXML file for the appointment form
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/appointmentscheduler/CustomerForm.fxml"));
        Parent root = loader.load();

        ComboBox<String> stateComboField = (ComboBox<String>) loader.getNamespace().get("stateComboField");
        stateComboField.setItems(states);
        TextField customerIDText = (TextField) loader.getNamespace().get("idTextField");
        customerIDText.setEditable(false);

        // Get the controller for the appointment form and pass the selected appointment
        customerController = loader.getController();
        customerController.setMainSchedulerController(this);
        customerController.setCustomerController(selectedCustomer); // Pass the existing appointment to populate the form fields

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();

    }

    public void logoutBtn(ActionEvent event) {
        Stage stage = (Stage) logoutBtn.getScene().getWindow();
        stage.close();
    }

    public void currentWeekBtn(ActionEvent event) {
        filterAppointmentsForCurrentWeek();
    }

    private void filterAppointmentsForCurrentWeek() {
        LocalDateTime startOfWeek = LocalDateTime.now().with(DayOfWeek.MONDAY).with(LocalTime.MIN);
        LocalDateTime endOfWeek = LocalDateTime.now().with(DayOfWeek.SUNDAY).with(LocalTime.MAX);

        ObservableList<Appointment> filteredAppointments = appointment.getAppointmentList().filtered(app -> {
            LocalDateTime startDateTime = app.getStartDateTime();
            LocalDateTime endDateTime = app.getEndDateTime();
            return (startDateTime.isAfter(startOfWeek) || startDateTime.isEqual(startOfWeek))
                    && (endDateTime.isBefore(endOfWeek) || endDateTime.isEqual(endOfWeek));
        });



        appointmentTableView.setItems(filteredAppointments);
    }

    private void filterAppointmentsForCurrentMonth() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime endOfMonth = LocalDateTime.now().withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth()).with(LocalTime.MAX);

        ObservableList<Appointment> filteredAppointments = appointment.getAppointmentList().filtered(app -> {
            LocalDateTime startDateTime = app.getStartDateTime();
            LocalDateTime endDateTime = app.getEndDateTime();
            return (startDateTime.isAfter(startOfMonth) || startDateTime.isEqual(startOfMonth))
                    && (endDateTime.isBefore(endOfMonth) || endDateTime.isEqual(endOfMonth));
        });

        appointmentTableView.setItems(filteredAppointments);
    }


    public void currentMonthBtn(ActionEvent event) {
        filterAppointmentsForCurrentMonth();
    }

    public void currentAppointmentBtn(ActionEvent event) {
        appointmentTableView.setItems(appointment.getAppointmentList());
    }

    public void reportBtn(ActionEvent event) throws IOException, SQLException {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/appointmentscheduler/reports.fxml"));
        Parent root = loader.load();

        TableColumn<String, TotalAppointment> appointmentType = (TableColumn<String, TotalAppointment>) loader.getNamespace().get("appointmentTypeColumn");
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<LocalDateTime, TotalAppointment> appointmentMonth = (TableColumn<LocalDateTime, TotalAppointment>) loader.getNamespace().get("appointmentMonthColumn");
        appointmentMonth.setCellValueFactory(new PropertyValueFactory<>("month"));

        TableColumn<Integer, TotalAppointment> total = (TableColumn<Integer, TotalAppointment>) loader.getNamespace().get("totalColumn");
        total.setCellValueFactory(new PropertyValueFactory<>("total"));




        TableColumn<String, TotalStateCustomer> divisionColumn = (TableColumn<String, TotalStateCustomer>) loader.getNamespace().get("divisionColumn");
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));

        TableColumn<String, TotalStateCustomer> totalCustomerColumn = (TableColumn<String, TotalStateCustomer>) loader.getNamespace().get("totalCustomerColumn");
        totalCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("totalCustomers"));

        TableColumn<Integer, Contact> id = (TableColumn<Integer, Contact>) loader.getNamespace().get("id");
        id.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        TableColumn<String, Contact> title = (TableColumn<String, Contact>) loader.getNamespace().get("title");
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<String, Contact> type = (TableColumn<String, Contact>) loader.getNamespace().get("type");
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<String, Contact> description = (TableColumn<String, Contact>) loader.getNamespace().get("description");
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<LocalDateTime, Contact> start = (TableColumn<LocalDateTime, Contact>) loader.getNamespace().get("start");
        start.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        TableColumn<LocalDateTime, Contact> end = (TableColumn<LocalDateTime, Contact>) loader.getNamespace().get("end");
        end.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        TableColumn<Integer, Contact> customer = (TableColumn<Integer, Contact>) loader.getNamespace().get("customer");
        customer.setCellValueFactory(new PropertyValueFactory<>("customerId"));


        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        TableView<Contact> contactTableView = (TableView<Contact>) loader.getNamespace().get("contactTableView");
        TableView<TotalStateCustomer> divisionTableView = (TableView<TotalStateCustomer>) loader.getNamespace().get("divisionTableView");
        divisionTableView.setItems(divisionDAO.fetchDataFromDatabase());
        TableView<TotalAppointment> appointmentTypeTableView = (TableView<TotalAppointment>) loader.getNamespace().get("appointmentTypeTableView");
        appointmentTypeTableView.setItems(appointmentDAO.fetchTotalAppointmentFromDatabase());

        Map<String, Integer> contactMap = new HashMap<>();
        contactMap.put("Anika Costa", 1);
        contactMap.put("Daniel Garcia", 2);
        contactMap.put("Li Lee", 3);

        ComboBox<String> reportCombo = (ComboBox<String>) loader.getNamespace().get("reportCombo");
        reportCombo.setItems(FXCollections.observableArrayList(contactMap.keySet()));

        reportCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Compare the selected item (newValue) with the keys in the contactMap
                // to determine which contact was selected
                if (newValue.equals("Anika Costa")) {
                    try {
                        contactTableView.setItems(ContactDAO.fetchInfo(1));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (newValue.equals("Daniel Garcia")) {
                    try {
                        contactTableView.setItems(ContactDAO.fetchInfo(2));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else if (newValue.equals("Li Lee")) {
                    try {
                        contactTableView.setItems(ContactDAO.fetchInfo(3));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void checkAndShowAppointmentAlerts() {
        ObservableList<Appointment> appointments = appointmentDAO.fetchAppointmentsFromDB();
        LocalDateTime currentDateTime = LocalDateTime.now();

        boolean hasUpcomingAppointment = false;

        for (Appointment appointment : appointments) {
            LocalDateTime appointmentDateTime = appointment.getStartDateTime();

            // Check if the appointment is within 30 minutes from the current datetime
            long minutesUntilAppointment = currentDateTime.until(appointmentDateTime, ChronoUnit.MINUTES);
            if (minutesUntilAppointment <= 15 && minutesUntilAppointment >= 0) {
                // Display an alert to the user
                String alertMessage = "You have an appointment within 15 minutes:\n"
                        + "Appointment Type: " + appointment.getType() + "\n"
                        + "Appointment Start Time: " + appointmentDateTime;
                showAlert(Alert.AlertType.INFORMATION, "Upcoming Appointment", alertMessage);
                hasUpcomingAppointment = true;
            }
        }

        // If no upcoming appointments, display a separate alert
        if (!hasUpcomingAppointment) {
            String noAppointmentMessage = "You have no appointments within the next 15 minutes.";
            showAlert(Alert.AlertType.INFORMATION, "No Upcoming Appointments", noAppointmentMessage);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
