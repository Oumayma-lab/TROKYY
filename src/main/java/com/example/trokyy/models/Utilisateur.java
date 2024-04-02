package com.example.trokyy.models;

import java.util.Date;
import java.util.List;
public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String mdp; // Correspond à "mdp" dans la base de données
    private String username;
    private Date dateInscription;
    private String photoProfil;
    private String adresse;
    private int tel ;
    private List<String> roles;
    //private String roles;
    private boolean isActive;

    public Utilisateur(int id, String nom, String prenom, String email, String username, String mdp, Date dateInscription, String photoProfil, String adresse, String tel, List<String> roles, boolean isActive) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.username = username;
        this.mdp = mdp;
        this.dateInscription = dateInscription;
        this.photoProfil = photoProfil;
        this.adresse = adresse;
        this.tel = Integer.parseInt(tel);
        this.roles = roles;
        this.isActive = isActive;
    }

    public Utilisateur(String nom, String prenom, String email, String mdp, String adresse, String tel) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.adresse = adresse;
        this.tel = Integer.parseInt(tel);
    }

    public Utilisateur(int id, String nom, String prenom, String email, String mdp) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
    }
    public Utilisateur( String nom, String prenom, String email, String mdp) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
    }


    public Utilisateur() {
    }

    public Utilisateur(String mdp, String username) {
        this.mdp = mdp;
        this.username = username;

    }

    public Utilisateur(String nom, String prenom, String email, String mdp, String username, String photoProfil, String adresse, int tel) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getPhotoProfil() {
        return photoProfil;
    }

    public void setPhotoProfil(String photoProfil) {
        this.photoProfil = photoProfil;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getTelephone() {
        return tel;
    }

    public void setTelephone(int tel) {
        this.tel = tel;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


}
