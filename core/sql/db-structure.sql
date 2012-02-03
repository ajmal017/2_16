-- MySQL dump 10.13  Distrib 5.1.48, for Win64 (unknown)
--
-- Host: localhost    Database: algotrader
-- ------------------------------------------------------
-- Server version    5.1.48-community-log

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
-- Table structure for table `allocation`
--

DROP TABLE IF EXISTS `allocation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `allocation` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `QUANTITY` bigint(20) NOT NULL,
  `SECURITY_FK` int(11) NOT NULL,
  `COMBINATION_FK` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `SECURITY_FK_COMBINATION_FK` (`SECURITY_FK`,`COMBINATION_FK`),
  KEY `ALLOCATION_SECURITY_FKC` (`SECURITY_FK`),
  KEY `ALLOCATION_COMBINATION_FKC` (`COMBINATION_FK`),
  CONSTRAINT `ALLOCATION_SECURITY_FKC` FOREIGN KEY (`SECURITY_FK`) REFERENCES `security` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `ALLOCATION_COMBINATION_FKC` FOREIGN KEY (`COMBINATION_FK`) REFERENCES `combination` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ask`
--

DROP TABLE IF EXISTS `ask`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ask` (
  `ID` int(11) NOT NULL,
  `DATE_TIME` datetime NOT NULL,
  `SECURITY_FK` int(11) NOT NULL,
  `PRICE` decimal(12,5) NOT NULL,
  `SIZE` bigint(20) NOT NULL,
  `VALID` datetime NOT NULL,
  `EXT_ID` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `MARKET_DATA_EVENT_SECURITY_FKC268f53e449d465cfe59` (`SECURITY_FK`),
  KEY `MARKET_DATA_EVENT_SECURITY_FKC6fa0ac2466f3e7c17a79` (`SECURITY_FK`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bar`
--

DROP TABLE IF EXISTS `bar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bar` (
  `ID` int(11) NOT NULL,
  `DATE_TIME` datetime NOT NULL,
  `SECURITY_FK` int(11) NOT NULL,
  `OPEN` decimal(12,5) NOT NULL,
  `HIGH` decimal(12,5) NOT NULL,
  `LOW` decimal(12,5) NOT NULL,
  `CLOSE` decimal(12,5) NOT NULL,
  `ADJ_CLOSE` decimal(12,5) DEFAULT NULL,
  `VOL` int(11) NOT NULL,
  `OPEN_INTEREST` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `MARKET_DATA_EVENT_SECURITY_FKCfff3` (`SECURITY_FK`),
  KEY `MARKET_DATA_EVENT_SECURITY_FKC17c13` (`SECURITY_FK`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bid`
--

DROP TABLE IF EXISTS `bid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bid` (
  `ID` int(11) NOT NULL,
  `DATE_TIME` datetime NOT NULL,
  `SECURITY_FK` int(11) NOT NULL,
  `PRICE` decimal(12,5) NOT NULL,
  `SIZE` bigint(20) NOT NULL,
  `VALID` datetime NOT NULL,
  `EXT_ID` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `MARKET_DATA_EVENT_SECURITY_FKC268f53e449d465c100dd` (`SECURITY_FK`),
  KEY `MARKET_DATA_EVENT_SECURITY_FKC6fa0ac2466f3e7c17cfd` (`SECURITY_FK`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cash_balance`
--

DROP TABLE IF EXISTS `cash_balance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cash_balance` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CURRENCY` enum('CHF','EUR','USD','GBP') NOT NULL,
  `AMOUNT` decimal(15,2) NOT NULL,
  `STRATEGY_FK` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `CURRENCY_STRATEGY_FK` (`CURRENCY`,`STRATEGY_FK`),
  KEY `CASH_BALANCE_STRATEGY_FKC` (`STRATEGY_FK`),
  CONSTRAINT `CASH_BALANCE_STRATEGY_FKC` FOREIGN KEY (`STRATEGY_FK`) REFERENCES `strategy` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `combination`
--

DROP TABLE IF EXISTS `combination`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `combination` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE` enum('VERTICAL_SPREAD','COVERED_CALL','RATIO_SPREAD','STRADDLE','STRANGLE','BUTTERFLY','CALENDAR_SPREAD','IRON_CONDOR') NOT NULL,
  `EXIT_VALUE` double DEFAULT NULL,
  `PROFIT_TARGET` double DEFAULT NULL,
  `STRATEGY_FK` int(11) NOT NULL,
  `MASTER_FK` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `STRATEGY_FK_MASTER_FK` (`STRATEGY_FK`,`MASTER_FK`),
  KEY `COMBINATION_MASTER_FKC` (`MASTER_FK`),
  KEY `COMBINATION_STRATEGY_FKC` (`STRATEGY_FK`),
  CONSTRAINT `COMBINATION_MASTER_FKC` FOREIGN KEY (`MASTER_FK`) REFERENCES `security` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `COMBINATION_STRATEGY_FKC` FOREIGN KEY (`STRATEGY_FK`) REFERENCES `strategy` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `equity_index`
--

DROP TABLE IF EXISTS `equity_index`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `equity_index` (
  `ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `EQUITY_INDEXIFKC` (`ID`),
  CONSTRAINT `EQUITY_INDEXIFKC` FOREIGN KEY (`ID`) REFERENCES `security` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `forex`
--

DROP TABLE IF EXISTS `forex`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forex` (
  `ID` int(11) NOT NULL,
  `BASE_CURRENCY` enum('CHF','EUR','USD','GBP') NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FOREXIFKC` (`ID`),
  CONSTRAINT `FOREXIFKC` FOREIGN KEY (`ID`) REFERENCES `security` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `future`
--

DROP TABLE IF EXISTS `future`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `future` (
  `ID` int(11) NOT NULL,
  `EXPIRATION` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FUTUREIFKC` (`ID`),
  KEY `EXPIRATION` (`EXPIRATION`),
  CONSTRAINT `FUTUREIFKC` FOREIGN KEY (`ID`) REFERENCES `security` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `future_family`
--

DROP TABLE IF EXISTS `future_family`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `future_family` (
  `ID` int(11) NOT NULL,
  `LENGTH` int(11) NOT NULL,
  `INTREST` double NOT NULL,
  `DIVIDEND` double NOT NULL,
  `MARGIN_PARAMETER` double NOT NULL,
  `EXPIRATION_TYPE` enum('NEXT_3_RD_FRIDAY','NEXT_3_RD_FRIDAY_3_MONTHS','THIRTY_DAYS_BEFORE_NEXT_3_RD_FRIDAY') NOT NULL,
  `EXPIRATION_MONTHS` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FUTURE_FAMILYIFKC` (`ID`),
  CONSTRAINT `FUTURE_FAMILYIFKC` FOREIGN KEY (`ID`) REFERENCES `security_family` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `generic_future`
--

DROP TABLE IF EXISTS `generic_future`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `generic_future` (
  `ID` int(11) NOT NULL,
  `DURATION` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `GENERIC_FUTUREIFKC` (`ID`),
  KEY `DURATION` (`DURATION`),
  CONSTRAINT `GENERIC_FUTUREIFKC` FOREIGN KEY (`ID`) REFERENCES `security` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `generic_future_family`
--

DROP TABLE IF EXISTS `generic_future_family`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `generic_future_family` (
  `ID` int(11) NOT NULL,
  `EXPIRATION_TYPE` enum('NEXT_3_RD_FRIDAY','NEXT_3_RD_FRIDAY_3_MONTHS','THIRTY_DAYS_BEFORE_NEXT_3_RD_FRIDAY') NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `GENERIC_FUTURE_FAMILYIFKC` (`ID`),
  CONSTRAINT `GENERIC_FUTURE_FAMILYIFKC` FOREIGN KEY (`ID`) REFERENCES `security_family` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `TIME` datetime NOT NULL,
  `TBL` varchar(255) NOT NULL,
  `REF_ID` int(11) NOT NULL,
  `COL` varchar(255) DEFAULT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `TBL` (`TBL`),
  KEY `REF_ID` (`REF_ID`),
  KEY `TIME` (`TIME`),
  KEY `COL` (`COL`)
) ENGINE=InnoDB AUTO_INCREMENT=8719 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `implied_volatility`
--

DROP TABLE IF EXISTS `implied_volatility`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `implied_volatility` (
  `ID` int(11) NOT NULL,
  `DURATION` int(11) NOT NULL,
  `MONEYNESS` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IMPLIED_VOLATILITYIFKC` (`ID`),
  CONSTRAINT `IMPLIED_VOLATILITYIFKC` FOREIGN KEY (`ID`) REFERENCES `security` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `intrest_rate`
--

DROP TABLE IF EXISTS `intrest_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `intrest_rate` (
  `ID` int(11) NOT NULL,
  `DURATION` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `INTREST_RATEIFKC` (`ID`),
  CONSTRAINT `INTREST_RATEIFKC` FOREIGN KEY (`ID`) REFERENCES `security` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `position`
--

DROP TABLE IF EXISTS `position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `position` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `QUANTITY` bigint(20) NOT NULL,
  `EXIT_VALUE` double DEFAULT NULL,
  `PROFIT_TARGET` double DEFAULT NULL,
  `MAINTENANCE_MARGIN` decimal(15,2) DEFAULT NULL,
  `SECURITY_FK` int(11) NOT NULL,
  `STRATEGY_FK` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `SECURITY_FK_STRATEGY_FK` (`SECURITY_FK`,`STRATEGY_FK`),
  KEY `QUANTITY` (`QUANTITY`),
  KEY `POSITION_SECURITY_FKC` (`SECURITY_FK`),
  KEY `POSITION_STRATEGY_FKC` (`STRATEGY_FK`),
  CONSTRAINT `POSITION_SECURITY_FKC` FOREIGN KEY (`SECURITY_FK`) REFERENCES `security` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `POSITION_STRATEGY_FKC` FOREIGN KEY (`STRATEGY_FK`) REFERENCES `strategy` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=196 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `saldo`
--

DROP TABLE IF EXISTS `saldo`;
/*!50001 DROP VIEW IF EXISTS `saldo`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `saldo` (
  `date_time` datetime,
  `type` enum('BUY','SELL','EXPIRATION','CREDIT','DEBIT','INTREST_PAID','INTREST_RECEIVED','FEES','REFUND','REBALANCE'),
  `symbol` varchar(30),
  `isin` varchar(20),
  `position_fk` int(11),
  `STRIKE` decimal(12,5),
  `expiration` datetime,
  `quantity` bigint(20),
  `price` decimal(12,5),
  `commission` decimal(15,2),
  `saldo` decimal(64,5)
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `security`
--

DROP TABLE IF EXISTS `security`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `SYMBOL` varchar(30) NOT NULL,
  `ISIN` varchar(20) DEFAULT NULL,
  `EXT_ID` varchar(30) DEFAULT NULL,
  `UNDERLAYING_FK` int(11) DEFAULT NULL,
  `SECURITY_FAMILY_FK` int(11) NOT NULL,
  `INTREST_RATE_FAMILY_FK` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `SYMBOL` (`SYMBOL`),
  UNIQUE KEY `ISIN` (`ISIN`),
  KEY `SECURITY_INTREST_RATE_FAMILY_C` (`INTREST_RATE_FAMILY_FK`),
  KEY `SECURITY_SECURITY_FAMILY_FKC` (`SECURITY_FAMILY_FK`),
  KEY `SECURITY_UNDERLAYING_FKC` (`UNDERLAYING_FK`),
  CONSTRAINT `SECURITY_UNDERLAYING_FKC` FOREIGN KEY (`UNDERLAYING_FK`) REFERENCES `security` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `SECURITY_INTREST_RATE_FAMILY_C` FOREIGN KEY (`INTREST_RATE_FAMILY_FK`) REFERENCES `security_family` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `SECURITY_SECURITY_FAMILY_FKC` FOREIGN KEY (`SECURITY_FAMILY_FK`) REFERENCES `security_family` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=40093 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `security_family`
--

DROP TABLE IF EXISTS `security_family`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_family` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `MARKET` enum('SOFFEX','DTB','IDEALPRO','CBOE','SMART','CFE','GLOBEX','NYMEX','CME','NYSE','NASDAQ') NOT NULL,
  `CURRENCY` enum('CHF','EUR','USD') NOT NULL,
  `CONTRACT_SIZE` int(11) NOT NULL,
  `SCALE` int(11) DEFAULT NULL,
  `TICK_SIZE_PATTERN` varchar(255) NOT NULL,
  `COMMISSION` decimal(12,5) DEFAULT NULL,
  `MARKET_OPEN` time NOT NULL,
  `MARKET_CLOSE` time NOT NULL,
  `TRADEABLE` bit(1) DEFAULT NULL,
  `SIMULATABLE` bit(1) NOT NULL,
  `SPREAD_SLOPE` double DEFAULT NULL,
  `SPREAD_CONSTANT` double DEFAULT NULL,
  `MAX_SPREAD_SLOPE` double DEFAULT NULL,
  `MAX_SPREAD_CONSTANT` double DEFAULT NULL,
  `PERIODICITY` enum('MINUTE','HOUR','DAY') NOT NULL,
  `UNDERLAYING_FK` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `SECURITY_FAMILY_UNDERLAYING_FC` (`UNDERLAYING_FK`),
  CONSTRAINT `SECURITY_FAMILY_UNDERLAYING_FC` FOREIGN KEY (`UNDERLAYING_FK`) REFERENCES `security` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock`
--

DROP TABLE IF EXISTS `stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stock` (
  `ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `STOCKIFKC` (`ID`),
  CONSTRAINT `STOCKIFKC` FOREIGN KEY (`ID`) REFERENCES `security` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock_option`
--

DROP TABLE IF EXISTS `stock_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stock_option` (
  `ID` int(11) NOT NULL,
  `STRIKE` decimal(12,5) NOT NULL,
  `EXPIRATION` datetime NOT NULL,
  `TYPE` enum('CALL','PUT') NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `STRIKE` (`STRIKE`),
  KEY `EXPIRATION` (`EXPIRATION`),
  KEY `TYPE` (`TYPE`),
  KEY `STOCK_OPTIONIFKC` (`ID`),
  CONSTRAINT `STOCK_OPTIONIFKC` FOREIGN KEY (`ID`) REFERENCES `security` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock_option_family`
--

DROP TABLE IF EXISTS `stock_option_family`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stock_option_family` (
  `ID` int(11) NOT NULL,
  `STRIKE_DISTANCE` double NOT NULL,
  `INTREST` double NOT NULL,
  `DIVIDEND` double NOT NULL,
  `MARGIN_PARAMETER` double NOT NULL,
  `EXPIRATION_TYPE` enum('NEXT_3_RD_FRIDAY','NEXT_3_RD_FRIDAY_3_MONTHS','THIRTY_DAYS_BEFORE_NEXT_3_RD_FRIDAY') NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `STOCK_OPTION_FAMILYIFKC` (`ID`),
  CONSTRAINT `STOCK_OPTION_FAMILYIFKC` FOREIGN KEY (`ID`) REFERENCES `security_family` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `strategy`
--

DROP TABLE IF EXISTS `strategy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `strategy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(30) NOT NULL,
  `FAMILY` varchar(20) NOT NULL,
  `AUTO_ACTIVATE` bit(1) NOT NULL,
  `ALLOCATION` double NOT NULL,
  `MODULES` varchar(255) NOT NULL,
  `BENCHMARK` decimal(15,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tick`
--

DROP TABLE IF EXISTS `tick`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tick` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `DATE_TIME` datetime NOT NULL,
  `LAST` decimal(12,5) DEFAULT NULL,
  `LAST_DATE_TIME` datetime DEFAULT NULL,
  `VOL` int(11) NOT NULL,
  `VOL_BID` int(11) NOT NULL,
  `VOL_ASK` int(11) NOT NULL,
  `BID` decimal(12,5) NOT NULL,
  `ASK` decimal(12,5) NOT NULL,
  `OPEN_INTREST` int(11) NOT NULL,
  `SETTLEMENT` decimal(12,5) DEFAULT NULL,
  `SECURITY_FK` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `DATE_TIME_SECURITY_FK` (`DATE_TIME`,`SECURITY_FK`),
  KEY `DATE_TIME` (`DATE_TIME`),
  KEY `MARKET_DATA_EVENT_SECURITY_FKC36519d` (`SECURITY_FK`)
) ENGINE=MyISAM AUTO_INCREMENT=364965 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trade`
--

DROP TABLE IF EXISTS `trade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trade` (
  `ID` int(11) NOT NULL,
  `DATE_TIME` datetime NOT NULL,
  `SECURITY_FK` int(11) NOT NULL,
  `PRICE` decimal(12,5) NOT NULL,
  `SIZE` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `MARKET_DATA_EVENT_SECURITY_FKC268f53e44c5f944` (`SECURITY_FK`),
  KEY `MARKET_DATA_EVENT_SECURITY_FKC6fa0ac24697f164` (`SECURITY_FK`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `EXT_ID` varchar(30) DEFAULT NULL,
  `DATE_TIME` datetime NOT NULL,
  `QUANTITY` bigint(20) NOT NULL,
  `PRICE` decimal(12,5) NOT NULL,
  `COMMISSION` decimal(15,2) DEFAULT NULL,
  `CURRENCY` enum('CHF','EUR','USD','GBP') NOT NULL,
  `TYPE` enum('BUY','SELL','EXPIRATION','CREDIT','DEBIT','INTREST_PAID','INTREST_RECEIVED','FEES','REFUND','REBALANCE') NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `SECURITY_FK` int(11) DEFAULT NULL,
  `STRATEGY_FK` int(11) DEFAULT NULL,
  `POSITION_FK` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `DATE_TIME_PRICE_TYPE_DESCRIPTION` (`DATE_TIME`,`PRICE`,`TYPE`,`DESCRIPTION`),
  UNIQUE KEY `EXT_ID` (`EXT_ID`),
  KEY `CURRENCY` (`CURRENCY`),
  KEY `DATE_TIME` (`DATE_TIME`),
  KEY `TRANSACTION_POSITION_FKC` (`POSITION_FK`),
  KEY `TRANSACTION_SECURITY_FKC` (`SECURITY_FK`),
  KEY `TRANSACTION_STRATEGY_FKC` (`STRATEGY_FK`),
  CONSTRAINT `TRANSACTION_STRATEGY_FKC` FOREIGN KEY (`STRATEGY_FK`) REFERENCES `strategy` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `TRANSACTION_POSITION_FKC` FOREIGN KEY (`POSITION_FK`) REFERENCES `position` (`id`),
  CONSTRAINT `TRANSACTION_SECURITY_FKC` FOREIGN KEY (`SECURITY_FK`) REFERENCES `security` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=707 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `watch_list_item`
--

DROP TABLE IF EXISTS `watch_list_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `watch_list_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `PERSISTENT` bit(1) NOT NULL,
  `SECURITY_FK` int(11) NOT NULL,
  `STRATEGY_FK` int(11) NOT NULL,
  `UPPER_ALERT_VALUE` double DEFAULT NULL,
  `LOWER_ALERT_VALUE` double DEFAULT NULL,
  `AMOUNT` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `STRATEGY_SECURITY_UNIQUE` (`SECURITY_FK`,`STRATEGY_FK`),
  KEY `WATCH_LIST_ITEM_SECURITY_FKC` (`SECURITY_FK`),
  KEY `WATCH_LIST_ITEM_STRATEGY_FKC` (`STRATEGY_FK`),
  KEY `PERSISTENT` (`PERSISTENT`),
  CONSTRAINT `WATCH_LIST_ITEM_SECURITY_FKC` FOREIGN KEY (`SECURITY_FK`) REFERENCES `security` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `WATCH_LIST_ITEM_STRATEGY_FKC` FOREIGN KEY (`STRATEGY_FK`) REFERENCES `strategy` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=170735 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Final view structure for view `saldo`
--

/*!50001 DROP TABLE IF EXISTS `saldo`*/;
/*!50001 DROP VIEW IF EXISTS `saldo`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = latin1 */;
/*!50001 SET character_set_results     = latin1 */;
/*!50001 SET collation_connection      = latin1_swedish_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `saldo` AS select `t1`.`DATE_TIME` AS `date_time`,`t1`.`TYPE` AS `type`,`s1`.`SYMBOL` AS `symbol`,`s1`.`ISIN` AS `isin`,`t1`.`POSITION_FK` AS `position_fk`,`o1`.`STRIKE` AS `STRIKE`,`o1`.`EXPIRATION` AS `expiration`,`t1`.`QUANTITY` AS `quantity`,`t1`.`PRICE` AS `price`,`t1`.`COMMISSION` AS `commission`,(select sum((case `t2`.`TYPE` when 'CREDIT' then `t2`.`PRICE` when 'DEBIT' then `t2`.`PRICE` else (((-(`t2`.`QUANTITY`) * `t2`.`PRICE`) * `f2`.`CONTRACT_SIZE`) - `t2`.`COMMISSION`) end)) from ((`transaction` `t2` left join `security` `s2` on((`t2`.`SECURITY_FK` = `s2`.`id`))) left join `security_family` `f2` on((`s2`.`SECURITY_FAMILY_FK` = `f2`.`id`))) where (`t2`.`id` <= `t1`.`id`)) AS `saldo` from ((`transaction` `t1` left join `security` `s1` on((`t1`.`SECURITY_FK` = `s1`.`id`))) left join `stock_option` `o1` on((`o1`.`ID` = `s1`.`id`))) order by `t1`.`id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-01-31 17:02:09