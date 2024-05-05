package com.example.trokyy.controllers;
import com.example.trokyy.controllers.User.PasswordChangeController;
import com.example.trokyy.services.UserDao;
import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.tools.MyDataBaseConnection;
import com.example.trokyy.tools.OTPService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

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
    @FXML
    private Circle clipCircle;
    private UserDao userDao = new UserDao();
    private int userId;
    private Stage stage;
    @FXML
    private Pane profilePane;
    @FXML
    private ImageView profileImage;
    @FXML
    private ImageView updatePhotoImageView;
    private Stage profileStage;
    private Stage mainStage;


    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmNewPasswordField;

    @FXML
    private Label errorLabel;

    @FXML
    private PasswordChangeController passwordChangeController;




    public ProfileController() {
        Connection connection = MyDataBaseConnection.getConnection();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void initialize(int userId) {
        this.profileStage = profileStage;
        this.mainStage = mainStage;

        try {
            this.userId = userId;
            Utilisateur utilisateur = userDao.getUserById(userId);
            if (utilisateur != null) {
                nameLabel.setText(utilisateur.getNom());
                emailLabel.setText(utilisateur.getEmail());
                usernameLabel.setText(utilisateur.getUsername());
                addressLabel.setText(utilisateur.getAdresse());
                telLabel.setText(String.valueOf(utilisateur.getTelephone()));
                String profileImageUrl = userDao.getProfilePhoto(userId);
                if (profileImageUrl != null) {
                    Image image = new Image(profileImageUrl);
                    profileImage.setImage(image);
                    profileImage.setClip(new Circle(90, 90, 90));
                }
            } else {
                System.err.println("Utilisateur introuvable pour l'ID : " + userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleDeleteAccount() {
        // Create and show delete account confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Account Deletion");
        alert.setHeaderText("Are you sure you want to delete your account?");
        alert.setContentText("Don't leave (╥﹏╥)");

        // Customize buttons
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        ButtonType deleteButtonType = new ButtonType("Delete Account", ButtonType.OK.getButtonData());
        alert.getButtonTypes().setAll(cancelButtonType, deleteButtonType);

        // Show dialog and handle user's choice
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == deleteButtonType) {
            try {
                // Delete account
                userDao.deleteUserAccount(userId);
                // Load main FXML and set it as scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/user/Main.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.setTitle("Troky");
                stage.setScene(scene);
                stage.show();

            } catch (SQLException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("An error occurred while deleting your account.");
                errorAlert.showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void handleChangePhoto() {
        // Open file chooser dialog
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Photo");
        File selectedFile = fileChooser.showOpenDialog(updatePhotoImageView.getScene().getWindow());
        if (selectedFile != null) {
            try {
                userDao.updateProfilePhoto(userId, selectedFile.toURI().toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            profileImage.setImage(new Image(selectedFile.toURI().toString()));
            profileImage.setClip(new Circle(90, 90, 90));
        }
    }


    // You need to implement a method to save the photo to a location accessible by your application
    private String savePhotoToFileSystem(File file) {
        // Implement your logic to save the photo to a directory
        return file.getAbsolutePath(); // For simplicity, returning absolute path as photo URL
    }

    @FXML
    private void editProfile() throws IOException, SQLException {

        // Load the edit profile view
        // Assuming you have an editProfile.fxml and corresponding controller EditProfileController
        // You can replace "editProfile.fxml" with the actual path to your edit profile view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/User/editProfile.fxml"));
        Parent root = loader.load();
        EditProfileController editProfileController = loader.getController();
        editProfileController.initialize(userId); // Pass user ID to edit profile controller
        initialize(userId);

        Stage editStage = new Stage();
        editStage.setScene(new Scene(root));
        editStage.setTitle("Profile Update");
        editStage.initModality(Modality.WINDOW_MODAL);
        editStage.initOwner(stage);
        editStage.showAndWait();
        // Close the profile view window

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleChangePassword() {
        try {
            String phoneNumber = userDao.getUserPhoneNumber(userId); // Retrieve the user's phone number from the database
            if (phoneNumber != null) {
                // Generate OTP and send it via Twilio to the user's phone number
                 String generatedOTP = OTPService.generateOTP(phoneNumber);
                OTPService.sendOTP(phoneNumber, generatedOTP);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("OTP Sent");
                alert.setHeaderText(null);
                alert.setContentText("An OTP has been sent to your phone number. Please enter the OTP to proceed.");
                alert.showAndWait();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/User/password_change.fxml"));
                Parent root = fxmlLoader.load();
                PasswordChangeController passwordChangeController = fxmlLoader.getController();
                passwordChangeController.setUserId(userId); // Pass the user ID to PasswordChangeController
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Change Password");
                stage.showAndWait();
            } else {
                System.out.println("User's phone number not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }
    // Method to handle "Verify OTP" button click

}
