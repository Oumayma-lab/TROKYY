package com.example.trokyy.controllers.Event;

import com.example.trokyy.models.Evenement;
import com.example.trokyy.services.ServiceEvenement;
import com.example.trokyy.tools.MyDataBaseConnection;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import com.google.zxing.WriterException;


import java.io.*;
import java.sql.*;
import java.util.EnumMap;
import java.util.Map;

public class EventCardController {
    @FXML
    private Pane Card;
    @FXML
    private Label datedebut;
    @FXML
    private Label datefin;
    @FXML
    private Label desc;
    @FXML
    private Label lieu;
    @FXML
    private Label titre;
    @FXML
    private Label type;
    @FXML
    private Label lien;
    private final ServiceEvenement EventS = new ServiceEvenement();
    int id, idType;
    Date debut, fin;
    String Titre, Lieu,Lien, Desc;
    private String[] colors = {"#e7f7ff", "#ceefff", "#b4e6ff"};
    private Connection cnx;


    @FXML
    private ImageView qrView;



    public EventCardController() {
        cnx = MyDataBaseConnection.getInstance().getConnection();
    }


    public void initialize() {

    }

    private Image generateQRCodeImage(String text) throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 300, 300, hints);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return new Image(new ByteArrayInputStream(pngData));
    }
    public void setData(Evenement event) {
        try {
            Image qrImage = generateQRCodeImage(event.getLien());
            qrView.setImage(qrImage);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        titre.setText(event.getTitre());
        datedebut.setText(String.valueOf("Debut: " + event.getDate_debut()));
        datefin.setText(String.valueOf("Fin: " + event.getDate_fin()));
        desc.setText(event.getDescription());
        lieu.setText(event.getLieu());
        lien.setText(event.getLien());
        String sql = "SELECT `type` FROM `type` WHERE `id` LIKE '%" + event.getType() + "%'";
        try {
            Statement ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(sql);
            while (rs.next()) {
                type.setText(rs.getString("type"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        // Card.setBackground(Background.fill(Color.web(colors[(int) (Math.random() * colors.length)])));


        BackgroundFill backgroundFill = new BackgroundFill(Color.web(colors[(int) (Math.random() * colors.length)]), null, null);
        Background background = new Background(backgroundFill);
        Card.setBackground(background);



        Card.setStyle("-fx-border-radius: 5px;-fx-border-color:#808080");

        id = event.getId();
        idType = event.getType();
        debut = event.getDate_debut();
        fin = event.getDate_fin();
        Titre = event.getTitre();
        Lieu = event.getLieu();
        Desc = event.getDescription();
        Lien = event.getLien();
    }

    @FXML
    void ModEvent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/Event/GestionEvents.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            EvenementController EC = loader.getController();
            EC.idtf.setText(String.valueOf(id));
            EC.titretf.setText(Titre);
            EC.desctf.setText(Desc);
            EC.lieutf.setText(Lieu);
            EC.lientf.setText(Lien);
            EC.typeid.setText(String.valueOf(idType));
            EC.datedebut.setValue(debut.toLocalDate());
            EC.datefin.setValue(fin.toLocalDate());
            stage.setScene(scene);
            stage.show();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void suppEvent(ActionEvent event) {
        EventS.DeleteByID(id);
    }
}
