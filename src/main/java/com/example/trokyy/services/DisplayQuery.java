package com.example.trokyy.services;



import com.example.trokyy.models.Reclamation;
import com.example.trokyy.models.Reponse;
import com.example.trokyy.tools.MyDataBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DisplayQuery {
    public List<Reclamation> getAllComplaints() {
    MyDataBaseConnection connection = new MyDataBaseConnection();
    List<Reclamation> complaints = new ArrayList<>();
    String query = "SELECT * FROM reclamation";
    try (Connection conn = connection.getConnection();
         PreparedStatement statement = conn.prepareStatement(query)) {
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Reclamation complaint = new Reclamation();
            java.sql.Date date = resultSet.getDate("date_reclamation");
            if (date != null) {
                complaint.setDate_reclamation(date);
            } else {

                complaint.setDate_reclamation(new Date());
            }
            complaint.setType(resultSet.getString("type"));
            complaint.setDescription_reclamation(resultSet.getString("description_reclamation"));

            complaint.setImage_path(resultSet.getString("image_path"));
            complaint.setId(resultSet.getInt("id"));
            complaint.setStatut_reclamation(resultSet.getString("statut_reclamation"));

            complaints.add(complaint);
        }
    } catch (SQLException e) {
        System.out.println("Error retrieving complaints: " + e.getMessage());
    }
    return complaints;
}
    public void deleteComplaint(Reclamation complaintToDelete) {
        MyDataBaseConnection connection = new MyDataBaseConnection();
        String query = "DELETE FROM reclamation WHERE id = ?";
        try (Connection conn = connection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, complaintToDelete.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Complaint deleted successfully!");
            } else {
                System.out.println("No complaint deleted. The complaint with ID " +
                        complaintToDelete.getId() + " was not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting complaint: " + e.getMessage());
        }
    }

    public String getComplaintResponse(int complaintId) {
        MyDataBaseConnection connection = new MyDataBaseConnection();

        String response = null;
        String query = "SELECT description FROM reponse WHERE reclam_reponse_id = ?";

        try (Connection conn = connection.getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, complaintId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                response = resultSet.getString("description");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return response;
    }

    public void deleteResponse(int complaintId) {
        MyDataBaseConnection connection = new MyDataBaseConnection();
        String queryDelete = "DELETE FROM reponse WHERE reclam_reponse_id = ?";
        String queryUpdateStatus = "UPDATE reclamation SET statut_reclamation = 'In progress' WHERE id = ?";

        try (Connection conn = connection.getConnection();
             PreparedStatement statementDelete = conn.prepareStatement(queryDelete);
             PreparedStatement statementUpdateStatus = conn.prepareStatement(queryUpdateStatus)) {

            // Delete the response
            statementDelete.setInt(1, complaintId);
            int rowsAffected = statementDelete.executeUpdate();

            // Update the status of the complaint
            statementUpdateStatus.setInt(1, complaintId);
            statementUpdateStatus.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Response deleted successfully for Complaint ID: " + complaintId);
                System.out.println("Status updated to 'In progress: ' for Complaint ID: " + complaintId);
            } else {
                System.out.println("No response deleted for Complaint ID: " + complaintId);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting response or updating status: " + e.getMessage());
        }
    }


}
