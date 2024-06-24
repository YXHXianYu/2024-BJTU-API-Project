/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : tiktok

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 23/06/2024 20:58:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for autho
-- ----------------------------
DROP TABLE IF EXISTS `autho`;
CREATE TABLE `autho`  (
  `uuid` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of autho
-- ----------------------------
INSERT INTO `autho` VALUES ('19d78067-bd0b-4b41-8740-ae394db9489f', '663e33644c3166a0a75b25169034aefb');
INSERT INTO `autho` VALUES ('45e4da4f-18e0-4ea3-8be4-3a073214eaa6', '250908eebc14df5c2f93da590bccc668');
INSERT INTO `autho` VALUES ('50809122-581d-49cb-8187-164dc1be8356', '663e33644c3166a0a75b25169034aefb');
INSERT INTO `autho` VALUES ('929c329e-eef4-4312-84d9-54193d52b3e2', '663e33644c3166a0a75b25169034aefb');
INSERT INTO `autho` VALUES ('9c015f59-4ab1-49cd-a69d-6e070ca798c4', '663e33644c3166a0a75b25169034aefb');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `telephone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('19d78067-bd0b-4b41-8740-ae394db9489f', 'user1', '***', '2943003@qq.com', '18123456789');
INSERT INTO `user` VALUES ('45e4da4f-18e0-4ea3-8be4-3a073214eaa6', 'user_different_passwd', '***', 'different@gmail.com', '11451419198');
INSERT INTO `user` VALUES ('50809122-581d-49cb-8187-164dc1be8356', 'user2', '***', '2943003@qq.com', '18123456789');
INSERT INTO `user` VALUES ('929c329e-eef4-4312-84d9-54193d52b3e2', 'user', '***', '2943003@qq.com', '18123456789');
INSERT INTO `user` VALUES ('9c015f59-4ab1-49cd-a69d-6e070ca798c4', 'admin', '***', '2943003@qq.com', '18123456789');

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video`  (
  `uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `filepath` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `likes` int NOT NULL DEFAULT 0,
  `userUuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `title`(`title` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;


SET FOREIGN_KEY_CHECKS = 1;
