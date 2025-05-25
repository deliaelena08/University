module org.example.ofertedevacanta {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.compiler;


    opens org.example.ofertedevacanta to javafx.fxml;
    exports org.example.ofertedevacanta;
}