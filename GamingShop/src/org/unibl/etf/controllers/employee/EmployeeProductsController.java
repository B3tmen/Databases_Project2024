package org.unibl.etf.controllers.employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.unibl.etf.database.dao.implementations.DiscountDAOImpl;
import org.unibl.etf.database.dao.implementations.ProductDAOImpl;
import org.unibl.etf.database.mysql.DBUtils;
import org.unibl.etf.model.product.Product;
import org.unibl.etf.model.purchases.Discount;
import org.unibl.etf.model.users.Employee;
import org.unibl.etf.model.users.User;
import org.unibl.etf.service.DiscountService;
import org.unibl.etf.service.ProductService;
import org.unibl.etf.util.AlertShower;
import org.unibl.etf.util.FxmlPaths;
import org.unibl.etf.util.FxmlViewManager;

import javax.swing.text.html.ImageView;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EmployeeProductsController implements Initializable {
    private ProductService productService;
    private DiscountService discountService;
    private ObservableList<Product> products;

    private Employee employee;

    @FXML
    private TableView productsTableView;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button deleteButton;

    public EmployeeProductsController(User user) {
        this.discountService = new DiscountService(new DiscountDAOImpl());
        this.productService = new ProductService(new ProductDAOImpl());
        this.employee = (Employee) user;
        this.products = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadProducts();
        setupProductsTableView();

        setupSearchMechanism();
    }

    private void loadProducts(){
        //DBUtils.loadTableViewFromDatabase(productService::getProducts, productsTableView);
        DBUtils.loadObservableListFromDatabase(productService::getProducts, products);      // load the products in a separate Thread to not burden the main JavaFX app thread

    }

    private void setupProductsTableView(){
        productsTableView.getColumns().clear();  // because of the FXML view to not stack columns one after the other

        // Set up the columns
        TableColumn<Product, ImageView> imageColumn = new TableColumn<>("Image");
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

        TableColumn<Product, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, BigDecimal> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityAvailable"));

        TableColumn<Product, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Product, Integer> warrantyColumn = new TableColumn<>("Warranty (Months)");
        warrantyColumn.setCellValueFactory(new PropertyValueFactory<>("warrantyMonths"));

        TableColumn<Product, Integer> manufacturerColumn = new TableColumn<>("Manufacturer");
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturerName"));

        TableColumn<Product, Integer> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));

        productsTableView.getColumns().addAll(imageColumn, idColumn, nameColumn, priceColumn, quantityColumn, descriptionColumn, warrantyColumn, manufacturerColumn, categoryColumn);
        productsTableView.setItems(products);
        productsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void showView(String fxmlPath, String title, java.lang.Object... controllerClass){
        FxmlViewManager viewManager = new FxmlViewManager(fxmlPath, title, controllerClass);
        viewManager.showView();
    }

    private void setupSearchMechanism(){
        FilteredList<Product> filteredProducts = new FilteredList<>(products, b -> true);

        searchTextField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredProducts.setPredicate(product -> {
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyWord = newValue.toLowerCase();
                if(product.getName().toLowerCase().contains(searchKeyWord)){
                    return true; // We found a match in ProductName in the table view based on the search bar
                }
                else if(String.valueOf(product.getId()).toLowerCase().contains(searchKeyWord)){
                    return true;
                }
                else if(product.getCategoryName().toLowerCase().contains(searchKeyWord)){
                    return true;
                }
                else {
                    return false;
                }
            });
        });

        SortedList<Product> sortedProducts = new SortedList<>(filteredProducts);
        sortedProducts.comparatorProperty().bind(productsTableView.comparatorProperty());

        productsTableView.setItems(sortedProducts);
    }

    public void refreshTable(){
        productsTableView.refresh();
    }

    @FXML
    public void addProductAction(ActionEvent actionEvent) {
        showView(FxmlPaths.EMPLOYEE_ADD_PRODUCT_VIEW, "Add a Product", new EmployeeAddProductController(employee));

    }

    @FXML
    public void updateProductAction(ActionEvent actionEvent) {
        Product product = (Product) productsTableView.getSelectionModel().getSelectedItem();
        if(product != null){
            Discount discount = null;
            if(product.getDiscountId() != 0){
                discount = discountService.getDiscountById(product.getDiscountId());
            }

            showView(FxmlPaths.EMPLOYEE_UPDATE_PRODUCT_VIEW, "Update a Product", new EmployeeUpdateProductController(employee, product, discount));
        }
        else{
            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Error", "You didn't select a product for updating.", "Try again.");
            alertShower.showAlert();
        }
    }

    @FXML
    public void deleteProductAction(ActionEvent actionEvent) {
        Product product = (Product) productsTableView.getSelectionModel().getSelectedItem();
        int numberOfDeletions = 0;
        if (product != null) {
            AlertShower questionShower = new AlertShower(Alert.AlertType.INFORMATION, "Deletion of Product", "You are about to delete a Product", "Are you sure you want to delete the selected product?", ButtonType.YES, ButtonType.NO);
            var answer = questionShower.showAlert();
            if(answer.get().equals(ButtonType.YES)){
                try {
                    numberOfDeletions = productService.deleteProduct(product);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if(numberOfDeletions > 0){
                    AlertShower alertShower = new AlertShower(Alert.AlertType.CONFIRMATION, "Success", "Selected Product succesfully deleted.", "");
                    alertShower.showAlert();

                    productsTableView.getSelectionModel().select(null);     // reset table view selection
                    products.remove(product);                                    // removing product from TableView
                }
            }
        }
        else{
            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Error", "Row for deletion not selected!", "Try again.");
            alertShower.showAlert();
        }
    }



}
