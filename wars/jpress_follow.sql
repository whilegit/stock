-- phpMyAdmin SQL Dump
-- version 4.4.15.10
-- https://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 14, 2017 at 03:30 PM
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
-- Table structure for table `jpress_follow`
--

CREATE TABLE IF NOT EXISTS `jpress_follow` (
  `id` int(11) NOT NULL,
  `followed_id` int(11) NOT NULL COMMENT '被关注者',
  `follower_id` int(11) NOT NULL COMMENT '关注者',
  `status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '状态，1有效，0无效',
  `change_time` datetime NOT NULL COMMENT '最后一次改变的时间'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COMMENT='关注表';

--
-- Dumping data for table `jpress_follow`
--

INSERT INTO `jpress_follow` (`id`, `followed_id`, `follower_id`, `status`, `change_time`) VALUES
(2, 2, 1, 1, '2017-08-14 23:14:44');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `jpress_follow`
--
ALTER TABLE `jpress_follow`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `jpress_follow`
--
ALTER TABLE `jpress_follow`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
