package com.example.trokyy.controllers;

import com.example.trokyy.services.UserDao;
import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.tools.MyDataBaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class EditProfileController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField addresseField;
    @FXML
    private TextField telField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    private UserDao userDao = new UserDao();
    private int userId;

    public EditProfileController() {
       // this.userDao = userDao;
        Connection connection = MyDataBaseConnection.getConnection();
    }
// Other fields and methods

    public void initialize(int userId) throws SQLException {
        this.userId = userId;
        Utilisateur utilisateur = userDao.getUserById(userId);
        if (utilisateur != null) {
            nameField.setText(utilisateur.getNom());
            emailField.setText(utilisateur.getEmail());
            usernameField.setText(utilisateur.getUsername());
            addresseField.setText(utilisateur.getAdresse());
            telField.setText(String.valueOf(utilisateur.getTelephone()));
        }
    }
    @FXML
    private void saveChanges() {
        Utilisateur updatedUser = new Utilisateur();
        updatedUser.setNom(nameField.getText());
        updatedUser.setPrenom(prenomField.getText());
        updatedUser.setEmail(emailField.getText());
        updatedUser.setUsername(usernameField.getText());
        updatedUser.setAdresse(addresseField.getText());
        updatedUser.setTelephone(Integer.parseInt(telField.getText()));
        try {
            userDao.updateUser(userId, updatedUser);
            // Refresh the user data from the database
            Utilisateur refreshedUser = userDao.getUserById(userId);
            // Update the UI with the refreshed data
            nameField.setText(refreshedUser.getNom());
            prenomField.setText(refreshedUser.getPrenom());
            emailField.setText(refreshedUser.getEmail());
            usernameField.setText(refreshedUser.getUsername());
            addresseField.setText(refreshedUser.getAdresse());
            telField.setText(String.valueOf(refreshedUser.getTelephone()));
            Stage editStage = (Stage) saveButton.getScene().getWindow();
            editStage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelEdit() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
