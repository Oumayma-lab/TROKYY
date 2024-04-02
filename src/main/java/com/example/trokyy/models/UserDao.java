package com.example.trokyy.controllers;

import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.tools.MyDataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao {
    private Connection connection;
    public UserDao() {
        connection = MyDataBaseConnection.getConnection();
    }
    public void createUser(Utilisateur user) {
        String query = " INSERT INTO `Utilisateur` (nom, prenom, email, mdp, adresse, tel) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getNom());
            statement.setString(2, user.getPrenom());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getMdp());
            statement.setString(5, user.getAdresse());
            statement.setString(6, user.getTelephone());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
