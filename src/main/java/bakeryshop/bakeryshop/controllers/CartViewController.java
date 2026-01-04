package bakeryshop.bakeryshop.controllers;

import bakeryshop.bakeryshop.models.Cake;
import bakeryshop.bakeryshop.models.User;
import bakeryshop.bakeryshop.services.IServiceCake;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class CartViewController {

    @FXML
    private VBox cartListVBox;
    private Label cartLabel;
    @FXML
    private Button placeOrderButton;

    private ObservableList<Cake> cart = FXCollections.observableArrayList();
    private IServiceCake serviceCake;
    private User currentUser;

    public void setData(ObservableList<Cake> cart, IServiceCake serviceCake, User user) {
        this.cart = cart;
        this.serviceCake = serviceCake;
        this.currentUser = user;
        displayCartItems();

        cart.addListener((ListChangeListener<Cake>) change -> displayCartItems());
    }

    private void displayCartItems() {
        cartListVBox.getChildren().clear();
        for (Cake cake : cart) {
            Label label = new Label(cake.getName() + " - " + cake.getPrice() + " RON");
            cartListVBox.getChildren().add(label);
        }
    }


    public void setCartLabel(Label cartLabel) {
        this.cartLabel = cartLabel;
    }

    @FXML
    private void handlePlaceOrder() {
        // TODO: salvează comanda în viitor
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Order placed!");
        alert.setContentText("Your delicious cakes are on the way!");
        alert.showAndWait();
        cart.clear();
        displayCartItems();

        if (cartLabel != null) {
            cartLabel.setText("🛒 0 items");
        }

    }
}

