##2019-04-10
CREATE TABLE IF NOT EXISTS `gmair_mqtt`.`mqtt_firmware` (
  `firmware_id` VARCHAR(20) NOT NULL,
  `firmware_version` VARCHAR(20) NOT NULL,
  `firmware_link` VARCHAR(50) NOT NULL,
  `block_flag`   TINYINT(1)   NOT NULL,
  `create_time`  DATETIME     NOT NULL,
  PRIMARY KEY (`firmware_id`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `gmair_mqtt`.`mahine_type` (
  `board_id` VARCHAR(20) NOT NULL,
  `board_version` INT NOT NULL,
  `device_name` VARCHAR(20) NOT NULL,
  `type_name` VARCHAR(50) NOT NULL,
  `block_flag`   TINYINT(1) NOT NULL,
  `create_time`  DATETIME NOT NULL,
  PRIMARY KEY (`board_id`)
) ENGINE = InnoDB;

##2019-04-11
CREATE TABLE IF NOT EXISTS `gmair_mqtt`.`machine_alert` (
  `alert_id` VARCHAR(20) NOT NULL,
  `machine_id` VARCHAR(20) NOT NULL,
  `alert_code` INT NOT NULL,
  `alert_msg` VARCHAR(20) NOT NULL,
  `alert_status` TINYINT(1) NOT NULL,
  `block_flag`   TINYINT(1) NOT NULL,
  `create_time`  DATETIME NOT NULL,
  PRIMARY KEY (`alert_id`)
) ENGINE = InnoDB;