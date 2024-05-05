package com.example.trokyy.controllers;


import com.example.trokyy.services.UserDao;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class HomepageController {
    @FXML
    private LineChart<Number, Number> userChart;
    @FXML
    private StackPane rootPane; // Reference to the root pane
    @FXML
    private PieChart regionPieChart;

    // Map to store the count of users per region
    private Map<String, Integer> usersPerRegion = new HashMap<>();

    private final UserDao userDao = new UserDao();

    // Define Tunisian governorates
    private static final List<String> TUNISIAN_GOVERNORATES = Arrays.asList(
            "Ariana", "Beja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba",
            "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine",
            "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine",
            "Tozeur", "Tunis", "Zaghouan"
    );

    // Sample data representing the number of users per governorate (replace with actual data)
    private static final Map<String, Integer> USERS_PER_GOVERNORATE = new HashMap<>();
 
    public void initialize() {


        if (userDao == null) {
            System.err.println("UserDao is not initialized!");
            return;
        }
        
        userChart.setStyle("-fx-background-color: transparent;");
        userChart.setHorizontalGridLinesVisible(false);
        userChart.setVerticalGridLinesVisible(false);

        // Set axis labels
        ((NumberAxis) userChart.getXAxis()).setLabel("Number of Users");
        ((NumberAxis) userChart.getYAxis()).setLabel("Number of Days");

        // Populate chart with sample data (You can replace this with your actual data)
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("User Evolution");

        series.getData().add(new XYChart.Data<>(100, 1));
        series.getData().add(new XYChart.Data<>(150, 2));
        series.getData().add(new XYChart.Data<>(200, 3));
        series.getData().add(new XYChart.Data<>(250, 4));

        userChart.getData().add(series);



        fetchDataFromDatabase();

        // Create pie chart data
        for (String region : usersPerRegion.keySet()) {
            regionPieChart.getData().add(new PieChart.Data(region, usersPerRegion.get(region)));
        }

        // Set color for each slice of the pie chart
        int colorIndex = 0;
        for (PieChart.Data data : regionPieChart.getData()) {
            data.getNode().setStyle("-fx-pie-color: #" + getColorHex(colorIndex));
            colorIndex++;
        }

    }

    private void fetchDataFromDatabase() {
        try {
            List<String> userAddresses = userDao.getUserAddresses();
            for (String address : userAddresses) {
                String region = extractRegionFromAddress(address);
                usersPerRegion.put(region, usersPerRegion.getOrDefault(region, 0) + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String extractRegionFromAddress(String address) {
        if (address == null) {
            return ""; // or handle null address appropriately
        }
        String[] parts = address.split(",");
        if (parts.length > 0) {
            return parts[0].trim();
        } else {
            return ""; // or handle empty address appropriately
        }
    }


    private String getColorHex(int index) {
        switch (index % 10) {
            case 0: return "FF5733";
            case 1: return "FFBD33";
            case 2: return "F9FF33";
            case 3: return "83FF33";
            case 4: return "33FFA5";
            case 5: return "33FFFC";
            case 6: return "3370FF";
            case 7: return "6B33FF";
            case 8: return "FF33E4";
            case 9: return "FF3363";
            default: return "000000";
        }
    }

}
