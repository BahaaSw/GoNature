-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: gonature
-- ------------------------------------------------------
-- Server version	8.0.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cancellationreport`
--

DROP TABLE IF EXISTS `cancellationreport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cancellationreport` (
  `ReportId` int NOT NULL,
  `CreationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`ReportId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cancellationreport`
--

LOCK TABLES `cancellationreport` WRITE;
/*!40000 ALTER TABLE `cancellationreport` DISABLE KEYS */;
INSERT INTO `cancellationreport` VALUES (1,'2023-03-17 00:00:00'),(2,'2023-03-17 01:00:00'),(3,'2023-03-17 02:00:00'),(4,'2023-03-17 03:00:00'),(5,'2023-03-17 04:00:00'),(6,'2023-03-17 05:00:00'),(7,'2023-03-17 06:00:00'),(8,'2023-03-17 07:00:00'),(9,'2023-03-17 08:00:00'),(10,'2023-03-17 09:00:00'),(11,'2023-03-17 10:00:00'),(12,'2023-03-17 11:00:00'),(13,'2023-03-17 12:00:00'),(14,'2023-03-17 13:00:00'),(15,'2023-03-17 14:00:00'),(16,'2023-03-17 15:00:00'),(17,'2023-03-17 16:00:00'),(18,'2023-03-17 17:00:00'),(19,'2023-03-17 18:00:00'),(20,'2023-03-17 19:00:00');
/*!40000 ALTER TABLE `cancellationreport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cancellationreportgenerator`
--

DROP TABLE IF EXISTS `cancellationreportgenerator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cancellationreportgenerator` (
  `reportId` int NOT NULL,
  `year` int DEFAULT NULL,
  `month` int DEFAULT NULL,
  PRIMARY KEY (`reportId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cancellationreportgenerator`
--

LOCK TABLES `cancellationreportgenerator` WRITE;
/*!40000 ALTER TABLE `cancellationreportgenerator` DISABLE KEYS */;
INSERT INTO `cancellationreportgenerator` VALUES (1,2023,1),(2,2023,2),(3,2023,3),(4,2023,4),(5,2023,5),(6,2023,6),(7,2023,7),(8,2023,8),(9,2023,9),(10,2023,10),(11,2023,11),(12,2023,12),(13,2024,1);
/*!40000 ALTER TABLE `cancellationreportgenerator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `completedvisits`
--

DROP TABLE IF EXISTS `completedvisits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `completedvisits` (
  `VisitId` int NOT NULL,
  `ParkId` int DEFAULT NULL,
  `OrderId` int DEFAULT NULL,
  `VisitType` varchar(255) DEFAULT NULL,
  `VisitDate` date DEFAULT NULL,
  `Amount` int DEFAULT NULL,
  `Price` double DEFAULT NULL,
  PRIMARY KEY (`VisitId`),
  KEY `ParkId` (`ParkId`),
  CONSTRAINT `completedvisits_ibfk_1` FOREIGN KEY (`ParkId`) REFERENCES `parks` (`ParkId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `completedvisits`
--

LOCK TABLES `completedvisits` WRITE;
/*!40000 ALTER TABLE `completedvisits` DISABLE KEYS */;
INSERT INTO `completedvisits` VALUES (1,1,101,'Solo','2024-01-15',1,20),(2,2,102,'Solo','2024-01-16',1,30),(3,3,103,'Solo','2024-01-17',1,50),(4,1,104,'Family','2024-01-18',4,80),(5,2,105,'Family','2024-01-19',5,100),(6,3,106,'Family','2024-01-20',6,120),(7,1,107,'Group','2024-01-21',7,140),(8,2,108,'Group','2024-01-22',8,160),(9,3,109,'Group','2024-01-23',9,180),(10,1,110,'Solo','2024-01-24',1,20),(11,2,111,'Solo','2024-01-25',1,30),(12,3,112,'Solo','2024-01-26',1,50),(13,1,113,'Family','2024-01-27',4,80),(14,2,114,'Family','2024-01-28',5,100),(15,3,115,'Family','2024-01-29',6,120),(16,1,116,'Group','2024-01-30',7,140),(17,2,117,'Group','2024-01-31',8,160),(18,3,118,'Group','2024-02-01',9,180),(19,1,119,'Solo','2024-02-02',1,20),(20,2,120,'Solo','2024-02-03',1,30),(21,3,121,'Solo','2024-02-04',1,50),(22,1,122,'Family','2024-02-05',4,80),(23,2,123,'Family','2024-02-06',5,100),(24,3,124,'Family','2024-02-07',6,120),(25,1,125,'Group','2024-02-08',7,140),(26,2,126,'Group','2024-02-09',8,160),(27,3,127,'Group','2024-02-10',9,180),(28,1,128,'Solo','2024-02-11',1,20),(29,2,129,'Solo','2024-02-12',1,30),(30,3,130,'Solo','2024-02-13',1,50),(31,1,131,'Family','2024-02-14',4,80),(32,2,132,'Family','2024-02-15',5,100),(33,3,133,'Family','2024-02-16',6,120),(34,1,134,'Group','2024-02-17',7,140),(35,2,135,'Group','2024-02-18',8,160),(36,3,136,'Group','2024-02-19',9,180),(37,1,137,'Solo','2024-02-20',1,20),(38,2,138,'Solo','2024-02-21',1,30),(39,3,139,'Solo','2024-02-22',1,50),(40,1,140,'Family','2024-02-23',4,80),(41,2,141,'Family','2024-02-24',5,100),(42,3,142,'Family','2024-02-25',6,120),(43,1,143,'Group','2024-02-26',7,140),(44,2,144,'Group','2024-02-27',8,160),(45,3,145,'Group','2024-02-28',9,180),(46,1,146,'Solo','2024-02-29',1,20),(47,2,147,'Solo','2024-03-01',1,30),(48,3,148,'Solo','2024-03-02',1,50),(49,1,149,'Family','2024-03-03',4,80),(50,2,150,'Family','2024-03-04',5,100),(51,3,151,'Family','2024-03-05',6,120),(52,1,152,'Group','2024-03-06',7,140),(53,2,153,'Group','2024-03-07',8,160),(54,3,154,'Group','2024-03-08',9,180),(55,1,155,'Solo','2024-03-09',1,20),(56,2,156,'Solo','2024-03-10',1,30),(57,3,157,'Solo','2024-03-11',1,50),(58,1,158,'Family','2024-03-12',4,80),(59,2,159,'Family','2024-03-13',5,100),(60,3,160,'Family','2024-03-14',6,120),(61,1,161,'Group','2024-03-15',7,140),(62,2,162,'Group','2024-03-16',8,160),(63,3,163,'Group','2024-03-17',9,180),(64,1,164,'Solo','2024-03-18',1,20),(65,2,165,'Solo','2024-03-19',1,30),(66,3,166,'Solo','2024-03-20',1,50),(67,1,167,'Family','2024-03-21',4,80),(68,2,168,'Family','2024-03-22',5,100),(69,3,169,'Family','2024-03-23',6,120),(70,1,170,'Group','2024-03-24',7,140),(71,2,171,'Group','2024-03-25',8,160),(72,3,172,'Group','2024-03-26',9,180),(73,1,173,'Solo','2024-03-27',1,20);
/*!40000 ALTER TABLE `completedvisits` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messages` (
  `MessageId` int NOT NULL,
  `OrderId` int DEFAULT NULL,
  `MessageSentTime` datetime DEFAULT NULL,
  `Status` varchar(255) DEFAULT NULL,
  `Content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`MessageId`),
  KEY `OrderId` (`OrderId`),
  CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`OrderId`) REFERENCES `preorders` (`OrderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `occasionalvisits`
--

DROP TABLE IF EXISTS `occasionalvisits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `occasionalvisits` (
  `OrderId` int NOT NULL,
  `ParkId` int DEFAULT NULL,
  `EnterDate` datetime DEFAULT NULL,
  `ExitDate` datetime DEFAULT NULL,
  `OrderStatus` varchar(255) DEFAULT NULL,
  `Email` varchar(255) DEFAULT NULL,
  `Phone` varchar(255) DEFAULT NULL,
  `FirstName` varchar(255) DEFAULT NULL,
  `LastName` varchar(255) DEFAULT NULL,
  `OrderType` varchar(255) DEFAULT NULL,
  `Amount` int DEFAULT NULL,
  `Price` double DEFAULT NULL,
  PRIMARY KEY (`OrderId`),
  KEY `ParkId` (`ParkId`),
  CONSTRAINT `occasionalvisits_ibfk_1` FOREIGN KEY (`ParkId`) REFERENCES `parks` (`ParkId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `occasionalvisits`
--

LOCK TABLES `occasionalvisits` WRITE;
/*!40000 ALTER TABLE `occasionalvisits` DISABLE KEYS */;
INSERT INTO `occasionalvisits` VALUES (1,1,'2024-04-01 09:30:00','2024-04-01 17:30:00','Wait Notify','JohnDoe@gmail.com','0521234567','John','Doe','Solo',1,28),(2,2,'2024-04-02 10:00:00','2024-04-02 18:00:00','Notified','JaneSmith@gmail.com','0542345678','Jane','Smith','Solo',1,31),(3,3,'2024-04-03 09:45:00','2024-04-03 17:45:00','Confirmed','AliceWonder@gmail.com','0553456789','Alice','Wonder','Family',3,87),(4,1,'2024-04-04 10:30:00','2024-04-04 18:30:00','Cancelled','BobBuilder@gmail.com','0524567890','Bob','Builder','Group',5,140),(5,2,'2024-04-05 11:15:00','2024-04-05 19:15:00','Wait in list','EvaGreen@gmail.com','0575678901','Eva','Green','Group',2,62),(6,3,'2024-04-06 09:30:00','2024-04-06 17:30:00','Wait Notify','MichaelJordan@gmail.com','0586789012','Michael','Jordan','Solo',1,29),(7,1,'2024-04-07 10:00:00','2024-04-07 18:00:00','Notified','JenniferLopez@gmail.com','0597890123','Jennifer','Lopez','Family',4,112),(8,2,'2024-04-08 09:45:00','2024-04-08 17:45:00','Confirmed','GeorgeClooney@gmail.com','0508901234','George','Clooney','Group',3,93),(9,3,'2024-04-09 10:30:00','2024-04-09 18:30:00','Cancelled','AngelinaJolie@gmail.com','0519012345','Angelina','Jolie','Solo',1,29),(10,1,'2024-04-10 11:15:00','2024-04-10 19:15:00','Wait in list','LeonardoDiCaprio@gmail.com','0520123456','Leonardo','DiCaprio','Solo',1,30),(11,2,'2024-04-11 09:30:00','2024-04-11 17:30:00','Wait Notify','ScarlettJohansson@gmail.com','0531234567','Scarlett','Johansson','Family',5,145),(12,3,'2024-04-12 10:00:00','2024-04-12 18:00:00','Notified','BradPitt@gmail.com','0542345678','Brad','Pitt','Group',2,62),(13,1,'2024-04-13 09:45:00','2024-04-13 17:45:00','Confirmed','EmmaWatson@gmail.com','0553456789','Emma','Watson','Group',4,120),(14,2,'2024-04-14 10:30:00','2024-04-14 18:30:00','Cancelled','JohnnyDepp@gmail.com','0564567890','Johnny','Depp','Family',3,87),(15,3,'2024-04-15 11:15:00','2024-04-15 19:15:00','Wait in list','TomCruise@gmail.com','0575678901','Tom','Cruise','Solo',1,30),(16,1,'2024-04-16 09:30:00','2024-04-16 17:30:00','Wait Notify','NicoleKidman@gmail.com','0586789012','Nicole','Kidman','Group',5,145),(17,2,'2024-04-17 10:00:00','2024-04-17 18:00:00','Notified','WillSmith@gmail.com','0597890123','Will','Smith','Family',3,87),(18,3,'2024-04-18 09:45:00','2024-04-18 17:45:00','Confirmed','JenniferAniston@gmail.com','0508901234','Jennifer','Aniston','Solo',1,29),(19,1,'2024-04-19 10:30:00','2024-04-19 18:30:00','Cancelled','ChrisHemsworth@gmail.com','0519012345','Chris','Hemsworth','Group',4,120),(20,2,'2024-04-20 11:15:00','2024-04-20 19:15:00','Wait in list','AngelinaJolie@gmail.com','0520123456','Angelina','Jolie','Family',2,62);
/*!40000 ALTER TABLE `occasionalvisits` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parks`
--

DROP TABLE IF EXISTS `parks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parks` (
  `ParkId` int NOT NULL,
  `ParkName` varchar(255) DEFAULT NULL,
  `MaxCapacity` int DEFAULT NULL,
  `EstimatedVisitTime` decimal(10,2) DEFAULT NULL,
  `ReservedSpots` int DEFAULT NULL,
  `CurrentInPark` int DEFAULT NULL,
  `Price` float DEFAULT NULL,
  PRIMARY KEY (`ParkId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parks`
--

LOCK TABLES `parks` WRITE;
/*!40000 ALTER TABLE `parks` DISABLE KEYS */;
INSERT INTO `parks` VALUES (1,'Banias',100,4.00,20,0,28),(2,'Masada',120,4.00,30,0,31),(3,'Herodium',90,4.00,10,0,29);
/*!40000 ALTER TABLE `parks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preorders`
--

DROP TABLE IF EXISTS `preorders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preorders` (
  `OrderId` int NOT NULL,
  `ParkId` int DEFAULT NULL,
  `OwnerId` int DEFAULT NULL,
  `OwnerType` varchar(255) DEFAULT NULL,
  `EnterDate` datetime DEFAULT NULL,
  `ExitDate` datetime DEFAULT NULL,
  `PayStatus` tinyint(1) DEFAULT NULL,
  `OrderStatus` varchar(255) DEFAULT NULL,
  `Email` varchar(255) DEFAULT NULL,
  `Phone` varchar(255) DEFAULT NULL,
  `FirstName` varchar(255) DEFAULT NULL,
  `LastName` varchar(255) DEFAULT NULL,
  `OrderType` varchar(255) DEFAULT NULL,
  `Amount` int DEFAULT NULL,
  `Price` double DEFAULT NULL,
  PRIMARY KEY (`OrderId`),
  KEY `ParkId` (`ParkId`),
  CONSTRAINT `preorders_ibfk_1` FOREIGN KEY (`ParkId`) REFERENCES `parks` (`ParkId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preorders`
--

LOCK TABLES `preorders` WRITE;
/*!40000 ALTER TABLE `preorders` DISABLE KEYS */;
INSERT INTO `preorders` VALUES (21,1,101,'Guide','2024-04-21 10:00:00','2024-04-21 18:00:00',1,'Confirmed','PeterSmith@gmail.com','0521234567','Peter','Smith','Solo',1,28),(22,2,102,'Visitor','2024-04-22 11:00:00','2024-04-22 19:00:00',0,'Wait in list','MaryJohnson@gmail.com','0542345678','Mary','Johnson','Solo',1,31),(23,3,103,'Guide','2024-04-23 09:30:00','2024-04-23 17:30:00',1,'Notified','RobertBrown@gmail.com','0553456789','Robert','Brown','Family',3,87),(24,1,104,'Visitor','2024-04-24 10:30:00','2024-04-24 18:30:00',1,'Cancelled','LauraMiller@gmail.com','0524567890','Laura','Miller','Group',5,140),(25,2,105,'Guide','2024-04-25 09:15:00','2024-04-25 17:15:00',0,'Wait Notify','PatriciaDavis@gmail.com','0575678901','Patricia','Davis','Group',2,62),(26,3,106,'Visitor','2024-04-26 10:15:00','2024-04-26 18:15:00',1,'Confirmed','RichardWilson@gmail.com','0586789012','Richard','Wilson','Solo',1,29),(27,1,107,'Guide','2024-04-27 12:00:00','2024-04-27 20:00:00',1,'Confirmed','LindaTaylor@gmail.com','0507890123','Linda','Taylor','Solo',1,28),(28,2,108,'Visitor','2024-04-28 09:45:00','2024-04-28 17:45:00',0,'Cancelled','ThomasClark@gmail.com','0518901234','Thomas','Clark','Solo',1,31),(29,3,109,'Guide','2024-04-29 11:30:00','2024-04-29 19:30:00',1,'Done','JenniferEvans@gmail.com','0549012345','Jennifer','Evans','Family',4,116),(30,1,110,'Visitor','2024-04-30 11:45:00','2024-04-30 19:45:00',0,'Wait Notify','ChristopherLee@gmail.com','0550123456','Christopher','Lee','Group',6,168),(31,2,111,'Guide','2024-05-01 10:00:00','2024-05-01 18:00:00',1,'Confirmed','MargaretGarcia@gmail.com','0561234567','Margaret','Garcia','Group',4,124),(32,3,112,'Visitor','2024-05-02 09:30:00','2024-05-02 17:30:00',0,'In park','DanielMartinez@gmail.com','0572345678','Daniel','Martinez','Solo',1,29),(33,1,113,'Guide','2024-05-03 11:15:00','2024-05-03 19:15:00',1,'Passed','LisaHernandez@gmail.com','0583456789','Lisa','Hernandez','Solo',1,28),(34,2,114,'Visitor','2024-05-04 10:45:00','2024-05-04 18:45:00',0,'Wait in list','PaulWalker@gmail.com','0594567890','Paul','Walker','Group',3,93),(35,3,115,'Guide','2024-05-05 09:30:00','2024-05-05 17:30:00',1,'Wait in list','JessicaTaylor@gmail.com','0505678901','Jessica','Taylor','Family',2,58),(36,1,116,'Visitor','2024-05-06 10:15:00','2024-05-06 18:15:00',0,'Notified','EdwardAnderson@gmail.com','0516789012','Edward','Anderson','Solo',1,28),(37,2,117,'Guide','2024-05-07 11:30:00','2024-05-07 19:30:00',1,'Confirmed','JenniferHill@gmail.com','0527890123','Jennifer','Hill','Solo',1,31),(38,3,118,'Visitor','2024-05-08 12:15:00','2024-05-08 20:15:00',0,'Notified','GeorgeYoung@gmail.com','0538901234','George','Young','Group',5,145),(39,1,119,'Guide','2024-05-09 10:30:00','2024-05-09 18:30:00',1,'Wait Notify','KatherineScott@gmail.com','0549012345','Katherine','Scott','Group',4,120),(40,2,120,'Visitor','2024-05-10 09:45:00','2024-05-10 17:45:00',0,'Wait in list','StevenKing@gmail.com','0550123456','Steven','King','Solo',1,30);
/*!40000 ALTER TABLE `preorders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reports`
--

DROP TABLE IF EXISTS `reports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reports` (
  `reportId` int NOT NULL,
  `reportType` varchar(255) DEFAULT NULL,
  `parkId` int DEFAULT NULL,
  `month` varchar(255) DEFAULT NULL,
  `comment` text,
  PRIMARY KEY (`reportId`),
  KEY `parkId` (`parkId`),
  CONSTRAINT `reports_ibfk_1` FOREIGN KEY (`parkId`) REFERENCES `parks` (`ParkId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reports`
--

LOCK TABLES `reports` WRITE;
/*!40000 ALTER TABLE `reports` DISABLE KEYS */;
INSERT INTO `reports` VALUES (1,'TotalVisitorsReport',1,'January','Total visitors report for January.'),(2,'UsageReport',2,'February','Usage report for February.'),(3,'VisitsReports',3,'March','Visits report for March.'),(4,'CancellationsReport',1,'April','Cancellations report for April.'),(5,'TotalVisitorsReport',2,'May','Total visitors report for May.'),(6,'UsageReport',3,'June','Usage report for June.'),(7,'VisitsReports',1,'July','Visits report for July.'),(8,'CancellationsReport',2,'August','Cancellations report for August.');
/*!40000 ALTER TABLE `reports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requests`
--

DROP TABLE IF EXISTS `requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requests` (
  `RequestId` int NOT NULL,
  `ParkId` int DEFAULT NULL,
  `RequestType` varchar(255) DEFAULT NULL,
  `OldValue` int DEFAULT NULL,
  `NewValue` int DEFAULT NULL,
  `RequestStatus` enum('Approved','Pending','Denied') DEFAULT NULL,
  `RequestDate` datetime DEFAULT NULL,
  PRIMARY KEY (`RequestId`),
  KEY `ParkId` (`ParkId`),
  CONSTRAINT `requests_ibfk_1` FOREIGN KEY (`ParkId`) REFERENCES `parks` (`ParkId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requests`
--

LOCK TABLES `requests` WRITE;
/*!40000 ALTER TABLE `requests` DISABLE KEYS */;
INSERT INTO `requests` VALUES (1,1,'Gap Amount Change',NULL,60,'Pending','2024-01-15 10:00:00'),(2,2,'Max Visitors In Park',NULL,NULL,'Approved','2024-01-16 11:30:00'),(3,3,'Max Visitors In Park',NULL,NULL,'Denied','2024-01-17 12:45:00'),(4,1,'Max Visitors In Park',NULL,NULL,'Pending','2024-01-18 13:20:00'),(5,2,'Gap Amount Change',NULL,150,'Approved','2024-01-19 14:10:00'),(6,3,'Gap Amount Change',NULL,30,'Pending','2024-01-20 15:25:00');
/*!40000 ALTER TABLE `requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `totalvisitorsreport`
--

DROP TABLE IF EXISTS `totalvisitorsreport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `totalvisitorsreport` (
  `ReportId` int NOT NULL,
  `CreationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`ReportId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `totalvisitorsreport`
--

LOCK TABLES `totalvisitorsreport` WRITE;
/*!40000 ALTER TABLE `totalvisitorsreport` DISABLE KEYS */;
INSERT INTO `totalvisitorsreport` VALUES (1,'2023-01-01 12:34:56'),(2,'2023-03-02 01:23:45'),(3,'2023-02-15 14:56:07'),(4,'2023-04-18 22:08:09'),(5,'2023-05-20 17:45:33'),(6,'2023-06-25 06:12:48'),(7,'2023-07-07 23:59:59'),(8,'2023-08-14 00:00:01'),(9,'2023-09-09 15:30:45'),(10,'2023-10-10 11:11:11'),(11,'2023-11-11 13:31:13'),(12,'2023-12-12 05:05:05'),(13,'2023-01-13 07:07:07'),(14,'2023-02-14 09:09:09'),(15,'2023-03-15 16:16:16'),(16,'2023-04-16 18:18:18'),(17,'2023-05-17 20:20:20'),(18,'2023-06-18 12:12:12'),(19,'2023-07-19 14:14:14'),(20,'2023-08-20 08:08:08'),(21,'2023-09-21 22:22:22'),(22,'2023-10-22 10:10:10'),(23,'2023-11-23 19:19:19'),(24,'2023-12-24 21:21:21'),(25,'2023-01-25 03:03:03'),(26,'2023-02-26 02:02:02'),(27,'2023-03-27 01:01:01'),(28,'2023-04-28 04:04:04'),(29,'2023-05-29 23:23:23'),(30,'2023-06-30 05:55:55'),(31,'2023-07-31 07:37:47'),(32,'2023-08-01 08:18:28'),(33,'2023-09-02 09:29:39'),(34,'2023-10-03 10:30:30'),(35,'2023-11-04 11:40:40'),(36,'2023-12-05 12:50:50'),(37,'2023-01-06 06:16:26'),(38,'2023-02-07 07:27:37'),(39,'2023-03-08 08:38:48'),(40,'2023-04-09 09:49:59');
/*!40000 ALTER TABLE `totalvisitorsreport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `totalvisitorsreportgenerator`
--

DROP TABLE IF EXISTS `totalvisitorsreportgenerator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `totalvisitorsreportgenerator` (
  `ReportId` int NOT NULL,
  `year` int DEFAULT NULL,
  `month` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `totalvisitorsreportgenerator`
--

LOCK TABLES `totalvisitorsreportgenerator` WRITE;
/*!40000 ALTER TABLE `totalvisitorsreportgenerator` DISABLE KEYS */;
INSERT INTO `totalvisitorsreportgenerator` VALUES (1,2023,1),(2,2023,2),(3,2023,3),(4,2023,4),(5,2023,5),(6,2023,6),(7,2023,7),(8,2023,8),(9,2023,9),(10,2023,10),(11,2023,11),(12,2023,12),(13,2024,1),(14,2024,2),(15,2024,3),(16,2024,4),(17,2024,5),(18,2024,6),(19,2024,7),(20,2024,8),(21,2024,9),(22,2024,10),(23,2024,11),(24,2024,12),(25,2025,1);
/*!40000 ALTER TABLE `totalvisitorsreportgenerator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usagereport`
--

DROP TABLE IF EXISTS `usagereport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usagereport` (
  `ReportId` int NOT NULL,
  `CreationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`ReportId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usagereport`
--

LOCK TABLES `usagereport` WRITE;
/*!40000 ALTER TABLE `usagereport` DISABLE KEYS */;
INSERT INTO `usagereport` VALUES (1,'2024-03-17 00:00:00'),(2,'2024-03-17 01:00:00'),(3,'2024-03-17 02:00:00'),(4,'2024-03-17 03:00:00'),(5,'2024-03-17 04:00:00'),(6,'2024-03-17 05:00:00'),(7,'2024-03-17 06:00:00'),(8,'2024-03-17 07:00:00'),(9,'2024-03-17 08:00:00'),(10,'2024-03-17 09:00:00'),(11,'2024-03-17 10:00:00'),(12,'2024-03-17 11:00:00'),(13,'2024-03-17 12:00:00'),(14,'2024-03-17 13:00:00'),(15,'2024-03-17 14:00:00'),(16,'2024-03-17 15:00:00'),(17,'2024-03-17 16:00:00'),(18,'2024-03-17 17:00:00'),(19,'2024-03-17 18:00:00'),(20,'2024-03-17 19:00:00');
/*!40000 ALTER TABLE `usagereport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usagereportgenerator`
--

DROP TABLE IF EXISTS `usagereportgenerator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usagereportgenerator` (
  `ReportId` int NOT NULL,
  `year` int DEFAULT NULL,
  `month` int DEFAULT NULL,
  PRIMARY KEY (`ReportId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usagereportgenerator`
--

LOCK TABLES `usagereportgenerator` WRITE;
/*!40000 ALTER TABLE `usagereportgenerator` DISABLE KEYS */;
INSERT INTO `usagereportgenerator` VALUES (1,2023,1),(2,2023,2),(3,2023,3),(4,2023,4),(5,2023,5),(6,2023,6),(7,2023,7),(8,2023,8),(9,2023,9),(10,2023,10),(11,2023,11),(12,2023,12),(13,2024,1),(14,2024,2),(15,2024,3),(16,2024,4),(17,2024,5),(18,2024,6),(19,2024,7),(20,2024,8);
/*!40000 ALTER TABLE `usagereportgenerator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `UserId` varchar(255) NOT NULL,
  `Username` varchar(255) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  `FirstName` varchar(255) DEFAULT NULL,
  `LastName` varchar(255) DEFAULT NULL,
  `Phone` varchar(255) DEFAULT NULL,
  `Email` varchar(255) DEFAULT NULL,
  `Status` enum('Approve','Pending') DEFAULT NULL,
  `UserType` varchar(255) DEFAULT NULL,
  `ParkId` int DEFAULT NULL,
  `EmployeeType` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`UserId`),
  UNIQUE KEY `Username` (`Username`),
  KEY `ParkId` (`ParkId`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`ParkId`) REFERENCES `parks` (`ParkId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('023456785','user10','password10','Isabella','Thomas','987654320','isabella.thomas@example.com','Approve','External User',1,'Service Employee'),('023456786','user8','password8','Sophia','Taylor','219876543','sophia.taylor@example.com','Approve','Guide',2,'Park Manager'),('023456787','user6','password6','Olivia','Miller','432198765','olivia.miller@example.com','Pending','External User',3,'Service Employee'),('023456788','user4','password4','Emily','Johnson','654321987','emily.johnson@example.com','Approve','Guide',1,'Park Manager'),('023456789','user2','password2','Jane','Smith','876543219','jane.smith@example.com','Pending','External User',2,'Service Employee'),('123456785','user9','password9','Ethan','Anderson','198765432','ethan.anderson@example.com','Pending','Employee',3,'Department Manager'),('123456786','user7','password7','Lucas','Moore','321987654','lucas.moore@example.com','Approve','Visitor',1,'Park Employee'),('123456787','user5','password5','Daniel','Wilson','543219876','daniel.wilson@example.com','Pending','Employee',2,'Department Manager'),('123456788','user3','password3','Michael','Brown','765432198','michael.brown@example.com','Approve','Visitor',3,'Park Employee'),('123456789','user1','password1','John','Doe','987654321','john.doe@example.com','Approve','Employee',1,'Department Manager');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visitors`
--

DROP TABLE IF EXISTS `visitors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visitors` (
  `VisitorId` varchar(255) NOT NULL,
  `FirstName` varchar(255) DEFAULT NULL,
  `LastName` varchar(255) DEFAULT NULL,
  `Phone` varchar(255) DEFAULT NULL,
  `Email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`VisitorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visitors`
--

LOCK TABLES `visitors` WRITE;
/*!40000 ALTER TABLE `visitors` DISABLE KEYS */;
INSERT INTO `visitors` VALUES ('V001','John','Doe','555-0101','johndoe@example.com'),('V002','Jane','Doe','555-0102','janedoe@example.com'),('V003','Michael','Smith','555-0103','michaelsmith@example.com'),('V004','Emily','Jones','555-0104','emilyjones@example.com'),('V005','William','Brown','555-0105','williambrown@example.com'),('V006','Olivia','Davis','555-0106','oliviadavis@example.com'),('V007','Henry','Miller','555-0107','henrymiller@example.com'),('V008','Ava','Wilson','555-0108','avawilson@example.com'),('V009','Matthew','Moore','555-0109','matthewmoore@example.com'),('V010','Sophia','Taylor','555-0110','sophiataylor@example.com'),('V011','Amelia','Anderson','555-0111','ameliaanderson@example.com'),('V012','James','Thomas','555-0112','jamesthomas@example.com'),('V013','Isabella','Jackson','555-0113','isabellajackson@example.com'),('V014','Alexander','White','555-0114','alexanderwhite@example.com'),('V015','Mia','Harris','555-0115','miaharris@example.com'),('V016','Logan','Martin','555-0116','loganmartin@example.com'),('V017','Emma','Thompson','555-0117','emmathompson@example.com'),('V018','Lucas','Garcia','555-0118','lucasgarcia@example.com'),('V019','Charlotte','Martinez','555-0119','charlottemartinez@example.com'),('V020','Ethan','Robinson','555-0120','ethanrobinson@example.com'),('V021','Harper','Clark','555-0121','harperclark@example.com'),('V022','Mason','Rodriguez','555-0122','masonrodriguez@example.com'),('V023','Evelyn','Lewis','555-0123','evelynlewis@example.com'),('V024','Jack','Lee','555-0124','jacklee@example.com'),('V025','Lily','Walker','555-0125','lilywalker@example.com'),('V026','Owen','Hall','555-0126','owenhall@example.com'),('V027','Aria','Allen','555-0127','ariaallen@example.com'),('V028','Noah','Young','555-0128','noahyoung@example.com'),('V029','Scarlett','Hernandez','555-0129','scarletthernandez@example.com'),('V030','Benjamin','King','555-0130','benjaminking@example.com');
/*!40000 ALTER TABLE `visitors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visitsreport`
--

DROP TABLE IF EXISTS `visitsreport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visitsreport` (
  `ReportId` int NOT NULL,
  `CreationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`ReportId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visitsreport`
--

LOCK TABLES `visitsreport` WRITE;
/*!40000 ALTER TABLE `visitsreport` DISABLE KEYS */;
INSERT INTO `visitsreport` VALUES (1,'2024-03-17 08:00:00'),(2,'2024-03-16 10:30:00'),(3,'2024-03-15 14:45:00'),(4,'2024-03-14 16:20:00'),(5,'2024-03-13 09:10:00'),(6,'2024-03-12 11:25:00'),(7,'2024-03-11 13:55:00'),(8,'2024-03-10 17:30:00'),(9,'2024-03-09 08:40:00'),(10,'2024-03-08 10:15:00'),(11,'2024-03-07 12:20:00'),(12,'2024-03-06 14:05:00'),(13,'2024-03-05 16:50:00'),(14,'2024-03-04 09:35:00'),(15,'2024-03-03 11:45:00');
/*!40000 ALTER TABLE `visitsreport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `waitinglist`
--

DROP TABLE IF EXISTS `waitinglist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `waitinglist` (
  `Id` int NOT NULL,
  `OrderId` int DEFAULT NULL,
  `EnterListTime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `OrderId` (`OrderId`),
  CONSTRAINT `waitinglist_ibfk_1` FOREIGN KEY (`OrderId`) REFERENCES `preorders` (`OrderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `waitinglist`
--

LOCK TABLES `waitinglist` WRITE;
/*!40000 ALTER TABLE `waitinglist` DISABLE KEYS */;
INSERT INTO `waitinglist` VALUES (1,22,'2024-04-22 11:00:00'),(2,34,'2024-05-04 18:45:00'),(3,35,'2024-05-05 09:30:00'),(4,40,'2024-05-10 09:45:00');
/*!40000 ALTER TABLE `waitinglist` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-03-17 18:28:36
