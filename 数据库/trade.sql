/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50515
Source Host           : localhost:3306
Source Database       : trade

Target Server Type    : MYSQL
Target Server Version : 50515
File Encoding         : 65001

Date: 2018-01-03 10:52:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect` (
  `collectionid` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `shopid` int(11) NOT NULL COMMENT '商品id',
  `username` varchar(1024) COLLATE utf8_unicode_ci NOT NULL COMMENT '收藏者姓名',
  PRIMARY KEY (`collectionid`),
  foreign key(shopid) references shop(shopid) on delete cascade
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of collect
-- ----------------------------

-- ----------------------------
-- Table structure for look
-- ----------------------------
DROP TABLE IF EXISTS `look`;
CREATE TABLE `look` (
  `lookid` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `lookname` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '求购商品名',
  `description` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '求购描述',
  `username` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '求购者姓名',
  `userphone` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '求购者电话',
  `category` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '求购类别',
  `put_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`lookid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of look
-- ----------------------------
INSERT INTO `look` VALUES ('1', 'PS期末设计', 'PS期末设计，包售后', '张二狗', '15742516617', '办公用品', '2020-7-27 14:34:41');
INSERT INTO `look` VALUES ('2', '无线鼠标', '设备良好，已使用三个月', '张二狗', '18089653214', '电子产品', '2020-7-27 14:51:31');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `messageid` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `content` varchar(1024) COLLATE utf8_unicode_ci NOT NULL COMMENT '留言内容',
  `username` varchar(1024) COLLATE utf8_unicode_ci NOT NULL COMMENT '留言者姓名',
  `receivename` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '接收留言者姓名',
  `leave_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '留言时间',
  PRIMARY KEY (`messageid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES ('1', '你好', '苗翠花', '张二狗', '2017-12-16 10:04:50');
INSERT INTO `message` VALUES ('5', '换个风格', '张二狗', '张二狗', '2017-12-18 15:20:22');

-- ----------------------------
-- Table structure for shop
-- ----------------------------
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop` (
  `shopid` int(11) NOT NULL AUTO_INCREMENT COMMENT '药品id',
  `shopname` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '药品名',
  `description` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '药品描述',
  `username` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '发布者姓名',
  `userphone` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户电话',
  `category` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '商品类别',
  `picture` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '商品图片',
  `price` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '商品价格',
  `put_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '发布时间',
  PRIMARY KEY (`shopid`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of shop
-- ----------------------------
INSERT INTO `shop` VALUES ('9', '板蓝根', '【性味归经】味苦，性寒。归心经、胃经。\r\n　　【功效与作用】清热解毒，凉血消肿。属清热药下属中的清热解毒药。\r\n　　【临床应用】9～15克，煎服。用治温病发斑、丹毒、流感、流脑，临床主要用治病毒性及细菌性疾病，如乙型肝炎、水痘、扁桃体炎、咽炎等。\r\n　　【药理研究】水煎剂具一定的抑菌作用，体外对病毒增殖有抑制作用，还具解热、抗炎作用。\r\n　　【化学成分】全草含三萜类。靛玉红有抗癌活性-(3H)-喹唑酮有抗炎、抗高血压活性，根含吲哚苷、大黄酚及多种氨基酸。另含靛苷、靛玉红羽扇豆酮、羽扇豆醇、白桦脂醇、β-谷固醇等成分。\r\n　　【使用禁忌】脾胃虚寒、无实火热毒者慎用。\r\n　　【相关药方】①治流行性腮腺炎：南板蓝根30克，或配金银花、蒲公英各15克，水煎服;外用鲜马蓝叶捣敷。(《浙南本革新编》)', '李二狗', '18798349438', '生活用品', '', '6.00元', '2020-7-28 08:51:25');
INSERT INTO `shop` VALUES ('10', '三七', '【性味归经】性温，味甘、微苦。归肝经、胃经。\r\n　　【功效与作用】散瘀止血、消肿定痛。属止血药下属分类的化瘀止血药。\r\n　　【临床应用】用量5～10克，煎汤服；外用磨汁涂、研末撒或调敷患处。用治咯血、吐血、衄血、便血、崩漏、外伤出血、胸腹刺痛、跌扑肿痛。', '张二狗', '15275426617', '生活用品', '', '600.00元', '2020-7-28 08:53:02');
INSERT INTO `shop` VALUES ('11', '厚朴', '【性味归经】性温，味苦、辛。归脾经、胃经、肺经、大肠经。\r\n　　【功效与作用】燥湿消痰、下气除满。属化湿药。\r\n　　【临床应用】用量3～9克，水煎服。用治湿滞伤中、脘痞吐泻、食积气滞、腹胀便秘、痰饮喘咳。\r\n　　【药理研究】具有肌肉松弛作用；小剂量兴奋肠管平滑肌，而大剂量具有抑制作用；具有抗溃疡的作用，有显著的中枢抑制作用，降血压，抗病原微生物，抗肿瘤；还有抗血小板和抑制细胞内钙流动等作用。', '张二狗', '15754216358', '生活用品', '', '25.00元', '2020-7-28 08:54:35');
INSERT INTO `shop` VALUES ('31', '花椒', '【性味归经】性温，味辛。归脾经、胃经、肾经。\r\n　　【功效与作用】温中止痛、杀虫止痒。属温里药。\r\n　　【临床应用】用量3～6克，外用适量，煎汤熏洗。用治脘腹冷痛、呕吐泄泻、虫积腹痛、蛔虫症等；外用治湿疹瘙痒。\r\n　　【药理研究】抗实验性胃溃疡、对肠平滑肌运动有双向作用、抗腹泻、保肝、镇静抗炎、局部麻醉、抗凝血、杀疥螨。药理实验表明，果皮注射液有止痛、麻醉作用;牻牛儿醇小量可引起离体肠管蠕动，大量则使之抑制;水煎剂对链球菌、葡萄球菌、肺炎球菌、炭疽杆菌、枯草杆菌、霍乱弧菌、副伤寒杆菌和绿脓杆菌均有抑制作用；所含挥发油可使蛔虫、蛲虫中毒。', '张二狗', '12345678910', '生活用品', '', '30.00元', '2020-7-28 08:55:07');
INSERT INTO `shop` VALUES ('32', '独活', '【药材性状】略圆柱形，下部2～3分枝或更多，长10～30厘米。根头部膨大，圆锥状，多横皱纹，直径1.5～3厘米，顶端有茎、叶的残基或凹陷，表面灰褐色或棕褐色，具纵皱纹，有隆起的横长皮孔及稍突起的细根痕。质较硬，受潮则变软，断面皮部灰白色，有多数散在的棕色油室，木部灰黄色至黄棕色，形成层环棕色。有特异香气，味苦、辛，微麻舌。\r\n　　【性味归经】性微温，味辛、苦。归肾经、膀胱经。\r\n　　【功效与作用】祛风除湿、通痹止痛。属祛风湿药下属分类的祛风湿散寒药。\r\n　　【临床应用】用量3～9克，煎服；或浸酒；或入丸、散；外用适量，煎汤洗。用治风寒湿痛、腰膝疼痛、少阴伏风头痛。', 'rerere', 'fwefewfewfew', '生活用品', '', '30.00元', '2020-7-28 09:00:14');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `userid` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户密码',
  `email` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户邮箱',
  `school` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户所在学校',
  `court` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户所在院',
  `professional` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户所学专业',
  PRIMARY KEY (`userid`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', '张二狗', '123456', '1600351631@qq.com', '青岛滨海学院', '信工', '计算机');
INSERT INTO `users` VALUES ('2', '苗翠花', 'zhangyu123456', '18006391760@139.com', '中国石油大学', '', '');
INSERT INTO `users` VALUES ('3', '123', '123', '15023155@qq.com', '青岛滨海学院', '文理工程学院', '电商');
INSERT INTO `users` VALUES ('4', '', '', '', '', '', '');
INSERT INTO `users` VALUES ('5', '李二狗', '123', '18006391670@186.com', '山东大学', '数学系', '计算机应用数学');
INSERT INTO `users` VALUES ('6', 'rerere', '123456', '18906391760@qq.com', 'sds', '18006391670', null);

-- ----------------------------
-- Table structure for users_copy
-- ----------------------------
DROP TABLE IF EXISTS `users_copy`;
CREATE TABLE `users_copy` (
  `userid` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户密码',
  `email` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户邮箱',
  `school` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户所在学校',
  `court` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户所在院',
  `professional` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户所学专业',
  PRIMARY KEY (`userid`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of users_copy
-- ----------------------------
DROP TABLE IF EXISTS `icon`;
create table `icon`
(
	`iconid` int(11) NOT NULL AUTO_INCREMENT,
	`userid` int(11) NOT NULL  COMMENT '用户id',
	`path`  varchar(64)  COLLATE utf8_unicode_ci NOT NULL COMMENT '用户头像名称',
	PRIMARY KEY(`iconid`),
	foreign key(userid) references users(`userid`) on delete cascade
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


delimiter $$
create trigger auto_copy after insert on users for each row  

begin
insert into users_copy(username,password,email,school,court,professional) values(new.username,new.password,new.email,new.school,new.court,new.professional);
end$$

delimiter $$
create trigger auto_update after update on users for each row  

begin
update users_copy set password = new.password,email=new.email,school=new.school,court=new.court,professional=new.professional where username = new.username;
end$$


set sql_safe_updates = 0;

