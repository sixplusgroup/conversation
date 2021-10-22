SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers` (
  `SCHED_NAME`    VARCHAR(120) CHARACTER SET utf8
  COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME`  VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci NOT NULL,
  `BLOB_DATA`     BLOB    NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `SCHED_NAME`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars` (
  `SCHED_NAME`    VARCHAR(120) CHARACTER SET utf8
  COLLATE utf8_general_ci NOT NULL,
  `CALENDAR_NAME` VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci NOT NULL,
  `CALENDAR`      BLOB    NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers` (
  `SCHED_NAME`      VARCHAR(120) CHARACTER SET utf8
  COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME`    VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP`   VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci NOT NULL,
  `CRON_EXPRESSION` VARCHAR(120) CHARACTER SET utf8
  COLLATE utf8_general_ci NOT NULL,
  `TIME_ZONE_ID`    VARCHAR(80) CHARACTER SET utf8
  COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME`        VARCHAR(120) CHARACTER SET utf8
  COLLATE utf8_general_ci        NOT NULL,
  `ENTRY_ID`          VARCHAR(95) CHARACTER SET utf8
  COLLATE utf8_general_ci        NOT NULL,
  `TRIGGER_NAME`      VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci        NOT NULL,
  `TRIGGER_GROUP`     VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci        NOT NULL,
  `INSTANCE_NAME`     VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci        NOT NULL,
  `FIRED_TIME`        BIGINT(13) NOT NULL,
  `SCHED_TIME`        BIGINT(13) NOT NULL,
  `PRIORITY`          INT(11)    NOT NULL,
  `STATE`             VARCHAR(16) CHARACTER SET utf8
  COLLATE utf8_general_ci        NOT NULL,
  `JOB_NAME`          VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci        NULL DEFAULT NULL,
  `JOB_GROUP`         VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci        NULL DEFAULT NULL,
  `IS_NONCONCURRENT`  VARCHAR(1) CHARACTER SET utf8
  COLLATE utf8_general_ci        NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` VARCHAR(1) CHARACTER SET utf8
  COLLATE utf8_general_ci        NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TRIG_INST_NAME`(`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY`(`SCHED_NAME`, `INSTANCE_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  INDEX `IDX_QRTZ_FT_J_G`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_T_G`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TG`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details` (
  `SCHED_NAME`        VARCHAR(120) CHARACTER SET utf8
  COLLATE utf8_general_ci  NOT NULL,
  `JOB_NAME`          VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci  NOT NULL,
  `JOB_GROUP`         VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci  NOT NULL,
  `DESCRIPTION`       VARCHAR(250) CHARACTER SET utf8
  COLLATE utf8_general_ci  NULL DEFAULT NULL,
  `JOB_CLASS_NAME`    VARCHAR(250) CHARACTER SET utf8
  COLLATE utf8_general_ci  NOT NULL,
  `IS_DURABLE`        VARCHAR(1) CHARACTER SET utf8
  COLLATE utf8_general_ci  NOT NULL,
  `IS_NONCONCURRENT`  VARCHAR(1) CHARACTER SET utf8
  COLLATE utf8_general_ci  NOT NULL,
  `IS_UPDATE_DATA`    VARCHAR(1) CHARACTER SET utf8
  COLLATE utf8_general_ci  NOT NULL,
  `REQUESTS_RECOVERY` VARCHAR(1) CHARACTER SET utf8
  COLLATE utf8_general_ci  NOT NULL,
  `JOB_DATA`          BLOB NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_J_REQ_RECOVERY`(`SCHED_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  INDEX `IDX_QRTZ_J_GRP`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks` (
  `SCHED_NAME` VARCHAR(120) CHARACTER SET utf8
  COLLATE utf8_general_ci NOT NULL,
  `LOCK_NAME`  VARCHAR(40) CHARACTER SET utf8
  COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `SCHED_NAME`    VARCHAR(120) CHARACTER SET utf8
  COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME`        VARCHAR(120) CHARACTER SET utf8
  COLLATE utf8_general_ci        NOT NULL,
  `INSTANCE_NAME`     VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci        NOT NULL,
  `LAST_CHECKIN_TIME` BIGINT(13) NOT NULL,
  `CHECKIN_INTERVAL`  BIGINT(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers` (
  `SCHED_NAME`      VARCHAR(120) CHARACTER SET utf8
  COLLATE utf8_general_ci      NOT NULL,
  `TRIGGER_NAME`    VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci      NOT NULL,
  `TRIGGER_GROUP`   VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci      NOT NULL,
  `REPEAT_COUNT`    BIGINT(7)  NOT NULL,
  `REPEAT_INTERVAL` BIGINT(12) NOT NULL,
  `TIMES_TRIGGERED` BIGINT(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers` (
  `SCHED_NAME`    VARCHAR(120) CHARACTER SET utf8
  COLLATE utf8_general_ci        NOT NULL,
  `TRIGGER_NAME`  VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci        NOT NULL,
  `TRIGGER_GROUP` VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci        NOT NULL,
  `STR_PROP_1`    VARCHAR(512) CHARACTER SET utf8
  COLLATE utf8_general_ci        NULL DEFAULT NULL,
  `STR_PROP_2`    VARCHAR(512) CHARACTER SET utf8
  COLLATE utf8_general_ci        NULL DEFAULT NULL,
  `STR_PROP_3`    VARCHAR(512) CHARACTER SET utf8
  COLLATE utf8_general_ci        NULL DEFAULT NULL,
  `INT_PROP_1`    INT(11)        NULL DEFAULT NULL,
  `INT_PROP_2`    INT(11)        NULL DEFAULT NULL,
  `LONG_PROP_1`   BIGINT(20)     NULL DEFAULT NULL,
  `LONG_PROP_2`   BIGINT(20)     NULL DEFAULT NULL,
  `DEC_PROP_1`    DECIMAL(13, 4) NULL DEFAULT NULL,
  `DEC_PROP_2`    DECIMAL(13, 4) NULL DEFAULT NULL,
  `BOOL_PROP_1`   VARCHAR(1) CHARACTER SET utf8
  COLLATE utf8_general_ci        NULL DEFAULT NULL,
  `BOOL_PROP_2`   VARCHAR(1) CHARACTER SET utf8
  COLLATE utf8_general_ci        NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers` (
  `SCHED_NAME`     VARCHAR(120) CHARACTER SET utf8
  COLLATE utf8_general_ci      NOT NULL,
  `TRIGGER_NAME`   VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci      NOT NULL,
  `TRIGGER_GROUP`  VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci      NOT NULL,
  `JOB_NAME`       VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci      NOT NULL,
  `JOB_GROUP`      VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci      NOT NULL,
  `DESCRIPTION`    VARCHAR(250) CHARACTER SET utf8
  COLLATE utf8_general_ci      NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` BIGINT(13)  NULL DEFAULT NULL,
  `PREV_FIRE_TIME` BIGINT(13)  NULL DEFAULT NULL,
  `PRIORITY`       INT(11)     NULL DEFAULT NULL,
  `TRIGGER_STATE`  VARCHAR(16) CHARACTER SET utf8
  COLLATE utf8_general_ci      NOT NULL,
  `TRIGGER_TYPE`   VARCHAR(8) CHARACTER SET utf8
  COLLATE utf8_general_ci      NOT NULL,
  `START_TIME`     BIGINT(13)  NOT NULL,
  `END_TIME`       BIGINT(13)  NULL DEFAULT NULL,
  `CALENDAR_NAME`  VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci      NULL DEFAULT NULL,
  `MISFIRE_INSTR`  SMALLINT(2) NULL DEFAULT NULL,
  `JOB_DATA`       BLOB        NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_J`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_C`(`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_T_G`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_STATE`(`SCHED_NAME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_STATE`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_G_STATE`(`SCHED_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NEXT_FIRE_TIME`(`SCHED_NAME`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST`(`SCHED_NAME`, `TRIGGER_STATE`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_app_connect
-- ----------------------------
DROP TABLE IF EXISTS `tz_app_connect`;
CREATE TABLE `tz_app_connect` (
  `id`          BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT 'id',
  `user_id`     VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci           NOT NULL
  COMMENT '本系统userId',
  `app_id`      TINYINT(2)          NULL     DEFAULT NULL
  COMMENT '第三方系统id 1：微信小程序',
  `nick_name`   VARCHAR(64) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '第三方系统昵称',
  `image_url`   VARCHAR(500) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '第三方系统头像',
  `biz_user_id` VARCHAR(255) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '第三方系统userid',
  `biz_unionid` VARCHAR(255) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '第三方系统unionid',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_app_id`(`user_id`, `app_id`) USING BTREE COMMENT '用户id和appid联合索引'
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 86
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_area
-- ----------------------------
DROP TABLE IF EXISTS `tz_area`;
CREATE TABLE `tz_area` (
  `area_id`   BIGINT(20)  NOT NULL AUTO_INCREMENT,
  `area_name` VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci NULL     DEFAULT NULL,
  `parent_id` BIGINT(20)  NULL     DEFAULT NULL,
  `level`     INT(1)      NULL     DEFAULT NULL,
  PRIMARY KEY (`area_id`) USING BTREE,
  INDEX `parent_id`(`parent_id`) USING BTREE COMMENT '上级id'
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 659006000000
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_attach_file
-- ----------------------------
DROP TABLE IF EXISTS `tz_attach_file`;
CREATE TABLE `tz_attach_file` (
  `file_id`        BIGINT(20) NOT NULL AUTO_INCREMENT,
  `file_path`      VARCHAR(255) CHARACTER SET utf8
  COLLATE utf8_general_ci     NULL     DEFAULT NULL
  COMMENT '文件路径',
  `file_type`      VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci     NULL     DEFAULT NULL
  COMMENT '文件类型',
  `file_size`      INT(10)    NULL     DEFAULT NULL
  COMMENT '文件大小',
  `upload_time`    DATETIME   NULL     DEFAULT NULL
  COMMENT '上传时间',
  `file_join_id`   BIGINT(20) NULL     DEFAULT NULL
  COMMENT '文件关联的表主键id',
  `file_join_type` TINYINT(2) NULL     DEFAULT NULL
  COMMENT '文件关联表类型：1 商品表  FileJoinType',
  PRIMARY KEY (`file_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 35
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_basket
-- ----------------------------
DROP TABLE IF EXISTS `tz_basket`;
CREATE TABLE `tz_basket` (
  `basket_id`            BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `shop_id`              BIGINT(20)          NOT NULL
  COMMENT '店铺ID',
  `prod_id`              BIGINT(20) UNSIGNED NOT NULL DEFAULT 0
  COMMENT '产品ID',
  `sku_id`               BIGINT(20) UNSIGNED NOT NULL DEFAULT 0
  COMMENT 'SkuID',
  `user_id`              VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci                    NOT NULL
  COMMENT '用户ID',
  `basket_count`         INT(11)             NOT NULL DEFAULT 0
  COMMENT '购物车产品个数',
  `basket_date`          DATETIME            NOT NULL
  COMMENT '购物时间',
  `discount_id`          BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '满减活动ID',
  `distribution_card_no` VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci                    NULL     DEFAULT NULL
  COMMENT '分销推广人卡号',
  PRIMARY KEY (`basket_id`) USING BTREE,
  UNIQUE INDEX `uk_user_shop_sku`(`sku_id`, `user_id`, `shop_id`) USING BTREE,
  INDEX `shop_id`(`shop_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 39
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '购物车'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_brand
-- ----------------------------
DROP TABLE IF EXISTS `tz_brand`;
CREATE TABLE `tz_brand` (
  `brand_id`    BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `brand_name`  VARCHAR(30) CHARACTER SET utf8
  COLLATE utf8_general_ci           NOT NULL DEFAULT ''
  COMMENT '品牌名称',
  `brand_pic`   VARCHAR(255) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '图片路径',
  `user_id`     VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci           NOT NULL DEFAULT ''
  COMMENT '用户ID',
  `memo`        VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '备注',
  `seq`         INT(11)             NULL     DEFAULT 1
  COMMENT '顺序',
  `status`      INT(1)              NOT NULL DEFAULT 1
  COMMENT '默认是1，表示正常状态,0为下线状态',
  `brief`       VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '简要描述',
  `content`     TEXT CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL
  COMMENT '内容',
  `rec_time`    DATETIME            NULL     DEFAULT NULL
  COMMENT '记录时间',
  `update_time` DATETIME            NULL     DEFAULT NULL
  COMMENT '更新时间',
  `first_char`  VARCHAR(1) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '品牌首字母',
  PRIMARY KEY (`brand_id`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '品牌表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_category
-- ----------------------------
DROP TABLE IF EXISTS `tz_category`;
CREATE TABLE `tz_category` (
  `category_id`   BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '类目ID',
  `shop_id`       BIGINT(20)          NOT NULL
  COMMENT '店铺ID',
  `parent_id`     BIGINT(20) UNSIGNED NOT NULL
  COMMENT '父节点',
  `category_name` VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci             NOT NULL DEFAULT ''
  COMMENT '产品类目名称',
  `icon`          VARCHAR(255) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '类目图标',
  `pic`           VARCHAR(300) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '类目的显示图片',
  `seq`           INT(5)              NOT NULL
  COMMENT '排序',
  `status`        INT(1)              NOT NULL DEFAULT 1
  COMMENT '默认是1，表示正常状态,0为下线状态',
  `rec_time`      DATETIME            NOT NULL
  COMMENT '记录时间',
  `grade`         INT(2)              NOT NULL
  COMMENT '分类层级',
  `update_time`   DATETIME            NULL     DEFAULT NULL
  COMMENT '更新时间',
  PRIMARY KEY (`category_id`) USING BTREE,
  INDEX `parent_id`(`parent_id`) USING BTREE,
  INDEX `shop_id`(`shop_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 96
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '产品类目'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_category_brand
-- ----------------------------
DROP TABLE IF EXISTS `tz_category_brand`;
CREATE TABLE `tz_category_brand` (
  `id`          BIGINT(20) NOT NULL AUTO_INCREMENT,
  `category_id` BIGINT(20) NULL     DEFAULT NULL
  COMMENT '分类id',
  `brand_id`    BIGINT(20) NULL     DEFAULT NULL
  COMMENT '品牌id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_id`(`category_id`) USING BTREE,
  INDEX `brand_id`(`brand_id`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_category_prop
-- ----------------------------
DROP TABLE IF EXISTS `tz_category_prop`;
CREATE TABLE `tz_category_prop` (
  `id`          BIGINT(20) NOT NULL AUTO_INCREMENT,
  `category_id` BIGINT(20) NULL     DEFAULT NULL
  COMMENT '分类id',
  `prop_id`     BIGINT(20) NULL     DEFAULT NULL
  COMMENT '商品属性id即表tz_prod_prop中的prop_id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_id`(`category_id`) USING BTREE,
  INDEX `prop_id`(`prop_id`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_delivery
-- ----------------------------
DROP TABLE IF EXISTS `tz_delivery`;
CREATE TABLE `tz_delivery` (
  `dvy_id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT 'ID',
  `dvy_name`         VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci                NOT NULL DEFAULT ''
  COMMENT '物流公司名称',
  `company_home_url` VARCHAR(255) CHARACTER SET utf8
  COLLATE utf8_general_ci                NULL     DEFAULT NULL
  COMMENT '公司主页',
  `rec_time`         DATETIME            NOT NULL
  COMMENT '建立时间',
  `modify_time`      DATETIME            NOT NULL
  COMMENT '修改时间',
  `query_url`        VARCHAR(520) CHARACTER SET utf8
  COLLATE utf8_general_ci                NOT NULL
  COMMENT '物流查询接口',
  PRIMARY KEY (`dvy_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 29
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '物流公司'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_hot_search
-- ----------------------------
DROP TABLE IF EXISTS `tz_hot_search`;
CREATE TABLE `tz_hot_search` (
  `hot_search_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `shop_id`       BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '店铺ID 0为全局热搜',
  `content`       VARCHAR(255) CHARACTER SET utf8
  COLLATE utf8_general_ci             NOT NULL
  COMMENT '内容',
  `rec_date`      DATETIME            NOT NULL
  COMMENT '录入时间',
  `seq`           INT(11)             NULL     DEFAULT NULL
  COMMENT '顺序',
  `status`        TINYINT(2)          NOT NULL DEFAULT 1
  COMMENT '状态 0下线 1上线',
  `title`         VARCHAR(255) CHARACTER SET utf8
  COLLATE utf8_general_ci             NOT NULL
  COMMENT '热搜标题',
  PRIMARY KEY (`hot_search_id`) USING BTREE,
  INDEX `shop_id`(`shop_id`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '热搜'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_index_img
-- ----------------------------
DROP TABLE IF EXISTS `tz_index_img`;
CREATE TABLE `tz_index_img` (
  `img_id`      BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `shop_id`     BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '店铺ID',
  `img_url`     VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci           NOT NULL
  COMMENT '图片',
  `des`         VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT ''
  COMMENT '说明文字,描述',
  `title`       VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '标题',
  `link`        VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '链接',
  `status`      TINYINT(1)          NULL     DEFAULT 0
  COMMENT '状态',
  `seq`         INT(11)             NULL     DEFAULT 0
  COMMENT '顺序',
  `upload_time` DATETIME            NULL     DEFAULT NULL
  COMMENT '上传时间',
  `relation`    BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '关联',
  `type`        INT(2)              NULL     DEFAULT NULL
  COMMENT '类型',
  PRIMARY KEY (`img_id`) USING BTREE,
  INDEX `shop_id`(`shop_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 6
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '主页轮播图'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_login_hist
-- ----------------------------
DROP TABLE IF EXISTS `tz_login_hist`;
CREATE TABLE `tz_login_hist` (
  `id`         BIGINT(20) NOT NULL AUTO_INCREMENT
  COMMENT 'ID',
  `area`       VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci NULL     DEFAULT NULL
  COMMENT '地区',
  `country`    VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci NULL     DEFAULT NULL
  COMMENT '国家',
  `user_id`    VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci NULL     DEFAULT NULL
  COMMENT '用户id',
  `ip`         VARCHAR(64) CHARACTER SET utf8
  COLLATE utf8_general_ci NOT NULL DEFAULT ''
  COMMENT 'IP',
  `login_time` DATETIME   NOT NULL
  COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '登录历史表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_message
-- ----------------------------
DROP TABLE IF EXISTS `tz_message`;
CREATE TABLE `tz_message` (
  `id`          BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `create_time` DATETIME            NULL
  COMMENT '留言创建时间',
  `user_name`   VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '姓名',
  `email`       VARCHAR(64) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '邮箱',
  `contact`     VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '联系方式',
  `content`     TEXT CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL
  COMMENT '留言内容',
  `reply`       TEXT CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL
  COMMENT '留言回复',
  `status`      TINYINT(2)          NULL     DEFAULT NULL
  COMMENT '状态：0:未审核 1审核通过',
  PRIMARY KEY (`id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 10
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_notice
-- ----------------------------
DROP TABLE IF EXISTS `tz_notice`;
CREATE TABLE `tz_notice` (
  `id`           BIGINT(20) NOT NULL AUTO_INCREMENT
  COMMENT '公告id',
  `shop_id`      BIGINT(20) NULL     DEFAULT NULL
  COMMENT '店铺id',
  `title`        VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL     DEFAULT NULL
  COMMENT '公告标题',
  `content`      TEXT CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL
  COMMENT '公告内容',
  `status`       TINYINT(1) NULL     DEFAULT NULL
  COMMENT '状态(1:公布 0:撤回)',
  `is_top`       TINYINT(2) NULL     DEFAULT NULL
  COMMENT '是否置顶',
  `publish_time` TIMESTAMP  NULL     DEFAULT NULL
  COMMENT '发布时间',
  `update_time`  TIMESTAMP  NULL     DEFAULT NULL
  COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 6
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_order
-- ----------------------------
DROP TABLE IF EXISTS `tz_order`;
CREATE TABLE `tz_order` (
  `order_id`                BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '订单ID',
  `shop_id`                 BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '店铺id',
  `prod_name`               VARCHAR(1000) CHARACTER SET utf8
  COLLATE utf8_general_ci                       NOT NULL DEFAULT ''
  COMMENT '产品名称,多个产品将会以逗号隔开',
  `user_id`                 VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci                       NOT NULL
  COMMENT '订购用户ID',
  `order_number`            VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci                       NOT NULL
  COMMENT '订购流水号',
  `total`                   DECIMAL(15, 2)      NOT NULL DEFAULT 0.00
  COMMENT '总值',
  `actual_total`            DECIMAL(15, 2)      NULL     DEFAULT NULL
  COMMENT '实际总值',
  `total_integral`          INT(11)             NULL     DEFAULT 0,
  `pay_type`                INT(1)              NULL     DEFAULT NULL
  COMMENT '支付方式 0 手动代付 1 微信支付 2 支付宝',
  `remarks`                 VARCHAR(1024) CHARACTER SET utf8
  COLLATE utf8_general_ci                       NULL     DEFAULT NULL
  COMMENT '订单备注',
  `status`                  INT(2)              NOT NULL DEFAULT 0
  COMMENT '订单状态 1:待付款 2:待发货 3:待收货 4:待评价 5:成功 6:失败',
  `dvy_type`                VARCHAR(10) CHARACTER SET utf8
  COLLATE utf8_general_ci                       NULL     DEFAULT NULL
  COMMENT '配送类型',
  `dvy_id`                  BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '配送方式ID',
  `dvy_flow_id`             VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci                       NULL     DEFAULT ''
  COMMENT '物流单号',
  `freight_amount`          DECIMAL(15, 2)      NULL     DEFAULT 0.00
  COMMENT '订单运费',
  `addr_order_id`           BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '用户订单地址Id',
  `product_nums`            INT(10)             NULL     DEFAULT NULL
  COMMENT '订单商品总数',
  `create_time`             DATETIME            NOT NULL
  COMMENT '订购时间',
  `update_time`             DATETIME            NULL     DEFAULT NULL
  COMMENT '订单更新时间',
  `pay_time`                DATETIME            NULL     DEFAULT NULL
  COMMENT '付款时间',
  `dvy_time`                DATETIME            NULL     DEFAULT NULL
  COMMENT '发货时间',
  `finally_time`            DATETIME            NULL     DEFAULT NULL
  COMMENT '完成时间',
  `cancel_time`             DATETIME            NULL     DEFAULT NULL
  COMMENT '取消时间',
  `is_payed`                TINYINT(1)          NULL     DEFAULT NULL
  COMMENT '是否已经支付，1：已经支付过，0：，没有支付过',
  `delete_status`           INT(1)              NULL     DEFAULT 0
  COMMENT '用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除',
  `refund_sts`              INT(1)              NULL     DEFAULT 0
  COMMENT '0:默认,1:在处理,2:处理完成',
  `reduce_amount`           DECIMAL(15, 2)      NULL     DEFAULT NULL
  COMMENT '优惠总额',
  `order_type`              TINYINT(2)          NULL     DEFAULT NULL
  COMMENT '订单类型',
  `close_type`              TINYINT(2)          NULL     DEFAULT NULL
  COMMENT '订单关闭原因 1-超时未支付 2-退款关闭 4-买家取消 15-已通过货到付款交易',
  `is_need_cash_of_all`     TINYINT(1)          NULL     DEFAULT 1,
  `is_need_integral_of_all` TINYINT(1)          NULL     DEFAULT 0,
  PRIMARY KEY (`order_id`) USING BTREE,
  UNIQUE INDEX `order_number_unique_ind`(`order_number`) USING BTREE,
  UNIQUE INDEX `order_number_userid_unique_ind`(`user_id`, `order_number`) USING BTREE,
  INDEX `shop_id`(`shop_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 34
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '订单表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_order_item
-- ----------------------------
DROP TABLE IF EXISTS `tz_order_item`;
CREATE TABLE `tz_order_item` (
  `order_item_id`         BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '订单项ID',
  `shop_id`               BIGINT(20)          NOT NULL
  COMMENT '店铺id',
  `order_number`          VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci                     NOT NULL
  COMMENT '订单order_number',
  `prod_id`               BIGINT(20) UNSIGNED NOT NULL
  COMMENT '产品ID',
  `sku_id`                BIGINT(20) UNSIGNED NOT NULL
  COMMENT '产品SkuID',
  `prod_count`            INT(11)             NOT NULL DEFAULT 0
  COMMENT '购物车产品个数',
  `prod_name`             VARCHAR(120) CHARACTER SET utf8
  COLLATE utf8_general_ci                     NOT NULL DEFAULT ''
  COMMENT '产品名称',
  `sku_name`              VARCHAR(120) CHARACTER SET utf8
  COLLATE utf8_general_ci                     NULL     DEFAULT NULL
  COMMENT 'sku名称',
  `pic`                   VARCHAR(255) CHARACTER SET utf8
  COLLATE utf8_general_ci                     NOT NULL DEFAULT ''
  COMMENT '产品主图片路径',
  `price`                 DECIMAL(15, 2)      NOT NULL
  COMMENT '产品价格',
  `product_total_amount`  DECIMAL(15, 2)      NOT NULL
  COMMENT '商品总金额',
  `user_id`               VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci                     NOT NULL DEFAULT ''
  COMMENT '用户Id',
  `is_need_integral`      TINYINT(1)          NULL     DEFAULT 0,
  `is_need_cash`          TINYINT(1)          NULL     DEFAULT 1,
  `integral_price`        INT(11)             NULL     DEFAULT 0,
  `integral_total_amount` INT(11)             NULL     DEFAULT 0,
  `rec_time`              DATETIME            NOT NULL
  COMMENT '购物时间',
  `comm_sts`              INT(1)              NOT NULL DEFAULT 0
  COMMENT '评论状态： 0 未评价  1 已评价',
  `distribution_card_no`  VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci                     NULL     DEFAULT NULL
  COMMENT '推广员使用的推销卡号',
  `basket_date`           DATETIME            NULL     DEFAULT NULL
  COMMENT '加入购物车时间',
  PRIMARY KEY (`order_item_id`) USING BTREE,
  INDEX `order_number`(`order_number`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 36
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '订单项'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_order_refund
-- ----------------------------
DROP TABLE IF EXISTS `tz_order_refund`;
CREATE TABLE `tz_order_refund` (
  `refund_id`        BIGINT(20)     NOT NULL AUTO_INCREMENT
  COMMENT '记录ID',
  `shop_id`          BIGINT(20)     NOT NULL
  COMMENT '店铺ID',
  `order_id`         BIGINT(20)     NOT NULL
  COMMENT '订单ID',
  `order_number`     VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci           NOT NULL
  COMMENT '订单流水号',
  `order_amount`     DOUBLE(12, 2)  NOT NULL
  COMMENT '订单总金额',
  `order_item_id`    BIGINT(20)     NOT NULL DEFAULT 0
  COMMENT '订单项ID 全部退款是0',
  `refund_sn`        VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci           NOT NULL
  COMMENT '退款编号',
  `flow_trade_no`    VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci           NOT NULL
  COMMENT '订单支付流水号',
  `out_refund_no`    VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '第三方退款单号(微信退款单号)',
  `pay_type`         INT(1)         NULL     DEFAULT NULL
  COMMENT '订单支付方式 1 微信支付 2 支付宝',
  `pay_type_name`    VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '订单支付名称',
  `user_id`          VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci           NOT NULL
  COMMENT '买家ID',
  `goods_num`        INT(11)        NULL     DEFAULT NULL
  COMMENT '退货数量',
  `refund_amount`    DECIMAL(10, 2) NOT NULL
  COMMENT '退款金额',
  `apply_type`       INT(1)         NOT NULL DEFAULT 0
  COMMENT '申请类型:1,仅退款,2退款退货',
  `refund_sts`       INT(1)         NOT NULL DEFAULT 0
  COMMENT '处理状态:1为待审核,2为同意,3为不同意',
  `return_money_sts` INT(1)         NOT NULL DEFAULT 0
  COMMENT '处理退款状态: 0:退款处理中 1:退款成功 -1:退款失败',
  `apply_time`       DATETIME       NOT NULL
  COMMENT '申请时间',
  `handel_time`      DATETIME       NULL     DEFAULT NULL
  COMMENT '卖家处理时间',
  `refund_time`      DATETIME       NULL     DEFAULT NULL
  COMMENT '退款时间',
  `photo_files`      VARCHAR(150) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '文件凭证json',
  `buyer_msg`        VARCHAR(300) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '申请原因',
  `seller_msg`       VARCHAR(300) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '卖家备注',
  `express_name`     VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '物流公司名称',
  `express_no`       VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '物流单号',
  `ship_time`        DATETIME       NULL     DEFAULT NULL
  COMMENT '发货时间',
  `receive_time`     DATETIME       NULL     DEFAULT NULL
  COMMENT '收货时间',
  `receive_message`  VARCHAR(300) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '收货备注',
  PRIMARY KEY (`refund_id`) USING BTREE,
  UNIQUE INDEX `refund_sn_unique`(`refund_sn`) USING BTREE,
  INDEX `order_number`(`order_number`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_order_settlement
-- ----------------------------
DROP TABLE IF EXISTS `tz_order_settlement`;
CREATE TABLE `tz_order_settlement` (
  `settlement_id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '支付结算单据ID',
  `pay_no`                  VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci                       NULL     DEFAULT NULL
  COMMENT '支付单号',
  `biz_pay_no`              VARCHAR(255) CHARACTER SET utf8
  COLLATE utf8_general_ci                       NULL     DEFAULT NULL
  COMMENT '外部订单流水号',
  `order_number`            VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci                       NULL     DEFAULT NULL
  COMMENT 'order表中的订单号',
  `pay_type`                INT(1)              NULL     DEFAULT NULL
  COMMENT '支付方式 1 微信支付 2 支付宝',
  `pay_type_name`           VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci                       NULL     DEFAULT NULL
  COMMENT '支付方式名称',
  `pay_amount`              DECIMAL(15, 2)      NULL     DEFAULT NULL
  COMMENT '支付金额',
  `pay_integral_amount`     INT(11)             NULL     DEFAULT 0,
  `is_clearing`             INT(1)              NULL     DEFAULT NULL
  COMMENT '是否清算 0:否 1:是',
  `user_id`                 VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci                       NULL     DEFAULT NULL
  COMMENT '用户ID',
  `create_time`             DATETIME            NOT NULL
  COMMENT '创建时间',
  `clearing_time`           DATETIME            NULL     DEFAULT NULL
  COMMENT '清算时间',
  `version`                 INT(11)             NULL     DEFAULT NULL
  COMMENT '版本号',
  `pay_status`              INT(1)              NULL     DEFAULT NULL
  COMMENT '支付状态',
  `is_need_cash_of_all`     TINYINT(1)          NULL     DEFAULT 1,
  `is_need_integral_of_all` TINYINT(1)          NULL     DEFAULT 0,
  PRIMARY KEY (`settlement_id`) USING BTREE,
  UNIQUE INDEX `primary_order_no`(`order_number`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 33
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_pick_addr
-- ----------------------------
DROP TABLE IF EXISTS `tz_pick_addr`;
CREATE TABLE `tz_pick_addr` (
  `addr_id`     BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT 'ID',
  `addr_name`   VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '自提点名称',
  `addr`        VARCHAR(1000) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '地址',
  `mobile`      VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '手机',
  `province_id` BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '省份ID',
  `province`    VARCHAR(32) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '省份',
  `city_id`     BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '城市ID',
  `city`        VARCHAR(32) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '城市',
  `area_id`     BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '区/县ID',
  `area`        VARCHAR(32) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '区/县',
  `shop_id`     BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '店铺id',
  PRIMARY KEY (`addr_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '用户配送地址'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_prod
-- ----------------------------
DROP TABLE IF EXISTS `tz_prod`;
CREATE TABLE `tz_prod` (
  `prod_id`              BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '产品ID',
  `prod_name`            VARCHAR(300) CHARACTER SET utf8
  COLLATE utf8_general_ci                    NOT NULL DEFAULT ''
  COMMENT '商品名称',
  `shop_id`              BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '店铺id',
  `ori_price`            DECIMAL(15, 2)      NULL     DEFAULT 0.00
  COMMENT '原价',
  `price`                DECIMAL(15, 2)      NULL     DEFAULT NULL
  COMMENT '现价',
  `brief`                VARCHAR(500) CHARACTER SET utf8
  COLLATE utf8_general_ci                    NULL     DEFAULT ''
  COMMENT '简要描述,卖点等',
  `content`              TEXT CHARACTER SET utf8
  COLLATE utf8_general_ci                    NULL
  COMMENT '详细描述',
  `pic`                  VARCHAR(255) CHARACTER SET utf8
  COLLATE utf8_general_ci                    NULL     DEFAULT NULL
  COMMENT '商品主图',
  `imgs`                 VARCHAR(1000) CHARACTER SET utf8
  COLLATE utf8_general_ci                    NULL     DEFAULT NULL
  COMMENT '商品图片，以,分割',
  `status`               INT(1)              NULL     DEFAULT 0
  COMMENT '默认是1，表示正常状态, -1表示删除, 0下架',
  `category_id`          BIGINT(20) UNSIGNED NULL     DEFAULT NULL
  COMMENT '商品分类',
  `sold_num`             INT(11)             NULL     DEFAULT NULL
  COMMENT '销量',
  `total_stocks`         INT(11)             NULL     DEFAULT 0
  COMMENT '总库存',
  `delivery_mode`        LONGTEXT            NULL
  COMMENT '配送方式json见TransportModeVO',
  `delivery_template_id` BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '运费模板id',
  `create_time`          DATETIME            NULL
  COMMENT '录入时间',
  `update_time`          DATETIME            NULL     DEFAULT NULL
  COMMENT '修改时间',
  `putaway_time`         DATETIME            NULL     DEFAULT NULL
  COMMENT '上架时间',
  `version`              INT(11)             NULL     DEFAULT NULL
  COMMENT '版本 乐观锁',
  `is_need_cash`         TINYINT(1)          NULL     DEFAULT 1,
  `is_need_integral`     TINYINT(1)          NULL     DEFAULT 0,
  `integral_price`       INT(11)             NULL     DEFAULT 0,
  PRIMARY KEY (`prod_id`) USING BTREE,
  INDEX `shop_id`(`shop_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 93
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '商品'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_prod_comm
-- ----------------------------
DROP TABLE IF EXISTS `tz_prod_comm`;
CREATE TABLE `tz_prod_comm` (
  `prod_comm_id`  BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT 'ID',
  `prod_id`       BIGINT(20) UNSIGNED NOT NULL
  COMMENT '商品ID',
  `order_item_id` BIGINT(20) UNSIGNED NULL     DEFAULT NULL
  COMMENT '订单项ID',
  `user_id`       VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '评论用户ID',
  `content`       VARCHAR(500) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT ''
  COMMENT '评论内容',
  `reply_content` VARCHAR(500) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT ''
  COMMENT '掌柜回复',
  `rec_time`      DATETIME            NULL     DEFAULT NULL
  COMMENT '记录时间',
  `reply_time`    DATETIME            NULL     DEFAULT NULL
  COMMENT '回复时间',
  `reply_sts`     INT(1)              NULL     DEFAULT 0
  COMMENT '是否回复 0:未回复  1:已回复',
  `postip`        VARCHAR(16) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT 'IP来源',
  `score`         TINYINT(2)          NULL     DEFAULT 0
  COMMENT '得分，0-5分',
  `useful_counts` INT(11)             NULL     DEFAULT 0
  COMMENT '有用的计数',
  `pics`          VARCHAR(1000) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '晒图的json字符串',
  `is_anonymous`  INT(1)              NULL     DEFAULT 0
  COMMENT '是否匿名(1:是  0:否)',
  `status`        INT(1)              NULL     DEFAULT NULL
  COMMENT '是否显示，1:为显示，0:待审核， -1：不通过审核，不显示。 如果需要审核评论，则是0,，否则1',
  `evaluate`      TINYINT(2)          NULL     DEFAULT NULL
  COMMENT '评价(0好评 1中评 2差评)',
  PRIMARY KEY (`prod_comm_id`) USING BTREE,
  INDEX `prod_id`(`prod_id`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '商品评论'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_prod_favorite
-- ----------------------------
DROP TABLE IF EXISTS `tz_prod_favorite`;
CREATE TABLE `tz_prod_favorite` (
  `favorite_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  `prod_id`     BIGINT(20) UNSIGNED NOT NULL
  COMMENT '商品ID',
  `rec_time`    DATETIME            NOT NULL
  COMMENT '收藏时间',
  `user_id`     VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci           NOT NULL DEFAULT ''
  COMMENT '用户ID',
  PRIMARY KEY (`favorite_id`) USING BTREE,
  INDEX `prod_id`(`prod_id`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '商品收藏表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_prod_prop
-- ----------------------------
DROP TABLE IF EXISTS `tz_prod_prop`;
CREATE TABLE `tz_prod_prop` (
  `prop_id`   BIGINT(20) NOT NULL AUTO_INCREMENT
  COMMENT '属性id',
  `prop_name` VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '属性名称',
  `rule`      TINYINT(2) NULL     DEFAULT NULL
  COMMENT 'ProdPropRule 1:销售属性(规格); 2:参数属性;',
  `shop_id`   BIGINT(20) NULL     DEFAULT NULL
  COMMENT '店铺id',
  PRIMARY KEY (`prop_id`) USING BTREE,
  INDEX `shop_id`(`shop_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 81
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_prod_prop_value
-- ----------------------------
DROP TABLE IF EXISTS `tz_prod_prop_value`;
CREATE TABLE `tz_prod_prop_value` (
  `value_id`   BIGINT(20) NOT NULL AUTO_INCREMENT
  COMMENT '属性值ID',
  `prop_value` VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci            NULL     DEFAULT NULL
  COMMENT '属性值名称',
  `prop_id`    BIGINT(20) NULL     DEFAULT NULL
  COMMENT '属性ID',
  PRIMARY KEY (`value_id`) USING BTREE,
  INDEX `prop_id`(`prop_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 386
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_prod_tag
-- ----------------------------
DROP TABLE IF EXISTS `tz_prod_tag`;
CREATE TABLE `tz_prod_tag` (
  `id`          BIGINT(20) NOT NULL AUTO_INCREMENT
  COMMENT '分组标签id',
  `title`       VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci  NULL     DEFAULT NULL
  COMMENT '分组标题',
  `shop_id`     BIGINT(20) NULL     DEFAULT NULL
  COMMENT '店铺Id',
  `status`      TINYINT(1) NULL     DEFAULT NULL
  COMMENT '状态(1为正常,0为删除)',
  `is_default`  TINYINT(1) NULL     DEFAULT NULL
  COMMENT '默认类型(0:商家自定义,1:系统默认)',
  `prod_count`  BIGINT(20) NULL     DEFAULT NULL
  COMMENT '商品数量',
  `style`       INT(10)    NULL     DEFAULT NULL
  COMMENT '列表样式(0:一列一个,1:一列两个,2:一列三个)',
  `seq`         INT(10)    NULL     DEFAULT NULL
  COMMENT '排序',
  `create_time` TIMESTAMP  NULL
  COMMENT '创建时间',
  `update_time` TIMESTAMP  NULL     DEFAULT NULL
  COMMENT '修改时间',
  `delete_time` TIMESTAMP  NULL     DEFAULT NULL
  COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 4
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '商品分组表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_prod_tag_reference
-- ----------------------------
DROP TABLE IF EXISTS `tz_prod_tag_reference`;
CREATE TABLE `tz_prod_tag_reference` (
  `reference_id` BIGINT(20) NOT NULL AUTO_INCREMENT
  COMMENT '分组引用id',
  `shop_id`      BIGINT(20) NULL     DEFAULT NULL
  COMMENT '店铺id',
  `tag_id`       BIGINT(20) NULL     DEFAULT NULL
  COMMENT '标签id',
  `prod_id`      BIGINT(20) NULL     DEFAULT NULL
  COMMENT '商品id',
  `status`       TINYINT(1) NULL     DEFAULT NULL
  COMMENT '状态(1:正常,0:删除)',
  `create_time`  TIMESTAMP  NOT NULL
  COMMENT '创建时间',
  PRIMARY KEY (`reference_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 389
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `tz_schedule_job`;
CREATE TABLE `tz_schedule_job` (
  `job_id`          BIGINT(20) NOT NULL AUTO_INCREMENT
  COMMENT '任务id',
  `bean_name`       VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci      NULL     DEFAULT NULL
  COMMENT 'spring bean名称',
  `method_name`     VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci      NULL     DEFAULT NULL
  COMMENT '方法名',
  `params`          VARCHAR(2000) CHARACTER SET utf8
  COLLATE utf8_general_ci      NULL     DEFAULT NULL
  COMMENT '参数',
  `cron_expression` VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci      NULL     DEFAULT NULL
  COMMENT 'cron表达式',
  `status`          TINYINT(4) NULL     DEFAULT NULL
  COMMENT '任务状态  0：正常  1：暂停',
  `remark`          VARCHAR(255) CHARACTER SET utf8
  COLLATE utf8_general_ci      NULL     DEFAULT NULL
  COMMENT '备注',
  `create_time`     DATETIME   NULL
  COMMENT '创建时间',
  PRIMARY KEY (`job_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 17
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '定时任务'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_schedule_job_log
-- ----------------------------
DROP TABLE IF EXISTS `tz_schedule_job_log`;
CREATE TABLE `tz_schedule_job_log` (
  `log_id`      BIGINT(20) NOT NULL AUTO_INCREMENT
  COMMENT '任务日志id',
  `job_id`      BIGINT(20) NOT NULL
  COMMENT '任务id',
  `bean_name`   VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci  NULL     DEFAULT NULL
  COMMENT 'spring bean名称',
  `method_name` VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci  NULL     DEFAULT NULL
  COMMENT '方法名',
  `params`      VARCHAR(2000) CHARACTER SET utf8
  COLLATE utf8_general_ci  NULL     DEFAULT NULL
  COMMENT '参数',
  `status`      TINYINT(4) NOT NULL
  COMMENT '任务状态    0：成功    1：失败',
  `error`       VARCHAR(2000) CHARACTER SET utf8
  COLLATE utf8_general_ci  NULL     DEFAULT NULL
  COMMENT '失败信息',
  `times`       INT(11)    NOT NULL
  COMMENT '耗时(单位：毫秒)',
  `create_time` DATETIME   NULL
  COMMENT '创建时间',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `job_id`(`job_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 996
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '定时任务日志'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_shop_detail
-- ----------------------------
DROP TABLE IF EXISTS `tz_shop_detail`;
CREATE TABLE `tz_shop_detail` (
  `shop_id`            BIGINT(20)     NOT NULL AUTO_INCREMENT
  COMMENT '店铺id',
  `shop_name`          VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '店铺名称(数字、中文，英文(可混合，不可有特殊字符)，可修改)、不唯一',
  `user_id`            VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '店长用户id',
  `shop_type`          TINYINT(2)     NULL     DEFAULT NULL
  COMMENT '店铺类型',
  `intro`              VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '店铺简介(可修改)',
  `shop_notice`        VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '店铺公告(可修改)',
  `shop_industry`      TINYINT(2)     NULL     DEFAULT NULL
  COMMENT '店铺行业(餐饮、生鲜果蔬、鲜花等)',
  `shop_owner`         VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '店长',
  `mobile`             VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '店铺绑定的手机(登录账号：唯一)',
  `tel`                VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '店铺联系电话',
  `shop_lat`           VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '店铺所在纬度(可修改)',
  `shop_lng`           VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '店铺所在经度(可修改)',
  `shop_address`       VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '店铺详细地址',
  `province`           VARCHAR(10) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '店铺所在省份（描述）',
  `city`               VARCHAR(10) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '店铺所在城市（描述）',
  `area`               VARCHAR(10) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '店铺所在区域（描述）',
  `pca_code`           VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '店铺省市区代码，用于回显',
  `shop_logo`          VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '店铺logo(可修改)',
  `shop_photos`        VARCHAR(1000) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '店铺相册',
  `open_time`          VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '每天营业时间段(可修改)',
  `shop_status`        TINYINT(2)     NULL     DEFAULT NULL
  COMMENT '店铺状态(-1:未开通 0: 停业中 1:营业中)，可修改',
  `transport_type`     TINYINT(2)     NULL     DEFAULT NULL
  COMMENT '0:商家承担运费; 1:买家承担运费',
  `fixed_freight`      DECIMAL(15, 2) NULL     DEFAULT NULL
  COMMENT '固定运费',
  `full_free_shipping` DECIMAL(15, 2) NULL     DEFAULT NULL
  COMMENT '满X包邮',
  `create_time`        DATETIME       NULL
  COMMENT '创建时间',
  `update_time`        DATETIME       NULL     DEFAULT NULL
  COMMENT '更新时间',
  `is_distribution`    TINYINT(2)     NULL     DEFAULT NULL
  COMMENT '分销开关(0:开启 1:关闭)',
  PRIMARY KEY (`shop_id`) USING BTREE,
  UNIQUE INDEX `mobile`(`mobile`) USING BTREE,
  UNIQUE INDEX `shop_user_id`(`user_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 4
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_sku
-- ----------------------------
DROP TABLE IF EXISTS `tz_sku`;
CREATE TABLE `tz_sku` (
  `sku_id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '单品ID',
  `prod_id`          BIGINT(20) UNSIGNED NOT NULL
  COMMENT '商品ID',
  `properties`       VARCHAR(2000) CHARACTER SET utf8
  COLLATE utf8_general_ci                NULL     DEFAULT ''
  COMMENT '销售属性组合字符串 格式是p1:v1;p2:v2',
  `ori_price`        DECIMAL(15, 2)      NULL     DEFAULT NULL
  COMMENT '原价',
  `price`            DECIMAL(15, 2)      NULL     DEFAULT NULL
  COMMENT '价格',
  `stocks`           INT(11)             NOT NULL
  COMMENT '商品在付款减库存的状态下，该sku上未付款的订单数量',
  `actual_stocks`    INT(11)             NULL     DEFAULT NULL
  COMMENT '实际库存',
  `update_time`      DATETIME            NOT NULL
  COMMENT '修改时间',
  `rec_time`         DATETIME            NOT NULL
  COMMENT '记录时间',
  `party_code`       VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci                NULL     DEFAULT NULL
  COMMENT '商家编码',
  `model_id`         VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci                NULL     DEFAULT NULL
  COMMENT '商品条形码',
  `pic`              VARCHAR(500) CHARACTER SET utf8
  COLLATE utf8_general_ci                NULL     DEFAULT NULL
  COMMENT 'sku图片',
  `sku_name`         VARCHAR(120) CHARACTER SET utf8
  COLLATE utf8_general_ci                NULL     DEFAULT NULL
  COMMENT 'sku名称',
  `prod_name`        VARCHAR(255) CHARACTER SET utf8
  COLLATE utf8_general_ci                NULL     DEFAULT NULL
  COMMENT '商品名称',
  `version`          INT(11)             NOT NULL DEFAULT 0
  COMMENT '版本号',
  `weight`           DOUBLE              NULL     DEFAULT NULL
  COMMENT '商品重量',
  `volume`           DOUBLE              NULL     DEFAULT NULL
  COMMENT '商品体积',
  `status`           TINYINT(2)          NULL     DEFAULT 1
  COMMENT '0 禁用 1 启用',
  `is_delete`        TINYINT(2)          NULL     DEFAULT NULL
  COMMENT '0 正常 1 已被删除',
  `is_need_cash`     TINYINT(1)          NULL     DEFAULT 1,
  `is_need_integral` TINYINT(1)          NULL     DEFAULT 0,
  `integral_price`   INT(11)             NULL     DEFAULT 0,
  PRIMARY KEY (`sku_id`) USING BTREE,
  INDEX `prod_id`(`prod_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 427
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '单品SKU表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_sms_log
-- ----------------------------
DROP TABLE IF EXISTS `tz_sms_log`;
CREATE TABLE `tz_sms_log` (
  `id`            BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT 'ID',
  `user_id`       VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '用户id',
  `user_phone`    VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci             NOT NULL DEFAULT ''
  COMMENT '手机号码',
  `content`       VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci             NOT NULL DEFAULT ''
  COMMENT '短信内容',
  `mobile_code`   VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci             NOT NULL DEFAULT ''
  COMMENT '手机验证码',
  `type`          INT(1)              NOT NULL DEFAULT 0
  COMMENT '短信类型  1:注册  2:验证',
  `rec_date`      DATETIME            NOT NULL
  COMMENT '发送时间',
  `response_code` VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '发送短信返回码',
  `status`        INT(1)              NOT NULL DEFAULT 0
  COMMENT '状态  1:有效  0：失效',
  PRIMARY KEY (`id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 15
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '短信记录表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_sys_config
-- ----------------------------
DROP TABLE IF EXISTS `tz_sys_config`;
CREATE TABLE `tz_sys_config` (
  `id`          BIGINT(20) NOT NULL AUTO_INCREMENT,
  `param_key`   VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci  NULL     DEFAULT NULL
  COMMENT 'key',
  `param_value` VARCHAR(2000) CHARACTER SET utf8
  COLLATE utf8_general_ci  NULL     DEFAULT NULL
  COMMENT 'value',
  `remark`      VARCHAR(500) CHARACTER SET utf8
  COLLATE utf8_general_ci  NULL     DEFAULT NULL
  COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `key`(`param_key`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '系统配置信息表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_sys_log
-- ----------------------------
DROP TABLE IF EXISTS `tz_sys_log`;
CREATE TABLE `tz_sys_log` (
  `id`          BIGINT(20) NOT NULL AUTO_INCREMENT,
  `username`    VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci  NULL     DEFAULT NULL
  COMMENT '用户名',
  `operation`   VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci  NULL     DEFAULT NULL
  COMMENT '用户操作',
  `method`      VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci  NULL     DEFAULT NULL
  COMMENT '请求方法',
  `params`      VARCHAR(5000) CHARACTER SET utf8
  COLLATE utf8_general_ci  NULL     DEFAULT NULL
  COMMENT '请求参数',
  `time`        BIGINT(20) NOT NULL
  COMMENT '执行时长(毫秒)',
  `ip`          VARCHAR(64) CHARACTER SET utf8
  COLLATE utf8_general_ci  NULL     DEFAULT NULL
  COMMENT 'IP地址',
  `create_date` DATETIME   NULL     DEFAULT NULL
  COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 852
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '系统日志'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `tz_sys_menu`;
CREATE TABLE `tz_sys_menu` (
  `menu_id`   BIGINT(20)  NOT NULL AUTO_INCREMENT,
  `parent_id` BIGINT(20)  NULL     DEFAULT NULL
  COMMENT '父菜单ID，一级菜单为0',
  `name`      VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci NULL     DEFAULT NULL
  COMMENT '菜单名称',
  `url`       VARCHAR(200) CHARACTER SET utf8
  COLLATE utf8_general_ci NULL     DEFAULT NULL
  COMMENT '菜单URL',
  `perms`     VARCHAR(500) CHARACTER SET utf8
  COLLATE utf8_general_ci NULL     DEFAULT NULL
  COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type`      INT(11)     NULL     DEFAULT NULL
  COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon`      VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci NULL     DEFAULT NULL
  COMMENT '菜单图标',
  `order_num` INT(11)     NULL     DEFAULT NULL
  COMMENT '排序',
  PRIMARY KEY (`menu_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 316
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '菜单管理'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `tz_sys_role`;
CREATE TABLE `tz_sys_role` (
  `role_id`        BIGINT(20) NOT NULL AUTO_INCREMENT,
  `role_name`      VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci     NULL     DEFAULT NULL
  COMMENT '角色名称',
  `remark`         VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci     NULL     DEFAULT NULL
  COMMENT '备注',
  `create_user_id` BIGINT(20) NULL     DEFAULT NULL
  COMMENT '创建者ID',
  `create_time`    DATETIME   NULL
  COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '角色'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `tz_sys_role_menu`;
CREATE TABLE `tz_sys_role_menu` (
  `id`      BIGINT(20) NOT NULL AUTO_INCREMENT,
  `role_id` BIGINT(20) NULL     DEFAULT NULL
  COMMENT '角色ID',
  `menu_id` BIGINT(20) NULL     DEFAULT NULL
  COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 178
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '角色与菜单对应关系'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `tz_sys_user`;
CREATE TABLE `tz_sys_user` (
  `user_id`        BIGINT(20) NOT NULL AUTO_INCREMENT,
  `username`       VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci     NOT NULL
  COMMENT '用户名',
  `password`       VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci     NULL     DEFAULT NULL
  COMMENT '密码',
  `email`          VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci     NULL     DEFAULT NULL
  COMMENT '邮箱',
  `mobile`         VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci     NULL     DEFAULT NULL
  COMMENT '手机号',
  `status`         TINYINT(4) NULL     DEFAULT NULL
  COMMENT '状态  0：禁用   1：正常',
  `create_user_id` BIGINT(20) NULL     DEFAULT NULL
  COMMENT '创建者ID',
  `create_time`    DATETIME   NULL
  COMMENT '创建时间',
  `shop_id`        BIGINT(20) NULL     DEFAULT NULL
  COMMENT '用户所在的商城Id',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '系统用户'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tz_sys_user_role`;
CREATE TABLE `tz_sys_user_role` (
  `id`      BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NULL     DEFAULT NULL
  COMMENT '用户ID',
  `role_id` BIGINT(20) NULL     DEFAULT NULL
  COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '用户与角色对应关系'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_transcity
-- ----------------------------
DROP TABLE IF EXISTS `tz_transcity`;
CREATE TABLE `tz_transcity` (
  `transcity_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `transfee_id`  BIGINT(20) NULL     DEFAULT NULL
  COMMENT '运费项id',
  `city_id`      BIGINT(20) NULL     DEFAULT NULL
  COMMENT '城市id',
  PRIMARY KEY (`transcity_id`) USING BTREE,
  INDEX `transfee_id`(`transfee_id`) USING BTREE,
  INDEX `city_id`(`city_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 666
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_transcity_free
-- ----------------------------
DROP TABLE IF EXISTS `tz_transcity_free`;
CREATE TABLE `tz_transcity_free` (
  `transcity_free_id` BIGINT(20) NOT NULL AUTO_INCREMENT
  COMMENT '指定条件包邮城市项id',
  `transfee_free_id`  BIGINT(20) NULL     DEFAULT NULL
  COMMENT '指定条件包邮项id',
  `free_city_id`      BIGINT(20) NULL     DEFAULT NULL
  COMMENT '城市id',
  PRIMARY KEY (`transcity_free_id`) USING BTREE,
  INDEX `transfee_free_id`(`transfee_free_id`) USING BTREE,
  INDEX `city_id`(`free_city_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 2325
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_transfee
-- ----------------------------
DROP TABLE IF EXISTS `tz_transfee`;
CREATE TABLE `tz_transfee` (
  `transfee_id`      BIGINT(20)     NOT NULL AUTO_INCREMENT
  COMMENT '运费项id',
  `transport_id`     BIGINT(20)     NULL     DEFAULT NULL
  COMMENT '运费模板id',
  `continuous_piece` DECIMAL(15, 2) NULL     DEFAULT NULL
  COMMENT '续件数量',
  `first_piece`      DECIMAL(15, 2) NULL     DEFAULT NULL
  COMMENT '首件数量',
  `continuous_fee`   DECIMAL(15, 2) NULL     DEFAULT NULL
  COMMENT '续件费用',
  `first_fee`        DECIMAL(15, 2) NULL     DEFAULT NULL
  COMMENT '首件费用',
  PRIMARY KEY (`transfee_id`) USING BTREE,
  INDEX `transport_id`(`transport_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 120
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_transfee_free
-- ----------------------------
DROP TABLE IF EXISTS `tz_transfee_free`;
CREATE TABLE `tz_transfee_free` (
  `transfee_free_id` BIGINT(20)     NOT NULL AUTO_INCREMENT
  COMMENT '指定条件包邮项id',
  `transport_id`     BIGINT(20)     NULL     DEFAULT NULL
  COMMENT '运费模板id',
  `free_type`        TINYINT(2)     NULL     DEFAULT NULL
  COMMENT '包邮方式 （0 满x件/重量/体积包邮 1满金额包邮 2满x件/重量/体积且满金额包邮）',
  `amount`           DECIMAL(15, 2) NULL     DEFAULT NULL
  COMMENT '需满金额',
  `piece`            DECIMAL(15, 2) NULL     DEFAULT NULL
  COMMENT '包邮x件/重量/体积',
  PRIMARY KEY (`transfee_free_id`) USING BTREE,
  INDEX `transport_id`(`transport_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 60
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_transport
-- ----------------------------
DROP TABLE IF EXISTS `tz_transport`;
CREATE TABLE `tz_transport` (
  `transport_id`       BIGINT(20) NOT NULL AUTO_INCREMENT
  COMMENT '运费模板id',
  `trans_name`         VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci         NULL     DEFAULT NULL
  COMMENT '运费模板名称',
  `create_time`        DATETIME   NULL
  COMMENT '创建时间',
  `shop_id`            BIGINT(20) NULL     DEFAULT NULL
  COMMENT '店铺id',
  `charge_type`        TINYINT(2) NULL     DEFAULT NULL
  COMMENT '收费方式（0 按件数,1 按重量 2 按体积）',
  `is_free_fee`        TINYINT(2) NULL     DEFAULT NULL
  COMMENT '是否包邮 0:不包邮 1:包邮',
  `has_free_condition` TINYINT(2) NULL     DEFAULT NULL
  COMMENT '是否含有包邮条件 0 否 1是',
  PRIMARY KEY (`transport_id`) USING BTREE,
  INDEX `shop_id`(`shop_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 51
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_user
-- ----------------------------
DROP TABLE IF EXISTS `tz_user`;
CREATE TABLE `tz_user` (
  `user_id`        VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci   NOT NULL DEFAULT ''
  COMMENT 'ID',
  `nick_name`      VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL     DEFAULT NULL
  COMMENT '用户昵称',
  `real_name`      VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL     DEFAULT NULL
  COMMENT '真实姓名',
  `user_mail`      VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL     DEFAULT NULL
  COMMENT '用户邮箱',
  `login_password` VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL     DEFAULT NULL
  COMMENT '登录密码',
  `pay_password`   VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL     DEFAULT NULL
  COMMENT '支付密码',
  `user_mobile`    VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL     DEFAULT NULL
  COMMENT '手机号码',
  `modify_time`    DATETIME NOT NULL
  COMMENT '修改时间',
  `user_regtime`   DATETIME NOT NULL
  COMMENT '注册时间',
  `user_regip`     VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL     DEFAULT NULL
  COMMENT '注册IP',
  `user_lasttime`  DATETIME NULL     DEFAULT NULL
  COMMENT '最后登录时间',
  `user_lastip`    VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL     DEFAULT NULL
  COMMENT '最后登录IP',
  `user_memo`      VARCHAR(500) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL     DEFAULT NULL
  COMMENT '备注',
  `sex`            CHAR(1) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL     DEFAULT 'M'
  COMMENT 'M(男) or F(女)',
  `birth_date`     CHAR(10) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL     DEFAULT NULL
  COMMENT '例如：2009-11-27',
  `pic`            VARCHAR(255) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL     DEFAULT NULL
  COMMENT '头像图片路径',
  `status`         INT(1)   NOT NULL DEFAULT 1
  COMMENT '状态 1 正常 0 无效',
  `score`          INT(11)  NULL     DEFAULT NULL
  COMMENT '用户积分',
  `country_code`   VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL     DEFAULT '86',
  `consumer_id`    VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci   NULL     DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `ud_user_mail`(`user_mail`) USING BTREE,
  UNIQUE INDEX `ud_user_unique_mobile`(`user_mobile`) USING BTREE
)
  ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '用户表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_user_addr
-- ----------------------------
DROP TABLE IF EXISTS `tz_user_addr`;
CREATE TABLE `tz_user_addr` (
  `addr_id`     BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT 'ID',
  `user_id`     VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci           NOT NULL DEFAULT '0'
  COMMENT '用户ID',
  `receiver`    VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '收货人',
  `province_id` BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '省ID',
  `province`    VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '省',
  `city`        VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '城市',
  `city_id`     BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '城市ID',
  `area`        VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '区',
  `area_id`     BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '区ID',
  `post_code`   VARCHAR(15) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '邮编',
  `addr`        VARCHAR(1000) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '地址',
  `mobile`      VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci           NULL     DEFAULT NULL
  COMMENT '手机',
  `status`      INT(1)              NOT NULL
  COMMENT '状态,1正常，0无效',
  `common_addr` INT(1)              NOT NULL DEFAULT 0
  COMMENT '是否默认地址 1是',
  `create_time` DATETIME            NOT NULL
  COMMENT '建立时间',
  `version`     INT(5)              NOT NULL DEFAULT 0
  COMMENT '版本号',
  `update_time` DATETIME            NOT NULL
  COMMENT '更新时间',
  PRIMARY KEY (`addr_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 8
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '用户配送地址'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_user_addr_order
-- ----------------------------
DROP TABLE IF EXISTS `tz_user_addr_order`;
CREATE TABLE `tz_user_addr_order` (
  `addr_order_id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT 'ID',
  `addr_id`       BIGINT(20) UNSIGNED NOT NULL
  COMMENT '地址ID',
  `user_id`       VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci             NOT NULL DEFAULT '0'
  COMMENT '用户ID',
  `receiver`      VARCHAR(50) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '收货人',
  `province_id`   BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '省ID',
  `province`      VARCHAR(100) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '省',
  `area_id`       BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '区域ID',
  `area`          VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '区',
  `city_id`       BIGINT(20)          NULL     DEFAULT NULL
  COMMENT '城市ID',
  `city`          VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '城市',
  `addr`          VARCHAR(1000) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '地址',
  `post_code`     VARCHAR(15) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '邮编',
  `mobile`        VARCHAR(20) CHARACTER SET utf8
  COLLATE utf8_general_ci             NULL     DEFAULT NULL
  COMMENT '手机',
  `create_time`   DATETIME            NOT NULL
  COMMENT '建立时间',
  `version`       INT(5)              NOT NULL DEFAULT 0
  COMMENT '版本号',
  PRIMARY KEY (`addr_order_id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 34
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  COMMENT = '用户订单配送地址'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for tz_user_collection
-- ----------------------------
DROP TABLE IF EXISTS `tz_user_collection`;
CREATE TABLE `tz_user_collection` (
  `id`          BIGINT(20) NOT NULL AUTO_INCREMENT
  COMMENT '收藏表',
  `prod_id`     BIGINT(20) NULL     DEFAULT NULL
  COMMENT '商品id',
  `user_id`     VARCHAR(36) CHARACTER SET utf8
  COLLATE utf8_general_ci  NOT NULL
  COMMENT '用户id',
  `create_time` DATETIME   NULL
  COMMENT '收藏时间',
  PRIMARY KEY (`id`) USING BTREE
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 7
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
