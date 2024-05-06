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

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import java.util.Properties;



public class ReclamationService implements IService<Reclamation> {
    Connection connection = new MyDataBaseConnection().getConnection();
   /* @Override
    public void addReclamation(Reclamation reclamation) {

        // Obtention de la date actuelle
        Timestamp dateActuelle = new Timestamp(System.currentTimeMillis());

        // Préparation de la requête SQL avec des paramètres de substitution
        String requete = "INSERT INTO Reclamation (date_reclamation, description_reclamation, statut_reclamation, type,image_path,reclamateur_id) VALUES (?, ?, ?, ?,?,?)";

        try (Connection connection = new MyDataBaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(requete)) {

            // Attribution des valeurs aux paramètres de la requête
            statement.setTimestamp(1, dateActuelle); // Date actuelle
            statement.setString(2, reclamation.getDescription_reclamation());
            statement.setString(3, "In progress"); // Statut par défaut
            statement.setString(4, reclamation.getType());
            statement.setString(5, reclamation.getImage_path());
            statement.setInt(6, 3);
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

    }*/


    @Override
    public void deleteReclamation(Reclamation reclamation) {
        String requete = "DELETE FROM reclamation WHERE id = ?";

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
                "statut_reclamation = ?, type = ?, image_path = ? WHERE id = ?";

        try (Connection connection = new MyDataBaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(requete)) {

            statement.setTimestamp(1, new java.sql.Timestamp(reclamation.getDate_reclamation().getTime()));
            statement.setString(2, reclamation.getDescription_reclamation());
            statement.setString(3, reclamation.getStatut_reclamation());
            statement.setString(4, reclamation.getType());
            statement.setString(5, reclamation.getImage_path()); // Mise à jour de l'image_path
            statement.setInt(6, reclamation.getId());

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

    public boolean isDescriptionUnique(String description) {
        String query = "SELECT COUNT(*) AS count FROM Reclamation WHERE description_reclamation = ?";
        try (Connection connection = new MyDataBaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, description);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count == 0; // Si count est 0, la description est unique ; sinon, elle est déjà utilisée
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'unicité de la description : " + e.getMessage());
        }
        return false; // En cas d'erreur, renvoyer false par défaut
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


    @Override
    public void addReclamation(Reclamation reclamation) {     // Obtention de la date actuelle
        Timestamp dateActuelle = new Timestamp(System.currentTimeMillis());


        Connection connection = new MyDataBaseConnection().getConnection();

        String query = "INSERT INTO Reclamation (date_reclamation, description_reclamation, statut_reclamation, type,image_path, reclamateur_id) VALUES (?, ?, ?, ?,?,?)";
        try ( Connection conn = new MyDataBaseConnection().getConnection();
             PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Attribution des valeurs aux paramètres de la requête
            statement.setTimestamp(1, dateActuelle); // Date actuelle
            statement.setString(2, reclamation.getDescription_reclamation());
            statement.setString(3, "In progress"); // Statut par défaut
            statement.setString(4, reclamation.getType());
            statement.setString(5, reclamation.getImage_path());
            statement.setInt(6, 3);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating complaint failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int complaintId = generatedKeys.getInt(1);
                    String userEmail = getUserEmailById(reclamation.getUserId());

                    sendEmail(userEmail);
                } else {
                    throw new SQLException("Creating complaint failed, no ID obtained.");
                }
            }

            System.out.println("Complaint added successfully!");
            DisplayQuery displayQuery = new DisplayQuery();
            List<Reclamation> updatedComplaints = displayQuery.getAllComplaints();

        } catch (SQLException e) {
            System.out.println("Error adding complaint: " + e.getMessage());
        }
    }




    public String getUserEmailById(int id) {
        String email = null;

        // SQL query to retrieve email by user ID
        String query = "SELECT email FROM utilisateur WHERE id = ?";

        try (
                Connection conn = new MyDataBaseConnection().getConnection();
                PreparedStatement statement = conn.prepareStatement(query)
        ) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    email = resultSet.getString("email");
                    System.out.println("Retrieved email: " + email);
                } else {
                    System.out.println("No email found for user with ID: " + id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return email;
    }


  public static void sendEmail(String recipientEmail) {
        // Sender's email
        String senderEmail = "batoutbata5@gmail.com";
        if (recipientEmail == null || recipientEmail.isEmpty()) {
            System.out.println("Recipient email is null or empty. Cannot send email.");
            return;
        }
        // Sender's password
        //  String password = "ialgvzhizvvrwozy";

        String password = "bvxlqtqlkcobnczw";

        // SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your Feedback Received");
            // Create a MimeMultipart object to hold the email content
            MimeMultipart multipart = new MimeMultipart();

            // Create and add a MimeBodyPart for the text content
            MimeBodyPart textPart = new MimeBodyPart();
            String contactInfo =  "<html>" +
                    "<body>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><b><font color=\"#000000\">Dear Customer,</font></b></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">Thank you for sharing your valuable feedback with us. Your insights are crucial in helping us improve our services to better serve you.</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">At TROKY, we're dedicated to providing top-notch service, and your feedback guides us in our ongoing efforts to exceed your expectations.</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">For any further assistance, feel free to contact our support team at <a href=\"mailto:troky@gmail.com\">troky@gmail.com</a> or 50 794 341. We're here to ensure your experience with us remains exceptional.</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">Thank you once again for choosing TROKY. We look forward to serving you again soon.</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">Best Regards,</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><b><font color=\"#007bff\">Complaints Management</font></b></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><b><font color=\"#007bff\">EMNA KHAMMASSI</font></b></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><b><font color=\"#007bff\">TROKY</font></b></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><a href=\"mailto:troky@gmail.com\">troky@gmail.com</a> |<b><font color=\"#007bff\">50 794 341</font></b></p>" +
                    "</body>" +
                    "</html>";
            textPart.setContent(contactInfo, "text/html");
            multipart.addBodyPart(textPart);

            // Set the content of the message to the MimeMultipart object
            message.setContent(multipart);
            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    // Méthode pour récupérer toutes les réclamations de la base de données et les trier par date d'ajout décroissante
    public List<Reclamation> getAllComplaintsOrderedByDate() {
        MyDataBaseConnection connection = new MyDataBaseConnection();
        List<Reclamation> complaints = new ArrayList<>();
        String query = "SELECT * FROM reclamation ORDER BY date_reclamation DESC";
        try (Connection conn = connection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Reclamation complaint = new Reclamation();
                // Initialisez la réclamation avec les données de la base de données comme vous l'avez fait précédemment
                complaints.add(complaint);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving complaints: " + e.getMessage());
        }
        return complaints;
    }



    public int getNumberOfComplaintsFromDatabase() {
        int numberOfComplaints = 0;
        String query = "SELECT COUNT(*) AS count FROM reclamation WHERE statut_reclamation = 'In progress'";

        try (Connection connection = new MyDataBaseConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                numberOfComplaints = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            System.out.println("Error counting complaints in progress: " + e.getMessage());
        }

        return numberOfComplaints;
    }

}