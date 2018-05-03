#2018.04.02
CREATE TABLE `gmair_order`.`platform_order_channel` (
  `channel_id`   VARCHAR(20) NOT NULL,
  `channel_name` VARCHAR(45) NOT NULL,
  `block_flag`   TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`channel_id`)
);

#2018.04.03
ALTER TABLE `gmair_order`.`platform_order`
  ADD COLUMN `order_channel` VARCHAR(45) NOT NULL
  AFTER `total_price`,
  ADD COLUMN `description` VARCHAR(100) NULL
  AFTER `order_channel`;

#2018.04.04
CREATE TABLE `gmair_order`.`machine_install_type` (
  mit_id       VARCHAR(20),
  install_type VARCHAR(45),
  block_flag   TINYINT(1) DEFAULT 0,
  create_time  DATETIME,
  PRIMARY KEY (mis_id)
)

#2018.04.21 province and city can be null in platform order
ALTER TABLE `gmair_order`.`platform_order`
  CHANGE COLUMN `province` `province` VARCHAR(45) NULL,
  CHANGE COLUMN `city` `city` VARCHAR(45) NULL;

#2018.04.23 add table to record whether the retry count has exceeded the maximum retry count, if yes, then system will no longer try to resolve it
CREATE TABLE `gmair_order`.`order_location_retry_count` (
  `order_id`    VARCHAR(20) NOT NULL,
  `retry_count` INT         NOT NULL DEFAULT 1,
  `update_time` DATETIME    NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`order_id`)
);

#add column order status
ALTER TABLE `gmair_order`.`platform_order`
  ADD COLUMN `order_status` TINYINT(1) NOT NULL DEFAULT 0
  AFTER `description`;

CREATE TABLE `order_commodity` (
  `com_id` varchar(20) NOT NULL,
  `com_name` varchar(45) DEFAULT NULL,
  `com_type` tinyint(1) NOT NULL DEFAULT '0',
  `com_price` double NOT NULL DEFAULT '0',
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`com_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE `order_item` (
  `order_item_id` varchar(20) NOT NULL,
  `order_id` varchar(20) NOT NULL,
  `com_id` varchar(20) NOT NULL,
  `price` decimal(10,0) NOT NULL,
  `order_item_quantity` int(11) NOT NULL DEFAULT '1',
  `block_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`order_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
