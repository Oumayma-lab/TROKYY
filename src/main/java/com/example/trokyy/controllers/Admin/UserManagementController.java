package com.example.trokyy.controllers.Admin;

import com.example.trokyy.tools.MyDataBaseConnection;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.services.UserDao;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;


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
    private static final UserDao userDao = new UserDao();


    public UserManagementController() throws SQLException {
        Connection connection = MyDataBaseConnection.getConnection();

    }


    @FXML
    public void initialize() {

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
        //statusColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isActive()));
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
            e.printStackTrace();
            // Handle database connection or query errors
        }


    }

    public void deleteUser(Utilisateur item) {
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

                        banIcon.setOnMouseClicked(event -> {
                            Utilisateur utilisateur = getTableView().getItems().get(getIndex());
                            userDao.banUser(utilisateur);
                            utilisateur.setActive(false);
                            getTableView().refresh();
                            // Perform ban action here
                        });

                        setGraphic(new HBox(banIcon, deleteIcon));
                    }
                }
            };
            return cell;
        }
    };



    @FXML
    void handleSearch() {
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



