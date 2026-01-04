module bakeryshop.bakeryshop {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.persistence;

    opens bakeryshop.bakeryshop to javafx.fxml;
    opens bakeryshop.bakeryshop.models to org.hibernate.orm.core;
    exports bakeryshop.bakeryshop;
}