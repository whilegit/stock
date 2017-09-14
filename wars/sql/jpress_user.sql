-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: jpress
-- ------------------------------------------------------
-- Server version	5.5.40-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `jpress_user`
--

DROP TABLE IF EXISTS `jpress_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jpress_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(128) DEFAULT NULL COMMENT '登录名',
  `nickname` varchar(128) DEFAULT NULL COMMENT '昵称',
  `realname` varchar(128) DEFAULT NULL COMMENT '实名',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `salt` varchar(32) DEFAULT NULL COMMENT '盐',
  `deal_password` varchar(32) DEFAULT NULL COMMENT '【新增】交易密码',
  `deal_salt` varchar(32) DEFAULT NULL COMMENT '【新增】交易密码的盐值',
  `email` varchar(64) DEFAULT NULL COMMENT '邮件',
  `email_status` varchar(32) DEFAULT NULL COMMENT '邮箱状态（是否认证等）',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机电话',
  `mobile_status` varchar(32) DEFAULT NULL COMMENT '手机状态（是否认证等）',
  `telephone` varchar(32) DEFAULT NULL COMMENT '固定电话',
  `amount` decimal(10,2) unsigned DEFAULT '0.00' COMMENT '金额（余额）',
  `can_borrow_money` int(10) NOT NULL DEFAULT '0' COMMENT '信用额度',
  `sys_push` tinyint(3) NOT NULL DEFAULT '1' COMMENT '系统消息提醒。0,1',
  `sale_push` tinyint(3) NOT NULL DEFAULT '1' COMMENT '交易消息提醒。0,1',
  `in_push` tinyint(3) NOT NULL DEFAULT '1' COMMENT '催收消息提醒。0,1',
  `out_push` tinyint(3) NOT NULL DEFAULT '1' COMMENT '借款订阅消息。0,1',
  `gender` varchar(16) DEFAULT NULL COMMENT '性别',
  `role` varchar(32) DEFAULT 'visitor' COMMENT '权限',
  `perm` varchar(1024) NOT NULL DEFAULT '' COMMENT '【新增】权限',
  `signature` varchar(2048) DEFAULT NULL COMMENT '签名',
  `content_count` int(11) unsigned DEFAULT '0' COMMENT '内容数量',
  `comment_count` int(11) unsigned DEFAULT '0' COMMENT '评论数量',
  `qq` varchar(16) DEFAULT NULL COMMENT 'QQ号码',
  `wechat` varchar(32) DEFAULT NULL COMMENT '微信号',
  `weibo` varchar(256) DEFAULT NULL COMMENT '微博',
  `facebook` varchar(256) DEFAULT NULL,
  `linkedin` varchar(256) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `company` varchar(256) DEFAULT NULL COMMENT '公司',
  `occupation` varchar(256) DEFAULT NULL COMMENT '职位、职业',
  `address` varchar(256) DEFAULT NULL COMMENT '地址',
  `zipcode` varchar(128) DEFAULT NULL COMMENT '邮政编码',
  `site` varchar(256) DEFAULT NULL COMMENT '个人网址',
  `graduateschool` varchar(256) DEFAULT NULL COMMENT '毕业学校',
  `education` varchar(256) DEFAULT NULL COMMENT '学历',
  `avatar` varchar(256) DEFAULT NULL COMMENT '头像',
  `idcardtype` varchar(128) DEFAULT NULL COMMENT '证件类型：身份证 护照 军官证等',
  `idcard` varchar(128) DEFAULT NULL COMMENT '证件号码',
  `idcard_front` varchar(255) DEFAULT '' COMMENT '身份证正面照片',
  `idcard_back` varchar(255) DEFAULT '' COMMENT '身份证反面照片',
  `status` varchar(32) DEFAULT 'normal' COMMENT '状态',
  `created` datetime DEFAULT NULL COMMENT '创建日期',
  `create_source` varchar(128) DEFAULT NULL COMMENT '用户来源（可能来之oauth第三方）',
  `logged` datetime DEFAULT NULL COMMENT '最后的登录时间',
  `activated` datetime DEFAULT NULL COMMENT '激活时间',
  `member_token` varchar(32) DEFAULT NULL COMMENT '【新增】用户令牌，由app使用',
  `score` int(11) NOT NULL DEFAULT '0' COMMENT '积分',
  `overdue` tinyint(3) DEFAULT '0' COMMENT '【新增】是否逾期',
  `credit_record` text COMMENT '【新增】信息记录',
  `can_lend` tinyint(3) DEFAULT '1' COMMENT '【新增】是否有能够直接借出',
  `lat` decimal(7,5) DEFAULT '0.00000' COMMENT '【新增】纬度',
  `lng` decimal(8,5) DEFAULT '0.00000' COMMENT '【新增】经度',
  `bankcard` varchar(45) DEFAULT '' COMMENT '银行卡',
  `banktype` varchar(45) DEFAULT NULL COMMENT '发卡行',
  `zhima_score` int(10) DEFAULT '0' COMMENT '【新增】用户芝麻分',
  `auth_bank` tinyint(3) DEFAULT '0' COMMENT '银行卡是否经过认证',
  `auth_zhima` tinyint(3) DEFAULT '0' COMMENT '【新增】芝麻认证',
  `auth_alipay` tinyint(3) DEFAULT '0' COMMENT '【新增】支付宝是否验证',
  `auth_xuexing` tinyint(3) DEFAULT '0' COMMENT '【新增】学信认证',
  `auth_book` tinyint(3) DEFAULT '0' COMMENT '【新增】通讯录认证',
  `auth_card` tinyint(3) DEFAULT '0' COMMENT '【新增】身份证认证',
  `auth_face` tinyint(3) DEFAULT '0' COMMENT '【新增】人脸验证',
  `auth_gjj` tinyint(3) DEFAULT '0' COMMENT '【新增】公积金认证',
  `interest` decimal(8,2) DEFAULT '0.00' COMMENT '【新增】利息收入',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `mobile` (`mobile`),
  KEY `status` (`status`),
  KEY `created` (`created`),
  KEY `content_count` (`content_count`),
  KEY `comment_count` (`comment_count`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表，保存用户信息。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jpress_user`
--

LOCK TABLES `jpress_user` WRITE;
/*!40000 ALTER TABLE `jpress_user` DISABLE KEYS */;
INSERT INTO `jpress_user` VALUES (1,'yjt','testttestss','林忠仁','70f73a53980c9d07b7925d0d07ade8b516ddf632feee29992d066503f41150d5','448bda287017a3fbc2fac','123',NULL,'6215714@qq.com',NULL,'18968596872','2',NULL,10000.00,3000,1,1,1,1,'1','administrator','perm-edit','xxx',1,0,'6215714',NULL,'while1',NULL,NULL,'1984-07-23 00:00:00',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'/attachment/20170615/4b04908922884382b3f2362948ea54f4.jpg',NULL,'331081198407237619','','','activited','2017-06-06 23:14:30',NULL,'2017-08-21 23:00:39',NULL,'1itFhBUkqqceZ1fKpZlMaX6WwMiUIxQl',0,0,NULL,1,0.00000,0.00000,'6228480402564881235',NULL,0,1,0,0,0,0,1,0,0,10.00),(2,'admin','admin1-nick','杨白劳','5a7612c7e12f83d58f1f185a6c32bae01c37cfb385f26129408ad9bcc6693b32','7b68edf4fd70f076160',NULL,NULL,'admin@yjt.com',NULL,'13012345678',NULL,NULL,0.00,3000,1,1,1,1,NULL,'visitor','user-edit','none',1,0,'6215714',NULL,'aaa',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'331081199008127619','','','activited','2017-06-20 23:33:04',NULL,'2017-06-20 23:33:05',NULL,'geZHCthskHSTgt8aeqg5LijepDeJ8F9j',0,0,NULL,1,0.00000,0.00000,'',NULL,0,0,0,0,0,0,0,0,0,0.00),(3,'test01','test01nick','张三','123456','abcd',NULL,NULL,'11@qq.com','y','13811111111','y','01011111111',0.00,0,1,1,1,1,'1','visitor','contract-list','哥是个传说',0,0,'1111111','wechat_111',NULL,NULL,NULL,'2000-11-01 00:00:00',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','','activited',NULL,NULL,NULL,NULL,'',0,0,NULL,1,0.00000,0.00000,'',NULL,0,0,0,0,0,0,0,0,0,0.00),(4,'test02','test02nick','李四','123456','abcd',NULL,NULL,'22@qq.com','y','13822222222','y','01022222222',0.00,0,1,1,1,1,'2','visitor','','姐是个传说',0,0,'22222222','wechat_111',NULL,NULL,NULL,'2000-11-02 00:00:00',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','','activited',NULL,NULL,NULL,NULL,'',0,0,NULL,1,0.00000,0.00000,'',NULL,0,0,0,0,0,0,0,0,0,0.00);
/*!40000 ALTER TABLE `jpress_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-09-14 11:22:51
