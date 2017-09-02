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
-- Table structure for table `jpress_contract`
--

DROP TABLE IF EXISTS `jpress_contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jpress_contract` (
  `id` bigint(20) NOT NULL,
  `contract_number` varchar(64) CHARACTER SET utf8mb4 NOT NULL COMMENT '协义自定义编号',
  `contract_type_id` int(10) unsigned NOT NULL COMMENT '合同范本编号',
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
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态，0合约初订立（贷方资金冻结），1风控一批准，2风控二批准，3风控三批准，4资金划转前关闭，5资金划转成功贷款正式进入还款期，6正常结束，7展期, 8损失,',
  `repayment_status` tinyint(11) unsigned NOT NULL DEFAULT '0' COMMENT '五级不良分类：0不适用，1付息正常，2付息关注，3付息次级，4付息可疑，5损失',
  `update_time` datetime DEFAULT NULL COMMENT '数据更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='借贷合约，描述借贷发生';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jpress_contract`
--

LOCK TABLES `jpress_contract` WRITE;
/*!40000 ALTER TABLE `jpress_contract` DISABLE KEYS */;
INSERT INTO `jpress_contract` VALUES (20,'CN20170901172929974120',1,1,2,1500.00,18.00,3,'2017-09-01 17:29:29','2017-09-01 17:29:29',10,'2017-10-31 00:00:00',0.00,0.00,0.00,0,NULL,0,NULL,0,NULL,5,1,NULL),(21,'CN20170901173342523410',1,4,2,1000.00,18.00,3,'2017-09-01 17:33:42','2017-09-01 17:33:42',0,'2017-10-31 00:00:00',0.00,0.00,0.00,0,NULL,0,NULL,0,NULL,5,1,NULL),(22,'CN20170901173959144290',1,3,2,1000.00,18.00,3,'2017-09-01 17:39:59','2017-09-01 17:39:59',0,'2017-10-31 00:00:00',0.00,0.00,0.00,0,NULL,0,NULL,0,NULL,5,1,NULL),(23,'CN20170901174302360222',1,1,2,2000.00,18.00,3,'2017-09-01 17:43:02','2017-09-01 17:43:02',60,'2017-10-31 00:00:00',0.00,0.00,0.00,0,NULL,0,NULL,0,NULL,5,1,NULL);
/*!40000 ALTER TABLE `jpress_contract` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-09-02  9:54:12
