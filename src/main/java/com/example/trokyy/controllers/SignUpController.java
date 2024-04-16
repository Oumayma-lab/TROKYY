package com.example.trokyy.controllers;

import com.example.trokyy.services.UserDao;
import com.example.trokyy.models.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class SignUpController {
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField useremail;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Label passwordStrengthLabel;
    @FXML
    private Button registerButton;
    private final UserDao userDao = new UserDao();
    @FXML
    private Pane passwordParent; // Add this field to reference the parent container of the password field in your FXML

    @FXML
    private Pane confirmPasswordParent; // Add this field to reference the parent container of the confirmPassword field in your FXML


    @FXML
    public void registerUser(ActionEvent actionEvent) throws SQLException {
        String nom = firstName.getText().trim();
        String prenom = lastName.getText().trim();
        String email = useremail.getText();
        String mdp = password.getText();
        String confirmUserPassword = confirmPassword.getText();
        // Validate fields
        ValidationResult validationResult = validateFields(nom, prenom, email, mdp, confirmUserPassword);
        if (!validationResult.isValid()) {
            showAlert(Alert.AlertType.ERROR, "Error", validationResult.getMessage());
            return;
        }
        // Retrieve the current date/time
        LocalDateTime dateInscription = LocalDateTime.now();
        // Create a new user object

        password.textProperty().addListener((observable, oldValue, newValue) -> {
            checkPasswordStrength(newValue);
        });
        // Add event listener to the confirmPassword field to check password match
        confirmPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            checkPasswordMatch(newValue);
        });

        Utilisateur user = new Utilisateur(nom, prenom, email, mdp); // Create user object

        try {
            UserDao.createUser(user, LocalDateTime.now()); // Register user with hashed password
            showAlert(Alert.AlertType.INFORMATION, "Success", "User registered successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to register user.");
        }

        showInfoMessage("Success", "Registration Successful!", "Welcome " + nom);
    }

    private void checkPasswordMatch(String confirmPassword) {
        // Check if passwords match
        String passwordText = password.getText();
        boolean match = passwordText.equals(confirmPassword);

        // Update parent container styles based on match status
        if (match) {
            passwordParent.setStyle("-fx-background-color: #FFFFFF;"); // White background for match
            confirmPasswordParent.setStyle("-fx-background-color: #FFFFFF;"); // White background for match
        } else {
            passwordParent.setStyle("-fx-background-color: #FF0000;"); // Red background for mismatch
            confirmPasswordParent.setStyle("-fx-background-color: #FF0000;"); // Red background for mismatch
        }
    }


    private void checkPasswordStrength(String password) {
        int strength = calculatePasswordStrength(password);
        if (strength == 0) {
            passwordStrengthLabel.setText("Password Strength: Very Weak");
            passwordStrengthLabel.setStyle("-fx-text-fill: #FF0000; -fx-font-weight: bold;");
        } else if (strength == 1) {
            passwordStrengthLabel.setText("Password Strength: Weak");
            passwordStrengthLabel.setStyle("-fx-text-fill: #FF6347; -fx-font-weight: bold;");
        } else if (strength == 2) {
            passwordStrengthLabel.setText("Password Strength: Moderate");
            passwordStrengthLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold;");
        } else if (strength == 3) {
            passwordStrengthLabel.setText("Password Strength: Strong");
            passwordStrengthLabel.setStyle("-fx-text-fill: #32CD32; -fx-font-weight: bold;");
        } else if (strength == 4) {
            passwordStrengthLabel.setText("Password Strength: Very Strong");
            passwordStrengthLabel.setStyle("-fx-text-fill: #008000; -fx-font-weight: bold;");
        }
        

    }



    private ValidationResult validateFields(String nom, String prenom, String email, String mdp, String confirmUserPassword) {
        ValidationResult validationResult = new ValidationResult();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty() || confirmUserPassword.isEmpty()) {
            validationResult.addMessage("Please fill in all fields.");
        }

        if (!isValidEmail(email)) {
            validationResult.addMessage("Invalid email. Please enter a valid address.");
        }

        if (userDao.emailExists(email)) {
            validationResult.addMessage("Email is already registered.");
        }

        if (!isValidPassword(mdp)) {
            validationResult.addMessage("Password must be at least 8 characters long and contain at least one digit.");
        }

        if (!mdp.equals(confirmUserPassword)) {
            validationResult.addMessage("Passwords do not match.");
        }

        return validationResult;
    }

    private boolean isValidEmail(String email) {
        // Check if email is valid format
        String emailRegex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        // Password pattern: at least 8 characters long and contain at least one digit
        String passwordRegex = "^(?=.*[0-9]).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoMessage(String title, String headerText, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handlePasswordChange() {
        String passwordText = password.getText();
        String confirmPasswordText = confirmPassword.getText();

        boolean match = passwordText.equals(confirmPasswordText);
        boolean complexity = isValidPassword(passwordText);

        if (complexity) {
            password.setStyle("-fx-background-color: #FFFFFF;"); // White background for complex password
            confirmPassword.setStyle("-fx-background-color: #FFFFFF;"); // White background for complex password
        } else {
            password.setStyle("-fx-background-color: #FF0000;"); // Red background for weak password
            confirmPassword.setStyle("-fx-background-color: #FF0000;"); // Red background for weak password
        }

        if (match) {
            password.setStyle("-fx-background-color: #FFFFFF;"); // White background for match
            confirmPassword.setStyle("-fx-background-color: #FFFFFF;"); // White background for match
        } else {
            password.setStyle("-fx-background-color: #FF0000;"); // Red background for mismatch
            confirmPassword.setStyle("-fx-background-color: #FF0000;"); // Red background for mismatch
        }
    }


    private static class ValidationResult {
        private final StringBuilder messageBuilder = new StringBuilder();

        public void addMessage(String message) {
            if (messageBuilder.length() > 0) {
                messageBuilder.append("\n");
            }
            messageBuilder.append(message);
        }

        public boolean isValid() {
            return messageBuilder.length() == 0;
        }

        public String getMessage() {
            return messageBuilder.toString();
        }
    }


    @FXML
    public void showLoginStage(MouseEvent mouseEvent) {
        try {
            // Load the FXML file for the login page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/User/LogIn.fxml"));
            Parent loginRoot = loader.load();
            // Set up a new stage for the login page
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(loginRoot));
            loginStage.setTitle("Log In");
            loginStage.show();
            // Close the current stage (signup stage)
            Stage signupStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            signupStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private int calculatePasswordStrength(String password) {
        String lowercaseRegex = ".*[a-z].*";
        String uppercaseRegex = ".*[A-Z].*";
        String digitRegex = ".*\\d.*";
        String specialCharRegex = ".*[^a-zA-Z0-9].*";
        int strength = 0;
        if (password.matches(lowercaseRegex)) {
            strength++;
        }
        if (password.matches(uppercaseRegex)) {
            strength++;
        }
        if (password.matches(digitRegex)) {
            strength++;
        }
        if (password.matches(specialCharRegex)) {
            strength++;
        }
        if (password.length() >= 8) {
            strength++;
        }
        return strength;
    }


}
