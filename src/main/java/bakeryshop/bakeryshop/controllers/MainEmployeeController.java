package bakeryshop.bakeryshop.controllers;

import bakeryshop.bakeryshop.models.*;
import bakeryshop.bakeryshop.services.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.LocalDateTime;
import java.util.List;
public class MainEmployeeController {

    @FXML private TableView<EmployeeOrder> ordersTable;
    @FXML private TableColumn<EmployeeOrder, String> cakeNameColumn;
    @FXML private TableColumn<EmployeeOrder, Integer> quantityColumn;
    @FXML private TableColumn<EmployeeOrder, Integer> priceColumn;
    @FXML private TableColumn<EmployeeOrder, String> orderDateColumn;
    @FXML private TableColumn<EmployeeOrder, String> statusColumn;
    @FXML private TableColumn<EmployeeOrder, Void> actionColumn;

    private User loggedEmployee;
    private IServiceOrderStatus serviceOrderStatus;
    private IServiceOrder serviceOrder;
    private IServiceEmployeeOrder serviceEmployeeOrder;
    private IServiceOrderItem serviceOrderItem;
    private IServiceStatus serviceStatus;
    private ObservableList<EmployeeOrder> orders = FXCollections.observableArrayList();

    public void setServices(IServiceEmployeeOrder serviceEmployeeOrder,
                            IServiceOrderItem serviceOrderItem,
                            IServiceOrderStatus serviceOrderStatus,
                            IServiceOrder serviceOrder,
                            IServiceStatus serviceStatus) {
        this.serviceEmployeeOrder = serviceEmployeeOrder;
        this.serviceOrderItem = serviceOrderItem;
        this.serviceOrderStatus = serviceOrderStatus;
        this.serviceOrder = serviceOrder;
        this.serviceStatus = serviceStatus;
    }

    public void setLoggedEmployee(User user) {
        this.loggedEmployee = user;
        loadAllOrders(); // încarcă TOATE comenzile, nu doar cele ale angajatului
    }

    private void loadAllOrders() {
        orders.clear();
        orders.addAll(serviceEmployeeOrder.getAll());
        ordersTable.setItems(orders);
    }

    @FXML
    public void initialize() {
        cakeNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCake().getName()));

        quantityColumn.setCellValueFactory(cellData -> {
            int orderId = cellData.getValue().getOrder().getId();
            int cakeId = cellData.getValue().getCake().getId();

            int quantity = serviceOrderItem.getAll().stream()
                    .filter(item -> item.getOrder().getId() == orderId && item.getCake().getId() == cakeId)
                    .mapToInt(OrderItem::getQuantity)
                    .findFirst()
                    .orElse(0);

            return new SimpleIntegerProperty(quantity).asObject();
        });

        priceColumn.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCake().getPrice()).asObject());

        orderDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOrder().getOrderDate()));

        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(serviceOrderStatus
                        .getById(cellData.getValue().getOrder().getId())
                        .getStatus().getOrderStatus().name()));

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button acceptBtn = new Button("Accept");
            private final Button finishBtn = new Button("Finish");
            private final Button rejectBtn = new Button("Reject");
            private final HBox pane = new HBox(5, acceptBtn, finishBtn, rejectBtn);

            {
                acceptBtn.setOnAction(event -> handleStatusChange(getTableView().getItems().get(getIndex()), OrderStatus.accepted));
                finishBtn.setOnAction(event -> handleStatusChange(getTableView().getItems().get(getIndex()), OrderStatus.delivered));
                rejectBtn.setOnAction(event -> handleStatusChange(getTableView().getItems().get(getIndex()), OrderStatus.rejected));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void handleStatusChange(EmployeeOrder employeeOrder, OrderStatus newStatus) {
        Order order = employeeOrder.getOrder();
        Status existingStatus = serviceStatus.getByOrderStatus(newStatus);

        Status newStatusObj;
        if (existingStatus != null) {
            newStatusObj = existingStatus;
            newStatusObj.setDescription("Status changed to " + newStatus.name());
            serviceStatus.update(newStatusObj);
        } else {
            newStatusObj = new Status();
            newStatusObj.setOrderStatus(newStatus);
            newStatusObj.setDescription("Status changed to " + newStatus.name());
            newStatusObj = serviceStatus.save(newStatusObj);
        }
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(order);
        history.setStatus(newStatusObj);
        history.setChangeDate(LocalDateTime.now());
        serviceOrderStatus.save(history);

        loadAllOrders();
    }


}
