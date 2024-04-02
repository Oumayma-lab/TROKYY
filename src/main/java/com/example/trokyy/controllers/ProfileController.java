package com.example.trokyy.controllers;
import com.example.trokyy.models.UserDao;
import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.tools.MyDataBaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Modality;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ProfileController {
    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label telLabel;
    @FXML
    private Label adresseLabel;
    @FXML
    private Label prenomLabel;
    @FXML
    private Button editButton;

    private UserDao userDao = new UserDao();
    private int userId;

    private Stage stage;

    public ProfileController() {
        Connection connection = MyDataBaseConnection.getConnection();


    }
    public void initialize(int userId) {
        try {
            this.userId = userId;
            Utilisateur utilisateur = userDao.getUserById(userId);
            if (utilisateur != null) {
                nameLabel.setText(utilisateur.getNom());
                emailLabel.setText(utilisateur.getEmail());
                usernameLabel.setText(utilisateur.getUsername());
                addressLabel.setText(utilisateur.getAdresse());
                telLabel.setText(String.valueOf(utilisateur.getTelephone()));
            } else {
                System.err.println("Utilisateur introuvable pour l'ID : " + userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void editProfile() throws IOException, SQLException {
        // Load the edit profile view
        // Assuming you have an editProfile.fxml and corresponding controller EditProfileController
        // You can replace "editProfile.fxml" with the actual path to your edit profile view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/editProfile.fxml"));
        Parent root = loader.load();
        EditProfileController editProfileController = loader.getController();
        editProfileController.initialize(userId); // Pass user ID to edit profile controller

        Stage editStage = new Stage();
        editStage.setScene(new Scene(root));
        editStage.setTitle("Modifier le profil");
        editStage.initModality(Modality.WINDOW_MODAL);
        editStage.initOwner(stage);
        editStage.showAndWait();
        // Close the profile view window
        initialize(userId);

        ((Stage) editButton.getScene().getWindow()).close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
