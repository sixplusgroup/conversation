#2018.04.02
CREATE TABLE `gmair_order`.`platform_order_channel` (
  `channel_id`   VARCHAR(20) NOT NULL,
  `channel_name` VARCHAR(45) NOT NULL,
  `block_flag`   TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`channel_id`)
);
