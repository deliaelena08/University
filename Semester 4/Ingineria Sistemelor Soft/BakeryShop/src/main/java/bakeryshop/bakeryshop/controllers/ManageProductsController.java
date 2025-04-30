package bakeryshop.bakeryshop.controllers;
import bakeryshop.bakeryshop.models.Cake;
import bakeryshop.bakeryshop.repos.IRepoCake;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageProductsController {

    @FXML private TableView<Cake> tableViewProducts;
    @FXML private TableColumn<Cake, Integer> columnId;
    @FXML private TableColumn<Cake, String> columnName;
    @FXML private TableColumn<Cake, Integer> columnPrice;
    @FXML private TableColumn<Cake, String> columnIngredients;
    @FXML private TableColumn<Cake, String> columnQuantity;

    @FXML private Button submitButton;
    @FXML private Button backButton;

    private IServiceCake serviceCake;
    private IServiceUser serviceUser;
    private IServiceOrder serviceOrder;
    private IServiceOrderItem serviceOrderItem;
    private ObservableList<Cake> cakeList;

    public void setServiceOrderItem(IServiceOrderItem serviceOrderItem) {
        this.serviceOrderItem = serviceOrderItem;
    }

    public void setServiceOrder(IServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public void setServiceCake(IServiceCake serviceCake) {
        this.serviceCake = serviceCake;
        loadProducts();
    }
    public void setServiceUser(IServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }

    private void loadProducts() {
        cakeList = FXCollections.observableArrayList(serviceCake.getAll());
        tableViewProducts.setItems(cakeList);
    }

    @FXML
    public void initialize() {
        tableViewProducts.setEditable(true);

        columnId.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));
        columnName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        columnName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnName.setOnEditCommit(event -> event.getRowValue().setName(event.getNewValue()));

        columnPrice.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPrice()));
        columnPrice.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.IntegerStringConverter()));
        columnPrice.setOnEditCommit(event -> event.getRowValue().setPrice(event.getNewValue()));

        columnIngredients.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIngredients()));
        columnIngredients.setCellFactory(TextFieldTableCell.forTableColumn());
        columnIngredients.setOnEditCommit(event -> event.getRowValue().setIngredients(event.getNewValue()));

        columnQuantity.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getQuantity()));
        columnQuantity.setCellFactory(TextFieldTableCell.forTableColumn());
        columnQuantity.setOnEditCommit(event -> event.getRowValue().setQuantity(event.getNewValue()));
    }

    @FXML
    public void handleSubmit() {
        for (Cake c : cakeList) {
            serviceCake.update(c);
        }
        loadProducts();
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/menumanager-view.fxml"));
            Scene scene = new Scene(loader.load());
            MenuManagerController controller = loader.getController();
            controller.setServiceCake(serviceCake);
            controller.setServiceUser(serviceUser);
            controller.setServiceOrder(serviceOrder);
            controller.setServiceOrderItem(serviceOrderItem);
            Stage stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage1.setScene(scene);
            stage1.setWidth(400);
            stage1.setHeight(600);
            stage1.setTitle("Menu Manager");
            stage1.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
