package com.example.trokyy.services;


import com.example.trokyy.interfaces.IType;
import com.example.trokyy.models.Type;
import com.example.trokyy.tools.MyDataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceType implements IType<Type> {
    private static Connection cnx;
    public ServiceType() {
        cnx = MyDataBaseConnection.getInstance().getConnection();
    }

    @Override
    public void Add(Type type) {
        String qry = "INSERT INTO `type`(`type`) VALUES (?)";
        try {
            PreparedStatement stm = cnx.prepareStatement(qry);
            stm.setString(1, type.getType());
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public List<Type> afficher() {
        List<Type> types = new ArrayList<>();
        String sql = "SELECT `id`, `type` FROM `type`";
        try {
            Statement ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(sql);
            while (rs.next()) {
                Type type = new Type();
                type.setId(rs.getInt("id"));
                type.setType(rs.getString("type"));
                types.add(type);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return types;
    }
    @Override
    public ArrayList<Type> getAll() {
        ArrayList<Type> types = new ArrayList<>();
        String qry = "SELECT * FROM `type`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                Type type = new Type();
                type.setId(rs.getInt("id"));
                type.setType(rs.getString("type"));
                types.add(type);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return types;
    }
    @Override
    public void Update(Type type) {
        try {
            String qry = "UPDATE `type` SET `type`=? WHERE `id`=?";
            PreparedStatement stm = cnx.prepareStatement(qry);
            stm.setString(1, type.getType());
            stm.setInt(2, type.getId());
            stm.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    @Override
    public void DeleteByID(int id) {
        try {
            String qry = "DELETE FROM `type` WHERE id=?";
            PreparedStatement smt = cnx.prepareStatement(qry);
            smt.setInt(1, id);
            smt.executeUpdate();
            System.out.println("Suppression Effectue");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
