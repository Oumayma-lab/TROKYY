package com.example.trokyy.controllers.User;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.trokyy.controllers.LogInController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class MainController implements Initializable {

    @FXML
    private Pane Container; // Assuming this is where you want to load the SignUp form
    @FXML
    private Pane FormContainer;
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization code
        loadLoginForm();


    }

    private void loadLoginForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/User/LogIn1.fxml"));
            Parent loginForm = loader.load();

            // Pass the Container to the LogInController
            LogInController loginController = loader.getController();
            loginController.setContainer(Container);

            Container.getChildren().setAll(loginForm);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading login form FXML file.");
        }
    }

    private void loadSignUpForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/User/signup.fxml"));
            Parent signUpForm = loader.load();
            FormContainer.getChildren().setAll(signUpForm);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading signup form FXML file.");
        }
    }



}
