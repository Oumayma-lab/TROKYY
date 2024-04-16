package com.example.trokyy.controllers;

import com.example.trokyy.services.UserDao;
import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.tools.MyDataBaseConnection;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.text.Text;
import org.controlsfx.control.Notifications;

public class LogInController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signUpButton;

    @FXML
    private Text registerText;
    private final UserDao userDao = new UserDao();

    public LogInController() {
        Connection connection = MyDataBaseConnection.getConnection();
    }


    @FXML
    public void loginUser() throws SQLException {
        String email = usernameField.getText();
        String password = passwordField.getText();

        // Check if user is banned
        if (userDao.isUserBanned(email, password)) {
            showBlockedMessage();
            return; // Stop login process if user is banned
        }
        
        
        if (!isValidEmail(email)) {
            showAlert("Error", "Please enter a valid email address.");
            usernameField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            showAlert("Error", "Please enter your password.");
            passwordField.requestFocus();
            return;
        }

        try {
            Utilisateur user = userDao.getUserByEmail(email);
            if (user != null) {
                if (UserDao.verifyPassword(password, user.getPassword())) {
                    if (user.getRoles() != null && user.getRoles().contains("ROLE_ADMIN")) {
                        openAdminHome();
                    } else {
                        System.out.println("User is not admin."); // Debugging statement
                        openProfile(user.getId());
                    }
                } else {
                    showAlert("Error", "Invalid username or password.");
                }
            } else {
                showAlert("Error", "User not found.");
            }
        }  catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to log in.");
        }
    }

    private void openAdminHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/Backoffice/AdminMain.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Home");
            stage.show();
            closeLoginStage();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showBlockedMessage() {
        Notifications.create()
                .title("Access Denied")
                .text("You have been blocked by the administrator. Please contact support for assistance.")
                .darkStyle() // Use dark style for the notification
                .showError(); // Show an error notification style
    }

    private boolean isValidEmail(String email) {
        // Simple email validation using regex
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void openProfile(int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/User/Profile.fxml"));
            Parent root = loader.load();
            ProfileController profileController = loader.getController();
            profileController.initialize(userId);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Profil Utilisateur");
            stage.show();
            closeLoginStage();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeLoginStage() {
        Stage loginStage = (Stage) usernameField.getScene().getWindow();
        loginStage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showRegisterStage(MouseEvent mouseEvent) {
        try {
            // Load the FXML file for the signup page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/User/SignUp.fxml"));
            Parent signupRoot = loader.load();
            Stage signupStage = new Stage();
            signupStage.setScene(new Scene(signupRoot));
            signupStage.setTitle("Sign Up");
            signupStage.show();
            closeLoginStage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


