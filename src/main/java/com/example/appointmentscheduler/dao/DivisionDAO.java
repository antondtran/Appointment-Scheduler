package com.example.appointmentscheduler.dao;

import com.example.appointmentscheduler.model.TotalStateCustomer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DivisionDAO {

    public List<String> fetchStateFromDatabase() throws SQLException {
        List<String> states = new ArrayList<>();

        String query = "SELECT Division FROM first_level_divisions";

        try (Connection connection = JDBCConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String state = resultSet.getString("Division");
                states.add(state);
            }
        }

        return states;
    }

    public Map<String, Integer> fetchDivisionFromDb() throws SQLException {
        Map<String, Integer> divisionMap = new HashMap<>();

        String query = "SELECT Division_ID, Division FROM first_level_divisions";

        try (Connection connection = JDBCConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int divisionId = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                divisionMap.put(divisionName, divisionId);
            }
        }

        return divisionMap;
    }

    public Map<Integer, String> fetchDivisionFromDbb() throws SQLException {
        Map<Integer, String> divisionMap = new HashMap<>();

        String query = "SELECT Division_ID, Division FROM first_level_divisions";

        try (Connection connection = JDBCConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int divisionId = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                divisionMap.put(divisionId, divisionName);
            }
        }

        return divisionMap;
    }

    public ObservableList<TotalStateCustomer> fetchDataFromDatabase() throws SQLException {
        ObservableList<TotalStateCustomer> totalStateCustomerList = FXCollections.observableArrayList();

        String query = "SELECT s.Division, COUNT(c.Customer_ID) AS total_customers " +
                "FROM first_level_divisions s " +
                "LEFT JOIN customers c ON s.Division_ID = c.Division_ID " +
                "GROUP BY s.Division";

        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String stateName = resultSet.getString("Division");
                int totalCustomers = resultSet.getInt("total_customers");
                TotalStateCustomer totalStateCustomer = new TotalStateCustomer(stateName, totalCustomers);
                totalStateCustomerList.add(totalStateCustomer);
            }
        }

        return totalStateCustomerList;
    }


}
