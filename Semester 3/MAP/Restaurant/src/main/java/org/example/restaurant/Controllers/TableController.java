package org.example.restaurant.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.example.restaurant.Services.Service;
import org.example.restaurant.models.MenuItem;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TableController {
    private Service service;
    private int tableId;

    @FXML
    private VBox menuContainer;

    public void setServiceAndTable(Service service, int tableId) {
        this.service = service;
        this.tableId = tableId;
        loadMenu();
    }

    private void loadMenu() {
        Map<String, List<MenuItem>> groupedMenu = service.groupMenuByCategory(service.getMenuItems());
        for (String category : groupedMenu.keySet()) {
            Label categoryLabel = new Label(category);
            menuContainer.getChildren().add(categoryLabel);

            for (MenuItem item : groupedMenu.get(category)) {
                CheckBox itemCheckBox = new CheckBox(item.getItem() + " - " + item.getPrice() + " " + item.getCurrency());
                itemCheckBox.setUserData(item);
                menuContainer.getChildren().add(itemCheckBox);
            }
        }
    }

    @FXML
    private void handlePlaceOrder() {
        List<MenuItem> selectedItems = menuContainer.getChildren().stream()
                .filter(node -> node instanceof CheckBox)
                .map(node -> (CheckBox) node)
                .filter(CheckBox::isSelected)
                .map(cb -> (MenuItem) cb.getUserData())
                .collect(Collectors.toList());

        service.placeOrder(tableId, selectedItems);
    }
}
