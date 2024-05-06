package com.example.trokyy.controllers.Admin.Reponse;

import com.example.trokyy.controllers.Admin.AdminMainController;
import com.example.trokyy.models.Reponse;
import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.services.DisplayQuery;
import com.example.trokyy.services.ReponseService;
import com.example.trokyy.services.UserDao;
import com.example.trokyy.tools.MyDataBaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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

public class UpdateReponsecontroller implements Initializable {
    @FXML
    private VBox imageContainer;
    private int reclam_reponse_id;
    private int complaintId;

    @FXML
    private Label complaintIdAndDescriptionLabel;

    @FXML
    private Label complaintIdLabel;
    private DisplayQuery displayQuery;
    private boolean update;
    @FXML
    private Button btnUploadImage;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private TextField keywordTextField;



    private Reponse selectedReponse; // Ajoutez cet attribut pour stocker la reponse sélectionnée

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
       // loadContent("Home.fxml");



        //--------------------------------------------//
        displayQuery = new DisplayQuery();
        // Initialize the UI with the complaint ID
        complaintIdLabel.setText("Complaint ID: " + complaintId);
    }


    public void setComplaintId(int complaintId) {

        this.reclam_reponse_id =  complaintId;

        this.complaintId = complaintId;
        complaintIdLabel.setText("Complaint ID: " + complaintId);

        // Récupérer les détails du complaint
        String complaintDetails = getComplaintDetails(complaintId);



        // Afficher le complaintId et les détails
        complaintIdAndDescriptionLabel.setText(complaintDetails);


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

    private ApplicationViewComplaintsController complaintsController;

    // Méthode pour définir le contrôleur de la vue des réclamations
    public void setComplaintsController(ApplicationViewComplaintsController complaintsController) {
        this.complaintsController = complaintsController;
    }




    @FXML
    private void clearFields() {
        descriptionTextArea.clear();

    }


        private void showAlert(String message) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }

    public void setReponseToUpdate(Reponse reponse) {
        if (reponse != null) {
            selectedReponse = reponse;
            descriptionTextArea.setText(reponse.getDescription());
            // Vous pouvez remplir d'autres champs avec les valeurs de la réponse si nécessaire
        }
    }




        @FXML
        private Button updatebtn;

        @FXML
        private void updateReponse(ActionEvent event) {
            // Vérifier
            if (selectedReponse == null) {
                showAlert("Aucune reponse à mettre à jour.");
                return;
            }

            // Récupérer les nouvelles valeurs des champs
            String description = descriptionTextArea.getText();


            // Filtrage des mots interdits
            description = filterBadWords(description);




            // Vérifier si les champs description et type sont vides
            if (description.isEmpty() ) {
                showAlert("Veuillez remplir tous les champs obligatoires.");
                return;
            }

            // Mettre à jour la reponse
            selectedReponse.setDescription(description);
            selectedReponse.setDate_reponse(new Date()); // Mettre à jour la date de réponse


            // Appeler la méthode updateReponse de  service Reponse
            ReponseService reponseService = new ReponseService();
            reponseService.updateReponse(selectedReponse);

            // Afficher un message de succès
            showNotification("Response Updated successfully");
            // Réinitialiser les champs après l'update
            clearFields();
            // Rafraîchir le TableView dans le contrôleur de la vue des réclamations
            complaintsController.refresh();



        }
    private String filterBadWords(String description) {
        String[] motsInterdits = {  "f r a u d",  "s p a m", "v i o l e n c e","h a t e", "t e r r o r i s m", "t e r r o r i s m",
                "d a n g e r"};
        for (String motInterdit : motsInterdits) {
            // Échappez les caractères spéciaux pour s'assurer que la regex fonctionne correctement
            motInterdit = motInterdit.replaceAll("([\\\\{}()\\[\\].+*?^$|])", "\\\\$1");
            // Remplacez les espaces par des expressions régulières correspondant à n'importe quel espace
            motInterdit = motInterdit.replaceAll(" ", "\\\\s*");
            description = description.replaceAll("(?i)" + motInterdit, "***");
        }
        return description;
    }

        public void showNotification(String message) {
            Notifications notifications = Notifications.create();
            notifications.text(message);
            notifications.title("Success Message");
            notifications.hideAfter(Duration.seconds(5));
            notifications.darkStyle();
            notifications.position(Pos.BOTTOM_CENTER);
            notifications.show();
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
