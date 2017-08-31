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
-- Table structure for table `jpress_feedback`
--

DROP TABLE IF EXISTS `jpress_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jpress_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) DEFAULT '0',
  `name` varchar(20) DEFAULT '',
  `tel` varchar(20) DEFAULT '',
  `content` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jpress_feedback`
--

LOCK TABLES `jpress_feedback` WRITE;
/*!40000 ALTER TABLE `jpress_feedback` DISABLE KEYS */;
INSERT INTO `jpress_feedback` VALUES (1,1,'','','xxxx','2017-08-31 15:41:05'),(2,1,'','','xxxx','2017-08-31 15:41:23'),(3,1,'','18968596872','xxxx','2017-08-31 15:41:29'),(4,1,'a','18968596872','xxxx','2017-08-31 15:41:37'),(5,2,'a','18968596872','xxxx','2017-08-31 15:42:13'),(6,2,'a','18968596872','xxxx','2017-08-31 15:43:09'),(7,1,'a','18968596872','xxxx','2017-08-31 15:49:27'),(8,1,'a','18968596872','xxxx','2017-08-31 15:49:34'),(9,0,'a','18968596872','xxxx','2017-08-31 15:49:43'),(10,0,'a','18968596872','xxxx','2017-08-31 15:50:32'),(11,1,'a','18968596872','xxxx','2017-08-31 15:51:14');
/*!40000 ALTER TABLE `jpress_feedback` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-31 15:53:40
