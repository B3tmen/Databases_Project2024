package org.unibl.etf.controllers.employee;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import org.unibl.etf.model.product.Product;
import org.unibl.etf.model.purchases.OrderItem;
import org.unibl.etf.service.ProductService;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeOrdersProductDetailsController implements Initializable {
    private ObservableList<OrderItem> orderItems;


    @FXML
    private TableView<OrderItem> orderItemsTableView;
    @FXML
    private TableColumn<OrderItem, ImageView> imageColumn;
    @FXML
    private TableColumn<OrderItem, String> productIdColumn;
    @FXML
    private TableColumn<OrderItem, String> productNameColumn;
    @FXML
    private TableColumn<OrderItem, BigDecimal> productPriceColumn;
    @FXML
    private TableColumn<OrderItem, Integer> orderItemQuantityColumn;
    @FXML
    private TableColumn<OrderItem, String> warrantyColumn;
    @FXML
    private TableColumn<OrderItem, BigDecimal> subtotalColumn;

    public EmployeeOrdersProductDetailsController(List<OrderItem> orderItems) {
        this.orderItems = FXCollections.observableList(orderItems);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableView();
    }

    private void setupTableView() {
        imageColumn.setCellValueFactory(param -> {
            Product product = param.getValue().getProduct();
            return new SimpleObjectProperty<>(product.getImage());
        });
        productIdColumn.setCellValueFactory(param -> {
            Product product = param.getValue().getProduct();
            return new SimpleStringProperty(String.valueOf(product.getId()));
        });
        productNameColumn.setCellValueFactory(param -> {
            Product product = param.getValue().getProduct();
            return new SimpleStringProperty(product.getName());
        });
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        orderItemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        warrantyColumn.setCellValueFactory(param -> {
            Product product = param.getValue().getProduct();
            return new SimpleStringProperty(String.valueOf(product.getWarrantyMonths()));
        });
        subtotalColumn.setCellValueFactory(new PropertyValueFactory<>("subTotal"));

        orderItemsTableView.setItems(orderItems);
    }

}
