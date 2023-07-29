package com.example.appointmentscheduler.dao;

import com.example.appointmentscheduler.model.Appointment;
import com.example.appointmentscheduler.model.Customer;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Map;

public class CustomerDAO {


    public static int getLastUsedCustomerId() {
        int lastUsedCustomerId = 0;

        String query = "SELECT MAX(Customer_ID) FROM customers";

        try (Connection connection = JDBCConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                lastUsedCustomerId = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastUsedCustomerId;
    }

    public void fetchCustomersFromDatabase(ObservableList<Customer> customers, TableView<Customer> customerTableView) {
        try (Connection connection = JDBCConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM customers")) {

            customers.clear();

            DivisionDAO divisionDAO = new DivisionDAO();
            Map<Integer, String> divisionMap = divisionDAO.fetchDivisionFromDbb(); // Fetch division data from the database

            while (resultSet.next()) {
                int id = resultSet.getInt("Customer_ID");
                String name = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                int divisionId = resultSet.getInt("Division_ID");
                String postalCode = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");

                // Retrieve the division name from the fetched divisionMap based on the division ID
                String divisionName = divisionMap.getOrDefault(divisionId, "Unknown");

                Customer customer = new Customer(id, name, address, phone, divisionId, postalCode);
                customer.setDivisionName(divisionName); // Set the division name to the Customer object

                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set the fetched data to the TableView
        customerTableView.setItems(customers);
    }




    public static void insertCustomer(Customer customer) throws SQLException {
        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO customers (Customer_ID, Customer_Name, Address, Division_ID, Phone, Postal_Code, Create_Date, Created_By) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            Timestamp createDate = Timestamp.valueOf(customer.getCreatedDate());
            preparedStatement.setInt(1, customer.getId());
            preparedStatement.setString(2, customer.getName());
            preparedStatement.setString(3, customer.getAddress());
            preparedStatement.setInt(4, customer.getDivisionID());
            preparedStatement.setString(5, customer.getPhoneNumber());
            preparedStatement.setString(6, customer.getPostalCode());
            preparedStatement.setTimestamp(7, createDate);
            preparedStatement.setString(8, customer.getCreatedBy());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static void deleteCustomer(int customerID) throws SQLException {
        String query = "DELETE FROM customers WHERE Customer_ID = ?";

        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, customerID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void updateCustomer(Customer existingCustomer) throws SQLException {
        // Get the database connection using the JDBCConnection class or any other method you have for establishing a connection.
        Connection connection = JDBCConnection.getConnection();
        Timestamp lastUpdate = Timestamp.valueOf(existingCustomer.getLastUpdate());
        // SQL update statement with placeholders for the values to be updated
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Phone = ?, Postal_Code = ?, Last_Update = ?, Last_Updated_By = ? WHERE Customer_ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Set the values of the placeholders using the attributes of the existingAppointment object
            statement.setString(1, existingCustomer.getName());
            statement.setString(2, existingCustomer.getAddress());
            statement.setString(3, existingCustomer.getPhoneNumber());
            statement.setString(4, existingCustomer.getPostalCode());
            statement.setTimestamp(5, lastUpdate);
            statement.setString(6, existingCustomer.getLastUpdatedBy());
            statement.setInt(7, existingCustomer.getId());


            // Execute the SQL update statement
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Customer updated successfully.");
            } else {
                System.out.println("Failed to update customer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the database resources
            connection.close();
        }
    }

    public static Customer getCustomerById(int id) {
        Customer customer = null;
        String query = "SELECT * FROM customers WHERE Customer_ID = ?";

        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int customerID = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                String phone = resultSet.getString("Phone");
                int division = resultSet.getInt("Division_ID");
                String postalCode = resultSet.getString("Postal_Code");



                // Create the Appointment object
                customer = new Customer(customerID, customerName, address, phone, division, postalCode);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

    public static boolean doesCustomerExist(int customerID) {
        String query = "SELECT COUNT(*) FROM customers WHERE Customer_ID = ?";

        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, customerID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
