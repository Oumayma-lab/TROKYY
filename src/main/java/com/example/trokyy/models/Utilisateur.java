package com.example.trokyy.models;

import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String mdp; // Correspond à "mdp" dans la base de données
    private String username;
    private LocalDateTime dateInscription;
    private String photoProfil;
    private String adresse;
    private int tel ;
    private List<String> roles = new ArrayList<>(Collections.singletonList("ROLE_USER"));
    //private String roles;
    private boolean isActive;


    public Utilisateur(String nom, String email, LocalDateTime dateInscription, String adresse, int tel, boolean isActive) {
        this.nom = nom;
        this.email = email;
        this.dateInscription = dateInscription;
        this.adresse = adresse;
        this.tel = tel;
        this.isActive = isActive;
    }

    public Utilisateur(String nom, String email, String mdp, String adresse, String tel) {
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



    public Utilisateur(String nom, String prenom, String email, String mdp, LocalDateTime dateInscription) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.dateInscription = dateInscription;



    }


    public Utilisateur(String name, int phoneNumber, String email, String address, Boolean status, String registrationDate) {
        this.nom = name;
        this.tel = phoneNumber;
        this.email = email;
        this.adresse = address;
        this.isActive = status;
        this.dateInscription= LocalDateTime.parse(registrationDate);
    }

    public Utilisateur(String nom, String prenom, String email, String mdp, String username, LocalDateTime dateInscription, int tel, boolean isActive) {
    }

    public Utilisateur(String nom,String prenom, int tel, String email, String adresse, boolean isActive, LocalDateTime dateInscription) {
        this.nom=nom;
        this.prenom=prenom;
        this.tel=tel;
        this.email=email;
        this.adresse=adresse;
        this.isActive=isActive;
        this.dateInscription=dateInscription;
    }

    public Utilisateur(int id, String nom, String prenom, int tel, String email, String adresse, boolean isActive, LocalDateTime dateInscription) {
        this.id=id;
        this.nom=nom;
        this.prenom=prenom;
        this.tel=tel;
        this.email=email;
        this.adresse=adresse;
        this.isActive=isActive;
        this.dateInscription=dateInscription;
    }

    public Utilisateur(int id, String nom, String prenom, int tel, String email, String adresse, boolean isActive) {
        this.id=id;
        this.nom=nom;
        this.prenom=prenom;
        this.tel=tel;
        this.email=email;
        this.adresse=adresse;
        this.isActive=isActive;
    }

    public Utilisateur(String nom, String prenom, String email, String mdp, int tel, String adresse) {
        this.nom=nom;
        this.prenom=prenom;
        this.email=email;
        this.mdp=mdp;
        this.tel= tel;
        this.adresse=adresse;
    }

    public Utilisateur(String nom, String prenom, String email, String mdp, TextField phoneNumber, String adresse) {

        this.nom=nom;
        this.prenom=prenom;
        this.email=email;
        this.mdp=mdp;
        this.tel= tel;
        this.adresse=adresse;

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

    public LocalDateTime getDateInscription() {
        return dateInscription;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean isAdmin() {
        return roles.contains("ROLE_ADMIN");
    }

    public String getFullName() {
        return nom + " " + prenom;

    }
}
