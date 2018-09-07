#enterprise order

CREATE TABLE `gmair_order`.`enterprise_order` (
  `order_id`        VARCHAR(20) NOT NULL,
  `merchant_id`     VARCHAR(20) NOT NULL,
  `model_name`      VARCHAR(20) NOT NULL,
  `require_install` TINYINT(1)  NOT NULL DEFAULT 0,
  `plan_confirmed`  TINYINT(1)  NOT NULL DEFAULT 0,
  `description`     VARCHAR(50) NULL,
  `block_flag`      TINYINT(1)  NOT NULL DEFAULT 0,
  `create_time`     DATETIME    NOT NULL,
  PRIMARY KEY (`order_id`)
);

#2018-09-06
ALTER TABLE `gmair_order`.`platform_order`
ADD COLUMN `latitude` DOUBLE NULL AFTER `district`,
ADD COLUMN `longitude` DOUBLE NULL AFTER `latitude`;

ALTER TABLE `gmair_order`.`platform_order`
CHANGE COLUMN `latitude` `latitude` DOUBLE NULL DEFAULT '0' ,
CHANGE COLUMN `longitude` `longitude` DOUBLE NULL DEFAULT '0' ;
