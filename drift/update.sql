##2018-10-10
CREATE TABLE IF NOT EXISTS `gmair_drift`.`drift_excode` (
  `code_id`     VARCHAR(45) NOT NULL,
  `activity_id` VARCHAR(45) NOT NULL,
  `code_value`  VARCHAR(45) NOT NULL,
  `code_status` TINYINT(1)  NOT NULL DEFAULT 0,
  `price`       DOUBLE      NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`code_id`),
  INDEX `fk_excode_drift_activity1_idx` (`activity_id` ASC),
  CONSTRAINT `fk_excode_drift_activity1`
  FOREIGN KEY (`activity_id`)
  REFERENCES `gmair_drift`.`drift_activity` (`activity_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS `gmair_drift`.`drift_order_item` (
  `item_id`     VARCHAR(45)    NOT NULL,
  `order_id`    VARCHAR(45)    NOT NULL,
  `item_name`   VARCHAR(63)    NOT NULL,
  `quantity`    INT(11)        NOT NULL DEFAULT '1',
  `item_price`  DECIMAL(10, 0) NOT NULL,
  `block_flag`  TINYINT(1)     NOT NULL DEFAULT '0',
  `create_time` DATETIME       NOT NULL,
  PRIMARY KEY (`item_id`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = UTF8;

CREATE TABLE IF NOT EXISTS `gmair_drift`.`drift_order` (
  `order_id`      VARCHAR(45)  NOT NULL,
  `order_no`      VARCHAR(50)           DEFAULT NULL,
  `consumer_id`   VARCHAR(45)  NOT NULL,
  `activity`      VARCHAR(45)  NOT NULL,
  `equip_id`      VARCHAR(45)  NOT NULL,
  `consignee`     VARCHAR(45)  NOT NULL,
  `phone`         VARCHAR(45)  NOT NULL,
  `address`       VARCHAR(100) NOT NULL,
  `expected_date` DATE         NOT NULL,
  `interval_date` INT          NOT NULL,
  `province`      VARCHAR(45)           DEFAULT NULL,
  `city`          VARCHAR(45)           DEFAULT NULL,
  `district`      VARCHAR(45)           DEFAULT NULL,
  `total_price`   DOUBLE       NOT NULL DEFAULT '0',
  `real_pay`      DOUBLE       NOT NULL DEFAULT '0',
  `excode`        VARCHAR(45)           DEFAULT NULL,
  `test_target`   VARCHAR(20)           DEFAULT NULL,
  `description`   VARCHAR(100)          DEFAULT NULL,
  `order_status`  TINYINT(1)   NOT NULL DEFAULT '0',
  `block_flag`    TINYINT(1)   NOT NULL DEFAULT '0',
  `create_time`   DATETIME     NOT NULL,
  PRIMARY KEY (`order_id`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = UTF8;

ALTER TABLE `gmair_drift`.`drift_activity`
  ADD COLUMN `reservable_days` INT(11) NOT NULL
  AFTER `threshold`;

##2018-10-22
CREATE TABLE IF NOT EXISTS `gmair_drift`.`equip_activity` (
  `equip_id`    VARCHAR(45) NOT NULL,
  `activity_id` VARCHAR(45) NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL DEFAULT '0',
  `create_time` DATETIME    NOT NULL
)
  ENGINE = INNODB
  DEFAULT CHARSET = UTF8;

##2018-10-23
CREATE VIEW activity_equipment_view
  AS
    SELECT
      drift_activity.activity_id,
      activity_name,
      drift_equipment.equip_id,
      equip_name,
      drift_equipment.block_flag,
      drift_equipment.create_time
    FROM gmair_drift.drift_activity, gmair_drift.equip_activity, gmair_drift.drift_equipment
    WHERE drift_activity.activity_id = equip_activity.activity_id
          AND equip_activity.equip_id = drift_equipment.equip_id
          AND drift_activity.block_flag = 0;

CREATE TABLE `gmair_drift`.`machine_excode` (
  `id`          VARCHAR(45) NOT NULL,
  `qrcode`      VARCHAR(45) NOT NULL,
  `excode`      VARCHAR(45) NOT NULL,
  `block_flag`  TINYINT(1)  NOT NULL,
  `create_time` DATETIME    NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = INNODB
  DEFAULT CHARSET = UTF8;

CREATE TABLE `gmair_drift`.`attachment` (
  `attach_id`    VARCHAR(45) NOT NULL,
  `equip_id`     VARCHAR(45) NOT NULL,
  `attach_name`  VARCHAR(45) NOT NULL,
  `attach_price` DOUBLE      NOT NULL,
  `block_flag`   TINYINT(1)  NOT NULL,
  `create_time`  DATETIME    NOT NULL,
  PRIMARY KEY (`attach_id`)
);

##2018-10-26
ALTER TABLE `gmair_drift`.`drift_activity`
  ADD COLUMN `introduction` VARCHAR(50) NOT NULL
  AFTER `end_time`;

## 2019-07-22
ALTER TABLE `gmair_drift`.`drift_activity`
  CHANGE COLUMN `introduction` `introduction` LONGTEXT NOT NULL;

ALTER TABLE `gmair_drift`.`drift_activity`
  DROP FOREIGN KEY `fk_drift_activity_drift_goods1`;
ALTER TABLE `gmair_drift`.`drift_activity`
  DROP COLUMN `goods_id`,
  DROP INDEX `fk_drift_activity_drift_goods1_idx`;

## 2019-07-25

CREATE TABLE `gmair_install`.`user_session` (
  `open_id`     VARCHAR(100) NOT NULL,
  `session_key` VARCHAR(100) NOT NULL,
  `block_flag`  TINYINT(1)   NOT NULL,
  `create_time` DATETIME     NOT NULL,
  PRIMARY KEY (`open_id`)
);

ALTER TABLE `gmair_drift`.`drift_order`
  ADD COLUMN `consumer_id` VARCHAR(45) NULL
  AFTER `order_no`;


ALTER TABLE `gmair_drift`.`drift_order`
  CHANGE COLUMN `province` `province` VARCHAR(45) NULL DEFAULT NULL
  AFTER `phone`,
  ADD COLUMN `activity_id` VARCHAR(45) NULL
  AFTER `consumer_id`;


ALTER TABLE `gmair_drift`.`drift_order`
  CHANGE COLUMN `activity_id` `activity_id` VARCHAR(45) NULL DEFAULT NULL
  AFTER `order_no`,
  ADD COLUMN `equip_id` VARCHAR(45) NULL
  AFTER `activity_id`;

ALTER TABLE `gmair_drift`.`drift_order`
  ADD COLUMN `expected_date` DATE NOT NULL
  AFTER `machine_orderNo`;

ALTER TABLE `gmair_drift`.`drift_order`
  ADD COLUMN `interval_date` INT NOT NULL
  AFTER `expected_date`;

ALTER TABLE `gmair_drift`.`drift_order`
  ADD COLUMN `real_pay` DOUBLE NULL
  AFTER `total_price`;

ALTER TABLE `gmair_drift`.`drift_order`
  ADD COLUMN `excode` VARCHAR(45) NULL
  AFTER `interval_date`;

ALTER TABLE `gmair_drift`.`drift_order`
  DROP COLUMN `order_no`;


ALTER TABLE `gmair_drift`.`drift_order_item`
  DROP COLUMN `test_target`;

## 2019-07-30
CREATE TABLE `gmair_drift`.`activity_notification` (
  `notification_id`      VARCHAR(20) NOT NULL,
  `activity_id`          VARCHAR(20) NOT NULL,
  `notification_context` LONGTEXT    NOT NULL,
  `block_flag`           TINYINT(1)  NOT NULL,
  `create_time`          DATETIME    NOT NULL,
  PRIMARY KEY (`notification_id`)
);

## 2019-8-14
CREATE TABLE `gmair_drift`.`activity_thumbnail` (
  `thumbnail_id`    VARCHAR(20) NOT NULL,
  `thumbnail_path`  VARCHAR(45) NOT NULL,
  `thumbnail_index` INT         NOT NULL,
  `block_flag`      TINYINT(1)  NOT NULL,
  `create_time`     DATETIME    NOT NULL,
  PRIMARY KEY (`thumbnail_id`)
);

ALTER TABLE `gmair_drift`.`activity_thumbnail`
  ADD COLUMN `activity_id` VARCHAR(20) NULL
  AFTER `thumbnail_id`;

## 2018-08-15
CREATE TABLE `gmair_drift`.`drift_promotion` (
  `promotion_id`          VARCHAR(20)  NOT NULL,
  `activity_id`           VARCHAR(20)  NOT NULL,
  `promotion_value`       DOUBLE       NOT NULL,
  `promotion_description` VARCHAR(100) NOT NULL,
  `block_flag`            TINYINT(1)   NOT NULL,
  `create_time`           DATETIME     NOT NULL,
  PRIMARY KEY (`promotion_id`)
);

## 2019-08-15
CREATE TABLE `gmair_drift`.`order_express` (
  `order_id`       VARCHAR(45) NULL,
  `express_id`     VARCHAR(45) NULL,
  `express_status` TINYINT(1)  NULL,
  `company`        VARCHAR(45) NULL,
  `block_flag`     TINYINT(1)  NULL,
  `create_time`    DATETIME(0) NULL
);

## 2019-08-21
ALTER TABLE `gmair_drift`.`drift_activity`
  ADD COLUMN `open_date` DATE NULL
  AFTER `create_time`;

ALTER TABLE `gmair_drift`.`drift_activity`
  ADD COLUMN `close_date` DATE NULL
  AFTER `open_date`;

ALTER TABLE `gmair_drift`.`drift_activity`
  ADD COLUMN `delay_days` INT NULL
  AFTER `close_date`;

## 2019-08-23

CREATE TABLE `drift_verification` (
  `verify_id`   VARCHAR(30) CHARACTER SET utf8
  COLLATE utf8_general_ci  NOT NULL,
  `id_card`     VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci  NOT NULL,
  `id_name`     VARCHAR(30) CHARACTER SET utf8
  COLLATE utf8_general_ci  NOT NULL,
  `block_flag`  TINYINT(1) NOT NULL,
  `create_time` DATETIME   NOT NULL,
  PRIMARY KEY (`verify_id`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

ALTER TABLE `gmair_drift`.`drift_verification`
  ADD COLUMN `openid` VARCHAR(50) NOT NULL
  AFTER `verify_id`;

#2019-09-03
ALTER TABLE `gmair_drift`.`attachment`
ADD COLUMN `set_meal` VARCHAR(45) NULL AFTER `attach_name`;

ALTER TABLE `gmair_drift`.`attachment`
ADD COLUMN `attach_single` INT NOT NULL AFTER `set_meal`;

ALTER TABLE `gmair_drift`.`drift_order_item`
ADD COLUMN `single_num` INT NOT NULL AFTER `item_name`;

#2019-09-04
CREATE TABLE `gmair_drift`.`drift_address` (
  `address_id` VARCHAR(45) NOT NULL,
  `consumer_id` VARCHAR(45) NOT NULL,
  `consignee` VARCHAR(45) NOT NULL,
  `province` VARCHAR(45) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  `district` VARCHAR(45) NOT NULL,
  `address_detail` VARCHAR(45) NOT NULL,
  `phone` VARCHAR(45) NOT NULL,
  `default_address` TINYINT(1) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`address_id`));

CREATE TABLE `gmair_drift`.`drift_user` (
  `user_id` VARCHAR(45) NOT NULL,
  `open_id` VARCHAR(50) NOT NULL,
  `nickname` VARCHAR(45) NOT NULL,
  `phone` VARCHAR(45) NOT NULL,
  `province` VARCHAR(45) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  `country` VARCHAR(45) NOT NULL,
  `user_sex` TINYINT(1) NOT NULL,
  `avatar_url` VARCHAR(45) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`user_id`));

ALTER TABLE `gmair_drift`.`drift_order_item`
ADD COLUMN `ex_quantity` INT NULL AFTER `quantity`;

ALTER TABLE `gmair_drift`.`drift_order_item`
CHANGE COLUMN `single_num` `single_num` INT(11) NULL ;

#2019-09-09

CREATE TABLE `gmair_drift`.`express_detail` (
  `detail_id` VARCHAR(45) NOT NULL,
  `context` VARCHAR(45) NOT NULL,
  `time` DATETIME NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `express_id` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`detail_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

#2019-10-10
ALTER TABLE `gmair_drift`.`order_express`
ADD COLUMN `express_num` VARCHAR(45) NOT NULL AFTER `block_flag`;



#2019-10-15
ALTER TABLE `gmair_drift`.`drift_activity`
ADD COLUMN `reservation_name` VARCHAR(45) NULL AFTER `text`,
ADD COLUMN `reservation_text` LONGTEXT NULL AFTER `reservation_name`;

ALTER TABLE `gmair_drift`.`drift_equipment`
ADD COLUMN `text` VARCHAR(45) NULL AFTER `create_time`;

ALTER TABLE `gmair_drift`.`drift_equipment`
ADD COLUMN `url` VARCHAR(45) NULL AFTER `text`;

ALTER TABLE `gmair_drift`.`drift_equipment`
ADD COLUMN `detail_url` VARCHAR(45) NULL AFTER `url`;

#2019-10-17
ALTER TABLE `gmair_drift`.`drift_order_item`
ADD COLUMN `text` VARCHAR(45) NULL AFTER `create_time`,
ADD COLUMN `url` VARCHAR(100) NULL AFTER `text`;

ALTER TABLE `gmair_drift`.`attachment`
ADD COLUMN `text` VARCHAR(45) NULL AFTER `attach_top`,
ADD COLUMN `url` VARCHAR(100) NULL AFTER `text`;

#2019-10-22
CREATE TABLE `gmair_drift`.`drift_cancel_order` (
  `order_id` VARCHAR(45) NOT NULL,
  `open_id` VARCHAR(45) NOT NULL,
  `price` DOUBLE NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;

ALTER TABLE `gmair_drift`.`drift_cancel_order`
ADD COLUMN `cancel_id` VARCHAR(45) NOT NULL FIRST,
ADD PRIMARY KEY (`cancel_id`);
;

ALTER TABLE `gmair_drift`.`drift_cancel_order`
ADD COLUMN `is_finish` TINYINT(1) NOT NULL AFTER `price`;

#2019-10-24
ALTER TABLE `gmair_drift`.`drift_order_item`
ADD COLUMN `total_price` DOUBLE NULL AFTER `item_price`,
ADD COLUMN `real_price` DOUBLE NULL AFTER `total_price`;



#2019-11-3
-- DROP VIEW if EXISTS order_activity_equipment_item_view;
--
-- CREATE VIEW `gmair_drift`.`order_activity_equipment_item_view`
-- AS SELECT drift_order.order_id as order_id,activity_name,drift_order.activity_id as activity_id,equip_name,drift_order.equip_id as equip_id,consumer_id,consignee,phone,province,address,city,district,concat(province,city,district,address) AS 'express_address',drift_order.total_price,real_pay,order_status,buy_machine,machine_orderNo,quantity,expected_date,interval_date,excode,drift_order.block_flag as block_flag,drift_order.create_time as create_time,express_num,company,express_status,description
-- FROM drift_order left join order_express on drift_order.order_id = order_express.order_id ,drift_activity,drift_equipment,drift_order_item
-- WHERE drift_order.activity_id = drift_activity.activity_id and drift_order.equip_id = drift_equipment.equip_id and drift_order.order_id = drift_order_item.order_id and item_name = '甲醛检测试纸'
-- ORDER BY expected_date

DROP VIEW if EXISTS order_activity_equipment_item_view;

CREATE VIEW `gmair_drift`.`order_activity_equipment_item_express_view`
AS SELECT drift_order.order_id as order_id,activity_name,drift_order.activity_id as activity_id,equip_name,drift_order.equip_id as equip_id,consumer_id,consignee,phone,province,address,city,district,concat(province,city,district,address) AS 'express_address',drift_order.total_price,drift_order.real_pay,order_status,buy_machine,machine_orderNo,quantity,drift_order_item.item_price ,drift_order_item.total_price as item_total_price,drift_order_item.real_price as item_real_price,expected_date,interval_date,excode,drift_order.block_flag as block_flag,drift_order.create_time as create_time,ex0.express_num as express_out_num,ex0.company as express_out_company,ex1.express_num as express_back_num,ex1.company as express_back_company,description
FROM ((drift_order left join (select * from order_express where order_express.express_status = 0) as ex0 on drift_order.order_id = ex0.order_id)left join (select * from order_express where order_express.express_status = 1) as ex1 on drift_order.order_id = ex1.order_id) left join drift_order_item on drift_order_item.order_id = drift_order.order_id ,drift_activity,drift_equipment
WHERE drift_order.activity_id = drift_activity.activity_id and drift_order.equip_id = drift_equipment.equip_id and drift_order.order_id = drift_order_item.order_id and item_name = '甲醛检测试纸'
ORDER BY expected_date

#2019-11-04
CREATE TABLE `gmair_drift`.`order_action` (
  `action_id` VARCHAR(45) NOT NULL,
  `order_id` VARCHAR(45) NOT NULL,
  `message` VARCHAR(100) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`action_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;

ALTER TABLE `gmair_drift`.`order_action`
ADD COLUMN `member` VARCHAR(45) NOT NULL AFTER `order_id`;
