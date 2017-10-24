CREATE DATABASE  IF NOT EXISTS `accounts`;
USE `accounts`;
--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
INSERT INTO `role` VALUES (1,'ROLE_USER');
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `fk_user_role_roleid_idx` (`role_id`),
  CONSTRAINT `fk_user_role_roleid` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_user_role_userid` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`product_id`),
  `pzn` varchar(255) DEFAULT NULL,
  `germanname` varchar(255) DEFAULT NULL,
  `chinesename` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `productorder`;
CREATE TABLE `productorder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  `shopname` varchar(255) DEFAULT NULL,
  `tourguidename` varchar(255) DEFAULT NULL,
  `tourguideid` varchar(255) DEFAULT NULL,
  `touristname` varchar(255) DEFAULT NULL,
  `orderdate`  date DEFAULT NULL,
  `pickupdate`  date DEFAULT NULL,
  `pickuptime`  varchar(255) DEFAULT NULL,
  `ordernumber`  varchar(255) DEFAULT NULL,
  `totalprice` DECIMAL(7,2) DEFAULT NULL,
  `creator`    varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `status`   varchar(255) DEFAULT NULL
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `orderdetail`;
CREATE TABLE `orderdetail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  `orderid` int(11) NOT NULL,
  CONSTRAINT `fk_orderdetail_orderid` FOREIGN KEY (`orderid`) REFERENCES `productorder` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  `pzn` varchar(255) DEFAULT NULL,
  `germanname` varchar(255) DEFAULT NULL,
  `chinesename` varchar(255) DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;




---
 mysql -u root -p
-- Password1234*
 use accounts;
 show tables;

--
export MAVEN_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"