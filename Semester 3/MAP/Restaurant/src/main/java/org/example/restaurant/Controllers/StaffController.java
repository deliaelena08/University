package org.example.restaurant.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.restaurant.Services.Service;
import org.example.restaurant.models.MenuItem;
import org.example.restaurant.models.Order;
import org.example.restaurant.models.Status;
import org.example.restaurant.models.Table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StaffController {
    private Service service;
    private final Map<Integer, Stage> tableWindows = new HashMap<>();
    @FXML
    private TableView<Order> placedOrdersTable;
    @FXML
    private TableColumn<Order, Integer> placedTableIdColumn;
    @FXML
    private TableColumn<Order, String> placedDateColumn;
    @FXML
    private TableColumn<Order, String> placedItemsColumn;

    @FXML
    private TableView<Order> preparingOrdersTable;
    @FXML
    private TableColumn<Order, Integer> preparingTableIdColumn;
    @FXML
    private TableColumn<Order, String> preparingDateColumn;
    @FXML
    private TableColumn<Order, String> preparingItemsColumn;

    public void setService(Service service) {
        this.service = service;
        service.setOnOrderPlacedCallback(this::loadOrders);
        service.setOnOrderStatusChangedCallback(this::handleOrderStatusChanged);

        loadTables();
        loadOrders();
    }

    private void loadTables() {
        for (Table table : service.getTables()) {
            openTableWindow(table.getId());
        }
    }

    private void openTableWindow(int tableId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/restaurant/views/table-view.fxml"));
            Scene scene = new Scene(loader.load());

            TableController controller = loader.getController();
            controller.setServiceAndTable(service, tableId);

            Stage tableStage = new Stage();
            tableStage.setTitle("Table " + tableId);
            tableStage.setScene(scene);
            tableStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void initialize() {
        // Configurare pentru tabela "Placed Orders"
        placedTableIdColumn.setCellValueFactory(new PropertyValueFactory<>("tableId"));
        placedDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate().toString()));
        placedItemsColumn.setCellValueFactory(cellData -> {
            List<MenuItem> items = service.getMenuItemsForOrder(cellData.getValue().getId());
            String itemsString = items.stream().map(MenuItem::getItem).collect(Collectors.joining(", "));
            return new SimpleStringProperty(itemsString);
        });

        // Configurare pentru tabela "Preparing Orders"
        preparingTableIdColumn.setCellValueFactory(new PropertyValueFactory<>("tableId"));
        preparingDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate().toString()));
        preparingItemsColumn.setCellValueFactory(cellData -> {
            List<MenuItem> items = service.getMenuItemsForOrder(cellData.getValue().getId());
            String itemsString = items.stream().map(MenuItem::getItem).collect(Collectors.joining(", "));
            return new SimpleStringProperty(itemsString);
        });
    }

    @FXML
    private void loadOrders() {
        // Preia comenzile pentru fiecare status
        List<Order> placedOrders = service.getPlacedOrders().stream()
                .filter(order -> order.getStatus() == Status.PLACED)
                .collect(Collectors.toList());

        List<Order> preparingOrders = service.getPreparingOrders();

        placedOrdersTable.getItems().setAll(placedOrders);
        preparingOrdersTable.getItems().setAll(preparingOrders);
    }
    private void handleOrderStatusChanged(Order order) {
        if (order.getStatus() == Status.PREPARING) {
            showNotification(order.getTableId(), "Your order is being prepared");
        } else if (order.getStatus() == Status.DELIVERED) {
            showNotification(order.getTableId(), "Your order has been delivered to your table");
        }
        loadOrders();
    }
    private void showNotification(int tableId, String message) {
        Stage tableStage = tableWindows.get(tableId);
        if (tableStage != null) {
            Label label = new Label(message);
            Scene scene = new Scene(label, 300, 100);
            tableStage.setScene(scene);
        }
    }
    @FXML
    private void markAsPreparing() {
        Order selectedOrder = placedOrdersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null && selectedOrder.getStatus() == Status.PLACED) {
            service.updateOrderStatus(selectedOrder.getId(), Status.PREPARING);
            loadOrders();
        }
    }

    @FXML
    private void markAsDelivered() {
        Order selectedOrder = preparingOrdersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null && selectedOrder.getStatus() == Status.PREPARING) {
            service.updateOrderStatus(selectedOrder.getId(), Status.DELIVERED);
            loadOrders();
        }
    }
}
