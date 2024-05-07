package com.example.trokyy.controllers.User;


import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CaptureImageDialogController {

    @FXML
    private ImageView imageView;

    private Stage stage;

    public void setImage(Image image) {
        imageView.setImage(image);
    }

    @FXML
    private void closeDialog() {
        stage = (Stage) imageView.getScene().getWindow();
        stage.close();
    }
}