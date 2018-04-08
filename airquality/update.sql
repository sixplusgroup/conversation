#2018.04.08 add city_daily_aqi_detail to store aqi data for the
CREATE TABLE `gmair_airquality`.`city_daily_aqi_detail` (
  `city_id` VARCHAR(20) NOT NULL,
  `aqi_index` INT NOT NULL,
  `aqi_level` VARCHAR(45) NOT NULL,
  `pri_pollut` VARCHAR(45) NOT NULL,
  `pm_2_5` DOUBLE NOT NULL DEFAULT 0,
  `pm_10` DOUBLE NOT NULL DEFAULT 0,
  `co` DOUBLE NOT NULL DEFAULT 0,
  `no_2` DOUBLE NOT NULL DEFAULT 0,
  `o_3` DOUBLE NOT NULL DEFAULT 0,
  `so_2` DOUBLE NOT NULL DEFAULT 0,
  `record_time` DATETIME NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL);
