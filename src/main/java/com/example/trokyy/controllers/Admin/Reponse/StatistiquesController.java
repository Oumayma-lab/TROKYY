package com.example.trokyy.controllers.Admin.Reponse;

import com.example.trokyy.tools.MyDataBaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StatistiquesController implements Initializable {
    @FXML
    private PieChart complaintsPieChart;
    @FXML

    public void initialize(URL location, ResourceBundle resources) {
        populateComplaintsPieChart();

    }
    private void populateComplaintsPieChart() {
        int resolvedCount = 0;
        int pendingCount = 0;
        int inProgressCount = 0;
        MyDataBaseConnection connection = new MyDataBaseConnection();
        String query = "SELECT COUNT(*) AS count, status FROM reclamation GROUP BY status";

        try (Connection conn = MyDataBaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String status = resultSet.getString("status");
                    int count = resultSet.getInt("count");
                    switch (status) {
                        case "Resolved":
                            resolvedCount += count;
                            break;
                        case "Sent":
                            pendingCount += count;
                            break;
                        case "In Progress":
                            inProgressCount += count;
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PieChart.Data resolvedData = new PieChart.Data("Resolved", resolvedCount);
        PieChart.Data pendingData = new PieChart.Data("Pending", pendingCount);
        PieChart.Data inProgressData = new PieChart.Data("In Progress", inProgressCount);
        complaintsPieChart.setTitle("Complaint Status's statistics");

        complaintsPieChart.getData().addAll(resolvedData, pendingData, inProgressData);
    }


    public void switchToAnotherView2(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationViewComplaints.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


