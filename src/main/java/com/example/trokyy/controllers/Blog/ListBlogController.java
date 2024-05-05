package com.example.trokyy.controllers.Blog;


import com.example.trokyy.models.Blog;
import com.example.trokyy.models.Commentaire;

import com.example.trokyy.services.BlogService;
import com.example.trokyy.services.CommentaireService;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class ListBlogController implements Initializable {

    @FXML
    private ListView<Blog> BlogListView;

    @FXML
    private JFXButton blogbtn;

    @FXML
    private JFXButton complaintbtn;

    @FXML
    private JFXButton donbtn;

    @FXML
    private JFXButton eventbtn;

    @FXML
    private JFXButton homebtn;

    @FXML
    private TextField keywordTextField;

    @FXML
    private JFXButton offrebtn;

    @FXML
    private JFXButton profilbtn;

    @FXML
    private VBox side;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;
    @FXML
    private ImageView back;

    BlogService blogService = new BlogService();
    CommentaireService commentaireService = new CommentaireService();

    @FXML
    private void GoToAddBlog() {
        System.out.println("Going To Add Blog");

        try {
            // Charge le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/Blog/AjoutBlog.fxml"));
            Parent root = loader.load();
            // Crée une nouvelle scène avec la vue des blogs
            Scene scene = new Scene(root);
            // Obtient la fenêtre actuelle (stage)
            Stage stage = (Stage) blogbtn.getScene().getWindow();
            // Met à jour la scène de la fenêtre actuelle avec la vue des blogs
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        BlogService blogService = new BlogService();
// Initially, populate the ListView with all blogs
        refreshList();
        List<Blog> blogs = blogService.getData();

        BlogListView.getItems().clear();

        BlogListView.setCellFactory(param -> new BlogtListCell());

        BlogListView.getItems().addAll(blogs);
// Add listener to keywordTextField's textProperty for filtering
        keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchFilter(newValue); // Call the searchFilter method with the new value
        });
        // Apply CSS styles to the ListView
        BlogListView.getStylesheets().add(getClass().getResource("/com/example/trokyy/FrontOffice/Blog/list-blog-styles.css").toExternalForm());
    }

    private void searchFilter(String newValue) {
        // Create a filtered list to hold the filtered items
        FilteredList<Blog> filteredList = new FilteredList<>(BlogListView.getItems());

        // Set the predicate for filtering based on the keyword
        filteredList.setPredicate(blog -> {
            // If the keyword is empty or null, show all items
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            // Convert the keyword and blog title to lowercase for case-insensitive search
            String lowerCaseKeyword = newValue.toLowerCase();
            String lowerCaseTitle = blog.getTitre().toLowerCase();

            // Return true if the blog title contains the keyword
            return lowerCaseTitle.contains(lowerCaseKeyword);
        });

        // Set the filtered list as the new items for the ListView
        BlogListView.setItems(filteredList);

        // If the search text is empty, show all items
        if (newValue == null || newValue.isEmpty()) {
            BlogListView.setItems((ObservableList<Blog>) blogService.getData());
        }
    }
    // Method to refresh the ListView with all blogs
    private void refreshList() {
        List<Blog> blogs = blogService.getData();
        BlogListView.getItems().clear(); // Clear existing items
        BlogListView.getItems().addAll(blogs); // Add all blogs to the ListView
    }

    public void handleBackClick(MouseEvent mouseEvent) {
        try {
            // Load the FXML file of the BlogsManagement page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/Backoffice/BlogsManagement.fxml"));
            Parent blogsManagementRoot = loader.load();

            // Create a new scene with the BlogsManagement root node
            Scene blogsManagementScene = new Scene(blogsManagementRoot);

            // Get the stage from the event source
            Stage stage = (Stage) back.getScene().getWindow();

            // Set the scene to the stage
            stage.setScene(blogsManagementScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class  BlogtListCell extends ListCell<Blog> {
        @Override
        protected void updateItem(Blog item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                VBox card = new VBox();
                card.getStyleClass().add("blog-card");

                // Create labels for each piece of information
                Label typeLabel = new Label(item.getTitre());
                typeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String currentDate = dateFormat.format(date);
                Label dateLabel = new Label("Date: " + currentDate); // Create

                // Create a description area with a titled border
                TitledPane descriptionPane = new TitledPane();
                descriptionPane.setText("Description");
                Label descriptionLabel = new Label(item.getContenu());
                descriptionPane.setContent(descriptionLabel);
                descriptionPane.setCollapsible(false);

                TitledPane priorityPane = new TitledPane();
                priorityPane.setText("All Comments for this blog");

                // Create a VBox to hold all the comments
                VBox commentsBox = new VBox();

                // Fetch all comments for the current blog from the database
                List<Commentaire> comments = commentaireService.getAllCommentsForBlog(item.getId());

                // Iterate through each comment and create a VBox to hold the comment label and buttons
                // Inside BlogtListCell class
                for (Commentaire commentaire : comments) {
                    VBox commentBox = new VBox();
                    Label commentLabel = new Label(commentaire.getContenu());
                    commentBox.getChildren().add(commentLabel);

                    Button editButton = new Button("Edit");
                    // Ensure commentaire is effectively final
                    final Commentaire finalCommentaire = commentaire;
                    editButton.setOnAction(event -> handleEditComment(finalCommentaire));

                    Button deleteButton = new Button("Delete");
                    deleteButton.setOnAction(event -> deleteComment(commentaire));

                    HBox buttonBox = new HBox(10);
                    buttonBox.setAlignment(Pos.CENTER_RIGHT);
                    buttonBox.getChildren().addAll(editButton, deleteButton);
                    commentBox.getChildren().add(buttonBox);

                    commentsBox.getChildren().add(commentBox);
                }

                // Add the comments VBox to the priorityPane
                priorityPane.setContent(commentsBox);

                card.getChildren().addAll(typeLabel, dateLabel, descriptionPane, priorityPane);

                // Like Icon and Comment Icon code remains unchanged

                setGraphic(card);
//////////////////
                String imagePath = item.getImage();
                if (imagePath != null && !imagePath.isEmpty()) {
                    ImageView imageView = new ImageView();
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    Image image = new Image(new File(imagePath).toURI().toString());
                    imageView.setImage(image);
                    card.getChildren().add(imageView);
                }

                Button editButton = new Button("Edit");
                editButton.getStyleClass().add("edit-button");

                editButton.setOnAction(event -> handleEdit(item));


                Button deleteButton = new Button("Delete");
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setOnAction(event -> deleteBlog(item));

                // Ajoutez du style personnalisé aux boutons "Edit" et "Delete"
                editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px;");

                HBox buttonBox = new HBox(10);
                buttonBox.setAlignment(Pos.CENTER_RIGHT);
                buttonBox.getChildren().addAll(editButton, deleteButton);
                card.getChildren().add(buttonBox);


                // Like Icon
                ImageView likeIcon = new ImageView(new Image("C:\\Users\\Lenovo\\Desktop\\TROKYY\\src\\main\\resources\\com\\example\\trokyy\\FrontOffice\\Blog\\thumb-up.png"));
                likeIcon.setFitWidth(25);
                likeIcon.setFitHeight(25);
                likeIcon.setOnMouseClicked(event -> handleLike(item));
                // Label to display the number of likes
                Label likeCountLabel = new Label(Integer.toString(item.getNombre_likes()));

                // Layout for the like icon and count
                HBox likeBox = new HBox(5);
                likeBox.setAlignment(Pos.CENTER_LEFT);
                likeBox.getChildren().addAll(likeIcon, likeCountLabel);
// Comment Icon
                ImageView commentIcon = new ImageView(new Image("C:\\Users\\Lenovo\\Desktop\\TROKYY\\src\\main\\resources\\com\\example\\trokyy\\FrontOffice\\Blog\\chat.png"));
                commentIcon.setFitWidth(25);
                commentIcon.setFitHeight(25);
                commentIcon.setOnMouseClicked(event -> handleAddComment(item));

                HBox iconBox = new HBox(10);
                iconBox.setAlignment(Pos.CENTER_RIGHT);
                iconBox.getChildren().addAll(likeIcon, commentIcon);
                card.getChildren().add(iconBox);

// Update like count label when the number of likes changes
                blogService.setOnLikeChangeListener((blog, likes) -> {
                    if (blog.getId() == item.getId()) {
                        likeCountLabel.setText(Integer.toString(likes));
                    }
                });
                }
            }
        }

        // Method to handle edit comment action
        private void handleEditComment(Commentaire commentaire) {
            // Create a dialog for editing the comment
            TextInputDialog dialog = new TextInputDialog(commentaire.getContenu());
            dialog.setTitle("Edit Comment");
            dialog.setHeaderText("Edit the selected comment:");
            dialog.setContentText("Enter the updated comment:");

            // Show the dialog and wait for user input
            Optional<String> result = dialog.showAndWait();

            // Update the comment if the user provided new input
            result.ifPresent(updatedComment -> {
                commentaire.setContenu(updatedComment);
                // Update the comment in the database using the CommentaireService
                commentaireService.updateCommentaire(commentaire);
                // Refresh the list view or perform any other necessary action
                refreshBlogListView(); // Assuming this method refreshes the blog list view
            });
            // Ajoutez du style personnalisé aux boutons "Edit"
            editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        }

        // Method to handle delete comment action
        private void deleteComment(Commentaire commentaire) {
            // Show a confirmation dialog before deleting the comment
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this comment?");
            alert.setContentText("This action cannot be undone.");

            // Wait for user confirmation
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Remove the comment from the database using the CommentaireService
                    commentaireService.deleteCommentaire(commentaire);
                    // Refresh the list view or perform any other necessary action
                    refreshBlogListView(); // Assuming this method refreshes the blog list view
                }
            });
            deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px;");

        }

        @FXML
        private void deleteBlog(Blog BlogToDelete) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this blog?");
            alert.setContentText("This action cannot be undone.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {

                    BlogListView.getItems().remove(BlogToDelete);
                    blogService.deleteBlog(BlogToDelete);

                    refreshBlogListView();

                }
            });
        }

        private void refreshBlogListView() {
            // Fetch the latest data from the database
            List<Blog> blogs = blogService.getData();

            BlogListView.getItems().clear();

            BlogListView.getItems().addAll(blogs);
        }

        @FXML
        private void handleEdit(Blog selectedBlog) {
            if (selectedBlog != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/Blog/EditBlog.fxml"));
                    Parent root = loader.load();

                    EditBlogController editController = loader.getController();
                    editController.initData(selectedBlog);

                    Stage stage = new Stage();
                    stage.setTitle("Edit Complaint");
                    stage.setScene(new Scene(root));

                    stage.showAndWait();

                    if (editController.isSaved()) {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Handle Like Action
        private void handleLike(Blog selectedBlog) {
            if (selectedBlog != null) {
                // Increment the number of likes for the selected blog
                selectedBlog.setNombre_likes(selectedBlog.getNombre_likes() + 1);

                // Update the likes count in the database using the BlogService
                blogService.updateLikes(selectedBlog);

                // Refresh the blog list view to reflect the updated likes count
                refreshBlogListView();



                // Afficher un message de confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Like ");
                alert.setHeaderText(null);
                alert.setContentText("You liked this blog :)");
                alert.showAndWait();

            }
        }


        // Handle Comment Action
        private void handleAddComment(Blog selectedBlog) {
            // Créez un nouveau commentaire et définissez l'ID du blog correspondant
            Commentaire newComment = new Commentaire();
            newComment.setBlog_id(selectedBlog.getId());

            // Créez une boîte de dialogue pour ajouter un commentaire
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Comment");
            dialog.setHeaderText("Add a comment to the selected blog");
            dialog.setContentText("Enter your comment:");

            // Affichez la boîte de dialogue et attendez la réponse de l'utilisateur
            Optional<String> result = dialog.showAndWait();

            // Check if the user entered a comment and update the new comment
            result.ifPresent(comment -> {
                // Filter out bad words from the comment
                String[] motsInterdits = {"mot1", "mot2", "mot3"};
                for (String motInterdit : motsInterdits) {
                    comment = comment.replaceAll("(?i)" + motInterdit, "****");
                }
                newComment.setContenu(comment);
                // Add the new comment to the database using the comment service
                commentaireService.addCommentaire(newComment);
                // Refresh the comments list or any other necessary action
            });
        }
        }



