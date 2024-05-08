package com.example.trokyy.controllers.Admin;

import com.example.trokyy.controllers.Blog.EditBlogController;
import com.example.trokyy.models.Blog;
import com.example.trokyy.services.BlogService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.time.LocalDate;
import java.util.ResourceBundle;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class BlogsManagementController implements Initializable {
    public TextField keywordTextField1;
    @FXML
    private TableColumn<Blog, Void> actionsColumn;

    @FXML
    private TableColumn<Blog, String> contentColumn;

    @FXML
    private TableColumn<Blog, LocalDate> dateColumn;

    @FXML
    private TableColumn<Blog, Integer> idColumn;

    @FXML
    private TableColumn<Blog,  String> imageColumn;

    @FXML
    private TableColumn<Blog, Integer> likesColumn;

    @FXML
    private TableView<Blog> tableView;

    @FXML
    private TableColumn<Blog, String> titleColumn;

    @FXML
    private TableColumn<Blog, Integer> userIdColumn;

    private final BlogService blogService = new BlogService();
    private final ObservableList<Blog> blogsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeColumns();
        loadDataFromDatabase();

        actionsColumn.setCellFactory(param -> new TableCell<Blog, Void>() {
            final Button editButton = new Button("Edit");
            final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> {
                    Blog blog = getTableView().getItems().get(getIndex());
                    handleEdit(blog);
                });

                deleteButton.setOnAction(event -> {
                    Blog blog = getTableView().getItems().get(getIndex());
                    deleteBlog(blog);
                });
                // Apply CSS styling to the buttons
                editButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            }


            // Define the handleEdit method to handle the edit functionality
            public void handleEdit(Blog blog) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/Blog/EditBlog.fxml"));
                    Parent root = loader.load();

                    EditBlogController editController = loader.getController();
                    editController.initData(blog);

                    Stage stage = new Stage();
                    stage.setTitle("Edit Blog");
                    stage.setScene(new Scene(root));

                    stage.showAndWait();

                    if (editController.isSaved()) {
                        // Implement what needs to be done after the blog is edited and saved
                        refresh();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Editing blog with id: " + blog.getId());
            }

            // Define the deleteBlog method to handle the delete functionality
            public void deleteBlog(Blog blog) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Deletion");
                alert.setHeaderText("Are you sure you want to delete this blog?");
                alert.setContentText("This action cannot be undone.");

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Delete the blog
                        blogService.deleteBlog(blog);
                        // Refresh the blog list view
                        refresh();
                    }
                });
                System.out.println("Deleting blog with id: " + blog.getId());
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() < 0) {
                    setGraphic(null);
                } else {
                    HBox buttonsBox = new HBox(5);
                    buttonsBox.getChildren().addAll(editButton, deleteButton);
                    setGraphic(buttonsBox);
                }
            }
        });


    //ll serch of back
    // Add listener to keywordTextField1 for filtering blogs
        keywordTextField1.textProperty().addListener((observable, oldValue, newValue) -> {
        searchFilter(newValue);
    });
}

    private void searchFilter(String newValue) {
        // Create a filtered list to hold the filtered items
        FilteredList<Blog> filteredList = new FilteredList<>(blogsList);

        // Set the predicate for filtering based on the keyword
        filteredList.setPredicate(blog -> {
            // If the keyword is empty or null, show all items
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            // Convert the keyword and blog title to lowercase for case-insensitive search
            String lowerCaseKeyword = newValue.toLowerCase();

            // Check if the blog title or content contains the keyword
            String lowerCaseTitle = blog.getTitre().toLowerCase();
            String lowerCaseContent = blog.getContenu().toLowerCase();

            // Return true if the blog title or content contains the keyword
            return lowerCaseTitle.contains(lowerCaseKeyword) || lowerCaseContent.contains(lowerCaseKeyword);
        });

        // Set the filtered list as the new items for the TableView
        tableView.setItems(filteredList);

        // If the search text is empty, show all items
        if (newValue == null || newValue.isEmpty()) {
            tableView.setItems(blogsList);
        }
    }
    private void initializeColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("auteur_id"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date_publication"));

        // Configuration de la colonne imageColumn pour afficher une image
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        imageColumn.setCellFactory(column -> new TableCell<Blog,  String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String image, boolean empty) {
                super.updateItem(image, empty);

                if (empty || image == null) {
                    // Si la cellule est vide ou si l'image est null, affichez une image vide
                    imageView.setImage(null);
                    setGraphic(null);
                } else {
                    // Afficher l'image dans la cellule
                    // Charger l'image à partir du chemin spécifié et l'afficher dans la cellule
                    Image imageV = new Image(new File(image).toURI().toString());
                    imageView.setImage(imageV);
                    imageView.setFitWidth(50); // Réglez la largeur de l'image
                    imageView.setFitHeight(50); // Réglez la hauteur de l'image
                    setGraphic(imageView);
                }
            }
        });
        likesColumn.setCellValueFactory(new PropertyValueFactory<>("nombre_likes"));
    }

    private void loadDataFromDatabase() {
        blogsList.setAll(blogService.getData());
        tableView.setItems(blogsList);
    }

    @FXML
    void refresh() {
        blogsList.clear();
        loadDataFromDatabase();
    }


    //recherche de backOffice :D


}
//corrction backoffice
//AI image
//statistique