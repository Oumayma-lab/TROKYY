package com.example.trokyy.tools;

import com.example.trokyy.models.Blog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class BlogQuery {
    // Préparation de la requête SQL avec des paramètres de substitution
    String requete = "INSERT INTO Blog (auteur_id, contenu, titre, date_publication, image) VALUES (?, ?, ?, ?,?)";

    // Method to add a blog to the database
    public void addBlog(Blog blog) {

            // Connect to the database
            try (Connection connection = new MyDataBaseConnection().getConnection();
                 PreparedStatement statement = connection.prepareStatement(requete)) {

                // Set values for the prepared statement
                statement.setInt(1, blog.getAuteur_id());
                statement.setString(2, blog.getContenu());
                statement.setString(3, blog.getTitre());
                statement.setDate(4, java.sql.Date.valueOf(blog.getDate_publication()));
                statement.setString(5, blog.getImage());

                // Execute the SQL statement
                statement.executeUpdate();

                // Close the connection
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }
}
