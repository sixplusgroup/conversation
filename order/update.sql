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
