package com.example.trokyy.tools;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SessionManager {

    private static SessionManager instance;

    private static int id;
    private static String nom;
    private static String prenom;
    private static String email;
    private static String mdp;
    private static String username;
    private static LocalDateTime dateInscription;
    private static String photoProfil;
    private static String adresse;
    private static int tel;
    private static List<String> roles = new ArrayList<>(Collections.singletonList("ROLE_USER"));
    private static boolean isActive;

    private SessionManager(int id, String nom, String prenom, String email, String mdp,
                           String username, LocalDateTime dateInscription, String photoProfil,
                           String adresse, int tel, List<String> roles, boolean isActive) {
        SessionManager.id = id;
        SessionManager.nom = nom;
        SessionManager.prenom = prenom;
        SessionManager.email = email;
        SessionManager.mdp = mdp;
        SessionManager.username = username;
        SessionManager.dateInscription = dateInscription;
        SessionManager.photoProfil = photoProfil;
        SessionManager.adresse = adresse;
        SessionManager.tel = tel;
        SessionManager.roles = roles;
        SessionManager.isActive = isActive;
    }

    public static SessionManager getInstance(int id, String nom, String prenom, String email, String mdp,
                                             String username, LocalDateTime dateInscription, String photoProfil,
                                             String adresse, int tel, List<String> roles, boolean isActive) {
        if (instance == null) {
            instance = new SessionManager(id, nom, prenom, email, mdp, username, dateInscription,
                    photoProfil, adresse, tel, roles, isActive);
        }
        return instance;
    }

    public static SessionManager getInstance() {
        return instance;
    }

    public static void setInstance(SessionManager instance) {
        SessionManager.instance = instance;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        SessionManager.id = id;
    }

    public static String getNom() {
        return nom;
    }

    public static void setNom(String nom) {
        SessionManager.nom = nom;
    }

    public static String getPrenom() {
        return prenom;
    }

    public static void setPrenom(String prenom) {
        SessionManager.prenom = prenom;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        SessionManager.email = email;
    }

    public static String getMdp() {
        return mdp;
    }

    public static void setMdp(String mdp) {
        SessionManager.mdp = mdp;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        SessionManager.username = username;
    }

    public static LocalDateTime getDateInscription() {
        return dateInscription;
    }

    public static void setDateInscription(LocalDateTime dateInscription) {
        SessionManager.dateInscription = dateInscription;
    }

    public static String getPhotoProfil() {
        return photoProfil;
    }

    public static void setPhotoProfil(String photoProfil) {
        SessionManager.photoProfil = photoProfil;
    }

    public static String getAdresse() {
        return adresse;
    }

    public static void setAdresse(String adresse) {
        SessionManager.adresse = adresse;
    }

    public static int getTel() {
        return tel;
    }

    public static void setTel(int tel) {
        SessionManager.tel = tel;
    }

    public static List<String> getRoles() {
        return roles;
    }

    public static void setRoles(List<String> roles) {
        SessionManager.roles = roles;
    }

    public static boolean isActive() {
        return isActive;
    }

    public static void setActive(boolean isActive) {
        SessionManager.isActive = isActive;
    }

    public static void cleanUserSession() {
        id = 0;
        nom = "";
        prenom = "";
        email = "";
        mdp = "";
        username = "";
        dateInscription = null;
        photoProfil = "";
        adresse = "";
        tel = 0;
        roles = new ArrayList<>(Collections.singletonList("ROLE_USER"));
        isActive = false;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", id = '" + id + '\'' +
                ", privileges=" + roles +
                '}';
    }
}
