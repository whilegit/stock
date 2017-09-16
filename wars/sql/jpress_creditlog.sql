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
-- Table structure for table `jpress_creditlog`
--

DROP TABLE IF EXISTS `jpress_creditlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jpress_creditlog` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '变动的用户',
  `credit_type` tinyint(3) NOT NULL COMMENT '1余额,2积分',
  `platform` tinyint(3) DEFAULT '0' COMMENT '变动相关的平台,1: weixin, 2: alipay, 3: unionpay, 4: 365借条',
  `change` decimal(8,2) NOT NULL COMMENT '余额变动额',
  `cur` decimal(8,2) NOT NULL COMMENT '当前值',
  `clerk` bigint(20) NOT NULL COMMENT '操作的员工号，若为0则表示用户自己操作',
  `log` varchar(255) NOT NULL COMMENT '操作事项',
  `create_time` datetime NOT NULL COMMENT '日志生成时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='【新增】余额变动日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jpress_creditlog`
--

LOCK TABLES `jpress_creditlog` WRITE;
/*!40000 ALTER TABLE `jpress_creditlog` DISABLE KEYS */;
INSERT INTO `jpress_creditlog` VALUES (1,1,1,3,10.00,30.00,1,'测试','2017-09-16 11:49:00');
/*!40000 ALTER TABLE `jpress_creditlog` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-09-16 11:53:51
