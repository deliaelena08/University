package bakeryshop.bakeryshop.controllers;

import bakeryshop.bakeryshop.models.Cake;
import bakeryshop.bakeryshop.models.Order;
import bakeryshop.bakeryshop.models.OrderItem;
import bakeryshop.bakeryshop.services.IServiceCake;
import bakeryshop.bakeryshop.services.IServiceOrder;
import bakeryshop.bakeryshop.services.IServiceOrderItem;
import bakeryshop.bakeryshop.services.IServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ManageOrdersController {

    @FXML private TableView<Order> tableOrders;
    @FXML private TableColumn<Order, Integer> columnOrderId;
    @FXML private TableColumn<Order, Integer> columnClientId;
    @FXML private TableColumn<Order, String> columnOrderDate;
    @FXML private TableColumn<Order, Double> columnTotalPrice;

    @FXML private TableView<OrderItemView> tableOrderItems;
    @FXML private TableColumn<OrderItemView, String> columnCakeName;
    @FXML private TableColumn<OrderItemView, Integer> columnQuantity;

    @FXML private Button backButton;

    private IServiceOrder serviceOrder;
    private IServiceOrderItem serviceOrderItem;
    private IServiceCake serviceCake;
    private IServiceUser serviceUser;
    private ObservableList<Order> orderList;
    private ObservableList<OrderItemView> orderItemViewList;

    public void setServices(IServiceOrder serviceOrder, IServiceOrderItem serviceOrderItem, IServiceCake serviceCake,IServiceUser serviceUser) {
        this.serviceOrder = serviceOrder;
        this.serviceOrderItem = serviceOrderItem;
        this.serviceCake = serviceCake;
        this.serviceUser = serviceUser;
        loadOrders();
    }

    private void loadOrders() {
        orderList = FXCollections.observableArrayList(serviceOrder.getAll());
        tableOrders.setItems(orderList);
    }

    @FXML
    public void initialize() {
        // Orders table
        columnOrderId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnClientId.setCellValueFactory(new PropertyValueFactory<>("idClient"));
        columnOrderDate.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        columnTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        // Items table
        columnCakeName.setCellValueFactory(new PropertyValueFactory<>("cakeName"));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        tableOrders.setRowFactory(tv -> {
            TableRow<Order> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Order selectedOrder = row.getItem();
                    loadOrderItems(selectedOrder.getId());
                }
            });
            return row;
        });

    }

    private void loadOrderItems(int orderId) {
        List<OrderItem> items = serviceOrderItem.findByOrderId(orderId);
        Map<Integer, String> cakeNames = serviceCake.getAll().stream()
                .collect(Collectors.toMap(Cake::getId, Cake::getName));

        orderItemViewList = FXCollections.observableArrayList(
                items.stream()
                        .map(oi -> new OrderItemView(cakeNames.get(oi.getCake().getId()), oi.getQuantity()))
                        .collect(Collectors.toList())
        );
        tableOrderItems.setItems(orderItemViewList);
        System.out.println("Order ID selectat: " + orderId);
        System.out.println("OrderItems găsite: " + items.size());

    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/menumanager-view.fxml"));
            Scene scene = new Scene(loader.load());
            MenuManagerController controller = loader.getController();
            controller.setServiceOrder(serviceOrder);
            controller.setServiceOrderItem(serviceOrderItem);
            controller.setServiceCake(serviceCake);
            controller.setServiceUser(serviceUser);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Menu Manager");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class OrderItemView {
        private final String cakeName;
        private final int quantity;

        public OrderItemView(String cakeName, int quantity) {
            this.cakeName = cakeName;
            this.quantity = quantity;
        }

        public String getCakeName() {
            return cakeName;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
