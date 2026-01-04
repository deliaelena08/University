package bakeryshop.bakeryshop;

import bakeryshop.bakeryshop.controllers.LoginController;
import bakeryshop.bakeryshop.repos.*;
import bakeryshop.bakeryshop.services.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        IRepoUser repoUser=new RepoDBUser();
        IServiceUser serviceUser = new ServiceUser(repoUser);
        IRepoCake repoCake=new RepoDBCake();
        IServiceCake serviceCake=new ServiceCake(repoCake);
        IRepoOrder repoOrder= new RepoDBOrder();
        IServiceOrder serviceOrder=new ServiceOrder(repoOrder);
        IRepoOrderItem repoOrderItem=new RepoDBOrderItem();
        IServiceOrderItem serviceOrderItem=new ServiceOrderItem(repoOrderItem);
        IRepoOrderStatus repoOrderStatus=new RepoDBOrderStatus();
        IServiceOrderStatus serviceOrderStatus=new ServiceOrderStatus(repoOrderStatus);
        IRepoStatus repoStatus=new RepoDBStatus();
        IServiceStatus serviceStatus=new ServiceStatus(repoStatus);
        IRepoEmployeeOrder repoEmployeeOrder=new RepoDBEmployeeOrder();
        IServiceEmployeeOrder serviceEmployeeOrder=new ServiceEmployeeOrder(repoEmployeeOrder);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/bakeryshop/bakeryshop/fxml/login-view.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.setServices(serviceUser,serviceCake,serviceOrder,serviceEmployeeOrder,serviceStatus,serviceOrderStatus,serviceOrderItem);
        primaryStage.setScene(new Scene(root));
        primaryStage.setWidth(400);
        primaryStage.setHeight(600);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}