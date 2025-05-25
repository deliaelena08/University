package bakeryshop.bakeryshop.controllers;

import bakeryshop.bakeryshop.models.User;
import bakeryshop.bakeryshop.models.UserType;
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
import java.util.stream.Collectors;

public class ManageClientsController {
    @FXML private TableView<User> tableViewClients;
    @FXML private TableColumn<User, Integer> columnId;
    @FXML private TableColumn<User, String> columnUsername;
    @FXML private TableColumn<User, String> columnPassword;

    @FXML private Button submitButton;
    @FXML private Button backButton;

    private IServiceUser serviceUser;
    private IServiceCake serviceCake;
    private IServiceOrder serviceOrder;
    private IServiceOrderItem serviceOrderItem;
    private ObservableList<User> clientList;

    public void setServiceOrderItem(IServiceOrderItem serviceOrderItem) {
        this.serviceOrderItem = serviceOrderItem;
    }

    public void setServiceOrder(IServiceOrder serviceOrder) {
        this.serviceOrder = serviceOrder;
    }

    public void setServiceUser(IServiceUser serviceUser) {
        this.serviceUser = serviceUser;
        loadClients();
    }

    public void setServiceCake(IServiceCake serviceCake) {
        this.serviceCake = serviceCake;
    }

    private void loadClients() {
        clientList = FXCollections.observableArrayList(
                serviceUser.getAll().stream()
                        .filter(u -> u.getType()== UserType.client)
                        .collect(Collectors.toList())
        );
        tableViewClients.setItems(clientList);
    }

    @FXML
    public void initialize() {
        tableViewClients.setEditable(true);

        columnId.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getIdUser()));
        columnUsername.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUsername()));
        columnUsername.setCellFactory(TextFieldTableCell.forTableColumn());
        columnUsername.setOnEditCommit(event -> event.getRowValue().setUsername(event.getNewValue()));

        columnPassword.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPassword()));
        columnPassword.setCellFactory(TextFieldTableCell.forTableColumn());
        columnPassword.setOnEditCommit(event -> event.getRowValue().setPassword(event.getNewValue()));
    }

    @FXML
    public void handleSubmit() {
        for (User u : clientList) {
            serviceUser.update(u); // Ai nevoie de metoda `update` în `IServiceUser`
        }
        loadClients(); // reîncarcă tabela
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/menumanager-view.fxml"));
            Scene scene = new Scene(loader.load());
            MenuManagerController controller = loader.getController();
            controller.setServiceUser(serviceUser);
            controller.setServiceCake(serviceCake);
            controller.setServiceOrder(serviceOrder);
            controller.setServiceOrderItem(serviceOrderItem);
            Stage stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage1.setScene(scene);
            stage1.setTitle("Menu Manager");
            stage1.setWidth(400);
            stage1.setHeight(600);
            stage1.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
