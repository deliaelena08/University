package bakeryshop.bakeryshop.controllers;

import bakeryshop.bakeryshop.models.User;
import bakeryshop.bakeryshop.models.UserType;
import bakeryshop.bakeryshop.services.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.Console;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    private IServiceUser userServiceDB;
    private IServiceCake serviceCakeDB;
    private IServiceOrder serviceOrderDB;
    private IServiceOrderItem serviceOrderItemDB;
    private IServiceEmployeeOrder serviceEmployeeOrderDB;
    private IServiceStatus serviceStatusDB;
    private IServiceOrderStatus serviceOrderStatusDB;
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            User user = userServiceDB.login(username, password);
            if (user != null) {
                System.out.println("ID-ul utilizatorului logat: " + user.getIdUser());
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Login cu succes!");
                success.setHeaderText(null);
                success.setContentText("Bine ati revenit "+user.getType()+" "+user.getUsername()+"!");
                success.showAndWait();
                setCurrentUser(user);
                if(user.getType()== UserType.client){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/menuclient-view.fxml"));
                    Scene scene = new Scene(loader.load());
                    MenuClientController controller = loader.getController();
                    controller.setCurrentUser(user);
                    controller.setServiceOrder(serviceOrderDB);
                    controller.setServiceUser(userServiceDB);
                    controller.setServiceCake(serviceCakeDB);
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setWidth(400);
                    stage.setHeight(600);
                    stage.setTitle("Client Menu");
                } else if (user.getType()==UserType.manager) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/menumanager-view.fxml"));
                    Scene scene = new Scene(loader.load());
                    MenuManagerController controller = loader.getController();
                    controller.setCurrentUser(user);
                    controller.setServiceCake(serviceCakeDB);
                    controller.setServiceUser(userServiceDB);
                    controller.setServiceOrderItem(serviceOrderItemDB);
                    controller.setServiceOrder(serviceOrderDB);
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setWidth(400);
                    stage.setHeight(600);
                    stage.setTitle("Manager Menu");
                }else{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/mainemployee-view.fxml"));
                    Scene scene = new Scene(loader.load());
                    MainEmployeeController controller = loader.getController();
                    controller.setServices(serviceEmployeeOrderDB,serviceOrderItemDB,serviceOrderStatusDB,serviceOrderDB,serviceStatusDB);
                    controller.setLoggedEmployee(user);
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setWidth(900);
                    stage.setHeight(600);
                    stage.setTitle("Main Page");
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Eroare");
                alert.setHeaderText(null);
                alert.setContentText("Utilizator inexistent sau parola gresita.");
                alert.showAndWait();
            }
        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    public void setServices(IServiceUser serviceUser,IServiceCake serviceCake,IServiceOrder serviceOrder,IServiceEmployeeOrder serviceEmployeeOrder,IServiceStatus serviceStatus,IServiceOrderStatus serviceOrderStatus,IServiceOrderItem serviceOrderItem) {
        this.userServiceDB=serviceUser;
        this.serviceCakeDB=serviceCake;
        this.serviceOrderDB=serviceOrder;
        this.serviceEmployeeOrderDB=serviceEmployeeOrder;
        this.serviceStatusDB=serviceStatus;
        this.serviceOrderStatusDB=serviceOrderStatus;
        this.serviceOrderItemDB=serviceOrderItem;
    }

    @FXML
    public void openSignupWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/signup-view.fxml"));
            Parent root = loader.load();
            SignupController controller = loader.getController();
            controller.setServiceUser(userServiceDB);
            Stage stage = new Stage();
            stage.setTitle("Sign Up");
            stage.setScene(new Scene(root));
            stage.setWidth(400);
            stage.setHeight(600);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
