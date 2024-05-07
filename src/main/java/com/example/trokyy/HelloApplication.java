package com.example.trokyy;

import com.example.trokyy.models.Utilisateur;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        //Parent root = FXMLLoader.load(getClass().getResource("FrontOffice/Reclamation/ListReclam.fxml"));


        //Eya Blog:
        //Parent root = FXMLLoader.load(getClass().getResource("FrontOffice/Blog/AjoutBlog.fxml"));
       // Parent root = FXMLLoader.load(getClass().getResource("FrontOffice/Blog/ListBlog.fxml"));
          Parent root = FXMLLoader.load(getClass().getResource("Backoffice/BlogsManagement.fxml"));

        //Parent root = FXMLLoader.load(getClass().getResource("backoffice/AdminMain.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("FrontOffice/user/login.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Troky ");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
