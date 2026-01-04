
module com.example.seminar6 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.seminar6 to javafx.fxml;
    exports com.example.seminar6;
}