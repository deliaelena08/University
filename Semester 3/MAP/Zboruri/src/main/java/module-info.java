module org.example.zboruri {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.zboruri to javafx.fxml;
    exports org.example.zboruri;
    exports org.example.zboruri.controllers;
    opens org.example.zboruri.controllers to javafx.fxml;
}