package org.unibl.etf.controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.unibl.etf.model.users.Administrator;
import org.unibl.etf.model.users.User;
import org.unibl.etf.util.FxmlPaths;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMainController implements Initializable {
    private Administrator administrator;

    public AdminMainController(User user) {
        this.administrator = (Administrator) user;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usernameLabel.setText(administrator.getUsername());
    }

    @FXML
    private BorderPane borderPane;
    @FXML
    private Label usernameLabel;


    @FXML
    public void showUsersTableAction(ActionEvent actionEvent) {
        showView(FxmlPaths.ADMIN_USERS_VIEW);
    }

    private void showView(String fxmlPath) {
        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            if(loader.getController() == null){
                if(fxmlPath.equals(FxmlPaths.ADMIN_USERS_VIEW)){
                    loader.setController(new AdminUsersController());
                }

            }

            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        borderPane.centerProperty().set(root);
    }

}
