package org.unibl.etf.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.unibl.etf.controllers.admin.AdminMainController;
import org.unibl.etf.controllers.employee.EmployeeMainShopController;
import org.unibl.etf.controllers.customer.CustomerMainShopController;
import org.unibl.etf.database.dao.implementations.UserDAOImpl;
import org.unibl.etf.model.users.Administrator;
import org.unibl.etf.model.users.Customer;
import org.unibl.etf.model.users.Employee;
import org.unibl.etf.model.users.User;
import org.unibl.etf.service.UserService;
import org.unibl.etf.util.AlertShower;
import org.unibl.etf.util.FxmlPaths;
import org.unibl.etf.util.ImagePaths;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserLoginController implements Initializable {
    private UserService userService = new UserService(new UserDAOImpl());

    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button registerButton;
    @FXML
    private Button signInButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private ImageView appLogoImageView;
    @FXML
    private Button showPasswordButton;
    @FXML
    private Button unshowPasswordButton;
    @FXML
    private TextField passwordRevealedTextField;


    public UserLoginController() { }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appLogoImageView.setImage(new Image(getClass().getResource(ImagePaths.STORE_LOGO).toExternalForm()));
        appLogoImageView.setFitWidth(500);
        appLogoImageView.setFitHeight(300);

        setupSeePasswordButtonsLocation();
        setupPasswordTextFields();
    }

    @FXML
    public void registerAccount(ActionEvent actionEvent) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(FxmlPaths.USER_REGISTER_FXML));
        } catch (IOException e) {
            e.printStackTrace();
        }

        showWindow(root, "Registration");
    }

    @FXML
    public void showPassword(ActionEvent actionEvent) {
        showPasswordButton.setVisible(false);
        unshowPasswordButton.setVisible(true);

        passwordTextField.setVisible(false);
        passwordRevealedTextField.setVisible(true);
    }

    @FXML
    public void unshowPassword(ActionEvent actionEvent) {
        showPasswordButton.setVisible(true);
        unshowPasswordButton.setVisible(false);

        passwordTextField.setVisible(true);
        passwordRevealedTextField.setVisible(false);
    }

    @FXML
    public void signInAction(ActionEvent actionEvent) {
        String username = usernameTextField.getText(), password = passwordTextField.getText();

        User user = null;

        try {
            user = userService.getLoginUser(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (user != null && user.isActive()) {
            showAlert(Alert.AlertType.CONFIRMATION, "Login", "LOGIN SUCCESSFUL", "Welcome " + user.getUsername());
            if(user instanceof Customer){
                signInUser(user, FxmlPaths.CUSTOMER_MAIN_FXML, actionEvent);
            }
            else if(user instanceof Employee) {
                signInUser(user, FxmlPaths.EMPLOYEE_MAIN_VIEW, actionEvent);
            }
            else{
                signInUser(user, FxmlPaths.ADMIN_MAIN_VIEW, actionEvent);
            }

        }
        else if(user != null && !user.isActive()){
            showAlert(Alert.AlertType.ERROR, "Login", "LOGIN UNSUCCESSFUL", "This account is no longer active. Contact Your administrator.");
        }
        else{
            showAlert(Alert.AlertType.ERROR, "Login", "LOGIN UNSUCCESSFUL", "Incorrect username or password. Try again.");
        }
    }

    private void signInUser(User user, String fxmlPath, ActionEvent actionEvent){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = null;
        try {
            if(user instanceof Customer){
                CustomerMainShopController controller = new CustomerMainShopController(user);
                loader.setController(controller);
            }
            else if(user instanceof Employee){
                EmployeeMainShopController controller = new EmployeeMainShopController(user);
                loader.setController(controller);
            }
            else{
                AdminMainController controller = new AdminMainController(user);
                loader.setController(controller);
            }

            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(root != null) {
            showWindow(root, "Gaming Shop");

            Node node = (Node) actionEvent.getSource();     // closing the current stage/login window
            Stage thisStage = (Stage) node.getScene().getWindow();
            thisStage.close();
        }
    }

    private void showWindow(Parent root, String title){
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.getIcons().add(new Image(getClass().getResource(ImagePaths.APP_ICON).toExternalForm()));
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText){
        AlertShower alertShower = new AlertShower(alertType, title, headerText, contentText);
        alertShower.showAlert();
    }

    private void setupSeePasswordButtonsLocation(){
        double x = showPasswordButton.getLayoutX(), y = showPasswordButton.getLayoutY();

        unshowPasswordButton.setLayoutX(x);
        unshowPasswordButton.setLayoutY(y);
        unshowPasswordButton.setVisible(false);
    }

    private void setupPasswordTextFields(){
        double x = passwordTextField.getLayoutX(), y = passwordTextField.getLayoutY();

        passwordRevealedTextField.setLayoutX(x);
        passwordRevealedTextField.setLayoutY(y);
        passwordRevealedTextField.setVisible(false);

        passwordRevealedTextField.textProperty().bindBidirectional(passwordTextField.textProperty());   // synchronizing the text content
    }


}
