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

    //requires deyl emnaa
    requires kernel;

    requires layout;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires org.json;





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
    exports com.example.trokyy.controllers.Admin.Reponse;
    opens com.example.trokyy.controllers.Admin.Reponse to javafx.fxml;
    exports com.example.trokyy.services;
    opens com.example.trokyy.services to javafx.fxml;
    exports com.example.trokyy.tools;
    opens com.example.trokyy.tools to javafx.fxml;
    //exports com.example.trokyy.test;
   // opens com.example.trokyy.test to javafx.fxml;
}