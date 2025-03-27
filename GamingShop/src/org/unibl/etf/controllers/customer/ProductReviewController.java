package org.unibl.etf.controllers.customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;
import org.unibl.etf.database.dao.implementations.ReviewDAOImpl;
import org.unibl.etf.model.enums.ReviewScoreEnum;
import org.unibl.etf.model.product.Review;
import org.unibl.etf.service.ReviewService;
import org.unibl.etf.util.AlertShower;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductReviewController implements Initializable {

    private int productId;
    private int customerId;

    private ReviewService reviewService = new ReviewService(new ReviewDAOImpl());
    private ProductDetailsController productDetailsController;

    @FXML
    private TextField reviewTitleTextField;
    @FXML
    private Rating reviewScoreRating;
    @FXML
    private TextArea reviewCommentTextArea;

    public ProductReviewController(int productId, int customerId, ProductDetailsController controller){
        this.productId = productId;
        this.customerId = customerId;
        this.productDetailsController = controller;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void finishReviewAction(ActionEvent actionEvent) {
        String reviewTitle = reviewTitleTextField.getText();
        if(!reviewTitle.isEmpty()){
            int score = (int) reviewScoreRating.getRating();
            ReviewScoreEnum reviewScoreEnum = ReviewScoreEnum.ZERO;
            String comment = reviewCommentTextArea.getText();

            switch (score){
                case 1 -> reviewScoreEnum = ReviewScoreEnum.ONE;
                case 2 -> reviewScoreEnum = ReviewScoreEnum.TWO;
                case 3 -> reviewScoreEnum = ReviewScoreEnum.THREE;
                case 4 -> reviewScoreEnum = ReviewScoreEnum.FOUR;
                case 5 -> reviewScoreEnum = ReviewScoreEnum.FIVE;
            }

            Review review = new Review(0, reviewTitle, reviewScoreEnum, BigDecimal.valueOf(0), comment, productId, customerId);

            AlertShower alertShower = new AlertShower(Alert.AlertType.CONFIRMATION, "Success", "Successfully left a review", "");
            alertShower.showAlert();
            reviewService.addReview(review);
            productDetailsController.refreshReviewTableView();


            Node node = (Node) actionEvent.getSource();     // closing the current stage/review window
            Stage thisStage = (Stage) node.getScene().getWindow();
            thisStage.close();
        }
        else{
            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Error", "Your review must at least have a title", "Please enter a title");
            alertShower.showAlert();
        }
    }

}
