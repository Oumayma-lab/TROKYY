package com.example.trokyy.models;

import java.util.Date;

public class Reclamation {
    //les attributs
    private int id;
    private int reclamateur_id;
    private Date date_reclamation;
    private String description_reclamation;
    private String statut_reclamation;
    private String type;

    private String image_path;

    private boolean vu;


// Constructeur par défaut


    public Reclamation(int id, int reclamateur_id, Date date_reclamation, String description_reclamation, String statut_reclamation, String type, String image_path) {
        this.id = id;
        this.reclamateur_id = reclamateur_id;
        this.date_reclamation = date_reclamation;
        this.description_reclamation = description_reclamation;
        this.statut_reclamation = statut_reclamation;
        this.type = type;
        this.image_path = image_path;
    }

    public Reclamation() {
    }


    public Reclamation(int id, Date date_reclamation, String description_reclamation, String statut_reclamation, String type) {
        this.id = id;
        this.date_reclamation = date_reclamation;
        this.description_reclamation = description_reclamation;
        this.statut_reclamation = statut_reclamation;
        this.type = type;
    }

    public Reclamation(String description_reclamation, String type) {
        this.description_reclamation = description_reclamation;
        this.type = type;
    }

    public Reclamation(String description_reclamation, String type, String image_path) {
        this.description_reclamation = description_reclamation;
        this.type = type;
        this.image_path = image_path;
    }


    public Reclamation(int id, Date date_reclamation, String description_reclamation, String statut_reclamation, String type, String image_path) {
        this.id = id;
        this.date_reclamation = date_reclamation;
        this.description_reclamation = description_reclamation;
        this.statut_reclamation = statut_reclamation;
        this.type = type;
        this.image_path = image_path;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReclamateur_id() {
        return reclamateur_id;
    }

    public void setReclamateur_id(int reclamateur_id) {
        this.reclamateur_id = reclamateur_id;
    }

    public Date getDate_reclamation() {
        return date_reclamation;
    }

    public void setDate_reclamation(Date date_reclamation) {
        this.date_reclamation = date_reclamation;
    }

    public String getDescription_reclamation() {
        return description_reclamation;
    }

    public void setDescription_reclamation(String description_reclamation) {
        this.description_reclamation = description_reclamation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatut_reclamation() {
        return statut_reclamation;
    }

    public void setStatut_reclamation(String statut_reclamation) {
        this.statut_reclamation = statut_reclamation;
    }

    // les getters et les setters pour l'image


    public String getImage_path() {

        return image_path;
    }

    public void setImage_path(String image_path) {

        this.image_path = image_path;
    }

    public Reclamation(int id, int reclamateur_id, Date date_reclamation, String description_reclamation, String statut_reclamation, String type, String image_path, boolean vu) {
        this.id = id;
        this.reclamateur_id = reclamateur_id;
        this.date_reclamation = date_reclamation;
        this.description_reclamation = description_reclamation;
        this.statut_reclamation = statut_reclamation;
        this.type = type;
        this.image_path = image_path;
        this.vu = vu;
    }

    public boolean isVu() {
        return vu;
    }

    public void setVu(boolean vu) {
        this.vu = vu;
    }


    private Utilisateur user;


    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }

    public int getUserId() {
        if (user != null) {
            return user.getId();
        } else {
            return 3;
        }
    }


}