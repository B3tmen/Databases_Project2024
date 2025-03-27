package org.unibl.etf.controllers.customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;
import org.unibl.etf.database.dao.implementations.DiscountDAOImpl;
import org.unibl.etf.database.dao.implementations.ReviewDAOImpl;
import org.unibl.etf.database.dao.implementations.ShoppingCartDAOImpl;
import org.unibl.etf.model.product.Product;
import org.unibl.etf.model.product.Review;
import org.unibl.etf.model.purchases.Discount;
import org.unibl.etf.model.purchases.ShoppingCart;
import org.unibl.etf.service.DiscountService;
import org.unibl.etf.service.ReviewService;
import org.unibl.etf.service.ShoppingCartService;
import org.unibl.etf.util.FxmlPaths;
import org.unibl.etf.util.ImagePaths;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ProductCardController implements Initializable {
    private static final String CURRENCY = "BAM";

    private CustomerMainShopController customerMainShopController;
    private Product product;
    private DiscountService discountService;
    private ReviewService reviewService;

    @FXML
    private AnchorPane anchorPaneCardContainer;
    @FXML
    private Button addToCartButton;
    @FXML
    private Button viewProductButton;
    @FXML
    private Label productPriceLabel;
    @FXML
    private Label productNameLabel;
    @FXML
    private ImageView productImageView;
    private Label productDiscountLabel;
    @FXML
    private HBox priceLabelHolderHBox;
    @FXML
    private Rating reviewRating;


    public ProductCardController(CustomerMainShopController controller, Product product){
        this.customerMainShopController = controller;
        this.product = product;
        this.discountService = new DiscountService(new DiscountDAOImpl());
        this.reviewService = new ReviewService(new ReviewDAOImpl());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setData();
    }

    public void setData(){
        productPriceLabel.setText(product.getPrice() + " " + CURRENCY);
        productNameLabel.setText(product.getName());
        productImageView.setImage(new Image(product.getImageUrl()));

        if(product.getDiscountId() != 0){
            Discount discount = discountService.getDiscountById(product.getDiscountId());
            Date todayDate = Date.valueOf(LocalDate.now());
            if(validDiscountDates(discount, todayDate)){      // If the discount is valid to today, we can hint the customer that the product has the discount
                productDiscountLabel = new Label("-" + discount.getPercentage() + " " + "%");
                productDiscountLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 22px; -fx-text-fill: red");
                priceLabelHolderHBox.getChildren().add(productDiscountLabel);
            }

        }

        Review review = reviewService.getByProductId(product.getId());
        double scoreNumber = 0.0;
        if(review != null){
            BigDecimal totalScore = review.getTotalScore();
            scoreNumber = totalScore.doubleValue();
        }

        reviewRating.setRating(scoreNumber);
    }

    private boolean validDiscountDates(Discount discount, Date todayDate){
        return todayDate.compareTo(discount.getDateFrom()) >= 0 && todayDate.compareTo(discount.getDateTo()) <= 0;
    }

    @FXML
    public void addProductToCartAction(ActionEvent actionEvent) {
        customerMainShopController.addToCart(product, 1);
    }

    @FXML
    public void viewProductDetailsAction(ActionEvent actionEvent) {
        //Parent content = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FxmlPaths.PRODUCT_DETAILS_FXML));
            fxmlLoader.setController(new ProductDetailsController(product, customerMainShopController));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Product details");
            stage.getIcons().add(new Image(getClass().getResource(ImagePaths.APP_ICON).toExternalForm()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
