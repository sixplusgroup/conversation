#remove column longitude, latitude from table district_list
ALTER TABLE `gmair_location`.`district_list`
  DROP COLUMN `latitude`,
  DROP COLUMN `longitude`;
