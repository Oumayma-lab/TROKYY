package com.example.trokyy.controllers;


import com.example.trokyy.models.Offer;
import com.example.trokyy.tools.MyDataBaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class OfferController implements Initializable {

    // crud page
    int id=0;
    Connection con=null;
    PreparedStatement st =null;
    ResultSet rs =null;
    private ObservableList<Offer> offerList;
    @FXML
    private ComboBox<String> category;
    @FXML
    private ComboBox<String> etat;
    private final String[] category_list= {"kids","beauty","mode","material"};
    private final String[] etat_list= {"good ","excellent","satisfactory"};
    @FXML
    private Button btnSave;
    @FXML
    private Button btnUpDate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;

    @FXML
    private Button btnImage1;

    @FXML
    private Button btnImage2;

    @FXML
    private Button btnImage3;

    @FXML
    private ImageView imageView1;

    @FXML
    private ImageView imageView2;

    @FXML
    private ImageView imageView3;

    private Image img;

    @FXML
    private TextField description;

    @FXML
    private TextField title;

    @FXML
    private TableColumn<Offer, String> descriptionColumn;

    @FXML
    private TableView<Offer> table;
    @FXML
    private TableColumn<Offer, String> titleColumn;
    @FXML
    private TableColumn<Offer, String> categoryColoumn;
    @FXML
    private TableColumn<Offer, String> etatColoumn;

    // show page
    @FXML
    private GridPane offerContainer;
    //card offer
    //offer card
    @FXML
    private VBox card;
    @FXML
    private Label etatOffer;
    @FXML
    private Label offerTitle;
    @FXML
    private ImageView imageOffer;
    @FXML
    private Button btnDetailOffer;

    @FXML
    void showDetailOffer(ActionEvent event) {
    }

    @FXML
    private Button btnTrade;

    @FXML
    void sendDemand(ActionEvent event) {
    }

    public void setDataCard(Offer offer){
        imageOffer.setImage(new Image(offer.getImage1()));
        offerTitle.setText(offer.getTitle());
        etatOffer.setText(offer.getEtat());
    }




    //upload image
    public void addOffreInsertImage(){
        FileChooser fileChooser =new FileChooser();
        fileChooser.setTitle("Choose Image ");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File file=fileChooser.showOpenDialog(null);
        if(file !=null){
            try {
                Image image = new Image(new FileInputStream(file));
                imageView1.setImage(image);
                img=image;
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public void addOffreInsertImage2(){}
    public void addOffreInsertImage3(){}

    //ajouter offre
    @FXML
    void createOffer(ActionEvent event) {
        String insert="insert into offres(title,description,category, image1, etat) values(?,?,?,?,?)";
        try {
            Connection connection = MyDataBaseConnection.getConnection();
            st = con.prepareStatement(insert);
            st.setString(1,title.getText());
            st.setString(2,description.getText());
            st.setString(3,category.getValue());
            st.setString(4,img.toString());
            st.setString(5,etat.getValue());
            st.executeUpdate();
            showOffers();
            clear();
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    // recupperer donnée de offre selectionné
    @FXML
    void getData(MouseEvent event) {
        Offer offer=table.getSelectionModel().getSelectedItem();
        id=offer.getId();
        title.setText(offer.getTitle());
        description.setText(offer.getDescription());
        category.setValue(offer.getCategory());
        etat.setValue(offer.getEtat());
        btnSave.setDisable(true);
    }

    @FXML
    void deleteOffer(ActionEvent event) {
        String delete="delete from offres where id=?";
        Connection connection = MyDataBaseConnection.getConnection();
        try {
            st=con.prepareStatement(delete);
            st.setInt(1,id);
            st.executeUpdate();
            showOffers();
            clear();

        }catch (SQLException e){
            throw new RuntimeException();
        }


    }
    @FXML
    void clearFiled(ActionEvent event) {
        clear();
    }
    void clear(){
        title.setText(null);
        description.setText(null);
        category.setValue(null);
        etat.setValue(null);
        btnSave.setDisable(false);
    }
    @FXML
    void upDateOffer(ActionEvent event) throws SQLException {
        String update="update offres set title=?, description=? , category=? , etat=? where id=?";
        Connection connection = MyDataBaseConnection.getConnection();
        try {

            st = con.prepareStatement(update);
            st.setString(1,title.getText());
            st.setString(2,description.getText());
            st.setString(3,category.getValue());
            st.setString(4,etat.getValue());
            st.setInt(5,id);

            st.executeUpdate();
            clear();
            showOffers();

        }catch(SQLException e){
            throw new RuntimeException();
        }

    }

    public ObservableList<Offer>  getOffers(){ //reccuperer donnée
        ObservableList<Offer> offres= FXCollections.observableArrayList();

        String query="select * from offres";
        Connection connection = MyDataBaseConnection.getConnection();

        try {
            st=con.prepareStatement(query);
            rs=st.executeQuery();

            while(rs.next()){
                Offer offre=new Offer();

                offre.setId(rs.getInt("id"));
                offre.setTitle(rs.getString("title"));
                offre.setDescription(rs.getString("description"));
                offre.setCategory(rs.getString("category"));
                offre.setEtat(rs.getString("etat"));
                offre.setImage1( rs.getString("image1"));
//                offre.setImage2((File) rs.getObject("image2"));
//                offre.setImage3((File) rs.getObject("image3"));
                offres.add(offre);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            throw  new RuntimeException(e);
        }

        return  offres;
    }
    public void showOffers(){  // afficher dans tableau
        ObservableList<Offer> list= getOffers();
        table.setItems(list);
        titleColumn.setCellValueFactory(new PropertyValueFactory<Offer,String>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Offer,String>("description"));
        categoryColoumn.setCellValueFactory(new PropertyValueFactory<Offer,String>("category"));
        etatColoumn.setCellValueFactory(new PropertyValueFactory<Offer,String>("etat"));
    }

    //affichage de card
    public void showCardOffer() {
        int row = 0;
        int col = 0;
        for (Offer offer : offerList) {
            try {
                // Chargement de la carte d'offre depuis le fichier FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Offers/offer.fxml"));
                VBox card = loader.load();

                // Remplissage des champs de la carte d'offre
                ImageView imageOffer = (ImageView) card.lookup("#imageOffer");
                Label titleOffer = (Label) card.lookup("#titleOffer");
                Label etatOffer = (Label) card.lookup("#etatOffer");
                imageOffer.setImage(new Image(offer.getImage1()));
                titleOffer.setText(offer.getTitle());
                etatOffer.setText(offer.getEtat());

                // Ajout de la carte d'offre à la grille
                offerContainer.add(card, col, row);

                // Gestion des lignes et colonnes
                col++;
                if (col == 3) { // 3 cartes par ligne, ajustez selon vos besoins
                    col = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        category.getItems().addAll(category_list);
        etat.getItems().addAll(etat_list);
        showOffers();
        offerList = getOffers();
        // showCardOffer();

    }

}

