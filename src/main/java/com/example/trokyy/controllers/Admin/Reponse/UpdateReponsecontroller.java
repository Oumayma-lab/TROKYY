package com.example.trokyy.controllers.Admin.Reponse;

import com.example.trokyy.models.Reponse;
import com.example.trokyy.services.ReponseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class UpdateReponsecontroller implements Initializable {



    private boolean update;
    @FXML
    private Button btnUploadImage;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField keywordTextField;



    private Reponse selectedReponse; // Ajoutez cet attribut pour stocker la reponse sélectionnée

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    private void clearFields() {
        descriptionTextArea.clear();

    }


        private void showAlert(String message) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }

    public void setReponseToUpdate(Reponse reponse) {
        if (reponse != null) {
            selectedReponse = reponse;
            descriptionTextArea.setText(reponse.getDescription());
            // Vous pouvez remplir d'autres champs avec les valeurs de la réponse si nécessaire
        }
    }




        @FXML
        private Button updatebtn;

        @FXML
        private void updateReponse(ActionEvent event) {
            // Vérifier
            if (selectedReponse == null) {
                showAlert("Aucune reponse à mettre à jour.");
                return;
            }

            // Récupérer les nouvelles valeurs des champs
            String description = descriptionTextArea.getText();


            // Filtrage des mots interdits
            String[] motsInterdits = {"mot1", "mot2", "mot3"}; // Ajoutez vos mots interdits ici
            for (String motInterdit : motsInterdits) {
                description = description.replaceAll("(?i)" + motInterdit, "***");
            }



            // Vérifier si les champs description et type sont vides
            if (description.isEmpty() ) {
                showAlert("Veuillez remplir tous les champs obligatoires.");
                return;
            }

            // Mettre à jour la reponse
            selectedReponse.setDescription(description);
            selectedReponse.setDate_reponse(new Date()); // Mettre à jour la date de réponse


            // Appeler la méthode updateReponse de  service Reponse
            ReponseService reponseService = new ReponseService();
            reponseService.updateReponse(selectedReponse);

            // Afficher un message de succès
            showNotification("Response Updated successfully");
            // Réinitialiser les champs après l'update
            clearFields();




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


