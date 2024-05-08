package com.example.trokyy.services;


import com.example.trokyy.interfaces.IEvenement;
import com.example.trokyy.models.Evenement;
import com.example.trokyy.tools.MyDataBaseConnection;

import javax.mail.Message;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement implements IEvenement<Evenement> {
    private Connection cnx;

    public ServiceEvenement() {
        cnx = MyDataBaseConnection.getInstance().getConnection();

    }



    @Override
    public void Add(Evenement evenement) {
        String qry = "INSERT INTO `evenement`(`titre`, `description`, `lieu`, `lien`, `date_debut`, `date_fin`, `type`) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement stm = cnx.prepareStatement(qry);
            stm.setString(1, evenement.getTitre());
            stm.setString(2, evenement.getDescription());
            stm.setString(3, evenement.getLieu());
            stm.setString(4, evenement.getLien());
            stm.setDate(5, evenement.getDate_debut());
            stm.setDate(6, evenement.getDate_fin());
            stm.setInt(7, evenement.getType());
            stm.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Evenement> afficher() {
        List<Evenement> events = new ArrayList<>();
        String sql = "SELECT `id`, `titre`, `description`, `lieu`, `lien`, `date_debut`, `date_fin`, `type` FROM `evenement`";
        try {
            Statement ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(sql);
            while (rs.next()) {
                Evenement event = new Evenement();
                event.setId(rs.getInt("id"));
                event.setTitre(rs.getString("titre"));
                event.setDescription(rs.getString("description"));
                event.setLieu(rs.getString("lieu"));
                event.setLien(rs.getString("lien"));
                event.setDate_debut(rs.getDate("date_debut"));
                event.setDate_fin(rs.getDate("date_fin"));
                event.setType(rs.getInt("type"));
                events.add(event);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return events;
    }

    @Override
    public List<Evenement> TriparTitre() {
        List<Evenement> events = new ArrayList<>();
        String sql = "SELECT `id`, `titre`, `description`, `lieu`, `lien`, `date_debut`, `date_fin`, `type` FROM `evenement` ORDER BY `titre`";
        ;
        try {
            Statement ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(sql);
            while (rs.next()) {
                Evenement event = new Evenement();
                event.setId(rs.getInt("id"));
                event.setTitre(rs.getString("titre"));
                event.setDescription(rs.getString("description"));
                event.setLieu(rs.getString("lieu"));
                event.setLien(rs.getString("lien"));
                event.setDate_debut(rs.getDate("date_debut"));
                event.setDate_fin(rs.getDate("date_fin"));
                event.setType(rs.getInt("type"));
                events.add(event);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return events;
    }

    @Override
    public List<Evenement> TriparLieu() {
        List<Evenement> events = new ArrayList<>();
        String sql = "SELECT `id`, `titre`, `description`, `lieu`, `lien`, `date_debut`, `date_fin`, `type` FROM `evenement` ORDER BY `lieu`";
        try {
            Statement ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(sql);
            while (rs.next()) {
                Evenement event = new Evenement();
                event.setId(rs.getInt("id"));
                event.setTitre(rs.getString("titre"));
                event.setDescription(rs.getString("description"));
                event.setLieu(rs.getString("lieu"));
                event.setLien(rs.getString("lien"));
                event.setDate_debut(rs.getDate("date_debut"));
                event.setDate_fin(rs.getDate("date_fin"));
                event.setType(rs.getInt("type"));
                events.add(event);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return events;
    }

    @Override
    public List<Evenement> Rechreche(String recherche) {
        List<Evenement> events = new ArrayList<>();
        String sql = "SELECT `id`, `titre`, `description`, `lieu`, `lien`, `date_debut`, `date_fin`, `type` FROM `evenement` WHERE `titre` LIKE '%" + recherche + "%' OR `lieu` LIKE '%" + recherche + "%' OR `id`LIKE '%" + recherche + "%'";
        try {
            Statement ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(sql);
            while (rs.next()) {
                Evenement event = new Evenement();
                event.setId(rs.getInt("id"));
                event.setTitre(rs.getString("titre"));
                event.setDescription(rs.getString("description"));
                event.setLieu(rs.getString("lieu"));
                event.setLien(rs.getString("lien"));
                event.setDate_debut(rs.getDate("date_debut"));
                event.setDate_fin(rs.getDate("date_fin"));
                event.setType(rs.getInt("type"));
                events.add(event);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return events;
    }

    @Override
    public void Update(Evenement evenement) {
        try {
            String qry = "UPDATE `evenement` SET `titre`=?,`description`=?,`lieu`=?,`lien`=?,`date_debut`=?,`date_fin`=?,`type`=? WHERE `id`=?";
            PreparedStatement stm = cnx.prepareStatement(qry);
            stm.setString(1, evenement.getTitre());
            stm.setString(2, evenement.getDescription());
            stm.setString(3, evenement.getLieu());
            stm.setString(4, evenement.getLien());
            stm.setDate(5, evenement.getDate_debut());
            stm.setDate(6, evenement.getDate_fin());
            stm.setInt(7, evenement.getType());
            stm.setInt(8, evenement.getId());
            stm.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void DeleteByID(int id) {
        try {
            String qry = "DELETE FROM `evenement` WHERE id=?";
            PreparedStatement smt = cnx.prepareStatement(qry);
            smt.setInt(1, id);
            smt.executeUpdate();
            System.out.println("Suppression Effectue");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public boolean CheckDate(Date debut, Date fin) {
        boolean result = false;
        String qry = "SELECT * FROM `evenement` WHERE (date_debut BETWEEN ? AND ?) OR (date_fin BETWEEN ? AND ?)";
        try {
            PreparedStatement stm = cnx.prepareStatement(qry);
            stm.setDate(1, debut);
            stm.setDate(2, fin);
            stm.setDate(3, debut);
            stm.setDate(4, fin);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
