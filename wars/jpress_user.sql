-- phpMyAdmin SQL Dump
-- version 4.4.15.10
-- https://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Aug 14, 2017 at 01:54 PM
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
-- Table structure for table `jpress_user`
--

CREATE TABLE IF NOT EXISTS `jpress_user` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键ID',
  `username` varchar(128) DEFAULT NULL COMMENT '登录名',
  `nickname` varchar(128) DEFAULT NULL COMMENT '昵称',
  `realname` varchar(128) DEFAULT NULL COMMENT '实名',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `salt` varchar(32) DEFAULT NULL COMMENT '盐',
  `email` varchar(64) DEFAULT NULL COMMENT '邮件',
  `email_status` varchar(32) DEFAULT NULL COMMENT '邮箱状态（是否认证等）',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机电话',
  `mobile_status` varchar(32) DEFAULT NULL COMMENT '手机状态（是否认证等）',
  `telephone` varchar(32) DEFAULT NULL COMMENT '固定电话',
  `amount` decimal(10,2) unsigned DEFAULT '0.00' COMMENT '金额（余额）',
  `gender` varchar(16) DEFAULT NULL COMMENT '性别',
  `role` varchar(32) DEFAULT 'visitor' COMMENT '权限',
  `perm` varchar(1024) NOT NULL DEFAULT '' COMMENT '【新增】权限',
  `signature` varchar(2048) DEFAULT NULL COMMENT '签名',
  `content_count` int(11) unsigned DEFAULT '0' COMMENT '内容数量',
  `comment_count` int(11) unsigned DEFAULT '0' COMMENT '评论数量',
  `qq` varchar(16) DEFAULT NULL COMMENT 'QQ号码',
  `wechat` varchar(32) DEFAULT NULL COMMENT '微信号',
  `weibo` varchar(256) DEFAULT NULL COMMENT '微博',
  `facebook` varchar(256) DEFAULT NULL,
  `linkedin` varchar(256) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `company` varchar(256) DEFAULT NULL COMMENT '公司',
  `occupation` varchar(256) DEFAULT NULL COMMENT '职位、职业',
  `address` varchar(256) DEFAULT NULL COMMENT '地址',
  `zipcode` varchar(128) DEFAULT NULL COMMENT '邮政编码',
  `site` varchar(256) DEFAULT NULL COMMENT '个人网址',
  `graduateschool` varchar(256) DEFAULT NULL COMMENT '毕业学校',
  `education` varchar(256) DEFAULT NULL COMMENT '学历',
  `avatar` varchar(256) DEFAULT NULL COMMENT '头像',
  `idcardtype` varchar(128) DEFAULT NULL COMMENT '证件类型：身份证 护照 军官证等',
  `idcard` varchar(128) DEFAULT NULL COMMENT '证件号码',
  `status` varchar(32) DEFAULT 'normal' COMMENT '状态',
  `created` datetime DEFAULT NULL COMMENT '创建日期',
  `create_source` varchar(128) DEFAULT NULL COMMENT '用户来源（可能来之oauth第三方）',
  `logged` datetime DEFAULT NULL COMMENT '最后的登录时间',
  `activated` datetime DEFAULT NULL COMMENT '激活时间',
  `member_token` varchar(32) DEFAULT NULL COMMENT '【新增】用户令牌，由app使用',
  `score` int(11) NOT NULL DEFAULT '0' COMMENT '积分'
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表，保存用户信息。';

--
-- Dumping data for table `jpress_user`
--

INSERT INTO `jpress_user` (`id`, `username`, `nickname`, `realname`, `password`, `salt`, `email`, `email_status`, `mobile`, `mobile_status`, `telephone`, `amount`, `gender`, `role`, `perm`, `signature`, `content_count`, `comment_count`, `qq`, `wechat`, `weibo`, `facebook`, `linkedin`, `birthday`, `company`, `occupation`, `address`, `zipcode`, `site`, `graduateschool`, `education`, `avatar`, `idcardtype`, `idcard`, `status`, `created`, `create_source`, `logged`, `activated`, `member_token`, `score`) VALUES
(1, 'yjt', 'testttestss', '林忠仁', '68f2741f481f237fe0d60b3d7faaa20f4807898cf1dfbdd756431b4f62146a27', '7acbd2b84e93e9ef4', '6215714@qq.com', NULL, '18968596872', NULL, NULL, '0.00', '1', 'administrator', 'perm-edit', 'xxx', 1, 0, '6215714', NULL, 'while1', NULL, NULL, '2000-07-11 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '/attachment/20170615/4b04908922884382b3f2362948ea54f4.jpg', NULL, NULL, 'activited', '2017-06-06 23:14:30', NULL, '2017-07-17 23:00:48', NULL, 'oa6UaYhgo8JPQNjG4srwMUvZdEo8C8sh', 0),
(2, 'admin', 'admin1-nick', NULL, '5a7612c7e12f83d58f1f185a6c32bae01c37cfb385f26129408ad9bcc6693b32', '7b68edf4fd70f076160', 'admin@yjt.com', NULL, '123456789', NULL, NULL, '0.00', NULL, 'visitor', 'user-edit', 'none', 1, 0, '6215714', NULL, 'aaa', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'activited', '2017-06-20 23:33:04', NULL, '2017-06-20 23:33:05', NULL, '', 0),
(3, 'test01', 'test01nick', '张三', '123456', 'abcd', '11@qq.com', 'y', '13811111111', 'y', '01011111111', '0.00', '1', 'visitor', 'contract-list', '哥是个传说', 0, 0, '1111111', 'wechat_111', NULL, NULL, NULL, '2000-11-01 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'activited', NULL, NULL, NULL, NULL, '', 0),
(4, 'test02', 'test02nick', '李四', '123456', 'abcd', '22@qq.com', 'y', '13822222222', 'y', '01022222222', '0.00', '2', 'visitor', '', '姐是个传说', 0, 0, '22222222', 'wechat_111', NULL, NULL, NULL, '2000-11-02 00:00:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'activited', NULL, NULL, NULL, NULL, '', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `jpress_user`
--
ALTER TABLE `jpress_user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `mobile` (`mobile`),
  ADD KEY `status` (`status`),
  ADD KEY `created` (`created`),
  ADD KEY `content_count` (`content_count`),
  ADD KEY `comment_count` (`comment_count`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `jpress_user`
--
ALTER TABLE `jpress_user`
  MODIFY `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',AUTO_INCREMENT=5;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
