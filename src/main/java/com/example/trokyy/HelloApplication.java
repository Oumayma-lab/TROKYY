package com.example.trokyy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        //Parent root = FXMLLoader.load(getClass().getResource("FrontOffice/user/profile.fxml"));
        ///Parent root = FXMLLoader.load(getClass().getResource("backoffice/AdminMain.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("FrontOffice/user/Main.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Troky ");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
