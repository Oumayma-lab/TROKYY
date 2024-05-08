package com.example.trokyy.controllers.Blog;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.io.File;
import java.util.Base64;

public class GeminiImageDescription extends Application {

    private static final String GEMINI_API_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro-vision:generateContent";
    private static final String GOOGLE_API_KEY = "AIzaSyCeqj4NkiCqAXvlJz6Yx5xuAwl7Ihbdhbo";

    private TextArea chatArea;
    private TextField inputField;
    private Button sendButton;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gemini Image Description");

        // UI components
        chatArea = new TextArea();
        chatArea.setEditable(false);
        inputField = new TextField();
        inputField.setPromptText("Enter image file path...");
        sendButton = new Button("Generate Description");
        sendButton.setOnAction(event -> generateDescription());

        // Layout
        VBox chatBox = new VBox(chatArea, inputField, sendButton);
        chatBox.setSpacing(10);
        chatBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(chatBox);

        // Scene
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void generateDescription() {
        String imagePath = inputField.getText().trim();
        if (!imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                try {
                    String description = getImageDescription(imageFile);
                    chatArea.setText(description);
                } catch (Exception ex) {
                    chatArea.setText("Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else {
                chatArea.setText("Error: Image file not found.");
            }
        } else {
            chatArea.setText("Please enter the path to an image file.");
        }
    }

    private String getImageDescription(File imageFile) {
        String base64Image = encodeImageToBase64(imageFile);
//        String requestJson = String.format("{\"contents\":[{\"parts\":[{\"text\"😕"What is this picture?\"},{\"inline_data\":{\"mime_type\"😕"image/jpeg\",\"data\"😕"%s\"}}]}]}", base64Image);
        String requestJson = String.format("{\"contents\":[{\"parts\":[{\"text\":\"What is this picture?\"},{\"inline_data\":{\"mime_type\":\"image/jpeg\",\"data\":\"%s\"}}]}]}", base64Image);

        HttpResponse<JsonNode> response = Unirest.post(GEMINI_API_ENDPOINT)
                .header("Content-Type", "application/json")
                .queryString("key", GOOGLE_API_KEY)
                .body(requestJson)
                .asJson();

        return response.getBody().getObject().getJSONArray("candidates")
                .getJSONObject(0).getJSONObject("content")
                .getJSONArray("parts").getJSONObject(0)
                .getString("text");
    }

    private String encodeImageToBase64(File imageFile) {
        String encodedString = null;
        try {
            byte[] fileContent = java.nio.file.Files.readAllBytes(imageFile.toPath());
            encodedString = Base64.getEncoder().encodeToString(fileContent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return encodedString;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

