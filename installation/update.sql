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
