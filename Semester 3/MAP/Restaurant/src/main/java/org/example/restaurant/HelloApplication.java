package org.example.restaurant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.restaurant.Controllers.StaffController;
import org.example.restaurant.Repos.MenuItemRepository;
import org.example.restaurant.Repos.OrderItemsRepository;
import org.example.restaurant.Repos.OrderRepository;
import org.example.restaurant.Repos.TableRepository;
import org.example.restaurant.Services.Service;
import org.example.restaurant.Validators.MenuItemValidator;
import org.example.restaurant.Validators.OrderValidator;
import org.example.restaurant.Validators.TableValidator;
import org.example.restaurant.Validators.Validator;
import org.example.restaurant.models.MenuItem;
import org.example.restaurant.models.Order;
import org.example.restaurant.models.Table;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String username="postgres";
        String password="Delia.08";
        String url="jdbc:postgresql://localhost:5432/Restaurant";
        Validator<Table> tableValidator = (Validator<Table>) new TableValidator();
        Validator<MenuItem> menuItemValidator = (Validator<MenuItem>) new MenuItemValidator();
        Validator<Order> orderValidator = (Validator<Order>) new OrderValidator();
        TableRepository tableDB=new TableRepository(tableValidator,url,username,password);
        MenuItemRepository menuItemDB=new MenuItemRepository(menuItemValidator,url,username,password);
        OrderRepository orderDB=new OrderRepository(orderValidator,url,username,password);
        OrderItemsRepository orderItemsDB=new OrderItemsRepository(url,username,password);
        Service service = new Service(tableDB,menuItemDB,orderDB,orderItemsDB);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/restaurant/views/staff-view.fxml"));
        Scene scene = new Scene(loader.load());
        StaffController controller = loader.getController();
        controller.setService(service);
        stage.setTitle("Staff");
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}