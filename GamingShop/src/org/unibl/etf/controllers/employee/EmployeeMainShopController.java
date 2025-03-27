package org.unibl.etf.controllers.employee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.unibl.etf.model.users.Employee;
import org.unibl.etf.model.users.User;
import org.unibl.etf.util.FxmlPaths;
import org.unibl.etf.util.FxmlViewManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeMainShopController implements Initializable {
    private Employee employee;

    @FXML
    private BorderPane borderPane;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button productsButton;
    @FXML
    private Button accountButton;
    @FXML
    private Button ordersButton;

    public EmployeeMainShopController(User user) {
        this.employee = (Employee) user;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usernameLabel.setText(employee.getUsername());
    }


    @FXML
    public void showProductsTableAction(ActionEvent actionEvent) {
        showView(FxmlPaths.EMPLOYEE_PRODUCTS_VIEW);
    }

    @FXML
    public void showOrdersTableAction(ActionEvent actionEvent) {
        showView(FxmlPaths.EMPLOYEE_ORDERS_VIEW);
    }

    @FXML
    public void showAccountAction(ActionEvent actionEvent) {
        showView(FxmlPaths.EMPLOYEE_ACCOUNT_VIEW);
    }


    private void showView(String fxmlPath) {
        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            if(loader.getController() == null){
                if(fxmlPath.equals(FxmlPaths.EMPLOYEE_PRODUCTS_VIEW)){
                    loader.setController(new EmployeeProductsController(employee));
                }
                else if(fxmlPath.equals(FxmlPaths.EMPLOYEE_ORDERS_VIEW)){
                    loader.setController(new EmployeeOrdersController());
                }
                else if(fxmlPath.equals(FxmlPaths.EMPLOYEE_ACCOUNT_VIEW)){
                    loader.setController(new EmployeeAccountController(employee));
                }

            }

            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        borderPane.centerProperty().set(root);
    }


    @FXML
    public void logOutAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to log out");
        alert.setContentText("Are you sure you want to log out?");

        if(alert.showAndWait().get() == ButtonType.OK){
            Node node = (Node) actionEvent.getSource();     // closing the current stage/customer window
            Stage thisStage = (Stage) node.getScene().getWindow();
            thisStage.close();

            FxmlViewManager viewManager = new FxmlViewManager(FxmlPaths.USER_LOGIN_FXML, "User login");
            viewManager.showView();
        }
    }
}
