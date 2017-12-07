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
-- Table structure for table `jpress_unionpaylog`
--

DROP TABLE IF EXISTS `jpress_unionpaylog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jpress_unionpaylog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pay_sn` varchar(45) DEFAULT NULL COMMENT '充值的字符串序列化',
  `user_id` bigint(20) DEFAULT NULL COMMENT '发起充值的用户',
  `fee` int(10) DEFAULT NULL COMMENT '充值金额，单位元。 注意：银联支付接口的金额单位为分。',
  `create_time` datetime DEFAULT NULL COMMENT '充值创建时间',
  `tn` varchar(45) DEFAULT '' COMMENT '从银联返回的tn号，传给前端调起银联App控件用',
  `update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `status` tinyint(3) DEFAULT '0' COMMENT '0：未支付； 1：支付成功。支付成功后，就更新creditlog表，并增加用户的balance字段。',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='银联充值日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jpress_unionpaylog`
--

LOCK TABLES `jpress_unionpaylog` WRITE;
/*!40000 ALTER TABLE `jpress_unionpaylog` DISABLE KEYS */;
INSERT INTO `jpress_unionpaylog` VALUES (1,'PA20170915163251422484',1,10,'2017-09-15 16:32:51','771930382168988608000','2017-09-15 16:32:51',0);
/*!40000 ALTER TABLE `jpress_unionpaylog` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-09-15 16:34:25
