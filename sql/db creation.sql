-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema hospital
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema hospital
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `hospital` DEFAULT CHARACTER SET utf8 ;
USE `hospital` ;

-- -----------------------------------------------------
-- Table `hospital`.`case`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hospital`.`case` (
  `idcase` INT(11) NOT NULL AUTO_INCREMENT,
  `admission_date` VARCHAR(10) CHARACTER SET 'utf8' NOT NULL,
  `complaints` VARCHAR(100) NULL DEFAULT NULL,
  `idmedical_card` INT(11) NOT NULL,
  `discharge_date` VARCHAR(10) CHARACTER SET 'utf8' NULL DEFAULT NULL,
  `final_diagnosis` TINYINT(7) UNSIGNED NULL DEFAULT NULL,
  `active` TINYINT(1) UNSIGNED ZEROFILL NOT NULL DEFAULT '0',
  `iddoctor` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`idcase`),
  UNIQUE INDEX `idrecord_UNIQUE` (`idcase` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 20
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `hospital`.`diagnosis`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hospital`.`diagnosis` (
  `iddiagnosis` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) CHARACTER SET 'utf8' NOT NULL,
  PRIMARY KEY (`iddiagnosis`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `hospital`.`medical_card`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hospital`.`medical_card` (
  `idmedical_card` INT(11) NOT NULL AUTO_INCREMENT,
  `iduser` INT(11) NOT NULL,
  `birth_date` VARCHAR(10) CHARACTER SET 'utf8' NULL DEFAULT NULL,
  `sex` TINYINT(1) NOT NULL,
  PRIMARY KEY (`idmedical_card`),
  INDEX `fk_medical_card_user1_idx` (`iduser` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 15
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `hospital`.`prescription`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hospital`.`prescription` (
  `idprescription` INT(11) NOT NULL AUTO_INCREMENT,
  `idprescription_type` INT(11) NOT NULL,
  `date` VARCHAR(10) CHARACTER SET 'utf8' NOT NULL,
  `details` VARCHAR(25) CHARACTER SET 'utf8' NOT NULL,
  `idcase` INT(11) NOT NULL,
  `iddoctor` INT(11) NOT NULL,
  `idcard` INT(11) NOT NULL,
  PRIMARY KEY (`idprescription`),
  INDEX `fk_prescription_prescription_type1_idx` (`idprescription_type` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 26
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `hospital`.`prescription_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hospital`.`prescription_type` (
  `idprescription_type` INT(11) NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idprescription_type`),
  UNIQUE INDEX `idprescription_type_UNIQUE` (`idprescription_type` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `hospital`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hospital`.`user` (
  `iduser` INT(11) NOT NULL AUTO_INCREMENT,
  `iduser_role` INT(11) NOT NULL,
  `login` VARCHAR(20) CHARACTER SET 'utf8' NOT NULL,
  `password` BIGINT(20) NOT NULL,
  `first_name` VARCHAR(20) CHARACTER SET 'utf8' NOT NULL,
  `last_name` VARCHAR(20) CHARACTER SET 'utf8' NOT NULL,
  `patronymic` VARCHAR(20) CHARACTER SET 'utf8' NOT NULL,
  PRIMARY KEY (`iduser`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
  INDEX `fk_user_user_role_idx` (`iduser_role` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 26
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `hospital`.`user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hospital`.`user_role` (
  `iduser_role` INT(11) NOT NULL AUTO_INCREMENT,
  `role_type` VARCHAR(20) CHARACTER SET 'utf8' NOT NULL,
  PRIMARY KEY (`iduser_role`),
  UNIQUE INDEX `iduser_role_UNIQUE` (`iduser_role` ASC) VISIBLE,
  UNIQUE INDEX `role_type_UNIQUE` (`role_type` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
