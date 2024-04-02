package com.example.trokyy.services;

import com.example.trokyy.models.personne;
import com.example.trokyy.tools.MyDataBaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ServicePersonne implements IServicePersonne {
    private static Connection connection;

    private static final String SELECT_ALL_PERSONNE_QUERY = "SELECT * FROM personne";
    public ServicePersonne() {
        connection = MyDataBaseConnection.getInstance().getConnection();
    }
    public ArrayList<personne> getAll() {
        ArrayList<personne> personnes = new ArrayList<>();
        Connection connection = MyDataBaseConnection.getConnection();
        try (
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL_PERSONNE_QUERY)) {
            while (resultSet.next()) {
                personne p = new personne();
                p.setId(resultSet.getInt("id"));
                p.setNom(resultSet.getString("nom"));
                p.setPrenom(resultSet.getString("prenom"));
                personnes.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database access error appropriately
        }
        return personnes;
    }
}
