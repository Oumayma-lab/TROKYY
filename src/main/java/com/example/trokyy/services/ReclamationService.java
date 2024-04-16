package com.example.trokyy.services;


import com.example.trokyy.interfaces.IService;
import com.example.trokyy.models.Reclamation;
import com.example.trokyy.models.Reponse;
import com.example.trokyy.tools.MyDataBaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReclamationService implements IService<Reclamation> {
    Connection connection = new MyDataBaseConnection().getConnection();
    @Override
    public void addReclamation(Reclamation reclamation) {

        // Obtention de la date actuelle
        Timestamp dateActuelle = new Timestamp(System.currentTimeMillis());

        // Préparation de la requête SQL avec des paramètres de substitution
        String requete = "INSERT INTO Reclamation (date_reclamation, description_reclamation, " +
                "statut_reclamation, type) VALUES (?, ?, ?, ?)";

        try (Connection connection = new MyDataBaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(requete)) {

            // Attribution des valeurs aux paramètres de la requête
            statement.setTimestamp(1, dateActuelle); // Date actuelle
            statement.setString(2, reclamation.getDescription_reclamation());
            statement.setString(3, reclamation.getStatut_reclamation());
            statement.setString(4, reclamation.getType());

            // Exécution de la requête d'insertion
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Réclamation ajoutée avec succès !");
            } else {
                System.out.println("Échec de l'ajout de la réclamation !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
        }




      /*  String requete = "INSERT INTO Reclamation ( date_reclamation, description_reclamation, " +
                "statut_reclamation, type) VALUES ('" +
                reclamation.getDateReclamation() + "', '" +
                reclamation.getDescriptionReclamation() + "', '" +
                reclamation.getStatutReclamation() + "', '" +
                reclamation.getType() + "')";

        try {
            Statement st = new MyDataBaseConnection().getConnection().createStatement();
            int rowsInserted = st.executeUpdate(requete);

            if (rowsInserted > 0) {
                System.out.println("Réclamation ajoutée avec succès !");
            } else {
                System.out.println("Échec de l'ajout de la réclamation !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
        }*/
    }


    @Override
    public void deleteReclamation(Reclamation reclamation) {
        String requete = "DELETE FROM Reclamation WHERE id = ?";

        try (Connection connection = new MyDataBaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(requete)) {

            // Définition de la valeur du paramètre ID de la réclamation à supprimer
            statement.setInt(1, reclamation.getId());

            // Exécution de la requête de suppression
            int rowsDeleted = statement.executeUpdate();

            // Vérification si des lignes ont été supprimées avec succès
            if (rowsDeleted > 0) {
                System.out.println("Réclamation supprimée avec succès !");
            } else {
                System.out.println("Échec de la suppression de la réclamation !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la réclamation : " + e.getMessage());

        }}

    @Override
    public void updateReclamation(Reclamation reclamation) {

        if (reclamation == null) {
            System.out.println("L'objet Reclamation est null. Impossible de mettre à jour la réclamation.");
            return;
        }

        if (reclamation.getDate_reclamation() == null) {
            System.out.println("La date de la réclamation est null. Impossible de mettre à jour la réclamation.");
            return;
        }

        String requete = "UPDATE Reclamation SET date_reclamation = ?, description_reclamation = ?, " +
                "statut_reclamation = ?, type = ? WHERE id = ?";

        try (Connection connection = new MyDataBaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(requete)) {

            statement.setTimestamp(1, new java.sql.Timestamp(reclamation.getDate_reclamation().getTime()));
            statement.setString(2, reclamation.getDescription_reclamation());
            statement.setString(3, reclamation.getStatut_reclamation());
            statement.setString(4, reclamation.getType());
            statement.setInt(5, reclamation.getId());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Réclamation mise à jour avec succès !");
            } else {
                System.out.println("Échec de la mise à jour de la réclamation !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la réclamation : " + e.getMessage());
        }

    }

    @Override
    public List<Reclamation> getData() {

        List<Reclamation> reclamations = new ArrayList<>();
        String requete = "SELECT * FROM Reclamation";

        try (Connection connection = new MyDataBaseConnection().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(requete)) {

            while (resultSet.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(resultSet.getInt("id"));
                reclamation.setDate_reclamation(resultSet.getTimestamp("date_reclamation"));
                reclamation.setDescription_reclamation(resultSet.getString("description_reclamation"));
                reclamation.setStatut_reclamation(resultSet.getString("statut_reclamation"));
                reclamation.setType(resultSet.getString("type"));
                reclamations.add(reclamation);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réclamations : " + e.getMessage());
        }
        for (Reclamation reclamation : reclamations) {
            reclamationMap.put(reclamation.getId(), reclamation);
        }
        return reclamations;

    }


    public List<Reponse> getReponses(int id) {
        List<Reponse> reps = new ArrayList<>();
        try {
            String requete = "SELECT * FROM reponse r where idr ="+id +" order by date_reponse desc";
            Connection connection = new MyDataBaseConnection().getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs =  statement.executeQuery(requete);
            while(rs.next()){
                Reponse r = new Reponse();
                r.setId(rs.getInt("id"));
                r.setReclam_reponse_id(rs.getInt("reclam_reponse_id"));
                r.setAdmin_id(rs.getInt("admin_id"));
                r.setDescription(rs.getString("description"));
                r.setDate_reponse(rs.getDate("date_reponse"));
                reps.add(r);

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return reps;
    }

    public int compter_rec() {

        int i = 0;
        String requete = "SELECT COUNT(*) as n FROM reclamation";

        try {
            PreparedStatement pst = connection.prepareStatement(requete);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                i = rs.getInt("n");
                System.out.println("le nombre de Reclamations est " + i);

            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return i;
    }

    public List<Reclamation> getMessages(int id) {
        List<Reclamation> ReclamationsList = new ArrayList<>();

        try {
            String requete = "SELECT * FROM reclamation r where id ="+id+" order by date_reclamation desc";
            //   String requete2 = "SELECT * FROM utilisateur u where idu ="+idU +" order by dater";
            Statement st = connection.createStatement();
            ResultSet rs =  st.executeQuery(requete);
            //  ResultSet rs2 = st.executeQuery(requete2);
            while(rs.next()){
                Reclamation r = new Reclamation();
                r.setId(rs.getInt("id"));

                r.setType(rs.getString("type"));
                r.setStatut_reclamation(rs.getString("Statut_reclamation"));
                r.setDate_reclamation(rs.getTimestamp("date_reclamation"));
                r.setDescription_reclamation(rs.getString("description_reclamation"));

                ReclamationsList.add(r);

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return ReclamationsList;
    }



    // Simulation de données avec une map (ID -> Réclamation)
    private static Map<Integer, Reclamation> reclamationMap = new HashMap<>();

    // Méthode pour ajouter une réclamation
    public void addReclamationMap(Reclamation reclamation) {
        reclamationMap.put(reclamation.getId(), reclamation);
    }

    // Méthode pour récupérer une réclamation par ID
    public static Reclamation getReclamationById(int id) {
        return reclamationMap.get(id);
    }

}