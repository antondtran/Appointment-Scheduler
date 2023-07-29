package com.example.appointmentscheduler.dao;

import com.example.appointmentscheduler.model.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDAO {

    public static ObservableList<Contact> fetchInfo(int id) throws SQLException {
        ObservableList<Contact> contactsData = FXCollections.observableArrayList();

        String query = "SELECT contacts.Contact_ID, appointments.Title, appointments.Type, appointments.Description, " +
                "appointments.Start, appointments.End, appointments.Customer_ID " +
                "FROM contacts " +
                "INNER JOIN appointments ON contacts.Contact_ID = appointments.Contact_ID " +
                "WHERE contacts.Contact_ID = ?";

        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the value of the parameter in the prepared statement
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Retrieve the data from the result set and create Contact objects
                    // Assuming Contact class has a constructor that takes necessary fields as parameters
                    Contact contact = new Contact(
                            resultSet.getInt("Contact_ID"),
                            resultSet.getString("Title"),
                            resultSet.getString("Type"),
                            resultSet.getString("Description"),
                            resultSet.getTimestamp("Start").toLocalDateTime(),
                            resultSet.getTimestamp("End").toLocalDateTime(),
                            resultSet.getInt("Customer_ID")
                    );
                    contactsData.add(contact);
                }
            }
        }

        return contactsData;
    }
}
