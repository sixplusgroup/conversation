/*
    Source Server Type    : MySQL
    Source Schema   : gmair_membership
    Data: 16/07/2021 14:53:17
*/


-- table: membership_consumer

DROP TABLE IF EXISTS `membership_consumer`;
CREATE TABLE `membership_consumer` (
  `consumer_id`     VARCHAR(20) NOT NULL,
  `first_integral`  INT         NOT NULL DEFAULT 0,
  `second_integral` INT         NOT NULL DEFAULT 0,
  `create_time`     DATETIME(0) NOT NULL,
  PRIMARY KEY (`consumer_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- table: integral_product
DROP TABLE IF EXISTS `integral_product`;
CREATE TABLE `integral_product` (
  `product_id`   VARCHAR(20) NOT NULL,
  `product_name` VARCHAR(20) NOT NULL DEFAULT '',
  `create_time`  DATETIME(0) NOT NULL,
  PRIMARY KEY (`product_id`) USING BTREE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- table: integral_record
DROP TABLE IF EXISTS `integral_add`;
CREATE TABLE `integral_add` (
  `add_id`         VARCHAR(20) NOT NULL,
  `consumer_id`    VARCHAR(20) NOT NULL,
  `integral_value` INT         NOT NULL DEFAULT 0,
  `is_confirmed`   TINYINT(1)  NOT NULL DEFAULT 0,
  `confirmed_time` DATETIME(0),
  `create_time`    DATETIME(0) NOT NULL,
  PRIMARY KEY (`add_id`) USING BTREE,
  FOREIGN KEY (`consumer_id`) REFERENCES `membership_consumer` (consumer_id)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- event gmair_membership_integral_maintain
-- show variables like '%event%'; 查看event_scheduler如果为OFF或0就表示关闭
-- SET GLOBAL event_scheduler = ON; 开启event

DROP EVENT IF EXISTS `gmair_membership_integral_maintain`;
CREATE EVENT `gmair_membership_integral_maintain`
  ON SCHEDULE
    EVERY '1' YEAR STARTS '2023-01-03 00:00:00'
  ON COMPLETION PRESERVE
  COMMENT 'integralmaintain'
DO UPDATE gmair_membership.membership_consumer
SET first_integral = second_integral, second_integral = 0;