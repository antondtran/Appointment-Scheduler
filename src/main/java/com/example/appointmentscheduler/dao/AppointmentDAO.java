package com.example.appointmentscheduler.dao;

import com.example.appointmentscheduler.model.Appointment;
import com.example.appointmentscheduler.model.Customer;
import com.example.appointmentscheduler.model.TotalAppointment;
import com.example.appointmentscheduler.model.TotalStateCustomer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppointmentDAO {
    public static void insertAppointment(Appointment appointment) throws SQLException {
        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO appointments (Appointment_ID, Title, Description, Type, Location, Start, End, Create_Date, Created_By, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            LocalDateTime startDateTime = appointment.getStartDateTime();
            LocalDateTime endDateTime = appointment.getEndDateTime();
            Timestamp createDate = Timestamp.valueOf(appointment.getCreatedDate());

// Convert LocalDateTime to Timestamp
            Timestamp timestampStart = (startDateTime != null) ? Timestamp.valueOf(startDateTime) : null;
            Timestamp timestampEnd = (endDateTime != null) ? Timestamp.valueOf(endDateTime) : null;


            preparedStatement.setInt(1, appointment.getId());
            preparedStatement.setString(2, appointment.getTitle());
            preparedStatement.setString(3, appointment.getDescription());
            preparedStatement.setString(4, appointment.getType());
            preparedStatement.setString(5, appointment.getLocation());
            preparedStatement.setTimestamp(6, timestampStart);
            preparedStatement.setTimestamp(7, timestampEnd);
            preparedStatement.setTimestamp(8, createDate);
            preparedStatement.setString(9, appointment.getCreatedBy());
            preparedStatement.setInt(10, appointment.getCustomerID());
            preparedStatement.setInt(11, appointment.getUserID());
            preparedStatement.setInt(12, appointment.getContactID());


            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateAppointment(Appointment existingAppointment) throws SQLException {
        // Get the database connection using the JDBCConnection class or any other method you have for establishing a connection.
        Connection connection = JDBCConnection.getConnection();

        Timestamp lastUpdate = Timestamp.valueOf(existingAppointment.getLastUpdate());

        // SQL update statement with placeholders for the values to be updated
        String sql = "UPDATE appointments SET Title = ?, Type = ?, Description = ?, Location = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, User_ID = ?, Customer_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values of the placeholders using the attributes of the existingAppointment object
            statement.setString(1, existingAppointment.getTitle());
            statement.setString(2, existingAppointment.getType());
            statement.setString(3, existingAppointment.getDescription());
            statement.setString(4, existingAppointment.getLocation());
            statement.setTimestamp(5, Timestamp.valueOf(existingAppointment.getStartDateTime()));
            statement.setTimestamp(6, Timestamp.valueOf(existingAppointment.getEndDateTime()));
            statement.setTimestamp(7, lastUpdate);
            statement.setString(8, existingAppointment.getLastUpdatedBy());
            statement.setInt(9, existingAppointment.getUserID());
            statement.setInt(10, existingAppointment.getCustomerID());
            statement.setInt(11, existingAppointment.getContactID());
            statement.setInt(12, existingAppointment.getId());

            // Execute the SQL update statement
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Appointment updated successfully.");
            } else {
                System.out.println("Failed to update appointment.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the database resources
            connection.close();
        }
    }

    public static int getLastUsedAppointmentId() {
        int lastUsedAppointmentId = 0;

        try (Connection connection = JDBCConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT MAX(Appointment_ID) AS max_id FROM appointments")) {

            if (resultSet.next()) {
                lastUsedAppointmentId = resultSet.getInt("max_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastUsedAppointmentId;
    }


    public void fetchAppointmentsFromDatabase(ObservableList<Appointment> appointments, TableView<Appointment> appointmentTableView) {
        // Use JDBC or any other data access method to fetch appointment data from the database
        // For example, using JDBC:

        try {
            Connection connection = JDBCConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM appointments");

            while (resultSet.next()) {
                int id = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String type = resultSet.getString("Type");
                String location = resultSet.getString("Location");
                Timestamp timestamp = resultSet.getTimestamp("Start");
                Timestamp timestamp2 = resultSet.getTimestamp("End");
                Timestamp timestamp3 = resultSet.getTimestamp("Create_Date");


                // Convert the Timestamp to a LocalDateTime
                LocalDateTime startDateTime = (timestamp != null) ? timestamp.toLocalDateTime() : null;
                LocalDateTime endDateTime = (timestamp2 != null) ? timestamp2.toLocalDateTime() : null;
                LocalDateTime createDate = (timestamp3 != null) ? timestamp3.toLocalDateTime() : null;
                String createdBy = resultSet.getString("Created_By");

                int userID = resultSet.getInt("User_ID");
                int customerID = resultSet.getInt("Customer_ID");
                int contactID = resultSet.getInt("Contact_ID");


                // Retrieve other data fields
                // ...

                // Create an Appointment object and add it to the appointments list
                Appointment appointment = new Appointment(id, title, type, description, location, startDateTime, endDateTime, createDate, createdBy, contactID, userID, customerID);
                appointments.add(appointment);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Set the fetched data to the TableView
        appointmentTableView.setItems(appointments);
    }


    public static Appointment getAppointmentById(int id) {
        Appointment appointment = null;
        String query = "SELECT * FROM appointments WHERE Appointment_ID = ?";

        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int appointmentId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String type = resultSet.getString("Type");
                String location = resultSet.getString("Location");
                Timestamp startTimestamp = resultSet.getTimestamp("Start");
                Timestamp endTimestamp = resultSet.getTimestamp("End");
                Timestamp createTimestamp = resultSet.getTimestamp("Create_Date");
                String createdBy = resultSet.getString("Created_By");
                int userId = resultSet.getInt("User_ID");
                int customerId = resultSet.getInt("Customer_ID");
                int contactId = resultSet.getInt("Contact_ID");

                // Convert the Timestamp to LocalDateTime
                LocalDateTime startDateTime = (startTimestamp != null) ? startTimestamp.toLocalDateTime() : null;
                LocalDateTime endDateTime = (endTimestamp != null) ? endTimestamp.toLocalDateTime() : null;
                LocalDateTime createDateTime = (createTimestamp != null) ? createTimestamp.toLocalDateTime() : null;

                // Create the Appointment object
                appointment = new Appointment(appointmentId, title, type, description, location, startDateTime,
                        endDateTime, createDateTime, createdBy, userId, customerId, contactId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointment;
    }

    public static void deleteAppointment(int appointmentId) throws SQLException {
        String query = "DELETE FROM appointments WHERE Appointment_ID = ?";

        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, appointmentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ObservableList<TotalAppointment> fetchTotalAppointmentFromDatabase() throws SQLException {
        ObservableList<TotalAppointment> totalAppointmentList = FXCollections.observableArrayList();

        String query = "SELECT COUNT(a.Appointment_ID) AS total_appointments, a.Type, a.Start " +
                "FROM appointments a GROUP BY a.Type, a.Start";

        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String appointmentType = resultSet.getString("Type");
                int totalAppointments = resultSet.getInt("total_appointments");
                LocalDateTime appointmentMonth = resultSet.getTimestamp("Start").toLocalDateTime();
                TotalAppointment totalAppointment = new TotalAppointment(totalAppointments, appointmentType, appointmentMonth);
                totalAppointmentList.add(totalAppointment);
            }
        }

        return totalAppointmentList;
    }

    public ObservableList<Appointment> fetchAppointmentsFromDB() {
        // Use JDBC or any other data access method to fetch appointment data from the database
        // For example, using JDBC:

        ObservableList<Appointment> appointmentObservableList = FXCollections.observableArrayList();

        try {
            Connection connection = JDBCConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM appointments");

            while (resultSet.next()) {
                int id = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String type = resultSet.getString("Type");
                String location = resultSet.getString("Location");
                Timestamp timestamp = resultSet.getTimestamp("Start");
                Timestamp timestamp2 = resultSet.getTimestamp("End");
                Timestamp timestamp3 = resultSet.getTimestamp("Create_Date");


                // Convert the Timestamp to a LocalDateTime
                LocalDateTime startDateTime = (timestamp != null) ? timestamp.toLocalDateTime() : null;
                LocalDateTime endDateTime = (timestamp2 != null) ? timestamp2.toLocalDateTime() : null;
                LocalDateTime createDate = (timestamp3 != null) ? timestamp3.toLocalDateTime() : null;
                String createdBy = resultSet.getString("Created_By");

                int userID = resultSet.getInt("User_ID");
                int customerID = resultSet.getInt("Customer_ID");
                int contactID = resultSet.getInt("Contact_ID");


                // Retrieve other data fields
                // ...

                // Create an Appointment object and add it to the appointments list
                Appointment appointment = new Appointment(id, title, type, description, location, startDateTime, endDateTime, createDate, createdBy, contactID, userID, customerID);
                appointmentObservableList.add(appointment);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentObservableList;
    }

}
