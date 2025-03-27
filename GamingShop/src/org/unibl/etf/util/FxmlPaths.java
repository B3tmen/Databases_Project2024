package org.unibl.etf.util;

public class FxmlPaths {

    private static final String c = "/";  // used for getClass().getResource("..../....")

    //general folder paths
    private static final String VIEW_FOLDER =  c + "view" + c;
    private static final String LOGIN_FOLDER = VIEW_FOLDER + "login" + c;
    private static final String SHOP_FOLDER = VIEW_FOLDER + "shop" + c;
    private static final String ADMIN_FOLDER = SHOP_FOLDER + "admin" + c;
    private static final String CUSTOMER_FOLDER = SHOP_FOLDER + "customer" + c;
    private static final String EMPLOYEE_FOLDER = SHOP_FOLDER + "employee" + c;


    //login/register
    public static final String USER_LOGIN_FXML = LOGIN_FOLDER + "UserLoginView.fxml";
    public static final String USER_REGISTER_FXML = LOGIN_FOLDER + "UserRegisterView.fxml";


    // admin
    public static final String ADMIN_MAIN_VIEW = ADMIN_FOLDER + "AdminMainView.fxml";
    public static final String ADMIN_USERS_VIEW = ADMIN_FOLDER + "AdminUsersView.fxml";
    public static final String ADMIN_ACCOUNT_VIEW = ADMIN_FOLDER + "AdminAccountView.fxml";
    public static final String ADMIN_ADD_USER_VIEW = ADMIN_FOLDER + "AdminAddAccountView.fxml";
    public static final String ADMIN_UPDATE_USER_VIEW = ADMIN_FOLDER + "AdminUpdateAccountView.fxml";


    // employee
    public static final String EMPLOYEE_MAIN_VIEW = EMPLOYEE_FOLDER + "EmployeeMainView.fxml";
    public static final String EMPLOYEE_PRODUCTS_VIEW = EMPLOYEE_FOLDER + "ProductsView.fxml";
    public static final String EMPLOYEE_ORDERS_VIEW = EMPLOYEE_FOLDER + "OrdersView.fxml";
    public static final String EMPLOYEE_ORDERS_CUSTOMER_DETAILS_VIEW = EMPLOYEE_FOLDER + "OrderCustomerDetailsView.fxml";
    public static final String EMPLOYEE_ORDERS_PRODUCT_DETAILS_VIEW = EMPLOYEE_FOLDER + "OrderProductDetailsView.fxml";
    public static final String EMPLOYEE_ADD_PRODUCT_VIEW = EMPLOYEE_FOLDER + "AddProductView.fxml";
    public static final String EMPLOYEE_ADD_DISCOUNT_VIEW = EMPLOYEE_FOLDER + "AddDiscountView.fxml";
    public static final String EMPLOYEE_UPDATE_PRODUCT_VIEW = EMPLOYEE_FOLDER + "UpdateProductView.fxml";
    public static final String EMPLOYEE_ACCOUNT_VIEW = EMPLOYEE_FOLDER + "EmployeeAccountView.fxml";


    // customer
    public static final String CUSTOMER_MAIN_FXML = CUSTOMER_FOLDER + "CustomerMainView.fxml";
    public static final String CUSTOMER_ACCOUNT_FXML = CUSTOMER_FOLDER + "CustomerAccountView.fxml";
    public static final String PRODUCT_DETAILS_FXML = CUSTOMER_FOLDER + "ProductDetailsView.fxml";
    public static final String PRODUCT_CARD_FXML = CUSTOMER_FOLDER + "ProductCardView.fxml";
    public static final String CUSTOMER_SHOPPING_CART_FXML = CUSTOMER_FOLDER + "ShoppingCartBuyView.fxml";
    public static final String CUSTOMER_ADD_PRODUCT_REVIEW_FXML = CUSTOMER_FOLDER + "AddProductReviewView.fxml";
}
