/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost
 Source Database       : test

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : utf-8

 Date: 06/28/2017 13:58:05 PM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `sso_user`
-- ----------------------------
DROP TABLE IF EXISTS `sso_user`;
CREATE TABLE `sso_user` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `u_name` varchar(128) NOT NULL COMMENT '用户名',
  `u_real_name` varchar(128) DEFAULT NULL COMMENT '用户姓名',
  `u_pwd` varchar(128) DEFAULT NULL COMMENT '用户密码',
  `u_email` varchar(128) DEFAULT NULL COMMENT '电子邮箱',
  `u_mobile` varchar(128) DEFAULT NULL COMMENT '手机号码',
  `u_create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `u_status` varchar(20) DEFAULT NULL COMMENT '用户状态',
  `u_sex` varchar(20) DEFAULT NULL COMMENT '用户性别',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_name` (`u_name`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='用户';

-- ----------------------------
--  Records of `sso_user`
-- ----------------------------
BEGIN;
INSERT INTO `sso_user` VALUES ('2', 'admin', '王贵源', '928bfd2577490322a6e19b793691467e', 'kerbores@zhcs.club', '18996359755', '2017-05-06 14:38:09', 'ENABLED', 'MALE'), ('3', 'sino201079691', null, null, 'test6709674272763495424@sinosoft.com.cn', null, null, 'DISABLED', 'MALE'), ('4', 'sino755850033', null, null, 'test4722312843154000896@sinosoft.com.cn', null, null, 'DISABLED', 'MALE'), ('5', 'sino652601210', null, null, 'test7984513809239708672@sinosoft.com.cn', null, null, 'DISABLED', 'MALE'), ('6', 'sino2127232939', null, null, 'test6151620604692021248@sinosoft.com.cn', null, null, 'DISABLED', 'MALE'), ('7', 'sino656585142', null, null, 'test5549111549019308032@sinosoft.com.cn', null, null, 'DISABLED', 'MALE'), ('8', 'sino773878883', null, null, 'test8413749733021370368@sinosoft.com.cn', null, null, 'DISABLED', 'MALE'), ('9', 'sino1573313053', null, null, 'test6377608768863233024@sinosoft.com.cn', null, null, 'DISABLED', 'MALE'), ('10', 'sino504718935', null, null, 'test1579006413694270464@sinosoft.com.cn', null, null, 'ENABLED', 'MALE'), ('11', 'sino1887309721', null, null, 'test975964715118947328@sinosoft.com.cn', null, null, 'ENABLED', 'MALE'), ('12', 'sino1034721323', null, null, 'test4835427169441309696@sinosoft.com.cn', null, null, 'ENABLED', 'MALE'), ('13', 'sino1021478748', null, null, 'test9064330712599843840@sinosoft.com.cn', null, null, 'ENABLED', 'MALE'), ('14', 'sino827141487', null, null, 'test3830349064279198720@sinosoft.com.cn', null, null, 'ENABLED', 'MALE'), ('15', 'sino1516747811', null, null, 'test6350472454489997312@sinosoft.com.cn', null, null, 'DISABLED', 'MALE'), ('16', 'sino793910750', null, null, 'test6704075674600424448@sinosoft.com.cn', null, null, 'DISABLED', 'MALE'), ('17', 'sino178640245', null, null, 'test2774875682346895360@sinosoft.com.cn', null, null, 'ENABLED', 'MALE');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
