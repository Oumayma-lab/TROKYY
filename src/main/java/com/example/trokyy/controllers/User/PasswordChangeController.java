package com.example.trokyy.controllers.User;
import com.example.trokyy.tools.OTPGenerator;
import com.example.trokyy.tools.OTPService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.mindrot.jbcrypt.BCrypt;
import com.example.trokyy.services.UserDao;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;


public class PasswordChangeController {

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmNewPasswordField;
    private UserDao userDao = new UserDao();
    private int userId;




    /////////////////////////////////////////OTP///////////////////////////////////////
    @FXML
    private TextField otpField;

    @FXML
    private Button verifyButton;

    // OTP generated for the user
    private String generatedOTP = ""; // Declare and initialize generatedOTP variable


    @FXML
    private VBox passwordChangeForm;



/////////////////////////////////////////OTP///////////////////////////////////////

    public void setUserId(int userId) {
        this.userId = userId;
    }


    @FXML
    private void oldhandleSavePassword() {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();

        if (!isOldPasswordCorrect(oldPassword, userId)) {
            showAlert("Incorrect old password.");
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            showAlert("New passwords do not match.");
            return;
        }


        userDao.updatePassword(newPassword, userId); // Update password in DAO
        closeWindow();
    }

    private boolean isOldPasswordCorrect(String oldPassword, int userId) {
        UserDao userDao = new UserDao();
        return userDao.verifyPasswordChange(oldPassword, userId);
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) oldPasswordField.getScene().getWindow();
        stage.close();
    }





    /////////////////////////////////////////OTP///////////////////////////////////////
// Method to handle "Password Change" button click
    @FXML
    public void handlePasswordChange() throws SQLException {
        String phoneNumber = userDao.getUserPhoneNumber(userId); // Retrieve the user's phone number from the database
        OTPService.sendOTP(phoneNumber, generatedOTP);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("OTP Sent");
        alert.setHeaderText(null);
        alert.setContentText("An OTP has been sent to your phone number. Please enter the OTP to proceed.");
        alert.showAndWait();
    }



    // Method to handle "Verify OTP" button click
    @FXML
    private void handleVerifyOTP() {
        String enteredOTP = otpField.getText().trim(); // Remove leading and trailing whitespace
        System.out.println("Entered OTP: " + enteredOTP); // Debugging statement

        if (generatedOTP != null && isOTPValid(enteredOTP)) {
            // Check if generatedOTP is not null
            System.out.println("OTP verified. Proceed with password change.");
        } else {
            // OTP verification failed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid OTP");
            alert.setHeaderText(null);
            alert.setContentText("Invalid OTP. Please try again.");
            alert.showAndWait();
        }
    }

    private boolean isOTPValid(String enteredOTP) {
        // Convert both OTPs to lowercase for case-insensitive comparison
        String generatedOTPLowerCase = generatedOTP.toLowerCase();
        String enteredOTPLowerCase = enteredOTP.toLowerCase();

        // Iterate over each character in the entered OTP
        for (int i = 0; i < enteredOTPLowerCase.length(); i++) {
            char currentChar = enteredOTPLowerCase.charAt(i);
            // Check if the current character exists in the generated OTP
            if (generatedOTPLowerCase.indexOf(currentChar) != -1) {
                return true; // OTP is valid if a matching character is found
            }
        }
        return false; // No matching characters found, OTP is invalid
    }
    @FXML
    private void handleSavePassword() {
        // Your password change logic here
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmNewPassword = confirmNewPasswordField.getText();

        // Validate new password and confirm new password
        if (!newPassword.equals(confirmNewPassword)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Password Mismatch");
            alert.setHeaderText(null);
            alert.setContentText("New password and confirm password do not match. Please try again.");
            alert.showAndWait();
            return;
        }

        // Proceed with password change
        // Implement your logic to update the password in the database
        System.out.println("Password changed successfully.");
    }

}
