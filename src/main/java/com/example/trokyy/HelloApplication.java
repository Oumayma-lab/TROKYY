package com.example.trokyy;

import com.example.trokyy.controllers.ProfileController;
import com.example.trokyy.controllers.User.LogoutController;
import com.example.trokyy.tools.SessionManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.IOException;

public class HelloApplication extends Application {
    private String currentSessionId; // Assume you have a way to track the current session ID
    private Stage stage; // Assuming you have access to the stage

    @Override
    public void start(Stage stage) throws IOException {
        //Parent root = FXMLLoader.load(getClass().getResource("FrontOffice/user/profile.fxml"));
     //Parent root = FXMLLoader.load(getClass().getResource("backoffice/AdminMain.fxml"));

        Parent root = FXMLLoader.load(getClass().getResource("FrontOffice/user/Main.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Troky ");
        stage.setScene(scene);
        stage.show();

    }




    public void logoutCurrentUser() {
        // Invalidate the current session
        LogoutController logoutController = new LogoutController();
        logoutController.logout(currentSessionId);

        // Load the login screen
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FrontOffice/user/Main.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("Troky");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception (e.g., show error message)
        }

    }
    public static void main(String[] args) {
        launch(args);
    }
}
