package com.example.trokyy.controllers.Admin.Reponse;

import com.example.trokyy.tools.MyDataBaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReponseController implements Initializable {
    @FXML
    private ComboBox<String> statusComboBox;


    @FXML
    private TextArea responseTextField;


    //private TextField responseTextField;


    private String statut_reclamation = "Resolved"; // Automatically set status to "Resolved"

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        MyDataBaseConnection connection = new MyDataBaseConnection();

        String responseText = responseTextField.getText();



        // Filtrage des mots interdits
        String[] motsInterdits = {"mot1", "mot2", "mot3"}; // Ajoutez vos mots interdits ici
        for (String motInterdit : motsInterdits) {
            responseText = responseText.replaceAll("(?i)" + motInterdit, "***");
        }


        int reclam_reponse_id = this.reclam_reponse_id;

        String insertResponseQuery = "INSERT INTO reponse (reclam_reponse_id, description, date_reponse) VALUES (?, ?,?)";

        String updateComplaintQuery = "UPDATE reclamation SET statut_reclamation = ? WHERE id = ?";


       try (Connection conn = connection.getConnection();
             PreparedStatement insertStatement = conn.prepareStatement(insertResponseQuery);
             PreparedStatement updateStatement = conn.prepareStatement(updateComplaintQuery)) {


           // Set parameters for the INSERT statement

            insertStatement.setInt(1, reclam_reponse_id);
            insertStatement.setString(2, responseText);
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
            insertStatement.setDate(3, currentDate); // Set current date
            int rowsInserted = insertStatement.executeUpdate();

           // updateStatement.setString(1, status);
           updateStatement.setString(1, statut_reclamation);
            updateStatement.setInt(2,reclam_reponse_id);

           
           

            updateStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Response added successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error adding response or updating status: " + e.getMessage());
        }


        if (responseText.isEmpty() ) {
            // Display an error message if the description is empty
            showAlert("Please fill out  the description .");
            return; // Exit the method early if the description  empty
        }
        // Afficher la notification
        showNotification("Response Added successfully");
        // Réinitialiser les champs après l'ajout
        clearFields();
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

    private int reclam_reponse_id;


    public void setComplaintId(int idReclamation) {

        this.reclam_reponse_id = idReclamation;
    }

    @FXML
    private void clearFields() {
        responseTextField.clear();
    }


    public void showNotification(String message) {
        Notifications notifications = Notifications.create();
        notifications.text(message);
        notifications.title("Success Message");
        notifications.hideAfter(Duration.seconds(30));
        notifications.darkStyle();
        notifications.position(Pos.BOTTOM_CENTER);
        notifications.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

