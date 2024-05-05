package com.example.trokyy.controllers.User;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;


import java.security.SecureRandom;
import java.util.Base64;

public class OTPController {


    private static final int OTP_LENGTH = 6;
    private static final String OTP_CHARS = "0123456789";

    @FXML
    private TextField otpField;

    private String generatedOTP;

    @FXML
    private void handleGenerateOTP() {
        // Generate a new OTP
        generatedOTP = generateOTP();
        // Display the generated OTP
        showAlert("Generated OTP: " + generatedOTP);
    }


    @FXML
    private void handleVerifyOTP() {
        // Get the entered OTP
        String enteredOTP = otpField.getText();
        // Verify the entered OTP
        if (verifyOTP(enteredOTP)) {
            showAlert("OTP verification successful.");
        } else {
            showAlert("Invalid OTP. Please try again.");
        }
    }

    private String generateOTP() {
        StringBuilder otp = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(OTP_CHARS.charAt(random.nextInt(OTP_CHARS.length())));
        }
        return otp.toString();
    }

    private boolean verifyOTP(String enteredOTP) {
        return enteredOTP.equals(generatedOTP);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("OTP Verification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
