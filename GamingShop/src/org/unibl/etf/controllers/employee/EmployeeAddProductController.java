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

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class EmployeeAddProductController implements Initializable {
    private final ProductService productService = new ProductService(new ProductDAOImpl());
    private final CategoryService categoryService = new CategoryService(new CategoryDAOImpl());
    private final ProductCategoryService productCategoryService = new ProductCategoryService(new ProductCategoryDAOImpl());
    private final ManufacturerService manufacturerService = new ManufacturerService(new ManufacturerDAOImpl());

    private Discount discount = null;
    private Employee employee = null;

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
    private TextField productImageUrlTextField;
    @FXML
    private ComboBox manufacturerComboBox;
    @FXML
    private ComboBox categoryComboBox;
    @FXML
    private RadioButton addDiscountNoRadioButton;
    @FXML
    private RadioButton addDiscountYesRadioButton;
    @FXML
    private Button discountButton;

    public EmployeeAddProductController(User user){
        this.employee = (Employee) user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        discountButton.setVisible(false);

        setupManufacturerComboBox();
        setupCategoryComboBox();
        setupRadioButtons();
    }

    private void setupRadioButtons(){
        ToggleGroup group = new ToggleGroup();
        addDiscountNoRadioButton.setToggleGroup(group);
        addDiscountYesRadioButton.setToggleGroup(group);

        addDiscountNoRadioButton.setOnAction(e -> {
            discountButton.setVisible(false);
        });
        addDiscountYesRadioButton.setOnAction(e -> {
            discountButton.setVisible(true);
        });
    }

    private void setupCategoryComboBox() {
        List<Category> categories = null;
        try {
            categories = categoryService.getAllCategories();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(categories.size());
        for (Category category : categories) {
            if(category.getParentCategoryId() != 0){
                categoryComboBox.getItems().add(category.getId() + "_" + category.getName());
            }

        }
    }

    private void setupManufacturerComboBox(){
        List<Manufacturer> manufacturers = null;
        try {
            manufacturers = manufacturerService.getAllManufacturers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(manufacturers.size());
        for (Manufacturer manufacturer : manufacturers) {
            manufacturerComboBox.getItems().add(manufacturer.getId() + "_" + manufacturer.getName());
        }
    }

    public void setDiscount(Discount discount){
        this.discount = discount;

    }

    @FXML
    public void chooseImageAction(ActionEvent actionEvent) {
        File initialDirectory = new File(System.getProperty("user.dir")); //System.getProperty("user.dir")

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
    public void finishAddingProductAction(ActionEvent actionEvent) {
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
            int employeeId = employee.getId();     //TODO: change admin to employee and use getId() instead of 1

            File image = new File(imageUrl);
            if(!image.exists()){
                imageUrl = null;
            }

            Product product = new Product(0, name, price, null, quantity, description, warrantyMonths, imageUrl, discount != null ? discount.getId() : 0, employeeId, manufacturerId);

            int productId = -1;
            if(discount.getId() > 0){
                product.setDiscountId(discount.getId());
            }
            try {
                productId = productService.addProduct(product);
                if(productId != -1){
                    productCategoryService.addProductCategory(new ProductCategory(productId, categoryId));
                    AlertShower alertShower = new AlertShower(Alert.AlertType.CONFIRMATION, "Success", "Succesfully added a Product", "");
                    alertShower.showAlert();

                    exitView(actionEvent);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    private void exitView(ActionEvent actionEvent){
        Node node = (Node) actionEvent.getSource();     // closing the current stage/window
        Stage thisStage = (Stage) node.getScene().getWindow();
        thisStage.close();
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

    @FXML
    public void addDiscountAction(ActionEvent actionEvent) {
        FxmlViewManager viewManager = new FxmlViewManager(FxmlPaths.EMPLOYEE_ADD_DISCOUNT_VIEW, "Add a discount", new EmployeeAddDiscountController(discount, employee, this));
        viewManager.showView();
    }
}