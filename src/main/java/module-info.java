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

    opens com.example.trokyy to javafx.fxml;
    exports com.example.trokyy;
    exports com.example.trokyy.controllers;
    opens com.example.trokyy.controllers to javafx.fxml;
    exports com.example.trokyy.models;
    opens com.example.trokyy.models to javafx.fxml;
    //exports com.example.trokyy.test;
   // opens com.example.trokyy.test to javafx.fxml;
}