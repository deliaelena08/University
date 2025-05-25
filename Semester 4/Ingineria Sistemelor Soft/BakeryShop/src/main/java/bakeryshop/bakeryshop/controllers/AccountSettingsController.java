package bakeryshop.bakeryshop.controllers;

import bakeryshop.bakeryshop.models.User;
import bakeryshop.bakeryshop.models.UserType;
import bakeryshop.bakeryshop.services.IServiceCake;
import bakeryshop.bakeryshop.services.IServiceOrder;
import bakeryshop.bakeryshop.services.IServiceOrderItem;
import bakeryshop.bakeryshop.services.IServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AccountSettingsController {

    private User currentUser;
    private IServiceCake serviceCake;
    private IServiceUser  serviceUser;
    private IServiceOrder serviceOrder;
    private IServiceOrderItem serviceOrderItem;

    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;

    @FXML
    public void initialize() {
        emailField.setEditable(false);
        passwordField.setEditable(false);
    }

    public void setServiceUser(IServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        emailField.setText(currentUser.getUsername());
        emailField.requestFocus();
        passwordField.setText(currentUser.getPassword());
        passwordField.requestFocus();
    }

    public void setServiceOrder(IServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public void setServiceOrderItem(IServiceOrderItem serviceOrderItem) {
        this.serviceOrderItem = serviceOrderItem;
    }

    public void setServiceCake(IServiceCake serviceCake) {
        this.serviceCake = serviceCake;
    }

    @FXML
    public void handleEditCredentials(ActionEvent event) {
        emailField.setEditable(true);
        passwordField.setEditable(true);
    }

    @FXML
    public void handleSetCredentials(ActionEvent event) {
        if(emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Email and password are required");
            alert.showAndWait();
        }
        else {
            User user = new User();
            user.setUsername(emailField.getText());
            user.setPassword(passwordField.getText());
            user.setType(UserType.client);
            user.setIdUser(currentUser.getIdUser());
            currentUser=serviceUser.update(user);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Successfully updated");
            alert.showAndWait();
            emailField.setEditable(false);
            passwordField.setEditable(false);
        }
    }

    @FXML
    public void handleDeleteAccount(ActionEvent event) {
        try{
            serviceUser.delete(currentUser.getIdUser());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Successfully deleted. You are now logged out");
            alert.showAndWait();
            setCurrentUser(null);

            FXMLLoader loadermenu = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/menuclient-view.fxml"));
            MenuClientController controllermenu = loadermenu.getController();
            controllermenu.setCurrentUser(null);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/login-view.fxml"));
            Scene scene = new Scene(loader.load());
            LoginController controller = loader.getController();
            controller.setCurrentUser(null);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/menuclient-view.fxml"));
            Scene scene = new Scene(loader.load());
            MenuClientController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            controller.setServiceUser(serviceUser);
            controller.setServiceOrderItem(serviceOrderItem);
            controller.setServiceCake(serviceCake);
            controller.setServiceOrder(serviceOrder);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("MenuClient");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
