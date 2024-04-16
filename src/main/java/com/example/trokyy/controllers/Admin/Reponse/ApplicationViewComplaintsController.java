package com.example.trokyy.controllers.Admin.Reponse;

import com.example.trokyy.models.Reclamation;
import com.example.trokyy.tools.MyDataBaseConnection;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
    @FXML
    private TextField searchByDateTextField;

    @FXML
    private TextField searchByTypeTextField;
    private ObservableList<Reclamation> allComplaints;

    private FilteredList<Reclamation> filteredData;








    // Method to retrieve data from the database
    public List<Reclamation> getAllComplaints() {
        MyDataBaseConnection connection = new MyDataBaseConnection();
        List<Reclamation> complaints = new ArrayList<>();
        String query = "SELECT * FROM reclamation";
        try (Connection conn = connection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Reclamation complaint = new Reclamation();
                complaint.setId(resultSet.getInt("id"));

                complaint.setType(resultSet.getString("type"));
                complaint.setDescription_reclamation(resultSet.getString("description_reclamation"));
                complaint.setDate_reclamation(resultSet.getTimestamp("date_reclamation"));
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
        filteredData = new FilteredList<>(observableComplaints, p -> true);
        complaintsTable.setItems(filteredData);

        searchByDateTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(complaint -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return complaint.getDate_reclamation().toString().toLowerCase().contains(lowerCaseFilter);
            });
        });

        searchByTypeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(complaint -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare type with filter text
                String lowerCaseFilter = newValue.toLowerCase();
                return complaint.getType().toLowerCase().contains(lowerCaseFilter);
            });
        });
        TableColumn<Reclamation, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(190);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button respondButton = new Button("Add a Reponse");
            private final Button displayResponsesButton = new Button("Détails");

            {
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
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("Reponse.fxml"));
                            Parent parent = loader.load();
                            ReponseController reponseController = loader.getController();
                            reponseController.setComplaintId(selectedComplaint.getId());
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent, 800, 600));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();
                        } catch (IOException ex) {
                            Logger.getLogger(ApplicationViewComplaintsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                displayResponsesButton.setOnAction(event -> {
                    Reclamation selectedComplaint = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("DisplayComplaintResponse.fxml"));
                    try {
                        Parent parent = loader.load();
                        DisplayComplaintResponseController displayComplaintResponseController = loader.getController();
                        displayComplaintResponseController.setComplaintId(selectedComplaint.getId());

                        Stage stage = new Stage();
                        stage.setScene(new Scene(parent, 800, 600)); // Taille moyenne de l'interface
                        stage.initStyle(StageStyle.UTILITY);
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(ReponseController.class.getName()).log(Level.SEVERE, null, ex);
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

                    if (selectedComplaint.isVu()) {
                        setGraphic(displayResponsesButton);
                    } else {
                        setGraphic(new HBox(10, respondButton, displayResponsesButton));
                    }
                }
            }
        });
        complaintsTable.getColumns().add(actionsColumn);

    }

}
