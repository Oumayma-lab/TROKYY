package com.example.trokyy.services;

import com.example.trokyy.interfaces.ICommentaire;
import com.example.trokyy.models.Commentaire;
import com.example.trokyy.tools.MyDataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CommentaireService implements ICommentaire<Commentaire> {
    private static Connection connection = new MyDataBaseConnection().getConnection();

    @Override
    public void addCommentaire(Commentaire commentaire) {
        String query = "INSERT INTO commentaire ( blog_id, contenu) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, commentaire.getBlog_id());
            preparedStatement.setString(2, commentaire.getContenu());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCommentaire(Commentaire commentaire) {
        String query = "DELETE FROM commentaire WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, commentaire.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCommentaire(Commentaire commentaire) {
        String query = "UPDATE commentaire SET blog_id = ?, contenu = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, commentaire.getBlog_id());
            preparedStatement.setString(2, commentaire.getContenu());
            preparedStatement.setInt(3, commentaire.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Commentaire> recupererCommentaire() {
        List<Commentaire> commentaires = new ArrayList<>();
        String query = "SELECT * FROM commentaires";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int commenteur_id = resultSet.getInt("commenteur_id");
                int blog_id = resultSet.getInt("blog_id");
                String contenu = resultSet.getString("contenu");
                Commentaire commentaire = new Commentaire(id, commenteur_id, blog_id, contenu);
                commentaires.add(commentaire);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commentaires;
    }


    public List<Commentaire> getAllCommentsForBlog(int blogId) {
        List<Commentaire> comments = new ArrayList<>();
        String query = "SELECT * FROM commentaire WHERE blog_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, blogId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Commentaire commentaire = new Commentaire();
                    commentaire.setId(resultSet.getInt("id"));
                    commentaire.setBlog_id(resultSet.getInt("blog_id"));
                    commentaire.setContenu(resultSet.getString("contenu"));
                    comments.add(commentaire);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving comments for blog: " + e.getMessage());
        }
        return comments;
    }
}