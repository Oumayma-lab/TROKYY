module com.example.trokyy {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires de.jensd.fx.glyphs.commons;
    requires de.jensd.fx.glyphs.materialdesignicons;
    requires de.jensd.fx.glyphs.fontawesome;
    requires jbcrypt;
    requires com.jfoenix;
    requires java.desktop;
    requires java.mail;
    requires google.api.client;
    requires com.google.api.client;
    requires google.api.services.oauth2.v2.rev157;
    requires org.json;
    requires org.apache.pdfbox;
    requires twilio;
    requires org.bytedeco.opencv;
    requires java.activation;
    //requires opencv;
    requires javafx.swing;
    requires webcam.capture;


    opens com.example.trokyy to javafx.fxml;
    exports com.example.trokyy;
    exports com.example.trokyy.controllers;
    opens com.example.trokyy.controllers to javafx.fxml;
    exports com.example.trokyy.models;
    opens com.example.trokyy.models to javafx.fxml;
    exports com.example.trokyy.controllers.Admin;
    opens com.example.trokyy.controllers.Admin to javafx.fxml;
    exports com.example.trokyy.controllers.Reclamation;
    opens com.example.trokyy.controllers.Reclamation to javafx.fxml;
    exports com.example.trokyy.services;
    opens com.example.trokyy.services to javafx.fxml;
    exports com.example.trokyy.controllers.User;
    opens com.example.trokyy.controllers.User to javafx.fxml;

    //exports com.example.trokyy.test;
   // opens com.example.trokyy.test to javafx.fxml;
}