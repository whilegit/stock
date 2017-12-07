-- phpMyAdmin SQL Dump
-- version 4.4.15.10
-- https://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 25, 2017 at 01:58 PM
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
-- Table structure for table `jpress_captcha`
--

CREATE TABLE IF NOT EXISTS `jpress_captcha` (
  `id` bigint(20) unsigned NOT NULL,
  `mobile` varchar(20) CHARACTER SET latin1 NOT NULL,
  `code` varchar(20) CHARACTER SET latin1 NOT NULL,
  `ip` varchar(50) NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='验证码表';

--
-- Dumping data for table `jpress_captcha`
--

INSERT INTO `jpress_captcha` (`id`, `mobile`, `code`, `ip`, `create_time`) VALUES
(2, '18968596872', '571377', '0:0:0:0:0:0:0:1', '2017-08-25 21:56:26'),
(3, '18968596872', '432701', '127.0.0.1', '2017-08-25 21:56:44');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `jpress_captcha`
--
ALTER TABLE `jpress_captcha`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `jpress_captcha`
--
ALTER TABLE `jpress_captcha`
  MODIFY `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
