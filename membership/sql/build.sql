/*
    Source Server Type    : MySQL
    Source Schema   : gmair_membership
    Data: 16/07/2021 14:53:17
*/


-- table: membership_consumer

drop table if exists `membership_consumer`;
create table `membership_consumer`(
    `consumer_id` varchar(20) not null,
    `first_integral` int not null default 0,
    `second_integral` int not null default 0,
    `create_time` datetime(0) NOT NULL,
    primary key (`consumer_id`) using btree
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- table: integral_product
drop table if exists `integral_product`;
create table `integral_product`(
    `product_id` varchar(20) not null,
    `product_name` varchar(20) not null default '',
    `create_time` datetime(0) NOT NULL,
    primary key(`product_id`) using btree
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- table: integral_record
drop table if exists `integral_add`;
create table `integral_add`(
    `add_id` varchar(20) not null,
    `consumer_id` varchar(20) not null,
    `product_id` varchar(20) not null,
    `integral_value` int not null default 0,
    `is_confirmed` tinyint(1) not null default 0,
    `confirmed_time` datetime(0),
    `create_time` datetime(0) NOT NULL,
    primary key (`add_id`) using btree,
    foreign key (`consumer_id`) references `membership_consumer` (consumer_id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    foreign key (`product_id`) references `integral_product` (product_id) ON DELETE RESTRICT ON UPDATE RESTRICT
)ENGINE=InnoDB DEFAULT CHARSET=utf8;