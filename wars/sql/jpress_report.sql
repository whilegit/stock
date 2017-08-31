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
-- Table structure for table `jpress_report`
--

DROP TABLE IF EXISTS `jpress_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jpress_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_userid` bigint(20) DEFAULT NULL COMMENT '举报人',
  `to_userid` bigint(20) DEFAULT '0' COMMENT '被举报人',
  `content` text,
  `imgs` varchar(1024) DEFAULT '' COMMENT '图片，多个时以逗号分隔',
  `create_time` varchar(45) DEFAULT NULL,
  `status` tinyint(3) DEFAULT '0' COMMENT '0未处理， 1已处理',
  `deleted` tinyint(3) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jpress_report`
--

LOCK TABLES `jpress_report` WRITE;
/*!40000 ALTER TABLE `jpress_report` DISABLE KEYS */;
INSERT INTO `jpress_report` VALUES (1,1,0,'\'xxxx\'','','2017-08-31 14:47:10',0,0),(2,1,0,'xxxx','','2017-08-31 14:47:47',0,0),(3,1,0,'xxxx','/attachment/aaa.jpg','2017-08-31 14:48:34',0,0),(4,1,3,'xxxx','/attachment/aaa.jpg','2017-08-31 14:48:56',0,0);
/*!40000 ALTER TABLE `jpress_report` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-31 14:52:07
