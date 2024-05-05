package com.example.trokyy.controllers.Admin;
import com.example.trokyy.services.UserDao;
import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.tools.MyDataBaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;


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
    public UserManagementController userManagementController;


    // Map to store buttons and their original styles
    private Map<Button, String> buttonStyles = new HashMap<>();

    private Button selectedButton = null;
    private static final UserDao userDao = new UserDao();

    @FXML
    private StackPane rootPane; // Reference to the root pane

    @FXML
    private LineChart<Number, Number> userChart;

    public AdminMainController() throws SQLException {
        Connection connection = MyDataBaseConnection.getConnection();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the button to FXML file mapping
        buttonFXMLMap.put(Home, "Home.fxml");
        buttonFXMLMap.put(Users, "UserManagement.fxml");
        buttonFXMLMap.put(Offers, "OffersManagement.fxml");
        buttonFXMLMap.put(Blogs, "BlogsManagement.fxml");
        buttonFXMLMap.put(Events, "EventsManagement.fxml");
        buttonFXMLMap.put(Complaints, "ComplaintsManagement.fxml");
        buttonFXMLMap.put(Donations, "DonationsManagement.fxml");

        // Initialize map with buttons and their styles
        initializeButtonStyles();
        // Add event handlers to all buttons
        addButtonEventHandlers();
        // Set initial content (Home page)
        loadContent("Home.fxml");
        // displayUserEvolutionChart();







        if (userChart != null) {
            NumberAxis xAxis = (NumberAxis) userChart.getXAxis();
            NumberAxis yAxis = (NumberAxis) userChart.getYAxis();
            xAxis.setLabel("Month");
            yAxis.setLabel("Number of Users");

            // Add sample data to represent user growth evolution
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName("User Growth");

            // Generating sample data for 12 months
            for (int i = 1; i <= 12; i++) {
                // Simulating new user creation, you can replace this with your actual logic
                int newUserCount = (int) (Math.random() * 100) + 50; // Random between 50 and 150
                series.getData().add(new XYChart.Data<>(i, newUserCount));
            }

            userChart.getData().add(series);

            // Apply styles
            rootPane.setStyle("-fx-background-color: white;");
            userChart.setStyle("-fx-stroke: green;");
        }

    }

    private void addButtonEventHandlers() {
        Home.setOnAction(this::handleButtonClick);
        Users.setOnAction(this::handleButtonClick);
        Offers.setOnAction(this::handleButtonClick);
        Blogs.setOnAction(this::handleButtonClick);
        Events.setOnAction(this::handleButtonClick);
        Complaints.setOnAction(this::handleButtonClick);
        Donations.setOnAction(this::handleButtonClick);
        Logout.setOnAction(this::handleLogout);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/Backoffice/" + fxmlFile));
            Parent content = loader.load();
            Object controller = loader.getController();
            borderPane.setCenter(content);

            if (controller instanceof UserManagementController) {
                userManagementController = (UserManagementController) controller;
            }
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
        loadContent("UserManagement.fxml");
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
        loadContent("ComplaintsManagement");
    }

    @FXML
    private void navigatetodontions(ActionEvent event) {
        loadContent("DonationsManagement");
    }


    @FXML
    private void handleSearch() throws SQLException {
        if (userManagementController != null) {
            String query = searchField.getText().trim();
            userManagementController.setSearchQuery(query);
        } else {
            System.err.println("User management controller is not initialized.");
        }
    }

    private void displayUserEvolutionChart() throws SQLException {
        // Retrieve user data
        List<Utilisateur> userList = userDao.getAllUsers();

        // Count the number of users registered on each day
        Map<LocalDate, Integer> userCountByDate = new HashMap<>();
        for (Utilisateur user : userList) {
            LocalDate registrationDate = user.getDateInscription().toLocalDate();
            userCountByDate.put(registrationDate, userCountByDate.getOrDefault(registrationDate, 0) + 1);
        }

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("User Evolution Over Days");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("User Count");

        userCountByDate.forEach((date, count) -> series.getData().add(new XYChart.Data<>(date.toEpochDay(), count)));

        lineChart.getData().add(series);

        borderPane.setCenter(lineChart);
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            // Load the FXML file of the login screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/User/Main.fxml"));
            Parent root = loader.load();

            // Create a new scene with the login screen
            Scene scene = new Scene(root);

            // Get the stage (window) from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Close the previous stage
            stage.close();

            // Open a new stage for the login screen
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    @FXML
    private void handleLogout(ActionEvent actionEvent) {
        // Create an alert dialog for confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Logout");
        alert.setHeaderText("Are you sure you want to logout? 😢");
        alert.setContentText("Don't go, trockino!");

        // Customize the dialog's appearance
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #7CFC00; -fx-border-color: #7CFC00; -fx-border-radius: 50px; -fx-background-radius: 50px;");
        dialogPane.getScene().getRoot().setStyle("-fx-background-color: transparent;");

        // Force the styles to be applied
        dialogPane.applyCss();

        // Show the alert and wait for user response
        Optional<ButtonType> result = alert.showAndWait();

        // If the user confirms logout, close the window and exit the application
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
            Platform.exit();
        }
    }





}
