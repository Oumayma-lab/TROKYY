package com.example.trokyy.models;

import java.sql.Date;

public class Evenement {
    private int id , type ;
    private Date date_debut,date_fin;
    private String titre , description, lieu, lien;

    public Evenement() {}

    public Evenement(int id, String titre, String description, String lieu, String lien, Date date_debut, Date date_fin, int type) {
        this.id = id;
        this.type = type;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.titre = titre;
        this.description = description;
        this.lieu = lieu;
        this.lien = lien;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public int getType() {return type;}
    public void setType(int type) {this.type = type;}

    public Date getDate_debut() {return date_debut;}
    public void setDate_debut(Date date_debut) {this.date_debut = date_debut;}

    public Date getDate_fin() {return date_fin;}
    public void setDate_fin(Date date_fin) {this.date_fin = date_fin;}

    public String getTitre() {return titre;}
    public void setTitre(String titre) {this.titre = titre;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getLieu() {return lieu;}
    public void setLieu(String lieu) {this.lieu = lieu;}

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", type=" + type +
                ", date_debut=" + date_debut +
                ", date_fin=" + date_fin +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", lieu='" + lieu + '\'' +
                ", lien='" + lien + '\'' +
                '}';
    }
}
