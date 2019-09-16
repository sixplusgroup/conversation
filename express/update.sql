#2018.04.21
ALTER TABLE `gmair_express`.`express_company`
  ADD COLUMN `company_code` VARCHAR(45) NOT NULL
  AFTER `company_name`;

ALTER TABLE `gmair_express`.`parcel_express`
  CHANGE COLUMN `code_value` `code_value` VARCHAR(45) NULL,
  ADD COLUMN `parcel_type` TINYINT(1) NOT NULL DEFAULT 0
  AFTER `express_status`;

#2018.04.27
ALTER TABLE `gmair_express`.`order_express`
  ADD COLUMN `deliver_time` DATETIME NULL
  AFTER `express_status`,
  ADD COLUMN `receive_time` DATETIME NULL
  AFTER `deliver_time`;

#2018.05.02 add driftExpress no in parcel driftExpress
ALTER TABLE `gmair_express`.`parcel_express`
  ADD COLUMN `express_no` VARCHAR(50) NOT NULL
  AFTER `parent_express`;

##2018.06.15 add company url in driftExpress
ALTER TABLE `gmair_express`.`express_company`
  ADD COLUMN `company_url` VARCHAR(100) NULL
  AFTER `company_code`;


##2018.09.15
CREATE TABLE `gmair_express`.`express_token` (
  `access_token` VARCHAR(200) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL);

