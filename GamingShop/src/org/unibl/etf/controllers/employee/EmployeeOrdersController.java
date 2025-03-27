package org.unibl.etf.controllers.employee;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.unibl.etf.database.dao.implementations.*;
import org.unibl.etf.database.mysql.DBUtils;
import org.unibl.etf.model.enums.OrderStatusEnum;
import org.unibl.etf.model.product.Product;
import org.unibl.etf.model.purchases.*;
import org.unibl.etf.service.*;
import org.unibl.etf.util.AlertShower;
import org.unibl.etf.util.FxmlPaths;
import org.unibl.etf.util.FxmlViewManager;
import org.unibl.etf.util.ReceiptWriter;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeOrdersController implements Initializable {
    private OrderService orderService;
    private OrderItemService orderItemService;
    private CustomerService customerService;
    private ReceiptService receiptService;
    private ReceiptItemService receiptItemService;
    private ObservableList<Order> orders;


    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<Order> ordersTableView;
    @FXML
    private Button shipOrderButton;
    @FXML
    private Button cancelOrderButton;
    @FXML
    private Button markOrderCompleteButton;


    public EmployeeOrdersController(){
        this.orderService = new OrderService(new OrderDAOImpl());
        this.orderItemService = new OrderItemService(new OrderItemDAOImpl());
        this.customerService = new CustomerService(new CustomerDAOImpl());
        this.receiptService = new ReceiptService(new ReceiptDAOImpl());
        this.receiptItemService = new ReceiptItemService(new ReceiptItemDAOImpl());
        this.orders = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadOrders();
        setupOrdersTableView();
        setupButtonsOnRecordClick();

        setupSearchMechanism();
    }

    private void setupButtonsOnRecordClick() {
        ordersTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            Order order = newSelection;
            OrderStatus status = order.getOrderStatus();
            if(status.getOrderStatusEnum().equals(OrderStatusEnum.PENDING)){
                shipOrderButton.setDisable(false);
                cancelOrderButton.setDisable(false);
                markOrderCompleteButton.setDisable(true);
            }
            else if(status.getOrderStatusEnum().equals(OrderStatusEnum.SHIPPED)){
                shipOrderButton.setDisable(true);
                cancelOrderButton.setDisable(true);
                markOrderCompleteButton.setDisable(false);
            }
            else if(status.getOrderStatusEnum().equals(OrderStatusEnum.COMPLETED)){
                shipOrderButton.setDisable(true);
                cancelOrderButton.setDisable(true);
                markOrderCompleteButton.setDisable(true);
            }
            else if(status.getOrderStatusEnum().equals(OrderStatusEnum.CANCELLED)){
                shipOrderButton.setDisable(true);
                cancelOrderButton.setDisable(true);
                markOrderCompleteButton.setDisable(true);
            }
            else{
                shipOrderButton.setDisable(true);
                cancelOrderButton.setDisable(true);
                markOrderCompleteButton.setDisable(true);
            }
        });
    }

    private void loadOrders() {
        //DBUtils.loadTableViewFromDatabase(orderService::getOrders, ordersTableView);
        DBUtils.loadObservableListFromDatabase(orderService::getOrders, orders);        // load the orders in a separate Thread to not burden the main JavaFX app thread
    }

    private void setupOrdersTableView() {
        ordersTableView.getColumns().clear();  // because of the columns in FXML view to not stack columns one after the other

        // Set up the columns
        TableColumn<Order, Integer> imageColumn = new TableColumn<>("Order ID");
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Order, String> orderStatusColumn = new TableColumn<>("Order status");
        orderStatusColumn.setCellValueFactory(param ->{
            Order order = param.getValue();
            return new SimpleStringProperty(order.getOrderStatus().getOrderStatusEnum().getStatus().toUpperCase());
        });

        TableColumn<Order, Date> orderDateColumn = new TableColumn<>("Order date");
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        TableColumn<Order, BigDecimal> shippingColumn = new TableColumn<>("Shipping cost");
        shippingColumn.setCellValueFactory(new PropertyValueFactory<>("shipping"));

        TableColumn<Order, Void> customerDetailsColumn = new TableColumn<>("Customer details");
        customerDetailsColumn.setCellFactory(getCellFactoryButton("View customer"));

        TableColumn<Order, Void> productDetailsColumn = new TableColumn<>("Product details");
        productDetailsColumn.setCellFactory(getCellFactoryButton("View products"));

        TableColumn<Order, BigDecimal> grandTotalColumn = new TableColumn<>("Grand total");
        grandTotalColumn.setCellValueFactory(new PropertyValueFactory<>("grandTotal"));

        ordersTableView.getColumns().addAll(imageColumn, orderStatusColumn, orderDateColumn, shippingColumn, customerDetailsColumn, productDetailsColumn, grandTotalColumn);
        ordersTableView.setItems(orders);
    }

    private void setupSearchMechanism(){
        FilteredList<Order> filteredProducts = new FilteredList<>(orders, b -> true);

        searchTextField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredProducts.setPredicate(order -> {
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){

                    return true;
                }

                String searchKeyWord = newValue.toLowerCase();
                if(String.valueOf(order.getId()).toLowerCase().contains(searchKeyWord)){
                    return true;    // We found a match in OrderID in the table view based on the search bar
                }
                else if(order.getOrderDate().toString().contains(searchKeyWord)){
                    return true;
                }
                else {
                    return false;
                }
            });
        });

        SortedList<Order> sortedProducts = new SortedList<>(filteredProducts);
        sortedProducts.comparatorProperty().bind(ordersTableView.comparatorProperty());

        ordersTableView.setItems(sortedProducts);
    }

    private Callback<TableColumn<Order, Void>, TableCell<Order, Void>> getCellFactoryButton(String buttonTitle) {
        return new Callback<>() {
            @Override
            public TableCell<Order, Void> call(final TableColumn<Order, Void> param) {
                final TableCell<Order, Void> cell = new TableCell<>() {

                    private final Button btn = new Button(buttonTitle);

                    {
                        btn.setOnAction((event) -> {
                            Order order = getTableView().getItems().get(getIndex());
                            List<OrderItem> orderItems = orderItemService.getOrderItemsById(order.getId());
                            order.setOrderItems(orderItems);

                            System.out.println("order items size: " + order.getOrderItems().size());
                            if(btn.getText().contains("customer")){
                                viewCustomerDetails(FxmlPaths.EMPLOYEE_ORDERS_CUSTOMER_DETAILS_VIEW, order.getCustomerId());
                            }
                            else if(btn.getText().contains("products")){
                                viewProductDetails(FxmlPaths.EMPLOYEE_ORDERS_PRODUCT_DETAILS_VIEW, order.getOrderItems());
                            }

                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
    }

    private void viewCustomerDetails(String fxmlPath, int customerId){
        FxmlViewManager viewManager = new FxmlViewManager(fxmlPath, "Customer details", new EmployeeOrdersCustomerDetailsController(customerId));
        viewManager.showView();
    }

    private void viewProductDetails(String fxmlPath, List<OrderItem> orderItems){
        FxmlViewManager viewManager = new FxmlViewManager(fxmlPath, "Product details", new EmployeeOrdersProductDetailsController(orderItems));
        viewManager.showView();
    }

    private void refreshTable(){
        //ordersTableView.getSelectionModel().clearSelection();
        ordersTableView.refresh();
    }

    @FXML
    public void cancelOrderAction(ActionEvent actionEvent) {
        setOrderStatusHelper(OrderStatusEnum.CANCELLED, "Cancelling an order.", "You are about to declare an order for cancellation.", "No order was selected for cancellation.");
    }

    @FXML
    public void shipOrderAction(ActionEvent actionEvent) {
        setOrderStatusHelper(OrderStatusEnum.SHIPPED, "Shipping an order.", "You are about to declare an order for shipment.", "No order was selected for shipment.");
    }

    @FXML
    public void completeOrderAction(ActionEvent actionEvent) {
        Order order = ordersTableView.getSelectionModel().getSelectedItem();
        setOrderStatusHelper(OrderStatusEnum.COMPLETED, "Completing an order.", "You are about to declare an order for completion.", "No order was selected for completion.");

        makeReceipt(order);
    }

    private void makeReceipt(Order order) {
        Receipt receipt = new Receipt(0, order, Timestamp.valueOf(LocalDateTime.now()));
        int receiptId = receiptService.addReceipt(receipt);
        for(OrderItem orderItem : order.getOrderItems()){
            ReceiptItem receiptItem = new ReceiptItem(receipt.getId(), orderItem.getProduct(), orderItem.getPrice(), orderItem.getQuantity(), orderItem.getSubTotal());

            receipt.addReceiptItem(receiptItem);
            receiptItemService.addReceiptItem(receiptItem);
        }

        String customerEmail = customerService.getCustomerById(order.getCustomerId()).getEmail();
        ReceiptWriter receiptWriter = new ReceiptWriter(receipt, order, customerEmail);
        receiptWriter.writeReceipt();
    }

    private void setOrderStatusHelper(OrderStatusEnum statusEnum, String alertTitle, String positiveAlertHeaderText, String negativeAlertHeaderText) {
        Order order = (Order) ordersTableView.getSelectionModel().getSelectedItem();


        if(order != null){
            AlertShower alertShower = new AlertShower(Alert.AlertType.INFORMATION, alertTitle, positiveAlertHeaderText, "Continue?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> answer = alertShower.showAlert();
            if(answer.get() == ButtonType.YES){
                order.getOrderStatus().setOrderStatusEnum(statusEnum);
                orderService.setOrderStatus(order.getId(), order.getOrderStatus().getId());

                refreshTable();
            }
        }
        else{
            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, alertTitle, negativeAlertHeaderText, "Try again.");
            alertShower.showAlert();
        }
    }
}
