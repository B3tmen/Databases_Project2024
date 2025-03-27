# =========================================== SHOPPING CART / ORDER TRIGGERS ===============================================
# --- 'Shopping cart' grand_total trigger calculation with insert of cart item
DELIMITER $$
CREATE TRIGGER trig_UpdateCartGrandTotal_after_insert
AFTER INSERT ON `cart_item` 
FOR EACH ROW
BEGIN
	-- Updating the grand total of a shopping cart
    UPDATE `shopping_cart`
    SET grand_total = grand_total + NEW.sub_total
    WHERE cart_id = NEW.fk_shopping_cart_id;
END $$
DELIMITER ;

# --- 'Shopping cart' grand_total trigger calculation with insert of cart item
DELIMITER $$
CREATE TRIGGER trig_UpdateCartGrandTotal_after_delete
AFTER DELETE ON `cart_item` 
FOR EACH ROW
BEGIN
	-- Updating the grand total of a shopping cart
    UPDATE `shopping_cart`
    SET grand_total = grand_total - OLD.sub_total
    WHERE cart_id = OLD.fk_shopping_cart_id;
END $$
DELIMITER ;



# --- Updating the grand total of an order based on sub totals of items
DELIMITER $$
CREATE TRIGGER trig_UpdateOrderGrandTotal_after_insert
AFTER INSERT ON order_item
FOR EACH ROW
BEGIN
	-- Updating the grand total of an order
    UPDATE `order`
    SET grand_total = (
        SELECT SUM(sub_total) FROM order_item
        WHERE fk_order_id = NEW.fk_order_id
    )
    WHERE order_id = NEW.fk_order_id;
END $$
DELIMITER ;


# --- Updating the grand total of a receipt
DELIMITER $$
CREATE TRIGGER trig_UpdateReceiptGrandTotal_after_insert
AFTER INSERT ON receipt_item
FOR EACH ROW
BEGIN
	-- Updating the grand total of an order
    UPDATE `receipt`
    SET grand_total = (
        SELECT SUM(sub_total) FROM receipt_item
        WHERE fk_receipt_id = NEW.fk_receipt_id
    )
    WHERE receipt_id = NEW.fk_receipt_id;
END $$
DELIMITER ;


# --- Updating the grand total of an order based on sub totals of items
DELIMITER $$
CREATE TRIGGER trig_UpdateProductQuantityAvailable_after_update
AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	DECLARE done INT DEFAULT FALSE;
	DECLARE v_product_id INT;
	DECLARE v_quantity INT;
    DECLARE cur CURSOR FOR 	-- Declare a cursor to iterate over the order items
		SELECT fk_product_id, quantity
		FROM order_item
		WHERE fk_order_id = OLD.order_id;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

	-- Check if the order status is updated to "shipped"
    IF NEW.fk_order_status_status_id = 3 THEN
        -- Open the cursor
        OPEN cur;

        -- Loop through each order item
        read_loop: LOOP
            FETCH cur INTO v_product_id, v_quantity;
            IF done THEN
                LEAVE read_loop;
            END IF;

            -- Update the product quantity in stock
            UPDATE product
            SET quantity_available = quantity_available - v_quantity
            WHERE product_id = v_product_id;
        END LOOP;

        -- Close the cursor
        CLOSE cur;
    END IF;
END $$
DELIMITER ;
#DROP TRIGGER trig_UpdateProductQuantityAvailable_after_update;

# =========================================== DISCOUNT TRIGGERS ===============================================
# --- Updating price of product on product insert with discount
DELIMITER $$
CREATE TRIGGER trig_UpdateProductPrice_before_insert
BEFORE INSERT ON `product`
FOR EACH ROW
BEGIN
	DECLARE v_discount_percentage DECIMAL(5,2);
    IF NEW.fk_discount_id IS NOT NULL THEN
        -- Fetch the discount percentage
        SELECT percentage INTO v_discount_percentage
        FROM discount
        WHERE discount_id = NEW.fk_discount_id;

        -- Calculate the new price based on the discount
        SET NEW.discounted_price = NEW.price - (NEW.price * v_discount_percentage / 100);
    END IF;
END $$
DELIMITER ;
#DROP TRIGGER trig_UpdateProductPrice_before_insert;

# --- Updating product discount_price on update of it with a discount
DELIMITER $$
CREATE TRIGGER trig_UpdateProductPrice_before_update
BEFORE UPDATE ON `product`
FOR EACH ROW
BEGIN
    DECLARE v_discount_percentage DECIMAL(5,2);

    IF NEW.fk_discount_id IS NOT NULL THEN
        -- Fetch the discount percentage
        SELECT percentage INTO v_discount_percentage
        FROM discount
        WHERE discount_id = NEW.fk_discount_id;

        -- Calculate the new price based on the discount
        SET NEW.discounted_price = NEW.price - (NEW.price * v_discount_percentage / 100);
    END IF;
END $$
DELIMITER ;

# --- updating price of product on the update of a discount
DELIMITER $$
CREATE TRIGGER trig_UpdateProductPrice_after_update
AFTER UPDATE ON `discount`
FOR EACH ROW
BEGIN
    
    -- Update the product with the new price
    UPDATE product p
	SET p.discounted_price = p.price - (p.price * NEW.percentage / 100)
    WHERE p.fk_discount_id = NEW.discount_id;
END $$
DELIMITER ;
#DROP TRIGGER trig_UpdateProductPrice_after_update;

# ============================================ REVIEW TRIGGERS ==============================================

#MySQL error happens:  it is not permitted to modify a table that is already being used (for reading or writing) by the statement that invoked the function or trigger.
-- DELIMITER $$
-- CREATE TRIGGER trig_UpdateTotalScore_after_insert
-- AFTER INSERT ON review
-- FOR EACH ROW
-- BEGIN
--     -- Call the stored procedure to update the total_score
--     CALL usp_UpdateReviewTotalScore(NEW.fk_product_id);
-- END $$
-- DELIMITER ;
#DROP TRIGGER trig_UpdateTotalScore_after_insert;

