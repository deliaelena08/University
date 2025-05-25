package bakeryshop.bakeryshop.controllers;

import bakeryshop.bakeryshop.models.User;
import bakeryshop.bakeryshop.models.UserType;
import bakeryshop.bakeryshop.services.IServiceUser;
import bakeryshop.bakeryshop.services.ServiceUser;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SignupController {

    @FXML
    private TextField fullnameField, emailField;
    @FXML
    private PasswordField passwordField, confirmPasswordField;
    private IServiceUser serviceUser;

    @FXML
    public void handleSignup() {
        String name = fullnameField.getText();
        String email = emailField.getText();
        String pass = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (!pass.equals(confirm)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare");
            alert.setHeaderText(null);
            alert.setContentText("Parolele nu coincid!");
            alert.showAndWait();
            return;
        }

        User user = new User();
        user.setUsername(email);
        user.setPassword(pass);
        user.setType(UserType.client);
        serviceUser.save(user);

        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Inregistrare reusita pentru "+name);
        success.setHeaderText(null);
        success.setContentText("Cont creat cu succes!");
        success.showAndWait();

        fullnameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }


    public void setServiceUser(IServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }

    @FXML
    public void cancelSignup() {
        ((Stage) fullnameField.getScene().getWindow()).close();
    }
}
