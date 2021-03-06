-- phpMyAdmin SQL Dump
-- version 4.4.15.10
-- https://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 05, 2017 at 04:11 PM
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
-- Table structure for table `jpress_apply`
--

CREATE TABLE IF NOT EXISTS `jpress_apply` (
  `id` bigint(20) NOT NULL,
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
  `contract_id` bigint(20) NOT NULL COMMENT '已达成交易的contractID',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态，0无效，1有效，2已达成交易'
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `jpress_apply`
--

INSERT INTO `jpress_apply` (`id`, `apply_uid`, `amount`, `annual_rate`, `repayment_method`, `value_date`, `maturity_date`, `purpose`, `description`, `to_friends`, `video`, `create_time`, `contract_id`, `status`) VALUES
(1, 1, '1000.00', '18.00', 0, NULL, '2017-10-31 00:00:00', 7, '', '1,2,3', '', '2017-08-27 00:54:05', 0, 1),
(2, 1, '1000.00', '18.00', 0, NULL, '2017-10-31 00:00:00', 7, '', '23,1', '', '2017-08-27 00:56:34', 0, 1),
(3, 1, '1000.00', '18.00', 0, NULL, '2017-10-31 00:00:00', 7, '', '23', '', '2017-08-27 00:56:49', 0, 1),
(4, 1, '1000.00', '18.00', 0, NULL, '2017-10-31 00:00:00', 7, '', '2,3', '', '2017-08-27 00:58:40', 0, 1),
(5, 1, '1000.00', '18.00', 0, NULL, '2017-10-31 00:00:00', 7, '', '2', '', '2017-08-27 01:11:18', 0, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `jpress_apply`
--
ALTER TABLE `jpress_apply`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `jpress_apply`
--
ALTER TABLE `jpress_apply`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
