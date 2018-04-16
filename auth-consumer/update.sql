CREATE TABLE `gmair_userinfo`.`admin_info` (
  `admin_id` VARCHAR(20) NOT NULL,
  `admin_email` VARCHAR(45) NOT NULL,
  `admin_name` VARCHAR(45) NOT NULL,
  `admin_password` VARCHAR(200) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 1,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`admin_id`));
