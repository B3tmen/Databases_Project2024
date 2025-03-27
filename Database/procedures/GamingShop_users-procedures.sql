# ======================================================== ADD/CREATION PROCEDURES ==========================================================
# --- Adding user
DELIMITER $$
CREATE PROCEDURE usp_AddUser(
	IN p_username VARCHAR(20), 
    IN p_password_hash VARCHAR(100),
    IN p_first_name VARCHAR(30),
    IN p_last_name VARCHAR(30),
    IN p_email VARCHAR(100),
    IN p_type ENUM('Customer', 'Employee', 'Administrator'),
    IN p_is_active TINYINT(1)
)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;
    
    START TRANSACTION;
	
    INSERT INTO `user` (username, password_hash, first_name, last_name, email, type_, is_active)
    VALUES (p_username, p_password_hash, p_first_name, p_last_name, p_email, p_type, p_is_active);
    
    COMMIT;
    SELECT @user_id;
END $$
DELIMITER ;


# --- Adding Customer
DELIMITER $$
CREATE PROCEDURE usp_AddCustomer(
    IN p_username VARCHAR(20),
    IN p_password_hash VARCHAR(100),
    IN p_first_name VARCHAR(30),
    IN p_last_name VARCHAR(30),
    IN p_email VARCHAR(100),
    IN p_is_active TINYINT(1),
    IN p_address_id INT,
    IN p_phone_number VARCHAR(45)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;

    START TRANSACTION;
    
    -- First inserting into the 'user' table
    CALL usp_AddUser(p_username, p_password_hash, p_first_name, p_last_name, p_email,
        'Customer',
        p_is_active
    );
    SET @customer_id = LAST_INSERT_ID();

    -- Inserting the customer record
    INSERT INTO `customer` (fk_user_id, fk_address_id, phone_number) 
    VALUES (@customer_id, p_address_id, p_phone_number);

    COMMIT;
    
    SELECT @customer_id;
END $$
DELIMITER ;


# --- Adding employee
DELIMITER $$
CREATE PROCEDURE usp_AddEmployee(
    IN p_username VARCHAR(20),
    IN p_password_hash VARCHAR(100),
    IN p_first_name VARCHAR(30),
    IN p_last_name VARCHAR(30),
    IN p_email VARCHAR(100),
    IN p_is_active TINYINT(1),
    IN p_position VARCHAR(45),
    IN p_salary DECIMAL(6, 2)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;

    START TRANSACTION;

    -- First inserting into the 'user' table
    CALL usp_AddUser(p_username, p_password_hash, p_first_name, p_last_name, p_email,
        'Employee',
        p_is_active
    );
	SET @employee_id = LAST_INSERT_ID();

    -- Inserting the employee record
    INSERT INTO `employee` (fk_user_id, position, salary) 
    VALUES (@employee_id, p_position, p_salary);
    
    COMMIT;
    
    SELECT @employee_id;
END $$
DELIMITER ;


# --- Adding Administrator
DELIMITER $$
CREATE PROCEDURE usp_AddAdministrator(
    IN p_username VARCHAR(20),
    IN p_password_hash VARCHAR(100),
    IN p_first_name VARCHAR(30),
    IN p_last_name VARCHAR(30),
    IN p_email VARCHAR(100),
    IN p_is_active TINYINT(1)
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		SHOW ERRORS;
        ROLLBACK;
	END;

    START TRANSACTION;

    -- Call the AddUser procedure to add the user
    CALL usp_AddUser(p_username, p_password_hash, p_first_name, p_last_name, p_email,
        'Administrator',
        p_is_active
    );
	SET @user_id = LAST_INSERT_ID();

    -- Inserting the administrator record
    INSERT INTO administrator (fk_user_id) 
    VALUES (@user_id);

    COMMIT;
    
    SELECT @user_id;
END $$
DELIMITER ;

# ======================================================== GET/RETRIEVE PROCEDURES ==========================================================
# --- Getting the user by username and password during login
DELIMITER $$
CREATE PROCEDURE usp_GetUserByUsernameAndPassword(IN p_username VARCHAR(20), IN p_password_hash VARCHAR(100))
BEGIN
    SELECT u.user_id, u.username, u.password_hash, u.first_name, u.last_name, u.email, u.is_active, u.type_,
        CASE 
            WHEN u.type_ = 'Customer' THEN c.phone_number
            ELSE NULL 
        END AS phone_number,
        CASE 
            WHEN u.type_ = 'Customer' THEN c.fk_address_id
            ELSE NULL 
        END AS fk_address_id,
        CASE 
            WHEN u.type_ = 'Employee' THEN e.position
            ELSE NULL 
        END AS position,
        CASE 
            WHEN u.type_ = 'Employee' THEN e.salary
            ELSE NULL 
        END AS salary
    FROM user u
    LEFT JOIN customer c ON u.user_id = c.fk_user_id
    LEFT JOIN employee e ON u.user_id = e.fk_user_id
    WHERE u.username = p_username AND u.password_hash = p_password_hash;
END $$
DELIMITER ;


# --- Getting a customer by ID
DELIMITER $$
CREATE PROCEDURE usp_GetCustomerById(IN p_customer_id INT)
BEGIN
	SELECT u.user_id, u.username, u.password_hash, u.first_name, u.last_name, u.email, u.is_active, c.phone_number, c.fk_address_id
    FROM user u
    INNER JOIN customer c ON u.user_id = p_customer_id;

END $$
DELIMITER ;


# --- Getting a employee by ID
DELIMITER $$
CREATE PROCEDURE usp_GetEmployeeById(IN p_employee_id INT)
BEGIN
	SELECT u.user_id, u.username, u.password_hash, u.first_name, u.last_name, u.email, u.is_active, e.position, e.salary
    FROM user u
    INNER JOIN employee e ON u.user_id = p_employee_id;
END $$
DELIMITER ;

# ======================================================== UPDATE PROCEDURES ==========================================================
# --- Updating a User
DELIMITER $$
CREATE PROCEDURE usp_UpdateUser(
	IN p_user_id INT,
	IN p_username VARCHAR(20), 
    IN p_password_hash VARCHAR(100), 
    IN p_first_name VARCHAR(30), 
    IN p_last_name VARCHAR(30),  
    IN p_email VARCHAR(100),
    IN p_is_active TINYINT(1)
)
BEGIN
	UPDATE user 
    SET
		username = p_username,
        password_hash = p_password_hash,
        first_name = p_first_name,
        last_name = p_last_name,
        email = p_email,
        is_active = p_is_active
	WHERE user_id = p_user_id;
END $$
DELIMITER ;

# --- Updating admin
DELIMITER $$
CREATE PROCEDURE usp_UpdateAdministrator(
    IN p_user_id INT,
    IN p_username VARCHAR(20), 
    IN p_password_hash VARCHAR(100), 
    IN p_first_name VARCHAR(30), 
    IN p_last_name VARCHAR(30),  
    IN p_email VARCHAR(100),
    IN p_is_active TINYINT(1)
)
BEGIN
    -- Call usp_UpdateUser to update the user table
    CALL usp_UpdateUser(p_user_id, p_username, p_password_hash, p_first_name, p_last_name, p_email, p_is_active);
END $$
DELIMITER ;

# --- Update a customer
DELIMITER $$
CREATE PROCEDURE usp_UpdateCustomer(
    IN p_user_id INT,
    IN p_username VARCHAR(20), 
    IN p_password_hash VARCHAR(100), 
    IN p_first_name VARCHAR(30), 
    IN p_last_name VARCHAR(30),  
    IN p_email VARCHAR(100),
    IN p_is_active TINYINT(1),
    IN p_phone_number VARCHAR(45),
    IN p_fk_address_id INT
)
BEGIN
    -- Call usp_UpdateUser to update the user table
    CALL usp_UpdateUser(p_user_id, p_username, p_password_hash, p_first_name, p_last_name, p_email, p_is_active);

    -- Update customer-specific fields
    UPDATE customer 
    SET
        phone_number = p_phone_number,
        fk_address_id = p_fk_address_id
    WHERE fk_user_id = p_user_id;
END $$
DELIMITER ;

# --- Updating employee
DELIMITER $$
CREATE PROCEDURE usp_UpdateEmployee(
    IN p_user_id INT,
    IN p_username VARCHAR(20), 
    IN p_password_hash VARCHAR(100), 
    IN p_first_name VARCHAR(30), 
    IN p_last_name VARCHAR(30),  
    IN p_email VARCHAR(100),
    IN p_is_active TINYINT(1),
    IN p_position VARCHAR(45),
    IN p_salary DECIMAL(6, 2)
)
BEGIN
    -- Call usp_UpdateUser to update the user table
    CALL usp_UpdateUser(p_user_id, p_username, p_password_hash, p_first_name, p_last_name, p_email, p_is_active);

    -- Update employee-specific fields
    UPDATE employee 
    SET
        position = p_position,
        salary = p_salary
    WHERE fk_user_id = p_user_id;
END $$
DELIMITER ;

# ======================================================== DELETION/ACTIVATION PROCEDURES ==========================================================
# --- Activating/Deactivating a User
DELIMITER $$
CREATE PROCEDURE usp_SetUserActivationStatus(IN p_user_id INT, IN p_activation_value TINYINT(1))
BEGIN
    DECLARE v_user_exists INT;
    -- Check if the user exists
    SELECT COUNT(*) INTO v_user_exists FROM `user` WHERE user_id = p_user_id;

    IF v_user_exists = 0 THEN	-- User does not exist, signal an error
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'User not found by ID';
    ELSE
        UPDATE `user` SET is_active = p_activation_value WHERE user_id = p_user_id;		        -- Update the is_active field for the user
    END IF;
END $$
DELIMITER ;


# --- Getting last customer id
DELIMITER $$
CREATE PROCEDURE usp_GetLastCustomerId(OUT p_last_customer_id INT)
BEGIN
	SELECT MAX(user_id) INTO p_last_customer_id
    FROM user u 
    INNER JOIN customer c ON u.user_id = c.fk_user_id;

END $$
DELIMITER ;