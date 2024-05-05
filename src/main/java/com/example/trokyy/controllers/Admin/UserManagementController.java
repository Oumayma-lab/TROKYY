package com.example.trokyy.controllers.Admin;

import com.example.trokyy.tools.EmailService;
import com.example.trokyy.tools.MyDataBaseConnection;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.services.UserDao;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ScheduledExecutorService;

import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.controlsfx.control.Notifications;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import javafx.scene.paint.Color;

import static com.example.trokyy.services.UserDao.banUser;
import static java.awt.Color.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class UserManagementController {
    @FXML
    private TableView<Utilisateur> tableView;
    @FXML
    private TableColumn<Utilisateur, String> nameColumn;
    @FXML
    private TableColumn<Utilisateur, Integer> phoneNumberColumn;
    @FXML
    private TableColumn<Utilisateur, String> emailColumn;
    @FXML
    private TableColumn<Utilisateur, String> addressColumn;
    @FXML
    private TableColumn<Utilisateur, Boolean> statusColumn;
    @FXML
    private TableColumn<Utilisateur, LocalDateTime> registrationDateColumn;
    @FXML
    private TableColumn<Utilisateur,Void> actionColumn;
    @FXML
    private TableColumn<Utilisateur, Integer> idColumn;
    @FXML
    private TextField searchField; // Text field for entering search query
    @FXML
    private ImageView banIcon;
    @FXML
    private ImageView deleteIcon;
    @FXML
    private ChoiceBox<String> statusFilter;

    private ScheduledExecutorService scheduler;

    private UserDao userDao = new UserDao();
    private final ObservableList<String> filterOptions = FXCollections.observableArrayList("All", "Active", "Banned");
    EmailService emailService = new EmailService("batoutbata5@gmail.com", "prxidlislxcnfedc");
    @FXML
    private ImageView imageView;
    @FXML
    private StackPane rootPane; // Reference to the root pane
    @FXML
    private LineChart<Number, Number> userChart;
    public UserManagementController() throws SQLException {
        Connection connection = MyDataBaseConnection.getConnection();
        userDao = new UserDao();
    }

    @FXML
    public void initialize() throws SQLException {

        userDao = new UserDao();
        tableView.setFixedCellSize(50);
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(data -> {
            Utilisateur utilisateur = data.getValue();
            String fullName = utilisateur.getNom() + " " + utilisateur.getPrenom();
            return new SimpleStringProperty(fullName);
        });
        phoneNumberColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTelephone()).asObject());
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("active"));
        statusColumn.setCellFactory(new Callback<TableColumn<Utilisateur, Boolean>, TableCell<Utilisateur, Boolean>>() {
            @Override
            public TableCell<Utilisateur, Boolean> call(TableColumn<Utilisateur, Boolean> param) {
                return new TableCell<Utilisateur, Boolean>() {
                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            Label label = new Label(item ? "Active" : "Inactive");
                            label.getStyleClass().add("badge");
                            label.getStyleClass().add(item ? "badge-success" : "badge-danger");
                            label.setStyle("-fx-border-radius: 10px;"); // Set border radius
                            setGraphic(label);

                        }
                    }
                };
            }
        });

        // Add columns to TableView
        //tableView.getColumns().add(statusColumn);
        registrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateInscription"));

        // Customize rendering of LocalDateTime to show only date
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        registrationDateColumn.setCellFactory(column -> new TableCell<Utilisateur, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null); // Hide the cell if it's empty

                } else {
                    setText(item.format(dateFormatter));
                }

            }
        });


        actionColumn.setCellFactory(cellFactory);


        // Load users into the table
        try {
            List<Utilisateur> userList = userDao.getAllUsers();
            tableView.getItems().addAll(userList);
        } catch (SQLException e) {
            e.printStackTrace();}

    }



    @FXML
    private void exportToPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.setInitialFileName("Users.pdf");
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            generatePDF(file);
        }
    }

    private File chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.setInitialFileName("user_list.pdf");
        return fileChooser.showSaveDialog(tableView.getScene().getWindow());
    }
    private void generatePDF(File file) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10); // Adjust font size

            float margin = 20;
            float yStart = page.getMediaBox().getHeight() - margin;
            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
            float yPosition = yStart;
            float rowHeight = 20;
            float cellMargin = 5;

            drawRow(contentStream, margin, yPosition, tableWidth, rowHeight,
                    "ID", "Name", "Phone Number", "Email", "Address", "Status", "Registration Date");
            yPosition -= rowHeight;

            contentStream.setStrokingColor(0, 0, 1); // Blue color
            contentStream.moveTo(margin, yPosition - cellMargin / 2);
            contentStream.lineTo(tableWidth + margin, yPosition - cellMargin / 2);
            contentStream.stroke();

            for (Utilisateur user : tableView.getItems()) {
                drawRow(contentStream, margin, yPosition, tableWidth, rowHeight,
                        String.valueOf(user.getId()),
                        user.getNom() + " " + user.getPrenom(),
                        String.valueOf(user.getTelephone()),
                        user.getEmail(),
                        user.getAdresse(),
                        user.isActive() ? "Active" : "Inactive",
                        user.getDateInscription() != null ? user.getDateInscription().toString() : "");
                yPosition -= rowHeight + cellMargin;
            }

            contentStream.close();
            document.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawRow(PDPageContentStream contentStream, float x, float y, float tableWidth, float rowHeight, String... content) throws IOException {
        float cellWidth = tableWidth / content.length;
        contentStream.setStrokingColor(0, 0, 1); // Blue color

        for (String text : content) {
            if (text != null) {
                float textWidth = PDType1Font.HELVETICA.getStringWidth(text) / 1000 * 10;
                float startX = x + (cellWidth - textWidth) / 2;
                float startY = y - rowHeight / 2 - 5;
                contentStream.beginText();
                contentStream.newLineAtOffset(startX, startY);
                contentStream.showText(text);
                contentStream.endText();
            }
            x += cellWidth;
        }
    }

    public void deleteUser(Utilisateur item) {
    }

    private void applyFilter() {
        String selectedFilter = statusFilter.getValue();
        try {
            tableView.getItems().clear();
            if (selectedFilter.equals("All")) {
                tableView.getItems().addAll(userDao.getAllUsers());
            } else if (selectedFilter.equals("Active")) {
                tableView.getItems().addAll(userDao.getActiveUsers());
            } else if (selectedFilter.equals("Banned")) {
                tableView.getItems().addAll(userDao.getBannedUsers());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database query errors
        }
    }


    Callback<TableColumn<Utilisateur, Void>, TableCell<Utilisateur, Void>> cellFactory = new Callback<>() {
        @Override
        public TableCell<Utilisateur, Void> call(final TableColumn<Utilisateur, Void> param) {
            final TableCell<Utilisateur, Void> cell = new TableCell<>() {
                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        deleteIcon.getStyleClass().add("delete-icon");
                        deleteIcon.setStyle("-fx-fill: red;"); // Set icon color
                        deleteIcon.setSize("30");

                        FontAwesomeIconView banIcon = new FontAwesomeIconView(FontAwesomeIcon.BAN);
                        banIcon.getStyleClass().add("ban-icon");
                        banIcon.setStyle("-fx-fill: orange;"); // Set icon color
                        banIcon.setSize("30");

                        deleteIcon.setOnMouseClicked(event -> {
                            Utilisateur utilisateur = getTableView().getItems().get(getIndex());
                            userDao.deleteUser(utilisateur);
                            getTableView().getItems().remove(utilisateur);
                        });
//                        banIcon.setOnMouseClicked(event -> {
//                            Utilisateur utilisateur = getTableView().getItems().get(getIndex());
//                            userDao.banUser(utilisateur);
//                            utilisateur.setActive(false);
//                            emailService.sendBanEmail(userEmail, fullName, banReason);
//                            getTableView().refresh();
//                            UserDao.reactivateBannedUsers();
//
//                        });
                        banIcon.setOnMouseClicked(event -> {
                            Utilisateur utilisateur = getTableView().getItems().get(getIndex());
                            showConfirmationDialog(utilisateur);

                            banUser(utilisateur);
                            utilisateur.setActive(false);
                            String fullName = utilisateur.getFullName(); // Assuming you have a method to get the full name
                            String userEmail = utilisateur.getEmail(); // Assuming you have a method to get the email
                            String banReason = "Violation of terms"; // Specify the ban reason
                            try {
                                emailService.sendBanEmail(userEmail, fullName, banReason);

                                showNotification("Success", "User has been banned successfully.");
                            } catch (Exception e) {
                                e.printStackTrace();
                                utilisateur.setActive(true);
                            }
                            getTableView().refresh();
                            UserDao.reactivateBannedUsers();
                        });


                        setGraphic(new HBox(banIcon, deleteIcon));
                    }
                }
            };
            return cell;
        }
    };


    private void showConfirmationDialog(Utilisateur utilisateur) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to ban this user?");
        alert.setContentText("This action cannot be undone.");

        // Set the CSS styling for the dialog title
        alert.getDialogPane().getStyleClass().add("confirmation-dialog");
        alert.getDialogPane().lookup(".header-panel").setStyle("-fx-background-color: #4CAF50; -fx-font-family: FontAwesome;");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                banUser(utilisateur);
            }
        });
    }
    private void showNotification(String title, String message) {
        FontAwesomeIconView successIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK_CIRCLE);
        successIcon.setSize("36");
        successIcon.setFill(Color.web("#4CAF50"));
        Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT)
                .graphic(successIcon)
                .darkStyle()
                .show();
    }




    public void setTableView(TableView<Utilisateur> tableView) {
        this.tableView = tableView;
    }



    public void setSearchQuery(String query) {
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



