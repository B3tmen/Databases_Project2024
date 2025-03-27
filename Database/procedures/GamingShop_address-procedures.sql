# --- Adding a country
DELIMITER $$
CREATE PROCEDURE usp_AddCountry(IN p_name VARCHAR(45))
BEGIN
	DECLARE v_country_id INT;
    
    -- Checking if the country already exists
    SELECT country_id INTO v_country_id
    FROM country WHERE name = p_name;
    
    -- if not, insert the new country
    IF v_country_id IS NULL THEN
		INSERT INTO country (name)
        VALUES (p_name);
        SET v_country_id = LAST_INSERT_ID();
    END IF;

	-- we return the new ID
    SELECT v_country_id AS id;
END $$
DELIMITER ;


# --- Adding a city
DELIMITER $$
CREATE PROCEDURE usp_AddCity(IN p_name VARCHAR(45), IN p_zip_code INT, IN p_country_id INT)
BEGIN
	DECLARE v_city_id INT;
    
    SELECT city_id INTO v_city_id
    FROM city
    WHERE name = p_name AND zip_code = p_zip_code AND fk_country_id = p_country_id;
    
    IF v_city_id IS NULL THEN
		INSERT INTO city (name, zip_code, fk_country_id)
		VALUES (p_name, p_zip_code, p_country_id);
        SET v_city_id = LAST_INSERT_ID();
    END IF;
    
    SELECT v_city_id AS id;
END $$
DELIMITER ;


# --- Adding an address
DELIMITER $$
CREATE PROCEDURE usp_AddAddress(IN p_address1 VARCHAR(45), IN p_address2 VARCHAR(45), IN p_city_id INT)
BEGIN
	DECLARE v_address_id INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;
    START TRANSACTION;
    
    INSERT INTO address (address_1, address_2, fk_city_id)
    VALUES (p_address1, p_address2, p_city_id);
    
    COMMIT;
    
    SET v_address_id = LAST_INSERT_ID();
    SELECT v_address_id AS id;
END $$
DELIMITER ;

