package com.example.trokyy.controllers.Reclamation;

import com.example.trokyy.services.ReclamationService ;
import com.example.trokyy.models.Reclamation;
import com.example.trokyy.models.Reponse;
import com.example.trokyy.tools.MyDataBaseConnection;
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

public class UpdateReclamationController implements Initializable {
    private boolean update;
    @FXML
    private Button btnUploadImage;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField keywordTextField;

    @FXML
    private Label lblImagePath;

    @FXML
    private String image_path;

    @FXML
    private ChoiceBox<String> typeChoiceBox;

    private Reclamation selectedReclamation; // Ajoutez cet attribut pour stocker la réclamation sélectionnée
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        typeChoiceBox.getItems().addAll("conflit", "arnaque", "autre");

    }
    @FXML
    private void clearFields() {
        descriptionTextArea.clear();
        typeChoiceBox.getSelectionModel().clearSelection();
        lblImagePath.setText("");
        image_path = null; // Réinitialiser le chemin de l'image à null
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
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setReclamationToUpdate(Reclamation reclamation) {
        if (reclamation != null) {
            selectedReclamation = reclamation;
            descriptionTextArea.setText(reclamation.getDescription_reclamation());
            typeChoiceBox.setValue(reclamation.getType());
            lblImagePath.setText(reclamation.getImage_path());
            // Vous pouvez également mettre à jour lblImagePath si nécessaire
        }
    }

    public void setUpdate(boolean b) {
        this.update = b;

    }

    @FXML
    private Button updatebtn;

    @FXML
    private void updateReclamation(ActionEvent event) {
        // Vérifier si une réclamation est sélectionnée
        if (selectedReclamation == null) {
            showAlert("Veuillez sélectionner une réclamation à mettre à jour.");
            return;
        }

        // Récupérer les nouvelles valeurs des champs
        String description = descriptionTextArea.getText();
        String type = typeChoiceBox.getValue();
        String imagePath = lblImagePath.getText(); // Récupérer le chemin de l'image

        // Vérifier si les champs description et type sont vides
        if (description.isEmpty() || type == null || type.isEmpty()) {
            showAlert("Veuillez remplir tous les champs obligatoires.");
            return;
        }



        // Filtrer les mots interdits dans la description
        String[] motsInterdits = {"mot1", "mot2", "mot3"}; // Ajoutez vos mots interdits ici
        for (String motInterdit : motsInterdits) {
            description = description.replaceAll("(?i)" + motInterdit, "***");
        }


        // Mettre à jour la réclamation
        selectedReclamation.setDescription_reclamation(description);
        selectedReclamation.setType(type);
        selectedReclamation.setImage_path(imagePath); // Utiliser imagePath ici

        // Appeler la méthode updateReclamation de  service ReclamationService
        ReclamationService reclamationService = new ReclamationService();
        reclamationService.updateReclamation(selectedReclamation);

        // Afficher un message de succès
        showNotification("Complaint Updated successfully");

        // Vous pouvez également fermer la fenêtre de mise à jour si nécessaire
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

    @FXML
    private void handleSearch(ActionEvent event) {
        String keywords = keywordTextField.getText().trim(); // Récupérer les mots-clés saisis
        // Ici, nous allons simplement imprimer les mots-clés pour l'exemple
        System.out.println("Mots-clés saisis : " + keywords);
    }



}
