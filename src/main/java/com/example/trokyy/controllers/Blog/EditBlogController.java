package com.example.trokyy.controllers.Blog;

import com.example.trokyy.models.Blog;
import com.example.trokyy.services.BlogService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EditBlogController implements Initializable {


    @FXML
    private TextField titleField;

    @FXML
    private Label lblImagePath;

    @FXML
    private TextArea descriptionTextArea;

    private Blog blogToEdit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void initData(Blog blog) {
        blogToEdit = blog;
        titleField.setText(blog.getTitre());
        descriptionTextArea.setText(blog.getContenu());
        lblImagePath.setText(blog.getImage());
    }

    @FXML
    private void handleSave(ActionEvent event) {

        saved = true;
        if (blogToEdit != null) {
            // Récupérer la description du champ de texte
            String description = descriptionTextArea.getText();
            // Liste des mots interdits
            String[] motsInterdits = {"kill", "murder", "danger"};
            // Parcourir la liste des mots interdits et remplacer chaque occurrence dans la description
            for (String motInterdit : motsInterdits) {
                // Utiliser une expression régulière insensible à la casse pour remplacer chaque occurrence du mot interdit
                description = description.replaceAll("(?i)" + motInterdit, "****");
            }

            // Récupérer le titre du champ de texte
            String title = titleField.getText();
            // Parcourir la liste des mots interdits et remplacer chaque occurrence dans le titre
            for (String motInterdit : motsInterdits) {
                // Utiliser une expression régulière insensible à la casse pour remplacer chaque occurrence du mot interdit
                title = title.replaceAll("(?i)" + motInterdit, "****");
            }
            blogToEdit.setTitre(title);  // Utilisez la variable filtrée "title" au lieu de "titleField.getText()"
            blogToEdit.setContenu(description);
            blogToEdit.setImage(lblImagePath.getText());
            // Mettre à jour le blog dans la base de données
            BlogService blogService = new BlogService();
            blogService.updateBlog(blogToEdit);
            // Fermer la fenêtre d'édition
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();


        }
    }


    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(lblImagePath.getScene().getWindow());

        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            lblImagePath.setText(imagePath);
        }
    }

    @FXML
    void clearFields() {
        titleField.clear();
        descriptionTextArea.clear();
        lblImagePath.setText("");
    }


    private boolean saved = false;


    public boolean isSaved() {
        return saved;
    }
}