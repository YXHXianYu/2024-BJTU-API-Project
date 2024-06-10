/*
 Navicat Premium Data Transfer

 Source Server         : Aliyun
 Source Server Type    : MySQL
 Source Server Version : 80036 (8.0.36-0ubuntu0.22.04.1)
 Source Host           : 60.205.253.222:3306
 Source Schema         : tiktok

 Target Server Type    : MySQL
 Target Server Version : 80036 (8.0.36-0ubuntu0.22.04.1)
 File Encoding         : 65001

 Date: 10/06/2024 16:40:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('44636283-00cd-4693-9090-c4b69caa11b9', 'admin', '663e33644c3166a0a75b25169034aefb', 'example@email.com', '10123456789');
INSERT INTO `user` VALUES ('b8ebd099-a26c-4291-b090-d849c2cdc9d6', 'user1', '663e33644c3166a0a75b25169034aefb', 'example@email.com', '10123456789');
INSERT INTO `user` VALUES ('edf1ec68-1e52-45db-9ace-99c4ab21ca5c', 'user', '663e33644c3166a0a75b25169034aefb', 'example@email.com', '10123456789');

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
  PRIMARY KEY (`uuid`) USING BTREE,
  UNIQUE INDEX `title`(`title` ASC) USING BTREE,
  UNIQUE INDEX `filepath`(`filepath` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of video
-- ----------------------------
-- INSERT INTO `video` VALUES ('35fa1f2b-1e9d-4edf-ac92-e3070915336f', 'COD挂', '.\\video-upload-dir\\35fa1f2b-1e9d-4edf-ac92-e3070915336f.mp4', 0, 'edf1ec68-1e52-45db-9ace-99c4ab21ca5c');
-- INSERT INTO `video` VALUES ('70f78bce-f1e5-4196-b928-ec7432bf0b0b', 'Inside The Backrooms如何溜窃皮者', '.\\video-upload-dir\\70f78bce-f1e5-4196-b928-ec7432bf0b0b.mp4', 0, 'edf1ec68-1e52-45db-9ace-99c4ab21ca5c');

SET FOREIGN_KEY_CHECKS = 1;
