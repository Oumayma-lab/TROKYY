package com.example.trokyy.tools;
import org.mindrot.jbcrypt.BCrypt;

public class BcryptPasswordHash {
    // Hash a password using bcrypt
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Verify a password against a hashed password
        public static boolean verifyPassword(String password, String hashedPassword) {
            // Check if password or hashedPassword is null or empty
            if (password == null || hashedPassword == null || password.isEmpty() || hashedPassword.isEmpty()) {
                return false; // Password cannot be verified if either is null or empty
            }
            // Verify the password
            return BCrypt.checkpw(password, hashedPassword);
        }
}

