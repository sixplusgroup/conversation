##2018-09-22
ALTER TABLE `gmair_drift`.`drift_reservation`
CHANGE COLUMN `interval` `interval_date` INT(11) NOT NULL;


##2018-09-25
ALTER TABLE `gmair_drift`.`drift_goods`
ADD COLUMN `goods_description` VARCHAR(45) NOT NULL AFTER `goods_name`,
ADD COLUMN `goods_price` DOUBLE NOT NULL AFTER `goods_description`;

##2018-10-10
CREATE TABLE IF NOT EXISTS `gmair_drift`.`drift_excode` (
  `code_id` VARCHAR(45) NOT NULL,
  `activity_id` VARCHAR(45) NOT NULL,
  `code_value` VARCHAR(45) NOT NULL,
  `code_status` TINYINT(1) NOT NULL DEFAULT 0,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`code_id`),
  INDEX `fk_excode_drift_activity1_idx` (`activity_id` ASC),
  CONSTRAINT `fk_excode_drift_activity1`
    FOREIGN KEY (`activity_id`)
    REFERENCES `gmair_drift`.`drift_activity` (`activity_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB DEFAULT CHARSET=UTF8;

##2018-10-12
CREATE TABLE IF NOT EXISTS `gmair_drift`.`drift_order_commodity` (
  `commodity_id` VARCHAR(45) NOT NULL,
  `commodity_name` VARCHAR(45) DEFAULT NULL,
  `commodity_type` TINYINT(1) NOT NULL DEFAULT '0',
  `commodity_price` DOUBLE NOT NULL DEFAULT '0',
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`commodity_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS `gmair_drift`.`drift_order_item` (
  `item_id` VARCHAR(45) NOT NULL,
  `order_id` VARCHAR(45) NOT NULL,
  `item_name` VARCHAR(63) NOT NULL,
  `quantity` INT(11) NOT NULL DEFAULT '1',
  `item_price` DECIMAL(10 , 0 ) NOT NULL,
  `test_target` VARCHAR(20) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`item_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS `gmair_drift`.`drift_order` (
  `order_id` VARCHAR(45) NOT NULL,
  `order_no` VARCHAR(50) DEFAULT NULL,
  `consignee` VARCHAR(45) NOT NULL,
  `phone` VARCHAR(45) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `province` VARCHAR(45) DEFAULT NULL,
  `city` VARCHAR(45) DEFAULT NULL,
  `district` VARCHAR(45) DEFAULT NULL,
  `total_price` DOUBLE NOT NULL DEFAULT '0',
  `description` VARCHAR(100) DEFAULT NULL,
  `order_status` TINYINT(1) NOT NULL DEFAULT '0',
  `buy_machine` TINYINT(1) NOT NULL DEFAULT '0',
  `machine_orderNo` VARCHAR(50) DEFAULT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`order_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8;

ALTER TABLE `gmair_drift`.`drift_excode`
ADD COLUMN `use_type` TINYINT(1) DEFAULT NULL AFTER `code_status`;

ALTER TABLE `gmair_drift`.`drift_order`
ADD COLUMN `real_pay` INT(11) NOT NULL AFTER `total_price`;

ALTER TABLE `gmair_drift`.`drift_activity`
ADD COLUMN `reservable_days` INT(11) NOT NULL AFTER `threshold`;


##2018-10-17
ALTER TABLE gmair_drift.drift_order_item
DROP COLUMN `test_target`;

ALTER TABLE `gmair_drift`.`drift_reservation`
ADD COLUMN `test_target` VARCHAR(20) NOT NULL AFTER `city_id`;

