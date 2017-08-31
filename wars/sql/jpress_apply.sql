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
-- Table structure for table `jpress_apply`
--

DROP TABLE IF EXISTS `jpress_apply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jpress_apply` (
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
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态，0无效，1有效，2已达成交易',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jpress_apply`
--

LOCK TABLES `jpress_apply` WRITE;
/*!40000 ALTER TABLE `jpress_apply` DISABLE KEYS */;
INSERT INTO `jpress_apply` VALUES (1,1,1000.00,18.00,0,NULL,'2017-10-31 00:00:00',7,'','23','','2017-08-27 00:54:05',1),(2,1,1000.00,18.00,0,NULL,'2017-10-31 00:00:00',7,'','23','','2017-08-27 00:56:34',1),(3,1,1000.00,18.00,0,NULL,'2017-10-31 00:00:00',7,'','23','','2017-08-27 00:56:49',1),(4,1,1000.00,18.00,0,NULL,'2017-10-31 00:00:00',7,'','2,3','','2017-08-27 00:58:40',1),(5,1,1000.00,18.00,0,NULL,'2017-10-31 00:00:00',7,'','2','','2017-08-27 01:11:18',1);
/*!40000 ALTER TABLE `jpress_apply` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-31 16:14:45
