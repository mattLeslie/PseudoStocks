-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema salstocks_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema salstocks_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `salstocks_db` DEFAULT CHARACTER SET utf8 ;
USE `salstocks_db` ;

-- -----------------------------------------------------
-- Table `salstocks_db`.`favorites`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `salstocks_db`.`favorites` (
  `favorites_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `ticker` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`favorites_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 74
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `salstocks_db`.`portfolios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `salstocks_db`.`portfolios` (
  `portfolios_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NULL DEFAULT NULL,
  `ticker` VARCHAR(45) NULL DEFAULT NULL,
  `shares` INT NULL DEFAULT '0',
  `datetime` DATETIME NULL DEFAULT NULL,
  `cost` DOUBLE NULL DEFAULT NULL,
  PRIMARY KEY (`portfolios_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 58
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `salstocks_db`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `salstocks_db`.`users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `username` VARCHAR(45) NULL DEFAULT NULL,
  `password` VARCHAR(45) NULL DEFAULT NULL,
  `cash` DOUBLE NULL DEFAULT NULL,
  `google` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 142
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
