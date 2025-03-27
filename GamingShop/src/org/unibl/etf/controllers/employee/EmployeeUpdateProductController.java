package org.unibl.etf.controllers.employee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.unibl.etf.database.dao.implementations.CategoryDAOImpl;
import org.unibl.etf.database.dao.implementations.ManufacturerDAOImpl;
import org.unibl.etf.database.dao.implementations.ProductCategoryDAOImpl;
import org.unibl.etf.database.dao.implementations.ProductDAOImpl;
import org.unibl.etf.model.product.Category;
import org.unibl.etf.model.product.Manufacturer;
import org.unibl.etf.model.product.Product;
import org.unibl.etf.model.product.ProductCategory;
import org.unibl.etf.model.purchases.Discount;
import org.unibl.etf.model.users.Employee;
import org.unibl.etf.model.users.User;
import org.unibl.etf.service.CategoryService;
import org.unibl.etf.service.ManufacturerService;
import org.unibl.etf.service.ProductCategoryService;
import org.unibl.etf.service.ProductService;
import org.unibl.etf.util.AlertShower;
import org.unibl.etf.util.FxmlPaths;
import org.unibl.etf.util.FxmlViewManager;
import org.unibl.etf.util.ImagePaths;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeUpdateProductController extends Object implements Initializable {
    private Product product;
    private Discount discount;
    private Employee employee;

    private final ProductService productService = new ProductService(new ProductDAOImpl());
    private final CategoryService categoryService = new CategoryService(new CategoryDAOImpl());
    private final ProductCategoryService productCategoryService = new ProductCategoryService(new ProductCategoryDAOImpl());
    private final ManufacturerService manufacturerService = new ManufacturerService(new ManufacturerDAOImpl());

    @FXML
    private ImageView productImageView;
    @FXML
    private TextField productQuantityTextField;
    @FXML
    private TextField productNameTextField;
    @FXML
    private TextField productPriceTextField;
    @FXML
    private TextArea productDescriptionTextArea;
    @FXML
    private TextField productWarrantyTextField;
    @FXML
    private Button discountButton;
    @FXML
    private TextField productImageUrlTextField;
    @FXML
    private ComboBox manufacturerComboBox;
    @FXML
    private ComboBox categoryComboBox;

    public EmployeeUpdateProductController(User user, Product product, Discount discount) {
        this.employee = (Employee) user;
        this.product = product;
        this.discount = discount;

    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTextFields();
        setupManufacturerComboBox();
        setupCategoryComboBox();
    }

    private void setupTextFields() {
        productNameTextField.setText(product.getName());
        productPriceTextField.setText(String.valueOf(product.getPrice()));
        productQuantityTextField.setText(String.valueOf(product.getQuantityAvailable()));
        productWarrantyTextField.setText(String.valueOf(product.getWarrantyMonths()));
        productDescriptionTextArea.setText(product.getDescription());
        productImageUrlTextField.setText(product.getImageUrl());
        productImageView.setImage(product.getImage().getImage());
    }

    private void setupCategoryComboBox() {
        List<Category> categories = null;
        try {
            categories = categoryService.getAllCategories();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Category category1 = null;
        try {
            category1 = categoryService.getCategoryByProductId(this.product.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int preselectedIdx = 0;
        for (Category category : categories) {
            if(category.getParentCategoryId() != 0){
                categoryComboBox.getItems().add(category.getId() + "_" + category.getName());

                if(category1 != null && category.getId() == category1.getId()){
                    preselectedIdx = categoryComboBox.getItems().size() - 1;
                }
            }
        }

        categoryComboBox.getSelectionModel().select(preselectedIdx);
    }

    private void setupManufacturerComboBox(){
        List<Manufacturer> manufacturers = null;
        try {
            manufacturers = manufacturerService.getAllManufacturers();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int preselectedIdx = 0;
        for (Manufacturer manufacturer : manufacturers) {
            manufacturerComboBox.getItems().add(manufacturer.getId() + "_" + manufacturer.getName());

            if(product.getManufacturerId() == manufacturer.getId()){
                preselectedIdx = manufacturerComboBox.getItems().size() - 1;
            }
        }

        manufacturerComboBox.getSelectionModel().select(preselectedIdx);
    }

    private boolean checkFields() {
        boolean isValid = false;

        if (productNameTextField.getText().isEmpty()) {
            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "Name field wasn't inputted! (empty)", "Try again.");
            alertShower.showAlert();
        }
        if (productPriceTextField.getText().isEmpty()) {
            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "Price field wasn't inputted! (empty)", "Try again.");
            alertShower.showAlert();
        }
        else if (productQuantityTextField.getText().isEmpty()) {
            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "Quantity wasn't inputted! (empty)", "Try again.");
            alertShower.showAlert();
        }
        else if (productDescriptionTextArea.getText().isEmpty()) {
            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "Description field wasn't inputted! (empty)", "Try again.");
            alertShower.showAlert();
        }
        else if (productWarrantyTextField.getText().isEmpty()) {
            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "Warranty field wasn't correctly inputted! (empty)", "Try again.");
            alertShower.showAlert();
        }
        else{
            String name = productNameTextField.getText();
            String description = productDescriptionTextArea.getText();
            String imageUrl = productImageUrlTextField.getText();

            BigDecimal price = null;
            try{
                price = new BigDecimal(productPriceTextField.getText());
            } catch (NumberFormatException e){
                AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "Price field not in correct format!", "Try again.");
                alertShower.showAlert();
            }

            int quantity = -1;
            try{
                quantity = Integer.parseInt(productQuantityTextField.getText());
            } catch (NumberFormatException e){
                AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "Quantity field not in correct format!", "Try again.");
                alertShower.showAlert();
            }

            int warrantyMonths = -1;
            try{
                warrantyMonths = Integer.parseInt(productWarrantyTextField.getText());
            } catch (NumberFormatException e){
                AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "Warranty field not in correct format!", "Try again.");
                alertShower.showAlert();
            }

            if (price.compareTo(new BigDecimal("0.0")) <= 0) {
                AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "Price field wasn't correctly inputted! (< 0)", "Try again.");
                alertShower.showAlert();
            }
            else if (quantity <= 0) {
                AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "Quantity wasn't correctly inputted! (need at least 1)", "Try again.");
                alertShower.showAlert();
            }
            else if (description.isEmpty()) {
                AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "Description field wasn't inputted! (empty)", "Try again.");
                alertShower.showAlert();
            }
            else if (warrantyMonths <= 0) {
                AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Data not correct.", "Warranty field wasn't correctly inputted! (need at least 1)", "Try again.");
                alertShower.showAlert();
            }
            else{
                isValid = true;
            }
        }

        return isValid;
    }

    public void setDiscount(Discount discount){
        this.discount = discount;
        this.product.setDiscountId(discount.getId());
    }

    private void closeView(ActionEvent actionEvent){
        Node source = (Node) actionEvent.getSource();

        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void chooseImageAction(ActionEvent actionEvent) {        //TODO: same as AddProduct, do DRY later
        File initialDirectory = new File(getClass().getResource(ImagePaths.IMAGES_FOLDER).toExternalForm()); //System.getProperty("user.dir")

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please Choose an Image for the Product");
        if (initialDirectory.exists() && initialDirectory.isDirectory()) {
            fileChooser.setInitialDirectory(initialDirectory);
        }

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String absolutePath = file.getAbsolutePath();
            Image image = new Image(absolutePath);

            productImageUrlTextField.setText(absolutePath);
            productImageView.setImage(image);
        }
    }

    @FXML
    public void finishUpdatingProductAction(ActionEvent actionEvent) {
        boolean isValidFields = checkFields();

        if (isValidFields) {
            String name = productNameTextField.getText();
            BigDecimal price = new BigDecimal(productPriceTextField.getText());
            int quantity = Integer.parseInt(productQuantityTextField.getText());
            String description = productDescriptionTextArea.getText();
            int warrantyMonths = Integer.parseInt(productWarrantyTextField.getText());
            String imageUrl = productImageUrlTextField.getText();

            String[] manufacturerParts = manufacturerComboBox.getValue().toString().split("_");
            String[] categoryParts = categoryComboBox.getValue().toString().split("_");
            int manufacturerId = Integer.parseInt(manufacturerParts[0]);
            int categoryId = Integer.parseInt(categoryParts[0]);
            int employeeId = employee.getId();

            File image = new File(imageUrl);
            if(!image.exists()){
                imageUrl = null;
            }

            Product product = new Product(this.product.getId(), name, price, null, quantity, description, warrantyMonths, imageUrl, discount != null ? discount.getId() : 0, employeeId, manufacturerId);

            int updatedProducts = 0;
            try {
                updatedProducts = productService.updateProduct(product);
                if(updatedProducts > 0){
                    productCategoryService.addProductCategory(new ProductCategory(product.getId(), categoryId));
                    AlertShower alertShower = new AlertShower(Alert.AlertType.CONFIRMATION, "Success", "Succesfully updated a Product", "");
                    alertShower.showAlert();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            closeView(actionEvent);
        }
    }

    @FXML
    public void viewDiscountAction(ActionEvent actionEvent) {
        if(discount == null){
            AlertShower alertShower = new AlertShower(Alert.AlertType.INFORMATION, "Attention!", "Discount was not added for this product.", "Add a discount?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> answer = alertShower.showAlert();

            if(answer.get() == ButtonType.YES){
                FxmlViewManager viewManager = new FxmlViewManager(FxmlPaths.EMPLOYEE_ADD_DISCOUNT_VIEW, "Add a discount", new EmployeeAddDiscountController(null, employee,  this));
                viewManager.showView();
            }
        }
        else {
            FxmlViewManager viewManager = new FxmlViewManager(FxmlPaths.EMPLOYEE_ADD_DISCOUNT_VIEW, "Update discount", new EmployeeAddDiscountController(discount, employee, this));
            viewManager.showView();
        }
    }

}
