package com.example.trokyy.tools;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.Random;
public class OTPService {
    public static final String ACCOUNT_SID = "AC7ecd8fd4d6022e8447b217f0fbf6fe86";
    public static final String AUTH_TOKEN = "6c97cd8adc3105e2fb47ef64f48409bd";
    public static final String TWILIO_PHONE_NUMBER = "+12607584297";


    public static String generateOTP(String phoneNumber) {
        // Generate random 6-digit OTP
        String otp = generateRandomOTP();


        // Send OTP via Twilio
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        //       Message message = Message.creator(
                //                      new PhoneNumber(formatPhoneNumber(phoneNumber)), // Destination phone number in international format
                //                    new PhoneNumber(TWILIO_PHONE_NUMBER), // Twilio phone number
                //                    "Hello Trockino ദ്ദി(˵•̀ᴗ-˵)," + "Your OTP for password change is: " + otp)
//                 .create();

        // Print message SID for reference
        System.out.println("Sent OTP: " + otp);
        return otp;
    }

    private static String formatPhoneNumber(String phoneNumber) {
        return "+216" + phoneNumber; // Tunisia country code is +216
    }


    private static String generateRandomOTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public static void sendOTP(String phoneNumber, String otp) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        // Send the OTP message
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(formatPhoneNumber(phoneNumber)),
                        new com.twilio.type.PhoneNumber(TWILIO_PHONE_NUMBER),
                        "\uD83C\uDF40Hello Trockino\uD83C\uDF40," + "Your OTP is: " + otp)
                .create();

        System.out.println("OTP sent successfully!");
    }



}