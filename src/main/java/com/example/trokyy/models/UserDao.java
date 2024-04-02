package com.example.trokyy.models;

import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.tools.MyDataBaseConnection;
import com.example.trokyy.tools.PBKDF2PasswordHash;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private static Connection connection;
    public UserDao() {
        connection = MyDataBaseConnection.getInstance().getConnection();
    }

    public static void createUser(Utilisateur user) {
        Connection connection = MyDataBaseConnection.getConnection();

        // Ensure connection is not null before proceeding
        if (connection == null) {
            System.err.println("Connection is null. Please check database connection.");
            return;
        }
        String query = "INSERT INTO `utilisateur` (nom, prenom, email, mdp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getNom());
            statement.setString(2, user.getPrenom());
            statement.setString(3, user.getEmail());
            String hashedPassword = PBKDF2PasswordHash.hashPassword(user.getPassword());
            statement.setString(4, hashedPassword);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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
                    utilisateur.setEmail(resultSet.getString("email"));
                    utilisateur.setMdp(resultSet.getString("mdp"));
                    // Set other attributes as needed

                    return utilisateur;                }
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
            }
        }
        return user;
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
}
