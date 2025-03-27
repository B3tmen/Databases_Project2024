package org.unibl.etf.controllers.employee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.unibl.etf.database.dao.implementations.DiscountDAOImpl;
import org.unibl.etf.model.purchases.Discount;
import org.unibl.etf.model.users.Employee;
import org.unibl.etf.model.users.User;
import org.unibl.etf.service.DiscountService;
import org.unibl.etf.util.AlertShower;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EmployeeAddDiscountController implements Initializable {
    DiscountService discountService;
    private Discount discount;
    private Object controllerClass;
    private Employee employee;

    @FXML
    private TextField discountPercentageTextField;
    @FXML
    private DatePicker validToDatePicker;
    @FXML
    private DatePicker validFromDatePicker;
    @FXML
    private TextField discountIdTextField;
    @FXML
    private Label discountIdLabel;

    public EmployeeAddDiscountController(Discount discount, User user, Object controllerClass) {
        this.employee = (Employee) user;
        this.discount = discount;
        this.controllerClass = controllerClass;
        this.discountService = new DiscountService(new DiscountDAOImpl());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (discount != null && controllerClass instanceof EmployeeUpdateProductController) {           // Update operation called
            // setting initial values
            discountIdTextField.setText(String.valueOf(discount.getId()));
            discountPercentageTextField.setText(discount.getPercentage().toString());
            validFromDatePicker.setValue(discount.getDateFrom().toLocalDate());
            validToDatePicker.setValue(discount.getDateTo().toLocalDate());

            discount = null;
        }
        else if(discount == null && controllerClass instanceof EmployeeUpdateProductController){
            discountIdLabel.setVisible(false);
            discountIdTextField.setVisible(false);
        }
        else if(discount == null && controllerClass instanceof EmployeeAddProductController) {
            discountIdLabel.setVisible(false);
            discountIdTextField.setVisible(false);
        }

        setupDatePickers();
    }

    private void setupDatePickers(){
        validFromDatePicker.setValue(LocalDate.now());
        validToDatePicker.setValue(LocalDate.now());
    }


    @FXML
    public void finishDiscountAction(ActionEvent actionEvent) {
        if(discount == null) {      // Adding operation called
            boolean isValid = checkFields();
            if(isValid) {
                BigDecimal percentage = new BigDecimal(discountPercentageTextField.getText());
                Date dateFrom = Date.valueOf(validFromDatePicker.getValue());
                Date dateTo = Date.valueOf(validToDatePicker.getValue());

                discount = new Discount(0, percentage, dateFrom, dateTo, employee.getId());

                if(controllerClass instanceof EmployeeAddProductController){
                    int discountId = discountService.addDiscount(discount);
                    if(discountId > 0){
                        discount.setId(discountId);
                        AlertShower alertShower = new AlertShower(Alert.AlertType.CONFIRMATION, "Success", "Succefully added a discount", "");
                        alertShower.showAlert();

                        ((EmployeeAddProductController) controllerClass).setDiscount(discount);
                    }


                }
                else if(controllerClass instanceof EmployeeUpdateProductController){
                    if(discountIdTextField.getText().isEmpty()){                        // Adding a discount from updating product
                        int discountId = discountService.addDiscount(discount);
                        if(discountId > 0){
                            discount.setId(discountId);

                            AlertShower alertShower = new AlertShower(Alert.AlertType.CONFIRMATION, "Success", "Succefully added a discount", "");
                            alertShower.showAlert();
                            ((EmployeeUpdateProductController) controllerClass).setDiscount(discount);
                        }
                    }
                    else{                                                               // Updating a discount from updating product
                        discount.setId(Integer.parseInt(discountIdTextField.getText()));
                        int updatedDiscounts = discountService.updateDiscount(discount);

                        if(updatedDiscounts > 0){
                            AlertShower alertShower = new AlertShower(Alert.AlertType.CONFIRMATION, "Success", "Succefully updated discount", "");
                            alertShower.showAlert();
                            ((EmployeeUpdateProductController) controllerClass).setDiscount(discount);
                        }
                    }

                }

                closeView(actionEvent);
            }
        }

    }

    private boolean checkFields(){
        boolean isValid = false;

        BigDecimal percentage = null;
        Date dateFrom = null;
        Date dateTo = null;

        try{
            percentage = new BigDecimal(discountPercentageTextField.getText());
        } catch (NumberFormatException e){
            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Incorrect data.", "Wrong format for percentage!", "Try again.");
            alertShower.showAlert();
            e.printStackTrace();
        }

        if(validFromDatePicker.getValue() != null){
            dateFrom = Date.valueOf(validFromDatePicker.getValue());
        }
        else{
            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Incorrect data.", "'From' Date not inputted!", "Try again.");
            alertShower.showAlert();
        }

        if(validToDatePicker.getValue() != null){
            dateTo = Date.valueOf(validToDatePicker.getValue());
        }
        else{
            AlertShower alertShower = new AlertShower(Alert.AlertType.ERROR, "Incorrect data.", "'To' Date not inputted!", "Try again.");
            alertShower.showAlert();
        }

        if(percentage != null && dateFrom != null && dateTo != null) {
            isValid = true;
        }

        return isValid;
    }

    private void closeView(ActionEvent actionEvent){
        Node source = (Node) actionEvent.getSource();

        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
