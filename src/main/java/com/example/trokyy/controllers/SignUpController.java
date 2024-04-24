package com.example.trokyy.controllers;

import com.example.trokyy.services.UserDao;
import com.example.trokyy.models.Utilisateur;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class SignUpController implements Initializable {
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
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField address;
    private final UserDao userDao = new UserDao();
    @FXML
    private Pane passwordParent; // Add this field to reference the parent container of the password field in your FXML

    @FXML
    private Pane confirmPasswordParent; // Add this field to reference the parent container of the confirmPassword field in your FXML
    @FXML
    private Pane FormContainer;

    private static final List<String> TUNISIAN_GOVERNORATES = Arrays.asList(
            "Ariana", "Beja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba",
            "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine",
            "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine",
            "Tozeur", "Tunis", "Zaghouan"
    );
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (FormContainer == null) {
            System.err.println("FormContainer is null. Check FXML binding.");
        } else {
            System.out.println("FormContainer is initialized successfully.");
        }


        firstName.textProperty().addListener((observable, oldValue, newValue) -> {
            validateField(firstName, newValue);
        });

        lastName.textProperty().addListener((observable, oldValue, newValue) -> {
            validateField(lastName, newValue);
        });

        useremail.textProperty().addListener((observable, oldValue, newValue) -> {
            validateField(useremail, newValue);
        });

        password.textProperty().addListener((observable, oldValue, newValue) -> {
            validateField(password, newValue);
        });

        confirmPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            validateField(confirmPassword, newValue);
        });
        phoneNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            validateField(phoneNumber, newValue);
        });
        address.textProperty().addListener((observable, oldValue, newValue) -> {
            validateField(address, newValue);
        });

    }

    @FXML
    public void registerUser(ActionEvent actionEvent) throws SQLException {
        String nom = firstName.getText().trim();
        String prenom = lastName.getText().trim();
        String email = useremail.getText();
        String mdp = password.getText();
        String confirmUserPassword = confirmPassword.getText();
        String phoneNumberText = phoneNumber.getText().trim();
        String adresse = address.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty() ||
                confirmUserPassword.isEmpty() || phoneNumberText.isEmpty() || adresse.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        int phoneNumberValue;
        try {
            phoneNumberValue = Integer.parseInt(phoneNumberText);
        } catch (NumberFormatException e) {
            // Handle invalid phone number format
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid phone number format.");
            return;
        }

        validateField(firstName, firstName.getText());
        validateField(lastName, lastName.getText());
        validateField(useremail, useremail.getText());
        validateField(password, password.getText());
        validateField(confirmPassword, confirmPassword.getText());
        validateField(phoneNumber, phoneNumber.getText());
        validateField(address, address.getText());

        ValidationResult validationResult = validateFields(nom, prenom, email, mdp, confirmUserPassword, Integer.parseInt(phoneNumberText), adresse);
        if (!validationResult.isValid()) {
            showAlert(Alert.AlertType.ERROR, "Error", validationResult.getMessage());
            return;
        }

        LocalDateTime dateInscription = LocalDateTime.now();
        Utilisateur user = new Utilisateur(nom, prenom, email, mdp,+phoneNumberValue, adresse) ; // Create user object
        try {
            System.out.println("Creating user: " + user); // Debug message
            UserDao.createUser(user, LocalDateTime.now()); // Register user with hashed password
            System.out.println("User created successfully."); // Debug message
            showNotification("Registration Successful!", "Welcome " + prenom + ". Please log in using your credentials.");
            showLoginForm(null);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to register user.");
        }

    }
    private void showNotification(String title, String message) {
        FontAwesomeIconView successIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK_CIRCLE);
        successIcon.setSize("36"); // Set icon size to 36
        successIcon.setFill(Color.web("#4CAF50")); // Set icon color to a shade of green

        Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(Duration.seconds(5)) // Hide after 5 seconds
                .position(Pos.BOTTOM_RIGHT) // Position of the notification
                .graphic(successIcon) // Custom icon for the notification
                .darkStyle() // Use dark style for the notification
                .show();
    }

    private void validateField(TextField field, String value){
        System.out.println("Validating field: " + field.getId()); // Add this line for debugging
        if (value.isEmpty()) {
            field.setStyle("-fx-border-color: red; -fx-border-radius: 30px;");
        } else {
            field.setStyle("-fx-border-color: inherit;");
        }
    }

    public static boolean isValidTunisianAddress(String address) {
        return TUNISIAN_GOVERNORATES.contains(address);
    }
    private void checkPasswordMatch(String confirmPassword) {
        String passwordText = password.getText();
        boolean match = passwordText.equals(confirmPassword);
        if (match) {
            passwordParent.setStyle("-fx-background-color: #FFFFFF;"); // White background for match
            confirmPasswordParent.setStyle("-fx-background-color: #FFFFFF;"); // White background for match
        } else {
            passwordParent.setStyle("-fx-background-color: #FF0000;"); // Red background for mismatch
            confirmPasswordParent.setStyle("-fx-background-color: #FF0000;"); // Red background for mismatch
        }
    }


    private ValidationResult validateFields(String nom, String prenom, String email, String mdp, String confirmUserPassword, int phoneNumber, String adresse){
        ValidationResult validationResult = new ValidationResult();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty() || confirmUserPassword.isEmpty() || adresse.isEmpty()) {
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

        if (nom.length() < 2 || nom.length() > 12) {
            validationResult.addMessage("Last name must be between 2 and 12 characters long.");
        }

        if (prenom.length() < 2 || prenom.length() > 12) {
            validationResult.addMessage("First name must be between 2 and 12 characters long.");
        }

        if (String.valueOf(phoneNumber).isEmpty() || String.valueOf(phoneNumber).length() != 8) {
            validationResult.addMessage("Invalid phone number format.");
        }

        if (!adresse.isEmpty()) {
            if (!isValidTunisianAddress(adresse)) {
                validationResult.addMessage("Please enter a valid Tunisian governorate.");
            }
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

    private static class ValidationResult {
        private final StringBuilder messageBuilder = new StringBuilder();
        public void addMessage(String message) {
            if (messageBuilder.length() > 0) {
                messageBuilder.append("\n");
            }
            messageBuilder.append("• ").append(message); // Add bullet points for better readability
        }
        public boolean isValid() {
            return messageBuilder.length() == 0;
        }
        public String getMessage() {
            return messageBuilder.toString() ;
        }
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
    private void showLoginForm(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/User/login1.fxml"));
            Parent loginForm = loader.load();
            if (FormContainer != null) {
                FormContainer.getChildren().setAll(loginForm);
            } else {
                System.err.println("FormContainer is null. Check FXML binding.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading login form FXML file.");
        }
    }

    public void setFormContainer(Pane container) {
        this.FormContainer = container;
    }


}
