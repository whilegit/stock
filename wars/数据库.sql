-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: rm-bp15891b8nc2bf33go.mysql.rds.aliyuncs.com    Database: yjt
-- ------------------------------------------------------
-- Server version	5.7.15-log

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED='e54af904-7658-11e7-837e-00163e13ede6:1-2817';

--
-- Table structure for table `y_ad`
--

DROP TABLE IF EXISTS `y_ad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_ad` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT '',
  `img` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  `dscrpt` varchar(255) NOT NULL DEFAULT '',
  `order_num` int(10) NOT NULL DEFAULT '0' COMMENT '排序',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '0／1',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_ad`
--

LOCK TABLES `y_ad` WRITE;
/*!40000 ALTER TABLE `y_ad` DISABLE KEYS */;
INSERT INTO `y_ad` VALUES (5,'测试a','/attachment/a.jpg','https://www.qq.com/','qq',10,1,'2017-12-06 22:57:25'),(6,'测试','/attachment/b.jpg','https://www.163.com/','163',10,1,'2017-11-06 21:55:43');
/*!40000 ALTER TABLE `y_ad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_apply`
--

DROP TABLE IF EXISTS `y_apply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_apply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `apply_uid` bigint(20) NOT NULL COMMENT '申请人',
  `amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '借款金额',
  `annual_rate` decimal(4,2) NOT NULL DEFAULT '0.00' COMMENT '年化利率，％',
  `repayment_method` tinyint(3) NOT NULL DEFAULT '0' COMMENT '约定的还款方式，1按月等额本息，2按月等额本金，3一次性还本付息',
  `value_date` datetime DEFAULT NULL COMMENT '用款日，也是起息日',
  `maturity_date` datetime NOT NULL COMMENT '还款日',
  `purpose` tinyint(3) NOT NULL DEFAULT '0' COMMENT '借款用途, 1个人经营，2消费，3创业，4临时周转，5助学，6家居装修，7旅游',
  `description` varchar(256) NOT NULL COMMENT '额外说明',
  `to_friends` varchar(1024) NOT NULL COMMENT '发布对象，多个时以逗号隔开',
  `video` varchar(255) NOT NULL COMMENT '如借款在3000元以上，需要上传视频',
  `create_time` datetime NOT NULL COMMENT '借款申请时间',
  `contract_id` bigint(20) DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态，0无效，1有效，2已达成交易, 3申请达成的交易等待核准',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_apply`
--

LOCK TABLES `y_apply` WRITE;
/*!40000 ALTER TABLE `y_apply` DISABLE KEYS */;
INSERT INTO `y_apply` VALUES (1,2,1000.00,18.00,0,'2017-09-01 17:43:02','2017-10-31 00:00:00',7,'','23,1','','2017-08-27 00:54:05',0,2),(2,1,1000.00,18.00,0,'2017-09-01 17:43:02','2017-10-31 00:00:00',7,'','3','','2017-08-27 00:56:34',25,2),(3,1,1000.00,18.00,0,NULL,'2017-10-31 00:00:00',7,'','23','','2017-08-27 00:56:49',0,1),(4,1,1000.00,18.00,0,'2017-10-08 00:35:59','2017-10-31 00:00:00',7,'','2,3','','2017-08-27 00:58:40',25,2),(5,1,1000.00,18.00,0,NULL,'2017-10-31 00:00:00',7,'','2','','2017-08-27 01:11:18',0,1),(6,8,100.00,10.00,0,'2017-10-08 00:26:26','2017-11-25 00:00:00',1,'','3,4','','2017-11-21 14:00:25',24,2),(7,8,200.00,10.00,0,NULL,'2017-11-25 00:00:00',4,'','3,4','','2017-11-21 14:03:21',0,1),(8,8,200.00,20.00,0,NULL,'2017-11-25 00:00:00',4,'','3','','2017-11-21 14:06:05',0,1),(9,8,400.00,20.00,0,NULL,'2017-11-25 00:00:00',4,'','3','','2017-11-21 14:07:06',0,1),(10,8,100.00,10.00,0,NULL,'2017-12-08 00:00:00',3,'','3','','2017-11-21 15:07:23',0,1),(11,8,300.00,10.00,0,NULL,'2017-12-08 00:00:00',1,'','3','/attachment/api/20171005/d25f69463f9246efb7723c67fed3c3b4.jpg','2017-11-17 15:13:31',0,1),(12,8,10.00,10.00,0,NULL,'2017-10-08 00:00:00',1,'','3','/attachment/api/20171005/3bfb4bf7ba7f476a8ba169e509c72ad4.mp4','2017-11-17 17:16:48',0,1),(13,8,100.00,10.00,0,'2017-10-12 23:12:31','2017-11-22 00:00:00',1,'','3','','2017-10-12 23:10:35',26,2),(14,8,100.00,10.00,0,NULL,'2017-11-22 00:00:00',1,'','3','','2017-10-12 23:21:00',0,1),(15,9,10.30,21.00,0,NULL,'2017-10-27 00:00:00',1,'','10','','2017-10-23 08:51:41',0,1);
/*!40000 ALTER TABLE `y_apply` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_attachment`
--

DROP TABLE IF EXISTS `y_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_attachment` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID主键',
  `title` text COMMENT '标题',
  `user_id` bigint(20) unsigned DEFAULT NULL COMMENT '上传附件的用户ID',
  `content_id` bigint(20) unsigned DEFAULT NULL COMMENT '附件所属的内容ID',
  `path` varchar(512) DEFAULT NULL COMMENT '路径',
  `mime_type` varchar(128) DEFAULT NULL COMMENT 'mime',
  `suffix` varchar(32) DEFAULT NULL COMMENT '附件的后缀',
  `type` varchar(32) DEFAULT NULL COMMENT '类型',
  `flag` varchar(256) DEFAULT NULL COMMENT '标示',
  `lat` decimal(20,16) DEFAULT NULL COMMENT '经度',
  `lng` decimal(20,16) DEFAULT NULL COMMENT '纬度',
  `order_number` int(11) DEFAULT NULL COMMENT '排序字段',
  `created` datetime DEFAULT NULL COMMENT '上传时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `created` (`created`),
  KEY `suffix` (`suffix`),
  KEY `mime_type` (`mime_type`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COMMENT='附件表，用于保存用户上传的附件内容。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_attachment`
--

LOCK TABLES `y_attachment` WRITE;
/*!40000 ALTER TABLE `y_attachment` DISABLE KEYS */;
INSERT INTO `y_attachment` VALUES (1,'borrow-1.jpg',1,NULL,'/attachment/api/20170927/bd6e27b4bfb14556a6038a7d30c62939.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 09:55:55'),(2,'info.log',1,NULL,'/attachment/api/20170927/9adb881487e2410a9b89e172463e8242.log','application/octet-stream','.log',NULL,NULL,NULL,NULL,NULL,'2017-09-27 09:57:12'),(3,'sfjmall_mobile_config.txt',1,NULL,'/attachment/api/20170927/c5d111114adc423ab0d807ae7d0ceb46.txt','text/plain','.txt',NULL,NULL,NULL,NULL,NULL,'2017-09-27 10:01:07'),(4,'2803894a6ddd7fc7b6fee025139f9698.jpg',1,NULL,'/attachment/api/20170927/48715f7815b640bb87609b299b3c6045.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 10:16:52'),(5,'borrow.jpg',1,NULL,'/attachment/api/20170927/6eb72ae0fc5a4bb4ac1a24e9af7606ff.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 10:26:23'),(6,'2803894a6ddd7fc7b6fee025139f9698.jpg',1,NULL,'/attachment/api/20170927/2816e4435f3549d88d66f7ec97d2323b.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 10:58:11'),(7,'2803894a6ddd7fc7b6fee025139f9698.jpg',1,NULL,'/attachment/api/20170927/7e77beae24094cd2a4d2894a6f466b80.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 11:04:18'),(8,'2803894a6ddd7fc7b6fee025139f9698.jpg',1,NULL,'/attachment/api/20170927/e2cae19f4c6b4c6ca41d9f32031f11b2.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 14:29:20'),(9,'2803894a6ddd7fc7b6fee025139f9698.jpg',1,NULL,'/attachment/api/20170927/1223198f35a646fa8673892f06deb07a.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 14:53:06'),(10,'borrow.jpg',1,NULL,'/attachment/api/20170927/f3c6705db69c4906aa0dc2c79441ec04.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 17:28:24'),(11,'borrow-1.jpg',1,NULL,'/attachment/api/20170927/a1a9bc9d8bb241499801ba9af4b467d5.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 17:57:55'),(12,'temp.jpg',7,NULL,'/attachment/api/20170927/31e0255fc3b544218345d8b8f546d2eb.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 17:59:49'),(13,'borrow-1.jpg',1,NULL,'/attachment/api/20170927/466a6b77b2f043eabcdfdc1b2c3fbbf9.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 18:01:59'),(14,'borrow-1.jpg',1,NULL,'/attachment/api/20170927/9cb1ee84ec94493cabee64d96df77e6d.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 18:04:47'),(15,'borrow-1.jpg',1,NULL,'/attachment/api/20170927/627480a9182a44c5978a439fe5113e75.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 18:04:57'),(16,'temp.jpg',7,NULL,'/attachment/api/20170927/cd631240c8a44f16b63d3760e94cb484.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 19:21:08'),(17,'temp.jpg',7,NULL,'/attachment/api/20170927/0b5173a45f5f471698b7a15fc6d40cf9.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 19:22:37'),(18,'temp.jpg',7,NULL,'/attachment/api/20170927/7e4f2317ccd24d6d9ca8332ddd914fdd.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 19:24:46'),(19,'temp.jpg',7,NULL,'/attachment/api/20170927/7749b86c1f524d0fb49824edf8bb4750.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 19:29:36'),(20,'temp.jpg',7,NULL,'/attachment/api/20170927/93bf5276093c48ea92a214f380c36e2f.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 19:29:44'),(21,'temp.jpg',7,NULL,'/attachment/api/20170927/9c7dd996788a46a0947d1b4200a78cf2.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 19:38:40'),(22,'temp.jpg',7,NULL,'/attachment/api/20170927/f56020bbec7e46f9b3dbc66cb3043f3e.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-27 19:38:47'),(23,'temp.jpg',7,NULL,'/attachment/api/20170928/3cdb21a37d584c439a80b596de863ff9.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-28 15:21:18'),(24,'temp.jpg',7,NULL,'/attachment/api/20170928/618274a764cd4511a09cc88751a7b08c.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-28 15:23:03'),(25,'temp.jpg',7,NULL,'/attachment/api/20170928/1a590bafd3d54cd8b75772fab546cf89.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-28 15:23:08'),(26,'temp.jpg',7,NULL,'/attachment/api/20170928/993b377be00449dba6459d8213d5d03c.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-28 16:50:16'),(27,'temp.jpg',7,NULL,'/attachment/api/20170928/1bc386fed69c4962994304db6ee593ce.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-28 16:50:24'),(28,'temp.jpg',7,NULL,'/attachment/api/20170929/7978fc9384a44fd48c1c0c4cc6368769.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-29 09:41:46'),(29,'temp.jpg',7,NULL,'/attachment/api/20170929/0173defb75b94b2db53292c9a868ec13.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-29 09:41:52'),(30,'temp.jpg',8,NULL,'/attachment/api/20170929/f248e5f011b14d49a905189bd2ab44cd.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-29 12:15:53'),(31,'temp.jpg',8,NULL,'/attachment/api/20170929/d41b3ccf434a4353894de4dbe463e052.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-29 12:16:00'),(32,'新建文本文档 (3).txt',1,NULL,'/attachment/api/20170930/86ef96fd1a34493fb44abea2a0631c47.txt','text/plain','.txt',NULL,NULL,NULL,NULL,NULL,'2017-09-30 14:35:50'),(33,'temp.jpg',8,NULL,'\\attachment\\api\\20170930\\0cda6c7e891b43998ecfa069237e3eaa.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-30 15:49:26'),(34,'temp.jpg',8,NULL,'\\attachment\\api\\20170930\\44f2faac461c4fdb818e213742fb97ef.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-30 17:02:38'),(35,'temp.jpg',8,NULL,'/attachment/api/20170930/e97dac3644af4790b1f2d46b054046bd.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-30 17:07:49'),(36,'temp.jpg',8,NULL,'/attachment/api/20170930/91b4c5dca4fb41cf9017e8d7eee19a60.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-30 17:08:56'),(37,'temp.jpg',8,NULL,'/attachment/api/20170930/805e3a527dc146fa938bd6e99d397f15.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-30 17:09:03'),(38,'temp.jpg',8,NULL,'/attachment/api/20170930/a1a53a8ea9ad46fb808f97405bd5c09f.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-09-30 17:16:08'),(39,'temp.jpg',8,NULL,'/attachment/api/20171004/6cd9a8cb23414db5b739e223f02162ef.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-10-04 16:35:55'),(40,'temp.jpg',8,NULL,'/attachment/api/20171004/7b098a0ba0984f1d86edc483a0979589.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-10-04 16:39:33'),(41,'temp.jpg',8,NULL,'/attachment/api/20171004/164070d0f56b49dfbf5543a197b3a0d8.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-10-04 16:48:26'),(42,'temp.jpg',8,NULL,'/attachment/api/20171004/fe827cd170644cc99db8e086a5c367cb.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-10-04 17:24:38'),(43,'temp.jpg',8,NULL,'/attachment/api/20171005/d47f3d7622bf4ad083f332ca2b93b188.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-10-05 15:00:34'),(44,'temp.jpg',8,NULL,'/attachment/api/20171005/d25f69463f9246efb7723c67fed3c3b4.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-10-05 15:13:14'),(45,'temp.jpg',8,NULL,'/attachment/api/20171005/4b51bfaf56f24f41a15335ec0953d3d1.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-10-05 15:25:29'),(46,'temp.jpg',8,NULL,'/attachment/api/20171005/3981515c11324739b5042832b4256e00.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-10-05 15:41:10'),(47,'temp.jpg',8,NULL,'/attachment/api/20171005/74212de8349548328d372008fda453ce.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-10-05 15:41:51'),(48,'temp.jpg',8,NULL,'/attachment/api/20171005/41b5dae3f79f4b8992886fa654e134c9.jpg','image/jpeg','.jpg',NULL,NULL,NULL,NULL,NULL,'2017-10-05 15:42:42'),(49,'temp.jpeg',8,NULL,'/attachment/api/20171005/c2f738b1c0a246b1b701783727127f6f.jpeg','image/jpeg','.jpeg',NULL,NULL,NULL,NULL,NULL,'2017-10-05 16:39:20'),(50,'temp.jpeg',8,NULL,'/attachment/api/20171005/a7f0ace0f2074a96979837f5ff19f38e.jpeg','image/jpeg','.jpeg',NULL,NULL,NULL,NULL,NULL,'2017-10-05 16:43:13'),(51,'temp.mp4',8,NULL,'/attachment/api/20171005/3bfb4bf7ba7f476a8ba169e509c72ad4.mp4','video/mpeg4','.mp4',NULL,NULL,NULL,NULL,NULL,'2017-10-05 17:16:19'),(52,'temp.jpeg',3,NULL,'/attachment/api/20171005/f3eb7f0c43b940c5a91cb2a7220ca204.jpeg','image/jpeg','.jpeg',NULL,NULL,NULL,NULL,NULL,'2017-10-05 19:24:59'),(53,'temp.jpeg',3,NULL,'/attachment/api/20171005/c414c87f4bf943c98d9d81da15644761.jpeg','image/jpeg','.jpeg',NULL,NULL,NULL,NULL,NULL,'2017-10-05 19:24:59'),(54,'temp.mp4',3,NULL,'/attachment/api/20171012/d071901152d146ea827c6da137850893.mp4','video/mpeg4','.mp4',NULL,NULL,NULL,NULL,NULL,'2017-10-12 18:00:46'),(55,'temp.jpeg',9,NULL,'/attachment/api/20171013/79903b39af2a4787b47aa2e3fe0fed08.jpeg','image/jpeg','.jpeg',NULL,NULL,NULL,NULL,NULL,'2017-10-13 19:58:29'),(56,'temp.mp4',9,NULL,'/attachment/api/20171014/7ad1684d45df4e9dbffdd52a65097bf1.mp4','video/mpeg4','.mp4',NULL,NULL,NULL,NULL,NULL,'2017-10-14 11:16:01'),(57,'temp.jpeg',10,NULL,'/attachment/api/20171016/d367952c588e4fd3b8395c80048d880e.jpeg','image/jpeg','.jpeg',NULL,NULL,NULL,NULL,NULL,'2017-10-16 09:43:23'),(58,'temp.jpeg',10,NULL,'/attachment/api/20171016/6a42d0581ba04e51acdc258b269d46ee.jpeg','image/jpeg','.jpeg',NULL,NULL,NULL,NULL,NULL,'2017-10-16 09:43:38'),(59,'temp.jpeg',9,NULL,'/attachment/api/20171016/69c6ee34a19048c7a71fa5961d405e99.jpeg','image/jpeg','.jpeg',NULL,NULL,NULL,NULL,NULL,'2017-10-16 09:46:04'),(60,'temp.jpeg',9,NULL,'/attachment/api/20171016/f9dd43714267481a9d70c290ddb3b80b.jpeg','image/jpeg','.jpeg',NULL,NULL,NULL,NULL,NULL,'2017-10-16 09:46:14'),(61,'temp.jpeg',9,NULL,'/attachment/api/20171016/8d0b031bb2e34b23b5384025aac59e4b.jpeg','image/jpeg','.jpeg',NULL,NULL,NULL,NULL,NULL,'2017-10-16 10:32:32'),(62,'temp.jpeg',9,NULL,'/attachment/api/20171016/041a71c7f21f45f285cf6a2e42ccc6ef.jpeg','image/jpeg','.jpeg',NULL,NULL,NULL,NULL,NULL,'2017-10-16 10:32:37'),(63,'temp.jpeg',9,NULL,'/attachment/api/20171016/3e8b060e673845adae8127ec4cf328d5.jpeg','image/jpeg','.jpeg',NULL,NULL,NULL,NULL,NULL,'2017-10-16 11:55:13'),(64,'temp.jpeg',9,NULL,'/attachment/api/20171016/e8546ab3dec5470da342583deccdaf0d.jpeg','image/jpeg','.jpeg',NULL,NULL,NULL,NULL,NULL,'2017-10-16 11:55:21'),(65,'temp.jpeg',9,NULL,'/attachment/api/20171016/baa9c736265d48d39e8681288ba5b490.jpeg','image/jpeg','.jpeg',NULL,NULL,NULL,NULL,NULL,'2017-10-16 12:26:18'),(66,'temp.mp4',3,NULL,'/attachment/api/20171202/5d51af9c753e48269324f168fb5646ec.mp4','video/mpeg4','.mp4',NULL,NULL,NULL,NULL,NULL,'2017-12-02 16:52:53');
/*!40000 ALTER TABLE `y_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_captcha`
--

DROP TABLE IF EXISTS `y_captcha`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_captcha` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `mobile` varchar(20) CHARACTER SET latin1 NOT NULL,
  `code` varchar(20) CHARACTER SET latin1 NOT NULL,
  `type` varchar(45) DEFAULT '',
  `ip` varchar(50) NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COMMENT='验证码表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_captcha`
--

LOCK TABLES `y_captcha` WRITE;
/*!40000 ALTER TABLE `y_captcha` DISABLE KEYS */;
INSERT INTO `y_captcha` VALUES (2,'18968596872','571377','','0:0:0:0:0:0:0:1','2017-08-25 21:56:26'),(3,'18968596872','432701','','127.0.0.1','2017-08-25 21:56:44'),(4,'15068633525','658541','','60.12.168.170','2017-09-10 11:35:58'),(5,'18968596872','593671','','60.12.168.170','2017-09-10 11:36:23'),(6,'18968596872','392443','','60.12.168.170','2017-09-10 11:42:49'),(7,'18968596872','568335','','60.12.168.170','2017-09-10 11:45:40'),(8,'15068633525','764322','','60.12.168.170','2017-09-10 11:46:02'),(9,'18968596872','528637','','60.12.168.170','2017-09-10 11:57:29'),(10,'18968596872','48878','','60.12.168.170','2017-09-10 13:13:46'),(11,'18968596872','766810','reg','60.12.168.170','2017-09-10 13:55:59'),(12,'18968596872','380970','reg','60.12.168.170','2017-09-10 14:00:40'),(13,'18968596872','844022','reg','60.12.168.170','2017-09-10 14:00:55'),(14,'15068633525','748210','reg','183.148.71.188','2017-09-21 23:06:48'),(15,'15068633525','462206','reg','183.148.75.101','2017-09-23 23:18:05'),(16,'15068633525','929458','findPwd','183.148.75.101','2017-09-24 01:00:57'),(17,'15068633525','548177','reg','183.148.75.101','2017-09-24 11:19:21'),(18,'15068633525','531578','findPwd','183.148.75.101','2017-09-24 17:03:22'),(19,'15068633525','800676','findPwd','183.148.75.101','2017-09-24 17:06:28'),(20,'15068633525','838732','findPwd','112.17.235.161','2017-09-24 17:11:05'),(21,'15068633525','832241','reg','60.12.168.170','2017-09-29 09:52:03'),(22,'15068633525','875506','chgMobile','115.203.198.239','2017-10-02 10:27:57'),(23,'15068633525','774218','chgMobile','115.203.198.239','2017-10-02 10:41:30'),(24,'15068633525','706051','chgMobile','115.203.198.239','2017-10-02 10:49:00'),(25,'15068633525','269625','chgMobile','115.203.198.239','2017-10-02 12:01:16'),(26,'13857665677','519456','reg','223.104.6.59','2017-10-13 19:25:34'),(27,'13566444288','046871','reg','112.17.238.138','2017-10-16 09:39:53'),(28,'13819657511','481940','reg','112.17.237.213','2017-10-31 17:01:45'),(29,'13819657511','774344','reg','112.17.237.213','2017-10-31 17:11:33'),(30,'15068633525','276346','findPwd','60.12.168.170','2017-11-23 15:36:40'),(31,'13857665677','242079','findPwd','60.162.166.84','2017-11-24 12:18:15');
/*!40000 ALTER TABLE `y_captcha` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_comment`
--

DROP TABLE IF EXISTS `y_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_comment` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` bigint(20) unsigned DEFAULT NULL COMMENT '回复的评论ID',
  `content_id` bigint(20) unsigned DEFAULT NULL COMMENT '评论的内容ID',
  `content_module` varchar(32) DEFAULT NULL COMMENT '评论的内容模型',
  `comment_count` int(11) unsigned DEFAULT '0' COMMENT '评论的回复数量',
  `order_number` int(11) unsigned DEFAULT '0' COMMENT '排序编号，常用语置顶等',
  `user_id` bigint(20) unsigned DEFAULT NULL COMMENT '评论的用户ID',
  `ip` varchar(64) DEFAULT NULL COMMENT '评论的IP地址',
  `author` varchar(128) DEFAULT NULL COMMENT '评论的作者',
  `type` varchar(32) DEFAULT 'comment' COMMENT '评论的类型，默认是comment',
  `text` longtext COMMENT '评论的内容',
  `agent` text COMMENT '提交评论的浏览器信息',
  `created` datetime DEFAULT NULL COMMENT '评论的时间',
  `slug` varchar(128) DEFAULT NULL COMMENT '评论的slug',
  `email` varchar(64) DEFAULT NULL COMMENT '评论用户的email',
  `status` varchar(32) DEFAULT NULL COMMENT '评论的状态',
  `vote_up` int(11) unsigned DEFAULT '0' COMMENT '“顶”的数量',
  `vote_down` int(11) unsigned DEFAULT '0' COMMENT '“踩”的数量',
  `flag` varchar(256) DEFAULT NULL COMMENT '标识',
  `lat` decimal(20,16) DEFAULT NULL COMMENT '纬度',
  `lng` decimal(20,16) DEFAULT NULL COMMENT '经度',
  PRIMARY KEY (`id`),
  UNIQUE KEY `slug` (`slug`),
  KEY `content_id` (`content_id`),
  KEY `user_id` (`user_id`),
  KEY `email` (`email`),
  KEY `created` (`created`),
  KEY `parent_id` (`parent_id`),
  KEY `content_module` (`content_module`),
  KEY `comment_count` (`comment_count`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COMMENT='评论表，用于保存content内容的回复、分享、推荐等信息。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_comment`
--

LOCK TABLES `y_comment` WRITE;
/*!40000 ALTER TABLE `y_comment` DISABLE KEYS */;
INSERT INTO `y_comment` VALUES (9,NULL,21,'article',0,0,1,'0:0:0:0:0:0:0:1','创始人','comment','测试','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36','2017-11-28 09:12:54',NULL,NULL,'delete',0,0,NULL,NULL,NULL),(10,NULL,20,'article',0,0,1,'0:0:0:0:0:0:0:1','创始人','comment','测试','Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36','2017-11-28 09:19:52',NULL,NULL,'delete',0,0,NULL,NULL,NULL),(11,NULL,10,'article',0,0,1,'0:0:0:0:0:0:0:1','创始人','comment','okokok ok','Mozilla/5.0 (X11; Linux x86_64; rv:57.0) Gecko/20100101 Firefox/57.0','2017-11-28 22:28:20',NULL,NULL,'delete',0,0,NULL,NULL,NULL),(12,NULL,10,'article',0,0,1,'0:0:0:0:0:0:0:1','创始人','comment','okokok ok ffff','Mozilla/5.0 (X11; Linux x86_64; rv:57.0) Gecko/20100101 Firefox/57.0','2017-11-28 22:28:45',NULL,NULL,'delete',0,0,NULL,NULL,NULL),(13,NULL,10,'article',0,0,1,'0:0:0:0:0:0:0:1','创始人','comment','下载客户端--&gt;点击首页提示注册登录--&gt;身份验证','Mozilla/5.0 (X11; Linux x86_64; rv:57.0) Gecko/20100101 Firefox/57.0','2017-11-28 22:28:57',NULL,NULL,'normal',0,0,NULL,NULL,NULL),(14,NULL,10,'article',0,0,1,'0:0:0:0:0:0:0:1','创始人','comment','下载客户端--&gt;点击首页提示注册登录--&gt;身份验证','Mozilla/5.0 (X11; Linux x86_64; rv:57.0) Gecko/20100101 Firefox/57.0','2017-11-28 22:30:29',NULL,NULL,'normal',0,0,NULL,NULL,NULL),(15,NULL,10,'article',0,0,1,'0:0:0:0:0:0:0:1','创始人','comment','下载客户端--&gt;点击首页提示注册登录--&gt;身份验证','Mozilla/5.0 (X11; Linux x86_64; rv:57.0) Gecko/20100101 Firefox/57.0','2017-11-28 22:31:54',NULL,NULL,'normal',0,0,NULL,NULL,NULL),(16,NULL,10,'article',0,0,1,'0:0:0:0:0:0:0:1','创始人','comment','下载客户端--&gt;点击首页提示注册登录--&gt;身份验证','Mozilla/5.0 (X11; Linux x86_64; rv:57.0) Gecko/20100101 Firefox/57.0','2017-11-28 22:32:32',NULL,NULL,'normal',0,0,NULL,NULL,NULL),(17,NULL,10,'article',0,0,1,'0:0:0:0:0:0:0:1','创始人','comment','下载客户端--&gt;点击首页提示注册登录--&gt;身份验证','Mozilla/5.0 (X11; Linux x86_64; rv:57.0) Gecko/20100101 Firefox/57.0','2017-11-28 22:34:34',NULL,NULL,'normal',0,0,NULL,NULL,NULL),(18,NULL,10,'article',0,0,1,'0:0:0:0:0:0:0:1','创始人','comment','下载客户端--&gt;点击首页提示注册登录--&gt;身份验证aa','Mozilla/5.0 (X11; Linux x86_64; rv:57.0) Gecko/20100101 Firefox/57.0','2017-11-28 22:34:41',NULL,NULL,'normal',0,0,NULL,NULL,NULL),(19,NULL,10,'article',0,0,1,'0:0:0:0:0:0:0:1','创始人','comment','aa','Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Mobile Safari/537.36','2017-11-29 09:31:14',NULL,NULL,'delete',0,0,NULL,NULL,NULL),(20,NULL,10,'article',0,0,1,'0:0:0:0:0:0:0:1','创始人','comment','测试','Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Mobile Safari/537.36','2017-11-29 09:41:27',NULL,NULL,'delete',0,0,NULL,NULL,NULL),(21,NULL,10,'article',0,0,2,'0:0:0:0:0:0:0:1','365借条','comment','测试','Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Mobile Safari/537.36','2017-11-29 09:41:37',NULL,NULL,'delete',0,0,NULL,NULL,NULL),(22,NULL,10,'article',0,0,1,'0:0:0:0:0:0:0:1','创始人','comment','测试2','Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Mobile Safari/537.36','2017-11-29 10:03:57',NULL,NULL,'delete',0,0,NULL,NULL,NULL),(23,NULL,10,'article',1,0,2,'0:0:0:0:0:0:0:1','365借条','comment','ddd','Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Mobile Safari/537.36','2017-11-29 10:04:07',NULL,NULL,'delete',0,0,NULL,NULL,NULL),(25,23,10,'article',0,0,1,'39.181.204.225','创始人','comment','<p>aaa</p>','Mozilla/5.0 (X11; Linux x86_64; rv:57.0) Gecko/20100101 Firefox/57.0','2017-12-06 21:54:25',NULL,'6215714@qq.com','delete',0,0,NULL,NULL,NULL);
/*!40000 ALTER TABLE `y_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_content`
--

DROP TABLE IF EXISTS `y_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_content` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` text COMMENT '标题',
  `text` longtext COMMENT '内容',
  `summary` text COMMENT '摘要',
  `link_to` varchar(256) DEFAULT NULL COMMENT '连接到(常用于谋文章只是一个连接)',
  `markdown_enable` tinyint(1) DEFAULT '0' COMMENT '是否启用markdown',
  `thumbnail` varchar(128) DEFAULT NULL COMMENT '缩略图',
  `module` varchar(32) DEFAULT NULL COMMENT '模型',
  `style` varchar(32) DEFAULT NULL COMMENT '样式',
  `user_id` bigint(20) unsigned DEFAULT NULL COMMENT '用户ID',
  `author` varchar(128) DEFAULT NULL COMMENT '匿名稿的用户',
  `user_email` varchar(128) DEFAULT NULL COMMENT '匿名稿的邮箱',
  `user_ip` varchar(128) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` text COMMENT '发布浏览agent',
  `parent_id` bigint(20) unsigned DEFAULT NULL COMMENT '父级内容ID',
  `object_id` bigint(20) unsigned DEFAULT NULL COMMENT '关联的对象ID',
  `order_number` int(11) unsigned DEFAULT '0' COMMENT '排序编号',
  `status` varchar(32) DEFAULT NULL COMMENT '状态',
  `vote_up` int(11) unsigned DEFAULT '0' COMMENT '支持人数',
  `vote_down` int(11) unsigned DEFAULT '0' COMMENT '反对人数',
  `rate` int(11) DEFAULT NULL COMMENT '评分分数',
  `rate_count` int(10) unsigned DEFAULT '0' COMMENT '评分次数',
  `price` decimal(10,2) DEFAULT '0.00' COMMENT '价格',
  `comment_status` varchar(32) DEFAULT NULL COMMENT '评论状态',
  `comment_count` int(11) unsigned DEFAULT '0' COMMENT '评论总数',
  `comment_time` datetime DEFAULT NULL COMMENT '最后评论时间',
  `view_count` int(11) unsigned DEFAULT '0' COMMENT '访问量',
  `created` datetime DEFAULT NULL COMMENT '创建日期',
  `modified` datetime DEFAULT NULL COMMENT '最后更新日期',
  `slug` varchar(128) DEFAULT NULL COMMENT 'slug',
  `flag` varchar(256) DEFAULT NULL COMMENT '标识',
  `lng` decimal(20,16) DEFAULT NULL COMMENT '经度',
  `lat` decimal(20,16) DEFAULT NULL COMMENT '纬度',
  `meta_keywords` varchar(256) DEFAULT NULL COMMENT 'SEO关键字',
  `meta_description` varchar(256) DEFAULT NULL COMMENT 'SEO描述信息',
  `remarks` text COMMENT '备注信息',
  `valuable` tinyint(3) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `slug` (`slug`),
  KEY `user_id` (`user_id`),
  KEY `parent_id` (`parent_id`),
  KEY `content_module` (`module`),
  KEY `created` (`created`),
  KEY `vote_down` (`vote_down`),
  KEY `vote_up` (`vote_up`),
  KEY `view_count` (`view_count`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COMMENT='内容表，用于存放比如文章、帖子、商品、问答等用户自定义模型内容。也用来存放比如菜单、购物车、消费记录等系统模型。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_content`
--

LOCK TABLES `y_content` WRITE;
/*!40000 ALTER TABLE `y_content` DISABLE KEYS */;
INSERT INTO `y_content` VALUES (1,'医生专培制度需要在试点中不断完善','<div class=\"daoyu\"> \n <p id=\"shareintro\">6月12日，酝酿多时的专科医师规范化培训有了新进展，中国医师协会公布了3个试点专科（神经外科、呼吸与危重病医学和心血管病学）的培训基地。不过，这一通知却招致医生群体的如潮恶评，“劝人学医，天打雷劈”“不孝有三，学医为大”“美国的培训标准、非洲的待遇标准”等等不一而足，而其中一条直接怒怼卫生部门的留言获得了两万多人的点赞。专培制度究竟是什么，为什么难以说服医生群体，又会带来怎样的影响？</p> \n</div> \n<div class=\"focus\"> \n <div class=\"tit\"> \n  <h3>要点速读</h3> \n </div> \n <div class=\"focus_list\"> \n  <ul> \n   <li class=\"inline-block\" data-text=\"“5+3+X”的培训制度是苏联模式向美国模式的转型，主要目的在于提升中国医生的同质化水平。\">1<a>“5+3+X”的培训制度是苏联模式向美国模式的转型，主要目的在于提升中国医生的同质化水平。</a></li> \n   <li class=\"inline-block\" data-text=\"虽然制度上学习美国，但配套措施并未跟上，时间成本大、待遇低，加重了医学生群体的焦虑感。廉价劳动力的身份，缺乏实质的培训内容，更让人看不到希望。\">2<a>虽然制度上学习美国，但配套措施并未跟上，时间成本大、待遇低，加重了医学生群体的焦虑感。廉价劳动力的身份，缺乏实质的培训内容，更让人看不到希望。</a></li> \n   <li class=\"inline-block\" data-text=\"部门之间应该相互协调，在试点中不断完善医生专培制度。\">3<a>部门之间应该相互协调，在试点中不断完善医生专培制度。</a></li> \n  </ul> \n </div> \n</div> \n<div id=\"articleContent\" class=\"article\"> \n <h2 data-page-model=\"title\">“5+3+X”的培训模式是跟美国接轨，目的在于实现医生的规范化和同质化</h2> \n <p data-page-model=\"text\">所谓专培，完整的说法是专科医师规范化培训，指的是医学院毕业的学生（本科一般是5年），在完成3年的住院医师培训之后，还需要继续2—4年的专科培训，培训通过才能成为正式的执业医生，所以这样的培训模式也被概括成“5+3+X”。</p> \n <p data-page-model=\"text\">虽然尚在试点阶段，官方的声明也表示并非强制，但是专培制度背后附带的各种隐性福利（比如优先评选职称），还是让大多数医学生拼了命也要往这座独木桥上挤。</p> \n <p data-page-model=\"text\">“5+3+X”的培训模式在中国并非一开始就有。最初中国的医学教育培训学习的是苏联模式，医学生毕业后直接进入到各大医院跟着自己的专科“师父”边学习边实操。后来这样的模式出现弊端（由于从一开始就专注于某一科，当遇到情况复杂的病人就难以及时判断和处理），转而学习美国，医学毕业生先要住院培训，到各个科室轮转，熟悉情况——以外科为例，3年的住院培训通常会在普外科、胸外科和骨科等科室轮转。专培就是在此基础之上的升级版，医学生对自己的专业进行更深入的学习，累积临床经验。比较来看，住院医师培训对应的是美国的Residency（住院医生实习期），而专科医师培训对应的则是Fellowship（专科住院医生）。因而，也有人总结，整个规培就是给医生再来一次“高考”。</p> \n <div class=\"tuzhu nofloat\" data-page-model=\"image\">\n  <img title=\"中国的医生专培制度主要效仿的是美国\" src=\"http://img1.gtimg.com/ninja/1/2017/06/ninja149765943965930.jpg\" alt=\"中国的医生专培制度主要效仿的是美国\">中国的医生专培制度主要效仿的是美国\n </div> \n <p data-page-model=\"text\">这样做的好处显而易见。一方面可以提升医生群体的水平，推动医学精英教育。另一方面更为重要，可以实现医生的同质化，这样将来在小医院也能享受到相当水平的医护服务。</p> \n <h2 data-page-model=\"title\">但是配套措施却脱了轨：增加的时间成本和较低的待遇让有志从医的学生感到焦虑</h2> \n <p data-page-model=\"text\">制度是好制度，美国的经验也表明确实行之有效，但是照搬到中国来后，却给人东施效颦的感觉，最直接的落差体现在两个方面：</p> \n <p data-page-model=\"text\"><strong>其一，增加的时间成本让人感到绝望，一眼忘不到头</strong></p> \n <p data-page-model=\"text\">专培试点的消息甫一出来，很多人就产生了“三年之后又是三年”的绝望感，有人简单算过一笔账，如果以18岁上大学来算，按照“5+3+X”的模式，至少接近30岁时才能开始自己的从医之路，这还不算那些上学比较晚，以及为了提高竞争力，拼命攻读硕博学位的人。网络上流传的一个段子很能体现这样的绝望感：</p> \n <p data-page-model=\"text\">王小二，生而聪颖……18入学医科，23取学士归，26谋硕士，29谋博士，而立之年学业终毕，规培专培7载，37方得行医之职，发如雪白，薪比尿淡，租居斗室，四壁萧然，父母不得养，无资谋妻儿……</p> \n <p data-page-model=\"text\">当然，对于有志学医的人来说，长时间的培训和磨练不算什么，毕竟这是一开始选择从医这条路就必须面临的考验，况且，如果比较来看的话，美国培训的时间长度和辛苦程度只增不减。这就要说到第二个问题。</p> \n <p data-page-model=\"text\"><strong>其二，与工作辛苦程度极不匹配的待遇让人自惭形秽</strong></p> \n <p data-page-model=\"text\">专培期间并非完全没有收入，根据国家规定，每个参加培训的人每年会有3万元的中央财政补贴。看上去还不错，但要注意，这3万块钱并非全部都能到医学生手上，其中1万要交给接受培训的机构，所以，算下来每人每个月也就只能拿到一千六。按理说，地方财政也会提供相应的支持，但是对很多人说，地方支持只是画出来的大饼，看得到吃不到。这到手的一千六，根本养不活自己，最后还得回家啃老。在被问到专培待遇问题时，中日友好医院院长、中国工程院院士王辰也直言不讳：“即使中央财政足额下发到学院手中，再加上地方财政补助，比起国际的通行标准依然是不够的。”</p> \n <p data-page-model=\"text\">没有比较，就没有“伤害”，国际通行的标准是什么呢？以美国为例，据一位在美学习的中国医生描述，美国住院医师培训期间的税前平均收入大约有5万美元（约合32.9万元人民币）。即便这样，他们还在抱怨收入太少，“时薪和收银员差不多”。</p> \n <p data-page-model=\"text\">总之制度是接轨了，但工资还脱轨十万八千里。培训比照的是美国的标准，待遇却是按照非洲来的。</p> \n <div class=\"tuzhu nofloat\" data-page-model=\"image\">\n  <img title=\"“急诊夜鹰”对专培的评价代表了很多医生的态度\" src=\"http://img1.gtimg.com/ninja/1/2017/06/ninja149765980544093.png\" alt=\"“急诊夜鹰”对专培的评价代表了很多医生的态度\">“急诊夜鹰”对专培的评价代表了很多医生的态度\n </div> \n <h2 data-page-model=\"title\">更重要的是，在培训过程中，医学生恐怕充当的是“廉价劳动力”，没有真正实现培训的目的</h2> \n <p data-page-model=\"text\">要是真能在培训期间夯实基础，真正学习到有用的知识，时间久点、待遇差点也不是什么大问题，咬咬牙也能坚持下去。但实际情况是很多医学生充当的是廉价劳动力，到底能学到多少货真价实的知识，值得怀疑。专培制度尚在试点，具体如何实施有待观察，但是我们可以从已经实施一段时间的住院培训制度窥得一二。</p> \n <p data-page-model=\"text\">有外科医生以自己的亲身经验为例，反映住院培训制度中最大的障碍就在于没有规范的外科手术培训计划，培训期间基本上是二助甚至三助。有人可能会说，能当二助已经很不错了，可以多看多学习。但是要知道，在一台手术中，真正起作用的是主刀和一助，“二助三助都是在打酱油。只有拉钩的份，看的多，动手少，给你关个腹就像赏赐你一样”。对于必须亲自实操才能积累技能的外科手术，光用眼睛看是学不会的。</p> \n <p data-page-model=\"text\">但是美国却不是这样，他们特别强调培训医生的实操能力，同样以外科系统为例，培训医生必须以主刀或者一助的身份完成规定的手术量。比如这个月要在普外科做主刀做5例胃大切手术，5例甲状腺部分切除术，以一助做10例胃癌根治术。只有这样子，“美国医生 3 年培训下来，才敢拍着胸脯说，老子能把这手术干下来！”。</p> \n <p data-page-model=\"text\">除此之外，在日常的查房、学术研讨和病例整理等细节中，美国的培训医院医生也会对参加培训的有着严格的要求。</p> \n <p data-page-model=\"text\">住院培训制度是整个“5+3+X”培训计划中先行的中间环节，培训过程还存在这么多槽点，让人不得不对专培制度接下来实施的效果产生担忧。</p> \n <div class=\"tuzhu nofloat\" data-page-model=\"image\">\n  <img title=\"规培过程辛苦不说，还可能学不到实质内容\" src=\"http://img1.gtimg.com/ninja/1/2017/06/ninja149766658226712.jpg\" alt=\"规培过程辛苦不说，还可能学不到实质内容\">规培过程辛苦不说，还可能学不到实质内容\n </div> \n <p data-page-model=\"text\">对比中美医生培训的内容不难发现，中国的培训制度并没有让学生学到精髓，配套措施跟不上，是一种“既要马儿跑，又要马儿不吃草”的不科学的培训方法。</p> \n <p data-page-model=\"text\">当然反对专培的声音中也不乏一些医疗技术水平不高，只想占便宜，不想付出努力的傲娇的医学生，这些人就应该通过各种培训实行退出机制。不过，从很多评论来看，培训制度的华而不实很有可能伤害真正有志于从医的学生的积极性，而这样的积极性的打击而最终也可能会影响到整个社会医护资源的供给。</p> \n <h2 data-page-model=\"title\">专培制度要想真正发挥作用需要在试点中不断完善</h2> \n <p data-page-model=\"text\">这次专培试点的通知公布之后，卫计委首当其冲遭到了抨击，作为主管医疗卫生的政府部门，回应批评和质疑是卫计委的分内之事。不过平心而论，要卫计委一家独立应对这个问题有点力不从心，因为整个规培制度牵涉到的部门太多太多：规培医生待遇低，这是财政部的事情，发多发少他们来定；涉及到社会保障体系，需要人社部进行协调；药价和器械价格则是发改委价格司的事；高校医学生的教育则是教育部主管。所以，要真正实现规培制度的完善，需要有一个能够协调各大部委的权威机构。完善也不是说说空话而已，毕竟马上就到了高考填报志愿的时候了。</p> \n</div> \n<div class=\"summary\"> \n <div class=\"summary_tit\">\n  &nbsp;\n </div> \n <div class=\"summary_text\"> \n  <p>打造规范化、制度化的医学精英教育没有错，但也要考虑配套措施是否跟得上，别理念先行，措施落后一大截。</p> \n </div> \n</div>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'delete',0,0,NULL,0,0.00,NULL,0,NULL,1,'2017-06-17 16:01:06','2017-06-17 16:01:06','医生专培制度需要在试点中不断完善',NULL,NULL,NULL,NULL,NULL,NULL,0),(2,'放过“彭宇案”，别再为自己的软弱找借口','<div class=\"daoyu\"> \n <p id=\"shareintro\">6月15日，最高法官微转发了人民法院报的文章《十年前彭宇案的真相是什么？》。文章认为是媒体的误读和人性的自私，使得彭宇案成为个体冷漠、逃避救助行为的借口。彭宇当年撞老太一案法律上早有定论，但为什么十年后，依然有人把它当成一个“好人被讹”的标志性事件？</p> \n</div> \n<div class=\"focus\"> \n <div class=\"tit\"> \n  <h3>要点速读</h3> \n </div> \n <div class=\"focus_list\"> \n  <ul> \n   <li class=\"inline-block\" data-text=\"“彭宇案”一审判决不恰当的说理方式使得该案被扣上了“引发道德滑坡”的大帽子，也把彭宇塑造成了“好人蒙冤”的典型。\">1<a>“彭宇案”一审判决不恰当的说理方式使得该案被扣上了“引发道德滑坡”的大帽子，也把彭宇塑造成了“好人蒙冤”的典型。</a></li> \n   <li class=\"inline-block\" data-text=\"“好人蒙冤”式的传播本身根植人心，人心本就倾向于相信恶，和夸大风险。\">2<a>“好人蒙冤”式的传播本身根植人心，人心本就倾向于相信恶，和夸大风险。</a></li> \n   <li class=\"inline-block\" data-text=\"别再拿“彭宇案”当借口，直面自己的软弱也是一种勇敢。\">3<a>别再拿“彭宇案”当借口，直面自己的软弱也是一种勇敢。</a></li> \n  </ul> \n </div> \n</div> \n<div id=\"articleContent\" class=\"article\"> \n <h2 data-page-model=\"title\">“彭宇案”确实已经成为一个坚挺的符号</h2> \n <p data-page-model=\"text\">南京\"彭宇案\"，是2006年末发生于南京市的一起引发极大争议的民事诉讼案。在法律程序和法律事实上，该案早已了结，官方也曾给出说明，南京市委常委、市政法委书记刘志伟2012年在接受《瞭望》新闻周刊采访时称：“舆论和公众认知的‘彭宇案’并非事实真相。由于多重因素被误读和放大的这起普通民事案件，不应成为社会‘道德滑坡’的标志性事件”。</p> \n <div class=\"tuzhu nofloat\" data-page-model=\"image\">\n  <img title=\"驻马店女子遭二次碾压现场\" src=\"http://img1.gtimg.com/ninja/1/2017/06/ninja149756951412521.jpeg\" alt=\"驻马店女子遭二次碾压现场\">驻马店女子遭二次碾压现场\n </div> \n <p data-page-model=\"text\">彭宇确实撞了人，“彭宇案”中不存在“好人蒙难”的情节，是经过证据检验的法律事实；一个社会的“道德滑坡”不可能只是因为一次法院判决，是通过常识可以做出的判断。但这不妨碍每逢关于世风的大讨论，彭宇就成了“好人难做”的符号，“彭宇案”是法律伤害、败坏道德的例证。远有小悦悦案，近有河南驻马店女子被汽车两次碾压无人上前施救的案例。而在与后者相关的微博评论区，最常见的评论是：“自从南京法官因为一个‘不是你撞的人，你干嘛去扶’这样一个理由判罚，全国风气已经败坏到了极点！”</p> \n <p data-page-model=\"text\">“彭宇案”的符号意义已经成为既定事实，这个漏洞百出的符号为什么如此坚挺？</p> \n <h2 data-page-model=\"title\">“不是你撞的你为什么要扶？”一审判决书糟糕的说理掩盖了最终确认的法律事实，也成为许多人忽视事实的借口</h2> \n <p data-page-model=\"text\">“不是你撞的你为什么要扶？”，十年过去了，公众早已不记得或者根本不清楚“彭宇案”判决书的具体细节，但却不会忘记这个简短有力，看似无赖的经典反问句。</p> \n <p data-page-model=\"text\">反问句是网友加工的成果，原话出自“彭宇案”的一审判决书：“从常理分析，其与原告相撞的可能性较大。如果被告是见义勇为做好事，更符合实际的做法应是抓住撞倒原告的人，而不仅仅是好心相扶；如果被告是做好事，根据社会情理，在原告的家人到达后，其完全可以在言明事实经过并让原告的家人将原告送往医院，然后自行离开，但被告未作此等选择，其行为显然与情理相悖。”</p> \n <p data-page-model=\"text\">这段判词在当年受到很多批评，主审法院王浩也因此被舆论批得狗血淋头。</p> \n <p data-page-model=\"text\">法官用“假设”推理到底有没有问题？民事诉讼中实行“高度盖然性占优势”的证明标准，所谓的盖然性即是可能性，在证据对某一事实的证明无法达到事实清楚、证据确实充分的情况下，法官可以根据当事人的陈述，相关证人陈述以及间接证据等，对盖然性较高的事实予以确认，所以一审判决书用“假设”推理并无不当。</p> \n <p data-page-model=\"text\">唯一的问题是，主审法官的“从常理分析”在逻辑上不够严密，不具有唯一性，毕竟这个世界还是存在“见义勇为”这种义举的。正如最高人民法院民一庭负责人分析：“彭宇案为何会引起争论？一个重要的原因就是一审判决没有正确理解和运用生活经验推理。”这也是当时许多法律专业人士、媒体人曾经撰文狠狠批评过的。</p> \n <div class=\"tuzhu nofloat\" data-page-model=\"image\">\n  <img title=\"有人用这幅漫画来形容“彭宇案”的道德杀伤力\" src=\"http://img1.gtimg.com/ninja/1/2017/06/ninja149757096245912.jpeg\" alt=\"有人用这幅漫画来形容“彭宇案”的道德杀伤力\">有人用这幅漫画来形容“彭宇案”的道德杀伤力\n </div> \n <p data-page-model=\"text\">但反过头来看，“高度盖然性占优势”的证明标准，本身就无法脱离法官的个人生活经验。彭宇案的一审主审法官以“人性恶”的个人经验判断作为社会一般经验判断，并不完全是荒唐可笑的，甚至具有很大必然性，尤其符合生活的逻辑。“人性恶”理论是现代一系列制度设计的基石。而对于许多一线司法人员来说，“人性恶”也更符合他们对人性的认知。</p> \n <p data-page-model=\"text\">“不是你撞的你为什么要扶”，讨论的重点就从“彭宇到底有没有撞人”变成了“会不会有人不计代价、不问回报的去做好事”。</p> \n <h2 data-page-model=\"title\">“彭宇案”的错误传播也与人性契合：人更容易相信恶，但人总是过高地估计自己的道德水准</h2> \n <p data-page-model=\"text\">与其说“彭宇案”产生的负面效应，严重影响公众心态，不如说是严重迎合了公众的想象。“好人蒙冤”“好人得不到好报”是经典的戏剧母题，非如此不足以引起关注。当年媒体报道失衡，一边倒地相信彭宇，以耸动的信息挑逗公众的情绪，是需要检讨的。</p> \n <p data-page-model=\"text\">而公众的心理常常是矛盾的。一方面他们觉得“不是你撞的你为什么要扶”是一种冒犯，原因是这句话假定所有人做好事只因功利，而非无私。这时候他们对人性，对自己都充满了美好的想象。但另一方面，当身处要不要扶老人的情境时，他们又会假定所有跌倒的老人都道德败坏，试图讹诈，“彭宇案”前车之鉴，不扶不是我孬种，而是法律“逼良为娼”。</p> \n <p data-page-model=\"text\">人总是倾向于高估自己的道德水准，人心本就倾向相信恶和夸大风险。</p> \n <p data-page-model=\"text\">人们热心传播的，永远都是好心人扶起摔倒老人反被讹诈的新闻，而对那些真撞倒了老人却冒充见义勇为的新闻视而不见。</p> \n <h2 data-page-model=\"title\">不要再拿“彭宇案”当软弱的借口</h2> \n <p data-page-model=\"text\">如果“彭宇真的撞了人”这一法律事实得到了充分传播，就会扭转现在的社会风气吗？</p> \n <p data-page-model=\"text\">并不会。“彭宇案”没有引发道德败坏，在它之前，人心没有多好；在它之后，人心也未必变得更坏。“世风日下，道德沦丧”的慨叹很难找到坐标系。</p> \n <p data-page-model=\"text\">做正确的事，从来都有成本和风险。人们要突破自利、自保的本能，需要更大的勇气，这是不能强求和苛责的。</p> \n <p data-page-model=\"text\">不敢做正确的事情，就是一种软弱。软弱在许多情况下，值得原谅。但“彭宇案”不是软弱的借口。做不到就是做不到，直面自己的软弱也是一种勇敢。</p> \n</div> \n<div class=\"summary\"> \n <div class=\"summary_tit\">\n  &nbsp;\n </div> \n <div class=\"summary_text\"> \n  <p>要坚持做正确的事。</p> \n </div> \n</div>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'delete',0,0,NULL,0,0.00,NULL,0,NULL,4,'2017-06-17 16:01:39','2017-06-17 16:01:39','放过彭宇案_别再为自己的软弱找借口',NULL,NULL,NULL,NULL,NULL,NULL,0),(3,'陈志武：左宗棠西征的金融故事','<p class=\"imgNode\" align=\"center\"><img src=\"http://inews.gtimg.com/newsapp_match/0/1674189008/0\" alt=\"\" width=\"100%\"></p> \n<p class=\"text image_desc\">&nbsp;</p> \n<p class=\"text\"><strong>【金融其实很简单】</strong></p> \n<p class=\"text\">今天，我们讲19世纪左宗棠西征成功背后的金融故事。之前大家已经了解到金融对个人、尤其对解放个人的重要性。但，实际上，<strong>金融对于拯救王朝同样重要，我们也可以用金融视角去重新梳理历史</strong>。</p> \n<p class=\"text\">就以历史上的湘军为例，晚清以来湘军影响力非凡。特别是19世纪中期西北回民起义爆发后, 左宗棠从1866年开始，率领湘军，用了14 年时间, 先镇压陕甘回民起义, 之后剑锋西指,收复西域新疆全境。但，你可能不知道，<strong>如果左宗棠不是在战争融资上进行创新，他的西征要么难以进行，要么会失败。</strong>因为如果没有金融支持，他可能发不了军饷，或者军饷一时有一时无，而没有军饷，就无法得到他需要的军心。如果没有军心，战争胜利从何而谈？</p> \n<p class=\"text\">那么，左宗棠做了什么金融创新呢？</p> \n<p class=\"text\"><strong>清朝战争军费的故事</strong></p> \n<p class=\"text\">我们先看看左宗棠所处的时代背景。不管是古代还是现代，对任何国家来说，正常税赋收入都有固定的用途安排，改变起来阻力很大，就像现在要财政部改变存量开支比登天还难一样。而战争、内乱何时发生，战争持续多久，以及需要多少军费开支等等，这些都是没法事先确定的，会很随机，所以，为战争融资一直是王朝最为头疼的事情，就像旧社会的灾荒、瘟疫事件让普通家庭很痛苦一样。</p> \n<p class=\"text\">&nbsp;</p> \n<p class=\"imgNode\" align=\"center\"><img src=\"http://inews.gtimg.com/newsapp_match/0/1674189009/0\" alt=\"\" width=\"100%\"></p> \n<p class=\"text image_desc\">&nbsp;</p> \n<p class=\"text\">在清代，突发战争带来的军费开支主要通过几种方式获得。</p> \n<p class=\"text\"><strong>首先是平时积累的财富</strong>，朝廷和地方督抚都尽量节省开支、多存银子，以防后患。这个办法的有效性很低，因为清朝政府没有多少剩余收入，存不了多少财富，而且我们今后会学到，存钱难以规避未来风险。</p> \n<p class=\"text\"><strong>二是靠卖官位</strong>，正常情况下都是卖虚职，比如，翰林待诏、官衔、候选官等。只有万不得已的时候才卖实权职位，像知州、知县之类。卖官收入有多重要呢？<strong>鸦片战争的那三年里，卖官收入占各省战争支出的23%。鸦片战争后的十年，中央和地方的财政赤字中，有46%靠卖官收入弥补。</strong></p> \n<p class=\"text\">&nbsp;</p> \n<p class=\"imgNode\" align=\"center\"><img src=\"http://inews.gtimg.com/newsapp_match/0/1674189010/0\" alt=\"\" width=\"100%\"></p> \n<p class=\"text image_desc\">&nbsp;</p> \n<p class=\"text\">对我们现代人，卖官似乎很不能理解。但在当时没有金融市场的背景下，一旦碰到国家存亡的战争，卖官位是没有办法的办法，这跟旧社会里一到灾荒发生时一些家庭卖妻妾求生存一样。只是政府在活不下去的时候没有妻妾或女儿可卖，但可以卖官位，或者放弃疆域领土。从这个意义上讲，如果清朝就有发达的金融市场，可能就不需要卖官了。</p> \n<p class=\"text\"><strong>三是靠协饷制度解决战争军费</strong>，也就是财政收入的跨省调配。由于不是每个省都能自行负担本省的军事支出，它们需要富裕省份的协助；而且也不是每个省都同时要对付内战，所以，没有战争的省要去支持处于战争之中的省份。比如，西部省份的财政收入少但军费开支高，它们往往是收协省份，接收临近富裕省份的财政盈余。因此，山西、山东与河南成为陕西和甘肃的主要协助省份。四川、云南、贵州则主要接收来自江西、湖南、湖北的协助。</p> \n<p class=\"text\">在康乾盛世时期、甚至到19世纪上半期，这个体系运行得还不是问题，可是，1851-1864年的太平天国内战涉及众多省份，大大冲击既有的战争开支体系。</p> \n<p class=\"text\">一方面，卖官位以前是户部的特权，省级层面即使卖也是在本省辖区内卖，但是太平天国期间这个规矩开始混乱，安徽的皖军跑到湖南长沙去卖官位、搞皖捐。结果，在1867年，当时的湖南巡抚刘崐向朝廷抱怨，要求安徽从湖南撤回它们的捐局，而安徽巡抚英翰也不示弱，向朝廷诉说他们的苦难。结果，同治皇帝下令维持现状，可以跨区域卖官！这就打乱阵脚。</p> \n<p class=\"text\">另一方面，协饷支持也迟迟不能到款，因为谁都面对经费挑战！</p> \n<p class=\"text\"><strong>左宗棠的金融创新</strong></p> \n<p class=\"text\">1866年左宗棠调任陕甘总督，带领湘军接手镇压陕甘回乱。他的军费挑战有多大呢？我们可以看看，左宗棠在1873 年成功平息陕甘回乱之后,提交给皇帝的《恳改拨饷, 以固军心折》,他在回顾自1866 年以来军费不足的困扰时,说<strong>“起初一年尚拨两月满饷。嗣后一年拨一月满饷, 至今一月满饷尚无可发,军心不问可知”</strong>。</p> \n<p class=\"text\">接下来，左宗棠筹备进一步西进收复新疆，提出800 万两银子的年度西征军费预算。朝廷对此极为重视, 向为西征军提供协饷的各省、海关发出指令, 限期将所欠协饷尽数解交甘肃前线。但，无论朝廷措辞多强硬, 都不能缓解西征经费的困境。</p> \n<p class=\"text\">怎么办呢？我们看到，西征军未来有协饷收入，只是这些未来收入总是拖欠，也很不稳定。那么，这些未来收入怎么可以转变成今天能花的钱呢？而且同样重要的一点是，军饷不稳定，左宗棠就无法稳住七八万军心。</p> \n<p class=\"text\">也就是说，<strong>如果能把未来收入一次性借到今天，那么，不仅军费会大增，因为未来是无限的，经费也会很稳定</strong>。</p> \n<p class=\"text\">但问题是，如何借？找谁借呢？在中国历史上，早在两千多年前的齐国，也是为了战争融资，管子说通了齐王向殷实之家借钱。可是，战争胜利之后齐王赖帐。所以，后来没有人相信朝廷的承诺了，中国就一直没有发展出国债或公债市场。</p> \n<p class=\"text\">如果左宗棠是明末崇祯皇帝的武将，那他的西征胜利前景就难说了！好在他是在晚清，上海金融市场已经有一定规模，尤其洋行融资能力很高。胡雪岩是左宗棠的好朋友，胡跟当时的上海金融界非常熟，认识当时的汇丰银行，也知道现代金融市场怎么运作。虽然到晚清，朝廷内外对借钱花是完全不认同、不接受，但经费困局是实实在在的。除非清廷不再西征、放弃西域，否则就无其它办法。</p> \n<p class=\"text\">&nbsp;</p> \n<p class=\"imgNode\" align=\"center\"><img src=\"http://inews.gtimg.com/newsapp_match/0/1674189011/0\" alt=\"\" width=\"100%\"></p> \n<p class=\"text image_desc\">&nbsp;</p> \n<p class=\"text\"><strong>在胡雪岩的帮助下，左宗棠选择了“华洋借款”融资。</strong>1875年借洋款249万两，1877年分两次从汇丰银行共借800万两，1878年再借350万两等等。年息最低10%，最高18%，比当时中国民间借贷普遍为20%以上要低不少，而且借钱金额之大也是那时国内华商市场难以承受的。</p> \n<p class=\"text\">有了这些借款后，左宗棠停止卖官。金融结束了持续多个朝代的陋习。</p> \n<p class=\"text\"><strong>那么，借款对左宗棠有多重要呢？在1874-1880年间，西征军费总开支5100万两银子，其中2000多万两是通过华洋借款得到的，是军费开支的第二大来源。由此，我们看到，如果不是通过“透支未来”借钱花，左宗棠收复新疆的历史也许会完全不同。</strong></p> \n<p class=\"text\">当国际、国内动荡导致财政能力削弱的情况下,左宗棠利用金融市场,以未来协饷收入、关税作抵押, 向华商、外国银行借款，为西北作战提供稳定军饷,稳住了几万大军的军心，保证了清王朝对西北边疆的控制与治理。</p> \n<p class=\"text\">从左宗棠的故事里，可以看到金融不只是一堆金融工具，而是提供一套分析框架、思维方式，让我们换个角度去思考历史。</p> \n<p class=\"text\"><strong>我们总结一下今天的要点：</strong></p> \n<p class=\"text\">第一，中国从管子以后，基本停止靠借贷解决战争开支。这不仅使金融市场难以发展，而且使处于财政挑战下的王朝难以生存。</p> \n<p class=\"text\">第二，左宗棠在军费压力之下，选择了“透支未来”的债务融资、放弃卖官，为他的西征历史功绩奠定基础。他的金融创新虽然在今天看很简单，但在当时是很艰难的，不管是在观念上、还是在实际操作上，他都面对巨大压力。他的西征成功为今后靠金融为战争融资开辟了新路。</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'delete',0,0,NULL,0,0.00,NULL,0,NULL,18,'2017-06-17 16:02:36','2017-06-17 16:02:36','别将老人一词变成暗含贬意的标签',NULL,NULL,NULL,NULL,NULL,NULL,0),(4,'套花呗','/article-套花呗-1',NULL,NULL,0,NULL,'menu',NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,0,NULL,0,0,NULL,0,0.00,NULL,0,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(5,'黄金首饰','/article-黄金首饰-1',NULL,NULL,0,NULL,'menu',NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,0,NULL,0,0,NULL,0,0.00,NULL,0,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(6,'奢侈品','/article-奢侈品-1',NULL,NULL,0,NULL,'menu',NULL,NULL,NULL,NULL,NULL,NULL,NULL,3,0,NULL,0,0,NULL,0,0.00,NULL,0,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(7,'汽车','/article-汽车-1',NULL,NULL,0,NULL,'menu',NULL,NULL,NULL,NULL,NULL,NULL,NULL,4,0,NULL,0,0,NULL,0,0.00,NULL,0,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(8,'手机','/article-手机-1',NULL,NULL,0,NULL,'menu',NULL,NULL,NULL,NULL,NULL,NULL,NULL,5,0,NULL,0,0,NULL,0,0.00,NULL,0,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(9,'话费回收','/article-话费回收-1',NULL,NULL,0,NULL,'menu',NULL,NULL,NULL,NULL,NULL,NULL,NULL,6,0,NULL,0,0,NULL,0,0.00,NULL,0,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0),(10,'如何注册','<p class=\"p1\">下载客户端--&gt;点击首页提示注册登录--&gt;身份验证</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'normal',0,0,NULL,0,0.00,NULL,6,NULL,140,'2017-11-27 14:06:24','2017-10-23 08:58:17','365借条如何借钱_',NULL,NULL,NULL,NULL,NULL,NULL,1),(11,'如何发起借款','<p class=\"p1\">1、如何借款？</p> \n<p class=\"p1\">账户登录 --&gt; 点击首页“借款”顺序操作--&gt;找好友借款。</p> \n<p class=\"p1\">（注：需要好友下载365借条APP 相互加为好友）</p> \n<p class=\"p1\">2、如何还款？</p> \n<p class=\"p1\">点击首页右下角“我的”--&gt; 借入--&gt;点击订单--&gt;我要还款。</p> \n<p class=\"p1\">a、若为全额还款，可选择直接还款或线下还款也可选择申请延期，线下还款需出借好友确认后才算还款成功；</p> \n<p class=\"p1\">b、一笔借款可多次申请延期，申请延期默认是1天，延期时间可自行修改，最长一年；</p> \n<p class=\"p1\">c、申请延期，需和出借人协商并出借人同意后才能发布延期申请，如出借人未同意债务人延期请求，则不能进行申请延期。</p> \n<p class=\"p1\">3、好友之间借款有利息吗？</p> \n<p class=\"p1\">利息是朋友双方平等协商决定，和365借条平台无关。</p> \n<p class=\"p1\">4、逾期未还款有什么影响？</p> \n<p class=\"p1\">影响个人信用，严重者会被拉入黑名单，还会影响您日后的借款。借款人可能申请催收、仲裁、诉讼，可能影响到贷款人的正常生活。</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'draft',0,0,NULL,0,0.00,NULL,0,NULL,1,'2017-10-23 08:57:44','2017-10-23 08:57:44','如何注册',NULL,NULL,NULL,NULL,NULL,NULL,0),(12,'如何进行身份认证？','<p class=\"p1\">点击首页右下角“我的”--&gt;安全认证中心--&gt;身份认证。</p> \n<p class=\"p1\">注：</p> \n<p class=\"p1\">a、认证信息必须是真实有效；</p> \n<p class=\"p1\">b、需提供身份证号、本人储蓄银行卡号</p> \n<p class=\"p1\">c、需提供常用地址。</p> \n<p class=\"p1\">d、需提供常用微信、支付宝帐号</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'normal',0,0,NULL,0,0.00,NULL,0,NULL,6,'2017-10-23 09:00:03','2017-10-23 09:00:03','如何进行身份认证_',NULL,NULL,NULL,NULL,NULL,NULL,0),(13,'关于用户安全保障','<p class=\"p1\">1、365借条具有高级别数据安保系统，上岗员工经过专业培训，签署《客户隐私信息保护协议书》，切实保障客户相关信息安全。</p> \n<p class=\"p1\">2、自动和人审相结合对用户信息的采集、甄别、分析严格把控，保障借贷双方信息安全，真实有效。</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'normal',0,0,NULL,0,0.00,NULL,0,NULL,14,'2017-10-23 09:00:26','2017-10-23 09:00:26','关于用户安全保障',NULL,NULL,NULL,NULL,NULL,NULL,1),(14,'关于帐户密码','<p class=\"p1\">1、忘记密码怎么找回？</p> \n<p class=\"p1\">登录页面，点击忘记密码--&gt;输入手机号码验证--&gt;设置新密码 。</p> \n<p class=\"p1\">2、如何修改密码？</p> \n<p class=\"p1\">点击首页右下角“我的”右上角设置--&gt;修改登录密码/手势密码。</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'normal',0,0,NULL,0,0.00,NULL,0,NULL,14,'2017-10-23 09:01:02','2017-10-23 09:01:02','关于帐户密码',NULL,NULL,NULL,NULL,NULL,NULL,0),(15,'关于充值提现问题1','<p class=\"p1\">1、如何充值？</p> \n<p class=\"p1\">登录账户，点击首页右下角“我的” --&gt; 点击余额--&gt;“充值”/“提现”。</p> \n<p class=\"p1\">2、充值都有哪些充值方式？</p> \n<p class=\"p1\">银联充值（需绑定银行卡）;银联单次充值最低10元起；</p> \n<p class=\"p1\">充值产生的手续由用户承担。</p> \n<p class=\"p1\">按每笔充值金额的<span class=\"Apple-converted-space\">&nbsp; </span>0.6<span class=\"Apple-converted-space\">&nbsp; </span>%计算。</p> \n<p class=\"p1\">3、充值之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现充值30分钟后未到账，核实银行是否扣款；特殊情况致电客服电话400-1899-365核实。</p> \n<p class=\"p1\">4、提款到账时间？提现手续费多少？</p> \n<p class=\"p1\">银联提现支付，实现提现实时到账(一般30分钟之内)，实际到账时间因银行有所差别；手续费每笔2元。</p> \n<p class=\"p1\">a、充值提现的银行卡类型仅支持储蓄卡，不支持信用卡；不支持提款到别人银行卡。</p> \n<p class=\"p1\">b、提现申请通过后，若因申请人信息有误导致银行打款失败，将扣除2元并退还用户。</p> \n<p class=\"p1\">c、提现之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现提现30分钟后未到账，核实帐户余额是否扣款；特殊情况致电客服电话400-1899-365核实。</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'delete',0,0,NULL,0,0.00,NULL,1,NULL,106,'2017-10-23 09:01:37','2017-10-23 09:01:37','关于充值提现问题1',NULL,NULL,NULL,NULL,NULL,NULL,1),(16,'关于充值提现问题2','<p class=\"p1\">1、如何充值？</p> \n<p class=\"p1\">登录账户，点击首页右下角“我的” --&gt; 点击余额--&gt;“充值”/“提现”。</p> \n<p class=\"p1\">2、充值都有哪些充值方式？</p> \n<p class=\"p1\">银联充值（需绑定银行卡）;银联单次充值最低10元起；</p> \n<p class=\"p1\">充值产生的手续由用户承担。</p> \n<p class=\"p1\">按每笔充值金额的<span class=\"Apple-converted-space\">&nbsp; </span>0.6<span class=\"Apple-converted-space\">&nbsp; </span>%计算。</p> \n<p class=\"p1\">3、充值之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现充值30分钟后未到账，核实银行是否扣款；特殊情况致电客服电话400-1899-365核实。</p> \n<p class=\"p1\">4、提款到账时间？提现手续费多少？</p> \n<p class=\"p1\">银联提现支付，实现提现实时到账(一般30分钟之内)，实际到账时间因银行有所差别；手续费每笔2元。</p> \n<p class=\"p1\">a、充值提现的银行卡类型仅支持储蓄卡，不支持信用卡；不支持提款到别人银行卡。</p> \n<p class=\"p1\">b、提现申请通过后，若因申请人信息有误导致银行打款失败，将扣除2元并退还用户。</p> \n<p class=\"p1\">c、提现之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现提现30分钟后未到账，核实帐户余额是否扣款；特殊情况致电客服电话400-1899-365核实。</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'delete',0,0,NULL,0,0.00,NULL,1,NULL,139,'2017-10-23 09:01:37','2017-10-23 09:01:37','关于充值提现问题2',NULL,NULL,NULL,NULL,NULL,NULL,1),(17,'关于充值提现问题3','<p class=\"p1\">1、如何充值？</p> \n<p class=\"p1\">登录账户，点击首页右下角“我的” --&gt; 点击余额--&gt;“充值”/“提现”。</p> \n<p class=\"p1\">2、充值都有哪些充值方式？</p> \n<p class=\"p1\">银联充值（需绑定银行卡）;银联单次充值最低10元起；</p> \n<p class=\"p1\">充值产生的手续由用户承担。</p> \n<p class=\"p1\">按每笔充值金额的<span class=\"Apple-converted-space\">&nbsp; </span>0.6<span class=\"Apple-converted-space\">&nbsp; </span>%计算。</p> \n<p class=\"p1\">3、充值之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现充值30分钟后未到账，核实银行是否扣款；特殊情况致电客服电话400-1899-365核实。</p> \n<p class=\"p1\">4、提款到账时间？提现手续费多少？</p> \n<p class=\"p1\">银联提现支付，实现提现实时到账(一般30分钟之内)，实际到账时间因银行有所差别；手续费每笔2元。</p> \n<p class=\"p1\">a、充值提现的银行卡类型仅支持储蓄卡，不支持信用卡；不支持提款到别人银行卡。</p> \n<p class=\"p1\">b、提现申请通过后，若因申请人信息有误导致银行打款失败，将扣除2元并退还用户。</p> \n<p class=\"p1\">c、提现之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现提现30分钟后未到账，核实帐户余额是否扣款；特殊情况致电客服电话400-1899-365核实。</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'delete',0,0,NULL,0,0.00,NULL,1,NULL,137,'2017-10-23 09:01:37','2017-10-23 09:01:37','关于充值提现问题3',NULL,NULL,NULL,NULL,NULL,NULL,0),(18,'关于充值提现问题4','<p class=\"p1\">1、如何充值？</p> \n<p class=\"p1\">登录账户，点击首页右下角“我的” --&gt; 点击余额--&gt;“充值”/“提现”。</p> \n<p class=\"p1\">2、充值都有哪些充值方式？</p> \n<p class=\"p1\">银联充值（需绑定银行卡）;银联单次充值最低10元起；</p> \n<p class=\"p1\">充值产生的手续由用户承担。</p> \n<p class=\"p1\">按每笔充值金额的<span class=\"Apple-converted-space\">&nbsp; </span>0.6<span class=\"Apple-converted-space\">&nbsp; </span>%计算。</p> \n<p class=\"p1\">3、充值之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现充值30分钟后未到账，核实银行是否扣款；特殊情况致电客服电话400-1899-365核实。</p> \n<p class=\"p1\">4、提款到账时间？提现手续费多少？</p> \n<p class=\"p1\">银联提现支付，实现提现实时到账(一般30分钟之内)，实际到账时间因银行有所差别；手续费每笔2元。</p> \n<p class=\"p1\">a、充值提现的银行卡类型仅支持储蓄卡，不支持信用卡；不支持提款到别人银行卡。</p> \n<p class=\"p1\">b、提现申请通过后，若因申请人信息有误导致银行打款失败，将扣除2元并退还用户。</p> \n<p class=\"p1\">c、提现之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现提现30分钟后未到账，核实帐户余额是否扣款；特殊情况致电客服电话400-1899-365核实。</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'delete',0,0,NULL,0,0.00,NULL,1,NULL,60,'2017-10-23 09:01:37','2017-10-23 09:01:37','关于充值提现问题4',NULL,NULL,NULL,NULL,NULL,NULL,0),(19,'关于充值提现问题5','<p class=\"p1\">1、如何充值？</p> \n<p class=\"p1\">登录账户，点击首页右下角“我的” --&gt; 点击余额--&gt;“充值”/“提现”。</p> \n<p class=\"p1\">2、充值都有哪些充值方式？</p> \n<p class=\"p1\">银联充值（需绑定银行卡）;银联单次充值最低10元起；</p> \n<p class=\"p1\">充值产生的手续由用户承担。</p> \n<p class=\"p1\">按每笔充值金额的<span class=\"Apple-converted-space\">&nbsp; </span>0.6<span class=\"Apple-converted-space\">&nbsp; </span>%计算。</p> \n<p class=\"p1\">3、充值之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现充值30分钟后未到账，核实银行是否扣款；特殊情况致电客服电话400-1899-365核实。</p> \n<p class=\"p1\">4、提款到账时间？提现手续费多少？</p> \n<p class=\"p1\">银联提现支付，实现提现实时到账(一般30分钟之内)，实际到账时间因银行有所差别；手续费每笔2元。</p> \n<p class=\"p1\">a、充值提现的银行卡类型仅支持储蓄卡，不支持信用卡；不支持提款到别人银行卡。</p> \n<p class=\"p1\">b、提现申请通过后，若因申请人信息有误导致银行打款失败，将扣除2元并退还用户。</p> \n<p class=\"p1\">c、提现之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现提现30分钟后未到账，核实帐户余额是否扣款；特殊情况致电客服电话400-1899-365核实。</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'delete',0,0,NULL,0,0.00,NULL,1,NULL,36,'2017-10-23 09:01:37','2017-10-23 09:01:37','关于充值提现问题5',NULL,NULL,NULL,NULL,NULL,NULL,0),(20,'关于充值提现问题6','<p class=\"p1\">1、如何充值？</p> \n<p class=\"p1\">登录账户，点击首页右下角“我的” --&gt; 点击余额--&gt;“充值”/“提现”。</p> \n<p class=\"p1\">2、充值都有哪些充值方式？</p> \n<p class=\"p1\">银联充值（需绑定银行卡）;银联单次充值最低10元起；</p> \n<p class=\"p1\">充值产生的手续由用户承担。</p> \n<p class=\"p1\">按每笔充值金额的<span class=\"Apple-converted-space\">&nbsp; </span>0.6<span class=\"Apple-converted-space\">&nbsp; </span>%计算。</p> \n<p class=\"p1\">3、充值之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现充值30分钟后未到账，核实银行是否扣款；特殊情况致电客服电话400-1899-365核实。</p> \n<p class=\"p1\">4、提款到账时间？提现手续费多少？</p> \n<p class=\"p1\">银联提现支付，实现提现实时到账(一般30分钟之内)，实际到账时间因银行有所差别；手续费每笔2元。</p> \n<p class=\"p1\">a、充值提现的银行卡类型仅支持储蓄卡，不支持信用卡；不支持提款到别人银行卡。</p> \n<p class=\"p1\">b、提现申请通过后，若因申请人信息有误导致银行打款失败，将扣除2元并退还用户。</p> \n<p class=\"p1\">c、提现之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现提现30分钟后未到账，核实帐户余额是否扣款；特殊情况致电客服电话400-1899-365核实。</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'delete',0,0,NULL,0,0.00,NULL,1,NULL,36,'2017-10-23 09:01:37','2017-10-23 09:01:37','关于充值提现问题6',NULL,NULL,NULL,NULL,NULL,NULL,0),(21,'关于充值提现问题7','<p class=\"p1\">1、如何充值？</p> \n<p class=\"p1\">登录账户，点击首页右下角“我的” --&gt; 点击余额--&gt;“充值”/“提现”。</p> \n<p class=\"p1\">2、充值都有哪些充值方式？</p> \n<p class=\"p1\">银联充值（需绑定银行卡）;银联单次充值最低10元起；</p> \n<p class=\"p1\">充值产生的手续由用户承担。</p> \n<p class=\"p1\">按每笔充值金额的<span class=\"Apple-converted-space\">&nbsp; </span>0.6<span class=\"Apple-converted-space\">&nbsp; </span>%计算。</p> \n<p class=\"p1\">3、充值之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现充值30分钟后未到账，核实银行是否扣款；特殊情况致电客服电话400-1899-365核实。</p> \n<p class=\"p1\">4、提款到账时间？提现手续费多少？</p> \n<p class=\"p1\">银联提现支付，实现提现实时到账(一般30分钟之内)，实际到账时间因银行有所差别；手续费每笔2元。</p> \n<p class=\"p1\">a、充值提现的银行卡类型仅支持储蓄卡，不支持信用卡；不支持提款到别人银行卡。</p> \n<p class=\"p1\">b、提现申请通过后，若因申请人信息有误导致银行打款失败，将扣除2元并退还用户。</p> \n<p class=\"p1\">c、提现之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现提现30分钟后未到账，核实帐户余额是否扣款；特殊情况致电客服电话400-1899-365核实。</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'delete',0,0,NULL,0,0.00,NULL,1,NULL,147,'2017-10-23 09:01:37','2017-10-23 09:01:37','关于充值提现问题7',NULL,NULL,NULL,NULL,NULL,NULL,1),(22,'关于充值提现问题8','<p class=\"p1\">1、如何充值？</p> \n<p class=\"p1\">登录账户，点击首页右下角“我的” --&gt; 点击余额--&gt;“充值”/“提现”。</p> \n<p class=\"p1\">2、充值都有哪些充值方式？</p> \n<p class=\"p1\">银联充值（需绑定银行卡）;银联单次充值最低10元起；</p> \n<p class=\"p1\">充值产生的手续由用户承担。</p> \n<p class=\"p1\">按每笔充值金额的<span class=\"Apple-converted-space\">&nbsp; </span>0.6<span class=\"Apple-converted-space\">&nbsp; </span>%计算。</p> \n<p class=\"p1\">3、充值之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现充值30分钟后未到账，核实银行是否扣款；特殊情况致电客服电话400-1899-365核实。</p> \n<p class=\"p1\">4、提款到账时间？提现手续费多少？</p> \n<p class=\"p1\">银联提现支付，实现提现实时到账(一般30分钟之内)，实际到账时间因银行有所差别；手续费每笔2元。</p> \n<p class=\"p1\">a、充值提现的银行卡类型仅支持储蓄卡，不支持信用卡；不支持提款到别人银行卡。</p> \n<p class=\"p1\">b、提现申请通过后，若因申请人信息有误导致银行打款失败，将扣除2元并退还用户。</p> \n<p class=\"p1\">c、提现之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现提现30分钟后未到账，核实帐户余额是否扣款；特殊情况致电客服电话400-1899-365核实。</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'delete',0,0,NULL,0,0.00,NULL,1,NULL,147,'2017-10-23 09:01:37','2017-10-23 09:01:37','关于充值提现问题8',NULL,NULL,NULL,NULL,NULL,NULL,1),(23,'关于充值提现问题9','<p class=\"p1\">1、如何充值？</p> \n<p class=\"p1\">登录账户，点击首页右下角“我的” --&gt; 点击余额--&gt;“充值”/“提现”。</p> \n<p class=\"p1\">2、充值都有哪些充值方式？</p> \n<p class=\"p1\">银联充值（需绑定银行卡）;银联单次充值最低10元起；</p> \n<p class=\"p1\">充值产生的手续由用户承担。</p> \n<p class=\"p1\">按每笔充值金额的<span class=\"Apple-converted-space\">&nbsp; </span>0.6<span class=\"Apple-converted-space\">&nbsp; </span>%计算。</p> \n<p class=\"p1\">3、充值之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现充值30分钟后未到账，核实银行是否扣款；特殊情况致电客服电话400-1899-365核实。</p> \n<p class=\"p1\">4、提款到账时间？提现手续费多少？</p> \n<p class=\"p1\">银联提现支付，实现提现实时到账(一般30分钟之内)，实际到账时间因银行有所差别；手续费每笔2元。</p> \n<p class=\"p1\">a、充值提现的银行卡类型仅支持储蓄卡，不支持信用卡；不支持提款到别人银行卡。</p> \n<p class=\"p1\">b、提现申请通过后，若因申请人信息有误导致银行打款失败，将扣除2元并退还用户。</p> \n<p class=\"p1\">c、提现之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现提现30分钟后未到账，核实帐户余额是否扣款；特殊情况致电客服电话400-1899-365核实。</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'delete',0,0,NULL,0,0.00,NULL,1,NULL,146,'2017-10-23 09:01:37','2017-10-23 09:01:37','关于充值提现问题9',NULL,NULL,NULL,NULL,NULL,NULL,1),(24,'关于充值提现问题10','<p class=\"p1\">1、如何充值？</p> \n<p class=\"p1\">登录账户，点击首页右下角“我的” --&gt; 点击余额--&gt;“充值”/“提现”。</p> \n<p class=\"p1\">2、充值都有哪些充值方式？</p> \n<p class=\"p1\">银联充值（需绑定银行卡）;银联单次充值最低10元起；</p> \n<p class=\"p1\">充值产生的手续由用户承担。</p> \n<p class=\"p1\">按每笔充值金额的<span class=\"Apple-converted-space\">&nbsp; </span>0.6<span class=\"Apple-converted-space\">&nbsp; </span>%计算。</p> \n<p class=\"p1\">3、充值之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现充值30分钟后未到账，核实银行是否扣款；特殊情况致电客服电话400-1899-365核实。</p> \n<p class=\"p1\">4、提款到账时间？提现手续费多少？</p> \n<p class=\"p1\">银联提现支付，实现提现实时到账(一般30分钟之内)，实际到账时间因银行有所差别；手续费每笔2元。</p> \n<p class=\"p1\">a、充值提现的银行卡类型仅支持储蓄卡，不支持信用卡；不支持提款到别人银行卡。</p> \n<p class=\"p1\">b、提现申请通过后，若因申请人信息有误导致银行打款失败，将扣除2元并退还用户。</p> \n<p class=\"p1\">c、提现之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现提现30分钟后未到账，核实帐户余额是否扣款；特殊情况致电客服电话400-1899-365核实。</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'delete',0,0,NULL,0,0.00,NULL,1,NULL,32,'2017-10-23 09:01:37','2017-10-23 09:01:37','关于充值提现问题10',NULL,NULL,NULL,NULL,NULL,NULL,0),(25,'关于充值提现问题11','<p class=\"p1\">1、如何充值？</p> \n<p class=\"p1\">登录账户，点击首页右下角“我的” --&gt; 点击余额--&gt;“充值”/“提现”。</p> \n<p class=\"p1\">2、充值都有哪些充值方式？</p> \n<p class=\"p1\">银联充值（需绑定银行卡）;银联单次充值最低10元起；</p> \n<p class=\"p1\">充值产生的手续由用户承担。</p> \n<p class=\"p1\">按每笔充值金额的<span class=\"Apple-converted-space\">&nbsp; </span>0.6<span class=\"Apple-converted-space\">&nbsp; </span>%计算。</p> \n<p class=\"p1\">3、充值之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现充值30分钟后未到账，核实银行是否扣款；特殊情况致电客服电话400-1899-365核实。</p> \n<p class=\"p1\">4、提款到账时间？提现手续费多少？</p> \n<p class=\"p1\">银联提现支付，实现提现实时到账(一般30分钟之内)，实际到账时间因银行有所差别；手续费每笔2元。</p> \n<p class=\"p1\">a、充值提现的银行卡类型仅支持储蓄卡，不支持信用卡；不支持提款到别人银行卡。</p> \n<p class=\"p1\">b、提现申请通过后，若因申请人信息有误导致银行打款失败，将扣除2元并退还用户。</p> \n<p class=\"p1\">c、提现之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现提现30分钟后未到账，核实帐户余额是否扣款；特殊情况致电客服电话400-1899-365核实。</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'delete',0,0,NULL,0,0.00,NULL,1,NULL,32,'2017-10-23 09:01:37','2017-10-23 09:01:37','关于充值提现问题11',NULL,NULL,NULL,NULL,NULL,NULL,0),(26,'关于充值提现问题','<p class=\"p1\">1、如何充值？</p> \n<p class=\"p1\">登录账户，点击首页右下角“我的” --&gt; 点击余额--&gt;“充值”/“提现”。</p> \n<p class=\"p1\">2、充值都有哪些充值方式？</p> \n<p class=\"p1\">银联充值（需绑定银行卡）;银联单次充值最低10元起；</p> \n<p class=\"p1\">充值产生的手续由用户承担。</p> \n<p class=\"p1\">按每笔充值金额的<span class=\"Apple-converted-space\">&nbsp; </span>0.6<span class=\"Apple-converted-space\">&nbsp; </span>%计算。</p> \n<p class=\"p1\">3、充值之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现充值30分钟后未到账，核实银行是否扣款；特殊情况致电客服电话400-1899-365核实。</p> \n<p class=\"p1\">4、提款到账时间？提现手续费多少？</p> \n<p class=\"p1\">银联提现支付，实现提现实时到账(一般30分钟之内)，实际到账时间因银行有所差别；手续费每笔2元。</p> \n<p class=\"p1\">a、充值提现的银行卡类型仅支持储蓄卡，不支持信用卡；不支持提款到别人银行卡。</p> \n<p class=\"p1\">b、提现申请通过后，若因申请人信息有误导致银行打款失败，将扣除2元并退还用户。</p> \n<p class=\"p1\">c、提现之后多久到帐？未到账怎么办？</p> \n<p class=\"p1\">即时到账，出现提现30分钟后未到账，核实帐户余额是否扣款；特殊情况致电客服电话400-1899-365核实。</p>',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'normal',0,0,NULL,0,0.00,NULL,1,NULL,32,'2017-10-23 09:01:37','2017-12-06 22:12:45','关于充值提现问题12',NULL,NULL,NULL,NULL,NULL,NULL,0),(29,'不知道什么标题好呢','更不知道要说什么',NULL,NULL,0,NULL,'article',NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,0,'delete',0,0,NULL,0,0.00,NULL,1,NULL,111,'2017-11-29 10:38:58',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `y_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_contract`
--

DROP TABLE IF EXISTS `y_contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_contract` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `contract_number` varchar(64) CHARACTER SET utf8mb4 NOT NULL COMMENT '协义自定义编号',
  `contract_type_id` int(10) unsigned NOT NULL COMMENT '合同范本编号',
  `agreement` varchar(255) DEFAULT '',
  `apply_id` bigint(20) NOT NULL,
  `credit_id` bigint(20) unsigned NOT NULL COMMENT '贷方,user.id',
  `debit_id` bigint(20) unsigned NOT NULL COMMENT '借方，user.id',
  `amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '标的金额，扣除一切规费后金额',
  `annual_rate` decimal(4,2) NOT NULL DEFAULT '0.00' COMMENT '年化利率，单位%',
  `repayment_method` tinyint(4) NOT NULL DEFAULT '0' COMMENT '约定的还款方式，1按月等额本息，2按月等额本金，3一次性还本付息',
  `create_time` datetime NOT NULL COMMENT '生成日期',
  `value_date` datetime NOT NULL COMMENT '起息日，利息真正发生的起止时间戳',
  `loan_term` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '贷款期限，天数（算头不算尾）。起息时间－到期时间＝天数*86400',
  `maturity_date` datetime DEFAULT NULL COMMENT '到期日，时间戳，一般为某日的00:00',
  `fee1` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '费用类型1',
  `fee2` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '费用类型2',
  `fee3` double(8,2) NOT NULL DEFAULT '0.00' COMMENT '费用类型3',
  `approval_u1` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '风控一级的user.id，为0表明未审批',
  `approval_time1` datetime DEFAULT NULL COMMENT '风控一级的批准时间',
  `approval_u2` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '风控二级的user.id',
  `approval_time2` datetime DEFAULT NULL COMMENT '风控二级的批准时间',
  `approval_u3` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '风控三级的user.id',
  `approval_time3` datetime DEFAULT NULL COMMENT '风控三级的批准时间，这是最终的审批意见',
  `status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '状态，0合约初订立（贷方资金冻结），1风控一批准，2风控二批准，3风控三批准，4资金划转前关闭，5资金划转成功贷款正式进入还款期，6正常结束，7展期, 8损失,',
  `repayment_status` tinyint(11) unsigned NOT NULL DEFAULT '0' COMMENT '五级不良分类：0不适用，1付息正常，2付息关注，3付息次级，4付息可疑，5损失',
  `update_time` datetime DEFAULT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1 COMMENT='借贷合约，描述借贷发生';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_contract`
--

LOCK TABLES `y_contract` WRITE;
/*!40000 ALTER TABLE `y_contract` DISABLE KEYS */;
INSERT INTO `y_contract` VALUES (20,'CN20170901172929974120',1,'',11,2,1,1500.00,18.00,3,'2017-09-01 17:29:29','2017-09-01 17:29:29',10,'2017-11-23 00:00:00',0.00,0.00,0.00,0,NULL,0,NULL,0,NULL,7,1,NULL),(21,'CN20170901173342523410',1,'',12,4,1,1000.00,18.00,3,'2017-09-01 17:33:42','2017-09-01 17:33:42',0,'2017-11-23 00:00:00',0.00,0.00,0.00,0,NULL,0,NULL,0,NULL,7,1,NULL),(22,'CN20170901173959144290',1,'',1,3,1,1000.00,18.00,3,'2017-09-01 17:39:59','2017-09-01 17:39:59',0,'2017-11-24 00:00:00',0.00,0.00,0.00,0,NULL,0,NULL,0,NULL,7,1,NULL),(23,'CN20170901174302360222',1,'',2,1,1,2000.00,18.00,3,'2017-09-01 17:43:02','2017-09-01 17:43:02',60,'2017-11-25 00:00:00',0.00,0.00,0.00,0,NULL,0,NULL,0,NULL,7,1,NULL),(24,'CN20171008002626707061',1,'',6,3,1,100.00,10.00,3,'2017-10-08 00:26:26','2017-10-08 00:26:26',28,'2017-11-26 00:00:00',0.00,0.00,0.00,0,NULL,0,NULL,0,NULL,7,1,'2017-10-12 18:28:55'),(25,'CN20171008003559868808',1,'/attachment/agreement/permenant/201710/10/935a51fce91547cf92b6e823cff99180.png',4,3,1,1000.00,18.00,3,'2017-10-08 00:35:59','2017-12-08 00:35:59',23,'2017-11-27 00:00:00',0.00,0.00,0.00,0,NULL,0,NULL,0,NULL,7,1,NULL),(26,'CN20171012231231372236',1,'',13,3,1,100.00,10.00,3,'2017-10-12 23:12:31','2017-10-12 23:12:31',31,'2017-11-28 00:00:00',0.00,0.00,0.00,0,NULL,0,NULL,0,NULL,7,1,NULL);
/*!40000 ALTER TABLE `y_contract` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_creditlog`
--

DROP TABLE IF EXISTS `y_creditlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_creditlog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '变动的用户',
  `credit_type` tinyint(3) NOT NULL COMMENT '1余额,2积分',
  `change` decimal(8,2) NOT NULL COMMENT '余额变动额',
  `cur` decimal(8,2) NOT NULL,
  `clerk` bigint(20) NOT NULL COMMENT '操作的员工号，若为0则表示用户自己操作',
  `log` varchar(255) NOT NULL COMMENT '操作事项',
  `create_time` datetime NOT NULL COMMENT '日志生成时间',
  `platform` tinyint(4) NOT NULL DEFAULT '0' COMMENT '平台',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='【新增】余额变动日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_creditlog`
--

LOCK TABLES `y_creditlog` WRITE;
/*!40000 ALTER TABLE `y_creditlog` DISABLE KEYS */;
INSERT INTO `y_creditlog` VALUES (1,7,1,0.01,1.04,0,'银联App控件版充值转入','2017-09-24 21:44:07',3),(2,1,1,-10.00,9990.00,0,'手机认证服务费','2017-09-27 17:36:14',4),(3,1,1,-10.00,9980.00,0,'手机认证服务费','2017-09-27 17:41:49',4),(4,8,1,10.00,10.00,0,'银联充值','2017-09-29 13:22:37',3),(5,8,1,-10.00,0.00,0,'手机号认证费扣款','2017-09-29 13:22:37',4),(6,8,1,-10.00,90.00,0,'手机认证服务费','2017-09-30 15:51:44',4),(7,8,1,-10.00,80.00,0,'手机认证服务费','2017-09-30 15:58:23',4),(8,8,1,-10.00,70.00,0,'手机认证服务费','2017-09-30 17:16:16',4),(9,3,1,-100.00,9900.00,0,'作为贷方扣款，交易号： CN20171008002626707061','2017-10-08 00:26:26',4),(10,8,1,100.00,170.00,0,'作为借方放款，交易号： CN20171008002626707061','2017-10-08 00:26:26',4),(11,8,1,-1000.00,8900.00,0,'作为贷方扣款，交易号： CN20171008003559868808','2017-10-08 00:35:59',4),(12,8,1,1000.00,10980.00,0,'作为借方放款，交易号： CN20171008003559868808','2017-10-08 00:35:59',4),(13,8,1,-100.73,69.27,0,'主动还款','2017-10-12 18:08:24',4),(14,8,1,100.73,9000.73,1,'收到还款','2017-10-12 18:08:24',4),(15,8,1,-100.73,69.27,0,'主动还款','2017-10-12 18:10:29',4),(16,3,1,100.73,9101.46,1,'收到还款','2017-10-12 18:10:29',4),(17,8,1,-100.73,68.54,0,'主动还款','2017-10-12 18:28:55',4),(18,3,1,100.73,9202.19,8,'收到还款','2017-10-12 18:28:55',4),(19,8,1,-100.00,9102.19,0,'作为贷方扣款，交易号： CN20171012231231372236','2017-10-12 23:12:31',4),(20,8,1,100.00,168.54,1,'作为借方放款，交易号： CN20171012231231372236','2017-10-12 23:12:31',4),(21,8,1,-10.00,158.54,1,'提现预扣款','2017-10-12 23:19:44',4),(22,8,1,10.00,10.00,1,'银联充值','2017-10-23 08:50:21',3),(23,8,1,-10.00,0.00,0,'手机号认证费扣款','2017-10-23 08:50:21',4),(24,8,1,20.00,20.00,0,'银联充值','2017-10-23 08:51:02',3),(25,8,1,-10.00,10.00,0,'手机认证服务费','2017-10-23 08:51:33',4);
/*!40000 ALTER TABLE `y_creditlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_feedback`
--

DROP TABLE IF EXISTS `y_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) DEFAULT '0',
  `name` varchar(20) DEFAULT '',
  `tel` varchar(20) DEFAULT '',
  `content` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `status` tinyint(3) DEFAULT '0',
  `feedback` varchar(45) DEFAULT '',
  `clerk` bigint(20) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_feedback`
--

LOCK TABLES `y_feedback` WRITE;
/*!40000 ALTER TABLE `y_feedback` DISABLE KEYS */;
INSERT INTO `y_feedback` VALUES (5,2,'a','18968596872','xxxx','2017-08-31 15:42:13',1,'fffff',1),(8,1,'a','18968596872','xxxx','2017-08-31 15:49:34',1,NULL,1),(11,1,'a','18968596872','xxxx','2017-08-31 15:51:14',0,'aaa',1);
/*!40000 ALTER TABLE `y_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_follow`
--

DROP TABLE IF EXISTS `y_follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_follow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `followed_id` int(11) NOT NULL COMMENT '被关注者',
  `follower_id` int(11) NOT NULL COMMENT '关注者',
  `status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '状态，1有效，0无效',
  `change_time` datetime NOT NULL COMMENT '最后一次改变的时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1 COMMENT='关注表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_follow`
--

LOCK TABLES `y_follow` WRITE;
/*!40000 ALTER TABLE `y_follow` DISABLE KEYS */;
INSERT INTO `y_follow` VALUES (2,2,1,1,'2017-08-14 23:14:44'),(3,1,1,1,'2017-09-10 17:27:19'),(4,4,6,1,'2017-09-24 10:15:03'),(5,3,6,1,'2017-09-24 10:15:18'),(6,3,7,0,'2017-09-26 15:35:49'),(7,3,8,1,'2017-12-02 10:18:09'),(8,4,8,1,'2017-10-04 17:23:18'),(9,8,3,1,'2017-11-23 17:23:31'),(10,4,3,1,'2017-11-22 01:28:33'),(11,8,4,1,'2017-10-12 17:23:12'),(12,10,9,1,'2017-10-23 08:42:21');
/*!40000 ALTER TABLE `y_follow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_mapping`
--

DROP TABLE IF EXISTS `y_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_mapping` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `content_id` bigint(20) unsigned NOT NULL COMMENT '内容ID',
  `taxonomy_id` bigint(20) unsigned NOT NULL COMMENT '分类ID',
  PRIMARY KEY (`id`),
  KEY `taxonomy_id` (`taxonomy_id`),
  KEY `content_id` (`content_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='内容和分类的多对多映射关系。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_mapping`
--

LOCK TABLES `y_mapping` WRITE;
/*!40000 ALTER TABLE `y_mapping` DISABLE KEYS */;
INSERT INTO `y_mapping` VALUES (3,11,7),(4,10,7),(5,12,7),(6,13,7),(8,14,7),(9,15,7);
/*!40000 ALTER TABLE `y_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_message`
--

DROP TABLE IF EXISTS `y_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_message` (
  `id` bigint(20) NOT NULL,
  `to_userid` bigint(20) NOT NULL COMMENT '信息收件人',
  `from_userid` bigint(20) NOT NULL COMMENT '发件人',
  `type` tinyint(3) NOT NULL COMMENT '类型',
  `content` text CHARACTER SET utf8 NOT NULL,
  `is_read` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否已读',
  `read_time` datetime DEFAULT NULL,
  `is_action` tinyint(3) NOT NULL DEFAULT '0' COMMENT '是否需要客户端执行',
  `act` varchar(32) CHARACTER SET utf8 NOT NULL COMMENT '动作类型，order或web',
  `contract_id` bigint(20) NOT NULL COMMENT '关联的合约id',
  `apply_id` bigint(20) NOT NULL COMMENT '关联的申请id',
  `url` varchar(255) CHARACTER SET utf8 NOT NULL COMMENT '网址',
  `create_time` datetime NOT NULL COMMENT '产生日期',
  `deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_message`
--

LOCK TABLES `y_message` WRITE;
/*!40000 ALTER TABLE `y_message` DISABLE KEYS */;
INSERT INTO `y_message` VALUES (1,1,2,1,'<div style=\"color:red;\">第一条信息哦</div>',1,'2017-08-31 14:08:14',0,'',1,1,'','2017-08-29 00:00:00',0);
/*!40000 ALTER TABLE `y_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_metadata`
--

DROP TABLE IF EXISTS `y_metadata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_metadata` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `meta_key` varchar(255) DEFAULT NULL COMMENT '元数据key',
  `meta_value` text COMMENT '元数据value',
  `object_type` varchar(32) DEFAULT NULL COMMENT '元数据的对象类型',
  `object_id` bigint(20) unsigned DEFAULT NULL COMMENT '元数据的对象ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COMMENT='元数据表，用来对其他表的字段扩充。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_metadata`
--

LOCK TABLES `y_metadata` WRITE;
/*!40000 ALTER TABLE `y_metadata` DISABLE KEYS */;
INSERT INTO `y_metadata` VALUES (1,'_meta1',NULL,'content',1),(2,'_meta2',NULL,'content',1),(3,'_meta1',NULL,'content',2),(4,'_meta2',NULL,'content',2),(5,'_meta1',NULL,'content',3),(6,'_meta2',NULL,'content',3),(7,'_meta1',NULL,'content',10),(8,'_meta2',NULL,'content',10),(9,'_meta1',NULL,'content',11),(10,'_meta2',NULL,'content',11),(11,'_meta1',NULL,'content',12),(12,'_meta2',NULL,'content',12),(13,'_meta1',NULL,'content',13),(14,'_meta2',NULL,'content',13),(15,'_meta1',NULL,'content',14),(16,'_meta2',NULL,'content',14),(17,'_meta1',NULL,'content',15),(18,'_meta2',NULL,'content',15),(19,'_meta1',NULL,'content',26),(20,'_meta2',NULL,'content',26);
/*!40000 ALTER TABLE `y_metadata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_oplog`
--

DROP TABLE IF EXISTS `y_oplog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_oplog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) DEFAULT '0' COMMENT '操作员',
  `name` varchar(45) DEFAULT '',
  `type` varchar(45) DEFAULT '',
  `create_time` datetime DEFAULT NULL,
  `op` varchar(1024) DEFAULT '',
  `ip` varchar(45) DEFAULT '0.0.0.0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8 COMMENT='后台操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_oplog`
--

LOCK TABLES `y_oplog` WRITE;
/*!40000 ALTER TABLE `y_oplog` DISABLE KEYS */;
INSERT INTO `y_oplog` VALUES (14,1,'后台登录','admin.login','2017-12-05 16:21:38','登录成功','60.12.168.170'),(15,1,'后台登录','admin.login','2017-12-06 21:52:50','登录成功','39.181.204.225'),(16,1,'评论丢入垃圾箱','comment.trash','2017-12-06 21:53:19','评论ID 24','39.181.204.225'),(17,1,'回复评论','comment.reply','2017-12-06 21:54:25','评论ID 25','39.181.204.225'),(18,1,'删除评论','comment.delete','2017-12-06 21:54:41','评论ID 24','39.181.204.225'),(19,1,'评论丢入垃圾箱','comment.trash','2017-12-06 21:55:04','评论ID 25','39.181.204.225'),(20,1,'评论丢入垃圾箱','comment.trash','2017-12-06 21:55:06','评论ID 23','39.181.204.225'),(21,1,'评论丢入垃圾箱','comment.trash','2017-12-06 21:55:08','评论ID 22','39.181.204.225'),(22,1,'评论丢入垃圾箱','comment.trash','2017-12-06 21:55:09','评论ID 21','39.181.204.225'),(23,1,'评论丢入垃圾箱','comment.trash','2017-12-06 21:55:10','评论ID 20','39.181.204.225'),(24,1,'评论丢入垃圾箱','comment.trash','2017-12-06 21:55:12','评论ID 19','39.181.204.225'),(25,1,'评论丢入垃圾箱','comment.trash','2017-12-06 21:55:14','评论ID 12','39.181.204.225'),(26,1,'评论丢入垃圾箱','comment.trash','2017-12-06 21:55:15','评论ID 11','39.181.204.225'),(27,1,'评论丢入垃圾箱','comment.trash','2017-12-06 21:55:17','评论ID 10','39.181.204.225'),(28,1,'评论丢入垃圾箱','comment.trash','2017-12-06 21:55:19','评论ID 9','39.181.204.225'),(29,1,'文章丢入垃圾箱','content.trash','2017-12-06 22:09:56','文章ID 17','39.181.204.225'),(30,1,'文章丢入垃圾箱','content.trash','2017-12-06 22:11:26','文章ID 16','39.181.204.225'),(31,1,'文章丢入垃圾箱','content.trash','2017-12-06 22:11:28','文章ID 18','39.181.204.225'),(32,1,'文章丢入垃圾箱','content.trash','2017-12-06 22:11:30','文章ID 19','39.181.204.225'),(33,1,'文章丢入垃圾箱','content.trash','2017-12-06 22:11:31','文章ID 20','39.181.204.225'),(34,1,'文章丢入垃圾箱','content.trash','2017-12-06 22:11:33','文章ID 21','39.181.204.225'),(35,1,'文章丢入垃圾箱','content.trash','2017-12-06 22:11:35','文章ID 22','39.181.204.225'),(36,1,'文章丢入垃圾箱','content.trash','2017-12-06 22:11:37','文章ID 23','39.181.204.225'),(37,1,'文章丢入垃圾箱','content.trash','2017-12-06 22:11:38','文章ID 24','39.181.204.225'),(38,1,'文章丢入垃圾箱','content.trash','2017-12-06 22:11:40','文章ID 25','39.181.204.225'),(39,1,'文章编辑','content.save','2017-12-06 22:12:45','文章ID 26','39.181.204.225'),(40,1,'年龄信用额度设置','option.credittableEdit.save','2017-12-06 22:17:27','内容 18:5000.0;19:8000.0;20:10000.0;21:15000.0;22:20000.0;23:200001.0','39.181.204.225'),(41,1,'年龄信用额度设置','option.credittableEdit.save','2017-12-06 22:17:48','内容 18:5000.0;19:8000.0;20:10000.0;21:15000.0;22:20000.0;23:200000.0','39.181.204.225'),(42,1,'变更角色','user.changeRole','2017-12-06 22:42:04','用户IDs [8]','39.181.204.225'),(43,1,'编辑广告','ad.save','2017-12-06 22:57:25','{\"createTime\":1512572245836,\"dscrpt\":\"qq\",\"id\":5,\"img\":\"/attachment/a.jpg\",\"imgMedia\":\"https://www.365jietiao.com/attachment/a.jpg\",\"orderNum\":10,\"primaryKey\":\"id\",\"primaryKeys\":[\"id\"],\"status\":1,\"tableName\":\"y_ad\",\"title\":\"测试a\",\"url\":\"https://www.qq.com/\",\"urlMedia\":\"https://www.qq.com/\"}','39.181.204.225'),(44,1,'编辑或新增用户','user.editOrCreate','2017-12-06 23:08:29','信息 11','0:0:0:0:0:0:0:1'),(45,1,'编辑或新增用户','user.editOrCreate','2017-12-06 23:09:27','用户ID [11]','39.181.204.225'),(46,1,'后台登录','admin.login','2017-12-07 12:00:26','登录成功','60.12.168.170'),(47,1,'查看银联流水','unionpaylog.index','2017-12-07 12:00:35','','60.12.168.170'),(48,1,'查看银联流水','unionpaylog.index','2017-12-07 12:00:36','','60.12.168.170'),(49,1,'查看提现列表','withdraw.index','2017-12-07 12:00:38','','60.12.168.170'),(50,1,'查看提现列表','withdraw.index','2017-12-07 12:00:39','','60.12.168.170'),(51,1,'查看提现列表','withdraw.index','2017-12-07 12:00:40','','60.12.168.170');
/*!40000 ALTER TABLE `y_oplog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_option`
--

DROP TABLE IF EXISTS `y_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_option` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `option_key` varchar(128) DEFAULT NULL COMMENT '配置KEY',
  `option_value` text COMMENT '配置内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COMMENT='配置信息表，用来保存网站的所有配置信息。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_option`
--

LOCK TABLES `y_option` WRITE;
/*!40000 ALTER TABLE `y_option` DISABLE KEYS */;
INSERT INTO `y_option` VALUES (1,'web_name','365借条'),(2,'web_administrator_email','ejie666@163.com'),(3,'web_administrator_wechat_openid','z831116'),(4,'web_icp_number','沪ICP备17035478号-1'),(5,'autosave','web_name,web_title,web_subtitle,web_domain,web_administrator_email,web_administrator_phone,web_administrator_wechat_openid,web_copyright,web_icp_number'),(6,'web_subtitle','365借条'),(7,'web_title','365借条'),(8,'web_domain','https://www.365jietiao.com'),(9,'web_administrator_phone','13857665677'),(10,'ucode','5d053396451cc79e6c0da92548ffab93'),(11,'web_copyright','@2017 版权所有\n上海易谈网络科技有限公司'),(12,'credittable','18:5000.0;19:8000.0;20:10000.0;21:15000.0;22:20000.0;23:200000.0'),(13,'sensitive','法轮,骗子,高利贷,卖淫,嫖娼'),(14,'sensitive_file','/attachment/20171122/b59d29eed2a942c8a73af567266e2061.cfg');
/*!40000 ALTER TABLE `y_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_report`
--

DROP TABLE IF EXISTS `y_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_userid` bigint(20) DEFAULT NULL COMMENT '举报人',
  `to_userid` bigint(20) DEFAULT '0' COMMENT '被举报人',
  `content` text,
  `imgs` varchar(1024) DEFAULT '' COMMENT '图片，多个时以逗号分隔',
  `create_time` datetime DEFAULT NULL,
  `feedback` text,
  `clerk` bigint(20) DEFAULT '0',
  `status` tinyint(3) DEFAULT '0' COMMENT '0未处理， 1已处理',
  `deleted` tinyint(3) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_report`
--

LOCK TABLES `y_report` WRITE;
/*!40000 ALTER TABLE `y_report` DISABLE KEYS */;
INSERT INTO `y_report` VALUES (3,1,0,'xxxx','/attachment/aaa.jpg','2017-08-31 14:48:34','bbb',1,1,0),(4,1,3,'xxxx','/attachment/aaa.jpg','2017-08-31 14:48:56','2222222222222ddd',1,1,0),(6,8,0,'就像那些惊喜交加','','2017-10-01 13:33:31','dd',1,1,0),(7,8,0,'难道你','','2017-10-04 17:51:45',NULL,0,0,0),(8,3,0,'哈哈哈','','2017-10-08 00:44:01',NULL,1,0,0),(10,8,0,'aaa','','2017-11-09 11:13:16',NULL,0,0,0),(11,8,0,'aaaa','','2017-11-09 11:33:45',NULL,0,0,0),(12,8,0,'bbb','','2017-11-09 11:34:01',NULL,0,0,0),(13,8,0,'aaa','','2017-11-15 09:52:57',NULL,0,0,0),(14,8,0,'bbb','','2017-11-15 09:54:54',NULL,0,0,0),(15,8,0,'bbb','','2017-11-15 09:54:57',NULL,0,0,0),(16,8,0,'bbb','','2017-11-15 09:55:28',NULL,0,0,0),(17,8,0,'bbb','','2017-11-15 09:56:43',NULL,0,0,0),(18,8,0,'aaa','','2017-11-15 11:39:54',NULL,0,0,0),(19,3,0,'high','','2017-11-23 16:57:51',NULL,0,0,0),(20,3,0,'high GCC GCC GCC fgvvvvvcc? fgvvvvvcc fgvvvvvcc fgvvvvvcc fgvvvvvcc fgvvvvvcc fgvvvvvcc fgvvvvvcc high higher education','','2017-11-23 17:04:59',NULL,0,0,0);
/*!40000 ALTER TABLE `y_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_taxonomy`
--

DROP TABLE IF EXISTS `y_taxonomy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_taxonomy` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(512) DEFAULT NULL COMMENT '标题',
  `text` text COMMENT '内容描述',
  `slug` varchar(128) DEFAULT NULL COMMENT 'slug',
  `type` varchar(32) DEFAULT NULL COMMENT '类型',
  `icon` varchar(128) DEFAULT NULL COMMENT '图标',
  `content_module` varchar(32) DEFAULT NULL COMMENT '对于的内容模型',
  `content_count` int(11) unsigned DEFAULT '0' COMMENT '该分类的内容数量',
  `order_number` int(11) DEFAULT NULL COMMENT '排序编码',
  `parent_id` bigint(20) unsigned DEFAULT NULL COMMENT '父级分类的ID',
  `object_id` bigint(20) unsigned DEFAULT NULL COMMENT '关联的对象ID',
  `flag` varchar(256) DEFAULT NULL COMMENT '标识',
  `lat` decimal(20,16) DEFAULT NULL COMMENT '经度',
  `lng` decimal(20,16) DEFAULT NULL COMMENT '纬度',
  `meta_keywords` varchar(256) DEFAULT NULL COMMENT 'SEO关键字',
  `meta_description` varchar(256) DEFAULT NULL COMMENT 'SEO描述内容',
  `created` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `parent_id` (`parent_id`),
  KEY `object_id` (`object_id`),
  KEY `content_module` (`content_module`),
  KEY `created` (`created`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='分类表。标签、专题、类别等都属于taxonomy。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_taxonomy`
--

LOCK TABLES `y_taxonomy` WRITE;
/*!40000 ALTER TABLE `y_taxonomy` DISABLE KEYS */;
INSERT INTO `y_taxonomy` VALUES (1,'套花呗',NULL,'套花呗','feature',NULL,'article',0,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,'黄金首饰',NULL,'黄金首饰','feature',NULL,'article',0,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,'奢侈品',NULL,'奢侈品','feature',NULL,'article',0,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,'汽车',NULL,'汽车','feature',NULL,'article',0,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(5,'手机',NULL,'手机','feature',NULL,'article',0,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6,'话费回收',NULL,'话费回收','feature',NULL,'article',0,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(7,'常见问题',NULL,'常见问题','feature',NULL,'article',5,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `y_taxonomy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_unionpaylog`
--

DROP TABLE IF EXISTS `y_unionpaylog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_unionpaylog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pay_sn` varchar(45) DEFAULT NULL COMMENT '充值的字符串序列化',
  `user_id` bigint(20) DEFAULT NULL COMMENT '发起充值的用户',
  `fee` decimal(8,2) DEFAULT NULL COMMENT '充值金额，单位元。 注意：银联支付接口的金额单位为分。',
  `create_time` datetime DEFAULT NULL COMMENT '充值创建时间',
  `tn` varchar(45) DEFAULT '' COMMENT '从银联返回的tn号，传给前端调起银联App控件用',
  `update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `status` tinyint(3) DEFAULT '0' COMMENT '0：未支付； 1：支付成功。支付成功后，就更新creditlog表，并增加用户的balance字段。',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='银联充值日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_unionpaylog`
--

LOCK TABLES `y_unionpaylog` WRITE;
/*!40000 ALTER TABLE `y_unionpaylog` DISABLE KEYS */;
INSERT INTO `y_unionpaylog` VALUES (1,'PA20170915163251422484',1,10.00,'2017-09-15 16:32:51','771930382168988608000','2017-09-15 16:32:51',0),(2,'PA20170924115209954634',1,10.00,'2017-09-24 11:52:09','751367547465092976001','2017-09-24 11:52:09',0),(3,'PA20170924135525551281',1,1.00,'2017-09-24 13:55:25','596015704998244058907','2017-09-24 13:55:25',0),(4,'PA20170924135536971484',1,0.01,'2017-09-24 13:55:36','685121877712029740006','2017-09-24 13:55:36',0),(5,'PA20170924160103070211',7,0.01,'2017-09-24 16:01:03','506983109709030400905','2017-09-24 16:01:03',0),(6,'PA20170924160152769758',7,0.01,'2017-09-24 16:01:52','788660260030765837010','2017-09-24 16:01:52',0),(7,'PA20170924160516403024',7,0.01,'2017-09-24 16:05:16','742332832725377209507','2017-09-24 16:05:16',0),(8,'PA20170924161938698647',7,0.01,'2017-09-24 16:19:38','471698448646956567307','2017-09-24 16:19:38',0),(9,'PA20170924162550419216',7,0.01,'2017-09-24 16:25:50','439441007326375341900','2017-09-24 16:25:50',0),(10,'PA20170924165817207719',7,0.01,'2017-09-24 16:58:17','478298619322844040403','2017-09-24 16:58:17',0),(11,'PA20170924172854957313',7,0.01,'2017-09-24 17:28:54','627386995570005123605','2017-09-24 17:28:54',0),(12,'PA20170924190324755516',7,0.01,'2017-09-24 19:03:24','687092478540725795707','2017-09-24 19:03:24',0),(13,'PA20170924210538268505',7,0.01,'2017-09-24 21:05:38','862028625616663354004','2017-09-24 21:06:28',1),(14,'PA20170924211621572915',7,0.01,'2017-09-24 21:16:21','822199092728594536606','2017-09-24 21:17:03',1),(15,'PA20170924212447843212',7,0.01,'2017-09-24 21:24:47','872735598905965047210','2017-09-24 21:25:28',1),(16,'PA20170924214326410966',7,0.01,'2017-09-24 21:43:26','810901974542119380806','2017-09-24 21:44:07',1),(17,'PM20170929131540801582',8,10.00,'2017-09-29 13:15:40','588925386366734268003','2017-09-29 13:22:37',1),(18,'PA20171001135629507323',8,0.01,'2017-10-01 13:56:29','539620007171287749107','2017-10-01 13:56:29',0),(19,'PA20171012231843387363',8,0.01,'2017-10-12 23:18:43','715174261744790685801','2017-10-12 23:18:43',0),(20,'PA20171016122716586951',9,1.00,'2017-10-16 12:27:16','814478744572288190400','2017-10-16 12:27:16',0),(21,'PM20171023084446663647',9,10.00,'2017-10-23 08:44:46','835153980856462285403','2017-10-23 08:50:21',1),(22,'PA20171023085044417100',9,20.00,'2017-10-23 08:50:44','523420710493233688905','2017-10-23 08:51:02',1),(23,'PA20171023085105103685',9,20.00,'2017-10-23 08:51:05','777264932177307281807','2017-10-23 08:51:05',0),(24,'PA20171025104912702616',9,1.00,'2017-10-25 10:49:12','760958566592851304301','2017-10-25 10:49:12',0);
/*!40000 ALTER TABLE `y_unionpaylog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_user`
--

DROP TABLE IF EXISTS `y_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(128) DEFAULT NULL COMMENT '登录名',
  `nickname` varchar(128) DEFAULT NULL COMMENT '昵称',
  `realname` varchar(128) DEFAULT NULL COMMENT '实名',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `salt` varchar(32) DEFAULT NULL COMMENT '盐',
  `deal_password` varchar(128) DEFAULT NULL COMMENT '【新增】交易密码',
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
  `faceimg` varchar(255) DEFAULT '' COMMENT '人脸照片',
  `status` varchar(32) DEFAULT 'normal' COMMENT '状态',
  `created` datetime DEFAULT NULL COMMENT '创建日期',
  `create_source` varchar(128) DEFAULT NULL COMMENT '用户来源（可能来之oauth第三方）',
  `logged` datetime DEFAULT NULL COMMENT '最后的登录时间',
  `activated` datetime DEFAULT NULL COMMENT '激活时间',
  `member_token` varchar(32) DEFAULT NULL COMMENT '【新增】用户令牌，由app使用',
  `device_token` varchar(60) DEFAULT NULL,
  `score` int(11) NOT NULL DEFAULT '0' COMMENT '积分',
  `overdue` tinyint(3) DEFAULT '0' COMMENT '【新增】是否逾期',
  `credit_record` text COMMENT '【新增】信息记录',
  `can_lend` tinyint(3) DEFAULT '1' COMMENT '【新增】是否有能够直接借出',
  `lat` decimal(7,5) DEFAULT '0.00000' COMMENT '【新增】纬度',
  `lng` decimal(8,5) DEFAULT '0.00000' COMMENT '【新增】经度',
  `books` text COMMENT '通讯录，json格式',
  `bankcard` varchar(45) DEFAULT '' COMMENT '银行卡号',
  `banktype` varchar(1024) DEFAULT '' COMMENT '发卡行',
  `zhima_score` int(10) DEFAULT '0' COMMENT '【新增】用户芝麻分',
  `auth_expire` datetime DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表，保存用户信息。';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_user`
--

LOCK TABLES `y_user` WRITE;
/*!40000 ALTER TABLE `y_user` DISABLE KEYS */;
INSERT INTO `y_user` VALUES (1,'admin','创始人','创始人','70f73a53980c9d07b7925d0d07ade8b516ddf632feee29992d066503f41150d5','448bda287017a3fbc2fac','',NULL,'6215714@qq.com',NULL,'18968596872','2',NULL,10980.00,3000,1,0,1,1,'1','administrator','','xxx',6,14,'6215714',NULL,'while1',NULL,NULL,'1984-07-23 00:00:00',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'/attachment/20170615/4b04908922884382b3f2362948ea54f4.jpg',NULL,'331081198407237618','','','','activited','2017-06-06 23:14:30',NULL,'2017-12-07 12:00:26',NULL,'uNcBhvseRhfhEglJMmHQVE9JSXa1GZhT','aaabbb',0,0,NULL,1,0.00000,0.00000,'m�','','',0,'2017-10-27 00:00:00',1,0,0,0,1,0,0,0,10.00),(2,'yjt','yjt','365借条','70f73a53980c9d07b7925d0d07ade8b516ddf632feee29992d066503f41150d5','448bda287017a3fbc2fac',NULL,NULL,'admin@yjt.com',NULL,'13012345678','1',NULL,0.00,3000,1,1,1,1,NULL,'administrator','user-list,user-view,user-edit,contract-list,withdraw-list,withdraw-pay,unionpaylog-list','none',1,2,'6215714',NULL,'aaa',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'331081199075123602','','','','activited','2017-06-20 23:33:04',NULL,'2017-12-05 16:20:47',NULL,'geZHCthskHSTgt8aeqg5LijepDeJ8F9j',NULL,0,0,NULL,1,0.00000,0.00000,NULL,'','',0,NULL,0,0,0,0,0,0,0,0,0.00),(3,'test01','test01nick','张三','c0e4dce021c864a00bb16a6108a837b0baa36642e8618751158d496ae341d0ee','33089ef544e73a','bb3fffe4fbbb8dfc81d1023cafb8cd1de4a3c006c93f7cea92b33c2efb3d1a1f','c83588e9595504','11@qq.com','y','13811111111','1','01011111111',9102.19,0,1,1,1,1,'1','visitor','contract-list','哥是个传说',0,0,'1111111','wechat_111',NULL,NULL,NULL,'2000-11-01 00:00:00',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'/attachment/api/20171005/c414c87f4bf943c98d9d81da15644761.jpeg',NULL,NULL,'','','','activited',NULL,NULL,NULL,NULL,'fC81WVjjSf0q1ysPwddZjwZfz0jVpBXB','ce0916d623d54952b78e9055c215571c',0,0,NULL,1,0.00000,0.00000,NULL,'','',0,'2018-01-01 00:00:00',1,1,1,1,1,1,1,1,0.00),(4,'test02','test02nick','李四','c0e4dce021c864a00bb16a6108a837b0baa36642e8618751158d496ae341d0ee','33089ef544e73a',NULL,NULL,'22@qq.com','y','13822222222','1','01022222222',0.00,0,1,1,1,1,'2','visitor','','姐是个传说',0,0,'22222222','wechat_111',NULL,NULL,NULL,'2000-11-02 00:00:00',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','','','activited',NULL,NULL,NULL,NULL,'uXoRLzDqKL6fzjhYrUH3wtqld9UEZzJx','cfe8948fc6864c71a36451877c2db14f',0,0,NULL,1,0.00000,0.00000,NULL,'','',0,NULL,0,0,0,0,0,0,0,0,0.00),(8,'lsw','吕布','吕思维','c0e4dce021c864a00bb16a6108a837b0baa36642e8618751158d496ae341d0ee','33089ef544e73a','bb3fffe4fbbb8dfc81d1023cafb8cd1de4a3c006c93f7cea92b33c2efb3d1a1f','c83588e9595504',NULL,NULL,'15068633525','1',NULL,158.54,200000,1,1,1,1,NULL,'visitor','',NULL,0,0,NULL,NULL,NULL,NULL,NULL,'1989-04-10 00:00:00',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'/attachment/api/20171005/a7f0ace0f2074a96979837f5ff19f38e.jpeg',NULL,'421083198904103595','/attachment/api/20170930/805e3a527dc146fa938bd6e99d397f15.jpg','','/attachment/api/20170930/a1a53a8ea9ad46fb808f97405bd5c09f.jpg','normal','2017-09-29 09:52:24',NULL,'2017-09-29 09:52:24',NULL,'P9nL1jomkGW1a6IpUNnDu19eSNFBSgwX','ce0916d623d54952b78e9055c215571c',0,0,NULL,1,29.20000,120.10000,'[{\"name\":\"姓名 1\",\"tel\":\"150-6863-3525\"},{\"name\":\"姓名2\",\"tel\":\"150-6863-3525\"}]','6214808801004324360','{\"errorCode\":0,\"result\":{\"bankname\":\"泰隆城市信用社\",\"banknum\":\"4733450\",\"cardlength\":\"19\",\"cardname\":\"借记IC卡\",\"cardprefixlength\":\"6\",\"cardprefixnum\":\"621480\",\"cardtype\":\"银联借记卡\",\"city\":\"台州市\",\"luhn\":true,\"province\":\"浙江省\"}}',0,'2017-10-30 00:00:00',1,0,0,0,1,0,1,0,0.00),(9,'zqz',NULL,'朱千增','eaa47b53e87f41a6f45792b3fbce3599c21e87de7a1821308512efa4396ab83e','94ead9a496150953bbef7',NULL,NULL,NULL,NULL,'13857665677','1',NULL,10.00,200000,1,1,1,1,NULL,'administrator','',NULL,0,0,NULL,NULL,NULL,NULL,NULL,'1982-11-16 00:00:00',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'/attachment/api/20171013/79903b39af2a4787b47aa2e3fe0fed08.jpeg',NULL,'331021198211163535','/attachment/api/20171016/3e8b060e673845adae8127ec4cf328d5.jpeg','/attachment/api/20171016/e8546ab3dec5470da342583deccdaf0d.jpeg','/attachment/api/20171016/baa9c736265d48d39e8681288ba5b490.jpeg','normal','2017-10-13 19:25:51',NULL,'2017-10-13 19:25:51',NULL,'3V4Nd9zK6xw5Z8VfZvgCdhjHEFfDAzYP','afc4a14246bf4431a83461e4b0b7145f',0,0,NULL,1,0.00000,0.00000,NULL,'6228581099025464660','{\"errorCode\":0,\"result\":{\"abbreviation\":\"ZJNX\",\"bankimage\":\"http://auth.apis.la/bank/114_ZJNX.png\",\"bankname\":\"浙江省农村信用社联合社\",\"banknum\":\"14293300\",\"bankurl\":\"\",\"cardlength\":\"19\",\"cardname\":\"丰收卡(银联卡)\",\"cardprefixlength\":\"6\",\"cardprefixnum\":\"622858\",\"cardtype\":\"银联借记卡\",\"city\":\"台州市\",\"enbankname\":\"\",\"luhn\":true,\"province\":\"浙江省\",\"servicephone\":\"\"}}',0,'2017-11-23 00:00:00',1,0,0,0,0,1,1,0,0.00),(10,'zz z',NULL,NULL,'6c52d94004927a4f732633d744bf569b748bdf6dabfb668257e911530b15f3dc','aee2b84db326e5b6',NULL,NULL,NULL,NULL,'13566444288','0',NULL,0.00,0,1,1,1,1,NULL,'visitor','',NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'/attachment/20171206/a3d8f267a20040569c25c6e2b0125276.png',NULL,NULL,'','','','normal','2017-10-16 09:41:26',NULL,'2017-10-16 09:41:26',NULL,'Zc4go0uXlAUOT8pNKHnCAgr6daSy0oX7',NULL,0,0,NULL,1,0.00000,0.00000,NULL,NULL,'',0,NULL,0,0,0,0,0,0,0,0,0.00),(11,'测试abcdef',NULL,NULL,NULL,'8ca5c7cd4140e8',NULL,NULL,NULL,NULL,'13012345666','1',NULL,0.00,0,1,1,1,1,'1','visitor','',NULL,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'','','','normal','2017-11-01 16:16:08',NULL,'2017-11-01 16:16:08',NULL,NULL,NULL,0,0,NULL,1,0.00000,0.00000,NULL,NULL,'',0,'2017-12-01 00:00:00',0,0,0,0,0,0,0,0,0.00);
/*!40000 ALTER TABLE `y_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `y_withdraw`
--

DROP TABLE IF EXISTS `y_withdraw`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `y_withdraw` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT '0' COMMENT '提出的用户',
  `type` tinyint(3) DEFAULT '0' COMMENT '提现类型，1微信， 2支付宝， 3银联',
  `money` decimal(8,2) DEFAULT '0.00' COMMENT '提现金额',
  `bank_account` varchar(45) DEFAULT '' COMMENT '银行卡号',
  `realname` varchar(12) DEFAULT '' COMMENT '真实姓名',
  `create_time` datetime DEFAULT NULL COMMENT '申请时间',
  `status` tinyint(3) DEFAULT '0' COMMENT '处理状态：0未处理， 1处理中，2处理成功， 3处理失败。注：处理失败时，提现暂扣款将冲回user.amount中。',
  `clerk` bigint(20) DEFAULT '0' COMMENT '操作员',
  `log` varchar(1024) DEFAULT '' COMMENT '处理说明',
  `code` varchar(1024) DEFAULT '',
  `update_time` datetime DEFAULT NULL COMMENT '工作人员操作的最后更新时间',
  `payed_time` datetime DEFAULT NULL COMMENT '到账时间，即银联回调成功的时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='提现申请';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `y_withdraw`
--

LOCK TABLES `y_withdraw` WRITE;
/*!40000 ALTER TABLE `y_withdraw` DISABLE KEYS */;
INSERT INTO `y_withdraw` VALUES (4,9,3,10.00,'1234567891035','林忠仁','2017-09-23 16:04:53',0,0,'','',NULL,NULL),(5,9,3,10.00,'1234567891035','林忠仁','2017-09-23 16:05:07',1,1,'\r\n测试','responseCode=0000&merId=808080211306315&merDate=20171028&merSeqId=11451363&cpDate=20171028&cpSeqId=328429&transAmt=100&stat=s&cardNo=6228581099025464660&chkValue=8270E1E042BB792D5C900C85068686CE20C4FCB56B0DAE0AA472FD345D57B4826A33008455ECCDB471C9C6837193A344579727197BCFCCF29174E42954C4B14A7B96111F3232A901468E447E0715B00C27224A010226263F4CCEE675C613FDAA9EF67D04FB0AD83AFEE2C1287B35624BDB0A5F98E8A263BC33A500E7CEBA8D61','2017-10-28 16:19:39','2017-10-28 16:19:39'),(6,8,3,1.00,'6214808801004324360','吕思维','2017-10-12 23:19:44',0,0,'\r\n测试\r\n测试','responseCode=0000&merId=808080211306315&merDate=20171028&merSeqId=64508262&cpDate=20171028&cpSeqId=139986&transAmt=100&stat=s&cardNo=6214808801004324360&chkValue=143A56ED4703F908503224D3D2BEDD8B0D6DA623BC09BF5F4A23859B1250771AE2E5A2F56BDE25DB6D3A80FFCC3580F53A7B83BBBC601B42C9D54BE7F4A1A4C7ECECC60364E9D32500110CAF68661595E1AAEA98DC5533A385164B5B8ED30DCFB14CD45C51A326C8E39D89317B587993BF2559DCEED8D28CDB8025B13EA93932','2017-10-28 14:25:19','2017-10-28 14:25:19');
/*!40000 ALTER TABLE `y_withdraw` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-07 12:58:05
