package bakeryshop.bakeryshop.controllers;

import bakeryshop.bakeryshop.models.User;
import bakeryshop.bakeryshop.services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class MenuClientController {
    private User currentUser;
    private IServiceUser serviceUser;
    private IServiceOrder serviceOrder;
    private IServiceCake serviceCake;
    private IServiceOrderItem serviceOrderItem;

    public void setServiceOrderItem(IServiceOrderItem serviceOrderItem) {
        this.serviceOrderItem = serviceOrderItem;
    }

    public void setServiceCake(IServiceCake serviceCake) {
        this.serviceCake = serviceCake;
    }

    public void setServiceOrder(IServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public void setServiceUser(IServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @FXML
    public void handlePlaceOrder(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/shop-view.fxml"));
            Scene scene = new Scene(loader.load());
            ShopViewController controller = loader.getController();
            controller.setService(serviceCake,serviceUser,serviceOrder,serviceOrderItem,currentUser);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Shopping Menu");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleHistory(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/clientorders-view.fxml"));
            Scene scene = new Scene(loader.load());
            ClientOrdersController controller = loader.getController();
            controller.setService(serviceOrder,serviceOrderItem,serviceUser,serviceCake,currentUser);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("History");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSettings(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/accountsettings-view.fxml"));
            Scene scene = new Scene(loader.load());
            AccountSettingsController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            controller.setServiceUser(serviceUser);
            controller.setServiceOrder(serviceOrder);
            controller.setServiceCake(serviceCake);
            controller.setServiceOrderItem(serviceOrderItem);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Account Settings");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/login-view.fxml"));
            Scene scene = new Scene(loader.load());
            LoginController controller = loader.getController();
            controller.setCurrentUser(null);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
