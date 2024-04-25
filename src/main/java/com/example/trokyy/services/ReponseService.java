package com.example.trokyy.services;

import com.example.trokyy.interfaces.IReponseService;
import com.example.trokyy.models.Reclamation;
import com.example.trokyy.models.Reponse;
import com.example.trokyy.tools.MyDataBaseConnection;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.example.trokyy.tools.MyDataBaseConnection;

public class ReponseService implements IReponseService<Reponse> {
    @Override
    public void addReponse(Reponse reponse) {
        try {
            // Créer un timestamp pour la date actuelle
            Timestamp dateActuelle = new Timestamp(System.currentTimeMillis());

            // Définir la requête SQL pour insérer une nouvelle réponse
            String query = "INSERT INTO reponse (reclam_reponse_id, admin_id, date_reponse, description) "
                    + "VALUES (?, ?, ?, ?)";
            String updateComplaintQuery = "UPDATE reclamation SET statut_reclamation = ? WHERE id = ?";
            try (
                    Connection conn = new MyDataBaseConnection().getConnection();

                    PreparedStatement updateStatement = conn.prepareStatement(updateComplaintQuery);

                    // Préparer la requête SQL avec la récupération des clés générées
                    PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
                // Définir les paramètres de la requête SQL
                statement.setInt(1, reponse.getReclam_reponse_id());
                statement.setInt(2, 1);
                statement.setTimestamp(3, dateActuelle); // Utiliser le timestamp actuel pour la date de réponse
                statement.setString(4, reponse.getDescription());

                // updateStatement.setString(1, status);
                updateStatement.setString(1, "Resolved");
                updateStatement.setInt(2,reponse.getReclam_reponse_id());




                updateStatement.executeUpdate();

                int affectedRows = statement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating Response failed, no rows affected.");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        String userEmail = getUserEmailById(reponse.getReclam_reponse_id());

                        sendEmail(userEmail);
                    } else {
                        throw new SQLException("Creating response failed, no ID obtained.");
                    }
                }

                System.out.println("Response added successfully!");
            }
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







    public String getUserEmailById(int id) {
        String email = null;
        String query = "SELECT u.email FROM utilisateur u INNER JOIN Reclamation r ON u.id = r.reclamateur_id WHERE r.id = ?";
        try (Connection conn = new MyDataBaseConnection().getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    email = resultSet.getString("email");
                    System.out.println("Retrieved email: " + email);
                } else {
                    System.out.println("No email found for reclamation with ID: " + id);
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
            message.setSubject("Your complaint has been successfully Resolved");
            // Create a MimeMultipart object to hold the email content
            MimeMultipart multipart = new MimeMultipart();

            // Create and add a MimeBodyPart for the text content
            MimeBodyPart textPart = new MimeBodyPart();
            String contactInfo =  "<html>" +
                    "<body>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><b><font color=\"#000000\">Dear ,</font></b></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">We are pleased to inform you that your recent complaint has been successfully Resolved. Your satisfaction is our priority, and we are delighted to have addressed your concerns effectively.</font></p></font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">At TROKY, we are committed to providing exceptional service, and your feedback plays a crucial role in helping us improve and deliver better experiences for our customers.</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">If you have any further questions or require additional assistance, please do not hesitate to contact our support team at <a href=\"mailto:troky@gmail.com\">troky@gmail.com</a> or call us at 50 794 341. We are here to ensure that your experience with us remains outstanding.</font></p>\n" +
                    "" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">Thank you once again for choosing TROKY. We appreciate your patience and understanding throughout this process.</font></p>" +
                    "<p style=\"font-family: Arial, sans-serif;\"><font color=\"#000000\">Best Regards,</font></p>" +

                    "<p style=\"font-family: Arial, sans-serif;\"><b><font color=\"#007bff\">Complaints Management</font></b></p>"+
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


}
