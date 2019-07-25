##2018-10-10
CREATE TABLE IF NOT EXISTS `gmair_drift`.`drift_excode` (
  `code_id`     VARCHAR(45) NOT NULL,
  `activity_id` VARCHAR(45) NOT NULL,
  `code_value`  VARCHAR(45) NOT NULL,
  `code_status` TINYINT(1)  NOT NULL DEFAULT 0,
  `price`       DOUBLE      NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`code_id`),
  INDEX `fk_excode_drift_activity1_idx` (`activity_id` ASC),
  CONSTRAINT `fk_excode_drift_activity1`
  FOREIGN KEY (`activity_id`)
  REFERENCES `gmair_drift`.`drift_activity` (`activity_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS `gmair_drift`.`drift_order_item` (
  `item_id`     VARCHAR(45)    NOT NULL,
  `order_id`    VARCHAR(45)    NOT NULL,
  `item_name`   VARCHAR(63)    NOT NULL,
  `quantity`    INT(11)        NOT NULL DEFAULT '1',
  `item_price`  DECIMAL(10, 0) NOT NULL,
  `block_flag`  TINYINT(1)     NOT NULL DEFAULT '0',
  `create_time` DATETIME       NOT NULL,
  PRIMARY KEY (`item_id`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS `gmair_drift`.`drift_order` (
  `order_id`      VARCHAR(45)  NOT NULL,
  `order_no`      VARCHAR(50)           DEFAULT NULL,
  `consumer_id`   VARCHAR(45)  NOT NULL,
  `activity`      VARCHAR(45)  NOT NULL,
  `equip_id`      VARCHAR(45)  NOT NULL,
  `consignee`     VARCHAR(45)  NOT NULL,
  `phone`         VARCHAR(45)  NOT NULL,
  `address`       VARCHAR(100) NOT NULL,
  `expected_date` DATE         NOT NULL,
  `interval_date` INT          NOT NULL,
  `province`      VARCHAR(45)           DEFAULT NULL,
  `city`          VARCHAR(45)           DEFAULT NULL,
  `district`      VARCHAR(45)           DEFAULT NULL,
  `total_price`   DOUBLE       NOT NULL DEFAULT '0',
  `real_pay`      DOUBLE       NOT NULL DEFAULT '0',
  `excode`        VARCHAR(45)           DEFAULT NULL,
  `test_target`   VARCHAR(20)           DEFAULT NULL,
  `description`   VARCHAR(100)          DEFAULT NULL,
  `order_status`  TINYINT(1)   NOT NULL DEFAULT '0',
  `block_flag`    TINYINT(1)   NOT NULL DEFAULT '0',
  `create_time`   DATETIME     NOT NULL,
  PRIMARY KEY (`order_id`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = UTF8;

ALTER TABLE `gmair_drift`.`drift_activity`
  ADD COLUMN `reservable_days` INT(11) NOT NULL
  AFTER `threshold`;

##2018-10-22
CREATE TABLE IF NOT EXISTS `gmair_drift`.`equip_activity` (
  `equip_id`    VARCHAR(45) NOT NULL,
  `activity_id` VARCHAR(45) NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL DEFAULT '0',
  `create_time` DATETIME    NOT NULL
)
  ENGINE = INNODB
  DEFAULT CHARSET = UTF8;

##2018-10-23
CREATE VIEW activity_equipment_view
  AS
    SELECT
      drift_activity.activity_id,
      activity_name,
      drift_equipment.equip_id,
      equip_name,
      drift_equipment.block_flag,
      drift_equipment.create_time
    FROM gmair_drift.drift_activity, gmair_drift.equip_activity, gmair_drift.drift_equipment
    WHERE drift_activity.activity_id = equip_activity.activity_id
          AND equip_activity.equip_id = drift_equipment.equip_id
          AND drift_activity.block_flag = 0;

CREATE TABLE `gmair_drift`.`machine_excode` (
  `id`          VARCHAR(45) NOT NULL,
  `qrcode`      VARCHAR(45) NOT NULL,
  `excode`      VARCHAR(45) NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = UTF8;

CREATE TABLE `gmair_drift`.`attachment` (
  `attach_id`    VARCHAR(45) NOT NULL,
  `equip_id`     VARCHAR(45) NOT NULL,
  `attach_name`  VARCHAR(45) NOT NULL,
  `attach_price` DOUBLE      NOT NULL,
  `block_flag`   TINYINT(1)  NOT NULL,
  `create_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`attach_id`)
);

##2018-10-26
ALTER TABLE `gmair_drift`.`drift_activity`
  ADD COLUMN `introduction` VARCHAR(50) NOT NULL
  AFTER `end_time`;

## 2019-07-22
ALTER TABLE `gmair_drift`.`drift_activity`
  CHANGE COLUMN `introduction` `introduction` LONGTEXT NOT NULL;

ALTER TABLE `gmair_drift`.`drift_activity`
  DROP FOREIGN KEY `fk_drift_activity_drift_goods1`;
ALTER TABLE `gmair_drift`.`drift_activity`
  DROP COLUMN `goods_id`,
  DROP INDEX `fk_drift_activity_drift_goods1_idx`;

## 2019-07-25

CREATE TABLE `gmair_install`.`user_session` (
  `open_id`     VARCHAR(100) NOT NULL,
  `session_key` VARCHAR(100) NOT NULL,
  `block_flag`  TINYINT(1)   NOT NULL,
  `create_time` DATETIME     NOT NULL,
  PRIMARY KEY (`open_id`)
);

ALTER TABLE `gmair_drift`.`drift_order`
  ADD COLUMN `consumer_id` VARCHAR(45) NULL AFTER `order_no`;


ALTER TABLE `gmair_drift`.`drift_order`
  CHANGE COLUMN `province` `province` VARCHAR(45) NULL DEFAULT NULL AFTER `phone`,
  ADD COLUMN `activity_id` VARCHAR(45) NULL AFTER `consumer_id`;


ALTER TABLE `gmair_drift`.`drift_order`
  CHANGE COLUMN `activity_id` `activity_id` VARCHAR(45) NULL DEFAULT NULL AFTER `order_no`,
  ADD COLUMN `equip_id` VARCHAR(45) NULL AFTER `activity_id`;

ALTER TABLE `gmair_drift`.`drift_order`
  ADD COLUMN `expected_date` DATE NOT NULL AFTER `machine_orderNo`;

ALTER TABLE `gmair_drift`.`drift_order`
  ADD COLUMN `interval_date` INT NOT NULL AFTER `expected_date`;

ALTER TABLE `gmair_drift`.`drift_order`
  ADD COLUMN `real_pay` DOUBLE NULL AFTER `total_price`;

ALTER TABLE `gmair_drift`.`drift_order`
  ADD COLUMN `excode` VARCHAR(45) NULL AFTER `interval_date`;



