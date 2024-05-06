package com.example.trokyy.controllers;

import com.example.trokyy.services.UserDao;
import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.tools.EmailService;
import com.example.trokyy.tools.CameraHandler;
import com.example.trokyy.tools.MyDataBaseConnection;

import com.example.trokyy.tools.SessionManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.text.Text;
import org.controlsfx.control.Notifications;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import static com.example.trokyy.tools.EmailService.sendEmailWithAttachment;


public class LogInController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signUpButton;

    @FXML
    private Text registerText;
    @FXML
    private Pane Container; // Reference to the container pane in Main.fxml
    private final UserDao userDao = new UserDao();
    private int loginAttempts = 0;
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private boolean isAccountLocked = false;
    private static final long LOCKOUT_DURATION = 5 * 60 * 1000; // 5 minutes in milliseconds



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
        if (isAccountLocked) {
            showAlert("Error", "Your account is locked. Please contact support to unlock.");
            return;
        }
        try {
            Utilisateur user = userDao.getUserByEmail(email);
            int userId = user.getId(); // Get user ID
            if (user != null) {
                if (UserDao.verifyPassword(password, user.getPassword())) {
                    List<String> roles = new ArrayList<>();
                    for (String nestedRoles : user.getRoles()) {
                        roles.addAll(Collections.singleton(nestedRoles));
                        String sessionId = generateSessionId(); // Generate a unique session ID
                        SessionManager.createSession(sessionId, user);
                    }
                    System.out.println("User roles: " + roles); // Debugging statement
                    if (roles.contains("ROLE_ADMIN")) {
                        openAdminHome();
                    } else {
                        System.out.println("User is not admin."); // Debugging statement
                        openProfile(user.getId());
                    }
                } else {
                    loginAttempts++;
                    if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                        lockAccount(userId); // Pass userId to lockAccount method
                    } else {
                        showNotification("Error", "Incorrect password. Attempts remaining: " + (MAX_LOGIN_ATTEMPTS - loginAttempts));
                    }
                }
            } else {
                showAlert("Error", "User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to log in.");
        }
    }




    private void showNotification(String title, String message) {
        FontAwesomeIconView warningIcon = new FontAwesomeIconView(FontAwesomeIcon.WARNING);
        warningIcon.setSize("36");
        warningIcon.setFill(Color.web("#FFA500"));
        Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT)
                .graphic(warningIcon)
                .darkStyle()
                .show();
    }


    private void lockAccount(int userId) {
        isAccountLocked = true;
        showNotification("Error", "Your account has been temporarily locked for 5 minutes due to multiple failed login attempts.");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                isAccountLocked = false;
                loginAttempts = 0;
                showNotification("Success", "Your account has been unlocked.");
            }
        }, LOCKOUT_DURATION);
    }



    private void captureAndSendImage(String userEmail) {
        String imagePath = CameraHandler.captureImage();
        sendEmailWithAttachment(userEmail, imagePath);
        System.out.println("Image captured and email sent to user.");
    }


    private String generateSessionId() {
        return UUID.randomUUID().toString();
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

    // Method to switch to the sign-up form when the text is clicked

    @FXML
    private void showSignUpForm(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/User/signup.fxml"));
            Parent signUpForm = loader.load();
            if (Container != null) {
                //Container.getChildren().setAll(signUpForm);
                animateTransition(Container, signUpForm);

            } else {
                System.err.println("Container is null. Check FXML binding.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading sign-up form FXML file.");
        }
    }


    private void animateTransition(Pane container, Parent newContent) {
        Node currentContent = container.getChildren().isEmpty() ? null : container.getChildren().get(0);
        if (currentContent != null) {
            // Create a rotate transition for the current content (flip out)
            RotateTransition rotateOut = new RotateTransition(Duration.seconds(0.3), currentContent);
            rotateOut.setAxis(Rotate.Y_AXIS);
            rotateOut.setFromAngle(0);
            rotateOut.setToAngle(-90);
            rotateOut.setOnFinished(event -> {
                container.getChildren().setAll(newContent);
                // Create a rotate transition for the new content (flip in)
                RotateTransition rotateIn = new RotateTransition(Duration.seconds(0.3), newContent);
                rotateIn.setAxis(Rotate.Y_AXIS);
                rotateIn.setFromAngle(90);
                rotateIn.setToAngle(0);
                rotateIn.play();
            });
            rotateOut.play();
        } else {
            // If no current content, simply add the new content
            container.getChildren().setAll(newContent);
        }
    }

    public void setContainer(Pane container) {
        this.Container = container;
    }
    @FXML
    public void showRegisterStage(MouseEvent mouseEvent) {
    }
}


