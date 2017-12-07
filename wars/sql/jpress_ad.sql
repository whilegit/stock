-- phpMyAdmin SQL Dump
-- version 4.4.15.10
-- https://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 03, 2017 at 02:26 PM
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
-- Table structure for table `jpress_ad`
--

CREATE TABLE IF NOT EXISTS `jpress_ad` (
  `id` bigint(20) NOT NULL,
  `img` varchar(255) CHARACTER SET utf8 NOT NULL,
  `url` varchar(255) CHARACTER SET utf8 NOT NULL,
  `dscrpt` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `order_num` int(10) NOT NULL DEFAULT '0' COMMENT '排序',
  `status` tinyint(3) NOT NULL DEFAULT '1' COMMENT '0／1'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `jpress_ad`
--

INSERT INTO `jpress_ad` (`id`, `img`, `url`, `dscrpt`, `order_num`, `status`) VALUES
(1, '/attachment/a.jpg', 'http://www.baidu.com/', 'aaa', 3, 1),
(2, '/attachment/b.jpg', 'http://www.sina.com.cn/', 'bbb', 1, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `jpress_ad`
--
ALTER TABLE `jpress_ad`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `jpress_ad`
--
ALTER TABLE `jpress_ad`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
