##2018-09-22
ALTER TABLE `gmair_drift`.`drift_reservation`
CHANGE COLUMN `interval` `interval_date` INT(11) NOT NULL ;


##2018-09-25
ALTER TABLE `gmair_drift`.`drift_goods`
ADD COLUMN `goods_description` VARCHAR(45) NOT NULL AFTER `goods_name`,
ADD COLUMN `goods_price` DOUBLE NOT NULL AFTER `goods_description`;

##2018-10-10
CREATE TABLE IF NOT EXISTS `gmair_drift`.`excode` (
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
ENGINE = InnoDB;