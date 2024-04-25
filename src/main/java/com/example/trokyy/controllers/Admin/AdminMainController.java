package com.example.trokyy.controllers.Admin;
import com.example.trokyy.services.UserDao;
import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.tools.MyDataBaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AdminMainController implements Initializable {
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


    public AdminMainController() throws SQLException {
        Connection connection = MyDataBaseConnection.getConnection();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
