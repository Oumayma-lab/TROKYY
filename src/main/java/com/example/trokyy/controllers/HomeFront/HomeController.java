package com.example.trokyy.controllers.HomeFront;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private JFXButton blogbtn;

    @FXML
    private JFXButton complaintbtn;

    @FXML
    private JFXButton donbtn;

    @FXML
    private JFXButton eventbtn;

    @FXML
    private JFXButton homebtn;

    @FXML
    private TextField keywordTextField;

    @FXML
    private JFXButton offrebtn;

    @FXML
    private JFXButton profilbtn;

    @FXML
    private AnchorPane root;

    @FXML
    private VBox side;


    @FXML
    private void showComplaintInterface(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AjoutReclam.fxml"));
        Parent complaintInterface = loader.load();

        // Ajouter l'interface de l'ajout de plainte à la fenêtre principale
        root.getChildren().setAll(complaintInterface);
    }

    // Méthode pour afficher l'interface de l'ajout d'offre
    @FXML
    private void showOfferInterface(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("offre.fxml"));
        Parent offerInterface = loader.load();

        // Ajouter l'interface de l'ajout d'offre à la fenêtre principale
        root.getChildren().setAll(offerInterface);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
