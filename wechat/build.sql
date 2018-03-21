-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gmair_wechat
-- -----------------------------------------------------
-- WeChat configuration and auto reply template

-- -----------------------------------------------------
-- Schema gmair_wechat
--
-- WeChat configuration and auto reply template
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gmair_wechat`
  DEFAULT CHARACTER SET utf8;
USE `gmair_wechat`;

-- -----------------------------------------------------
-- Table `gmair_wechat`.`access_token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_wechat`.`access_token` (
  `access_token` VARCHAR(200) NOT NULL,
  `block_flag`   TINYINT(1)   NOT NULL,
  `create_time`  DATETIME     NOT NULL
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_wechat`.`text_template`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_wechat`.`text_template` (
  `template_id`  INT         NOT NULL,
  `message_type` VARCHAR(45) NULL,
  `response`     LONGTEXT    NOT NULL,
  `block_flag`   TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`  VARCHAR(45) NOT NULL,
  PRIMARY KEY (`template_id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_wechat`.`article_template`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_wechat`.`article_template` (
  `template_id`         VARCHAR(20)  NOT NULL,
  `article_title`       VARCHAR(45)  NOT NULL,
  `description_type`    TINYINT(1)   NOT NULL,
  `description_content` VARCHAR(100) NOT NULL,
  `picture_url`         VARCHAR(200) NOT NULL,
  `article_url`         VARCHAR(200) NOT NULL,
  `block_flag`          TINYINT(1)   NOT NULL DEFAULT 0,
  `create_time`         DATETIME     NOT NULL,
  PRIMARY KEY (`template_id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_wechat`.`auto_reply`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_wechat`.`auto_reply` (
  `reply_id`     VARCHAR(20) NOT NULL,
  `message_type` VARCHAR(45) NOT NULL,
  `keyword`      VARCHAR(45) NULL,
  `template_id`  VARCHAR(20) NOT NULL,
  `block_flag`   TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`reply_id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_wechat`.`picture_template`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_wechat`.`picture_template` (
  `template_id` VARCHAR(20)  NOT NULL,
  `picture_url` VARCHAR(200) NOT NULL,
  `block_flag`  TINYINT(1)   NOT NULL DEFAULT 0,
  `create_time` DATETIME     NOT NULL,
  PRIMARY KEY (`template_id`)
)
  ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `gmair_wechat`.`wechat_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gmair_wechat`.`wechat_user` (
  `user_id`       VARCHAR(20) NOT NULL,
  `wechat_id`     VARCHAR(50) NOT NULL,
  `nickname`      VARCHAR(45) NULL,
  `user_sex`      TINYINT(1)  NULL,
  `user_city`     VARCHAR(45) NOT NULL,
  `user_province` VARCHAR(45) NOT NULL,
  `block_flag`    TINYINT(1)  NOT NULL,
  `create_time`   DATETIME    NOT NULL,
  PRIMARY KEY (`user_id`)
)
  ENGINE = InnoDB;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
