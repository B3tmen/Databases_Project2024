package org.unibl.etf.controllers.admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.unibl.etf.database.dao.implementations.UserDAOImpl;
import org.unibl.etf.model.product.Product;
import org.unibl.etf.model.users.Administrator;
import org.unibl.etf.model.users.Employee;
import org.unibl.etf.model.users.User;
import org.unibl.etf.service.UserService;
import org.unibl.etf.util.AlertShower;
import org.unibl.etf.util.FxmlPaths;
import org.unibl.etf.util.FxmlViewManager;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminUsersController implements Initializable {
    private UserService userService;

    private ObservableList<User> accounts;

    @FXML
    private TableView<User> accountsTableView;
    @FXML
    private TextField searchTextField;

    public AdminUsersController() {
        this.userService = new UserService(new UserDAOImpl());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadUsers();
        setupProductsTableView();
        setupSearchMechanism();
    }

    private void loadUsers() {
        accounts = FXCollections.observableArrayList(userService.getUsers());
    }

    private void setupProductsTableView(){
        accountsTableView.getColumns().clear();  // because of the FXML view to not stack columns one after the other

        // Set up the columns

        TableColumn<User, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> firstNameColumn = new TableColumn<>("First name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<User, String> lastNameColumn = new TableColumn<>("Last name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(param -> {
            User user = param.getValue();
            if(user instanceof Administrator){
                return new SimpleStringProperty("Administrator");
            }
            else if(user instanceof Employee){
                return new SimpleStringProperty("Employee");
            }
            else{
                return new SimpleStringProperty("Customer");
            }
        });

        TableColumn<User, String> isActiveColumn = new TableColumn<>("Active");
        isActiveColumn.setCellValueFactory(param ->{
            User user = param.getValue();
            if(user.isActive()){
                return new SimpleStringProperty("TRUE");
            }
            else{
                return new SimpleStringProperty("FALSE");
            }
        });

        accountsTableView.getColumns().addAll(idColumn, usernameColumn, firstNameColumn, lastNameColumn, emailColumn, typeColumn, isActiveColumn);
        accountsTableView.setItems(accounts);
    }

    private void setupSearchMechanism(){
        FilteredList<User> filteredUsers = new FilteredList<>(accounts, b -> true);

        searchTextField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredUsers.setPredicate(user -> {
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyWord = newValue.toLowerCase();
                if(user.getUsername().toLowerCase().contains(searchKeyWord)){
                    return true;
                }
                else if(String.valueOf(user.getId()).toLowerCase().contains(searchKeyWord)){
                    return true;
                }
                else {
                    return false;
                }
            });
        });

        SortedList<User> sortedUsers = new SortedList<>(filteredUsers);
        sortedUsers.comparatorProperty().bind(accountsTableView.comparatorProperty());

        accountsTableView.setItems(sortedUsers);
    }

    private void showView(String fxmlPath, String title, java.lang.Object... controllerClass){
        FxmlViewManager viewManager = new FxmlViewManager(fxmlPath, title, controllerClass);
        viewManager.showView();
    }

    private void activateDeactivateUserHelper(boolean activationStatus){
        User user = accountsTableView.getSelectionModel().getSelectedItem();
        int numberOfActivationsDeactivations = 0;

        if (user != null) {
            numberOfActivationsDeactivations = userService.setUserActivationStatus(user.getId(), activationStatus);
            user.setActive(activationStatus);


            if(numberOfActivationsDeactivations > 0){
                AlertShower alertShower = new AlertShower(Alert.AlertType.CONFIRMATION, "Success", "Selected User succesfully " + (activationStatus ? "activated" : "deactivated") + ".", "");
                alertShower.showAlert();

                accountsTableView.getSelectionModel().select(null);     // reset table view selection
                refreshTable();
            }
        }
        else{
            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Error", "Row for " + (activationStatus ? "activation" : "deactivation") + " not selected!", "Try again.");
            alertShower.showAlert();
        }
    }

    private void refreshTable(){
        accountsTableView.refresh();
    }

    @FXML
    public void addAccountAction(ActionEvent actionEvent) {
        showView(FxmlPaths.ADMIN_ADD_USER_VIEW, "Add a User", new AdminAddAccountController());

    }

    @FXML
    public void updateAccountAction(ActionEvent actionEvent) {
        User user = accountsTableView.getSelectionModel().getSelectedItem();
        if(user != null){
            showView(FxmlPaths.ADMIN_UPDATE_USER_VIEW, "Add a User", new AdminUpdateAccountController(user));
            refreshTable();
        }
        else{
            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Error", "Please select an Account for updating.", "Try again.");
            alertShower.showAlert();
        }
    }


    @FXML
    public void deActivateAccountAction(ActionEvent actionEvent) {
        activateDeactivateUserHelper(false);
    }

    @FXML
    public void activateAccountAction(ActionEvent actionEvent) {
        activateDeactivateUserHelper(true);
    }



}
