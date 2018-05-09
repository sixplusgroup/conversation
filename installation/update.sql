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

#20180509 install assign default team id can be null

ALTER TABLE `gmair_install`.`install_assign`
CHANGE COLUMN `team_id` `team_id` VARCHAR(20) NULL ;
