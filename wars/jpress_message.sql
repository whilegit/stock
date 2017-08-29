-- phpMyAdmin SQL Dump
-- version 4.4.15.10
-- https://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 29, 2017 at 03:14 PM
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
-- Table structure for table `jpress_message`
--

CREATE TABLE IF NOT EXISTS `jpress_message` (
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
  `deleted` tinyint(3) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `jpress_message`
--

INSERT INTO `jpress_message` (`id`, `to_userid`, `from_userid`, `type`, `content`, `is_read`, `read_time`, `is_action`, `act`, `contract_id`, `apply_id`, `url`, `create_time`, `deleted`) VALUES
(1, 1, 2, 1, '<div style="color:red;">第一条信息哦</div>', 0, '2017-08-30 00:00:00', 0, '', 1, 1, '', '2017-08-29 00:00:00', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `jpress_message`
--
ALTER TABLE `jpress_message`
  ADD PRIMARY KEY (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
