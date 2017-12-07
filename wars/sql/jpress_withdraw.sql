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
-- Table structure for table `jpress_withdraw`
--

DROP TABLE IF EXISTS `jpress_withdraw`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jpress_withdraw` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT '0' COMMENT '提出的用户',
  `type` tinyint(3) DEFAULT '0' COMMENT '提现类型，1微信， 2支付宝， 3银联',
  `money` int(10) DEFAULT '0' COMMENT '提现金额',
  `bank_account` varchar(45) DEFAULT '' COMMENT '银行卡号',
  `realname` varchar(12) DEFAULT '' COMMENT '真实姓名',
  `create_time` datetime DEFAULT NULL COMMENT '申请时间',
  `status` tinyint(3) DEFAULT '0' COMMENT '处理状态：0未处理， 1处理中，2处理成功， 3处理失败。注：处理失败时，提现暂扣款将冲回user.amount中。',
  `clerk` bigint(20) DEFAULT '0' COMMENT '操作员',
  `log` varchar(255) DEFAULT '' COMMENT '处理说明',
  `update_time` datetime DEFAULT NULL COMMENT '工作人员操作的最后更新时间',
  `payed_time` datetime DEFAULT NULL COMMENT '到账时间，即银联回调成功的时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='提现申请';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jpress_withdraw`
--

LOCK TABLES `jpress_withdraw` WRITE;
/*!40000 ALTER TABLE `jpress_withdraw` DISABLE KEYS */;
INSERT INTO `jpress_withdraw` VALUES (5,1,3,10,'1234567891035','林忠仁','2017-09-23 16:05:07',0,0,'',NULL,NULL),(4,1,3,10,'1234567891035','林忠仁','2017-09-23 16:04:53',0,0,'',NULL,NULL);
/*!40000 ALTER TABLE `jpress_withdraw` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-09-23 16:12:09
