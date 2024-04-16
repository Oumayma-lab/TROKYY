package com.example.trokyy.models;

import java.util.Date;

public class Reponse {
      private int id;
    private int reclam_reponse_id;
    private int admin_id;
    private String description;
    private Date date_reponse;

    public Reponse() {
    }

    public Reponse(int id, int reclam_reponse_id, int admin_id, String description, Date date_reponse) {
        this.id = id;
        this.reclam_reponse_id = reclam_reponse_id;
        this.admin_id = admin_id;
        this.description = description;
        this.date_reponse = date_reponse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReclam_reponse_id() {
        return reclam_reponse_id;
    }

    public void setReclam_reponse_id(int reclam_reponse_id) {
        this.reclam_reponse_id = reclam_reponse_id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate_reponse() {
        return date_reponse;
    }

    public void setDate_reponse(Date date_reponse) {
        this.date_reponse = date_reponse;
    }
}
