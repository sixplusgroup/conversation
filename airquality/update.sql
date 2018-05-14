#2018.04.08 add city_daily_aqi_detail to store aqi data for the
CREATE TABLE   (
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

create table `gmair_airquality`.`city_url` (
	  city_id varchar(20) not null UNIQUE,
    city_url varchar(255) not null,
    block_flag tinyint(1) default 0,
    create_time datetime not null,
    PRIMARY KEY (city_id);
)

# 2018-05-11 create obscure city table
CREATE TABLE `gmair_airquality`.`obscure_city` (
  `oc_id` CHAR(20) NOT NULL,
  `city_name` VARCHAR(31) NOT NULL ,
  `city_id` VARCHAR(31) NOT NULL,
  `block_flag` TINYINT(1) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`oc_id`),
  UNIQUE INDEX `oc_id_UNIQUE` (`oc_id` ASC));