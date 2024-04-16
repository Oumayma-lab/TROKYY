package com.example.trokyy.services;

import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.tools.MyDataBaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
        String query = "INSERT INTO utilisateur (nom, prenom, email, mdp, is_active,dateinscription) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getNom());
            statement.setString(2, user.getPrenom());
            statement.setString(3, user.getEmail());
            String salt = BCrypt.gensalt(12); // You can adjust the strength of the salt (e.g., 12)
            String hashedPassword = BCrypt.hashpw(user.getPassword(), salt);
            statement.setString(4, hashedPassword);
            statement.setBoolean(5, true); // Set is_active to true by default
            statement.setTimestamp(6, Timestamp.valueOf(registrationDate));
            statement.executeUpdate();
            assignUserRole(user.getEmail(), "ROLE_USER");

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    private static void assignUserRole(String userEmail, String role) {
        String query = "UPDATE utilisateur SET roles = array_append(roles, ?) WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, role);
            statement.setString(2, userEmail);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
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
                    // Verify password using bcrypt
                    String rolesString = resultSet.getString("roles");
                    // Assuming roles are stored as comma-separated values in the database
                    rolesString = rolesString.substring(1, rolesString.length() - 1);
                    List<String> roles = Arrays.asList(rolesString.split(","));utilisateur.setRoles(roles);
                    return utilisateur;

                }
            }
        }
        return null;
    }


    public static boolean verifyPassword(String enteredPassword, String hashedPassword) {
        // Compare the entered password with the hashed password retrieved from the database
        return BCrypt.checkpw(enteredPassword, hashedPassword);
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
        String query = "UPDATE utilisateur SET nom=?, prenom=?, email=?, mdp=?, username=?, adresse=?, tel=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, updatedUser.getNom());
            statement.setString(2, updatedUser.getPrenom());
            statement.setString(3, updatedUser.getEmail());
            statement.setString(4, updatedUser.getPassword());
            statement.setString(5, updatedUser.getUsername());
            statement.setString(6, updatedUser.getAdresse());
            statement.setInt(7, updatedUser.getTelephone());
            statement.setInt(8, userId);
            statement.executeUpdate();
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

}
