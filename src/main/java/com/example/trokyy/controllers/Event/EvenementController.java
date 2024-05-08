package com.example.trokyy.controllers.Event;

import com.example.trokyy.models.Evenement;
import com.example.trokyy.models.Type;
import com.example.trokyy.services.ServiceEvenement;
import com.example.trokyy.services.ServiceType;
import com.twilio.Twilio;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import com.twilio.type.PhoneNumber;


import com.twilio.rest.api.v2010.account.Message;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class EvenementController implements Initializable {
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private GridPane EventContainer;
    @FXML
    public TextField lientf;
    @FXML
    public DatePicker datedebut;
    @FXML
    public DatePicker datefin;
    @FXML
    public TextArea desctf;
    @FXML
    private TextField eventsearch;
    @FXML
    public TextField idtf;
    @FXML
    private TextField idtype;
    @FXML
    private Label labelinfo;
    @FXML
    public TextField lieutf;
    @FXML
    private TableView<Type> tableT;
    @FXML
    private TableColumn<Type, String> tableid;
    @FXML
    private TableColumn<Type, String> tabletype;
    @FXML
    public TextField titretf;
    @FXML
    public TextField typeid;
    @FXML
    private TextField typetf;

    @FXML
    private TextField phonenumber;



    private final ServiceEvenement EventS = new ServiceEvenement();
    private final ServiceType TypeS = new ServiceType();

    private static final String TWILIO_PHONE_NUMBER = "+14058963517";
    private static final String ACCOUNT_SID = "ACd07d0a2049d678d56d80070215d03cf4";
    private static final String AUTH_TOKEN = "94673931fc3cbd5218fc71e7d019d74f";


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LoadTypes();
        LoadEvent();
    }
    @FXML
    void refresh(ActionEvent event) {
        EventContainer.getChildren().clear();
        LoadTypes();
        LoadEvent();
    }
    @FXML
    void selection(MouseEvent event) {
        Type selectedType = tableT.getSelectionModel().getSelectedItem();
        if (selectedType != null) {
            idtype.setText(String.valueOf(selectedType.getId()));
            typeid.setText(String.valueOf(selectedType.getId()));
            typetf.setText(selectedType.getType());
        }
    }
    @FXML
    void ajouterevent(ActionEvent event) {
        String TITRE = titretf.getText();
        String DESCRIPTION = desctf.getText();
        String LIEU = lieutf.getText();
        String LIEN = lientf.getText();
        int TYPE = Integer.parseInt(typeid.getText());
        Date DATE_DEBUT = Date.valueOf(datedebut.getValue());
        Date DATE_FIN = Date.valueOf(datefin.getValue());
        if (!EventS.CheckDate(DATE_DEBUT,DATE_FIN)){
            sendSmsNotification("\n * Hello, There is a new event coming soon called "+ titretf.getText()+" It will start in "+ datedebut.getValue()+" and ends in "+datefin.getValue() );
            EventS.Add(new Evenement(0, TITRE, DESCRIPTION, LIEU, LIEN, DATE_DEBUT, DATE_FIN, TYPE));
        labelinfo.setText("The event has been successfully added ");
        LoadEvent();
        }else{
            labelinfo.setText("The period between the two dates is already reserved.");
        }
    }
    static {
        Twilio.init(ACCOUNT_SID,AUTH_TOKEN);
    }
    public void sendSmsNotification(String messageBody) {
        Message message = Message.creator(
                new PhoneNumber(phonenumber.getText()),  // To phone number
                new PhoneNumber(TWILIO_PHONE_NUMBER),  // From Twilio phone number
                messageBody
        ).create();

        System.out.println("Sent message with ID: " + message.getSid());
    }
    @FXML
    void modifierevent(ActionEvent event) {
        int ID = Integer.parseInt(idtf.getText());
        String TITRE = titretf.getText();
        String DESCRIPTION = desctf.getText();
        String LIEU = lieutf.getText();
        String LIEN = lientf.getText();
        int TYPE = Integer.parseInt(typeid.getText());
        Date DATE_DEBUT = Date.valueOf(datedebut.getValue());
        Date DATE_FIN = Date.valueOf(datefin.getValue());
        if (!EventS.CheckDate(DATE_DEBUT,DATE_FIN)){
        EventS.Update(new Evenement(ID, TITRE, DESCRIPTION, LIEU, LIEN, DATE_DEBUT, DATE_FIN, TYPE));
        labelinfo.setText("The event has been successfully added ");
        LoadEvent();
        }else{
            labelinfo.setText("The period between the two dates is already reserved.");
        }
    }

    public void LoadEvent() {
        int column = 0;
        int row = 1;
        try {
            for (Evenement events : EventS.afficher()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/example/trokyy/FrontOffice/Event/EventCard.fxml"));
                Pane userBox = fxmlLoader.load();
                EventCardController EventCardC = fxmlLoader.getController();
                EventCardC.setData(events);
                if (column == 4) {
                    column = 0;
                    ++row;
                }
                EventContainer.add(userBox, column++, row);
                GridPane.setMargin(userBox, new Insets(12));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Recherche(ActionEvent event) {
        int column = 0;
        int row = 1;
        String recherche = eventsearch.getText();
        try {
            EventContainer.getChildren().clear();
            for (Evenement events : EventS.Rechreche(recherche)) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/example/trokyy/FrontOffice/Event/EventCard.fxml"));
                Pane userBox = fxmlLoader.load();
                EventCardController EventCardC = fxmlLoader.getController();
                EventCardC.setData(events);
                if (column == 4) {
                    column = 0;
                    ++row;
                }
                EventContainer.add(userBox, column++, row);
                GridPane.setMargin(userBox, new Insets(12));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void tripartitre(ActionEvent event) {
        int column = 0;
        int row = 1;
        try {
            for (Evenement events : EventS.TriparTitre()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/example/trokyy/FrontOffice/Event/EventCard.fxml"));
                Pane userBox = fxmlLoader.load();
                EventCardController EventCardC = fxmlLoader.getController();
                EventCardC.setData(events);
                if (column == 4) {
                    column = 0;
                    ++row;
                }
                EventContainer.add(userBox, column++, row);
                GridPane.setMargin(userBox, new Insets(12));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void triparLieu(ActionEvent event) {
        int column = 0;
        int row = 1;
        try {
            for (Evenement events : EventS.TriparLieu()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/example/trokyy/FrontOffice/Event/EventCard.fxml"));
                Pane userBox = fxmlLoader.load();
                EventCardController EventCardC = fxmlLoader.getController();
                EventCardC.setData(events);
                if (column == 4) {
                    column = 0;
                    ++row;
                }
                EventContainer.add(userBox, column++, row);
                GridPane.setMargin(userBox, new Insets(12));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addtype(ActionEvent event) {
        String TYPE = typetf.getText();
        TypeS.Add(new Type(0, TYPE));
        labelinfo.setText("Type est ajouter avec succes");
        LoadTypes();
    }

    @FXML
    void deletetype(ActionEvent event) {
        int ID = Integer.parseInt(idtype.getText());
        TypeS.DeleteByID(ID);
        labelinfo.setText("Type est supprimer avec succes");
        LoadTypes();
    }

    @FXML
    void updatetype(ActionEvent event) {
        int ID = Integer.parseInt(idtype.getText());
        String TYPE = typetf.getText();
        TypeS.Update(new Type(ID, TYPE));
        labelinfo.setText("Type est modifier avec succes");
        LoadTypes();
    }

    public void LoadTypes() {
        List<Type> types = TypeS.getAll();
        ObservableList<Type> observableList = FXCollections.observableList(types);
        tableT.setItems(observableList);
        tableid.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabletype.setCellValueFactory(new PropertyValueFactory<>("type"));
    }
}
