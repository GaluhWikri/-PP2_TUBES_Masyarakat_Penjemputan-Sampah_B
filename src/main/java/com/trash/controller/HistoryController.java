package com.trash.controller;


import com.trash.db.DatabaseConnector;
import com.trash.model.HistoryModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;

public class HistoryController {

    public List<HistoryModel> getHistory() {
        List<HistoryModel> historyList = new ArrayList<>();
        String query = "SELECT r.id, c.name AS wasteCategory, r.description, r.weight, r.request_date, r.address\n" +
                "FROM requestpickup r\n" +
                "JOIN category c ON r.waste_id = c.id;";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String wasteCategory = rs.getString("wasteCategory");
                String description = rs.getString("description");
                double weight = rs.getDouble("weight");
                Date requestDate = rs.getDate("request_date");
                String address = rs.getString("address");

                HistoryModel history = new HistoryModel(id, wasteCategory, description, weight, requestDate, address);
                historyList.add(history);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historyList;
    }
}

