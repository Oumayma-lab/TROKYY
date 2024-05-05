package com.example.trokyy.tools;
import java.security.SecureRandom;

import java.security.SecureRandom;

public class OTPGenerator {

    // Generate OTP of specified length
    public static String generateOTP(int length) {
        // Define characters allowed in OTP
        String allowedChars = "0123456789";

        // Initialize secure random number generator
        SecureRandom random = new SecureRandom();

        // StringBuilder to store generated OTP
        StringBuilder otp = new StringBuilder();

        // Generate random OTP characters
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            otp.append(allowedChars.charAt(index));
        }

        return otp.toString();
    }
}