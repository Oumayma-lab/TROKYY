package com.example.trokyy.controllers.Reclamation;

import com.example.trokyy.controllers.Admin.AdminMainController;
import com.example.trokyy.models.Captcha;
import com.example.trokyy.models.Reclamation;
import com.example.trokyy.services.ReclamationService;

import com.example.trokyy.tools.Chatbot;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.Future;
import java.util.regex.Pattern;


import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.AnchorPane;


import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import com.microsoft.cognitiveservices.speech.*;
import javafx.scene.control.Button;

public class AjoutReclamController implements Initializable {
    @FXML
    private JFXButton btncaptcha;
    @FXML
    private AnchorPane root;

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

    private Reclamation selectedReclamation; //stocker la réclamation sélectionnée

    private final ReclamationService reclamationService = new ReclamationService();

    @FXML
    private Button chatbot;


    @FXML
    private Button openChatButton; // Button to open chat

    @FXML
    private void startConversation(ActionEvent event) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
        System.out.println("Starting a conversation...");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatbot.setOnAction(event -> startChatbot());
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
    public void AddReclamation(ActionEvent event)  {
        if (captchaController != null && captchaController.isCaptchaVerified()) {
        String description = descriptionTextArea.getText();
        String type = typeChoiceBox.getValue();

        if (description.isEmpty() || type == null || type.isEmpty()) {
            showAlert("Please fill out both the description and type fields.");
            return;
        }

        // Filtrage des mots interdits
        description = filterBadWords(description);



        // Vérifier si la description est unique
        if (!reclamationService.isDescriptionUnique(description)) {
            showAlert("The description of the complaint already exists. Please enter a different one.");
            return;
        }

        // Si la description est unique, continuer avec l'ajout de la réclamation
        Reclamation reclamation = new Reclamation(description, type, image_path);
        reclamationService.addReclamation(reclamation);
        showNotification("Complaint Added successfully.");
        clearFields();


    }
     else {
        showAlert("Captcha Incorrect Veuillez saisir le captcha correct.");
    }}


    // filterBadWords
    private String filterBadWords(String description) {
        String[] motsInterdits = {  "f r a u d",  "s p a m", "v i o l e n c e","h a t e", "t e r r o r i s m", "t e r r o r i s m",
            "d a n g e r"};
        for (String motInterdit : motsInterdits) {
            // Échappez les caractères spéciaux pour s'assurer que la regex fonctionne correctement
            motInterdit = motInterdit.replaceAll("([\\\\{}()\\[\\].+*?^$|])", "\\\\$1");
            // Remplacez les espaces par des expressions régulières correspondant à n'importe quel espace
            motInterdit = motInterdit.replaceAll(" ", "\\\\s*");
            description = description.replaceAll("(?i)" + motInterdit, "***");
        }
        return description;
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
        notifications.hideAfter(Duration.seconds(10));
        notifications.darkStyle();
        notifications.position(Pos.BOTTOM_CENTER);
        notifications.show();
    }



    private void startChatbot() {
        Chatbot chatbot = new Chatbot();
        Stage chatbotStage = new Stage();
        chatbot.start(chatbotStage);
    }




@FXML
    public void switchToAnotherView1(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/Reclamation/List.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    @FXML
    void verifierr(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/Reclamation/captcha.fxml"));
        Parent root = loader.load();
        captchaController = loader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private Captcha captchaController;








    @FXML
    private Text Repreponse;
    @FXML
    void stt(MouseEvent event){
        // Create a new Task for asynchronous speech recognition
        Task<String> recognitionTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                // Replace with your subscription key and region
                SpeechConfig speechConfig = SpeechConfig.fromSubscription("9060f63ba0654e7b8533abfa8e407a6e", "westeurope");

                try (SpeechRecognizer speechRecognizer = new SpeechRecognizer(speechConfig, AudioConfig.fromDefaultMicrophoneInput())) {
                    System.out.println("Speak into your microphone."); // Inform user to speak

                    Future<SpeechRecognitionResult> task = speechRecognizer.recognizeOnceAsync();
                    SpeechRecognitionResult result = task.get();

                    if (result.getReason() == ResultReason.Canceled) {
                        System.out.println("Cancellation detected.");
                        return null;
                    } else if (result.getReason() == ResultReason.NoMatch) {
                        System.out.println("No speech recognized.");
                        return null;
                    } else {
                        String recognizedText = result.getText();
                        //System.out.println("Recognized text: " + recognizedText);
                        return recognizedText;
                    }
                }
            }
        };

        // Start the recognition task and handle the result
        recognitionTask.setOnSucceeded(event1 -> {
            String transcribedText = recognitionTask.getValue();
            if (transcribedText != null) {
                // Update UI element (if desired)
                Repreponse.setText(transcribedText);
            }
        });

        recognitionTask.setOnFailed(event1 -> {
            Throwable exception = recognitionTask.getException();
            System.err.println("Speech recognition failed: " + exception.getMessage());
        });

        new Thread(recognitionTask).start();
    }
    }