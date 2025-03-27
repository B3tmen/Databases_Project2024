package org.unibl.etf.controllers.employee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.unibl.etf.model.address.City;
import org.unibl.etf.model.address.Country;
import org.unibl.etf.model.users.Employee;
import org.unibl.etf.model.users.User;
import org.unibl.etf.util.ImagePaths;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeAccountController implements Initializable {
    private Employee employee;

    @FXML
    private TextField salaryTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField positionTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private Button closeButton;
    @FXML
    private Circle imageHolderCircle;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private Label fullNameLabel;
    @FXML
    private TextField usernameTextField;

    public EmployeeAccountController(User user) {
        this.employee = (Employee) user;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image(getClass().getResource(ImagePaths.USER_PROFILE_LOGO).toExternalForm());
        imageHolderCircle.setFill(new ImagePattern(img));
        loadEmployee();

        disableTextFieldEditing();
    }

    private void loadEmployee() {
        fullNameLabel.setText(employee.getFirstName() + " " + employee.getLastName());
        usernameTextField.setText(employee.getUsername());
        firstNameTextField.setText(employee.getFirstName());
        lastNameTextField.setText(employee.getLastName());
        emailTextField.setText(employee.getEmail());
        positionTextField.setText(employee.getPosition());
        salaryTextField.setText(String.valueOf(employee.getSalary()));
    }

    private void disableTextFieldEditing(){
        usernameTextField.setEditable(false);
        firstNameTextField.setEditable(false);
        lastNameTextField.setEditable(false);
        emailTextField.setEditable(false);
        positionTextField.setEditable(false);
        salaryTextField.setEditable(false);
    }

    @FXML
    public void closeAccountViewAction(ActionEvent actionEvent) {
        Node button = (Node) actionEvent.getSource();
        Node accountAnchorPane = button.getParent();

        var parent = (BorderPane) accountAnchorPane.getParent();
        parent.getChildren().remove(accountAnchorPane);
    }
}
