package bakeryshop.bakeryshop.controllers;

import bakeryshop.bakeryshop.models.Order;
import bakeryshop.bakeryshop.models.User;
import bakeryshop.bakeryshop.services.IServiceCake;
import bakeryshop.bakeryshop.services.IServiceOrder;
import bakeryshop.bakeryshop.services.IServiceOrderItem;
import bakeryshop.bakeryshop.services.IServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ClientOrdersController {

    @FXML private TableView<Order> tableClientOrders;
    @FXML private TableColumn<Order, Integer> columnOrderId;
    @FXML private TableColumn<Order, String> columnOrderDate;
    @FXML private TableColumn<Order, Double> columnTotalPrice;
    @FXML private TableColumn<Order, String> columnStatus;

    private IServiceCake serviceCake;
    private IServiceUser serviceUser;
    private IServiceOrder serviceOrder;
    private IServiceOrderItem serviceOrderItem;
    private User currentUser;
    private ObservableList<Order> clientOrdersList;

    public void setService(IServiceOrder serviceOrder,IServiceOrderItem orderItem,IServiceUser serviceUser,IServiceCake serviceCake, User currentUser) {
        this.serviceOrder = serviceOrder;
        this.serviceOrderItem = orderItem;
        this.serviceUser = serviceUser;
        this.serviceCake = serviceCake;
        this.currentUser = currentUser;
        loadClientOrders();
    }

    @FXML
    public void initialize() {
        columnOrderId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        columnTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadClientOrders() {
        List<Order> allOrders = serviceOrder.getAll();
        List<Order> clientOrders = allOrders.stream()
                .filter(order -> order.getIdClient() == currentUser.getIdUser())
                .collect(Collectors.toList());

        clientOrdersList = FXCollections.observableArrayList(clientOrders);
        tableClientOrders.setItems(clientOrdersList);
    }
    @FXML
    public void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/menuclient-view.fxml"));
            Scene scene = new Scene(loader.load());
            MenuClientController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            controller.setServiceOrder(serviceOrder);
            controller.setServiceCake(serviceCake);
            controller.setServiceUser(serviceUser);
            controller.setServiceOrderItem(serviceOrderItem);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Menu Client");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

