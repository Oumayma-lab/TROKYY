package com.example.trokyy.controllers.Reclamation;

import com.example.trokyy.controllers.Admin.Reponse.ShowResponseController;
import com.example.trokyy.models.Pdf;
import com.example.trokyy.models.Reclamation;
import com.example.trokyy.services.DisplayQuery;
import com.example.trokyy.tools.MyDataBaseConnection;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListReclamController implements Initializable {



    @FXML
    private TextField SearchBar;

    @FXML
    private TableColumn<Reclamation, String> DateCol;

    @FXML
    private TableColumn<Reclamation, String> DescriptionCol;

    @FXML
    private TableColumn<Reclamation, String> IDCol;

    @FXML
    private TableColumn<Reclamation, String> StatutCol;

    @FXML
    private TableView<Reclamation> reclamationtable;

    @FXML
    private TableColumn<Reclamation, String> TypeCol;

    @FXML
    private TableColumn<Reclamation, String> ImageCol;

    @FXML
    private TableColumn<Reclamation, String> editCol;


    private DisplayQuery displayQuery  ;

    String query = null;
    MyDataBaseConnection connection ; // Initialisation de l'attribut connection

    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Reclamation reclamation = null ;

    // Liste observable pour stocker les réclamations
    ObservableList<Reclamation>  ReclamationList = FXCollections.observableArrayList();



    // Méthode pour fermer la fenêtre
    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    // Méthode pour ouvrir la vue d'ajout de réclamation
    @FXML
    private void getAddView(MouseEvent event) {
        try {
            // Load the AddReclam.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/Reclamation/AjoutReclam.fxml"));
            Parent parent = loader.load();

            // Get the controller associated with the AjoutReclam.fxml file
            AjoutReclamController ajoutReclamController = loader.getController();



            // Show the AddReclam scene in a new stage
            Scene scene = new Scene(parent, 800, 600);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ListReclamController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }




    // Méthode pour rafraîchir la table des réclamations
    @FXML
    private void refreshtable() {

        try {
            // Efface la liste des réclamations actuellement affichées
              ReclamationList.clear();

            // Récupère une connexion à la base de données
            Connection connection = new MyDataBaseConnection().getConnection();

            // Définit la requête SQL pour sélectionner toutes les réclamations
            String query = "SELECT * FROM reclamation";

            // Prépare la requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(query); // Utilisation de la connexion correcte

            // Exécute la requête SQL et récupère le résultat dans un objet ResultSet
            ResultSet resultSet = preparedStatement.executeQuery();

            // Parcourt le résultat de la requête SQL pour chaque réclamation trouvée
            while (resultSet.next()) {
                // Ajoute une nouvelle réclamation à la liste des réclamations à afficher dans la table
                ReclamationList.add(new Reclamation(
                        resultSet.getInt("id"),
                        resultSet.getDate("date_reclamation"),
                        resultSet.getString("description_reclamation"),
                        resultSet.getString("statut_reclamation"),
                        resultSet.getString("type"),
                        resultSet.getString("image_path")
                ));
            }

            // Affecte la liste des réclamations à afficher à la table
            reclamationtable.setItems(ReclamationList);

        } catch (SQLException ex) {
            // Gère toute exception SQLException en la journalisant
            Logger.getLogger(ListReclamController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }




    // Initialisation du contrôleur
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Charger les données dans la table
        loadDate();

        // Initialiser la connexion à la base de données
        connection = new MyDataBaseConnection();

        // Initialisation des colonnes de la table
        IDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        DateCol.setCellValueFactory(new PropertyValueFactory<>("date_reclamation"));
        DescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description_reclamation"));
        StatutCol.setCellValueFactory(new PropertyValueFactory<>("statut_reclamation"));
        TypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        //ImageCol.setCellValueFactory(new PropertyValueFactory<>("image_path"));
        // Configuration de la colonne ImageCol pour afficher une image
        ImageCol.setCellValueFactory(new PropertyValueFactory<>("image_path"));
        ImageCol.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);

                if (empty || imagePath == null || imagePath.isEmpty()) {
                    // Si la cellule est vide ou si le chemin de l'image est null ou vide, affichez une image vide
                    imageView.setImage(null);
                    setGraphic(null);
                } else {
                    // Charger l'image à partir du chemin spécifié et l'afficher dans la cellule
                    Image image = new Image(imagePath);
                    imageView.setImage(image);
                    imageView.setFitWidth(50); // Réglez la largeur de l'image
                    imageView.setFitHeight(50); // Réglez la hauteur de l'image
                    setGraphic(imageView);
                }
            }



        });

        // Affecte la liste des réclamations à la table
        reclamationtable.setItems(ReclamationList);







        // Appliquer le style au statut de réclamation
        StatutCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String statut, boolean empty) {
                super.updateItem(statut, empty);
                if (statut == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label label = new Label(statut.equals("Resolved") ? "Resolved" : "In progress");
                    label.getStyleClass().add("badge");
                    label.getStyleClass().add(statut.equals("Resolved") ? "badge-success" : "badge-danger");
                    label.setStyle("-fx-border-radius: 10px;-fx-text-fill: white;"); // Définir le rayon de bordure
                    setGraphic(label);
                }
            }
        });







        // Applique le filtre de recherche
        searchFilter();

        // Initialiser displayQuery
        displayQuery = new DisplayQuery();

        ObservableList<String> types = FXCollections.observableArrayList("All", "arnaque", "autre");
        typeFilterChoiceBox.setItems(types);


    }



    private void loadDate() {

        // Récupérer la connexion à la base de données

        Connection connection = new MyDataBaseConnection().getConnection();

        // Rafraîchir la table des réclamations
        refreshtable();

        // Configuration des colonnes de la table avec leurs propriétés de valeur
        IDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        DateCol.setCellValueFactory(new PropertyValueFactory<>("date_reclamation"));
        DescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description_reclamation"));
        StatutCol.setCellValueFactory(new PropertyValueFactory<>("statut_reclamation"));
        TypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        ImageCol.setCellValueFactory(new PropertyValueFactory<>("image_path"));

        // Ajout d'une cellule contenant des boutons d'édition et de suppression
        Callback<TableColumn<Reclamation, String>, TableCell<Reclamation, String>> cellFactory = (TableColumn<Reclamation, String> param) -> {
            // Création d'une cellule contenant des boutons
            final TableCell<Reclamation, String> cell = new TableCell<Reclamation, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    // Cette cellule est créée uniquement pour les lignes non vides
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {

                        // Récupérer la réclamation associée à cette ligne
                        Reclamation currentReclamation = getTableView().getItems().get(getIndex());




                        // Création des icônes de bouton d'édition et de suppression
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        deleteIcon.getStyleClass().add("delete-icon");

                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);
                        editIcon.getStyleClass().add("edit-icon");

                        FontAwesomeIconView responseIcon = new FontAwesomeIconView(FontAwesomeIcon.COMMENT_ALT);
                        responseIcon.getStyleClass().add("response-icon");



                        // Récupérer la réclamation associée à cette ligne
                        // Reclamation currentReclamation = getTableView().getItems().get(getIndex());




                        // Récupérer la date de création de la réclamation
                        Date reclamationDate = currentReclamation.getDate_reclamation();
                        Date utilDate = new Date(reclamationDate.getTime()); // Conversion en java.util.Date
                        LocalDateTime reclamationLocalDateTime = LocalDateTime.ofInstant(utilDate.toInstant(), ZoneId.systemDefault());

                     // Calculer la durée entre la date de création de la réclamation et la date actuelle
                        LocalDateTime now = LocalDateTime.now();
                        Duration duration = Duration.between(reclamationLocalDateTime, now);

                      // Vérifier si la durée est supérieure à 48 heures
                        boolean isOver48Hours = duration.toHours() > 2;



                        // Action de suppression lors du clic sur l'icône de suppression
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            if (!isOver48Hours) {
                                // Afficher l'alerte de confirmation
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation");
                                alert.setHeaderText(null);
                                alert.setContentText("Are you sure you want to delete this complaint?");

                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    try {
                                        // Supprimer la réclamation uniquement si l'utilisateur clique sur "OK"
                                        reclamation = getTableView().getItems().get(getIndex());
                                        query = "DELETE FROM `reclamation` WHERE id  =" + reclamation.getId();
                                        Connection connection = new MyDataBaseConnection().getConnection();
                                        preparedStatement = connection.prepareStatement(query);
                                        preparedStatement.execute();



                                    } catch (SQLException ex) {
                                        Logger.getLogger(ListReclamController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        });
                        // Action d'édition lors du clic sur l'icône d'édition
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            if (!isOver48Hours) {
                                // Afficher l'alerte de confirmation
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation");
                                alert.setHeaderText(null);
                                alert.setContentText("Are you sure you want to update this complaint?");

                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    // Mettre à jour la réclamation uniquement si l'utilisateur clique sur "OK"
                                    try {
                                        reclamation = getTableView().getItems().get(getIndex());
                                        FXMLLoader loader = new FXMLLoader();
                                        loader.setLocation(getClass().getResource("/com/example/trokyy/FrontOffice/Reclamation/UpdateReclamation.fxml"));
                                        loader.load();
                                        UpdateReclamationController updateReclamationController = loader.getController();
                                        updateReclamationController.setUpdate(true);
                                        updateReclamationController.setReclamationToUpdate(reclamation);
                                        Parent parent = loader.getRoot();
                                        Stage stage = new Stage();
                                        stage.setScene(new Scene(parent, 800, 600));
                                        stage.initStyle(StageStyle.UTILITY);
                                        stage.showAndWait();

                                        if (updateReclamationController.isSaved()) {
                                            displayQuery = new DisplayQuery();
                                            List<Reclamation> complaints = displayQuery.getAllComplaints();
                                            reclamationtable.getItems().addAll(complaints);
                                            refreshtable();

                                        }
                                    } catch (IOException ex) {
                                        Logger.getLogger(ListReclamController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        });

                       // Récupérer la réclamation associée à cette ligne
                        //  Reclamation currentReclamation = getTableView().getItems().get(getIndex());

                        // Vérifier si la réclamation a une réponse et n'est pas vue
                        if (currentReclamation.getStatut_reclamation().equals("Resolved") ) {
                            // Définir l'action lors du clic sur l'icône de réponse
                            responseIcon.setOnMouseClicked(event -> ShowResponse(currentReclamation));
                            // Afficher l'icône de réponse dans la cellule
                            setGraphic(new HBox(editIcon, deleteIcon, responseIcon));
                            if (isOver48Hours) {
                                deleteIcon.setVisible(false);
                                editIcon.setVisible(false);
                            }
                        } else {
                            // Afficher les icônes d'édition et de suppression uniquement
                            setGraphic(new HBox(editIcon, deleteIcon));
                            if (isOver48Hours) {
                                deleteIcon.setVisible(false);
                                editIcon.setVisible(false);
                            }
                        }

                        setText(null);
                    }
                }
            };

            return cell;
        };

// Définition de la cellule d'édition pour la colonne correspondante
        editCol.setCellFactory(cellFactory);

        // Affectation de la liste des réclamations à la table
        reclamationtable.setItems(ReclamationList);
    }

    @FXML
    private ChoiceBox<String> statusFilterChoiceBox;

    // Méthode appelée lors du changement de sélection dans le ChoiceBox de filtre de statut
    @FXML
    private void handleStatusFilterChange(ActionEvent event) {
        String selectedStatus = statusFilterChoiceBox.getValue();
        if (selectedStatus != null) {
            // Rafraîchir la table des réclamations en fonction du statut sélectionné
            refreshTableByStatus(selectedStatus);
        }
    }
    // Méthode pour rafraîchir la table des réclamations en fonction du statut sélectionné
    private void refreshTableByStatus(String status) {
        try {
            // Efface la liste des réclamations actuellement affichées
            ReclamationList.clear();

            // Récupère une connexion à la base de données
            Connection connection = new MyDataBaseConnection().getConnection();

            // Définit la requête SQL pour sélectionner les réclamations en fonction du statut
            String query = "SELECT * FROM reclamation";
            if (!status.equals("All")) {
                query += " WHERE statut_reclamation = ?";
            }

            // Prépare la requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            if (!status.equals("All")) {
                preparedStatement.setString(1, status);
            }

            // Exécute la requête SQL et récupère le résultat dans un objet ResultSet
            ResultSet resultSet = preparedStatement.executeQuery();

            // Parcourt le résultat de la requête SQL pour chaque réclamation trouvée
            while (resultSet.next()) {
                // Ajoute une nouvelle réclamation à la liste des réclamations à afficher dans la table
                ReclamationList.add(new Reclamation(
                        resultSet.getInt("id"),
                        resultSet.getDate("date_reclamation"),
                        resultSet.getString("description_reclamation"),
                        resultSet.getString("statut_reclamation"),
                        resultSet.getString("type"),
                        resultSet.getString("image_path")
                ));
            }

            // Affecte la liste des réclamations à afficher à la table
            reclamationtable.setItems(ReclamationList);

        } catch (SQLException ex) {
            // Gère toute exception SQLException en la journalisant
            Logger.getLogger(ListReclamController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    //tekhdem jawha behy hmd
    private void searchFilter () {
        // Création d'une liste filtrée initialisée avec la liste de réclamations et un prédicat permettant de filtrer toutes les réclamations par défaut
        FilteredList<Reclamation> filterData = new FilteredList<>(ReclamationList, e -> true);

        // Définition de l'action à effectuer lorsqu'une touche est relâchée dans la barre de recherche
        SearchBar.setOnKeyReleased(e -> {
            // Ajout d'un écouteur sur la propriété text de la barre de recherche pour mettre à jour le prédicat à chaque modification de texte
            SearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
                // Mise à jour du prédicat en fonction du nouveau texte de recherche
                filterData.setPredicate((Predicate<? super Reclamation>) reclamation -> {
                    // Si le texte de recherche est vide, toutes les réclamations sont affichées
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    // Convertir le texte de recherche en minuscules
                    String toLowerCaseFilter = newValue.toLowerCase();
                    // Vérifier si le type, le statut ou la description de la réclamation contient le texte de recherche
                    if (reclamation.getType().toLowerCase().contains(toLowerCaseFilter)) {
                        return true;
                    } else if (reclamation.getStatut_reclamation().toLowerCase().contains(toLowerCaseFilter)) {
                        return true;
                    } else if (reclamation.getDescription_reclamation().toLowerCase().contains(toLowerCaseFilter)) {
                        return true;
                    }
                    // Si aucune correspondance n'est trouvée, la réclamation est filtrée
                    return false;
                });
            });

            // Création d'une liste triée à partir de la liste filtrée
            final SortedList<Reclamation> reclamations = new SortedList<>(filterData);
            // Liaison du comparateur de la liste triée avec le comparateur de la table des réclamations
            reclamations.comparatorProperty().bind(reclamationtable.comparatorProperty());
            // Mise à jour de la table des réclamations avec la liste triée
            reclamationtable.setItems(reclamations);
        });
    }

    public static void showAlert (String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    @FXML
    private void showDetails(ActionEvent event) {
        // Récupérer la réclamation sélectionnée dans la liste
        Reclamation selectedReclamation = reclamationtable.getSelectionModel().getSelectedItem();

        if (selectedReclamation != null) {
            try {
                // Charger le fichier FXML de la nouvelle fenêtre
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/Reclamation/ShowReclam.fxml"));
                Parent root = loader.load();

                // Obtenir le contrôleur de la nouvelle fenêtre
                ShowReclamController showReclamController = loader.getController();

                // Passer la réclamation sélectionnée au contrôleur de la nouvelle fenêtre
                showReclamController.showReclamationDetails(selectedReclamation);

                // Créer une nouvelle scène
                Scene scene = new Scene(root);

                // Créer une nouvelle fenêtre
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();



            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Aucune réclamation sélectionnée, afficher un message d'alerte
            showAlert("Please select a Reclamation to show details.");
        }
    }

    @FXML
    private void ShowResponse(Reclamation complaintToShowResponse) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/Reponse/ShowResponse.fxml"));
            Parent root = loader.load();

            ShowResponseController controller = loader.getController();
            String response = displayQuery.getComplaintResponse(complaintToShowResponse.getId());
            controller.setResponse(response);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Complaint Response");
            stage.showAndWait();

            complaintToShowResponse.setVu(true);
            markResponseVu(complaintToShowResponse.getId());
            refreshtable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void markResponseVu ( int complaintId){
        MyDataBaseConnection connection = new MyDataBaseConnection();

        String query = "UPDATE reclamation SET vu = ? WHERE id = ?";
        try (Connection conn = connection. getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setBoolean(1, true);
            statement.setInt(2, complaintId);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error marking response as seen by user: " + e.getMessage());
        }





    }


    //Afficher l'interface AjoutReclam
    public void switchToAnotherView2(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/Reclamation/AjoutReclam.fxml"));
            Parent root = loader.load();
// Get the controller associated with the AjoutReclam.fxml file
            AjoutReclamController ajoutReclamController = loader.getController();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void PDF(MouseEvent event) {
        // Récupérer la réclamation sélectionnée dans la liste
        Reclamation selectedReclamation = reclamationtable.getSelectionModel().getSelectedItem();

        if (selectedReclamation != null) {
            // Créer un sélecteur de fichiers pour choisir l'emplacement où enregistrer le PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF");
            fileChooser.setInitialFileName("MesInformations.pdf");

            // Afficher la boîte de dialogue pour enregistrer le fichier et obtenir le chemin du fichier choisi
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                // Si l'utilisateur a choisi un emplacement, générez le PDF et enregistrez-le à cet emplacement
                Pdf pd = new Pdf();
                try {
                    pd.GeneratePdf(file.getAbsolutePath(), selectedReclamation, selectedReclamation.getId());
                    System.out.println("PDF saved successfully.");

                } catch (Exception ex) {
                    Logger.getLogger(ListReclamController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            showAlert("Please select a Reclamation to generate PDF.");
        }
    }

    @FXML
    private ChoiceBox<String> typeFilterChoiceBox;
    @FXML
    private void handleTypeFilterChange(ActionEvent event) {
        String selectedType = typeFilterChoiceBox.getValue();
        if (selectedType != null) {
            // Rafraîchir la table des réclamations en fonction du type sélectionné
            refreshTableByType(selectedType);
        }
    }
    private void refreshTableByType(String type) {
        try {
            // Efface la liste des réclamations actuellement affichées
            ReclamationList.clear();

            // Récupère une connexion à la base de données
            Connection connection = new MyDataBaseConnection().getConnection();

            // Définit la requête SQL pour sélectionner les réclamations en fonction du type
            String query = "SELECT * FROM reclamation";
            if (!type.equals("All")) {
                query += " WHERE type = ?";
            }

            // Prépare la requête SQL
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            if (!type.equals("All")) {
                preparedStatement.setString(1, type);
            }

            // Exécute la requête SQL et récupère le résultat dans un objet ResultSet
            ResultSet resultSet = preparedStatement.executeQuery();

            // Parcourt le résultat de la requête SQL pour chaque réclamation trouvée
            while (resultSet.next()) {
                // Ajoute une nouvelle réclamation à la liste des réclamations à afficher dans la table
                ReclamationList.add(new Reclamation(
                        resultSet.getInt("id"),
                        resultSet.getDate("date_reclamation"),
                        resultSet.getString("description_reclamation"),
                        resultSet.getString("statut_reclamation"),
                        resultSet.getString("type"),
                        resultSet.getString("image_path")
                ));
            }

            // Affecte la liste des réclamations à afficher à la table
            reclamationtable.setItems(ReclamationList);

        } catch (SQLException ex) {
            // Gère toute exception SQLException en la journalisant
            Logger.getLogger(ListReclamController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}