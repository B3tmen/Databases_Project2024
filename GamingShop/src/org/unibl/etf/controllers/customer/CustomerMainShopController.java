package org.unibl.etf.controllers.customer;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.unibl.etf.database.dao.implementations.ProductDAOImpl;
import org.unibl.etf.database.dao.implementations.ShoppingCartDAOImpl;
import org.unibl.etf.database.mysql.DBUtils;
import org.unibl.etf.model.purchases.CartItem;
import org.unibl.etf.model.purchases.ShoppingCart;
import org.unibl.etf.model.product.Product;
import org.unibl.etf.model.users.Customer;
import org.unibl.etf.model.users.User;
import org.unibl.etf.service.ProductService;
import org.unibl.etf.service.ShoppingCartService;
import org.unibl.etf.util.FxmlPaths;
import org.unibl.etf.util.FxmlViewManager;
import org.unibl.etf.util.ImagePaths;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;

public class CustomerMainShopController implements Initializable {

    // region Regular fields
    private ProductService productService = new ProductService(new ProductDAOImpl());
    private ShoppingCartService shoppingCartService = new ShoppingCartService(new ShoppingCartDAOImpl());

    private boolean isSideBarHidden = true;
    private ObservableList<Product> productList;
    private ShoppingCart shoppingCart;
    private Customer customer;

    // endregion


    // region FXML fields

    @FXML
    private AnchorPane mainContentAnchorPane;
    @FXML
    private AnchorPane productContainerAnchorPane;
    @FXML
    private Button shoppingCartButton;
    @FXML
    private Button sideBarButton;
    @FXML
    private Button logOutButton;
    @FXML
    private Button accountButton;
    @FXML
    private Button PCComponentsButton;
    @FXML
    private Button PCPeripheralsButton;
    @FXML
    private Button consolesButton;
    @FXML
    private Button cablesButton;
    @FXML
    private Button gamesButton;
    @FXML
    private VBox sideBarContainerVBox;
    @FXML
    private VBox dropDownPCComponentsVbox;
    @FXML
    private VBox dropDownPCPeripheralsVBox;
    @FXML
    private VBox dropDownConsolesVBox;
    @FXML
    private HBox titleContainerHBox;
    @FXML
    private GridPane productContainerGridPane;
    @FXML
    private FontIcon sidebarButtonIcon;
    @FXML
    private Label cartPriceLabel;
    @FXML
    private Label numberOfCartItemsLabel;
    @FXML
    private Label customerUsernameLabel;

    // endregion


    //region Constructors

    public CustomerMainShopController(User user){
        this.customer = (Customer) user;
        this.productList = FXCollections.observableArrayList();

        fillProductList(productService::getProducts);
        makeShoppingCart();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSideBar();
        setupDropDownMenu(dropDownPCComponentsVbox, PCComponentsButton);
        setupDropDownMenu(dropDownPCPeripheralsVBox, PCPeripheralsButton);
        setupDropDownMenu(dropDownConsolesVBox, consolesButton);

        fillProductContainerGridPane();
        setupLabels();
    }

    public void setupLabels() {
        customerUsernameLabel.setText(customer.getUsername());

        updateCartItemsNumberLabel(shoppingCart.getCartItems().size());
        updateCartPriceLabel(shoppingCart.getGrandTotal());
    }

    // endregion


    //region Helper methods
    private void fillProductContainerGridPane() {
        int column = 0, row = 1;
        productContainerGridPane.setHgap(15);
        productContainerGridPane.setVgap(15);

        if(!productContainerGridPane.getChildren().isEmpty()){
            productContainerGridPane.getChildren().clear();
            fillProductContainerGridPane(column, row);
        }
        else{
            fillProductContainerGridPane(column, row);
        }
    }

    private void fillProductContainerGridPane(int column, int row) {
        for(Product product : productList) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FxmlPaths.PRODUCT_CARD_FXML));
            loader.setController(new ProductCardController(this, product));
            AnchorPane productCardAnchorPane = null;
            try {
                productCardAnchorPane = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(column == 5){
                column = 0;
                row++;
            }

            productContainerGridPane.add(productCardAnchorPane, column++, row);
        }
    }

    private void makeShoppingCart() {
        int customerId = customer.getId();
        boolean hasShoppingCart = shoppingCartService.customerHasShoppingCart(customerId);

        if(hasShoppingCart){
            this.shoppingCart = shoppingCartService.getShoppingCartByCustomerId(customerId);
        }
        else{
            this.shoppingCart = new ShoppingCart(0, customer.getId());
            shoppingCartService.addShoppingCart(shoppingCart);
        }
    }

    private void fillProductList(Supplier<List<Product>> serviceMethod){
        if(productList.isEmpty()){
            productList.addAll(serviceMethod.get());
        }
        else{
            productList.setAll(serviceMethod.get());
        }
    }

    private void animateAnchor(DoubleProperty leftAnchor, double value, Node container) {
        Timeline timeline = new Timeline();
        KeyValue keyValue;

        keyValue = new KeyValue(leftAnchor, value); // Stretch to the left

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.4), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

        leftAnchor.addListener((obs, oldVal, newVal) -> AnchorPane.setLeftAnchor(container, newVal.doubleValue()));
    }

    public void addItemToCart(CartItem cartItem){
        shoppingCart.getCartItems().add(cartItem);
    }

    private void animateSideBarIcon(boolean isClosingAction){
        if(isClosingAction){
            sidebarButtonIcon.setIconLiteral("fas-bars");
        }
        else{
            sidebarButtonIcon.setIconLiteral("fas-window-close");
        }
    }

    private void initSideBar(){
        double width = sideBarContainerVBox.getWidth();
        sideBarContainerVBox.setTranslateX(-150);   // TODO: change to width property of sidebar

        dropDownPCComponentsVbox.setVisible(false);
        dropDownPCComponentsVbox.setManaged(false);
        dropDownPCPeripheralsVBox.setVisible(false);
        dropDownPCPeripheralsVBox.setManaged(false);
        dropDownConsolesVBox.setVisible(false);
        dropDownConsolesVBox.setManaged(false);
    }

    private void setMainContentAnchorPane(String fxmlViewPath){
        Parent content = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlViewPath));

        try {
            if(fxmlViewPath.equals(FxmlPaths.CUSTOMER_ACCOUNT_FXML)){
                loader.setController(new CustomerAccountController(customer));
            }

            content = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainContentAnchorPane.getChildren().add(content);
        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);

        //setSubContentCssStyle(content, CssPaths.CUSTOMER_ACCOUNT_CSS);
    }

    private void setupDropDownMenu(VBox dropdownMenu, Button buttonActivator){
        buttonActivator.setOnAction(event -> {
            if(dropdownMenu.isVisible()){
                dropdownMenu.setVisible(false);
                dropdownMenu.setManaged(false);
            }
            else{
                dropdownMenu.setVisible(true);
                dropdownMenu.setManaged(true);
            }
        });
    }

    public void addToCart(Product product, int quantity){
        boolean productExists = shoppingCart.productExists(product);
        if(productExists){      // if product already exists in cart, no need to add new product, just update the quantity
            shoppingCart.updateQuantityOfSameProduct(product, quantity);
            updateCartItemsNumberLabel(shoppingCart.getCartItems().size());
        }
        else{
            shoppingCart.addToCart(new CartItem(shoppingCart.getId(), product, quantity));
        }

        updateCartItemsNumberLabel(shoppingCart.getCartItems().size());
        updateCartPriceLabel(shoppingCart.getGrandTotal());
    }

    private void updateCartPriceLabel(BigDecimal price){
        cartPriceLabel.setText(price.toPlainString());
    }

    private void updateCartItemsNumberLabel(int number){
        numberOfCartItemsLabel.setText(String.valueOf(number));
    }

    int getCustomerId(){
        return customer.getId();
    }

    //endregion


    //region FXML methods
    @FXML
    public void hideSideBar(Event event) {
        double sideBarWidth = sideBarContainerVBox.getWidth();
        List<TranslateTransition> transitions = new ArrayList<>();  //0=sideBar, 1=title, 2=productContainer
        transitions.add(new TranslateTransition(Duration.millis(400), sideBarContainerVBox));
        transitions.add(new TranslateTransition(Duration.millis(400), titleContainerHBox));
        transitions.add(new TranslateTransition(Duration.millis(400), productContainerAnchorPane));


        TranslateTransition slideTransition = new TranslateTransition();
        slideTransition.setDuration(Duration.seconds(0.4));
        slideTransition.setNode(sideBarContainerVBox);
        if(isSideBarHidden){    // opening the sidebar
            for(int i = 0; i < transitions.size(); i++){
                TranslateTransition transition = transitions.get(i);
                if(i == 0){
                    transition.setToX(0);
                    sideBarContainerVBox.setTranslateX(-sideBarWidth);
                    animateSideBarIcon(false);

                    transition.setOnFinished((ActionEvent e)-> {
                        isSideBarHidden = false;
                    });
                }
                else if(i == 1){
                    // when the sidebar shows, the main content should contract accordingly

                    animateAnchor(new SimpleDoubleProperty(0), 10, titleContainerHBox);
                }
                else{
                    animateAnchor(new SimpleDoubleProperty(50), 10, productContainerAnchorPane);
                }
            }


        }
        else{                   // closing the sidebar
            for(int i = 0; i < transitions.size(); i++){
                TranslateTransition transition = transitions.get(i);
                if(i == 0){
                    transition.setToX(-sideBarWidth);
                    sideBarContainerVBox.setTranslateX(0);
                    animateSideBarIcon(true);

                    transition.setOnFinished((ActionEvent e)-> {
                        isSideBarHidden = true;
                    });
                }
                else if(i == 1){
                    // when the sidebar shows, the main content should contract accordingly

                    animateAnchor(new SimpleDoubleProperty(50), -sideBarWidth + 10, titleContainerHBox);
                }
                else{
                    animateAnchor(new SimpleDoubleProperty(50), -sideBarWidth + 10, productContainerAnchorPane);
                    //AnchorPane.setLeftAnchor(productContainerAnchorPane, -sideBarWidth + 10);
                }
            }

        }

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(transitions);
        parallelTransition.play();
    }

    @FXML
    public void showShoppingCartWindowAction(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FxmlPaths.CUSTOMER_SHOPPING_CART_FXML));
        fxmlLoader.setController(new ShoppingCartBuyController(shoppingCart, customer, this));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Shopping Cart");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResource(ImagePaths.APP_ICON).toExternalForm()));
        stage.show();
    }

    @FXML
    public void showAccountDetailsAction(ActionEvent actionEvent) {
        setMainContentAnchorPane(FxmlPaths.CUSTOMER_ACCOUNT_FXML);
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

    // =================================================================================================================

    @FXML
    public void showHomeAllProductsAction(ActionEvent actionEvent) {
        fillProductList(productService::getProducts);
        fillProductContainerGridPane();
    }

    @FXML
    public void showAllPcComponentsAction(ActionEvent actionEvent) {
        fillProductList(productService::getPcComponentsProducts);
        fillProductContainerGridPane();
    }

    @FXML
    public void showAllProcessorsAction(ActionEvent actionEvent) {
        fillProductList(productService::getCpuProducts);
        fillProductContainerGridPane();
    }

    @FXML
    public void showAllGraphicsCardsAction(ActionEvent actionEvent) {
        fillProductList(productService::getGpuProducts);
        fillProductContainerGridPane();
    }

    @FXML
    public void showAllRamAction(ActionEvent actionEvent) {
        fillProductList(productService::getRamProducts);
        fillProductContainerGridPane();

    }

    @FXML
    public void showAllStorageAction(ActionEvent actionEvent) {
        fillProductList(productService::getStorageProducts);
        fillProductContainerGridPane();

    }

    @FXML
    public void showAllMotherboardsAction(ActionEvent actionEvent) {
        fillProductList(productService::getMotherboardProducts);
        fillProductContainerGridPane();
    }

    @FXML
    public void showAllPcPeripheralsAction(ActionEvent actionEvent) {
        fillProductList(productService::getPcPeripheralsProducts);
        fillProductContainerGridPane();
    }

    @FXML
    public void showAllKeyboardsAction(ActionEvent actionEvent) {
        fillProductList(productService::getKeyboardProducts);
        fillProductContainerGridPane();
    }

    @FXML
    public void showAllMiceAction(ActionEvent actionEvent) {
        fillProductList(productService::getMiceProducts);
        fillProductContainerGridPane();
    }

    @FXML
    public void showAllHeadphonesAction(ActionEvent actionEvent) {
        fillProductList(productService::getHeadphonesProducts);
        fillProductContainerGridPane();
    }

    @FXML
    public void showAllConsolesAction(ActionEvent actionEvent) {
        fillProductList(productService::getConsolesProducts);
        fillProductContainerGridPane();
    }

    @FXML
    public void showAllPlaystationAction(ActionEvent actionEvent) {
        fillProductList(productService::getPlaystationProducts);
        fillProductContainerGridPane();
    }

    @FXML
    public void showAllXboxAction(ActionEvent actionEvent) {
        fillProductList(productService::getXboxProducts);
        fillProductContainerGridPane();
    }

    @FXML
    public void showAllGamesAction(ActionEvent actionEvent) {
        fillProductList(productService::getGamesProducts);
        fillProductContainerGridPane();
    }

    @FXML
    public void showAllCablesAction(ActionEvent actionEvent) {
        fillProductList(productService::getCablesAndAdaptersProducts);
        fillProductContainerGridPane();
    }



    //endregion
}
