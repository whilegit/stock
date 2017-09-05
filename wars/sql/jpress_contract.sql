-- phpMyAdmin SQL Dump
-- version 4.4.15.10
-- https://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 05, 2017 at 04:10 PM
-- Server version: 5.7.18
-- PHP Version: 5.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `jpress`
--

-- --------------------------------------------------------

--
-- Table structure for table `jpress_contract`
--

CREATE TABLE IF NOT EXISTS `jpress_contract` (
  `id` bigint(20) NOT NULL,
  `contract_number` varchar(64) CHARACTER SET utf8mb4 NOT NULL COMMENT '协义自定义编号',
  `contract_type_id` int(10) unsigned NOT NULL COMMENT '合同范本编号',
  `apply_id` bigint(20) NOT NULL COMMENT '申请号',
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
  `update_time` datetime DEFAULT NULL COMMENT '数据更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='借贷合约，描述借贷发生';

--
-- Dumping data for table `jpress_contract`
--

INSERT INTO `jpress_contract` (`id`, `contract_number`, `contract_type_id`, `apply_id`, `credit_id`, `debit_id`, `amount`, `annual_rate`, `repayment_method`, `create_time`, `value_date`, `loan_term`, `maturity_date`, `fee1`, `fee2`, `fee3`, `approval_u1`, `approval_time1`, `approval_u2`, `approval_time2`, `approval_u3`, `approval_time3`, `status`, `repayment_status`, `update_time`) VALUES
(20, 'CN20170901172929974120', 1, 0, 1, 2, '1500.00', '18.00', 3, '2017-09-01 17:29:29', '2017-09-01 17:29:29', 10, '2017-10-31 00:00:00', 0.00, 0.00, 0.00, 0, NULL, 0, NULL, 0, NULL, 5, 1, NULL),
(21, 'CN20170901173342523410', 1, 0, 4, 2, '1000.00', '18.00', 3, '2017-09-01 17:33:42', '2017-09-01 17:33:42', 0, '2017-10-31 00:00:00', 0.00, 0.00, 0.00, 0, NULL, 0, NULL, 0, NULL, 5, 1, NULL),
(22, 'CN20170901173959144290', 1, 0, 3, 2, '1000.00', '18.00', 3, '2017-09-01 17:39:59', '2017-09-01 17:39:59', 0, '2017-10-31 00:00:00', 0.00, 0.00, 0.00, 0, NULL, 0, NULL, 0, NULL, 5, 1, NULL),
(23, 'CN20170901174302360222', 1, 0, 1, 2, '2000.00', '18.00', 3, '2017-09-01 17:43:02', '2017-09-01 17:43:02', 60, '2017-10-31 00:00:00', 0.00, 0.00, 0.00, 0, NULL, 0, NULL, 0, NULL, 5, 1, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `jpress_contract`
--
ALTER TABLE `jpress_contract`
  ADD PRIMARY KEY (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
