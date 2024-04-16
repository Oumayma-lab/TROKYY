package com.example.trokyy.controllers.Reclamation;

import com.example.trokyy.models.Reclamation;
import com.example.trokyy.tools.AppQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class AjoutReclamController implements Initializable {
    private boolean update;

    @FXML
    private Button btnUploadImage;
    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private ChoiceBox<String> typeChoiceBox;

    @FXML
    private String image_path; // Modifier le type de l'attribut image_path

    @FXML
    private Label lblImagePath; // Déclarer un Label pour afficher le chemin de l'image

    @FXML
    private TextField keywordTextField;

    private Reclamation selectedReclamation; // Ajoutez cet attribut pour stocker la réclamation sélectionnée

    private final AppQuery appQuery = new AppQuery();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        typeChoiceBox.getItems().addAll("conflit", "arnaque", "autre");
    }





    @FXML
    private void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            image_path = selectedFile.getAbsolutePath();
            lblImagePath.setText(image_path);
        }
    }
    @FXML
    public void AddReclamation(ActionEvent event) {
        String description = descriptionTextArea.getText();
        // Liste des mots interdits
        String[] motsInterdits = {"mot1", "mot2", "mot3"}; // Ajoutez vos mots interdits ici

        // Parcourir la liste des mots interdits et remplacer chaque occurrence dans la description
        for (String motInterdit : motsInterdits) {
            // Utiliser une expression régulière insensible à la casse pour remplacer chaque occurrence du mot interdit
            description = description.replaceAll("(?i)" + motInterdit, "***");
        }


        String type = typeChoiceBox.getValue();

        if (description.isEmpty() || type == null || type.isEmpty()) {
            // Display an error message if the description or type is empty
            showAlert("Please fill out both the description and type fields.");
            return; // Exit the method early if the description or type is empty
        }

            // Sinon, ajoutez une nouvelle réclamation
            Reclamation reclamation = new Reclamation(description, type, image_path);
            appQuery.addReclamation(reclamation);
            // Afficher la notification
            showNotification("Complaint Added successfully");

            // Réinitialiser les champs après l'ajout
            clearFields();


    }



    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setReclamationToUpdate(Reclamation reclamation) {
        // Cette méthode est appelée pour préremplir les champs avec les valeurs de la réclamation sélectionnée
        selectedReclamation = reclamation;
        descriptionTextArea.setText(reclamation.getDescription_reclamation());
        typeChoiceBox.setValue(reclamation.getType());
        // Assurez-vous de mettre à jour l'image_path si nécessaire
    }
    void setUpdate(boolean b) {
        this.update = b;

    }

    void setTextField(int id, String description, String type) {
        // Mettre à jour les champs avec les valeurs de la réclamation existante
        descriptionTextArea.setText(description);
        typeChoiceBox.setValue(type);
    }





//tekhdem hmdlh
    public void btnNotifcationOnAction(ActionEvent actionEvent) {
       // Image image=new Image("src/main/resources/com/example/javatroky/error.png");

        Notifications notifications=Notifications.create();
        //notifications.graphic(new ImageView(image));
        notifications.text("Complaint Added succecfully");
        notifications.title("Success Message");
        notifications.hideAfter(Duration.seconds(4));
        notifications.darkStyle();
        notifications.position(Pos.BOTTOM_CENTER);
        notifications.show();


    }




    @FXML
    private void handleSearch(ActionEvent event) {
        String keywords = keywordTextField.getText().trim(); // Récupérer les mots-clés saisis
        // Ici, nous allons simplement imprimer les mots-clés pour l'exemple
        System.out.println("Mots-clés saisis : " + keywords);
    }


    @FXML
    private void clearFields() {
        descriptionTextArea.clear();
        typeChoiceBox.getSelectionModel().clearSelection();
        lblImagePath.setText("");
        image_path = null; // Réinitialiser le chemin de l'image à null
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