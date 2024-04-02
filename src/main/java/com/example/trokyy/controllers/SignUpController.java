package com.example.trokyy.controllers;

import com.example.trokyy.models.UserDao;
import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.services.ServicePersonne;
import com.example.trokyy.tools.MyDataBaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.SQLException;

public class SignUpController {
    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField useremail;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private TextField confirmPassword;

    @FXML
    private Button registerButton;

    private final UserDao userDao = new UserDao();

    public SignUpController() {
        Connection connection = MyDataBaseConnection.getConnection();

    }


    public void registerUser(ActionEvent actionEvent) {
        String nom = firstName.getText();
        String prenom = lastName.getText();
        String email = useremail.getText();
        String mdp = password.getText();
        String confirmUserPassword = confirmPassword.getText();

        // Check if passwords match
       /* if (!mdp.equals(confirmUserPassword)) {
            System.err.println("Passwords do not match.");
            // You can handle this case as per your application requirements, e.g., show an error message
            return;
        }
       */
        if (!mdp.equals(confirmUserPassword)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Passwords do not match.");
            alert.showAndWait();
            return;
        }

        Utilisateur utilisateur = new Utilisateur(nom, prenom, email, mdp);

        //UserDao.createUser(utilisateur);
        userDao.createUser(utilisateur);
        System.out.println("User added successfully.");
        // You can add additional logic here such as showing a success message to the user
        // You can add additional logic here such as showing a success message to the user
    }
}
