module org.example.taximetrie {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.taximetrie to javafx.fxml;
    exports org.example.taximetrie;
    exports org.example.taximetrie.controllers;
    opens org.example.taximetrie.controllers to javafx.fxml;
}