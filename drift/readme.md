# Drift Module

## Introduction
* This module aims to provide core drift background implementation
* It is a component to:
    * Reservation of formaldehyde detector
    * Provide reservation status for user
    * Reservation order management
    * Report management
* For each reservation, messages will be engaged to inform user of the current status.

## Functions
* User information. All users should make a reservation if he/she wishes to use it.
* Goods. Drift activities may contain several different goods, there should be a list maintained by this module.
* Repository. There should be a pool for goods available.
* Activity. An activity should be published at first. Each activity contains only one goods. An interval should be set for the activity. A pool size should be set when the activity is created. Meanwhile, threshold will be configured.
* Threshold. Threshold is configured for a week. All success reservation cannot exceed the total quantity * threshold. Thus threshold is less than 1 but greater than 0.

## Data Structure
* User registration
    * Registration will call auth consumer service, which is transparent here.
* Drift goods
    * Columns: goods_id | goods_name | block_flag | create_time
* Repository
    * Columns: repository_id | goods_id | pool_size | block_flag | create_time
* Activity
    * Columns: activity_id | goods_id | activity_name | repository_size | threshold | start | end | block_flag | create_time
* Reservation
    * Columns: reservation_id | consumer_id | activity_id | expected_date | interval | consignee_name | consignee_phone | consignee_address | province_id | city_id | block_flag | create_time

## API List