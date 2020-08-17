/*
 Navicat Premium Data Transfer

 Source Server         : lucky
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : 47.100.55.116:3306
 Source Schema         : lucky_organization

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 19/05/2020 15:21:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user`  (
  `id` varchar(36) NOT NULL,
  `user_id` varchar(36) NOT NULL COMMENT '用户id',
  `username` varchar(45) DEFAULT '' COMMENT '真实姓名',
  `account` varchar(36) DEFAULT '' COMMENT '账号',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `email` varchar(30) DEFAULT '' COMMENT '用户邮箱',
  `phone` varchar(13) DEFAULT '' COMMENT '手机号码',
  `gender` tinyint DEFAULT '0' COMMENT '性别 0男 1女',
  `created_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建的时间',
  `updated_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改的时间',
  `created_by` varchar(30) DEFAULT ''  COMMENT '创建人',
  `updated_by` varchar(30) DEFAULT ''  COMMENT '修改人',
  `is_del` tinyint DEFAULT '0' COMMENT '是否删除 0否 1是',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_id` (`user_id`),
  UNIQUE KEY `idx_account` (`account`),
  UNIQUE KEY `idx_phone` (`phone`),
  KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';

SET FOREIGN_KEY_CHECKS = 1;
