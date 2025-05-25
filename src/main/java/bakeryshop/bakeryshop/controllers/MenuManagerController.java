package bakeryshop.bakeryshop.controllers;

import bakeryshop.bakeryshop.models.User;
import bakeryshop.bakeryshop.services.IServiceCake;
import bakeryshop.bakeryshop.services.IServiceOrder;
import bakeryshop.bakeryshop.services.IServiceOrderItem;
import bakeryshop.bakeryshop.services.IServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuManagerController {

    private User currentUser;
    private IServiceUser serviceUser;
    private IServiceCake serviceCake;
    private IServiceOrderItem serviceOrderItem;
    private IServiceOrder serviceOrder;

    public void setServiceOrderItem(IServiceOrderItem serviceOrderItem) {
        this.serviceOrderItem = serviceOrderItem;
    }

    public void setServiceOrder(IServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public void setServiceCake(IServiceCake serviceCake) {
        this.serviceCake = serviceCake;
    }

    public void setServiceUser(IServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    public User getCurrentUser() {
        return currentUser;
    }

    @FXML
    public void handleManageClients(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/manageclients-view.fxml"));
            Scene scene = new Scene(loader.load());
            ManageClientsController controller = loader.getController();
            controller.setServiceUser(serviceUser);
            controller.setServiceCake(serviceCake);
            controller.setServiceOrder(serviceOrder);
            controller.setServiceOrderItem(serviceOrderItem);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setWidth(400);
            stage.setHeight(750);
            stage.setTitle("Manage Clients");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleManageProducts(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/manageproducts-view.fxml"));
            Scene scene = new Scene(loader.load());
            ManageProductsController controller = loader.getController();
            controller.setServiceUser(serviceUser);
            controller.setServiceCake(serviceCake);
            controller.setServiceOrder(serviceOrder);
            controller.setServiceOrderItem(serviceOrderItem);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setWidth(900);
            stage.setHeight(800);
            stage.setTitle("Manage Products");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleManageOrders(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/manageorders-view.fxml"));
            Scene scene = new Scene(loader.load());
            ManageOrdersController controller = loader.getController();
            controller.setServices(serviceOrder,serviceOrderItem,serviceCake,serviceUser);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setWidth(900);
            stage.setHeight(800);
            stage.setTitle("Manage Products");
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
