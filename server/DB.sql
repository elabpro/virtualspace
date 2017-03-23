-- MySQL dump 10.13  Distrib 5.7.17, for Linux (x86_64)
--
-- Host: localhost    Database: vncserver
-- ------------------------------------------------------
-- Server version	5.7.17-0ubuntu0.16.04.1

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
-- Table structure for table `application`
--

DROP TABLE IF EXISTS `application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `application` (
  `id_application` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id приложения',
  `name` varchar(45) DEFAULT NULL COMMENT 'название',
  `describe` varchar(45) DEFAULT NULL COMMENT 'описание',
  PRIMARY KEY (`id_application`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='Таблица приложений запускаемых на сервере';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `application`
--

LOCK TABLES `application` WRITE;
/*!40000 ALTER TABLE `application` DISABLE KEYS */;
INSERT INTO `application` VALUES (1,'default','команды операционной систем'),(2,'LibreOffice','Свободный прикладной пакет офисных приложений'),(3,'ThunderBird','Программа для работы с электронной почтой'),(4,'gedit','Текстовый блокнот'),(5,'Firefox','Браузер для выхода на веб ресурсы'),(6,'rhythmbox','музыкальный проигрыватель'),(7,'calc','калькулятор');
/*!40000 ALTER TABLE `application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `available_commands`
--

DROP TABLE IF EXISTS `available_commands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `available_commands` (
  `id` int(11) NOT NULL,
  `id_command` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `available_commands`
--

LOCK TABLES `available_commands` WRITE;
/*!40000 ALTER TABLE `available_commands` DISABLE KEYS */;
INSERT INTO `available_commands` VALUES (0,1),(1,2),(2,4),(3,5),(4,6),(5,8),(6,9),(7,10),(8,11),(9,12),(10,13),(11,14),(12,15),(13,16),(14,36),(15,37),(16,38),(17,39),(18,40),(19,42),(20,47);
/*!40000 ALTER TABLE `available_commands` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `commands`
--

DROP TABLE IF EXISTS `commands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `commands` (
  `id_command` int(11) NOT NULL AUTO_INCREMENT,
  `id_application` int(11) NOT NULL,
  `message` varchar(80) NOT NULL,
  `answer` varchar(45) NOT NULL,
  `console` varchar(60) NOT NULL,
  `start_application` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_command`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `commands`
--

LOCK TABLES `commands` WRITE;
/*!40000 ALTER TABLE `commands` DISABLE KEYS */;
INSERT INTO `commands` VALUES (1,1,'ева запусти браузер','браузер был успешно запущен','exec firefox &',5),(2,1,'ева запусти офис','офис был успешно запущен','exec libreoffice &',2),(4,1,'ева открой почту','почтовый клиент был успешно запущен','exec thunderbird &',3),(5,1,'ева запусти блокнот','блокнот был успешно открыт','exec gedit &',4),(6,1,'ева открой блокнот','блокнот был открыт','exec gedit &',4),(8,1,'ева открой браузер','браузер был успешно запущен','exec firefox &',5),(9,1,'ева открой новости','показ новостей от системы поиска яндекс','exec firefox http://news.yandex.ru &',5),(10,1,'ева закрой приложение','','xdotool key Alt + F4',1),(11,1,'ева переключи приложение','','xdotool keydown Alt + Tab keyup Alt + Tab',1),(12,1,'ева влево','','xdotool mousemove_relative -- -10 0',0),(13,1,'ева вправо','','xdotool mousemove_relative -- 10 0',0),(14,1,'ева вверх','','xdotool mousemove_relative -- 0 -10',0),(15,1,'ева вниз','','xdotool mousemove_relative -- 0 10',0),(16,1,'ева кликни','','xdotool click 1',0),(17,3,'ева покажи главное окно почты','','xdotool key ctrl+1',3),(18,3,'ева напиши новое сообщение ','','xdotool key ctrl+N',3),(19,3,'ева отправь новое сообщение немедленно','','xdotool key ctrl+enter',3),(20,3,'ева отправь новое сообщение позже ','','xdotool key ctrl+shift+enter',3),(21,3,'ева перейди к следующему сообщению','','xdotool key f',3),(22,3,'ева перейди к предыдущему сообщению','','xdotool key b',3),(23,3,'ева перейди к следующему новому сообщению','','xdotool key n',3),(24,3,'ева перейди к предыдущему непрочитанному сообщению','','xdotool key p',3),(25,3,'ева закрой почту','почта закрыта','xdotool key ctrl+q',1),(27,5,'ева открой новую вкладку','','xdotool key ctrl+t',5),(28,5,'закрыть вкладку ','','xdotool key ctrl+W',5),(29,5,'следующая вкладка','','xdotool key ctrl+tab',5),(30,5,'предыдущая вкладка','','xdotool key ctrl+shift+tab',5),(31,5,'обновить страницу браузера','','xdotool key f5',5),(32,5,'открыть закрытую вкладку','','xdotool key ctrl+shift+t',5),(33,6,'остановить (закрыть) музыку','','xdotool key ctrl+q',6),(34,6,'проигрывать музыку','','xdotool key ctrl+p',6),(35,6,'стоп (пауза)','','xdotool key ctrl+p',6),(36,1,'сделать звук громче (+5% громкости)','','amixer -D pulse set Master 5%+',6),(37,1,'сделать звук тише (-5% громкости)','','amixer -D pulse set Master 5%-',NULL),(38,1,'выключить звук ','','amixer -D pulse set Master 0%',NULL),(39,1,'сделать скриншот','','gnome-screenshot',NULL),(40,1,'открыть калькулятор ','','gnome-calculator',7),(41,7,'закрыть калькулятор','','xdotool key Alt + f4',NULL),(42,1,'открыть терминал','','xdotool key Ctrl + Alt + T',NULL),(43,6,'следующий трек','','xdotool key ctrl+right',NULL),(44,6,'предыдущий трек','','xdotool key ctrl+left',NULL),(45,6,'сделать громче','','xdotool key ctrl+up',NULL),(46,6,'сделать тише','','xdotool key ctrl+down',6),(47,1,'ева запусти почту','почтовый клиент был успешно запущен','thunderbird &',3);
/*!40000 ALTER TABLE `commands` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history` (
  `date` timestamp NULL DEFAULT NULL,
  `id_command` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history`
--

LOCK TABLES `history` WRITE;
/*!40000 ALTER TABLE `history` DISABLE KEYS */;
INSERT INTO `history` VALUES ('2017-03-16 14:27:29',4),('2017-03-16 14:49:32',4),('2017-03-16 14:51:06',4),('2017-03-16 14:51:13',4),('2017-03-16 14:52:22',2),('2017-03-16 14:53:37',2),('2017-03-17 10:49:15',4),('2017-03-17 10:49:24',4),('2017-03-17 10:49:52',4),('2017-03-17 10:50:02',4),('2017-03-17 10:50:19',4),('2017-03-17 11:32:25',4),('2017-03-18 05:02:52',4),('2017-03-18 05:06:02',1),('2017-03-18 06:48:32',4),('2017-03-18 06:52:53',4),('2017-03-18 12:38:31',4),('2017-03-18 12:43:19',4),('2017-03-18 16:29:25',4),('2017-03-18 16:29:25',4),('2017-03-18 16:29:26',4),('2017-03-18 16:33:35',4),('2017-03-18 16:33:35',4),('2017-03-18 16:33:35',4),('2017-03-18 16:34:22',4),('2017-03-18 16:34:22',4),('2017-03-18 16:34:22',4),('2017-03-18 16:35:20',4),('2017-03-18 16:35:21',4),('2017-03-18 16:35:22',4),('2017-03-18 16:36:11',4),('2017-03-18 16:36:12',4),('2017-03-18 16:36:13',4),('2017-03-18 16:37:22',4),('2017-03-18 16:37:23',4),('2017-03-18 16:37:24',4),('2017-03-18 16:40:05',4),('2017-03-18 16:40:06',4),('2017-03-18 16:40:07',4),('2017-03-18 16:45:48',4),('2017-03-18 16:45:51',4),('2017-03-18 16:45:53',4),('2017-03-18 16:48:09',4),('2017-03-18 16:48:11',4),('2017-03-18 16:48:14',4),('2017-03-18 16:52:18',4),('2017-03-18 16:52:20',4),('2017-03-18 16:52:23',4),('2017-03-18 16:54:19',4),('2017-03-18 16:54:26',4),('2017-03-18 16:54:32',4),('2017-03-18 16:58:14',4),('2017-03-18 16:58:21',4),('2017-03-18 16:58:26',4),('2017-03-18 17:00:09',4),('2017-03-18 17:00:15',4),('2017-03-18 17:00:21',4),('2017-03-18 17:24:12',4),('2017-03-18 17:24:46',4),('2017-03-18 17:26:54',4),('2017-03-18 17:26:59',4),('2017-03-18 17:31:03',1),('2017-03-18 17:31:32',47),('2017-03-18 17:31:42',47),('2017-03-18 17:31:49',4),('2017-03-18 17:32:04',6),('2017-03-18 17:32:12',9),('2017-03-18 17:35:00',4),('2017-03-18 17:38:03',4),('2017-03-18 17:40:33',4),('2017-03-18 17:42:29',4),('2017-03-19 07:20:33',4),('2017-03-19 07:20:39',4),('2017-03-19 07:20:44',4),('2017-03-19 07:26:49',4),('2017-03-19 07:26:57',4),('2017-03-19 07:27:02',4),('2017-03-19 08:52:17',4),('2017-03-19 08:52:26',5),('2017-03-19 08:55:11',13),('2017-03-19 08:55:17',4),('2017-03-19 08:55:28',6),('2017-03-19 14:26:11',2),('2017-03-19 14:26:25',5),('2017-03-19 14:57:30',5),('2017-03-19 15:38:30',5),('2017-03-19 15:38:58',4),('2017-03-19 15:42:23',4),('2017-03-19 15:45:22',2),('2017-03-19 15:47:03',4),('2017-03-19 15:49:25',4),('2017-03-19 16:00:33',4),('2017-03-19 16:01:43',2),('2017-03-19 16:03:59',2),('2017-03-19 16:04:06',4),('2017-03-19 16:04:34',4),('2017-03-19 16:04:55',47),('2017-03-19 16:07:01',4),('2017-03-19 16:09:13',4),('2017-03-19 16:11:30',4),('2017-03-19 16:11:47',9),('2017-03-19 16:11:59',9),('2017-03-20 02:15:50',14),('2017-03-20 02:16:05',15),('2017-03-20 02:16:13',15),('2017-03-20 02:17:06',4),('2017-03-20 02:18:20',15),('2017-03-20 02:18:47',15),('2017-03-20 02:19:36',15),('2017-03-20 02:20:06',15),('2017-03-20 02:51:21',4),('2017-03-20 02:52:55',4),('2017-03-20 02:59:31',4),('2017-03-20 03:01:25',15),('2017-03-21 07:44:25',4),('2017-03-21 07:44:36',4),('2017-03-21 07:44:46',4),('2017-03-21 07:59:03',4),('2017-03-21 07:59:12',4),('2017-03-21 07:59:22',4),('2017-03-21 08:12:37',4),('2017-03-21 08:12:48',4),('2017-03-21 08:12:58',4),('2017-03-21 08:38:10',4),('2017-03-21 08:38:20',4),('2017-03-21 08:52:03',4),('2017-03-21 08:55:32',4),('2017-03-21 08:57:35',4),('2017-03-21 08:59:03',4),('2017-03-21 09:02:32',4),('2017-03-21 09:04:07',4),('2017-03-21 09:05:04',4),('2017-03-21 09:06:38',4),('2017-03-21 09:08:20',47),('2017-03-21 09:10:27',4),('2017-03-21 09:15:22',4),('2017-03-21 09:17:12',4),('2017-03-21 09:19:19',4),('2017-03-21 09:23:04',4),('2017-03-21 09:25:39',4),('2017-03-21 09:27:37',4),('2017-03-21 09:27:46',4),('2017-03-21 09:27:57',4),('2017-03-21 10:21:31',4),('2017-03-21 10:21:54',4),('2017-03-21 10:22:16',4),('2017-03-21 10:30:45',4),('2017-03-21 10:30:56',4),('2017-03-21 10:31:06',4),('2017-03-21 10:31:44',4),('2017-03-21 10:38:16',4),('2017-03-21 10:39:44',4),('2017-03-21 10:40:50',4),('2017-03-21 10:42:02',4),('2017-03-21 10:43:01',4),('2017-03-21 10:52:17',4),('2017-03-21 10:53:36',4),('2017-03-21 10:54:58',4),('2017-03-21 10:56:44',4),('2017-03-21 10:56:53',4),('2017-03-21 10:57:07',4),('2017-03-21 10:59:33',4),('2017-03-21 10:59:45',4),('2017-03-21 10:59:54',4),('2017-03-21 11:03:03',4),('2017-03-21 11:03:14',4),('2017-03-21 11:03:26',4),('2017-03-21 11:11:27',4),('2017-03-21 11:11:35',4),('2017-03-21 11:11:44',4),('2017-03-21 11:12:14',14),('2017-03-21 11:24:09',4),('2017-03-21 11:24:18',4),('2017-03-21 11:24:25',4),('2017-03-21 11:25:00',25),('2017-03-21 11:25:33',15),('2017-03-21 11:26:49',4),('2017-03-21 11:26:58',4),('2017-03-21 11:27:07',4),('2017-03-21 11:27:28',15),('2017-03-21 12:28:28',4),('2017-03-21 12:29:16',25),('2017-03-21 12:37:33',4),('2017-03-21 12:37:41',4),('2017-03-21 12:37:52',4),('2017-03-21 12:38:21',25);
/*!40000 ALTER TABLE `history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `last_update_state_system`
--

DROP TABLE IF EXISTS `last_update_state_system`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `last_update_state_system` (
  `opcode` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `last_update_state_system`
--

LOCK TABLES `last_update_state_system` WRITE;
/*!40000 ALTER TABLE `last_update_state_system` DISABLE KEYS */;
INSERT INTO `last_update_state_system` VALUES (1);
/*!40000 ALTER TABLE `last_update_state_system` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openning_application`
--

DROP TABLE IF EXISTS `openning_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openning_application` (
  `id` int(11) NOT NULL,
  `id_application` int(11) DEFAULT NULL COMMENT 'id приложения',
  `id_window` int(11) DEFAULT NULL COMMENT 'идентификатор открытого приложения в виртуальном пространстве',
  PRIMARY KEY (`id`),
  KEY `fk_openning_application_application1_idx` (`id_application`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Информация о запущенных приложениях в системе';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openning_application`
--

LOCK TABLES `openning_application` WRITE;
/*!40000 ALTER TABLE `openning_application` DISABLE KEYS */;
/*!40000 ALTER TABLE `openning_application` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-03-23 19:38:51
