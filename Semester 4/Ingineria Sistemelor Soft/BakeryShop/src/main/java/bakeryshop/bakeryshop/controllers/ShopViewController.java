package bakeryshop.bakeryshop.controllers;

import bakeryshop.bakeryshop.models.Cake;
import bakeryshop.bakeryshop.models.User;
import bakeryshop.bakeryshop.services.*;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShopViewController {

    @FXML
    private VBox cakeListVBox;
    @FXML
    private Label cartLabel;
    private IServiceCake serviceCake;
    private IServiceUser  serviceUser;
    private IServiceOrder serviceOrder;
    private IServiceOrderItem serviceOrderItem;
    private User currentUser;

    private final ObservableList<Cake> cart = FXCollections.observableArrayList();

    @FXML
    public void handleViewCart(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/cart-view.fxml"));
            Scene scene = new Scene(loader.load());
            CartViewController controller = loader.getController();
            controller.setData(cart, serviceCake, currentUser);
            controller.setCartLabel(cartLabel);
            Stage stage = new Stage();
            stage.setTitle("Your Cart");
            stage.setWidth(200);
            stage.setHeight(300);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setService(IServiceCake serviceCake, IServiceUser serviceUser, IServiceOrder serviceOrder, IServiceOrderItem serviceOrderItem, User user) {
        this.serviceCake = serviceCake;
        this.serviceUser = serviceUser;
        this.serviceOrder = serviceOrder;
        this.serviceOrderItem = serviceOrderItem;
        this.currentUser = user;
        loadCakes();
    }

    private void loadCakes() {
        cakeListVBox.getChildren().clear();
        for (Cake cake : serviceCake.getAll()) {
            VBox box = new VBox(5);
            box.setPadding(new Insets(10));
            box.setStyle("-fx-border-color: #000; -fx-background-radius: 10; -fx-border-radius: 10; -fx-background-color: linear-gradient(to right, #fff0c1, #fdd1f9);");

            // Imagine
            Image image;
            try {
                image = new Image(getClass().getResource("/bakeryshop/bakeryshop/images/cake_id" + String.valueOf(cake.getId()) + ".jpg").toExternalForm());
            } catch (Exception e) {
                image = new Image(Objects.requireNonNull(getClass().getResource("/bakeryshop/bakeryshop/images/default.jpg")).toExternalForm());
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);

            Label nameLabel = new Label(cake.getName());
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            Label descriptionLabel = new Label(cake.getIngredients());
            descriptionLabel.setWrapText(true);

            Label priceLabel = new Label(cake.getPrice() + " RON");
            priceLabel.setStyle("-fx-text-fill: green;");

            Button addButton = new Button("Add to cart");
            addButton.setOnAction(e -> {
                cart.add(cake);
                updateCartLabel();
            });

            box.getChildren().addAll(nameLabel, descriptionLabel, imageView, priceLabel, addButton);
            cakeListVBox.getChildren().add(box);
        }
    }

    private void updateCartLabel() {
        cartLabel.setText("🛒 " + cart.size() + " items");
    }

    public void setCartLabel(Label cartLabel) {
        this.cartLabel = cartLabel;
    }
    @FXML
    public void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/menuclient-view.fxml"));
            Scene scene = new Scene(loader.load());
            MenuClientController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            controller.setServiceUser(serviceUser);
            controller.setServiceOrder(serviceOrder);
            controller.setServiceOrderItem(serviceOrderItem);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("MenuClient");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
