##2018-09-22
ALTER TABLE `gmair_drift`.`drift_reservation`
CHANGE COLUMN `interval` `interval_date` INT(11) NOT NULL ;


##2018-09-25
ALTER TABLE `gmair_drift`.`drift_goods`
ADD COLUMN `goods_description` VARCHAR(45) NOT NULL AFTER `goods_name`,
ADD COLUMN `goods_price` DOUBLE NOT NULL AFTER `goods_description`;
