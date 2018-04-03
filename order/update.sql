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
