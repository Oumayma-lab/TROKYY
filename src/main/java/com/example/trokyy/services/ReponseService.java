package com.example.trokyy.services;

import com.example.trokyy.interfaces.IReponseService;
import com.example.trokyy.models.Reclamation;
import com.example.trokyy.models.Reponse;
import com.example.trokyy.tools.MyDataBaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseService implements IReponseService<Reponse> {
    @Override
    public void addReponse(Reponse reponse) {

        try {
            // Créer un timestamp pour la date actuelle
            Timestamp dateActuelle = new Timestamp(System.currentTimeMillis());

            // Définir la requête SQL pour insérer une nouvelle réponse
            String requete = "INSERT INTO reponse (reclam_reponse_id, admin_id, date_reponse, description) "
                    + "VALUES (?, ?, ?, ?)";

            // Préparer la requête SQL
            PreparedStatement pst = new MyDataBaseConnection().getConnection().prepareStatement(requete);

            // Définir les paramètres de la requête SQL
            pst.setInt(1, reponse.getReclam_reponse_id());
            pst.setInt(2, reponse.getAdmin_id());
            pst.setTimestamp(3, dateActuelle); // Utiliser le timestamp actuel pour la date de réponse
            pst.setString(4, reponse.getDescription());

            // Exécuter la requête SQL d'insertion
            pst.executeUpdate();
            System.out.println("Reponse insérée avec succès");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    // Méthode pour ajouter une réponse à une réclamation spécifique
    public void addReponseToReclamation(int reclamationId, Reponse reponse) {
        try {
            // Établir la connexion à la base de données


            // Requête SQL pour insérer une réponse dans la table reponse
            String query = "INSERT INTO reponse (reclam_reponse_id, description, date_reponse) VALUES (?, ?, ?)";

            // Préparation de la requête SQL
            PreparedStatement statement = new MyDataBaseConnection().getConnection().prepareStatement(query);;
            statement.setInt(1, reclamationId);
            statement.setString(2, reponse.getDescription());
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis())); // Date actuelle

            // Exécution de la requête
            statement.executeUpdate();

            System.out.println("Réponse ajoutée avec succès à la réclamation d'ID : " + reclamationId);

            // Fermeture des ressources
            statement.close();

        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout de la réponse à la réclamation : " + ex.getMessage());
        }
    }

    @Override
    public void deleteReponse(Reponse reponse) {

        try {
            String requete = "DELETE FROM reponse where id=?";
            PreparedStatement pst = new MyDataBaseConnection().getConnection().prepareStatement(requete);
            pst.setInt(1, reponse.getId());
            pst.executeUpdate();
            System.out.println("Réponse supprimée");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public void updateReponse(Reponse reponse) {
    try {
        String requete = "UPDATE reponse SET description=? Where id =?";
        PreparedStatement pst =new MyDataBaseConnection().getConnection().prepareStatement(requete);

        pst.setString(1, reponse.getDescription());
        pst.setInt(2,reponse.getId());
        pst.executeUpdate();
        System.out.println("Reclamation modifiée");
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }

    }

    @Override
    public List<Reponse> getDataReponse() {



        List<Reponse> ReponseList = new ArrayList<>();
        try {
            String requete = "SELECT * FROM reponse r ";
            Statement st = new MyDataBaseConnection().getConnection().createStatement();
            ResultSet rs =  st.executeQuery(requete);
            while(rs.next()){
                Reponse r = new Reponse();
                r.setId(rs.getInt("id"));
                r.setReclam_reponse_id(rs.getInt("Reclam_reponse_id"));
                r.setDescription(rs.getString("Description"));
                ReponseList.add(r);

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return ReponseList ;

    }


    }
