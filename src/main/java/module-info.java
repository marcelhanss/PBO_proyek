module org.example.uas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires kernel;
    requires io;
    requires layout;
    requires javafx.media;


    opens org.example.uas to javafx.fxml;
    exports org.example.uas;
    exports org.example.uas.beans;
    opens org.example.uas.beans to javafx.fxml;
    exports org.example.uas.controller;
    opens org.example.uas.controller to javafx.fxml;
    exports org.example.uas.Database;
    opens org.example.uas.Database to javafx.fxml;
    exports org.example.uas.dao;
    opens org.example.uas.dao to javafx.fxml;
}