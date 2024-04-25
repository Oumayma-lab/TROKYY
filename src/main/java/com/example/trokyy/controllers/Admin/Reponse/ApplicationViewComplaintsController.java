package com.example.trokyy.controllers.Admin.Reponse;

import com.example.trokyy.controllers.Reclamation.ListReclamController;
import com.example.trokyy.models.Reclamation;
import com.example.trokyy.tools.MyDataBaseConnection;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationViewComplaintsController implements Initializable {


    @FXML
    private TableColumn<Reclamation, Integer> idColumn;


    @FXML
    private TableView<Reclamation> complaintsTable;

    @FXML
    private TableColumn<Reclamation, String> dateColumn;

    @FXML
    private TableColumn<Reclamation, String> typeColumn;

    @FXML
    private TableColumn<Reclamation, String> descriptionColumn;

    @FXML
    private TableColumn<Reclamation, String> StatusColumn;

    @FXML
    private TableColumn<Reclamation, Image> imageColumn;

    private ObservableList<Reclamation> allComplaints;


    // Déclaration de la liste des réclamations comme une variable de membre
    private ObservableList<Reclamation> complaintsList = FXCollections.observableArrayList();



    // Method to retrieve data from the database
    public List<Reclamation> getAllComplaints() {
        MyDataBaseConnection connection = new MyDataBaseConnection();
        List<Reclamation> complaints = new ArrayList<>();
        String query = "SELECT * FROM reclamation ORDER BY date_reclamation DESC";
        try (Connection conn = connection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Reclamation complaint = new Reclamation();
                complaint.setId(resultSet.getInt("id"));

                complaint.setType(resultSet.getString("type"));
                complaint.setDescription_reclamation(resultSet.getString("description_reclamation"));
                complaint.setDate_reclamation(resultSet.getDate("date_reclamation"));
                //complaint.setDate_reclamation(resultSet.getTimestamp("date_reclamation"));
                complaint.setImage_path(resultSet.getString("image_path"));
                complaint.setStatut_reclamation(resultSet.getString("statut_reclamation"));
                complaint.setVu(resultSet.getBoolean("vu"));

                complaints.add(complaint);


            }
        } catch (SQLException e) {
            System.out.println("Error retrieving complaints: " + e.getMessage());
        }
        return complaints;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Applique le filtre de recherche
        searchFilter();
        // Initialiser la valeur par défaut du ChoiceBox statusFilterChoiceBox à "All"
        statusFilterChoiceBox.setValue("All");
        // Initialiser la valeur par défaut du ChoiceBox typeFilterChoiceBox à "All"
        typeFilterChoiceBox.setValue("All");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date_reclamation"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description_reclamation"));
        StatusColumn.setCellValueFactory(new PropertyValueFactory<>("statut_reclamation"));

        imageColumn.setCellValueFactory(cellData -> {
            String imagePath = cellData.getValue().getImage_path();
            if (imagePath != null && !imagePath.isEmpty()) {
                Image image = new Image(imagePath);
                return new SimpleObjectProperty<>(image);
            } else {
                return new SimpleObjectProperty<>(null);
            }
        });

        imageColumn.setCellFactory(column -> {
            return new TableCell<Reclamation, Image>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(Image image, boolean empty) {
                    super.updateItem(image, empty);
                    if (empty || image == null) {
                        setGraphic(null);
                    } else {
                        imageView.setImage(image);
                        imageView.setFitWidth(100);
                        imageView.setPreserveRatio(true);
                        setGraphic(imageView);
                    }
                }
            };
        });

        List<Reclamation> complaintsList = getAllComplaints();
        complaintsTable.getItems().addAll(complaintsList);

        ObservableList<Reclamation> observableComplaints = FXCollections.observableArrayList(complaintsList);

         // Applique le filtre de recherche
        searchFilter();

        // Initialiser la valeur par défaut du ChoiceBox statusFilterChoiceBox à "All"
        statusFilterChoiceBox.setItems(FXCollections.observableArrayList("All", "Resolved", "In Progress")); // Ajoutez les autres options selon vos besoins
        statusFilterChoiceBox.setValue("All");

        // Initialiser la valeur par défaut du ChoiceBox typeFilterChoiceBox à "All"
        typeFilterChoiceBox.setItems(FXCollections.observableArrayList("All","conflit", "arnaque", "autre")); // Ajoutez les autres options selon vos besoins
        typeFilterChoiceBox.setValue("All");


        TableColumn<Reclamation, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(260);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button respondButton = new Button("Add a Reponse");
            private final Button displayResponsesButton = new Button("More Details");

            {
                // Style du bouton respondButton
                respondButton.setStyle("-fx-background-color: #2252e5; " +
                        "-fx-background-radius: 30; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: 'Open Sans'; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 12px; " +
                        "-fx-min-width: 100px;"); // Largeur minimale de 120 pixels

                displayResponsesButton.setStyle("-fx-background-color: #28a745; " +
                        "-fx-background-radius: 30; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: 'Open Sans'; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 12px; " +
                        "-fx-min-width: 100px;"); // Largeur minimale de 120 pixels


                respondButton.setOnAction(event -> {
                    // Get the selected complaint
                    Reclamation selectedComplaint = getTableView().getItems().get(getIndex());

                    // Check if the complaint is resolved
                    if (selectedComplaint.getStatut_reclamation().equalsIgnoreCase("Resolved")) {
                        // Create a custom alert
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Complaint Resolved");
                        alert.setHeaderText("This complaint has already been resolved.");
                        alert.setContentText("No further response is needed.");
                        alert.showAndWait();
                    } else {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/Backoffice/ComplaintResponse/AjoutReponse.fxml"));
                            Parent parent = loader.load();
                            AjoutReponseController reponseController = loader.getController();
                            reponseController.setComplaintId(selectedComplaint.getId());
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();

                        } catch (IOException ex) {
                            Logger.getLogger(ApplicationViewComplaintsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                displayResponsesButton.setOnAction(event -> {
                    Reclamation selectedComplaint = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/Backoffice/ComplaintResponse/Display.fxml"));
                    try {
                        Parent parent = loader.load();
                        DisplayComplaintResponseController displayComplaintResponseController = loader.getController();
                        displayComplaintResponseController.setComplaintId(selectedComplaint.getId());

                        Stage stage = new Stage();
                        stage.setScene(new Scene(parent, 800, 600)); // Taille moyenne de l'interface
                        stage.initStyle(StageStyle.UTILITY);
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(AjoutReponseController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

                respondButton.getStyleClass().add("edit-button");
                displayResponsesButton.getStyleClass().add("delete-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Reclamation selectedComplaint = getTableView().getItems().get(getIndex());

                    if (selectedComplaint.getStatut_reclamation().equalsIgnoreCase("Resolved")) {
                        // Si la réclamation est résolue, ne pas afficher le bouton "Add a Response"
                        setGraphic(displayResponsesButton);
                    } else {
                        setGraphic(new HBox(20, respondButton, displayResponsesButton));
                    }
                }
            }
        });



        // Appliquer le style au statut de réclamation
        StatusColumn.setCellFactory(column -> new TableCell<>() {
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
        complaintsTable.getColumns().add(actionsColumn);


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

    @FXML
    private ChoiceBox<String> typeFilterChoiceBox;

    // Méthode appelée lors du changement de sélection dans le ChoiceBox de filtre de statut
    @FXML
    private void handleTypeFilterChange(ActionEvent event) {
        String selectedType = typeFilterChoiceBox.getValue();
        if (selectedType != null) {
            // Rafraîchir la table des réclamations en fonction du statut sélectionné
            refreshTableByType(selectedType);
        }
    }







    // Méthode pour rafraîchir la table des réclamations en fonction du statut sélectionné
    private void refreshTableByStatus(String status) {
        try {
            // Efface la liste des réclamations actuellement affichées
            complaintsList.clear();

            // Récupère une connexion à la base de données
            MyDataBaseConnection connection = new MyDataBaseConnection();
            Connection conn = connection.getConnection();
            // Définit la requête SQL pour sélectionner les réclamations en fonction du statut
            String query = "SELECT * FROM reclamation";
            if (!status.equals("All")) {
                query += " WHERE statut_reclamation = ?";
            }


            // Prépare la requête SQL
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            if (!status.equals("All")) {
                preparedStatement.setString(1, status);
            }

            // Exécute la requête SQL et récupère le résultat dans un objet ResultSet
            ResultSet resultSet = preparedStatement.executeQuery();

            // Parcourt le résultat de la requête SQL pour chaque réclamation trouvée
            while (resultSet.next()) {
                // Ajoute une nouvelle réclamation à la liste des réclamations à afficher dans la table
                complaintsList.add(new Reclamation(
                        resultSet.getInt("id"),
                        resultSet.getDate("date_reclamation"),
                        resultSet.getString("description_reclamation"),
                        resultSet.getString("statut_reclamation"),
                        resultSet.getString("type"),
                        resultSet.getString("image_path")
                ));
            }

            // Affecte la liste des réclamations à afficher à la table
            complaintsTable.setItems(complaintsList);

        } catch (SQLException ex) {
            // Gère toute exception SQLException en la journalisant
            Logger.getLogger(ApplicationViewComplaintsController.class.getName()).log(Level.SEVERE, null, ex);
        }



    }




    // Méthode pour rafraîchir la table des réclamations en fonction du type sélectionné
    private void refreshTableByType(String type) {
        try {
            // Efface la liste des réclamations actuellement affichées
            complaintsList.clear();

            // Récupère une connexion à la base de données
            MyDataBaseConnection connection = new MyDataBaseConnection();
            Connection conn = connection.getConnection();
            // Définit la requête SQL pour sélectionner les réclamations en fonction du type
            String query = "SELECT * FROM reclamation";
            if (!type.equals("All")) {
                query += " WHERE type = ?";
            }


            // Prépare la requête SQL
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            if (!type.equals("All")) {
                preparedStatement.setString(1, type);
            }

            // Exécute la requête SQL et récupère le résultat dans un objet ResultSet
            ResultSet resultSet = preparedStatement.executeQuery();

            // Parcourt le résultat de la requête SQL pour chaque réclamation trouvée
            while (resultSet.next()) {
                // Ajoute une nouvelle réclamation à la liste des réclamations à afficher dans la table
                complaintsList.add(new Reclamation(
                        resultSet.getInt("id"),
                        resultSet.getDate("date_reclamation"),
                        resultSet.getString("description_reclamation"),
                        resultSet.getString("statut_reclamation"),
                        resultSet.getString("type"),
                        resultSet.getString("image_path")
                ));
            }

            // Affecte la liste des réclamations à afficher à la table
            complaintsTable.setItems(complaintsList);

        } catch (SQLException ex) {
            // Gère toute exception SQLException en la journalisant
            Logger.getLogger(ApplicationViewComplaintsController.class.getName()).log(Level.SEVERE, null, ex);
        }



    }

    @FXML
    private TextField SearchBar;
    private void searchFilter () {
        // Création d'une liste filtrée initialisée avec la liste de réclamations et un prédicat permettant de filtrer toutes les réclamations par défaut
        FilteredList<Reclamation> filterData = new FilteredList<>(complaintsList, e -> true);

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
            reclamations.comparatorProperty().bind(complaintsTable.comparatorProperty());
            // Mise à jour de la table des réclamations avec la liste triée
            complaintsTable.setItems(reclamations);
        });
    }


   /* @FXML
    private void refresh() {
        // Rafraîchir la liste des réclamations à partir de la base de données
        List<Reclamation> refreshedComplaints = getAllComplaints();

        // Effacer la liste actuelle des réclamations affichées dans la table
        complaintsList.clear();

        // Ajouter les réclamations rafraîchies à la liste des réclamations à afficher dans la table
        complaintsList.addAll(refreshedComplaints);

        // Rafraîchir la table des réclamations avec la liste mise à jour
        complaintsTable.setItems(complaintsList);
    }*/

    @FXML
    private void refresh() {

        try {
            // Efface la liste des réclamations actuellement affichées
            complaintsList.clear();

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
                complaintsList.add(new Reclamation(
                        resultSet.getInt("id"),
                        resultSet.getDate("date_reclamation"),
                        resultSet.getString("description_reclamation"),
                        resultSet.getString("statut_reclamation"),
                        resultSet.getString("type"),
                        resultSet.getString("image_path")
                ));
            }

            // Affecte la liste des réclamations à afficher à la table
            complaintsTable.setItems(complaintsList);

        } catch (SQLException ex) {
            // Gère toute exception SQLException en la journalisant
            Logger.getLogger(ApplicationViewComplaintsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


}

//par defaut sont triée par l'ordre decroi de date