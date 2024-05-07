package com.example.trokyy.models;

import java.time.LocalDate;

public class Blog {
    private int id;
    private int auteur_id;
    private String contenu;
    private String titre;
    private LocalDate date_publication;
    private String image;

    int  nombre_likes;

    // Constructeur
    public Blog() {
    }

    public Blog(int auteur_id, String contenu, String titre, LocalDate date_publication, String image ) {
        this.auteur_id = auteur_id;
        this.contenu = contenu;
        this.titre = titre;
        this.date_publication = date_publication;
        this.image = image;
    }

    public Blog(String contenu, String titre, String image) {
        this.contenu = contenu;
        this.titre = titre;
        this.image = image;
    }

    public Blog(int id, int auteur_id, String contenu, String titre, LocalDate date_publication, String image, int nombre_likes ) {
        this.id = id;
        this.auteur_id = auteur_id;
        this.contenu = contenu;
        this.titre = titre;
        this.date_publication = date_publication;
        this.image = image;
        this.nombre_likes = nombre_likes;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", auteur_id=" + auteur_id +
                ", contenu='" + contenu + '\'' +
                ", titre='" + titre + '\'' +
                ", date_publication=" + date_publication +
                ", image='" + image + '\'' +
                ", nombre_likes=" + nombre_likes +
                '}';
    }

    //getter and setter
    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public int getAuteur_id() {

        return auteur_id;
    }

    public void setAuteur_id(int auteur_id) {

        this.auteur_id = auteur_id;
    }

    public String getContenu() {

        return contenu;
    }

    public void setContenu(String contenu) {

        this.contenu = contenu;
    }

    public String getTitre() {

        return titre;
    }

    public void setTitre(String titre) {

        this.titre = titre;
    }

    public LocalDate getDate_publication() {

        return date_publication;
    }

    public void setDate_publication(LocalDate date_publication) {

        this.date_publication = date_publication;
    }

    public String getImage() {

        return image;
    }


    public int getNombre_likes() {
        return nombre_likes;
    }

    public void setNombre_likes(int nombre_likes) {
        this.nombre_likes = nombre_likes;
    }

    public void setImage(String image) {

        this.image = image;
    }


}

