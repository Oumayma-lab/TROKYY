package com.example.trokyy.controllers.Admin.Reponse;

import com.example.trokyy.controllers.Admin.AdminMainController;
import com.example.trokyy.models.Reclamation;
import com.example.trokyy.models.Reponse;
import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.services.DisplayQuery;
import com.example.trokyy.services.UserDao;
import com.example.trokyy.tools.MyDataBaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DisplayComplaintResponseController implements Initializable {
    private DisplayQuery displayQuery;

    private int complaintId;

    @FXML
    private Button btn;

    @FXML
    private Label detailsreponseLabel;

    @FXML
    private Label complaintIdLabel;

    @FXML
    private Label complaintResponseLabel;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private HBox root;

    @FXML
    private VBox side1;

    @FXML
    private Label reponseDetailsLabel; // Nouveau label pour afficher les détails de la réponse
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //backoffice

        // Initialize the button to FXML file mapping
        buttonFXMLMap.put(Home, "Home.fxml");
        buttonFXMLMap.put(Users, "UserManagement1.fxml");
        buttonFXMLMap.put(Offers, "OffersManagement.fxml");
        buttonFXMLMap.put(Blogs, "BlogsManagement.fxml");
        buttonFXMLMap.put(Events, "EventsManagement.fxml");
        buttonFXMLMap.put(Complaints, "ReclamManagement.fxml");
        buttonFXMLMap.put(Donations, "DonationsManagement.fxml");

        // Initialize map with buttons and their styles
        initializeButtonStyles();

        // Add event handlers to all buttons
        addButtonEventHandlers();

        // Set initial content (Home page)
        loadContent("Home.fxml");





        displayQuery = new DisplayQuery();
        // Initialize the UI with the complaint ID
        complaintIdLabel.setText("Complaint ID: " + complaintId);

        // Récupérer la réponse de la réclamation
        String complaintResponse = getComplaintResponse(complaintId);


        // Récupérer les détails de la réponse
        String reponseDetails = getReponseDetails(complaintId);

        // Afficher les détails de la réponse
        detailsreponseLabel.setText(reponseDetails);

        // Afficher la réponse s'il y en a une, sinon afficher un message indiquant qu'aucune réponse n'a été trouvée
       /* if (complaintResponse != null) {
            complaintResponseLabel.setText("Response: " + complaintResponse);
            // Vérifier si l'attribut "vu" est true, et rendre les boutons invisibles si c'est le cas
            if (getResponseVu(complaintId)) {
                deleteButton.setVisible(false);
                editButton.setVisible(false);
            }
        } else {
            complaintResponseLabel.setText("No response found for Complaint ID: " + complaintId);
        }*/

        // Afficher la réponse s'il y en a une, sinon afficher un message indiquant qu'aucune réponse n'a été trouvée
        if (complaintResponse != null) {
            complaintResponseLabel.setText("Response: " + complaintResponse);
            // Vérifier si l'attribut "vu" est true, et rendre les boutons invisibles si c'est le cas
            if (getResponseVu(complaintId)) {
                deleteButton.setVisible(false);
                editButton.setVisible(false);
            }else{
                deleteButton.setVisible(true);
                editButton.setVisible(true);
            }
        } else {
            complaintResponseLabel.setText("No response found for Complaint ID: " + complaintId);
            // Rendre les boutons "Edit" et "Delete" invisibles si aucune réponse n'est trouvée
            deleteButton.setVisible(false);
            editButton.setVisible(false);
        }
    }


    public String getComplaintResponse(int complaintId) {

        return displayQuery.getComplaintResponse(complaintId);
    }


    @FXML
    private Label complaintIdAndDescriptionLabel;

    @FXML
    private VBox imageContainer;
    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
        complaintIdLabel.setText("Complaint ID: " + complaintId);

        // Récupérer les détails du complaint
        String complaintDetails = getComplaintDetails(complaintId);

        // Récupérer les détails du reponse
        String reponseDetils = getReponseDetails((complaintId));

        // Afficher le complaintId et les détails
        complaintIdAndDescriptionLabel.setText(complaintDetails);

        // Récupérer la réponse de la réclamation
        String complaintResponse = getComplaintResponse(complaintId);

        // Afficher  les détails du reponse
        detailsreponseLabel.setText(reponseDetils);


       // Afficher la réponse s'il y en a une, sinon afficher un message indiquant qu'aucune réponse n'a été trouvée
        if (complaintResponse != null) {
            complaintResponseLabel.setText("Response: " + complaintResponse);
            // Vérifier si l'attribut "vu" est true, et rendre les boutons invisibles si c'est le cas
            if (getResponseVu(complaintId)) {
                deleteButton.setVisible(false);
                editButton.setVisible(false);
            } else{
            deleteButton.setVisible(true);
            editButton.setVisible(true);
              }
          } else {
            complaintResponseLabel.setText("No response found for Complaint ID: " + complaintId);
            // Rendre les boutons "Edit" et "Delete" invisibles si aucune réponse n'est trouvée
            deleteButton.setVisible(false);
            editButton.setVisible(false);

        }

    }


    public String getReponseDetailss(int complaintId) {
        MyDataBaseConnection connection = new MyDataBaseConnection();

        String details = null;
        String query = "SELECT * FROM reponse WHERE reclam_reponse_id = ?";

        try (Connection conn = connection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, complaintId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Récupérer les détails de la réclamation à partir du ResultSet
                String id = resultSet.getString("id");
                String adminId = resultSet.getString("admin_id");
                String description = resultSet.getString("description");
                Date dateReponse = resultSet.getDate("date_reponse");

                // Construire la chaîne de détails de la réclamation
                StringBuilder sb = new StringBuilder();
                sb.append("id: ").append(id).append("\n");
                sb.append("adminId: ").append(adminId).append("\n");
                sb.append("description: ").append(description).append("\n");
                sb.append("Date: ").append(dateReponse).append("\n");


                // Affecter la chaîne de détails
                details = sb.toString();

            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return details;
    }

    public String getReponseDetails(int complaintId) {

        return getReponseDetailss(complaintId);
    }


    public String getComplaintDetailss(int complaintId) {
        MyDataBaseConnection connection = new MyDataBaseConnection();

        String details = null;
        String query = "SELECT * FROM reclamation WHERE id = ?";

        try (
             Connection conn = MyDataBaseConnection.getConnection();

             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, complaintId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Récupérer les détails de la réclamation à partir du ResultSet
                String description = resultSet.getString("description_reclamation");
                String statut = resultSet.getString("statut_reclamation");
                String type = resultSet.getString("type");
                String imagePath = resultSet.getString("image_path");
                Date dateReclamation = resultSet.getDate("date_reclamation");

                // Construire la chaîne de détails de la réclamation
                StringBuilder sb = new StringBuilder();
                sb.append("Description: ").append(description).append("\n");
                sb.append("Statut: ").append(statut).append("\n");
                sb.append("Type: ").append(type).append("\n");
                sb.append("Date: ").append(dateReclamation).append("\n");
               // sb.append("Image Path: ").append(imagePath).append("\n");

                // Affecter la chaîne de détails
                details = sb.toString();

                // Charger et afficher l'image associée à la réclamation si le chemin de l'image n'est pas null
                if (imagePath != null) {
                    Image image = new Image(imagePath);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(100); // Définir la hauteur souhaitée de l'image
                    imageView.setPreserveRatio(true); // Préserver les proportions de l'image
                    imageView.setSmooth(true); // Activer le lissage de l'image
                    imageView.setCache(true); // Activer le cache de l'image pour améliorer les performances
                    // Ajouter l'ImageView à votre interface utilisateur
                    // Supposons que vous ayez un conteneur VBox nommé "imageContainer" dans votre interface utilisateur
                    imageContainer.getChildren().add(imageView);
                } else {
                    System.out.println("imageContainer is null!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return details;
    }

    public String getComplaintDetails(int complaintId) {

        return getComplaintDetailss(complaintId);
    }


    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        // Récupérer la réponse de la réclamation associée à l'ID complaintId
        String complaintResponse = getComplaintResponse(complaintId);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete this response?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Supprimer la réponse de la base de données
                if (complaintResponse != null) {
                    displayQuery.deleteResponse(complaintId);
                    // Mettre à jour l'affichage pour indiquer que la réponse a été supprimée
                    detailsreponseLabel.setText(""); // Effacer le contenu du label
                    complaintResponseLabel.setText("No response found for Complaint ID: " + complaintId);


                    // Afficher la notification
                    showNotification("Response deleted successfully");

                }
            }
        });

    }



    public Reponse getReponseById(int complaintId) {
        MyDataBaseConnection connection = new MyDataBaseConnection();
        Reponse reponse = null;
        String query = "SELECT * FROM reponse WHERE reclam_reponse_id = ?";

        try (Connection conn = connection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, complaintId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                reponse = new Reponse();
                reponse.setId(resultSet.getInt("id"));
                reponse.setDescription(resultSet.getString("description"));
                // Assurez-vous de récupérer tous les autres attributs nécessaires de la réponse depuis le ResultSet
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception selon vos besoins
        }

        return reponse;
    }


    @FXML
    private void handleEditButtonAction(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Update");
        alert.setHeaderText("Are you sure you want to update this response?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Charger la nouvelle interface depuis le fichier FXML
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/Backoffice/ComplaintResponse/UpdateResponse.fxml"));
                    Parent parent = loader.load();

                    // Obtenez le contrôleur de la nouvelle interface
                    UpdateReponsecontroller controller = loader.getController();

                    // Passez la réponse à mettre à jour au contrôleur de la nouvelle interface
                    // Récupérer la réponse associée à l'ID de la réclamation
                    Reponse selectedReponse = getReponseById(complaintId); // Vous devez implémenter getReponseById pour récupérer la réponse associée à l'ID
                    controller.setReponseToUpdate(selectedReponse);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(parent, 800, 600)); // Taille moyenne de l'interface
                    stage.initStyle(StageStyle.UTILITY);
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(AjoutReponseController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });


        displayQuery = new DisplayQuery();
        // Initialize the UI with the complaint ID
        complaintIdLabel.setText("Complaint ID: " + complaintId);

        // Récupérer la réponse de la réclamation
        String complaintResponse = getComplaintResponse(complaintId);


        // Récupérer les détails de la réponse
        String reponseDetails = getReponseDetails(complaintId);

        // Afficher les détails de la réponse
        detailsreponseLabel.setText(reponseDetails);

        // Afficher la réponse s'il y en a une, sinon afficher un message indiquant qu'aucune réponse n'a été trouvée
        if (complaintResponse != null) {
            complaintResponseLabel.setText("Response: " + complaintResponse);
        } else {
            complaintResponseLabel.setText("No response found for Complaint ID: " + complaintId);
        }
    }


    public void showNotification(String message) {
        Notifications notifications = Notifications.create();
        notifications.text(message);
        notifications.title("Success Message");
        notifications.hideAfter(Duration.seconds(30));
        notifications.darkStyle();
        notifications.position(Pos.BOTTOM_CENTER);
        notifications.show();
    }



    public Reclamation getReclamationById(int complaintId) {
        MyDataBaseConnection connection = new MyDataBaseConnection();
        Reclamation reclamation = null;
        String query = "SELECT * FROM reclamation WHERE id = ?";

        try (Connection conn = connection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, complaintId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                reclamation = new Reclamation();
                reclamation.setId(resultSet.getInt("id"));
                // Vous pouvez ajouter d'autres attributs de la réclamation en fonction de votre structure de base de données
                reclamation.setVu(resultSet.getBoolean("vu"));
                // Assurez-vous de récupérer tous les autres attributs nécessaires de la réclamation depuis le ResultSet
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception selon vos besoins
        }

        return reclamation;
    }



    public boolean getResponseVu(int complaintId) {
        boolean vu = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Établir une connexion à la base de données
            connection = new MyDataBaseConnection().getConnection();

            // Requête SQL pour sélectionner l'attribut "vu" de la réclamation avec l'ID donné
            String query = "SELECT vu FROM reclamation WHERE id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, complaintId);

            // Exécution de la requête
            resultSet = preparedStatement.executeQuery();

            // Si un enregistrement est trouvé, récupérer la valeur de l'attribut "vu"
            if (resultSet.next()) {
                vu = resultSet.getBoolean("vu");
            }
        } catch (SQLException e) {
            System.out.println("Error getting response vu: " + e.getMessage());
        } finally {
            // Fermeture des ressources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

        return vu;
    }


    @FXML
    private BorderPane borderPane;

    @FXML
    private Button Home;

    @FXML
    private Button Users;

    @FXML
    private Button Offers;

    @FXML
    private Button Blogs;

    @FXML
    private Button Events;

    @FXML
    private Button Complaints;

    @FXML
    private Button Donations;

    @FXML
    private Button Logout;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Utilisateur> tableView;
    // Mapping between buttons and their respective FXML files
    private final Map<Button, String> buttonFXMLMap = new HashMap<>();

    // Map to store buttons and their original styles
    private Map<Button, String> buttonStyles = new HashMap<>();

    private Button selectedButton = null;
    private static final UserDao userDao = new UserDao();


    public DisplayComplaintResponseController() throws SQLException {
        Connection connection = MyDataBaseConnection.getConnection();

    }


    private void addButtonEventHandlers() {
        Home.setOnAction(this::handleButtonClick);
        Users.setOnAction(this::handleButtonClick);
        Offers.setOnAction(this::handleButtonClick);
        Blogs.setOnAction(this::handleButtonClick);
        Events.setOnAction(this::handleButtonClick);
        Complaints.setOnAction(this::handleButtonClick);
        Donations.setOnAction(this::handleButtonClick);
        Logout.setOnAction(this::handleButtonClick);
    }


    private void initializeButtonStyles() {
        buttonStyles.put(Home, Home.getStyle());
        buttonStyles.put(Users, Users.getStyle());
        buttonStyles.put(Offers, Offers.getStyle());
        buttonStyles.put(Blogs, Blogs.getStyle());
        buttonStyles.put(Events, Events.getStyle());
        buttonStyles.put(Complaints, Complaints.getStyle());
        buttonStyles.put(Donations, Donations.getStyle());
        buttonStyles.put(Logout, Logout.getStyle());
    }


    @FXML
    private void handleButtonClick(ActionEvent event) {
        // Get the button that triggered the event
        Button clickedButton = (Button) event.getSource();
        // If a button is already selected, revert its style to the original
        if (selectedButton != null) {
            selectedButton.setStyle(buttonStyles.get(selectedButton));
        }
        // Update selectedButton and apply new style to the clicked button
        selectedButton = clickedButton;
        selectedButton.setStyle("-fx-background-color: #2252e5; " +
                "-fx-background-radius: 30; " +
                "-fx-text-fill: white; " +
                "-fx-font-family: 'Open Sans'; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 12px;");

        // Load the corresponding FXML file and set it as the center content
        String fxmlFile = buttonFXMLMap.get(clickedButton);
        loadContent(fxmlFile);
    }

    private void loadContent(String fxmlFile) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/Backoffice/" + fxmlFile));
            Parent content = loader.load();

            // Set the loaded content as the center of the BorderPane
            borderPane.setCenter(content);
        } catch (IOException e) {
            Logger.getLogger(AdminMainController.class.getName()).log(Level.SEVERE, "Error loading FXML file: " + fxmlFile, e);
        } catch (Exception ex) {
            Logger.getLogger(AdminMainController.class.getName()).log(Level.SEVERE, "Unexpected error loading FXML file: " + fxmlFile, ex);
        }
    }




    @FXML

    public void navigatetohome(ActionEvent actionEvent) {
        loadContent("Home.fxml");

    }
    @FXML
    private void navigatetousers(ActionEvent event) {
        loadContent("UserManagement1.fxml");
    }



    @FXML
    private void navigatetooffers(ActionEvent event) {
        loadContent("OffersManagement");
    }

    @FXML
    private void navigatetoblogs(ActionEvent event) {
        loadContent("BlogsManagement");
    }

    @FXML
    private void navigatetoevents(ActionEvent event) {
        loadContent("EventsManagament");
    }

    @FXML
    private void navigatetocomplaints(ActionEvent event) {
        loadContent("ReclamManagement");
    }

    @FXML
    private void navigatetodontions(ActionEvent event) {
        loadContent("DonationsManagement");
    }

    @FXML
    private void logout(ActionEvent event) {
        // Handle logout action here
    }




    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            try {
                List<Utilisateur> searchResults = userDao.searchUsers(query);
                tableView.getItems().clear(); // Clear existing data
                tableView.getItems().addAll(searchResults); // Display search results
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database query errors
            }
        } else {
            // If the search query is empty, display all users
            try {
                tableView.getItems().clear(); // Clear existing data
                List<Utilisateur> userList = userDao.getAllUsers();
                tableView.getItems().addAll(userList); // Display all users
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database query errors
            }
        }
    }






}

