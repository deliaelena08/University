module org.example.adoptii {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.adoptii to javafx.fxml;
    exports org.example.adoptii;
    exports org.example.adoptii.controllers;
    opens org.example.adoptii.controllers to javafx.fxml;
}