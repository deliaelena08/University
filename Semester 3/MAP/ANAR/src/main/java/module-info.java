module org.example.anar {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.anar to javafx.fxml;
    exports org.example.anar;
    exports org.example.anar.controllers;
    opens org.example.anar.controllers to javafx.fxml;
}