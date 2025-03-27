# --- Adding a Product
DELIMITER $$
CREATE PROCEDURE usp_AddProduct(
	IN p_name VARCHAR(45), 
    IN p_price DECIMAL(6, 2), 
    IN p_quantity_available INT, 
    IN p_description TEXT(4000), 
    IN p_warranty_months INT,
    IN p_image_url VARCHAR(300),
    IN p_fk_discount_id INT, 
    IN p_fk_employee_id INT,
    IN p_fk_manufacturer_id INT
)
BEGIN 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;
    
    START TRANSACTION;
	
	INSERT INTO `product` (name, price, quantity_available, description, warranty_months, image_url, fk_discount_id, fk_employee_id, fk_manufacturer_id)
    VALUES (p_name, p_price, p_quantity_available, p_description, p_warranty_months, p_image_url, p_fk_discount_id, p_fk_employee_id, p_fk_manufacturer_id);
    
    COMMIT;
	SELECT LAST_INSERT_ID() AS product_id;
END $$
DELIMITER ;


# --- Adding a manufacturer
DELIMITER $$
CREATE PROCEDURE usp_AddManufacturer(IN p_name VARCHAR(45))
BEGIN 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;
    
    START TRANSACTION;
	
	INSERT INTO `manufacturer` (name)
    VALUES (p_name);
    
    COMMIT;
	
END $$
DELIMITER ;


# --- Adding a discount
DELIMITER $$
CREATE PROCEDURE usp_AddDiscount(IN p_percentage DECIMAL(5, 2), IN p_date_from DATE, IN p_date_to DATE, IN p_fk_employee_id INT)	#YYYY-MM-DD for dates
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;
    
    START TRANSACTION;
    
    INSERT INTO `discount` (percentage, date_from, date_to, fk_employee_id)
    VALUES (p_percentage, p_date_from, p_date_to, p_fk_employee_id);
    
    COMMIT;
	
    SELECT LAST_INSERT_ID();
END $$
DELIMITER ;


# --- Adding a product_category(M:M junction table)
DELIMITER $$
CREATE PROCEDURE usp_AddProductCategoryJunction(IN p_product_id INT, IN p_category_id INT, OUT out_fk_product_id INT)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;
	
    START TRANSACTION;
    
    INSERT INTO `product_category` (fk_product_id, fk_category_id)
    VALUES (p_product_id, p_category_id);
    
    SET out_fk_product_id = p_product_id;
    
    COMMIT;
END $$
DELIMITER ;

# --- Adding a category
DELIMITER $$
CREATE PROCEDURE usp_AddParentCategory(IN p_name VARCHAR(45))
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;
	
    START TRANSACTION;
    
    INSERT INTO `category` (name)
    VALUES (p_name);
    
    COMMIT;

END $$
DELIMITER ;

# --- Adding a sub category
DELIMITER $$
CREATE PROCEDURE usp_AddSubCategory(IN p_name VARCHAR(45), IN p_fk_parent_category_id INT)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;
	
    START TRANSACTION;
    
    INSERT INTO `category` (name, fk_parent_category_id)
    VALUES (p_name, p_fk_parent_category_id);
    
    COMMIT;
END $$
DELIMITER ;

#ALTER TABLE `review` AUTO_INCREMENT = 1;
# --- Adding a review
DELIMITER $$
CREATE PROCEDURE usp_AddReview(IN p_title VARCHAR(45), IN p_score ENUM('0', '1', '2', '3', '4', '5'), IN p_comment TEXT(5000), IN p_fk_product_id INT, IN p_fk_customer_id INT)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;
    
    START TRANSACTION;
    
    INSERT INTO `review` (title, score, comment, fk_product_id, fk_customer_id)
    VALUES (p_title, p_score, p_comment, p_fk_product_id, p_fk_customer_id);
    
    COMMIT;
    
    CALL usp_UpdateReviewTotalScore(p_fk_product_id);
END $$
DELIMITER ;
#ALTER TABLE `review` AUTO_INCREMENT = 1;

DELIMITER $$
CREATE PROCEDURE usp_UpdateReviewTotalScore(IN p_product_id INT)
BEGIN
    DECLARE avg_score DECIMAL(3,2);

    -- Calculate the arithmetic mean of scores for the specific product
    SELECT AVG(CASE score
                WHEN '0' THEN 0
                WHEN '1' THEN 1
                WHEN '2' THEN 2
                WHEN '3' THEN 3
                WHEN '4' THEN 4
                WHEN '5' THEN 5
               END)
    INTO avg_score
    FROM review
    WHERE fk_product_id = p_product_id;

    -- Update the total_score field for all reviews of the product
    UPDATE review
    SET total_score = avg_score
    WHERE fk_product_id = p_product_id;
END $$
DELIMITER ;
# ============================================================================================

DELIMITER $$
CREATE PROCEDURE usp_GetCategoryByProductId(IN p_product_id INT)
BEGIN

    SELECT c.category_id, c.name, c.fk_parent_category_id
    FROM product p
    JOIN product_category pc ON p.product_id = pc.fk_product_id
    JOIN category c ON pc.fk_category_id = c.category_id
    WHERE pc.fk_product_id = p_product_id;

END $$
DELIMITER ;

# ============================================================================================
DELIMITER $$
CREATE PROCEDURE usp_DeleteProductById (IN p_product_id INT)
BEGIN
	-- Delete from receipt_item
    DELETE FROM receipt_item WHERE fk_product_id = p_product_id;
	-- Delete from order_item
    DELETE FROM order_item WHERE fk_product_id = p_product_id;
	-- Delete from cart_item
    DELETE FROM cart_item WHERE fk_product_id = p_product_id;
    -- Delete from review
    DELETE FROM review WHERE fk_product_id = p_product_id;
    -- Delete from product_category
    DELETE FROM product_category WHERE fk_product_id = p_product_id;
    
    -- Finally, delete from product
    DELETE FROM product WHERE product_id = p_product_id;
END $$
DELIMITER ;

# ============================================================================================
DELIMITER $$
CREATE PROCEDURE usp_UpdateProduct(
	IN p_product_id INT,
	IN p_name VARCHAR(45), 
    IN p_price DECIMAL(6, 2), 
    IN p_quantity_available INT, 
    IN p_description TEXT(4000), 
    IN p_warranty_months INT, 
    IN p_image_url VARCHAR(300),
    IN p_fk_discount_id INT,
    IN p_fk_employee_id INT,
    IN p_fk_manufacturer_id INT
)
BEGIN
	UPDATE product 
    SET
		name = p_name,
        price = p_price,
        quantity_available = p_quantity_available,
        description = p_description,
        warranty_months = p_warranty_months,
        image_url = p_image_url,
        fk_discount_id = p_fk_discount_id,
        fk_employee_id = p_fk_employee_id,
        fk_manufacturer_id = p_fk_manufacturer_id
	WHERE product_id = p_product_id;
END $$
DELIMITER ;


# --- updating discount
DELIMITER $$
CREATE PROCEDURE usp_UpdateDiscount(
	IN p_discount_id INT,
	IN p_percentage DECIMAL(5, 2), 
    IN p_date_from DATE, 
    IN p_date_to DATE
)
BEGIN
	UPDATE discount
    SET
		discount_id = p_discount_id,
        percentage = p_percentage,
        date_from = p_date_from,
        date_to = p_date_to
	WHERE discount_id = p_discount_id;
END $$
DELIMITER ;

