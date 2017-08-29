/*
Navicat MySQL Data Transfer

Source Server         : nlp422
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : dataset

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-08-29 18:35:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for location
-- ----------------------------
DROP TABLE IF EXISTS `location`;
CREATE TABLE `location` (
  `sid` varchar(255) NOT NULL,
  `lid` int(255) NOT NULL AUTO_INCREMENT,
  `start_left` int(11) NOT NULL,
  `start_right` int(11) NOT NULL,
  `end_left` int(11) NOT NULL,
  `end_right` int(11) NOT NULL,
  PRIMARY KEY (`lid`),
  KEY `sid` (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sentence
-- ----------------------------
DROP TABLE IF EXISTS `sentence`;
CREATE TABLE `sentence` (
  `sentence` varchar(1000) DEFAULT NULL,
  `sid` varchar(255) NOT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
