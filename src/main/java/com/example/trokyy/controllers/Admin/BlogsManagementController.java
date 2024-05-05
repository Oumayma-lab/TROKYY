package com.example.trokyy.controllers.Admin;

import com.example.trokyy.models.Blog;
import com.example.trokyy.tools.MyDataBaseConnection;
import javafx.fxml.FXML;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


import java.sql.*;
import java.time.LocalDate;


public class BlogsManagementController {
    private static Connection connection = new MyDataBaseConnection().getConnection();

    @FXML
    private TableView<Blog> tableView;

    @FXML
    private TableColumn<Blog, Integer> idColumn;

    @FXML
    private TableColumn<Blog, Integer> userIdColumn;

    @FXML
    private TableColumn<Blog, String> contentColumn;

    @FXML
    private TableColumn<Blog, String> titleColumn;

    @FXML
    private TableColumn<Blog, LocalDate> dateColumn;

    @FXML
    private TableColumn<Blog, String> imageColumn;

    @FXML
    private TableColumn<Blog, Integer> likesColumn;

    @FXML
    private TableColumn<Blog, String> statusColumn;

    @FXML
    private TableColumn<Blog, String> actionsColumn;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Text helloLabel;
    public void initialize() {
        populateTableView();
    }

    private void populateTableView() {

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM blog")) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("auteur_id");
                String content = resultSet.getString("contenu");
                String title = resultSet.getString("titre");
                LocalDate date = resultSet.getDate("date_publication").toLocalDate();
                String image = resultSet.getString("image");
                int likes = resultSet.getInt("nombre_likes");

                // Create a Blog object
                Blog blog = new Blog(id, userId, content, title, date, image, likes);

                // Add the Blog object to the TableView
                tableView.getItems().add(blog);
            }

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }