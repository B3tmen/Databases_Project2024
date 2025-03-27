-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema BP_GamingShop
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema BP_GamingShop
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `BP_GamingShop` DEFAULT CHARACTER SET utf8 ;
USE `BP_GamingShop` ;

-- -----------------------------------------------------
-- Table `BP_GamingShop`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`user` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(20) NOT NULL,
  `password_hash` VARCHAR(100) NOT NULL,
  `first_name` VARCHAR(30) NOT NULL,
  `last_name` VARCHAR(30) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `type_` ENUM('Customer', 'Employee', 'Administrator') NULL,
  `is_active` TINYINT(1) NOT NULL,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`administrator`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`administrator` (
  `fk_user_id` INT NOT NULL,
  PRIMARY KEY (`fk_user_id`),
  INDEX `fk_ADMINISTRATOR_USER1_idx` (`fk_user_id` ASC) VISIBLE,
  CONSTRAINT `fk_ADMINISTRATOR_USER1`
    FOREIGN KEY (`fk_user_id`)
    REFERENCES `BP_GamingShop`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`country`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`country` (
  `country_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`country_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`city`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`city` (
  `city_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `zip_code` VARCHAR(45) NOT NULL,
  `fk_country_id` INT NOT NULL,
  PRIMARY KEY (`city_id`),
  INDEX `fk_city_country1_idx` (`fk_country_id` ASC) VISIBLE,
  CONSTRAINT `fk_city_country1`
    FOREIGN KEY (`fk_country_id`)
    REFERENCES `BP_GamingShop`.`country` (`country_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`address` (
  `address_id` INT NOT NULL AUTO_INCREMENT,
  `address_1` VARCHAR(45) NOT NULL,
  `address_2` VARCHAR(45) NULL,
  `fk_city_id` INT NOT NULL,
  PRIMARY KEY (`address_id`),
  INDEX `fk_address_city1_idx` (`fk_city_id` ASC) VISIBLE,
  CONSTRAINT `fk_address_city1`
    FOREIGN KEY (`fk_city_id`)
    REFERENCES `BP_GamingShop`.`city` (`city_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`customer` (
  `fk_user_id` INT NOT NULL,
  `fk_address_id` INT NOT NULL,
  `phone_number` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`fk_user_id`),
  INDEX `fk_CUSTOMER_USER1_idx` (`fk_user_id` ASC) VISIBLE,
  INDEX `fk_CUSTOMER_ADRESS1_idx` (`fk_address_id` ASC) VISIBLE,
  CONSTRAINT `fk_CUSTOMER_USER1`
    FOREIGN KEY (`fk_user_id`)
    REFERENCES `BP_GamingShop`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CUSTOMER_ADRESS1`
    FOREIGN KEY (`fk_address_id`)
    REFERENCES `BP_GamingShop`.`address` (`address_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`employee`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`employee` (
  `fk_user_id` INT NOT NULL,
  `position` VARCHAR(45) NOT NULL,
  `salary` DECIMAL(6,2) NOT NULL,
  PRIMARY KEY (`fk_user_id`),
  INDEX `fk_EMPLOYEE_USER1_idx` (`fk_user_id` ASC) VISIBLE,
  CONSTRAINT `fk_EMPLOYEE_USER1`
    FOREIGN KEY (`fk_user_id`)
    REFERENCES `BP_GamingShop`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`discount`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`discount` (
  `discount_id` INT NOT NULL AUTO_INCREMENT,
  `percentage` DECIMAL(5,2) NULL,
  `date_from` DATE NULL,
  `date_to` DATE NULL,
  `fk_employee_id` INT NOT NULL,
  PRIMARY KEY (`discount_id`),
  INDEX `fk_discount_employee1_idx` (`fk_employee_id` ASC) VISIBLE,
  CONSTRAINT `fk_discount_employee1`
    FOREIGN KEY (`fk_employee_id`)
    REFERENCES `BP_GamingShop`.`employee` (`fk_user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`manufacturer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`manufacturer` (
  `manufacturer_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`manufacturer_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`product` (
  `product_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `price` DECIMAL(6,2) NOT NULL,
  `discounted_price` DECIMAL(6,2) NULL,
  `quantity_available` INT NULL,
  `description` TEXT(4000) NULL,
  `warranty_months` INT NULL,
  `image_url` VARCHAR(300) NULL,
  `fk_discount_id` INT NULL,
  `fk_employee_id` INT NOT NULL,
  `fk_manufacturer_id` INT NOT NULL,
  PRIMARY KEY (`product_id`),
  INDEX `fk_PRODUCT_SALE1_idx` (`fk_discount_id` ASC) VISIBLE,
  INDEX `fk_PRODUCT_EMPLOYEE1_idx` (`fk_employee_id` ASC) VISIBLE,
  INDEX `fk_PRODUCT_MANUFACTURER1_idx` (`fk_manufacturer_id` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  CONSTRAINT `fk_PRODUCT_SALE1`
    FOREIGN KEY (`fk_discount_id`)
    REFERENCES `BP_GamingShop`.`discount` (`discount_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PRODUCT_EMPLOYEE1`
    FOREIGN KEY (`fk_employee_id`)
    REFERENCES `BP_GamingShop`.`employee` (`fk_user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PRODUCT_MANUFACTURER1`
    FOREIGN KEY (`fk_manufacturer_id`)
    REFERENCES `BP_GamingShop`.`manufacturer` (`manufacturer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`category` (
  `category_id` INT NOT NULL AUTO_INCREMENT,
  `fk_parent_category_id` INT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`category_id`),
  INDEX `fk_CATEGORY_CATEGORY1_idx` (`fk_parent_category_id` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  CONSTRAINT `fk_CATEGORY_CATEGORY1`
    FOREIGN KEY (`fk_parent_category_id`)
    REFERENCES `BP_GamingShop`.`category` (`category_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`order_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`order_status` (
  `status_id` INT NOT NULL AUTO_INCREMENT,
  `status` ENUM('pending', 'completed', 'shipped', 'cancelled') NULL,
  PRIMARY KEY (`status_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`order` (
  `order_id` INT NOT NULL AUTO_INCREMENT,
  `grand_total` DECIMAL(6,2) NULL,
  `shipping` DECIMAL(6,2) NULL,
  `order_date` DATE NULL,
  `fk_order_status_status_id` INT NOT NULL,
  `fk_customer_id` INT NOT NULL,
  PRIMARY KEY (`order_id`),
  INDEX `fk_SHOP_ORDER_ORDER_STATUS1_idx` (`fk_order_status_status_id` ASC) VISIBLE,
  INDEX `fk_ORDER_CUSTOMER1_idx` (`fk_customer_id` ASC) VISIBLE,
  CONSTRAINT `fk_SHOP_ORDER_ORDER_STATUS1`
    FOREIGN KEY (`fk_order_status_status_id`)
    REFERENCES `BP_GamingShop`.`order_status` (`status_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ORDER_CUSTOMER1`
    FOREIGN KEY (`fk_customer_id`)
    REFERENCES `BP_GamingShop`.`customer` (`fk_user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`order_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`order_item` (
  `fk_order_id` INT NOT NULL,
  `fk_product_id` INT NOT NULL,
  `price` DECIMAL(6,2) NULL,
  `quantity` INT NULL,
  `sub_total` DECIMAL(6,2) NULL DEFAULT (price*quantity),
  PRIMARY KEY (`fk_order_id`, `fk_product_id`),
  INDEX `fk_ORDER_ITEM_ORDER1_idx` (`fk_order_id` ASC) VISIBLE,
  INDEX `fk_ORDER_ITEM_PRODUCT1_idx` (`fk_product_id` ASC) VISIBLE,
  CONSTRAINT `fk_ORDER_ITEM_ORDER1`
    FOREIGN KEY (`fk_order_id`)
    REFERENCES `BP_GamingShop`.`order` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ORDER_ITEM_PRODUCT1`
    FOREIGN KEY (`fk_product_id`)
    REFERENCES `BP_GamingShop`.`product` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`review`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`review` (
  `review_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NULL,
  `score` ENUM('0', '1', '2', '3', '4', '5') NULL,
  `total_score` DECIMAL(3,2) NULL,
  `comment` TEXT(5000) NULL,
  `fk_product_id` INT NOT NULL,
  `fk_customer_id` INT NOT NULL,
  PRIMARY KEY (`review_id`),
  INDEX `fk_REVIEW_PRODUCT1_idx` (`fk_product_id` ASC) VISIBLE,
  INDEX `fk_REVIEW_CUSTOMER1_idx` (`fk_customer_id` ASC) VISIBLE,
  CONSTRAINT `fk_REVIEW_PRODUCT1`
    FOREIGN KEY (`fk_product_id`)
    REFERENCES `BP_GamingShop`.`product` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_REVIEW_CUSTOMER1`
    FOREIGN KEY (`fk_customer_id`)
    REFERENCES `BP_GamingShop`.`customer` (`fk_user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`shopping_cart`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`shopping_cart` (
  `cart_id` INT NOT NULL AUTO_INCREMENT,
  `grand_total` DECIMAL(6,2) NULL,
  `fk_customer_id` INT NOT NULL,
  PRIMARY KEY (`cart_id`),
  INDEX `fk_SHOPPING_CART_CUSTOMER1_idx` (`fk_customer_id` ASC) VISIBLE,
  CONSTRAINT `fk_SHOPPING_CART_CUSTOMER1`
    FOREIGN KEY (`fk_customer_id`)
    REFERENCES `BP_GamingShop`.`customer` (`fk_user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`cart_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`cart_item` (
  `fk_shopping_cart_id` INT NOT NULL,
  `fk_product_id` INT NOT NULL,
  `price` DECIMAL(6,2) NULL,
  `quantity` INT NULL,
  `sub_total` DECIMAL(6,2) NULL DEFAULT (price*quantity),
  PRIMARY KEY (`fk_shopping_cart_id`, `fk_product_id`),
  INDEX `fk_CART_ITEM_SHOPPING_CART1_idx` (`fk_shopping_cart_id` ASC) VISIBLE,
  INDEX `fk_CART_ITEM_PRODUCT1_idx` (`fk_product_id` ASC) VISIBLE,
  CONSTRAINT `fk_CART_ITEM_SHOPPING_CART1`
    FOREIGN KEY (`fk_shopping_cart_id`)
    REFERENCES `BP_GamingShop`.`shopping_cart` (`cart_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CART_ITEM_PRODUCT1`
    FOREIGN KEY (`fk_product_id`)
    REFERENCES `BP_GamingShop`.`product` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`product_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`product_category` (
  `fk_product_id` INT NOT NULL,
  `fk_category_id` INT NOT NULL,
  PRIMARY KEY (`fk_product_id`, `fk_category_id`),
  INDEX `fk_PRODUCT_CATEGORY_CATEGORY1_idx` (`fk_category_id` ASC) VISIBLE,
  CONSTRAINT `fk_PRODUCT_CATEGORY_PRODUCT1`
    FOREIGN KEY (`fk_product_id`)
    REFERENCES `BP_GamingShop`.`product` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PRODUCT_CATEGORY_CATEGORY1`
    FOREIGN KEY (`fk_category_id`)
    REFERENCES `BP_GamingShop`.`category` (`category_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`receipt`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`receipt` (
  `receipt_id` INT NOT NULL AUTO_INCREMENT,
  `date_issued` DATETIME NULL,
  `fk_order_id` INT NULL,
  `grand_total` DECIMAL(6,2) NULL,
  PRIMARY KEY (`receipt_id`),
  INDEX `fk_RECEIPT_ORDER1_idx` (`fk_order_id` ASC) VISIBLE,
  CONSTRAINT `fk_RECEIPT_ORDER1`
    FOREIGN KEY (`fk_order_id`)
    REFERENCES `BP_GamingShop`.`order` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BP_GamingShop`.`receipt_item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BP_GamingShop`.`receipt_item` (
  `fk_receipt_id` INT NOT NULL,
  `fk_product_id` INT NOT NULL,
  `price` DECIMAL(6,2) NULL,
  `quantity` INT NULL,
  `sub_total` DECIMAL(6,2) NULL DEFAULT (price*quantity),
  PRIMARY KEY (`fk_receipt_id`, `fk_product_id`),
  INDEX `fk_RECEIPT_ITEM_PRODUCT1_idx` (`fk_product_id` ASC) VISIBLE,
  CONSTRAINT `fk_RECEIPT_ITEM_RECEIPT1`
    FOREIGN KEY (`fk_receipt_id`)
    REFERENCES `BP_GamingShop`.`receipt` (`receipt_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RECEIPT_ITEM_PRODUCT1`
    FOREIGN KEY (`fk_product_id`)
    REFERENCES `BP_GamingShop`.`product` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
