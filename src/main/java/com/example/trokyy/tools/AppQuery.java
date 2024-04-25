package com.example.trokyy.tools;

import com.example.trokyy.services.DisplayQuery;
import com.example.trokyy.models.Reclamation;
import com.example.trokyy.models.Reponse;
import com.example.trokyy.tools.MyDataBaseConnection;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AppQuery {







    public void addReclamation(Reclamation reclamation) {
        // Obtention de la date actuelle
        Timestamp dateActuelle = new Timestamp(System.currentTimeMillis());

        MyDataBaseConnection connection = new MyDataBaseConnection();

        String query = "INSERT INTO Reclamation (date_reclamation, description_reclamation, statut_reclamation, type,image_path, reclamateur_id) VALUES (?, ?, ?, ?,?,?)";
        try (Connection conn = connection.getConnection();
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

            for (Reclamation complaint : updatedComplaints) {
                System.out.println(complaint.toString());
            }
        } catch (SQLException e) {
            System.out.println("Error adding complaint: " + e.getMessage());
        }
    }

    public void updateReclamation(Reclamation reclamation) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = new MyDataBaseConnection().getConnection();
            String query = "UPDATE reclamation SET description_reclamation=?, type=?, image_path=? WHERE id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, reclamation.getDescription_reclamation());
            preparedStatement.setString(2, reclamation.getType());
            preparedStatement.setString(3, reclamation.getImage_path());
            preparedStatement.setInt(4, reclamation.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AppQuery.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Fermer la connexion et les ressources
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(AppQuery.class.getName()).log(Level.SEVERE, null, ex);
            }
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

        String password = "fnzxvnehjospijxt";

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
            message.setSubject("Thank you for your feedback");
            message.setText("Dear Customer,\n\nThank you for your feedback. We appreciate your input.\n\nBest regards,\nYour Company");

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }








}
