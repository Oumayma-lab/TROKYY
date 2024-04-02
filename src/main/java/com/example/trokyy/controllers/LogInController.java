package com.example.trokyy.controllers;
import com.example.trokyy.models.UserDao;
import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.tools.MyDataBaseConnection;
import com.example.trokyy.tools.PBKDF2PasswordHash;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class LogInController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final UserDao userDao = new UserDao();

    public LogInController() {
        Connection connection = MyDataBaseConnection.getConnection();

    }
    @FXML
    public void loginUser(ActionEvent actionEvent) {
        String email = usernameField.getText();
        String password = passwordField.getText();

        try {
            Utilisateur utilisateur = userDao.getUserByEmail(email);
            if (utilisateur != null && PBKDF2PasswordHash.verifyPassword(password, utilisateur.getPassword())) {
                System.out.println("Login successful.");
                openProfile(utilisateur.getId()); // Here you're passing the user ID
                // You can add additional logic here such as navigating to another screen
            } else {
                System.out.println("Invalid username or password.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to log in.");
            alert.showAndWait();
            // You can handle the database error here, e.g., show an error message to the user
        }
    }


    private void openHomepage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/homepage.fxml"));
            Parent root = loader.load();
            HomepageController homepageController = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Homepage");
            stage.show();
            // Close the login window
            Stage loginStage = (Stage) usernameField.getScene().getWindow();
            loginStage.close();
        } catch (IOException e) {
            System.err.println("Error loading homepage.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void openProfile(int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/profile.fxml"));
            Parent root = loader.load();
            ProfileController profileController = loader.getController();
            profileController.initialize(userId);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Profil Utilisateur");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }






}
