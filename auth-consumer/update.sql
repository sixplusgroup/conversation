CREATE TABLE `gmair_userinfo`.`admin_info` (
  `admin_id` VARCHAR(20) NOT NULL,
  `admin_email` VARCHAR(45) NOT NULL,
  `admin_name` VARCHAR(45) NOT NULL,
  `admin_password` VARCHAR(200) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 1,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`admin_id`));

#2018-09-01 修改view
CREATE
    ALGORITHM = UNDEFINED
    DEFINER = `root`@`localhost`
    SQL SECURITY DEFINER
VIEW `gmair_userinfo`.`view_consumer_info` AS
    (SELECT
        `ci`.`consumer_id` AS `consumer_id`,
        `ci`.`consumer_name` AS `consumer_name`,
        `ci`.`consumer_username` AS `consumer_username`,
        `ci`.`consumer_wechat` AS `consumer_wechat`,
        `cp`.`phone_number` AS `phone_number`,
        `ca`.`address_detail` AS `address_detail`,
        `ca`.`address_province` AS `address_province`,
        `ca`.`address_city` AS `address_city`,
        `ca`.`address_district` AS `address_district`,
        `ci`.`block_flag` AS `block_flag`,
        `ci`.`create_time` AS `create_time`
    FROM
        (
        (`gmair_userinfo`.`consumer_info` `ci` LEFT JOIN `gmair_userinfo`.`consumer_phone` `cp` ON ((`ci`.`consumer_id` = `cp`.`consumer_id`)))
        LEFT JOIN `gmair_userinfo`.`consumer_addr` `ca` ON ((`ci`.`consumer_id` = `ca`.`consumer_id` AND  `ca`.`addr_preferred` = 1)))
	)