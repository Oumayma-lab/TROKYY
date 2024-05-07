package com.example.trokyy.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;


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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/Reclamation/AjoutReclam.fxml"));
        Parent complaintInterface = loader.load();

        // Convertir Parent en Region pour pouvoir utiliser setPrefSize
        Region region = (Region) complaintInterface;

        // Obtenir la taille de l'écran
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Set la taille de l'interface pour correspondre à la taille de l'écran
        region.setPrefSize(screenWidth, screenHeight);

        // Ajouter l'interface de l'ajout de plainte à la fenêtre principale
        root.getChildren().setAll(complaintInterface);
    }



    // Méthode pour afficher l'interface de l'ajout d'offre
    @FXML
    private void showOfferInterface(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("offre.fxml"));
        Parent offerInterface = loader.load();
    // Convertir Parent en Region pour pouvoir utiliser setPrefSize
        Region region = (Region) offerInterface;

        // Obtenir la taille de l'écran
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Set la taille de l'interface pour correspondre à la taille de l'écran
        region.setPrefSize(screenWidth, screenHeight);

        // Ajouter l'interface de l'ajout d'offre à la fenêtre principale
        root.getChildren().setAll(offerInterface);
    }




    // Méthode pour afficher l'interface de l'ajout du blog
    @FXML
    private void showBlogInterface(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("blog.fxml"));
        Parent blogInterface = loader.load();
        // Convertir Parent en Region pour pouvoir utiliser setPrefSize
        Region region = (Region) blogInterface;

        // Obtenir la taille de l'écran
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Set la taille de l'interface pour correspondre à la taille de l'écran
        region.setPrefSize(screenWidth, screenHeight);



        // Ajouter l'interface de l'ajout du blog à la fenêtre principale
        root.getChildren().setAll(blogInterface);
    }

    // Méthode pour afficher l'interface de l'ajout du un event

    @FXML
    private void showEventInterface(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("event.fxml"));
        Parent eventInterface = loader.load();// Convertir Parent en Region pour pouvoir utiliser setPrefSize
        Region region = (Region) eventInterface;

        // Obtenir la taille de l'écran
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Set la taille de l'interface pour correspondre à la taille de l'écran
        region.setPrefSize(screenWidth, screenHeight);


        // Ajouter l'interface de l'ajout de l'événement à la fenêtre principale
        root.getChildren().setAll(eventInterface);
    }

    // Méthode pour afficher l'interface de l'ajout d'un don
    @FXML
    private void showDonInterface(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("don.fxml"));
        Parent donInterface = loader.load();
        // Convertir Parent en Region pour pouvoir utiliser setPrefSize
        Region region = (Region) donInterface;

        // Obtenir la taille de l'écran
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Set la taille de l'interface pour correspondre à la taille de l'écran
        region.setPrefSize(screenWidth, screenHeight);


        // Ajouter l'interface de l'ajout du don à la fenêtre principale
        root.getChildren().setAll(donInterface);
    }
    // Méthode pour afficher l'interface de consulter profil
    @FXML
    private void showProfileInterface(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("profile.fxml"));
        Parent profileInterface = loader.load();
        // Convertir Parent en Region pour pouvoir utiliser setPrefSize
        Region region = (Region) profileInterface;

        // Obtenir la taille de l'écran
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        // Set la taille de l'interface pour correspondre à la taille de l'écran
        region.setPrefSize(screenWidth, screenHeight);


        // Ajouter l'interface de consultation de profil à la fenêtre principale
        root.getChildren().setAll(profileInterface);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
