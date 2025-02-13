module org.example.restaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.restaurant to javafx.fxml;
    exports org.example.restaurant;
    exports org.example.restaurant.Controllers;
    opens org.example.restaurant.Controllers to javafx.fxml;
}