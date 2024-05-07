package com.example.trokyy.controllers.Admin;

import com.example.trokyy.models.Blog;
import com.example.trokyy.services.BlogService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.io.File;
import java.net.URL;

import java.time.LocalDate;
import java.util.ResourceBundle;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BlogsManagementController implements Initializable {
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
}
//corrction backoffice
//AI image
//statistique