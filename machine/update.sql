USE `gmair_machine` ;
## create machine power setting view
CREATE VIEW `gmair_machine`.`power_setting_view`
AS
SELECT
    power_setting.setting_id     AS setting_id,
    machine_setting.consumer_id  AS consumer_id,
    machine_setting.setting_name AS setting_name,
    machine_setting.code_value   AS code_value,
    power_setting.power_action   AS power_action,
    power_setting.trigger_hour   AS trigger_hour,
    power_setting.trigger_minute AS trigger_minute
FROM
    gmair_machine.power_setting, gmair_machine.machine_setting
WHERE power_setting.setting_id = machine_setting.setting_id
  AND power_setting.block_flag = 0
  AND machine_setting.block_flag = 0;

# 2018-06-12 modify machine_monthly_status
ALTER TABLE `gmair_machine`.`machine_monthly_status`
    CHANGE COLUMN `record_date` `index` INT NOT NULL;

ALTER TABLE `gmair_machine`.`machine_daily_status`
    ADD COLUMN `status_id` VARCHAR(45) NOT NULL
        AFTER `create_time`,
    DROP PRIMARY KEY,
    ADD PRIMARY KEY (`status_id`);
ALTER TABLE `gmair_machine`.`machine_daily_status`
    CHANGE COLUMN `status_id` `status_id` VARCHAR(45) NOT NULL
        FIRST;

ALTER TABLE `gmair_machine`.`machine_hourly_status`
    ADD COLUMN `machine_id` VARCHAR(45) NOT NULL
        AFTER `status_id`;

ALTER TABLE `gmair_machine`.`machine_monthly_status`
    ADD COLUMN `status_id` VARCHAR(45) NOT NULL
        FIRST,
    DROP PRIMARY KEY,
    ADD PRIMARY KEY (`status_id`);

## 2018-06-14 create machine volume setting view
CREATE VIEW `gmair_machine`.`volume_setting_view`
AS
SELECT
    volume_setting.setting_id    AS setting_id,
    machine_setting.consumer_id  AS consumer_id,
    machine_setting.setting_name AS setting_name,
    machine_setting.code_value   AS code_value,
    volume_setting.floor_pm2_5   AS floor_pm2_5,
    volume_setting.upper_pm2_5   AS upper_pm2_5,
    volume_setting.speed_value   AS speed_value
FROM
    gmair_machine.volume_setting, gmair_machine.machine_setting
WHERE volume_setting.setting_id = machine_setting.setting_id
  AND volume_setting.block_flag = 0
  AND machine_setting.block_flag = 0;

## 2018-06-15 create machine light setting view
CREATE VIEW `gmair_machine`.`light_setting_view`
AS
SELECT
    light_setting.setting_id     AS setting_id,
    machine_setting.consumer_id  AS consumer_id,
    machine_setting.setting_name AS setting_name,
    machine_setting.code_value   AS code_value,
    light_setting.light_value    AS light_value
FROM
    gmair_machine.light_setting, gmair_machine.machine_setting
WHERE light_setting.setting_id = machine_setting.setting_id
  AND light_setting.block_flag = 0
  AND machine_setting.block_flag = 0;

#2018-06-19 add table board_version
CREATE TABLE `gmair_machine`.`board_version` (
                                                 `machine_id`    VARCHAR(45) NOT NULL,
                                                 `board_version` INT         NOT NULL DEFAULT 0,
                                                 `block_flag`    TINYINT(1)  NOT NULL DEFAULT 0,
                                                 `create_time`   DATETIME    NOT NULL,
                                                 PRIMARY KEY (`machine_id`)
);

#2018-06-21 add control table
CREATE TABLE `gmair_machine`.`control_option` (
                                                  `option_id`        VARCHAR(20) NOT NULL,
                                                  `option_name`      VARCHAR(45) NOT NULL,
                                                  `option_component` VARCHAR(45) NOT NULL,
                                                  `block_flag`       TINYINT(1)  NOT NULL DEFAULT 0,
                                                  `create_time`      DATETIME    NOT NULL,
                                                  PRIMARY KEY (`option_id`)
);

CREATE TABLE `gmair_machine`.`control_option_action` (
                                                         `value_id`        VARCHAR(20) NOT NULL,
                                                         `control_id`      VARCHAR(20) NOT NULL,
                                                         `action_name`     VARCHAR(45) NOT NULL,
                                                         `action_operator` VARCHAR(45) NOT NULL,
                                                         `block_flag`      TINYINT(1)  NOT NULL DEFAULT 0,
                                                         `create_time`     DATETIME    NOT NULL,
                                                         PRIMARY KEY (`value_id`)
);

ALTER TABLE `gmair_machine`.`control_option_action`
    ADD COLUMN `model_id` VARCHAR(20) NOT NULL
        AFTER `control_id`;

#2018-06-25
ALTER TABLE `gmair_machine`.`pre_bind`
    ADD COLUMN `board_version` INT NOT NULL DEFAULT 0
        AFTER `code_value`;

ALTER TABLE `gmair_machine`.`pre_bind`
    DROP COLUMN `board_version`;

#2018-06-28
ALTER TABLE `gmair_machine`.`control_option_action`
    ADD COLUMN `command_value` INT NOT NULL DEFAULT 0
        AFTER `action_operator`;

#2018-07-11 add table machine_default_location
CREATE TABLE `gmair_machine`.`machine_default_location` (
                                                            `location_id` INT         NOT NULL,
                                                            `city_id`     VARCHAR(20) NOT NULL,
                                                            `code_value`  VARCHAR(45) NOT NULL,
                                                            `block_flag`  VARCHAR(45) NOT NULL,
                                                            `create_time` VARCHAR(45) NOT NULL,
                                                            PRIMARY KEY (`location_id`)
);

ALTER TABLE `gmair_machine`.`machine_default_location`
    CHANGE COLUMN `location_id` `location_id` VARCHAR(20) NOT NULL;

ALTER TABLE `gmair_machine`.`machine_default_location`
    CHANGE COLUMN `block_flag` `block_flag` TINYINT(1) NOT NULL,
    CHANGE COLUMN `create_time` `create_time` DATETIME NOT NULL;

#2018-07-25
ALTER TABLE `gmair_machine`.`code_machine_bind`
    CHANGE COLUMN `bind_id` `bind_id` VARCHAR(20) NOT NULL;

#2018-07-30 add table pm2_5_boundary
CREATE TABLE `gmair_machine`.`pm2_5_boundary` (
                                                  `boundary_id` VARCHAR(20) NOT NULL,
                                                  `model_id`    VARCHAR(20) NULL,
                                                  `pm2_5`       DOUBLE      NULL,
                                                  `create_time` DATETIME    NULL,
                                                  `block_flag`  TINYINT(1)  NULL,
                                                  PRIMARY KEY (`boundary_id`)
);

ALTER TABLE `gmair_machine`.`pm2_5_boundary`
    CHANGE COLUMN `model_id` `model_id` VARCHAR(20) NOT NULL,
    CHANGE COLUMN `pm2_5` `pm2_5` DOUBLE NOT NULL,
    CHANGE COLUMN `create_time` `create_time` DATETIME NOT NULL,
    CHANGE COLUMN `block_flag` `block_flag` TINYINT(1) NOT NULL;

#2018-07-31 add table pm2_5_latest
CREATE TABLE `gmair_machine`.`pm2_5_latest` (
                                                `latest_id`   VARCHAR(20) NOT NULL,
                                                `machine_id`  VARCHAR(20) NOT NULL,
                                                `pm2_5`       DOUBLE      NOT NULL,
                                                `create_time` DATETIME    NOT NULL,
                                                `block_flag`  TINYINT(1)  NOT NULL,
                                                PRIMARY KEY (`latest_id`)
);

#2018-08-01
ALTER TABLE `gmair_machine`.`pm2_5_latest`
    CHANGE COLUMN `pm2_5` `pm2_5` INT NOT NULL;

ALTER TABLE `gmair_machine`.`pm2_5_boundary`
    CHANGE COLUMN `pm2_5` `pm2_5_info` INT NOT NULL,
    ADD COLUMN `pm2_5_warning` INT NOT NULL
        AFTER `pm2_5_info`;

#2018-08-03 add table
CREATE TABLE `gmair_machine`.`model_light_config` (
                                                      `config_id`   VARCHAR(20) NOT NULL,
                                                      `model_id`    VARCHAR(20) NOT NULL,
                                                      `min_light`   INT(11)     NOT NULL,
                                                      `max_light`   INT(11)     NOT NULL,
                                                      `block_flag`  TINYINT(1)  NOT NULL,
                                                      `create_time` DATETIME    NOT NULL,
                                                      PRIMARY KEY (`config_id`)
);

#2018-10-23 add table
CREATE TABLE `out_pm2_5_daily` (
                                   `record_id`    VARCHAR(20) NOT NULL,
                                   `machine_id`   VARCHAR(20) NOT NULL,
                                   `average_pm25` DOUBLE      NOT NULL DEFAULT '0',
                                   `over_count`   TINYINT(5)  NOT NULL DEFAULT '0',
                                   `block_flag`   TINYINT(5)  NOT NULL,
                                   `create_time`  DATETIME    NOT NULL,
                                   PRIMARY KEY (`record_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

CREATE TABLE `filter_limit_config` (
                                       `over_count_limit` INT(11) NOT NULL,
                                       `over_pm25_limit`  INT(11) NOT NULL,
                                       PRIMARY KEY (`over_count_limit`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

CREATE TABLE `gmair_machine`.`filter_light` (
                                                `machine_id`   VARCHAR(20) NOT NULL,
                                                `light_status` TINYINT(5)  NOT NULL,
                                                `block_flag`   TINYINT(5)  NOT NULL,
                                                `create_time`  DATETIME    NOT NULL,
                                                PRIMARY KEY (`machine_id`)
);

ALTER TABLE `gmair_machine`.`pm2_5_latest`
    RENAME TO `gmair_machine`.`out_pm2_5_hourly`;

ALTER TABLE `gmair_machine`.`out_pm2_5_hourly`
    ADD COLUMN `index` INT(11) NOT NULL
        AFTER `pm2_5`;

ALTER TABLE `gmair_machine`.`out_pm2_5_hourly`
    CHANGE COLUMN `index` `index_hour` INT(11) NOT NULL;

##2018-11-26
CREATE TABLE `gmair_machine`.`machine_on_off` (
                                                  `config_id`   VARCHAR(45) NOT NULL,
                                                  `uid`         VARCHAR(45) NOT NULL,
                                                  `status`      TINYINT(1)  NOT NULL,
                                                  `start_time`  TIME DEFAULT NULL,
                                                  `end_time`    TIME DEFAULT NULL,
                                                  `block_flag`  TINYINT(1)  NOT NULL,
                                                  `create_time` DATETIME    NOT NULL,
                                                  PRIMARY KEY (`config_id`)
);

#2018-01-02
CREATE TABLE `machine_daily_power` (
                                       `status_id`   VARCHAR(25) NOT NULL,
                                       `machine_id`  VARCHAR(45) NOT NULL,
                                       `power_usage` VARCHAR(45) NOT NULL,
                                       `block_flag`  TINYINT(1)  NOT NULL,
                                       `create_time` DATETIME    NOT NULL,
                                       PRIMARY KEY (`status_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

#2018-01-14
CREATE TABLE `machine_list_daily` (
                                      `consumer_id`    VARCHAR(45) NOT NULL,
                                      `bind_name`      VARCHAR(45) NOT NULL,
                                      `code_value`     VARCHAR(45) NOT NULL,
                                      `machine_id`     VARCHAR(45) NULL,
                                      `consumer_name`  VARCHAR(45) NULL,
                                      `consumer_phone` VARCHAR(45) NULL,
                                      `over_count`     INT(11)     NOT NULL DEFAULT '0',
                                      `offline`        TINYINT(1)  NOT NULL DEFAULT '0',
                                      `block_flag`     TINYINT(10) NOT NULL,
                                      `create_time`    DATETIME    NOT NULL,
                                      PRIMARY KEY (`consumer_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

#2018-01-14
CREATE
    ALGORITHM = UNDEFINED
    DEFINER = `root`@`localhost`
    SQL SECURITY DEFINER
    VIEW `gmair_machine`.`machine_list_view` AS
SELECT
    `ccb`.`consumer_id`  AS `consumer_id`,
    `ccb`.`bind_name`    AS `bind_name`,
    `ccb`.`code_value`   AS `code_value`,
    `cmb`.`machine_id`   AS `machine_id`,
    `ci`.`consumer_name` AS `consumer_name`,
    `cp`.`phone_number`  AS `consumer_phone`,
    `ccb`.`block_flag`   AS `block_flag`,
    `ccb`.`create_time`  AS `create_time`
FROM
    (((`gmair_machine`.`consumer_code_bind` `ccb`
        LEFT JOIN `gmair_machine`.`code_machine_bind` `cmb` ON ((`ccb`.`code_value` = `cmb`.`code_value`)))
        LEFT JOIN `gmair_userinfo`.`consumer_info` `ci` ON ((`ccb`.`consumer_id` = `ci`.`consumer_id`)))
        LEFT JOIN `gmair_userinfo`.`consumer_phone` `cp` ON ((`ccb`.`consumer_id` = `cp`.`consumer_id`)))
WHERE
    ((`ccb`.`ownership` = 0)
        AND (`ccb`.`block_flag` = 0));

#2018-01-15
CREATE
    ALGORITHM = UNDEFINED
    DEFINER = `root`@`localhost`
    SQL SECURITY DEFINER
    VIEW `gmair_machine`.`machine_list_second_view` AS
SELECT
    `ccb`.`consumer_id`  AS `consumer_id`,
    `ccb`.`bind_name`    AS `bind_name`,
    `ccb`.`code_value`   AS `code_value`,
    `cmb`.`machine_id`   AS `machine_id`,
    `ci`.`consumer_name` AS `consumer_name`,
    `cp`.`phone_number`  AS `consumer_phone`,
    `opd`.`over_count`   AS `over_count`,
    `ccb`.`block_flag`   AS `block_flag`,
    `ccb`.`create_time`  AS `create_time`,
    `opd`.`create_time`  AS `out_pm25_time`
FROM
    ((((`gmair_machine`.`consumer_code_bind` `ccb`
        LEFT JOIN `gmair_machine`.`code_machine_bind` `cmb` ON ((`ccb`.`code_value` = `cmb`.`code_value`)))
        LEFT JOIN `gmair_userinfo`.`consumer_info` `ci` ON ((`ccb`.`consumer_id` = `ci`.`consumer_id`)))
        LEFT JOIN `gmair_userinfo`.`consumer_phone` `cp` ON ((`ccb`.`consumer_id` = `cp`.`consumer_id`)))
        LEFT JOIN `gmair_machine`.`out_pm2_5_daily` `opd` ON ((`opd`.`machine_id` = `cmb`.`machine_id`)))
WHERE
    ((`ccb`.`ownership` = 0)
        AND (`ccb`.`block_flag` = 0)
        AND ((TO_DAYS(CURDATE()) - TO_DAYS(`opd`.`create_time`)) < 1));

#2019-11-04
ALTER TABLE `gmair_machine`.`filter_limit_config`
    ADD COLUMN `config_id` VARCHAR(20) NOT NULL
        FIRST,
    ADD COLUMN `model_id` VARCHAR(20) NOT NULL
        AFTER `config_id`,
    ADD COLUMN `duration` INT NOT NULL DEFAULT 1
        AFTER `over_pm25_limit`,
    ADD COLUMN `block_flag` TINYINT(1) NOT NULL DEFAULT 0
        AFTER `duration`,
    ADD COLUMN `create_time` DATETIME NOT NULL
        AFTER `block_flag`;

ALTER TABLE `gmair_machine`.`filter_limit_config`
    DROP PRIMARY KEY,
    ADD PRIMARY KEY (`config_id`);

#2019-12-14
CREATE OR REPLACE VIEW `goods_model_view` AS
SELECT
    `g`.`goods_id`          AS `goods_id`,
    `g`.`goods_name`        AS `goods_name`,
    `g`.`goods_description` AS `goods_description`,
    `gm`.`model_id`         AS `model_id`,
    `gm`.`model_code`       AS `model_code`,
    `gm`.`model_name`       AS `model_name`,
    `gm`.`model_abbr`       AS `model_abbr`,
    `gm`.`model_thumbnail`       AS `model_thumbnail`,
    `gm`.`model_bg`       AS `model_bg`
FROM
    (`goods_model` `gm`
        LEFT JOIN `goods` `g` ON ((`gm`.`goods_id` = `g`.`goods_id`)));

#2019-12-31
CREATE
    ALGORITHM = UNDEFINED
    DEFINER = `root`@`localhost`
    SQL SECURITY DEFINER
    VIEW `consumer_machine_bind_view` AS
SELECT
    `gmair_machine`.`consumer_code_bind`.`bind_id` AS `bind_id`,
    `gmair_machine`.`consumer_code_bind`.`consumer_id` AS `consumer_id`,
    `gmair_machine`.`consumer_code_bind`.`bind_name` AS `bind_name`,
    `gmair_machine`.`consumer_code_bind`.`code_value` AS `code_value`,
    `gmair_machine`.`consumer_code_bind`.`ownership` AS `ownership`,
    `gmair_machine`.`consumer_code_bind`.`block_flag` AS `block_flag`,
    `gmair_machine`.`consumer_code_bind`.`create_time` AS `create_time`,
    `gmair_userinfo`.`consumer_info`.`consumer_name` AS `consumer_name`,
    `gmair_userinfo`.`consumer_phone`.`phone_number` AS `phone`
FROM
    ((`gmair_machine`.`consumer_code_bind`
        JOIN `gmair_userinfo`.`consumer_info`)
        JOIN `gmair_userinfo`.`consumer_phone`)
WHERE
    ((`gmair_machine`.`consumer_code_bind`.`block_flag` = 0)
        AND (`gmair_machine`.`consumer_code_bind`.`consumer_id` = `gmair_userinfo`.`consumer_info`.`consumer_id`)
        AND (`gmair_machine`.`consumer_code_bind`.`consumer_id` = `gmair_userinfo`.`consumer_phone`.`consumer_id`));

#2020-07-04
CREATE TABLE `machine_filter_clean` (

                                        `qr_code`                 VARCHAR(255) NOT NULL,
                                        `is_need_clean`           BOOLEAN NOT NULL DEFAULT FALSE,
                                        `clean_remind_status`     BOOLEAN NOT NULL DEFAULT TRUE,
                                        `is_reminded`             BOOLEAN NOT NULL DEFAULT FALSE,
                                        `last_confirm_time`       DATETIME NOT NULL,
                                        `block_flag`              BOOLEAN NOT NULL DEFAULT FALSE,
                                        `create_time`             DATETIME NOT NULL DEFAULT current_timestamp(),
                                        PRIMARY KEY (`qr_code`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

#2020-07-06
insert into machine_filter_clean (`qr_code`, `last_confirm_time`)
select `code_value`, min(`create_time`) from `code_machine_bind` where `block_flag` = 0
group by `code_value`;

delete from machine_filter_clean where qr_code in
                                       (select qr_code from
                                           (select mc.qr_code from machine_filter_clean mc,qrcode qr,goods_model gm
                                            where mc.qr_code=qr.code_value and qr.model_id=gm.model_id and gm.goods_id <> 'GUO20180607ggxi8a96') temp);

update machine_filter_clean set `is_need_clean` = true where `block_flag` = 0
                                                         and `is_need_clean` = false
                                                         and (unix_timestamp() - unix_timestamp(`last_confirm_time`)) >= (30 * 24 * 60 * 60);

#2020-07-18
CREATE TABLE `machine_turbo_volume` (

                                        `qr_code`                 VARCHAR(255) NOT NULL,
                                        `turbo_volume_status`     BOOLEAN NOT NULL DEFAULT false,
                                        `block_flag`              BOOLEAN NOT NULL DEFAULT FALSE,
                                        `create_time`             DATETIME NOT NULL DEFAULT current_timestamp(),
                                        PRIMARY KEY (`qr_code`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

insert into machine_turbo_volume (`qr_code`)
select distinct cmb.code_value from code_machine_bind cmb,qrcode q,model_volume_config mlc
where cmb.code_value = q.code_value and q.model_id = mlc.model_id and mlc.block_flag = 0 and
        cmb.block_flag = 0 and q.block_flag = 0 and mlc.turbo_volume is not null;

# 2020-07-27
# machine模块新增一张数据表，记录设备型号(model_id)对应的耗材名称和耗材购买链接
create table map_model_material
(
    mmm_id        int auto_increment comment '主键id'
        primary key,
    model_id      varchar(20)  null comment '设备型号',
    material_name varchar(50)  null comment '耗材名称',
    material_link varchar(100) null comment '耗材购买链接',
    create_time   datetime     null comment '数据行创建时间',
    modify_time   datetime     null comment '数据行修改时间'
)
    comment 'modelId对应的耗材信息';
     
#2020-07-24
CREATE TABLE `machine_efficient_filter` (

    `qr_code`                 VARCHAR(255) NOT NULL,
    `replace_status`          tinyint NOT NULL DEFAULT 0,
    `replace_remind_on`       BOOLEAN NOT NULL DEFAULT TRUE,
    `is_reminded_status`      tinyint NOT NULL DEFAULT 0,
    `block_flag`              BOOLEAN NOT NULL DEFAULT FALSE,
    `create_time`             DATETIME NOT NULL,
    PRIMARY KEY (`qr_code`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;
    
insert ignore into machine_efficient_filter (qr_code,create_time)
    select cmb.code_value,current_timestamp() from code_machine_bind cmb,qrcode q 
    where cmb.code_value = q.code_value
    and cmb.block_flag = 0 and q.block_flag = 0 
    and q.model_id in (select model_id from model_efficient_config);

#2020-08-06
CREATE TABLE `model_efficient_config` (
    `config_id`               VARCHAR(255) NOT NULL,
    `model_id`                VARCHAR(255) NOT NULL,
    `first_remind`            int,
    `second_remind`           int,
    `reset_hour`              int NOT NULL DEFAULT 0,
    `block_flag`              BOOLEAN NOT NULL DEFAULT FALSE,
    `create_time`             DATETIME NOT NULL,
    PRIMARY KEY (`config_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;

INSERT ignore into `model_efficient_config` VALUES ('CFG00000001','MOD20200718g4l9xy79',120,60,2160,0,'2020-08-06 14:46:27'),('CFG00000002','MOD20180717nuya5y41',120,60,2160,0,'2020-08-06 14:46:27'),('CFG00000003','MOD20190521n283i850',120,60,2160,0,'2020-08-06 14:46:27'),('CFG00000004','MOD20190527vy5xr321',120,60,2160,0,'2020-08-06 14:46:27'),('CFG00000005','MOD201910193io6yr97',120,60,2160,0,'2020-08-06 14:46:27'),('CFG00000006','MOD20191019luv3ov78',120,60,2160,0,'2020-08-06 14:46:27'),('CFG00000007','MOD20191019nzxei777',120,60,2160,0,'2020-08-06 14:46:27'),('CFG00000008','MOD20191019or6ze589',120,60,2160,0,'2020-08-06 14:46:27');

#2020-08-27
CREATE TABLE `machine_efficient_information` (
    `qr_code`                 VARCHAR(255) NOT NULL,
    `last_confirm_time`       DATETIME NOT NULL,
    `running`                 int NOT NULL DEFAULT 0,
    `conti`                   int NOT NULL DEFAULT 0,
    `abnormal`                int NOT NULL DEFAULT 0,
    `block_flag`              BOOLEAN NOT NULL DEFAULT FALSE,
    `create_time`             DATETIME NOT NULL,
    PRIMARY KEY (`qr_code`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;
    
#2020-9-24
insert ignore into machine_efficient_information (qr_code,last_confirm_time,create_time)
    select cmb.code_value,min(cmb.create_time),current_timestamp() from code_machine_bind cmb,qrcode q 
    where cmb.code_value = q.code_value
    and cmb.block_flag = 0 and q.block_flag = 0 
    and q.model_id in (select model_id from model_efficient_config where reset_hour = 0)
    group by cmb.code_value;

# 2020年12月23日
DROP TABLE IF EXISTS `filter`;
CREATE TABLE `filter`  (
                           `filter_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
                           `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '滤网名称',
                           `link` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '滤网链接',
                           `remarks` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                           PRIMARY KEY (`filter_id`) USING BTREE,
                           UNIQUE INDEX `filter_filter_id_uindex`(`filter_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '滤网链接表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of filter
-- ----------------------------
INSERT INTO `filter` VALUES ('FL00000001', '420抗菌滤芯', 'https://item.jd.com/66405474940.html', '同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000002', '420活性炭滤芯', 'https://item.jd.com/66405474941.html', '同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000003', '420标准HEPA滤芯', 'https://item.jd.com/66405474939.html', '同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000004', '420滤网套餐', 'https://item.jd.com/10022943605015.html', '同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000005', '280滤芯单片', 'https://item.jd.com/10021647370757.html', '同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000006', '280滤芯套餐', 'https://item.jd.com/10021647370758.htm', '同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000007', '280滤芯套餐', 'https://item.jd.com/10022944513072.html', '和280在同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000008', '500玻纤滤网', 'https://item.jd.com/10020146162562.html', NULL);
INSERT INTO `filter` VALUES ('FL00000009', '抗菌滤网适用320', 'https://item.jd.com/32252756483.html', '抗菌滤网：同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000010', '抗菌滤网适用320PRO', 'https://item.jd.com/65526149226.html', '抗菌滤网：同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000011', '抗菌滤网适用420', 'https://item.jd.com/32252756484.html', '抗菌滤网：同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000012', '抗菌滤网适用520/500', 'https://item.jd.com/49011804478.html', '抗菌滤网：同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000013', 'HEPA滤网 适用GM320', 'https://item.jd.com/32252410937.html', 'HEPA滤网：同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000014', 'HEPA滤网 适用GM320Pro', 'https://item.jd.com/65526137321.html', 'HEPA滤网：同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000015', 'HEPA滤网 适用GM420', 'https://item.jd.com/32252410938.html', 'HEPA滤网：同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000016', 'HEPA滤网 适用GM500/520', 'https://item.jd.com/49010639259.html', 'HEPA滤网：同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000017', '活性炭滤网 适用GM320', 'https://item.jd.com/32252195714.html', '活性炭滤网：同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000018', '活性炭滤网 适用GM320Pro', 'https://item.jd.com/65526108894.htm', '活性炭滤网：同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000019', '活性炭滤网 适用GM420', 'https://item.jd.com/32252195715.html', '活性炭滤网：同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000020', '活性炭滤网 适用GM520/GM500', 'https://item.jd.com/49007895093.html', '活性炭滤网：同一个SPU中');
INSERT INTO `filter` VALUES ('FL00000021', '无叶风扇除霾滤芯', 'https://item.jd.com/71801924241.html', NULL);
INSERT INTO `filter` VALUES ('FL00000022', '无叶风扇除醛滤芯', 'https://item.jd.com/71798815450.html', NULL);

DROP TABLE IF EXISTS `map_model_material`;
DROP TABLE IF EXISTS `map_model_filter`;
CREATE TABLE `map_model_filter`  (
                                     `mmm_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
                                     `model_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机器型号ID',
                                     `filter_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '滤网主键ID',
                                     `create_time` datetime NULL DEFAULT NULL COMMENT '数据行创建时间',
                                     `modify_time` datetime NULL DEFAULT NULL COMMENT '数据行修改时间',
                                     PRIMARY KEY (`mmm_id`) USING BTREE,
                                     UNIQUE INDEX `map_model_filter_mmm_id_uindex`(`mmm_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '不同型号机器对应的滤网信息' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of map_model_filter
-- ----------------------------
INSERT INTO `map_model_filter` VALUES ('MMM00000001', 'MOD20191210iu8h6y78', 'FL00000021', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000002', 'MOD20191210iu8h6y78', 'FL00000022', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000003', 'MOD202005114la9yn31', 'FL00000021', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000004', 'MOD202005114la9yn31', 'FL00000022', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000005', 'MOD20200511ia34nv24', 'FL00000021', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000006', 'MOD20200511ia34nv24', 'FL00000022', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000007', 'MOD20200718g4l9xy79', 'FL00000005', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000008', 'MOD20200718g4l9xy79', 'FL00000006', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000009', 'MOD20200718g4l9xy79', 'FL00000007', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000010', 'MODxyyirx74', 'FL00000009', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000011', 'MODxyyirx74', 'FL00000013', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000012', 'MODxyyirx74', 'FL00000017', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000013', 'MODxyyirx75', 'FL00000009', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000014', 'MODxyyirx75', 'FL00000013', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000015', 'MODxyyirx75', 'FL00000017', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000016', 'MOD20191019luv3ov78', 'FL00000010', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000017', 'MOD20191019luv3ov78', 'FL00000014', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000018', 'MOD20191019luv3ov78', 'FL00000018', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000019', 'MOD20180717nuya5y40', 'FL00000001', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000020', 'MOD20180717nuya5y40', 'FL00000002', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000021', 'MOD20180717nuya5y40', 'FL00000003', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000022', 'MOD20180717nuya5y40', 'FL00000004', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000023', 'MOD20180717nuya5y40', 'FL00000011', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000024', 'MOD20180717nuya5y40', 'FL00000015', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000025', 'MOD20180717nuya5y40', 'FL00000019', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000026', 'MOD20180717nuya5y41', 'FL00000001', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000027', 'MOD20180717nuya5y41', 'FL00000002', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000028', 'MOD20180717nuya5y41', 'FL00000003', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000029', 'MOD20180717nuya5y41', 'FL00000004', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000030', 'MOD20180717nuya5y41', 'FL00000011', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000031', 'MOD20180717nuya5y41', 'FL00000015', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000032', 'MOD20180717nuya5y41', 'FL00000019', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000033', 'MOD20190521n283i850', 'FL00000001', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000034', 'MOD20190521n283i850', 'FL00000002', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000035', 'MOD20190521n283i850', 'FL00000003', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000036', 'MOD20190521n283i850', 'FL00000004', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000037', 'MOD20190521n283i850', 'FL00000011', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000038', 'MOD20190521n283i850', 'FL00000015', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000039', 'MOD20190521n283i850', 'FL00000019', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000040', 'MOD20191019or6ze589', 'FL00000008', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000041', 'MOD20191019or6ze589', 'FL00000012', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000042', 'MOD20191019or6ze589', 'FL00000016', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000043', 'MOD20191019or6ze589', 'FL00000020', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000044', 'MOD201903054zaw270', 'FL00000012', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000045', 'MOD201903054zaw270', 'FL00000016', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000046', 'MOD201903054zaw270', 'FL00000020', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000047', 'MOD20190527vy5xr321', 'FL00000012', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000048', 'MOD20190527vy5xr321', 'FL00000016', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
INSERT INTO `map_model_filter` VALUES ('MMM00000049', 'MOD20190527vy5xr321', 'FL00000020', '2020-12-23 14:02:08', '2020-12-23 14:02:08');
    
#2020-10-29
CREATE TABLE `machine_filter_text` (
    `text_id`                 VARCHAR(255) NOT NULL,
    `text_content`            VARCHAR(255) NOT NULL,
    `block_flag`              BOOLEAN NOT NULL DEFAULT FALSE,
    `create_time`             DATETIME NOT NULL,
    PRIMARY KEY (`qr_code`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8;
