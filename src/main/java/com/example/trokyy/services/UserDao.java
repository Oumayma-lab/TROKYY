package com.example.trokyy.services;

import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.tools.MyDataBaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDao {
    private static Connection connection;

    public UserDao() {
        connection = MyDataBaseConnection.getInstance().getConnection();
    }

    public static void createUser(Utilisateur user, LocalDateTime registrationDate) throws SQLException {
        Connection connection = MyDataBaseConnection.getConnection();
        // Ensure connection is not null before proceeding
        if (connection == null) {
            System.err.println("Connection is null. Please check database connection.");
            return;
        }
        String query = "INSERT INTO utilisateur (nom, prenom, email, mdp, tel, adresse, is_active, dateinscription, roles) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getNom());
            statement.setString(2, user.getPrenom());
            statement.setString(3, user.getEmail());
            String salt = BCrypt.gensalt(12); // You can adjust the strength of the salt (e.g., 12)
            String hashedPassword = BCrypt.hashpw(user.getPassword(), salt);
            statement.setString(4, hashedPassword);
            statement.setString(5, String.valueOf(user.getTelephone())); // Set telephone number as string
            statement.setString(6, user.getAdresse());   // Set address
            statement.setBoolean(7, true); // Set is_active to true by default
            statement.setTimestamp(8, Timestamp.valueOf(registrationDate));
            String rolesString = String.join(",", user.getRoles());
            statement.setString(9, rolesString);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new user was inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public static void assignRole(String userEmail, String role) {
        String updateQuery = "UPDATE utilisateur SET roles = ? WHERE email = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            // Retrieve existing roles
            String[] existingRoles = getRolesByEmail(userEmail);
            List<String> updatedRoles = new ArrayList<>(Arrays.asList(existingRoles));
            // Add the new role to the list of roles
            updatedRoles.add(role);
            // Convert the list back to an array
            Array updatedRolesArray = connection.createArrayOf("VARCHAR", updatedRoles.toArray());
            // Update the user's roles
            updateStatement.setArray(1, updatedRolesArray);
            updateStatement.setString(2, userEmail);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    private static String[] getRolesByEmail(String userEmail) throws SQLException {
        String query = "SELECT roles FROM utilisateur WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userEmail);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Array rolesArray = resultSet.getArray("roles");
                    return (String[]) rolesArray.getArray();
                } else {
                    System.err.println("User not found with email: " + userEmail);
                }
            }
        }
        return new String[0]; // Return an empty array if no roles found
    }


    public Utilisateur getUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM utilisateur WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(resultSet.getInt("id"));
                    utilisateur.setUsername(resultSet.getString("username"));
                    utilisateur.setPrenom(resultSet.getString("prenom"));
                    utilisateur.setEmail(resultSet.getString("email"));
                    utilisateur.setMdp(resultSet.getString("mdp")); // Retrieve hashed password from the database
                    utilisateur.setActive(resultSet.getBoolean("is_active"));
                    // Retrieve roles as an array
                    String rolesString = resultSet.getString("roles");
                    if (rolesString != null) {
                        String[] rolesArray = rolesString.split(",");
                        utilisateur.setRoles(Arrays.asList(rolesArray));
                    }
                    return utilisateur;

                }
            }
        }
        return null;
    }





    public Utilisateur getUserById(int userId) throws SQLException {
        Utilisateur user = null;
        String query = "SELECT * FROM utilisateur WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId); // Set the user ID parameter
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new Utilisateur();
                user.setId(resultSet.getInt("id"));
                user.setNom(resultSet.getString("nom"));
                user.setPrenom(resultSet.getString("prenom"));
                user.setEmail(resultSet.getString("email"));
                user.setMdp(resultSet.getString("mdp"));
                user.setUsername(resultSet.getString("username"));
                user.setPhotoProfil(resultSet.getString("avatar"));
                user.setAdresse(resultSet.getString("adresse"));
                user.setTelephone(resultSet.getInt("tel"));
                user.setActive(resultSet.getBoolean("is_active"));

            }
        }
        return user;
    }

    public static boolean verifyUser(String email, String password) {
        String hashedPasswordFromDatabase = getHashedPasswordFromDatabase(email);

        // Compare the hashed password from the database with the password entered during login
        return hashedPasswordFromDatabase != null && BCrypt.checkpw(password, hashedPasswordFromDatabase);
    }

    private static String getHashedPasswordFromDatabase(String email) {
        Connection connection = MyDataBaseConnection.getConnection();
        String query = "SELECT mdp FROM utilisateur WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("mdp");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void updateUser(int userId, Utilisateur updatedUser) throws SQLException {
        String query = "UPDATE utilisateur SET nom=?, prenom=?, email=?, username=?, adresse=?, tel=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, updatedUser.getNom());
            statement.setString(2, updatedUser.getPrenom());
            statement.setString(3, updatedUser.getEmail());
            statement.setString(4, updatedUser.getUsername());
            statement.setString(5, updatedUser.getAdresse());
            statement.setInt(6, updatedUser.getTelephone());
            statement.setInt(7, userId);
            statement.executeUpdate();
            System.out.println("User profile updated successfully.");


        }
    }

    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public List<Utilisateur> getAllUsers() throws SQLException {
        List<Utilisateur> userList = new ArrayList<>();
        String query = "SELECT id, nom, prenom, tel, email, adresse, is_active, dateinscription FROM Utilisateur";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                int tel = resultSet.getInt("tel");
                String email = resultSet.getString("email");
                String adresse = resultSet.getString("adresse");
                boolean is_active = resultSet.getBoolean("is_active");
                Timestamp timestamp = resultSet.getTimestamp("dateinscription");
                LocalDateTime dateInscription = (timestamp != null) ? timestamp.toLocalDateTime() : null;
                Utilisateur utilisateur = new Utilisateur(id, nom,prenom, tel, email, adresse, is_active, dateInscription);
                userList.add(utilisateur);
            }
        }
        return userList;
    }


    public static void deleteUser(Utilisateur utilisateur) {
        String query = "DELETE FROM Utilisateur WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, utilisateur.getId());
            System.out.println("Deleting user with ID: " + utilisateur.getId());
            preparedStatement.executeUpdate();
            System.out.println("User deleted: " + utilisateur);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void banUser(Utilisateur utilisateur) {
        String query = "UPDATE Utilisateur SET is_active = ? WHERE id = ?";
        try (Connection connection = MyDataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBoolean(1, false);
            preparedStatement.setInt(2, utilisateur.getId());
            preparedStatement.executeUpdate();
            System.out.println("User banned: " + utilisateur.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isUserBanned(String email, String password) throws SQLException {
        String query = "SELECT is_active FROM utilisateur WHERE email = ? AND mdp = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return !resultSet.getBoolean("is_active"); // Return true if user is banned (isActive is false)

                }
            }
        }
        return false; // Return false if user is not found or isActive is true
    }

    public List<Utilisateur> searchUsers(String query) throws SQLException {
        List<Utilisateur> searchResults = new ArrayList<>();
        String sql = "SELECT * FROM Utilisateur WHERE nom LIKE ? OR prenom LIKE ? OR email LIKE ? OR adresse LIKE ? OR id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= 5 ; i++) {
                statement.setString(i, "%" + query + "%");
            }
            // For the id column
            try {
                int id = Integer.parseInt(query);
                statement.setInt(5, id);
            } catch (NumberFormatException e) {
                // Ignore if the query cannot be parsed as an integer
                statement.setString(5, "%" + query + "%");
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Retrieve user data from the result set and create Utilisateur objects
                    Utilisateur utilisateur = new Utilisateur(
                            resultSet.getInt("id"),
                            resultSet.getString("nom"),
                            resultSet.getString("prenom"),
                            resultSet.getInt("tel"),
                            resultSet.getString("email"),
                            resultSet.getString("adresse"),
                            resultSet.getBoolean("is_active")
                    );
                    searchResults.add(utilisateur);
                }
            }
        }
        return searchResults;
    }

    public List<Utilisateur> getUsersByStatus(boolean isActive) throws SQLException {
        List<Utilisateur> userList = new ArrayList<>();
        String query = "SELECT id, nom, prenom, tel, email, adresse, is_active, dateinscription FROM Utilisateur WHERE is_active = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBoolean(1, isActive);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    int tel = resultSet.getInt("tel");
                    String email = resultSet.getString("email");
                    String adresse = resultSet.getString("adresse");
                    boolean is_active = resultSet.getBoolean("is_active");
                    Timestamp timestamp = resultSet.getTimestamp("dateinscription");
                    LocalDateTime dateInscription = (timestamp != null) ? timestamp.toLocalDateTime() : null;
                    Utilisateur utilisateur = new Utilisateur(id, nom, prenom, tel, email, adresse, is_active, dateInscription);
                    userList.add(utilisateur);
                }
            }
        }
        return userList;
    }

    public List<Utilisateur> getActiveUsers() throws SQLException {
        List<Utilisateur> activeUsers = new ArrayList<>();
        String query = "SELECT id, nom, prenom, tel, email, adresse, is_active, dateinscription FROM Utilisateur WHERE is_active = true";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                // Retrieve user details and create Utilisateur object
                // Add the Utilisateur object to the activeUsers list
            }
        }
        return activeUsers;
    }

    public List<Utilisateur> getBannedUsers() throws SQLException {
        List<Utilisateur> bannedUsers = new ArrayList<>();
        String query = "SELECT id, nom, prenom, tel, email, adresse, is_active, dateinscription FROM Utilisateur WHERE is_active = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                // Retrieve user details and create Utilisateur object
                // Add the Utilisateur object to the bannedUsers list
            }
        }
        return bannedUsers;
    }

    public static void reactivateBannedUsers() {
        String query = "UPDATE Utilisateur SET is_active = ? WHERE is_active = ? AND banned_time <= ?";
        long currentTime = System.currentTimeMillis();
        long tenMinutesInMillis = 10 * 60 * 1000; // 10 minutes in milliseconds
        long reactivationTime = currentTime - tenMinutesInMillis;

        try (Connection connection = MyDataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBoolean(1, true); // Reactivate user
            preparedStatement.setBoolean(2, false); // User is currently banned
            preparedStatement.setLong(3, reactivationTime); // Users banned before reactivationTime
            preparedStatement.executeUpdate();
            System.out.println("Reactivated banned users.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProfilePhoto(int userId, String photoUrl) throws SQLException {
        String query = "UPDATE Utilisateur SET avatar = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, photoUrl);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }

    public String getProfilePhoto(int userId) throws SQLException {
        String query = "SELECT avatar FROM Utilisateur WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("avatar");
                }
            }
        }
        return null; // If no photo found
    }


    public void deleteUserAccount(int userId) throws SQLException {
        String query = "DELETE FROM Utilisateur WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
    }

    public String getPassword(int userId) throws SQLException {
        String query = "SELECT password FROM Utilisateur WHERE id = ?";
        try (
             Connection connection = MyDataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("password");
                } else {
                    throw new SQLException("User not found");
                }
            }
        }
    }

    public static boolean verifyPassword(String enteredPassword, String hashedPassword) {
        // Check if either enteredPassword or hashedPassword is null or empty
        if (enteredPassword == null || enteredPassword.isEmpty() || hashedPassword == null || hashedPassword.isEmpty()) {
            return false; // Password verification fails if either password is null or empty
        }

        // Compare the entered password with the hashed password retrieved from the database
        return BCrypt.checkpw(enteredPassword, hashedPassword);
    }




    public boolean verifyPasswordChange(String oldPassword, int userId) {
        try (
            PreparedStatement statement = connection.prepareStatement("SELECT mdp FROM Utilisateur WHERE id = ?")) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String savedPassword = resultSet.getString("mdp");
                return BCrypt.checkpw(oldPassword, savedPassword); // Check if old password matches
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void updatePassword(String newPassword, int userId) {
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        try (
             PreparedStatement statement = connection.prepareStatement("UPDATE Utilisateur SET mdp = ? WHERE id = ?")) {
            statement.setString(1, hashedPassword);
            statement.setInt(2, userId);
            statement.executeUpdate();

            System.out.println("Password updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String getUserPhoneNumber(int userId) throws SQLException {
        String phoneNumber = null;
        try (
             PreparedStatement statement = connection.prepareStatement("SELECT tel FROM Utilisateur WHERE id = ?");
        ) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    phoneNumber = String.valueOf(resultSet.getInt("tel"));
                }
            }
        }
        return phoneNumber;
    }

    public List<String> getUserAddresses() throws SQLException {
        List<String> addresses = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT adresse FROM Utilisateur";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                addresses.add(resultSet.getString("adresse"));
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }

        return addresses;
    }
}

