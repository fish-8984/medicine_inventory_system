-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: intelligent_dim_system
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `audit_logs`
--

DROP TABLE IF EXISTS `audit_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_logs` (
  `log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` int NOT NULL COMMENT '操作用户',
  `action` varchar(100) COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '操作类型',
  `target_table` varchar(100) COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '目标表名',
  `target_id` varchar(100) COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '目标记录ID',
  `change_details` json DEFAULT NULL COMMENT '变更详情（JSON格式）',
  `action_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`log_id`),
  KEY `idx_action_date` (`action_date`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=361 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_croatian_ci COMMENT='系统操作审计日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_logs`
--

LOCK TABLES `audit_logs` WRITE;
/*!40000 ALTER TABLE `audit_logs` DISABLE KEYS */;
INSERT INTO `audit_logs` VALUES (2,0,'insert','departments','12','{\"new\": {\"name\": \"急诊科\", \"location\": \"\", \"managerId\": 1, \"departmentId\": 12}}','2025-02-28 10:03:40'),(4,0,'insert','medicines','18','{\"new\": {\"name\": \"阿莫西胶囊\", \"unit\": \"盒\", \"brand\": \"辉瑞制药\", \"price\": 942.99, \"createdAt\": \"2025-03-05 16:08:40\", \"isGeneric\": false, \"updatedAt\": \"2025-03-05 16:08:40\", \"categoryId\": 1, \"medicineId\": 18, \"specification\": \"0.25*12粒/盒\"}}','2025-03-05 08:08:40'),(5,0,'insert','suppliers','29','{\"new\": {\"name\": \"辉瑞制药\", \"phone\": \"13728430841\", \"address\": \"江苏省 西汉市 黄大仙区 士旁3号 14层\", \"supplierId\": 29, \"contactPerson\": \"刘备\", \"contractEndDate\": \"2030-02-07\"}}','2025-03-05 08:12:13'),(43,2,'insert','medicine_batches','20230801A','{\"new\": {\"batchNo\": \"20230801A\", \"createdAt\": \"2025-03-06 11:04:33\", \"expiryDate\": \"2013-08-17\", \"medicineId\": 18, \"supplierId\": 29, \"productionDate\": \"1978-04-25\", \"initialQuantity\": 1000, \"storageLocation\": \"门诊药房1区\"}}','2025-03-06 03:04:33'),(44,2,'insert','inventory','24','{\"new\": {\"batchNo\": \"20230801A\", \"lastUsed\": \"2025-03-06 11:04:33\", \"minStock\": 100, \"inventoryId\": 24, \"lastRestocked\": \"2025-03-06 11:04:33\", \"currentQuantity\": 1000}}','2025-03-06 03:04:33'),(45,2,'insert','medicine_batches','20230801A','{\"new\": {\"batchNo\": \"20230801A\", \"createdAt\": \"2025-03-06 11:15:46\", \"expiryDate\": \"2013-08-17\", \"medicineId\": 18, \"supplierId\": 29, \"productionDate\": \"1978-04-25\", \"initialQuantity\": 1000, \"storageLocation\": \"门诊药房1区\"}}','2025-03-06 03:15:46'),(46,2,'insert','inventory','25','{\"new\": {\"batchNo\": \"20230801A\", \"lastUsed\": \"2025-03-06 11:15:46\", \"minStock\": 100, \"inventoryId\": 25, \"lastRestocked\": \"2025-03-06 11:15:46\", \"currentQuantity\": 1000}}','2025-03-06 03:15:46'),(47,2,'insert','staff','2','{\"new\": {\"name\": \"颜浩晨\", \"title\": \"护士\", \"staffId\": 2, \"isActive\": true, \"createdAt\": \"2025-03-08 10:14:58\", \"department\": \"眼科\"}}','2025-03-08 02:14:58'),(48,2,'insert','staff','3','{\"new\": {\"name\": \"小红\", \"title\": \"护士\", \"staffId\": 3, \"isActive\": true, \"createdAt\": \"2025-03-08 10:16:04\", \"department\": \"眼科\"}}','2025-03-08 02:16:04'),(49,2,'insert','staff','4','{\"new\": {\"name\": \"力语汐\", \"title\": \"护士\", \"staffId\": 4, \"isActive\": true, \"createdAt\": \"2025-03-08 10:16:55\", \"department\": \"眼科\"}}','2025-03-08 02:16:55'),(50,2,'insert','staff','5','{\"new\": {\"name\": \"姚智杰\", \"title\": \"护士\", \"staffId\": 5, \"isActive\": true, \"createdAt\": \"2025-03-08 10:17:28\", \"department\": \"眼科\"}}','2025-03-08 02:17:28'),(51,2,'insert','staff','6','{\"new\": {\"name\": \"羿若汐\", \"title\": \"护士\", \"staffId\": 6, \"isActive\": true, \"createdAt\": \"2025-03-08 10:17:45\", \"department\": \"眼科\"}}','2025-03-08 02:17:45'),(52,2,'insert','staff','7','{\"new\": {\"name\": \"良语汐\", \"title\": \"护士\", \"staffId\": 7, \"isActive\": true, \"createdAt\": \"2025-03-08 10:17:57\", \"department\": \"眼科\"}}','2025-03-08 02:17:57'),(53,2,'insert','staff','8','{\"new\": {\"name\": \"农霞\", \"title\": \"护士\", \"staffId\": 8, \"isActive\": true, \"createdAt\": \"2025-03-08 10:18:33\", \"department\": \"眼科\"}}','2025-03-08 02:18:33'),(54,2,'insert','staff','9','{\"new\": {\"name\": \"林磊\", \"title\": \"护士\", \"staffId\": 9, \"isActive\": true, \"createdAt\": \"2025-03-08 10:18:43\", \"department\": \"眼科\"}}','2025-03-08 02:18:43'),(55,2,'insert','staff','10','{\"new\": {\"name\": \"董丽\", \"title\": \"护士\", \"staffId\": 10, \"isActive\": true, \"createdAt\": \"2025-03-08 10:18:53\", \"department\": \"眼科\"}}','2025-03-08 02:18:53'),(60,2,'update','staff','2','{\"new\": {\"name\": \"颜浩晨\", \"title\": \"助手\", \"staffId\": 2, \"isActive\": 0, \"createdAt\": \"2025-03-08 10:14:59\", \"department\": \"眼科\"}}','2025-03-08 04:24:30'),(61,2,'update','staff','2','{\"new\": {\"name\": \"颜浩晨\", \"title\": \"护士\", \"staffId\": 2, \"isActive\": 0, \"createdAt\": \"2025-03-08 10:14:59\", \"department\": \"眼科\"}}','2025-03-08 04:27:30'),(62,2,'update','staff','2','{\"new\": {\"name\": \"颜浩晨\", \"title\": \"助手\", \"staffId\": 2, \"isActive\": 0, \"createdAt\": \"2025-03-08 10:14:59\", \"department\": \"眼科\"}}','2025-03-08 04:31:28'),(63,2,'update','staff','2','{\"new\": {\"name\": \"颜浩晨\", \"title\": \"护士\", \"staffId\": 2, \"isActive\": 0, \"createdAt\": \"2025-03-08 10:14:59\", \"department\": \"眼科\"}}','2025-03-08 04:40:52'),(64,2,'update','staff','2','{\"new\": {\"name\": \"颜浩晨\", \"title\": \"助手\", \"staffId\": 2, \"isActive\": 0, \"createdAt\": \"2025-03-08 10:14:59\", \"department\": \"眼科\"}}','2025-03-08 05:50:13'),(65,2,'update','staff','2','{\"new\": {\"name\": \"颜浩晨\", \"title\": \"助手\", \"staffId\": 2, \"isActive\": 0, \"createdAt\": \"2025-03-08 10:14:59\", \"department\": \"眼科\"}}','2025-03-08 07:42:59'),(66,2,'update','staff','2','{\"new\": {\"name\": \"颜浩\", \"title\": null, \"staffId\": 2, \"isActive\": null, \"createdAt\": null, \"department\": null}}','2025-03-08 07:45:29'),(67,2,'delete','staff',NULL,'{\"new\": {\"name\": null, \"title\": null, \"staffId\": null, \"isActive\": null, \"createdAt\": null, \"department\": null}}','2025-03-08 09:36:56'),(68,2,'delete','staff','2','{\"new\": {\"name\": \"颜浩\", \"title\": \"助手\", \"staffId\": 2, \"isActive\": 0, \"createdAt\": \"2025-03-08 10:14:59\", \"department\": \"眼科\"}}','2025-03-08 09:37:14'),(69,2,'insert','suppliers','84','{\"new\": {\"name\": \"包都市哲瀚旅游发展有限公司\", \"phone\": \"14685393173\", \"address\": \"重庆市 海码市 斗门区 乜路6518号 99层\", \"supplierId\": 30, \"contactPerson\": \"赏国英\", \"contractEndDate\": \"2027-09-16\"}}','2025-03-08 12:23:49'),(70,2,'insert','suppliers','38','{\"new\": {\"name\": \"北京市烨华矿业（集团）有限公司\", \"phone\": \"15812509721\", \"address\": \"海南省 太原市 嘉定区 督巷88号 98室\", \"supplierId\": 31, \"contactPerson\": \"廖帅\", \"contractEndDate\": \"2027-10-15\"}}','2025-03-08 12:23:55'),(71,2,'insert','suppliers','99','{\"new\": {\"name\": \"济阳市瑾瑜网络科技股份有限公司\", \"phone\": \"17461777737\", \"address\": \"海南省 包原市 印台区 巧中心6125号 43单元\", \"supplierId\": 32, \"contactPerson\": \"皋丽芳\", \"contractEndDate\": \"2028-01-10\"}}','2025-03-08 12:23:59'),(273,1,'update','patients','39','{\"new\": {\"name\": \"丛倩\", \"gender\": \"0\", \"birthdate\": \"1946-08-01\", \"createdAt\": \"2025-03-19 20:45:51\", \"patientId\": 39, \"contactPhone\": \"19178234494\"}}','2025-03-19 12:47:34'),(274,1,'update','patients','78','{\"new\": {\"name\": \"庞浩\", \"gender\": \"1\", \"birthdate\": \"1997-01-30\", \"createdAt\": \"2025-03-08 20:41:22\", \"patientId\": 78, \"contactPhone\": \"13554263680\"}}','2025-03-19 12:48:12'),(275,1,'insert','patients','101','{\"new\": {\"name\": \"小米\", \"gender\": \"0\", \"birthdate\": \"2015-03-18\", \"createdAt\": \"2025-03-19 20:49:17\", \"patientId\": 101, \"contactPhone\": \"17345678958\"}}','2025-03-19 12:49:17'),(276,1,'update','staff','10','{\"new\": {\"name\": \"董丽\", \"title\": \"护士\", \"staffId\": 10, \"isActive\": 1, \"createdAt\": \"2025-03-08 10:18:53\", \"department\": \"眼科\"}}','2025-03-20 04:56:29'),(277,1,'insert','staff','11','{\"new\": {\"name\": \"小米\", \"title\": \"护士\", \"staffId\": 11, \"isActive\": 0, \"createdAt\": \"2025-03-20 12:57:18\", \"department\": \"眼科\"}}','2025-03-20 04:57:18'),(278,1,'update','suppliers','29','{\"new\": {\"name\": \"辉瑞制药\", \"phone\": \"13728430841\", \"address\": \"江苏省 西汉市 黄大仙区 士旁3号 14层\", \"supplierId\": 29, \"contactPerson\": \"李白\", \"contractEndDate\": \"2030-02-07\"}}','2025-03-20 06:36:52'),(279,1,'insert','suppliers','49','{\"new\": {\"name\": \"发士大夫\", \"phone\": \"17390187917\", \"address\": \"江苏省 西汉市 黄大仙区 士旁3号 14层\", \"supplierId\": 49, \"contactPerson\": \"的是\", \"contractEndDate\": \"2029-03-14\"}}','2025-03-20 06:41:33'),(280,1,'update','departments','12','{\"new\": {\"name\": \"急诊科\", \"location\": \"fsdfsdf\", \"managerId\": 1, \"departmentId\": 12}}','2025-03-20 12:24:53'),(281,1,'update','departments','1','{\"new\": {\"name\": \"眼科\", \"location\": \"dfsdfsdf\", \"managerId\": 1, \"departmentId\": 1}}','2025-03-20 12:25:06'),(282,1,'update','departments','18','{\"new\": {\"name\": \"边宇轩\", \"location\": \"烦烦烦方法\", \"managerId\": 3, \"departmentId\": 18}}','2025-03-21 02:43:32'),(283,1,'insert','departments','19','{\"new\": {\"name\": \"吃个饭\", \"location\": \"合法化\", \"managerId\": 2, \"departmentId\": 19}}','2025-03-21 03:02:07'),(284,1,'insert','departments','20','{\"new\": {\"name\": \"沙发沙发\", \"location\": \"沙发沙发\", \"managerId\": 3, \"departmentId\": 20}}','2025-03-21 03:07:05'),(285,1,'update','medicines','23','{\"new\": {\"name\": \"cExpI\", \"unit\": \"JyTqv\", \"brand\": \"mYuOp\", \"price\": 385.54, \"createdAt\": \"2025-03-10 10:33:57\", \"isGeneric\": true, \"updatedAt\": \"2025-03-23 13:55:07\", \"categoryId\": 1, \"medicineId\": 23, \"specification\": \"SbNSc\"}}','2025-03-23 05:55:07'),(286,1,'update','medicines','20','{\"new\": {\"name\": \"SmeoT\", \"unit\": \"PpmST\", \"brand\": \"PEqvO\", \"price\": 705.05, \"createdAt\": \"2025-03-10 10:34:10\", \"isGeneric\": false, \"updatedAt\": \"2025-03-23 13:55:28\", \"categoryId\": 3, \"medicineId\": 20, \"specification\": \"YFHBE\"}}','2025-03-23 05:55:28'),(287,1,'update','medicines','20','{\"new\": {\"name\": \"SmeoT\", \"unit\": \"PpmST\", \"brand\": \"PEqvO\", \"price\": 705.05, \"createdAt\": \"2025-03-10 10:34:10\", \"isGeneric\": false, \"updatedAt\": \"2025-03-23 16:00:03\", \"categoryId\": 4, \"medicineId\": 20, \"specification\": \"YFHBE\"}}','2025-03-23 08:00:03'),(288,1,'insert','medicines','100','{\"new\": {\"name\": \"打发打发\", \"unit\": \"打发士大夫\", \"brand\": \"BmaUL\", \"price\": 233, \"createdAt\": \"2025-03-23 16:26:51\", \"isGeneric\": true, \"updatedAt\": \"2025-03-23 16:26:51\", \"categoryId\": 3, \"medicineId\": 100, \"specification\": \"3434\"}}','2025-03-23 08:26:51'),(289,1,'update','medicines','100','{\"new\": {\"name\": \"打发打发\", \"unit\": \"打发士大夫\", \"brand\": \"BmaUL\", \"price\": 233, \"createdAt\": \"2025-03-23 16:26:51\", \"isGeneric\": true, \"updatedAt\": \"2025-03-23 16:31:47\", \"categoryId\": 2, \"medicineId\": 100, \"specification\": \"3434\"}}','2025-03-23 08:31:47'),(290,1,'insert','medicines','101','{\"new\": {\"name\": \"二位\", \"unit\": \"阿大撒\", \"brand\": \"辉瑞制药\", \"price\": 333, \"createdAt\": \"2025-03-23 16:32:38\", \"isGeneric\": false, \"updatedAt\": \"2025-03-23 16:32:38\", \"categoryId\": 2, \"medicineId\": 101, \"specification\": \"热热\"}}','2025-03-23 08:32:38'),(291,1,'insert','medicines','102','{\"new\": {\"name\": \"撒大苏打\", \"unit\": \"啊实打实\", \"brand\": \"辉瑞制药\", \"price\": 2323, \"createdAt\": \"2025-03-25 15:36:42\", \"isGeneric\": true, \"updatedAt\": \"2025-03-25 15:36:42\", \"categoryId\": 3, \"medicineId\": 102, \"specification\": \"撒大苏打\"}}','2025-03-25 07:36:42'),(292,1,'insert','medicines','103','{\"new\": {\"name\": \"打赏\", \"unit\": \"撒旦撒\", \"brand\": \"辉瑞制药\", \"price\": 2121.22, \"createdAt\": \"2025-03-25 15:37:40\", \"isGeneric\": true, \"updatedAt\": \"2025-03-25 15:37:40\", \"categoryId\": 4, \"medicineId\": 103, \"specification\": \"撒大苏打\"}}','2025-03-25 07:37:40'),(293,1,'update','medicines','102','{\"new\": {\"name\": \"撒大苏打\", \"unit\": \"啊实打实\", \"brand\": \"辉瑞制药\", \"price\": 2323, \"createdAt\": \"2025-03-25 15:36:43\", \"isGeneric\": true, \"updatedAt\": \"2025-03-25 15:38:09\", \"categoryId\": 4, \"medicineId\": 102, \"specification\": \"撒大苏打\"}}','2025-03-25 07:38:09'),(294,1,'insert','medicine_categories','5','{\"new\": {\"parentId\": 1, \"createdAt\": \"2025-03-26 12:58:24\", \"isDeleted\": false, \"sortOrder\": 1, \"updatedAt\": \"2025-03-26 12:58:24\", \"categoryId\": 5, \"description\": \"\", \"categoryName\": \"血清\"}}','2025-03-26 04:58:24'),(295,1,'update','medicine_categories','5','{\"new\": {\"parentId\": 2, \"createdAt\": \"2025-03-26 12:58:25\", \"isDeleted\": false, \"sortOrder\": 1, \"updatedAt\": \"2025-03-26 13:13:08\", \"categoryId\": 5, \"description\": \"\", \"categoryName\": \"血清\"}}','2025-03-26 05:13:08'),(296,1,'update','medicine_categories','4','{\"new\": {\"parentId\": 2, \"createdAt\": \"2025-03-10 09:43:30\", \"isDeleted\": false, \"sortOrder\": 1, \"updatedAt\": \"2025-03-26 13:13:33\", \"categoryId\": 4, \"description\": \"\", \"categoryName\": \"头孢类\"}}','2025-03-26 05:13:33'),(297,1,'update','medicine_categories','4','{\"new\": {\"parentId\": 1, \"createdAt\": \"2025-03-10 09:43:30\", \"isDeleted\": false, \"sortOrder\": 1, \"updatedAt\": \"2025-03-26 13:14:03\", \"categoryId\": 4, \"description\": \"\", \"categoryName\": \"头孢类\"}}','2025-03-26 05:14:03'),(298,1,'update','medicine_categories','5','{\"new\": {\"parentId\": 1, \"createdAt\": \"2025-03-26 12:58:25\", \"isDeleted\": false, \"sortOrder\": 1, \"updatedAt\": \"2025-03-26 13:14:10\", \"categoryId\": 5, \"description\": \"\", \"categoryName\": \"血清\"}}','2025-03-26 05:14:10'),(299,1,'insert','medicine_categories','6','{\"new\": {\"parentId\": 5, \"createdAt\": \"2025-03-26 13:49:17\", \"isDeleted\": false, \"sortOrder\": 2, \"updatedAt\": \"2025-03-26 13:49:17\", \"categoryId\": 6, \"description\": \"\", \"categoryName\": \"发士大夫\"}}','2025-03-26 05:49:17'),(300,1,'insert','medicine_categories','7','{\"new\": {\"parentId\": 2, \"createdAt\": \"2025-03-26 13:59:03\", \"isDeleted\": false, \"sortOrder\": 7, \"updatedAt\": \"2025-03-26 13:59:03\", \"categoryId\": 7, \"description\": \"\", \"categoryName\": \"烦烦烦\"}}','2025-03-26 05:59:03'),(301,1,'update','medicine_categories','2','{\"new\": {\"parentId\": 1, \"createdAt\": \"2025-03-05 16:08:09\", \"isDeleted\": false, \"sortOrder\": 0, \"updatedAt\": \"2025-03-26 14:04:39\", \"categoryId\": 2, \"description\": \"未分类\", \"categoryName\": \"未分类\"}}','2025-03-26 06:04:39'),(302,1,'delete','medicine_categories','7','{\"new\": {\"value\": 7}}','2025-03-26 06:22:08'),(303,1,'insert','medicine_batches',NULL,'{\"new\": {\"batchNo\": \"Ckzz5Z4wx\", \"createdAt\": null, \"expiryDate\": \"2037-03-28\", \"medicineId\": 33, \"supplierId\": 1, \"productionDate\": \"2020-03-02\", \"initialQuantity\": 200, \"storageLocation\": \"\"}}','2025-03-27 02:25:36'),(304,1,'insert','medicine_batches',NULL,'{\"new\": {\"batchNo\": \"Ckzz5Z4wx\", \"createdAt\": null, \"expiryDate\": \"2037-03-28\", \"medicineId\": 33, \"supplierId\": 2, \"productionDate\": \"2020-03-02\", \"initialQuantity\": 200, \"storageLocation\": \"\"}}','2025-03-27 02:25:42'),(305,1,'insert','inventory','62','{\"new\": {\"batchNo\": \"Ckzz5Z4wx\", \"lastUsed\": \"2025-03-27 10:26:09\", \"minStock\": 100, \"inventoryId\": 62, \"lastRestocked\": \"2025-03-27 10:26:09\", \"currentQuantity\": 200}}','2025-03-27 02:26:09'),(306,1,'insert','medicine_batches','Ckzz5Z4wx','{\"new\": {\"batchNo\": \"Ckzz5Z4wx\", \"createdAt\": \"2025-03-27 10:26:09\", \"expiryDate\": \"2037-03-28\", \"medicineId\": 33, \"supplierId\": 29, \"productionDate\": \"2020-03-02\", \"initialQuantity\": 200, \"storageLocation\": \"\"}}','2025-03-27 02:26:09'),(307,1,'insert','medicine_batches','ddsfsdf','{\"new\": {\"batchNo\": \"ddsfsdf\", \"createdAt\": \"2025-03-27 13:37:07\", \"expiryDate\": \"2025-03-29\", \"medicineId\": 20, \"supplierId\": 29, \"productionDate\": \"2025-02-23\", \"initialQuantity\": 120, \"storageLocation\": \"\"}}','2025-03-27 05:37:07'),(308,1,'insert','inventory','63','{\"new\": {\"batchNo\": \"ddsfsdf\", \"lastUsed\": \"2025-03-27 13:37:07\", \"minStock\": 100, \"inventoryId\": 63, \"lastRestocked\": \"2025-03-27 13:37:07\", \"currentQuantity\": 120}}','2025-03-27 05:37:07'),(309,1,'insert','stock_alerts','4','{\"new\": {\"alertId\": 4, \"isEnabled\": true, \"alertLevel\": \"low\", \"medicineId\": 25, \"minQuantity\": 100, \"notificationMethods\": \"[\\\"邮件:email\\\"]\"}}','2025-03-28 07:45:22'),(310,1,'insert','stock_alerts','4','{\"new\": {\"alertId\": null, \"isEnabled\": true, \"alertLevel\": \"low\", \"medicineId\": 25, \"minQuantity\": 100, \"notificationMethods\": \"[\\\"邮件:email\\\"]\"}}','2025-03-28 07:51:10'),(311,1,'update','stock_alerts',NULL,'{\"new\": {\"alertId\": 4, \"isEnabled\": true, \"alertLevel\": \"low\", \"medicineId\": 25, \"minQuantity\": 100, \"notificationMethods\": \"[\\\"邮件:email\\\",\\\"短信:sms\\\",\\\"系统:system\\\"]\"}}','2025-03-28 07:51:37'),(312,1,'delete','stock_alerts','4','{\"new\": {\"value\": 4}}','2025-03-28 08:04:12'),(313,1,'update','stock_alerts',NULL,'{\"new\": {\"alertId\": 2, \"isEnabled\": true, \"alertLevel\": \"low\", \"medicineId\": 18, \"minQuantity\": 468, \"notificationMethods\": \"[\\\"邮件:email\\\",\\\"短信:sms\\\",\\\"系统:system\\\"]\"}}','2025-03-28 08:04:52'),(314,1,'update','stock_alerts',NULL,'{\"new\": {\"alertId\": 2, \"isEnabled\": false, \"alertLevel\": \"low\", \"medicineId\": 18, \"minQuantity\": 450, \"notificationMethods\": \"[\\\"邮件:email\\\",\\\"短信:sms\\\",\\\"系统:system\\\"]\"}}','2025-03-28 08:05:05'),(315,1,'update','stock_alerts','2','{\"new\": {\"alertId\": 2, \"isEnabled\": false, \"alertLevel\": \"low\", \"medicineId\": 18, \"minQuantity\": 450, \"notificationMethods\": \"[\\\"邮件:email\\\",\\\"短信:sms\\\",\\\"系统:system\\\"]\"}}','2025-03-28 08:09:03'),(316,1,'update','purchase_orders','1','{\"new\": {\"poId\": 1, \"status\": null, \"createdBy\": null, \"orderDate\": null, \"supplierId\": null, \"totalAmount\": null, \"expectedDeliveryDate\": null}}','2025-03-29 05:26:31'),(317,1,'update','purchase_orders','2','{\"new\": {\"poId\": 2, \"status\": null, \"createdBy\": null, \"orderDate\": null, \"supplierId\": null, \"totalAmount\": null, \"expectedDeliveryDate\": null}}','2025-03-29 05:26:34'),(318,1,'update','purchase_orders','3','{\"new\": {\"poId\": 3, \"status\": null, \"createdBy\": null, \"orderDate\": null, \"supplierId\": null, \"totalAmount\": null, \"expectedDeliveryDate\": null}}','2025-03-29 05:26:36'),(319,1,'insert','purchase_records','70','{\"new\": {\"poId\": 3, \"batchNo\": \"2MJLVn0Lf\", \"quantity\": 100, \"recordId\": 70, \"unitPrice\": 230, \"medicineId\": 23}}','2025-03-29 12:15:44'),(320,1,'insert','purchase_records','72','{\"new\": {\"poId\": 3, \"batchNo\": \"2MJLVn0Lf\", \"quantity\": 100, \"recordId\": 72, \"unitPrice\": 2300, \"medicineId\": 23}}','2025-03-29 12:17:37'),(321,1,'update','purchase_records','71','{\"new\": {\"poId\": 3, \"batchNo\": \"2MJLVn0Lf\", \"quantity\": 120, \"recordId\": 71, \"unitPrice\": 2300, \"medicineId\": 23}}','2025-03-30 06:30:27'),(322,1,'update','prescriptions',NULL,'{\"new\": {\"status\": null, \"doctorId\": null, \"patientId\": null, \"dispensedAt\": null, \"dispensedBy\": null, \"prescriptionId\": null, \"prescriptionDate\": null}}','2025-03-30 12:31:13'),(323,1,'update','prescriptions',NULL,'{\"new\": {\"prescriptions\": {\"status\": \"2\", \"doctorId\": 1, \"patientId\": 1, \"dispensedAt\": \"2025-03-11 18:03:25\", \"dispensedBy\": 2, \"prescriptionId\": 1, \"prescriptionDate\": \"2025-03-11 16:52:20\"}, \"prescriptionMedicines\": [{\"dosage\": \"1片/次\", \"batchNo\": \"20230801A\", \"recordId\": 1, \"medicineId\": 18, \"prescriptionId\": 1, \"quantityDispensed\": 14}, {\"dosage\": \"2片/次\", \"batchNo\": \"2MJLVn0Lf\", \"recordId\": 2, \"medicineId\": 23, \"prescriptionId\": 1, \"quantityDispensed\": 5}]}}','2025-04-01 09:10:50'),(324,1,'update','prescriptions',NULL,'{\"new\": {\"prescriptions\": {\"status\": \"2\", \"doctorId\": 1, \"patientId\": 1, \"dispensedAt\": \"2025-03-11 18:03:25\", \"dispensedBy\": 2, \"prescriptionId\": 1, \"prescriptionDate\": \"2025-03-11 16:52:20\"}, \"prescriptionMedicines\": [{\"dosage\": \"1片/次\", \"batchNo\": \"20230801A\", \"recordId\": 1, \"medicineId\": 18, \"prescriptionId\": 1, \"quantityDispensed\": 15}, {\"dosage\": \"2片/次\", \"batchNo\": \"2MJLVn0Lf\", \"recordId\": 2, \"medicineId\": 23, \"prescriptionId\": 1, \"quantityDispensed\": 10}]}}','2025-04-01 09:26:52'),(325,1,'insert','prescriptions','2','{\"new\": {\"prescriptions\": {\"status\": \"0\", \"doctorId\": 1, \"patientId\": 1, \"dispensedAt\": null, \"dispensedBy\": null, \"prescriptionId\": 2, \"prescriptionDate\": null}, \"prescriptionMedicines\": [{\"dosage\": \"1片/次\", \"batchNo\": \"20230801A\", \"recordId\": null, \"medicineId\": 18, \"prescriptionId\": null, \"quantityDispensed\": 7}]}}','2025-04-01 09:58:56'),(326,1,'insert','prescriptions',NULL,'{\"new\": {\"prescriptions\": {\"status\": \"\", \"doctorId\": 1111, \"patientId\": 101, \"dispensedAt\": null, \"dispensedBy\": null, \"prescriptionId\": null, \"prescriptionDate\": null}, \"prescriptionMedicines\": [{\"dosage\": \"手动阀手动阀\", \"batchNo\": \"20230801A\", \"recordId\": null, \"medicineId\": 18, \"prescriptionId\": null, \"quantityDispensed\": 2}]}}','2025-04-03 11:16:10'),(327,1,'insert','prescriptions','3','{\"new\": {\"prescriptions\": {\"status\": \"0\", \"doctorId\": 1, \"patientId\": 101, \"dispensedAt\": null, \"dispensedBy\": null, \"prescriptionId\": 3, \"prescriptionDate\": null}, \"prescriptionMedicines\": [{\"dosage\": \"感到十分\", \"batchNo\": \"g1Mbqjymn\", \"recordId\": null, \"medicineId\": 28, \"prescriptionId\": null, \"quantityDispensed\": 2}]}}','2025-04-03 11:41:13'),(328,1,'update','prescriptions',NULL,'{\"new\": {\"prescriptions\": {\"status\": \"2\", \"doctorId\": 1, \"patientId\": 1, \"dispensedAt\": \"2025-03-11 18:03:25\", \"dispensedBy\": 2, \"prescriptionId\": 1, \"prescriptionDate\": \"2025-03-11 16:52:20\"}, \"prescriptionMedicines\": [{\"dosage\": \"1片/次\", \"batchNo\": \"20230801A\", \"recordId\": 1, \"medicineId\": 18, \"prescriptionId\": 1, \"quantityDispensed\": 10}, {\"dosage\": \"2片/次\", \"batchNo\": \"2MJLVn0Lf\", \"recordId\": 2, \"medicineId\": 23, \"prescriptionId\": 1, \"quantityDispensed\": 5}]}}','2025-04-03 12:03:19'),(329,1,'update','prescription_medicines','3','{\"new\": {\"dosage\": \"1片/次\", \"batchNo\": \"9Fze4ywiU\", \"recordId\": 3, \"medicineId\": 18, \"prescriptionId\": 2, \"quantityDispensed\": 7}}','2025-04-03 12:13:44'),(330,1,'update','prescriptions','3','{\"new\": {\"prescriptions\": {\"status\": \"0\", \"doctorId\": 1, \"patientId\": 1, \"dispensedAt\": null, \"dispensedBy\": null, \"prescriptionId\": 2, \"prescriptionDate\": \"2025-04-01 17:58:56\"}, \"prescriptionMedicines\": [{\"dosage\": \"1片/次\", \"batchNo\": \"9Fze4ywiU\", \"recordId\": 3, \"medicineId\": 18, \"prescriptionId\": 2, \"quantityDispensed\": 7}]}}','2025-04-03 12:13:44'),(331,1,'update','prescriptions','3','{\"new\": {\"prescriptions\": {\"status\": \"0\", \"doctorId\": 1, \"patientId\": 1, \"dispensedAt\": null, \"dispensedBy\": null, \"prescriptionId\": 2, \"prescriptionDate\": \"2025-04-01 17:58:56\"}, \"prescriptionMedicines\": [{\"dosage\": \"1片/次\", \"batchNo\": \"9Fze4ywiU\", \"recordId\": 3, \"medicineId\": 18, \"prescriptionId\": 2, \"quantityDispensed\": 7}]}}','2025-04-03 12:13:50'),(332,1,'update','prescription_medicines','3','{\"new\": {\"dosage\": \"1片/次\", \"batchNo\": \"9Fze4ywiU\", \"recordId\": 3, \"medicineId\": 18, \"prescriptionId\": 2, \"quantityDispensed\": 7}}','2025-04-03 12:13:50'),(333,1,'update','prescriptions','3','{\"new\": {\"prescriptions\": {\"status\": \"0\", \"doctorId\": 1, \"patientId\": 1, \"dispensedAt\": null, \"dispensedBy\": null, \"prescriptionId\": 2, \"prescriptionDate\": \"2025-04-01 17:58:56\"}, \"prescriptionMedicines\": [{\"dosage\": \"1片/次\", \"batchNo\": \"9Fze4ywiU\", \"recordId\": 3, \"medicineId\": 18, \"prescriptionId\": 2, \"quantityDispensed\": 7}]}}','2025-04-03 12:16:05'),(334,1,'update','prescription_medicines','3','{\"new\": {\"dosage\": \"1片/次\", \"batchNo\": \"9Fze4ywiU\", \"recordId\": 3, \"medicineId\": 18, \"prescriptionId\": 2, \"quantityDispensed\": 7}}','2025-04-03 12:16:05'),(335,1,'update','prescription_medicines','4','{\"new\": {\"dosage\": \"感到十分\", \"batchNo\": \"PXscf7XXp\", \"recordId\": 4, \"medicineId\": 28, \"prescriptionId\": 3, \"quantityDispensed\": 2}}','2025-04-03 12:19:13'),(336,1,'update','prescriptions','3','{\"new\": {\"prescriptions\": {\"status\": \"0\", \"doctorId\": 1, \"patientId\": 101, \"dispensedAt\": null, \"dispensedBy\": 1, \"prescriptionId\": 3, \"prescriptionDate\": \"2025-04-03 19:41:13\"}, \"prescriptionMedicines\": [{\"dosage\": \"感到十分\", \"batchNo\": \"PXscf7XXp\", \"recordId\": 4, \"medicineId\": 28, \"prescriptionId\": 3, \"quantityDispensed\": 2}]}}','2025-04-03 12:19:13'),(337,1,'update','prescription_medicines','4','{\"new\": {\"dosage\": \"感到十分\", \"batchNo\": \"g1Mbqjymn\", \"recordId\": 4, \"medicineId\": 28, \"prescriptionId\": 3, \"quantityDispensed\": 2}}','2025-04-03 12:25:56'),(338,1,'update','prescriptions','3','{\"new\": {\"prescriptions\": {\"status\": \"0\", \"doctorId\": 1, \"patientId\": 101, \"dispensedAt\": \"2025-04-03 20:19:13\", \"dispensedBy\": 1, \"prescriptionId\": 3, \"prescriptionDate\": \"2025-04-03 19:41:13\"}, \"prescriptionMedicines\": [{\"dosage\": \"感到十分\", \"batchNo\": \"g1Mbqjymn\", \"recordId\": 4, \"medicineId\": 28, \"prescriptionId\": 3, \"quantityDispensed\": 2}]}}','2025-04-03 12:25:56'),(339,1,'update','prescription_medicines','4','{\"new\": {\"dosage\": \"感到十分\", \"batchNo\": \"PXscf7XXp\", \"recordId\": 4, \"medicineId\": 28, \"prescriptionId\": 3, \"quantityDispensed\": 2}}','2025-04-03 12:27:45'),(340,1,'update','prescriptions','3','{\"new\": {\"prescriptions\": {\"status\": \"0\", \"doctorId\": 1, \"patientId\": 101, \"dispensedAt\": \"2025-04-03 20:25:56\", \"dispensedBy\": null, \"prescriptionId\": 3, \"prescriptionDate\": \"2025-04-03 19:41:13\"}, \"prescriptionMedicines\": [{\"dosage\": \"感到十分\", \"batchNo\": \"PXscf7XXp\", \"recordId\": 4, \"medicineId\": 28, \"prescriptionId\": 3, \"quantityDispensed\": 2}]}}','2025-04-03 12:27:45'),(341,1,'delete','prescriptions','3','{\"new\": {\"value\": 3}}','2025-04-04 01:43:30'),(342,1,'update','prescriptions','1','{\"new\": {\"status\": \"2\", \"doctorId\": 1, \"patientId\": 1, \"dispensedAt\": \"2025-03-11 18:03:25\", \"dispensedBy\": 2, \"prescriptionId\": 1, \"prescriptionDate\": \"2025-03-11 16:52:20\"}}','2025-04-04 02:15:45'),(343,1,'delete','prescriptions',NULL,'{\"new\": {\"value\": 2}}','2025-04-04 02:27:21'),(344,1,'delete','prescriptions',NULL,'{\"new\": {\"value\": 1}}','2025-04-04 02:27:24'),(345,1,'delete','medicines','103','{\"new\": {\"value\": 103}}','2025-04-05 06:03:11'),(346,1,'delete','medicines','102','{\"new\": {\"value\": 102}}','2025-04-05 06:03:20'),(347,1,'delete','medicines',NULL,'{\"new\": {\"value\": 23}}','2025-04-05 06:04:06'),(348,1,'delete','medicines','101','{\"new\": {\"value\": 101}}','2025-04-05 06:04:11'),(349,1,'insert','purchase_orders','34','{\"new\": {\"poId\": 34, \"status\": \"pending\", \"createdBy\": 1, \"orderDate\": \"2025-04-05\", \"supplierId\": 45, \"totalAmount\": 7878, \"expectedDeliveryDate\": \"2025-05-03\"}}','2025-04-05 06:18:01'),(350,1,'update','purchase_records','71','{\"new\": {\"poId\": 3, \"batchNo\": \"2MJLVn0Lf\", \"quantity\": 122, \"recordId\": 71, \"unitPrice\": 2300, \"medicineId\": 23}}','2025-04-05 06:29:59'),(351,1,'update','purchase_orders',NULL,'{\"new\": {\"poId\": 3, \"status\": \"cancelled\", \"createdBy\": 2, \"orderDate\": \"2024-07-01\", \"supplierId\": 29, \"totalAmount\": 32572.69, \"expectedDeliveryDate\": \"2025-08-09\"}}','2025-04-05 06:56:10'),(352,1,'update','purchase_orders','4','{\"new\": {\"poId\": 4, \"status\": null, \"createdBy\": null, \"orderDate\": null, \"supplierId\": null, \"totalAmount\": null, \"expectedDeliveryDate\": null}}','2025-04-05 06:57:11'),(353,1,'insert','purchase_orders','35','{\"new\": {\"poId\": 35, \"status\": \"pending\", \"createdBy\": 1, \"orderDate\": \"2025-04-05\", \"supplierId\": 29, \"totalAmount\": 10000, \"expectedDeliveryDate\": \"2025-04-19\"}}','2025-04-05 07:08:18'),(354,1,'insert','purchase_records',NULL,'{\"new\": {\"poId\": 23, \"batchNo\": \"FGHFD34G\", \"quantity\": 1000, \"recordId\": null, \"unitPrice\": 41, \"medicineId\": 23}}','2025-04-05 07:11:03'),(355,1,'insert','purchase_records','73','{\"new\": {\"poId\": 23, \"batchNo\": \"2MJLVn0Lf\", \"quantity\": 100, \"recordId\": 73, \"unitPrice\": 50, \"medicineId\": 23}}','2025-04-05 07:11:40'),(356,1,'insert','purchase_records','74','{\"new\": {\"poId\": 35, \"batchNo\": \"2MJLVn0Lf\", \"quantity\": 100, \"recordId\": 74, \"unitPrice\": 50, \"medicineId\": 23}}','2025-04-05 07:12:24'),(357,1,'insert','inventory','64','{\"new\": {\"batchNo\": \"2MJLVnOLf\", \"lastUsed\": \"2025-04-05 15:20:07\", \"minStock\": 100, \"inventoryId\": 64, \"lastRestocked\": \"2025-04-05 15:20:07\", \"currentQuantity\": 100}}','2025-04-05 07:20:07'),(358,1,'insert','medicine_batches','2MJLVnOLf','{\"new\": {\"batchNo\": \"2MJLVnOLf\", \"createdAt\": \"2025-04-05 15:20:07\", \"expiryDate\": \"2025-05-10\", \"medicineId\": 23, \"supplierId\": 29, \"productionDate\": \"2025-03-30\", \"initialQuantity\": 100, \"storageLocation\": \"\"}}','2025-04-05 07:20:07'),(359,1,'insert','purchase_orders','36','{\"new\": {\"poId\": 36, \"status\": \"pending\", \"createdBy\": 1, \"orderDate\": \"2025-04-05\", \"supplierId\": 43, \"totalAmount\": 10000, \"expectedDeliveryDate\": \"2025-05-10\"}}','2025-04-05 07:45:37'),(360,1,'insert','purchase_records','75','{\"new\": {\"poId\": 36, \"batchNo\": \"2MJLVn0Lf\", \"quantity\": 1000, \"recordId\": 75, \"unitPrice\": 50, \"medicineId\": 23}}','2025-04-05 07:46:22');
/*!40000 ALTER TABLE `audit_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departments`
--

DROP TABLE IF EXISTS `departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departments` (
  `department_id` int NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `name` varchar(100) COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '科室名称（如心血管内科）',
  `location` varchar(200) COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '物理位置',
  `manager_id` int DEFAULT NULL COMMENT '科室负责人',
  PRIMARY KEY (`department_id`),
  UNIQUE KEY `departments_name_uindex` (`name`),
  KEY `manager_id` (`manager_id`),
  CONSTRAINT `departments_ibfk_1` FOREIGN KEY (`manager_id`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_croatian_ci COMMENT='医院科室表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departments`
--

LOCK TABLES `departments` WRITE;
/*!40000 ALTER TABLE `departments` DISABLE KEYS */;
INSERT INTO `departments` VALUES (1,'眼科','dfsdfsdf',1),(12,'急诊科','fsdfsdf',1),(13,'北鹏','dolore',1),(15,'蹉斌','deserunt Excepteur pariatur',1),(16,'沙浩然','id eu in commodo ullamco',1),(20,'沙发沙发','沙发沙发',3);
/*!40000 ALTER TABLE `departments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `inventory_id` bigint NOT NULL AUTO_INCREMENT COMMENT '库存记录ID',
  `batch_no` varchar(50) COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '批次号',
  `current_quantity` int NOT NULL COMMENT '当前库存数量',
  `min_stock` int DEFAULT '10' COMMENT '安全库存阈值',
  `last_restocked` timestamp NULL DEFAULT NULL COMMENT '最近补货时间',
  `last_used` timestamp NULL DEFAULT NULL COMMENT '最近领用时间',
  PRIMARY KEY (`inventory_id`),
  KEY `idx_batch` (`batch_no`),
  KEY `idx_quantity` (`current_quantity`),
  CONSTRAINT `inventory_ibfk_1` FOREIGN KEY (`batch_no`) REFERENCES `medicine_batches` (`batch_no`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_croatian_ci COMMENT='实时库存状态表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (25,'20230801A',980,450,'2025-03-06 03:15:47','2025-03-06 03:15:47'),(26,'aq0siJ3oQ',307,100,'2025-03-10 04:04:01','2025-03-10 04:04:01'),(27,'SfTkWs55Y',4394,100,'2025-03-10 04:04:04','2025-03-10 04:04:04'),(28,'RXC1Zbh4b',2458,100,'2025-03-10 04:05:16','2025-03-10 04:05:16'),(29,'KT2PAMLn3',1843,100,'2025-03-10 04:05:19','2025-03-10 04:05:19'),(30,'AF676qMj9',3952,100,'2025-03-10 04:05:29','2025-03-10 04:05:29'),(31,'q7DvdyK5o',2704,100,'2025-03-10 04:05:31','2025-03-10 04:05:31'),(32,'3lxmKMe3m',636,100,'2025-03-10 04:05:33','2025-03-10 04:05:33'),(33,'g1Mbqjymn',1054,100,'2025-03-10 04:05:35','2025-03-10 04:05:35'),(34,'VTJhHBmlP',2228,100,'2025-03-10 04:05:38','2025-03-10 04:05:38'),(35,'qtw8txsNN',4505,100,'2025-03-10 04:07:02','2025-03-10 04:07:02'),(36,'PXscf7XXp',3651,100,'2025-03-10 04:07:03','2025-03-10 04:07:03'),(37,'gk5wM2F16',4074,100,'2025-03-10 04:07:04','2025-03-10 04:07:04'),(38,'rDLNPK045',4307,100,'2025-03-10 04:07:05','2025-03-10 04:07:05'),(39,'rBb9mKYa7',1569,100,'2025-03-10 04:07:08','2025-03-10 04:07:08'),(40,'i8PORs1Gc',1566,100,'2025-03-10 04:07:56','2025-03-10 04:07:56'),(41,'9Fze4ywiU',2302,450,'2025-03-10 04:07:58','2025-03-10 04:07:58'),(42,'ItLuuPDv3',2212,100,'2025-03-10 04:08:00','2025-03-10 04:08:00'),(43,'PyvfHzDWv',4818,100,'2025-03-10 04:08:01','2025-03-10 04:08:01'),(44,'8bHVmDggx',517,100,'2025-03-10 04:08:02','2025-03-10 04:08:02'),(45,'9Ov6GcOBP',332,100,'2025-03-10 04:08:03','2025-03-10 04:08:03'),(46,'TkePsGped',1961,100,'2025-03-10 04:08:49','2025-03-10 04:08:49'),(47,'FBvmsy6GR',543,100,'2025-03-10 04:08:51','2025-03-10 04:08:51'),(48,'ev4zvTVhe',3379,100,'2025-03-10 04:08:52','2025-03-10 04:08:52'),(49,'DRXivRdNY',901,100,'2025-03-10 04:08:53','2025-03-10 04:08:53'),(50,'2MJLVn0Lf',1797,100,'2025-03-10 04:08:54','2025-03-10 04:08:54'),(51,'9dx5BysQi',1090,100,'2025-03-10 04:08:55','2025-03-10 04:08:55'),(52,'MT0xWmchZ',4141,100,'2025-03-10 04:08:55','2025-03-10 04:08:55'),(53,'Bzv4FSp8C',634,100,'2025-03-10 04:08:56','2025-03-10 04:08:56'),(54,'dfZZwvmWd',494,100,'2025-03-10 04:08:57','2025-03-10 04:08:57'),(55,'8T0AH6txf',968,100,'2025-03-10 04:08:57','2025-03-10 04:08:57'),(56,'5XeQLYukj',2753,100,'2025-03-10 04:08:58','2025-03-10 04:08:58'),(57,'MQLTm6cKr',4230,100,'2025-03-10 04:08:59','2025-03-10 04:08:59'),(58,'5BsNcH2Vd',3069,100,'2025-03-10 04:09:00','2025-03-10 04:09:00'),(59,'yvItiTEkH',4522,100,'2025-03-10 04:09:01','2025-03-10 04:09:01'),(60,'pdhLtj7e5',2518,100,'2025-03-10 04:09:02','2025-03-10 04:09:02'),(61,'Ckzz5Z4wS',553,100,'2025-03-10 04:09:03','2025-03-10 04:09:03'),(62,'Ckzz5Z4wx',200,100,'2025-03-27 02:26:09','2025-03-27 02:26:09'),(63,'ddsfsdf',120,100,'2025-03-27 05:37:08','2025-03-27 05:37:08'),(64,'2MJLVnOLf',100,100,'2025-04-05 07:20:07','2025-04-05 07:20:07');
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_transactions`
--

DROP TABLE IF EXISTS `inventory_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_transactions` (
  `transaction_id` bigint NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `batch_no` varchar(50) COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '批次号',
  `transaction_type` enum('IN','OUT','ADJUST') COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '操作类型',
  `quantity` int NOT NULL COMMENT '变动数量',
  `related_id` bigint DEFAULT NULL COMMENT '关联业务ID（如采购单/处方单）',
  `transaction_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `operator_id` int NOT NULL COMMENT '操作人',
  `notes` text COLLATE utf8mb4_croatian_ci COMMENT '备注说明',
  PRIMARY KEY (`transaction_id`),
  KEY `batch_no` (`batch_no`),
  KEY `idx_transaction_time` (`transaction_time`),
  CONSTRAINT `inventory_transactions_ibfk_1` FOREIGN KEY (`batch_no`) REFERENCES `medicine_batches` (`batch_no`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_croatian_ci COMMENT='库存流水记录表（支撑AI分析）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_transactions`
--

LOCK TABLES `inventory_transactions` WRITE;
/*!40000 ALTER TABLE `inventory_transactions` DISABLE KEYS */;
INSERT INTO `inventory_transactions` VALUES (8,'20230801A','IN',1000,18,'2025-03-06 03:15:47',2,'采购入库, 供应商ID:29'),(9,'aq0siJ3oQ','IN',307,22,'2025-03-10 04:04:01',2,'采购入库, 供应商ID:32'),(10,'SfTkWs55Y','IN',4394,25,'2025-03-10 04:04:04',2,'采购入库, 供应商ID:35'),(11,'RXC1Zbh4b','IN',2458,27,'2025-03-10 04:05:16',2,'采购入库, 供应商ID:36'),(12,'KT2PAMLn3','IN',1843,26,'2025-03-10 04:05:19',2,'采购入库, 供应商ID:41'),(13,'AF676qMj9','IN',3952,22,'2025-03-10 04:05:29',2,'采购入库, 供应商ID:33'),(14,'q7DvdyK5o','IN',2704,31,'2025-03-10 04:05:31',2,'采购入库, 供应商ID:36'),(15,'3lxmKMe3m','IN',636,30,'2025-03-10 04:05:33',2,'采购入库, 供应商ID:31'),(16,'g1Mbqjymn','IN',1054,28,'2025-03-10 04:05:35',2,'采购入库, 供应商ID:34'),(17,'VTJhHBmlP','IN',2228,34,'2025-03-10 04:05:38',2,'采购入库, 供应商ID:31'),(18,'qtw8txsNN','IN',4505,19,'2025-03-10 04:07:02',2,'采购入库, 供应商ID:40'),(19,'PXscf7XXp','IN',3651,28,'2025-03-10 04:07:03',2,'采购入库, 供应商ID:38'),(20,'gk5wM2F16','IN',4074,25,'2025-03-10 04:07:04',2,'采购入库, 供应商ID:47'),(21,'rDLNPK045','IN',4307,33,'2025-03-10 04:07:05',2,'采购入库, 供应商ID:46'),(22,'rBb9mKYa7','IN',1569,25,'2025-03-10 04:07:08',2,'采购入库, 供应商ID:39'),(23,'i8PORs1Gc','IN',1566,25,'2025-03-10 04:07:56',2,'采购入库, 供应商ID:43'),(24,'9Fze4ywiU','IN',2309,18,'2025-03-10 04:07:58',2,'采购入库, 供应商ID:34'),(25,'ItLuuPDv3','IN',2212,17,'2025-03-10 04:08:00',2,'采购入库, 供应商ID:34'),(26,'PyvfHzDWv','IN',4818,21,'2025-03-10 04:08:01',2,'采购入库, 供应商ID:42'),(27,'8bHVmDggx','IN',517,21,'2025-03-10 04:08:02',2,'采购入库, 供应商ID:37'),(28,'9Ov6GcOBP','IN',332,11,'2025-03-10 04:08:03',2,'采购入库, 供应商ID:30'),(29,'TkePsGped','IN',1961,30,'2025-03-10 04:08:49',2,'采购入库, 供应商ID:31'),(30,'FBvmsy6GR','IN',543,34,'2025-03-10 04:08:51',2,'采购入库, 供应商ID:42'),(31,'ev4zvTVhe','IN',3379,31,'2025-03-10 04:08:52',2,'采购入库, 供应商ID:41'),(32,'DRXivRdNY','IN',901,20,'2025-03-10 04:08:53',2,'采购入库, 供应商ID:40'),(33,'2MJLVn0Lf','IN',1807,23,'2025-03-10 04:08:54',2,'采购入库, 供应商ID:38'),(34,'9dx5BysQi','IN',1090,25,'2025-03-10 04:08:55',2,'采购入库, 供应商ID:30'),(35,'MT0xWmchZ','IN',4141,22,'2025-03-10 04:08:55',2,'采购入库, 供应商ID:39'),(36,'Bzv4FSp8C','IN',634,32,'2025-03-10 04:08:56',2,'采购入库, 供应商ID:40'),(37,'dfZZwvmWd','IN',494,32,'2025-03-10 04:08:57',2,'采购入库, 供应商ID:33'),(38,'8T0AH6txf','IN',968,33,'2025-03-10 04:08:57',2,'采购入库, 供应商ID:34'),(39,'5XeQLYukj','IN',2753,20,'2025-03-10 04:08:58',2,'采购入库, 供应商ID:30'),(40,'MQLTm6cKr','IN',4230,31,'2025-03-10 04:08:59',2,'采购入库, 供应商ID:44'),(41,'5BsNcH2Vd','IN',3069,29,'2025-03-10 04:09:00',2,'采购入库, 供应商ID:40'),(42,'yvItiTEkH','IN',4522,21,'2025-03-10 04:09:01',2,'采购入库, 供应商ID:30'),(43,'pdhLtj7e5','IN',2518,22,'2025-03-10 04:09:02',2,'采购入库, 供应商ID:31'),(44,'Ckzz5Z4wS','IN',553,34,'2025-03-10 04:09:03',2,'采购入库, 供应商ID:31'),(45,'Ckzz5Z4wx','IN',200,33,'2025-03-27 02:26:09',1,'采购入库, 供应商ID:29'),(46,'ddsfsdf','IN',120,20,'2025-03-27 05:37:08',1,'采购入库, 供应商ID:29'),(47,'20230801A','OUT',10,1,'2025-04-04 02:15:46',1,'发药出库, 处方单ID:1'),(48,'2MJLVn0Lf','OUT',5,1,'2025-04-04 02:15:46',1,'发药出库, 处方单ID:1'),(49,'9Fze4ywiU','OUT',7,2,'2025-04-04 02:26:45',1,'发药出库, 处方单ID:2'),(50,'2MJLVnOLf','IN',100,23,'2025-04-05 07:20:07',1,'采购入库, 供应商ID:29');
/*!40000 ALTER TABLE `inventory_transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicine_batches`
--

DROP TABLE IF EXISTS `medicine_batches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicine_batches` (
  `batch_no` varchar(50) COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '批次号（如"20230801A"）',
  `medicine_id` bigint NOT NULL COMMENT '关联药品ID',
  `expiry_date` date NOT NULL COMMENT '有效期至',
  `production_date` date NOT NULL COMMENT '生产日期',
  `supplier_id` int DEFAULT NULL COMMENT '供应商ID',
  `initial_quantity` int NOT NULL COMMENT '批次总数量',
  `storage_location` varchar(100) COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '库存位置（如"门诊药房1区"）',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
  PRIMARY KEY (`batch_no`),
  KEY `supplier_id` (`supplier_id`),
  KEY `idx_expiry` (`expiry_date`),
  KEY `idx_medicine` (`medicine_id`),
  CONSTRAINT `medicine_batches_ibfk_1` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`medicine_id`),
  CONSTRAINT `medicine_batches_ibfk_2` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_croatian_ci COMMENT='药品批次管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicine_batches`
--

LOCK TABLES `medicine_batches` WRITE;
/*!40000 ALTER TABLE `medicine_batches` DISABLE KEYS */;
INSERT INTO `medicine_batches` VALUES ('20230801A',18,'2013-08-17','1978-04-25',29,1000,'门诊药房1区','2025-03-06 03:15:47'),('2MJLVn0Lf',23,'2028-10-31','2024-12-26',38,1807,'88室','2025-03-10 04:08:54'),('2MJLVnOLf',23,'2025-05-10','2025-03-30',29,100,'','2025-04-05 07:20:07'),('3lxmKMe3m',30,'2026-06-12','2024-06-24',31,636,'58号门牌','2025-03-10 04:05:33'),('5BsNcH2Vd',29,'2028-01-22','2023-04-30',40,3069,'42号楼','2025-03-10 04:09:00'),('5XeQLYukj',20,'2025-06-07','2025-01-20',30,2753,'73室','2025-03-10 04:08:58'),('8bHVmDggx',21,'2029-05-01','2023-06-25',37,517,'6号房间','2025-03-10 04:08:02'),('8T0AH6txf',33,'2030-01-27','2024-07-01',34,968,'18单元','2025-03-10 04:08:57'),('9dx5BysQi',25,'2028-09-20','2023-06-10',30,1090,'54单元','2025-03-10 04:08:55'),('9Fze4ywiU',18,'2025-06-22','2024-11-01',34,2309,'42号院','2025-03-10 04:07:58'),('9Ov6GcOBP',11,'2025-09-04','2023-12-12',30,332,'13号门牌','2025-03-10 04:08:03'),('AF676qMj9',22,'2029-06-05','2024-07-31',33,3952,'84单元','2025-03-10 04:05:29'),('aq0siJ3oQ',22,'2029-10-04','2023-11-11',32,307,'99单元','2025-03-10 04:04:01'),('Bzv4FSp8C',32,'2027-08-26','2024-06-03',40,634,'91室','2025-03-10 04:08:56'),('Ckzz5Z4wS',34,'2028-04-08','2023-04-25',31,553,'67号楼','2025-03-10 04:09:03'),('Ckzz5Z4wx',33,'2037-03-28','2020-03-02',29,200,'','2025-03-27 02:26:09'),('ddsfsdf',20,'2025-03-29','2025-02-23',29,120,'','2025-03-27 05:37:08'),('dfZZwvmWd',32,'2028-08-04','2024-07-18',33,494,'93室','2025-03-10 04:08:57'),('DRXivRdNY',20,'2029-12-26','2023-03-16',40,901,'42号楼','2025-03-10 04:08:53'),('ev4zvTVhe',31,'2029-05-05','2024-03-06',41,3379,'36层','2025-03-10 04:08:52'),('FBvmsy6GR',34,'2026-08-25','2023-07-26',42,543,'65号门牌','2025-03-10 04:08:51'),('g1Mbqjymn',28,'2027-02-26','2024-05-21',34,1054,'57号楼','2025-03-10 04:05:35'),('gk5wM2F16',25,'2028-04-04','2024-02-01',47,4074,'75单元','2025-03-10 04:07:04'),('i8PORs1Gc',25,'2026-05-01','2024-05-16',43,1566,'45单元','2025-03-10 04:07:56'),('ItLuuPDv3',17,'2025-10-03','2023-12-29',34,2212,'94层','2025-03-10 04:08:00'),('KT2PAMLn3',26,'2028-03-28','2024-07-07',41,1843,'89层','2025-03-10 04:05:19'),('MQLTm6cKr',31,'2026-01-03','2023-12-18',44,4230,'88号楼','2025-03-10 04:08:59'),('MT0xWmchZ',22,'2029-03-15','2024-10-27',39,4141,'33室','2025-03-10 04:08:55'),('pdhLtj7e5',22,'2026-04-05','2025-01-31',31,2518,'9号房间','2025-03-10 04:09:02'),('PXscf7XXp',28,'2028-12-04','2023-11-19',38,3651,'11单元','2025-03-10 04:07:03'),('PyvfHzDWv',21,'2027-07-07','2023-09-01',42,4818,'12号院','2025-03-10 04:08:01'),('q7DvdyK5o',31,'2026-02-25','2024-10-18',36,2704,'71号院','2025-03-10 04:05:31'),('qtw8txsNN',19,'2029-08-28','2023-12-02',40,4505,'93室','2025-03-10 04:07:02'),('rBb9mKYa7',25,'2029-06-26','2023-08-19',39,1569,'74号楼','2025-03-10 04:07:08'),('rDLNPK045',33,'2026-09-23','2023-05-21',46,4307,'89号楼','2025-03-10 04:07:05'),('RXC1Zbh4b',27,'2027-12-08','2023-07-27',36,2458,'14号院','2025-03-10 04:05:16'),('SfTkWs55Y',25,'2027-06-27','2024-07-15',35,4394,'25单元','2025-03-10 04:04:04'),('TkePsGped',30,'2029-02-20','2025-03-06',31,1961,'12层','2025-03-10 04:08:49'),('VTJhHBmlP',34,'2027-06-24','2023-04-03',31,2228,'96号门牌','2025-03-10 04:05:38'),('yvItiTEkH',21,'2025-12-13','2024-07-09',30,4522,'4号房间','2025-03-10 04:09:01');
/*!40000 ALTER TABLE `medicine_batches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicine_categories`
--

DROP TABLE IF EXISTS `medicine_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicine_categories` (
  `category_id` int NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `category_name` varchar(100) COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '分类名称（如"抗生素类"）',
  `parent_id` int DEFAULT NULL COMMENT '父分类ID',
  `description` text COLLATE utf8mb4_croatian_ci COMMENT '分类描述',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序序号',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除（0-正常，1-删除）',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`category_id`),
  KEY `fk_parent_category` (`parent_id`),
  CONSTRAINT `fk_parent_category` FOREIGN KEY (`parent_id`) REFERENCES `medicine_categories` (`category_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_croatian_ci COMMENT='药品分类表（支持多级分类）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicine_categories`
--

LOCK TABLES `medicine_categories` WRITE;
/*!40000 ALTER TABLE `medicine_categories` DISABLE KEYS */;
INSERT INTO `medicine_categories` VALUES (1,'全部药品',NULL,'根目录',0,0,'2025-03-05 08:08:09','2025-03-22 10:17:31'),(2,'未分类',1,'未分类',0,0,'2025-03-05 08:08:09','2025-03-26 06:04:39'),(3,'抗生素类',1,'抗生素类',1,0,'2025-03-09 14:00:36','2025-03-22 10:17:31'),(4,'头孢类',1,'',1,0,'2025-03-10 01:43:30','2025-03-26 05:14:04'),(5,'血清',1,'',1,0,'2025-03-26 04:58:25','2025-03-26 06:13:21'),(6,'发士大夫',5,'',2,0,'2025-03-26 05:49:18','2025-03-26 06:13:21'),(7,'烦烦烦',2,'',7,1,'2025-03-26 05:59:03','2025-03-26 06:13:21');
/*!40000 ALTER TABLE `medicine_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicines`
--

DROP TABLE IF EXISTS `medicines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicines` (
  `medicine_id` bigint NOT NULL AUTO_INCREMENT COMMENT '药品唯一标识',
  `name` varchar(200) COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '药品通用名（如"阿莫西林胶囊"）',
  `specification` varchar(50) COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '规格（如"0.25g*12粒/盒"）',
  `brand` varchar(100) COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '生产商/品牌（如"辉瑞制药"）',
  `category_id` int DEFAULT NULL COMMENT '药品分类ID（外键关联分类表）',
  `unit` varchar(20) COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '计量单位（盒/支/瓶）',
  `price` decimal(10,2) NOT NULL COMMENT '单价（含税）',
  `is_generic` tinyint(1) DEFAULT '0' COMMENT '是否为仿制药',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`medicine_id`),
  KEY `idx_category` (`category_id`),
  KEY `idx_name` (`name`(50))
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_croatian_ci COMMENT='药品基础信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicines`
--

LOCK TABLES `medicines` WRITE;
/*!40000 ALTER TABLE `medicines` DISABLE KEYS */;
INSERT INTO `medicines` VALUES (11,'jdeqf','zVgll','yzvqV',1,'EcBmE',583.59,1,'2025-03-10 02:34:22','2025-03-22 10:23:40'),(14,'KUwmR','nDzCk','rDuYj',1,'LtsSC',376.61,1,'2025-03-10 02:34:04','2025-03-10 02:40:06'),(17,'InQlf','uEebg','xJFAd',1,'vNIdC',509.67,0,'2025-03-10 02:33:50','2025-03-10 02:33:50'),(18,'阿莫西胶囊','0.25*12粒/盒','辉瑞制药',3,'盒',942.99,0,'2025-03-05 08:08:40','2025-03-10 01:55:46'),(19,'尔杰','culpa occaecat nisi','nisi dolore esse',3,'ea',619.55,1,'2025-03-10 02:21:25','2025-03-10 02:31:06'),(20,'SmeoT','YFHBE','PEqvO',4,'PpmST',705.05,0,'2025-03-10 02:34:10','2025-03-23 08:00:04'),(21,'rmTNI','aztRZ','CwpJB',1,'FkQMv',298.04,1,'2025-03-10 02:34:17','2025-03-10 02:55:23'),(22,'HVnPa','dxZQw','XFhki',2,'gfuDw',816.36,0,'2025-03-10 02:33:47','2025-03-22 10:51:15'),(23,'cExpI','SbNSc','mYuOp',1,'JyTqv',385.54,1,'2025-03-10 02:33:57','2025-03-23 05:55:08'),(24,'jgpnZ','gPRJH','BmaUL',2,'nVPXm',532.29,1,'2025-03-10 02:33:59','2025-03-22 10:51:15'),(25,'AaFrZ','WbwyA','IYuiI',2,'rInvD',637.13,1,'2025-03-10 02:34:08','2025-03-22 10:51:15'),(26,'fOsyp','ckCQQ','lHddX',4,'NOEbP',915.72,1,'2025-03-10 02:36:59','2025-03-22 10:30:15'),(27,'ycnXo','iEcCr','aEVgk',2,'ssVzs',63.67,0,'2025-03-10 02:33:44','2025-03-22 10:51:15'),(28,'qdGBI','whRnl','LrPUL',4,'PvXPn',588.58,1,'2025-03-10 02:34:06','2025-03-22 10:30:16'),(29,'tyPpT','tVjUZ','bYdKl',4,'Cuaqa',625.64,1,'2025-03-10 02:33:41','2025-03-22 10:30:16'),(30,'YQKGR','BqmZn','VRKKy',4,'XGQKD',941.27,1,'2025-03-10 02:34:19','2025-03-22 10:30:15'),(31,'DEhSX','lMEtG','MHRPh',1,'kuZTL',464.92,1,'2025-03-10 02:33:52','2025-03-10 02:55:23'),(32,'dUbKW','csmdj','XcZhi',1,'qyabE',925.31,1,'2025-03-10 02:34:14','2025-03-22 10:30:00'),(33,'zSyQa','XUlSh','KJIfB',1,'CslMT',188.93,0,'2025-03-10 02:33:54','2025-03-22 10:30:00'),(34,'ZalQG','LBqmq','vLKQg',3,'obZpj',215.84,0,'2025-03-10 02:34:02','2025-03-10 02:55:23'),(100,'打发打发','3434','BmaUL',2,'打发士大夫',233.00,1,'2025-03-23 08:26:51','2025-03-23 08:31:47');
/*!40000 ALTER TABLE `medicines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patients` (
  `patient_id` int NOT NULL AUTO_INCREMENT COMMENT '患者唯一标识',
  `name` varchar(100) COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '患者姓名',
  `gender` varchar(25) COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT ' 0:女 1:男',
  `birthdate` date DEFAULT NULL COMMENT '出生日期',
  `contact_phone` varchar(20) COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '联系电话',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建档时间',
  PRIMARY KEY (`patient_id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_croatian_ci COMMENT='患者信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;
/*!40000 ALTER TABLE `patients` DISABLE KEYS */;
INSERT INTO `patients` VALUES (1,'鄞中海','1','1982-11-20','14848173998','2025-03-08 12:41:49'),(11,'张国栋','1','2017-04-13','19777436448','2025-03-08 12:41:47'),(15,'山诗雨','0','2020-06-16','18456991659','2025-03-08 12:42:02'),(22,'吾振东','0','1949-11-08','13115195109','2025-03-08 12:41:42'),(30,'登婷方','0','1944-11-15','19981412825','2025-03-08 12:41:53'),(38,'么国珍','1','2000-02-06','13412343846','2025-03-08 12:41:51'),(39,'丛倩','0','1946-08-01','19178234494','2025-03-19 12:47:34'),(45,'赏帅','1','1952-08-02','19568853585','2025-03-08 12:41:44'),(46,'齐建华','1','2021-03-26','15082957526','2025-03-08 12:41:29'),(49,'之兰英','0','2009-12-09','18538190851','2025-03-08 12:41:39'),(50,'苦浩轩','0','1999-03-30','16090779176','2025-03-08 12:36:52'),(53,'屈国秀','1','1972-11-21','18649095458','2025-03-08 12:41:27'),(54,'示辉','0','2008-01-30','13132459830','2025-03-08 12:41:34'),(59,'米丹','1','2017-12-21','14710323840','2025-03-08 12:41:32'),(68,'竹熙瑶','0','1999-05-30','16957279039','2025-03-08 12:41:24'),(78,'庞浩','1','1997-01-30','13554263680','2025-03-19 12:48:13'),(96,'郁燕','1','1953-12-12','18348368784','2025-03-08 12:41:37'),(99,'诸葛艺涵','0','1982-07-06','15142087891','2025-03-09 05:27:57'),(100,'鱼洁','0','1977-11-14','18559787885','2025-03-09 02:52:56'),(101,'小米','0','2015-03-18','17345678958','2025-03-19 12:49:17');
/*!40000 ALTER TABLE `patients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescription_medicines`
--

DROP TABLE IF EXISTS `prescription_medicines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescription_medicines` (
  `record_id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `prescription_id` bigint NOT NULL COMMENT '处方单ID',
  `medicine_id` bigint NOT NULL COMMENT '药品ID',
  `batch_no` varchar(50) COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '发放批次号',
  `dosage` varchar(50) COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '用法用量（如"每次1片，每日3次"）',
  `quantity_dispensed` int NOT NULL COMMENT '发放数量',
  PRIMARY KEY (`record_id`),
  KEY `medicine_id` (`medicine_id`),
  KEY `batch_no` (`batch_no`),
  KEY `idx_prescription` (`prescription_id`),
  CONSTRAINT `prescription_medicines_ibfk_2` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`medicine_id`),
  CONSTRAINT `prescription_medicines_ibfk_3` FOREIGN KEY (`batch_no`) REFERENCES `medicine_batches` (`batch_no`),
  CONSTRAINT `prescription_medicines_prescriptions_prescription_id_fk` FOREIGN KEY (`prescription_id`) REFERENCES `prescriptions` (`prescription_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_croatian_ci COMMENT='处方药品明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription_medicines`
--

LOCK TABLES `prescription_medicines` WRITE;
/*!40000 ALTER TABLE `prescription_medicines` DISABLE KEYS */;
INSERT INTO `prescription_medicines` VALUES (1,1,18,'20230801A','1片/次',10),(2,1,23,'2MJLVn0Lf','2片/次',5),(3,2,18,'9Fze4ywiU','1片/次',7);
/*!40000 ALTER TABLE `prescription_medicines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescriptions`
--

DROP TABLE IF EXISTS `prescriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescriptions` (
  `prescription_id` bigint NOT NULL AUTO_INCREMENT COMMENT '处方单ID',
  `patient_id` int NOT NULL COMMENT '患者ID',
  `doctor_id` int NOT NULL COMMENT '开方医生ID',
  `prescription_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开方时间',
  `status` varchar(10) COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '执行状态',
  `dispensed_by` int DEFAULT NULL COMMENT '发药人（关联users.user_id）',
  `dispensed_at` timestamp NULL DEFAULT NULL COMMENT '发药时间',
  PRIMARY KEY (`prescription_id`),
  KEY `dispensed_by` (`dispensed_by`),
  KEY `idx_doctor` (`doctor_id`),
  KEY `idx_patient` (`patient_id`),
  CONSTRAINT `prescriptions_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`patient_id`),
  CONSTRAINT `prescriptions_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `staff` (`staff_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_croatian_ci COMMENT='处方单主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescriptions`
--

LOCK TABLES `prescriptions` WRITE;
/*!40000 ALTER TABLE `prescriptions` DISABLE KEYS */;
INSERT INTO `prescriptions` VALUES (1,1,1,'2025-03-11 08:52:20','2',1,'2025-04-04 02:15:46'),(2,1,1,'2025-04-01 09:58:56','2',1,'2025-04-04 02:26:45');
/*!40000 ALTER TABLE `prescriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_orders`
--

DROP TABLE IF EXISTS `purchase_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_orders` (
  `po_id` bigint NOT NULL AUTO_INCREMENT COMMENT '采购订单ID',
  `supplier_id` int NOT NULL COMMENT '供应商ID',
  `order_date` date NOT NULL COMMENT '下单日期',
  `expected_delivery_date` date DEFAULT NULL COMMENT '预计到货日期',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '订单总金额',
  `status` varchar(20) COLLATE utf8mb4_croatian_ci DEFAULT 'pending' COMMENT '订单状态',
  `created_by` int DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`po_id`),
  KEY `idx_status` (`status`),
  KEY `idx_supplier` (`supplier_id`),
  CONSTRAINT `purchase_orders_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_croatian_ci COMMENT='采购订单主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_orders`
--

LOCK TABLES `purchase_orders` WRITE;
/*!40000 ALTER TABLE `purchase_orders` DISABLE KEYS */;
INSERT INTO `purchase_orders` VALUES (1,43,'2024-08-23','2026-02-16',11274.74,'cancelled',2),(2,47,'2024-12-29','2025-10-10',41389.74,'cancelled',2),(3,38,'2024-07-01','2025-08-09',32572.69,'cancelled',2),(4,37,'2024-08-16','2025-09-30',73891.39,'cancelled',2),(5,33,'2024-09-19','2025-07-31',77110.08,'pending',2),(6,41,'2024-03-16','2025-07-29',56995.81,'pending',2),(7,29,'2024-04-10','2025-10-22',74877.26,'pending',2),(8,42,'2024-07-03','2025-12-16',42895.39,'pending',2),(9,41,'2024-05-17','2025-07-18',20764.50,'pending',2),(10,44,'2024-04-24','2025-10-10',30498.05,'pending',2),(11,31,'2024-03-10','2025-09-02',90925.99,'pending',2),(12,47,'2024-06-17','2025-07-09',39880.05,'pending',2),(13,44,'2025-02-14','2025-07-20',47888.81,'pending',2),(14,45,'2024-12-29','2026-01-09',37960.45,'cancelled',2),(15,34,'2024-04-27','2025-06-19',41109.22,'pending',2),(16,37,'2024-03-21','2026-02-11',16697.28,'pending',2),(17,33,'2024-04-13','2025-08-14',63395.53,'pending',2),(18,34,'2024-07-27','2025-06-15',18025.84,'pending',2),(19,38,'2024-04-07','2025-03-17',22444.64,'pending',2),(20,34,'2024-10-13','2025-05-25',46051.17,'pending',2),(21,47,'2024-10-04','2025-09-22',42305.15,'pending',2),(22,43,'2025-01-01','2025-12-21',24541.66,'pending',2),(23,41,'2024-07-19','2025-07-23',11720.62,'pending',2),(24,38,'2024-12-24','2025-10-04',18187.86,'pending',2),(25,47,'2024-08-07','2025-07-30',64280.74,'pending',2),(26,42,'2024-11-27','2026-02-14',38162.84,'pending',2),(27,32,'2024-05-30','2026-01-03',40446.65,'pending',2),(28,44,'2024-09-22','2025-07-02',74639.94,'pending',2),(29,33,'2024-04-09','2025-09-21',35514.95,'pending',2),(30,44,'2024-11-20','2025-10-08',12619.73,'pending',2),(31,30,'2024-12-05','2025-06-17',16247.58,'pending',2),(32,45,'2024-09-23','2025-12-05',51795.43,'pending',2),(33,43,'2024-08-23','2026-02-16',11274.74,'pending',2),(34,45,'2025-04-05','2025-05-03',7878.00,'pending',1),(35,29,'2025-04-05','2025-04-19',10000.00,'completed',1),(36,43,'2025-04-05','2025-05-10',10000.00,'completed',1);
/*!40000 ALTER TABLE `purchase_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_records`
--

DROP TABLE IF EXISTS `purchase_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_records` (
  `record_id` bigint NOT NULL AUTO_INCREMENT COMMENT '采购记录ID',
  `po_id` bigint NOT NULL COMMENT '关联订单ID',
  `medicine_id` bigint NOT NULL COMMENT '药品ID',
  `batch_no` varchar(50) COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '入库批次号',
  `quantity` int NOT NULL COMMENT '采购数量',
  `unit_price` decimal(10,2) DEFAULT NULL COMMENT '采购单价',
  PRIMARY KEY (`record_id`),
  KEY `medicine_id` (`medicine_id`),
  KEY `batch_no` (`batch_no`),
  KEY `idx_po` (`po_id`),
  CONSTRAINT `purchase_records_ibfk_1` FOREIGN KEY (`po_id`) REFERENCES `purchase_orders` (`po_id`),
  CONSTRAINT `purchase_records_ibfk_2` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`medicine_id`),
  CONSTRAINT `purchase_records_ibfk_3` FOREIGN KEY (`batch_no`) REFERENCES `medicine_batches` (`batch_no`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_croatian_ci COMMENT='采购明细记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_records`
--

LOCK TABLES `purchase_records` WRITE;
/*!40000 ALTER TABLE `purchase_records` DISABLE KEYS */;
INSERT INTO `purchase_records` VALUES (67,2,23,'2MJLVn0Lf',1807,385.54),(68,1,23,'2MJLVn0Lf',1807,385.54),(71,3,23,'2MJLVn0Lf',122,2300.00),(73,23,23,'2MJLVn0Lf',100,50.00),(74,35,23,'2MJLVn0Lf',100,50.00),(75,36,23,'2MJLVn0Lf',1000,50.00);
/*!40000 ALTER TABLE `purchase_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `role_id` int NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` varchar(100) COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '角色名称',
  `permissions` json NOT NULL COMMENT '权限配置（JSON数组）',
  `description` text COLLATE utf8mb4_croatian_ci COMMENT '角色描述',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_croatian_ci COMMENT='角色权限配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (13,'Admin','[\"AuditLogs:create\", \"AuditLogs:read\", \"AuditLogs:update\", \"AuditLogs:delete\", \"Departments:create\", \"Departments:read\", \"Departments:update\", \"Departments:delete\", \"Inventory:create\", \"Inventory:read\", \"Inventory:update\", \"Inventory:delete\", \"InventoryTransactions:create\", \"InventoryTransactions:read\", \"InventoryTransactions:update\", \"InventoryTransactions:delete\", \"MedicineBatches:create\", \"MedicineBatches:read\", \"MedicineBatches:update\", \"MedicineBatches:delete\", \"MedicineCategories:create\", \"MedicineCategories:read\", \"MedicineCategories:update\", \"MedicineCategories:delete\", \"Medicines:create\", \"Medicines:read\", \"Medicines:update\", \"Medicines:delete\", \"Patients:create\", \"Patients:read\", \"Patients:update\", \"Patients:delete\", \"PrescriptionMedicines:create\", \"PrescriptionMedicines:read\", \"PrescriptionMedicines:update\", \"PrescriptionMedicines:delete\", \"Prescriptions:create\", \"Prescriptions:read\", \"Prescriptions:update\", \"Prescriptions:delete\", \"PurchaseOrders:create\", \"PurchaseOrders:read\", \"PurchaseOrders:update\", \"PurchaseOrders:delete\", \"PurchaseRecords:create\", \"PurchaseRecords:read\", \"PurchaseRecords:update\", \"PurchaseRecords:delete\", \"Roles:create\", \"Roles:read\", \"Roles:update\", \"Roles:delete\", \"Staff:create\", \"Staff:read\", \"Staff:update\", \"Staff:delete\", \"StockAlerts:create\", \"StockAlerts:read\", \"StockAlerts:update\", \"StockAlerts:delete\", \"Suppliers:create\", \"Suppliers:read\", \"Suppliers:update\", \"Suppliers:delete\", \"Users:create\", \"Users:read\", \"Users:update\", \"Users:delete\"]','管理员'),(15,'Procurement','[\"PurchaseOrders:create\", \"PurchaseOrders:read\", \"PurchaseOrders:update\", \"PurchaseRecords:create\", \"PurchaseRecords:read\", \"PurchaseRecords:update\", \"Suppliers:read\", \"Inventory:read\", \"MedicineBatches:read\"]','采购员'),(16,'Doctor','[\"Patients:create\", \"Patients:read\", \"Patients:update\", \"Prescriptions:create\", \"Prescriptions:read\", \"Prescriptions:update\", \"PrescriptionMedicines:create\", \"PrescriptionMedicines:read\", \"Medicines:read\", \"MedicineCategories:read\"]','医生'),(17,'Pharmacist','[\"AuditLogs:read\"]','药剂师');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff` (
  `staff_id` int NOT NULL AUTO_INCREMENT COMMENT '员工ID',
  `name` varchar(100) COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '姓名',
  `title` varchar(50) COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '职称（如主任医师）',
  `department` varchar(100) COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '所属科室',
  `is_active` int DEFAULT '1' COMMENT '是否在职 0: 在职 1: 离职',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入职时间',
  PRIMARY KEY (`staff_id`),
  UNIQUE KEY `staff_name_uindex` (`name`),
  KEY `idx_department` (`department`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_croatian_ci COMMENT='医护人员信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES (1,'小名','主任医师','眼科',0,'2025-02-25 14:44:57'),(2,'颜浩','助手','眼科',0,'2025-03-08 02:14:59'),(3,'小红','护士','眼科',0,'2025-03-08 02:16:05'),(4,'力语汐','护士','眼科',0,'2025-03-08 02:16:55'),(5,'姚智杰','护士','眼科',0,'2025-03-08 02:17:29'),(6,'羿若汐','护士','眼科',0,'2025-03-08 02:17:46'),(7,'良语汐','护士','眼科',0,'2025-03-08 02:17:58'),(8,'农霞','护士','眼科',0,'2025-03-08 02:18:33'),(9,'林磊','护士','眼科',0,'2025-03-08 02:18:43'),(10,'董丽','护士','眼科',1,'2025-03-08 02:18:53'),(11,'小米','护士','眼科',1,'2025-03-20 04:57:18');
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_alerts`
--

DROP TABLE IF EXISTS `stock_alerts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_alerts` (
  `alert_id` bigint NOT NULL AUTO_INCREMENT COMMENT '预警规则ID',
  `medicine_id` bigint NOT NULL COMMENT '关联药品ID',
  `min_quantity` int NOT NULL COMMENT '最低库存阈值',
  `alert_level` enum('low','medium','high') COLLATE utf8mb4_croatian_ci DEFAULT 'low' COMMENT '预警级别',
  `notification_methods` json DEFAULT NULL COMMENT '通知方式（JSON数组）',
  `is_enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用',
  PRIMARY KEY (`alert_id`),
  KEY `idx_medicine` (`medicine_id`),
  CONSTRAINT `stock_alerts_ibfk_1` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`medicine_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_croatian_ci COMMENT='库存预警规则配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_alerts`
--

LOCK TABLES `stock_alerts` WRITE;
/*!40000 ALTER TABLE `stock_alerts` DISABLE KEYS */;
INSERT INTO `stock_alerts` VALUES (2,18,450,'low','[\"邮件通知:email\", \"sms:短信\", \"system:系统\"]',1),(3,21,300,'low','[\"邮件通知:email\"]',1);
/*!40000 ALTER TABLE `stock_alerts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `supplier_id` int NOT NULL AUTO_INCREMENT COMMENT '供应商ID',
  `name` varchar(200) COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '供应商名称',
  `contact_person` varchar(100) COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '联系人',
  `phone` varchar(20) COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '联系电话',
  `address` text COLLATE utf8mb4_croatian_ci COMMENT '联系地址',
  `contract_end_date` date DEFAULT NULL COMMENT '合同到期日',
  PRIMARY KEY (`supplier_id`),
  KEY `idx_name` (`name`(50))
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_croatian_ci COMMENT='供应商信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
INSERT INTO `suppliers` VALUES (29,'辉瑞制药','李白','13728430841','江苏省 西汉市 黄大仙区 士旁3号 14层','2030-02-07'),(30,'包都市哲瀚旅游发展有限公司','赏国英','14685393173','重庆市 海码市 斗门区 乜路6518号 99层','2027-09-16'),(31,'北京市烨华矿业（集团）有限公司','廖帅','15812509721','海南省 太原市 嘉定区 督巷88号 98室','2027-10-15'),(32,'济阳市瑾瑜网络科技股份有限公司','皋丽芳','17461777737','海南省 包原市 印台区 巧中心6125号 43单元','2028-01-10'),(33,'辽宁省雪松印刷股份有限公司','钞洁','16848394444','江苏省 诸宁市 务川仡佬族苗族自治县 留侬1号 60层','2027-07-24'),(34,'长乡县荣轩水产有限责任公司','祈超栋','15555722236','贵州省 海徽市 寿宁县 旅巷9号 17号楼','2027-04-09'),(35,'宁海市擎苍食品（集团）有限公司','毋敬阳','14441445748','上海市 包乡县 金溪县 悉街1号 78室','2027-07-14'),(36,'山东省雪松燃气（集团）有限公司','笃辉','19385826864','吉林省 成阳市 寿宁县 酒栋64号 63号门牌','2027-10-08'),(37,'海门市鹤轩印刷有限责任公司','欧阳波','14476791449','海南省 宁海市 海宁市 委旁61号 12室','2027-09-08'),(38,'上原市天宇保险集团有限公司','扈榕融','15574804631','吉林省 太安市 张北县 书巷65513号 11号楼','2028-01-05'),(39,'珠宁市哲瀚传媒（集团）有限公司','繁梓豪','18433096487','辽宁省 贵京市 石渠县 元侬1号 57室','2027-10-27'),(40,'福头市雅鑫食品无限公司','掌治文','16054152342','四川省 成林市 广灵县 全栋30435号 41层','2028-01-06'),(41,'珠沙市文博林业（集团）有限公司','频文韬','16720431547','重庆市 武乡县 长安区 尾旁3674号 73室','2027-03-26'),(42,'厦安市金鑫水产有限责任公司','函蒙','18211297524','河北省 济海市 霍城县 福路6436号 86号门牌','2027-09-05'),(43,'吉都市楷瑞旅游发展无限责任公司','余治文','17632570714','湖南省 诸徽市 新化县 蒋街8号 73号楼','2028-02-25'),(44,'济原市癸霖物流无限公司','言杰','18157823472','广东省 武乡县 界首市 税街325号 5号房间','2028-01-15'),(45,'长口市鑫磊物流（集团）有限公司','完国辉','18780351748','广西壮族自治区 珠码市 向阳区 符中心1号 66号楼','2028-02-01'),(46,'河南省子涵旅游发展有限公司','书建国','19192809320','贵州省 福头市 涵江区 利巷82136号 59号楼','2027-12-26'),(47,'青海省鹏飞运输（集团）有限公司','圣俊凯','19114461871','新疆维吾尔自治区 武原市 兴城市 伊栋12308号 57号院','2027-10-09');
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(100) COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '登录账号',
  `password_hash` varchar(255) COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '加密密码',
  `role_id` int NOT NULL COMMENT '角色ID',
  `department` varchar(100) COLLATE utf8mb4_croatian_ci DEFAULT NULL COMMENT '所属部门',
  `last_login` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `department_id` int DEFAULT NULL COMMENT '关联部门表',
  `status` int NOT NULL COMMENT '0,启用 1,弃用',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  KEY `role_id` (`role_id`),
  KEY `idx_username` (`username`),
  KEY `department_id` (`department_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
  CONSTRAINT `users_ibfk_2` FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_croatian_ci COMMENT='系统用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'小名','$2a$10$sJn0Yz4J79jC8k8jN0hZW.bRLhEyQF1kaRKG.JoJ3q/.JawKPTyiS',13,'眼科','2025-04-09 05:29:08','2025-02-25 14:39:51',1,0),(3,'3333','$2a$10$sJn0Yz4J79jC8k8jN0hZW.bRLhEyQF1kaRKG.JoJ3q/.JawKPTyiS',15,'急诊科',NULL,'2025-03-07 12:03:27',12,0),(4,'4444','$2a$10$sJn0Yz4J79jC8k8jN0hZW.bRLhEyQF1kaRKG.JoJ3q/.JawKPTyiS',16,'急诊科',NULL,'2025-03-07 12:03:27',12,0),(5,'5555','$2a$10$sJn0Yz4J79jC8k8jN0hZW.bRLhEyQF1kaRKG.JoJ3q/.JawKPTyiS',17,'急诊科','2025-03-15 05:16:18','2025-03-07 12:03:27',12,0),(14,'6666','$2a$10$8cUBcM0RqqHSgl0ymrrDLep1qvZJAEawqqFtf10oA4gURdNAXUy9e',13,'急诊科','2025-03-15 14:19:01','2025-03-11 05:02:15',12,1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-12 17:09:51
