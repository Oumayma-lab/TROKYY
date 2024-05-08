package com.example.trokyy.services;

import com.example.trokyy.interfaces.IBlog;
import com.example.trokyy.models.Blog;
import com.example.trokyy.tools.MyDataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class BlogService implements IBlog<Blog> {
    private static Connection connection = new MyDataBaseConnection().getConnection();


    @Override
    public void addBlog(Blog blog) {
        // Obtention de la date actuelle
        Timestamp dateActuelle = new Timestamp(System.currentTimeMillis());
        String query = "INSERT INTO blog (titre, contenu, image, date_publication) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, blog.getContenu());
            statement.setString(2, blog.getTitre());
            statement.setString(3, blog.getImage());
            statement.setTimestamp(4, dateActuelle); // Date actuelle
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Blog ajouté avec succès !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du blog : " + e.getMessage());
        }
    }

    @Override
    public void deleteBlog(Blog blog) {
        String query = "DELETE FROM blog WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, blog.getId());
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Blog supprimé avec succès !");
            } else {
                System.out.println("Aucun blog trouvé avec l'ID spécifié.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du blog : " + e.getMessage());
        }
    }


    @Override
    public void updateBlog(Blog blog) {
        String query = "UPDATE blog SET titre = ?, contenu = ?, image = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, blog.getTitre());
            statement.setString(2, blog.getContenu());
            statement.setString(3, blog.getImage());
            statement.setInt(4, blog.getId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Blog mis à jour avec succès !");
            } else {
                System.out.println("Aucun blog trouvé avec l'ID spécifié.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du blog : " + e.getMessage());
        }
    }

    @Override
    public List<Blog> getData() {
        List<Blog> blogs = new ArrayList<>();
        String query = "SELECT * FROM blog";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Blog blog = new Blog();
                blog.setId(resultSet.getInt("id"));
                blog.setTitre(resultSet.getString("titre"));
                blog.setContenu(resultSet.getString("contenu"));
                blog.setImage(resultSet.getString("image"));
                blog.setDate_publication(resultSet.getDate("date_publication").toLocalDate());
                blog.setNombre_likes(resultSet.getInt("nombre_likes"));
                blogs.add(blog);
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving data : " + e.getMessage());
        }
        return blogs;
    }

    private BiConsumer<Blog, Integer> likeChangeListener;

    public void setOnLikeChangeListener(BiConsumer<Blog, Integer> listener) {
        this.likeChangeListener = listener;
    }

    public void updateLikes(Blog blog) {
        // Utilisez votre couche d'accès aux données (DAO) pour mettre à jour le nombre de likes dans la base de données
        // Exemple fictif :
        try {
            Connection connection = new MyDataBaseConnection().getConnection();
                    String query = "UPDATE blog SET nombre_likes = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, blog.getNombre_likes());
            statement.setInt(2, blog.getId());
            statement.executeUpdate();
            // Fermez les ressources (statement, connection) après utilisation
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Gérez les erreurs de manière appropriée
        }
    }


    public void likeBlog(Blog blog) {
        String query = "UPDATE blog SET nombre_likes = nombre_likes + 1 WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, blog.getId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Blog liked successfully!");
            } else {
                System.out.println("No blog found with the specified ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error while liking the blog: " + e.getMessage());
        }
    }


}