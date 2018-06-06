#2018.04.28 create table for reconnaissance list
CREATE TABLE `gmair_install`.`reconnaissance_list` (
  `recon_id`     VARCHAR(20)  NOT NULL,
  `order_id`     VARCHAR(20)  NOT NULL,
  `setup_method` TINYINT(1)   NOT NULL DEFAULT 0,
  `description`  VARCHAR(100) NULL,
  `block_flag`   TINYINT(1)   NOT NULL DEFAULT 0,
  `create_time`  DATETIME     NOT NULL,
  PRIMARY KEY (`recon_id`)
);

ALTER TABLE `gmair_install`.`reconnaissance_list`
  ADD COLUMN `recon_status` TINYINT(1) NOT NULL DEFAULT 0 AFTER `description`;

ALTER TABLE `gmair_install`.`reconnaissance_list`
  ADD COLUMN `recon_date` DATE NOT NULL AFTER `recon_status`;

ALTER TABLE `gmair_install`.`reconnaissance_list`
  modify setup_method VARCHAR (31);

#2018.05.02 add some previous changes to the installation part

ALTER TABLE `gmair_install`.`team_member`
  ADD COLUMN `wechat_id` VARCHAR(50) NULL AFTER `member_phone`;

CREATE TABLE `gmair_install`.`install_pic` (
  `pic_id` VARCHAR(20) NOT NULL,
  `pic_address` VARCHAR(100) NOT NULL,
  `pic_md5` VARCHAR(45) NOT NULL,
  `member_phone` VARCHAR(20) NOT NULL,
  `copy_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `block_flag` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`pic_id`));

ALTER TABLE `gmair_install`.`install_feedback`
  ADD COLUMN `assign_id` VARCHAR(20) NOT NULL AFTER `feedback_id`,
  ADD COLUMN `status` VARCHAR(45) NOT NULL AFTER `member_phone`;

ALTER TABLE `gmair_install`.`install_snapshot`
  DROP COLUMN `hole_direction`,
  DROP COLUMN `indoor_post_air`,
  DROP COLUMN `indoor_pre_air`,
  DROP COLUMN `outdoor_hole`,
  DROP COLUMN `indoor_hole`,
  DROP COLUMN `check_list`,
  ADD COLUMN `pic_path` LONGTEXT NOT NULL AFTER `member_phone`;

alter table reconnaissance_list modify recon_date date default null;

ALTER TABLE `gmair_install`.`install_assign`
  CHANGE COLUMN `create_time` `create_time` DATETIME NOT NULL ,
  ADD COLUMN `assign_status` TINYINT(1) NOT NULL AFTER `member_id`;

ALTER TABLE `gmair_install`.`install_assign`
ADD COLUMN `consumer_consignee` VARCHAR(45) NOT NULL AFTER `create_time`,
ADD COLUMN `consumer_phone` VARCHAR(45) NOT NULL AFTER `consumer_consignee`,
ADD COLUMN `consumer_address` VARCHAR(255) NOT NULL AFTER `consumer_phone`;

#20180509 install assign default team id and assigndate can be null

ALTER TABLE `gmair_install`.`install_assign`
CHANGE COLUMN `team_id` `team_id` VARCHAR(20) NULL ;

ALTER TABLE `gmair_install`.`install_assign`
CHANGE COLUMN `assign_date` `assign_date` DATETIME NULL ;

#20180510 add member_role

ALTER TABLE `gmair_install`.`team_member`
ADD COLUMN `member_role` TINYINT(1) NOT NULL AFTER `wechat_id`;

#20180511 add table snapshot_location

CREATE TABLE `gmair_install`.`snapshot_location` (
  `location_id` VARCHAR(20) NOT NULL,
  `snapshot_id` VARCHAR(20) NOT NULL,
  `location_lng` DOUBLE NULL,
  `location_lat` DOUBLE NULL,
  `location_place` VARCHAR(200) NULL,
  PRIMARY KEY (`location_id`));

ALTER TABLE `gmair_install`.`snapshot_location`
ADD COLUMN `block_flag` TINYINT(1) NOT NULL AFTER `location_place`,
ADD COLUMN `create_time` DATETIME NOT NULL AFTER `block_flag`;

#20180515 rename member_phone to snapshot_id

ALTER TABLE `gmair_install`.`install_pic`
CHANGE COLUMN `member_phone` `snapshot_id` VARCHAR(45) NOT NULL ;

#20180517 add column occupied to install_pic, add column net to install_snapshot

ALTER TABLE `gmair_install`.`install_pic`
ADD COLUMN `occupied` TINYINT(1) NOT NULL DEFAULT '0' AFTER `copy_flag`;

ALTER TABLE `gmair_install`.`install_snapshot`
ADD COLUMN `net` TINYINT(1) NOT NULL AFTER `pic_path`;

#20180523 add view install_view

CREATE VIEW `gmair_install`.`assign_view` AS
    SELECT
        `gmair_install`.`install_assign`.`assign_id` AS `assign_id`,
        `gmair_install`.`install_assign`.`code_value` AS `code_value`,
        `gmair_install`.`install_team`.`team_name` AS `team_name`,
        `gmair_install`.`team_member`.`member_name` AS `member_name`,
        `gmair_install`.`install_assign`.`assign_status` AS `assign_status`,
        `gmair_install`.`install_assign`.`assign_date` AS `assign_date`,
        `gmair_install`.`install_assign`.`block_flag` AS `block_flag`,
        `gmair_install`.`install_assign`.`create_time` AS `create_time`,
        `gmair_install`.`install_assign`.`consumer_consignee` AS `consumer_consignee`,
        `gmair_install`.`install_assign`.`consumer_phone` AS `consumer_phone`,
        `gmair_install`.`install_assign`.`consumer_address` AS `consumer_address`
    FROM
        ((`gmair_install`.`install_assign`
        JOIN `gmair_install`.`install_team`)
        JOIN `gmair_install`.`team_member`)
    WHERE
        ((`gmair_install`.`install_assign`.`team_id` = `gmair_install`.`install_team`.`team_id`)
            AND (`gmair_install`.`install_assign`.`member_id` = `gmair_install`.`team_member`.`member_id`))

ALTER TABLE `gmair_install`.`install_snapshot`
ADD COLUMN `install_type` VARCHAR(45) NOT NULL AFTER `net`;

CREATE
VIEW `gmair_install`.`finished_view` AS
    SELECT
        `gmair_install`.`install_assign`.`assign_id` AS `assign_id`,
        `gmair_install`.`install_snapshot`.`pic_path` AS `pic_path`,
        `gmair_install`.`install_snapshot`.`install_type` AS `install_type`,
        `gmair_install`.`snapshot_location`.`location_place` AS `location_place`,
        `gmair_install`.`install_assign`.`assign_date` AS `assign_date`,
        `gmair_install`.`install_assign`.`block_flag` AS `block_flag`,
        `gmair_install`.`install_assign`.`create_time` AS `create_time`
    FROM
        ((`gmair_install`.`install_assign`
        JOIN `gmair_install`.`install_snapshot`)
        JOIN `gmair_install`.`snapshot_location`)
    WHERE
        ((`gmair_install`.`install_assign`.`assign_id` = `gmair_install`.`install_snapshot`.`assign_id`)
            AND (`gmair_install`.`install_snapshot`.`snapshot_id` = `gmair_install`.`snapshot_location`.`snapshot_id`))

##2018-05-30
CREATE OR REPLACE VIEW `gmair_install`.`team_member_view` AS
  SELECT
    tm.member_id,
    tm.member_name,
    tm.member_phone,
    tm.wechat_id,
    tm.member_role,
    it.team_name
  FROM
    gmair_install.team_member tm
    LEFT JOIN
    gmair_install.install_team it ON tm.block_flag = 0
                                     AND it.team_id = tm.team_id

CREATE OR REPLACE VIEW `gmair_install`.`team_view` AS
  SELECT
    it.team_id,
    it.team_name,
    team_area,
    team_description,
    IFNULL(COUNT(tm.member_id), 0) AS team_member_count
  FROM
    gmair_install.install_team it
    LEFT JOIN
    gmair_install.team_member tm ON it.team_id = tm.team_id
                                    AND tm.block_flag = 0
  GROUP BY it.team_id

# update view
DROP  VIEW assign_view;
CREATE
    ALGORITHM = UNDEFINED
    DEFINER = `root`@`localhost`
    SQL SECURITY DEFINER
VIEW `assign_view` AS
    SELECT
        `install_assign`.`assign_id` AS `assign_id`,
        `install_assign`.`code_value` AS `code_value`,
        `install_team`.`team_name` AS `team_name`,
        `team_member`.`member_name` AS `member_name`,
        `install_assign`.`assign_status` AS `assign_status`,
        `install_assign`.`assign_date` AS `assign_date`,
        `install_assign`.`block_flag` AS `block_flag`,
        `install_assign`.`create_time` AS `create_time`,
        `install_assign`.`consumer_consignee` AS `consumer_consignee`,
        `install_assign`.`consumer_phone` AS `consumer_phone`,
        `install_assign`.`consumer_address` AS `consumer_address`
    FROM
        ((`install_assign`
        LEFT JOIN `install_team` ON ((`install_assign`.`team_id` = `install_team`.`team_id`)))
        LEFT JOIN `team_member` ON ((`install_assign`.`member_id` = `team_member`.`member_id`)));

#2018.06.06
ALTER TABLE `gmair_install`.`install_feedback`
  DROP COLUMN `status`,
  DROP COLUMN `member_phone`, RENAME TO  `gmair_install`.`install_cancel_feedback` ;

ALTER TABLE `gmair_install`.`install_cancel_feedback`
RENAME TO  `gmair_install`.`install_feedback` ;
