package com.example.trokyy.controllers.Blog;

import com.example.trokyy.models.Blog;
import com.example.trokyy.services.BlogService;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Control;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


public class AjoutBlogController implements Initializable {


    @FXML
    private JFXButton blogbtn;

    @FXML
    private Button btnUploadImage;

    @FXML
    private JFXButton complaintbtn;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private JFXButton donbtn;

    @FXML
    private JFXButton eventbtn;

    @FXML
    private JFXButton homebtn;

    @FXML
    private TextField keywordTextField;

    @FXML
    private Label lblImagePath;

    @FXML
    private JFXButton offrebtn;

    @FXML
    private JFXButton profilbtn;

    @FXML
    private AnchorPane root;

    @FXML
    private VBox side;

    @FXML
    private TextField titletextfild;

    @FXML
    private String image;

    @FXML
    private TextField titleField;


    private final BlogService blogService = new BlogService();
    @FXML
    private void handleButtonAction() {
        System.out.println("button 'Blogs' cliq ");

        try {
            // Charge le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/Blog/ListBlog.fxml"));
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
    @FXML
    void addBlog(javafx.event.ActionEvent actionEvent) {
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

        // Vérifier si la description ou le titre est vide
        if (description.isEmpty() || title.isEmpty()) {
            // Afficher un message d'erreur si la description ou le titre est vide
            showAlert("Please fill in both the description and the title.");
            return; // Quitter la méthode prématurément si la description ou le titre est vide
        }

        // Si les champs sont remplis, ajouter un nouveau blog
        Blog blog = new Blog(title, description, image);
        blogService.addBlog(blog); // Appeler la méthode pour ajouter le blog

        // Afficher une notification pour indiquer que le blog a été ajouté avec succès
        showNotification("Blog added successfully");

        // Réinitialiser les champs après l'ajout
        clearFields();


    }

    @FXML
    void clearFields() {
        descriptionTextArea.clear();
        titleField.clear();
        lblImagePath.setText("");
        image = null; // Réinitialiser le chemin de l'image à null
    }

    @FXML
    void uploadImage(javafx.event.ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            image = selectedFile.getAbsolutePath();
            lblImagePath.setText(image);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
// Ajoutez des écouteurs aux champs de titre et de contenu pour surveiller les modifications
        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            highlightEmptyField(newValue.isEmpty(), titleField);
        });

        descriptionTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            highlightEmptyField(newValue.isEmpty(), descriptionTextArea);
        });

    }

    // Méthode pour mettre en évidence les champs vides en modifiant la classe CSS
    private void highlightEmptyField(boolean isEmpty, Control control) {
        if (isEmpty) {
            // Si le champ est vide, ajoutez la classe CSS pour le mettre en évidence
            control.getStyleClass().add("empty-field");
        } else {
            // Sinon, supprimez la classe CSS
            control.getStyleClass().remove("empty-field");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showNotification(String message) {
        Notifications notifications = Notifications.create();
        notifications.text(message);
        notifications.title("Success Message");
        notifications.hideAfter(Duration.seconds(30));
        notifications.darkStyle();
        notifications.position(Pos.BOTTOM_CENTER);
        notifications.show();
    }
}