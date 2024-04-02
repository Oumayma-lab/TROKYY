package com.example.trokyy.controllers;

import com.example.trokyy.models.personne;
import com.example.trokyy.services.ServicePersonne; // Import ServicePersonne class
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class AfficherPersonneController {
    @FXML
    private Label idLabel;

    @FXML
    private Label nomLabel;

    @FXML
    private Label prenomLabel;

    // Create an instance of ServicePersonne
    private final ServicePersonne servicePersonne = new ServicePersonne();

    // Method to fetch and display personne data
    public void initialize() {
        afficherPersonne();
    }

    public void afficherPersonne() {
        ArrayList<personne> personnes = servicePersonne.getAll();
        for (personne currentPersonne : personnes) {
            idLabel.setText(String.valueOf(currentPersonne.getId()));
            nomLabel.setText(currentPersonne.getNom());
            prenomLabel.setText(currentPersonne.getPrenom());

            // Add labels to your UI layout, e.g., VBox or GridPane
            // Add idLabel, nomLabel, prenomLabel to your UI layout here
        }
    }



}
