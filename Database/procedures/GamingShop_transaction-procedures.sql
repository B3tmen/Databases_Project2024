# --- Adding shopping cart
DELIMITER $$
CREATE PROCEDURE usp_AddShoppingCart(IN p_customer_id INT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;

    START TRANSACTION;

    INSERT INTO `shopping_cart` (grand_total, fk_customer_id) 
    VALUES (0.00, p_customer_id);

    COMMIT;
END $$
DELIMITER ;


# --- Check if a customer has a cart
DELIMITER $$
CREATE PROCEDURE usp_HasShoppingCart(IN p_customer_id INT, OUT p_has_cart BOOLEAN)
BEGIN
    DECLARE v_cart_count INT;

    SELECT COUNT(*) INTO v_cart_count
    FROM shopping_cart
    WHERE fk_customer_id = p_customer_id;

    SET p_has_cart = (v_cart_count > 0);
END $$
DELIMITER ;

# --- Getting a cart by customer ID
DELIMITER $$
CREATE PROCEDURE usp_GetShoppingCartByCustomerId(IN p_customer_id INT)
BEGIN
	SELECT cart_id, grand_total, fk_customer_id
    FROM shopping_cart
    WHERE fk_customer_id = p_customer_id;
END $$
DELIMITER ;


# --- Adding cart item
DELIMITER $$
CREATE PROCEDURE usp_AddCartItem(
    IN p_shopping_cart_id INT,
    IN p_product_id INT,
    IN p_price DECIMAL(6, 2),
    IN p_quantity INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;

    START TRANSACTION;

    INSERT INTO `cart_item` (fk_shopping_cart_id, fk_product_id, price, quantity) 
    VALUES (p_shopping_cart_id, p_product_id, p_price, p_quantity);

    COMMIT;
END $$
DELIMITER ;

# --- Deleting cart item by cart and product ID
DELIMITER $$
CREATE PROCEDURE usp_DeleteCartItem(IN p_fk_shopping_cart_id INT, IN p_fk_product_id INT)
BEGIN
	-- Check if the cart item exists
    IF EXISTS (
        SELECT 1
        FROM cart_item
        WHERE fk_shopping_cart_id = p_fk_shopping_cart_id AND fk_product_id = p_fk_product_id
    ) 
    THEN
        DELETE FROM cart_item
        WHERE fk_shopping_cart_id = p_fk_shopping_cart_id AND fk_product_id = p_fk_product_id;
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cart item with the specified IDs does not exist.';
    END IF;
    
END $$
DELIMITER ;



# --- Adding an order
DELIMITER $$
CREATE PROCEDURE usp_AddOrder(
	IN p_shipping DECIMAL(6,2), 
	IN p_order_date DATE, 
	IN p_fk_order_status_status_id INT, 
	IN p_fk_customer_id INT
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;
    
    START TRANSACTION;
    
    INSERT INTO `order`(shipping, order_date, fk_order_status_status_id, fk_customer_id)
    VALUES (p_shipping, p_order_date, p_fk_order_status_status_id, p_fk_customer_id);
    
    COMMIT;
    
    SELECT LAST_INSERT_ID();
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE usp_DeleteOrderById (IN p_order_id INT)
BEGIN
	CALL usp_DeleteOrderItem(p_order_id);
    
    DELETE FROM `order` WHERE order_id = p_order_id;
    
END $$
DELIMITER ;

# --- Setting the order status of the order
DELIMITER $$
CREATE PROCEDURE usp_SetOrderStatus(
    IN p_order_id INT,
    IN p_status_id INT
)
BEGIN
    UPDATE `order`
    SET fk_order_status_status_id = p_status_id
    WHERE order_id = p_order_id;
END $$
DELIMITER ;


# --- Adding order item
DELIMITER $$
CREATE PROCEDURE usp_AddOrderItem(
	IN p_fk_order_id INT,
    IN p_fk_product_id INT,
    IN p_price DECIMAL(6, 2),
    IN p_quantity INT
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;
    
    START TRANSACTION;
    
    INSERT INTO `order_item`(fk_order_id, fk_product_id, price, quantity)
    VALUES (p_fk_order_id, p_fk_product_id, p_price, p_quantity);
    
    COMMIT;
END $$
DELIMITER ;

# --- Getting order item(s) by order ID
DELIMITER $$
CREATE PROCEDURE usp_GetOrderItemsByOrderId(IN p_fk_order_id INT)
BEGIN
    SELECT fk_order_id, fk_product_id, price, quantity, sub_total
    FROM order_item oi
    INNER JOIN `order` o ON o.order_id = fk_order_id
    WHERE fk_order_id = p_fk_order_id;
END $$
DELIMITER ;

# --- Deleting order item
DELIMITER $$
CREATE PROCEDURE usp_DeleteOrderItem (IN p_fk_order_id INT)
BEGIN
	-- Delete from order_item
    DELETE FROM order_item WHERE fk_order_id = p_fk_order_id;

END $$
DELIMITER ;

# --- Adding a status to the order
DELIMITER $$
CREATE PROCEDURE usp_AddOrderStatus(
	IN p_status ENUM('pending', 'completed', 'shipped', 'cancelled')
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;
    
    START TRANSACTION;
    
    INSERT INTO `order_status`(status)
    VALUES (p_status);
    
    COMMIT;
END $$
DELIMITER ;


# --- Getting order status by name
DELIMITER $$
CREATE PROCEDURE usp_GetOrderStatusByName(IN p_status ENUM('pending', 'completed', 'shipped', 'cancelled'))
BEGIN
    SELECT * FROM order_status WHERE status = status_name;
END $$
DELIMITER ;


# --- Adding a Receipt
DELIMITER $$
CREATE PROCEDURE usp_AddReceipt(IN p_date_issued DATETIME, IN p_fk_order_id INT, IN p_grand_total DECIMAL(6, 2))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;

    START TRANSACTION;

    INSERT INTO `receipt` (date_issued, fk_order_id, grand_total) 
    VALUES (p_date_issued, p_fk_order_id, p_grand_total);

    COMMIT;
    
    SELECT LAST_INSERT_ID();
END $$
DELIMITER ;


# --- Adding receipt item
DELIMITER $$
CREATE PROCEDURE usp_AddReceiptItem(
	IN p_fk_receipt_id INT,
    IN p_fk_product_id INT,
    IN p_price DECIMAL(6, 2),
    IN p_quantity INT
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;
    
    START TRANSACTION;
    
    INSERT INTO `receipt_item`(fk_receipt_id, fk_product_id, price, quantity)
    VALUES (p_fk_receipt_id, p_fk_product_id, p_price, p_quantity);
    
    COMMIT;
END $$
DELIMITER ;