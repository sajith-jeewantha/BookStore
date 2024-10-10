/*
 Navicat MySQL Dump SQL

 Source Server         : 127.0.0.1_3306
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : 127.0.0.1:3306
 Source Schema         : bookstore

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 10/10/2024 13:54:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `line1` text NOT NULL,
  `line2` text NOT NULL,
  `postal_code` varchar(10) NOT NULL,
  `mobile` varchar(10) NOT NULL,
  `city_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id` DESC),
  KEY `fk_user` (`user_id`),
  KEY `fk_city` (`city_id`),
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`),
  CONSTRAINT `address_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of address
-- ----------------------------
BEGIN;
INSERT INTO `address` (`id`, `first_name`, `last_name`, `line1`, `line2`, `postal_code`, `mobile`, `city_id`, `user_id`) VALUES (7, 'sajith', 'jeewantha', 'example address', 'example address', '12345', '0761111111', 2, 41);
INSERT INTO `address` (`id`, `first_name`, `last_name`, `line1`, `line2`, `postal_code`, `mobile`, `city_id`, `user_id`) VALUES (6, 'fman1', 'lman1', 'example address', 'example address', '12345', '0762222222', 2, 40);
INSERT INTO `address` (`id`, `first_name`, `last_name`, `line1`, `line2`, `postal_code`, `mobile`, `city_id`, `user_id`) VALUES (1, 'Sajith', 'Jeewantha', 'example address', 'example address', '12345', '0761111111', 2, 38);
COMMIT;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `inc` varchar(20) NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of admin
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for author
-- ----------------------------
DROP TABLE IF EXISTS `author`;
CREATE TABLE `author` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of author
-- ----------------------------
BEGIN;
INSERT INTO `author` (`id`, `name`) VALUES (13, 'Napoleon Hill');
INSERT INTO `author` (`id`, `name`) VALUES (12, 'Brian Tracy');
INSERT INTO `author` (`id`, `name`) VALUES (11, 'MJ DeMarco');
INSERT INTO `author` (`id`, `name`) VALUES (10, 'Robin Sharma');
INSERT INTO `author` (`id`, `name`) VALUES (9, 'Robert Greene');
INSERT INTO `author` (`id`, `name`) VALUES (8, 'Simon Sinek');
INSERT INTO `author` (`id`, `name`) VALUES (7, 'Robert T. Kiyosaki');
INSERT INTO `author` (`id`, `name`) VALUES (6, 'John C. Maxwell');
INSERT INTO `author` (`id`, `name`) VALUES (5, 'Brian Tracy');
INSERT INTO `author` (`id`, `name`) VALUES (4, 'James Clear');
INSERT INTO `author` (`id`, `name`) VALUES (3, 'Al Ries, Jack Trout');
INSERT INTO `author` (`id`, `name`) VALUES (2, 'Robert Greene');
INSERT INTO `author` (`id`, `name`) VALUES (1, 'Grant Cardone');
COMMIT;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` text NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `condition_id` int NOT NULL,
  `price` double NOT NULL,
  `qty` int NOT NULL,
  `year` varchar(5) NOT NULL,
  `user_id` int NOT NULL,
  `date_time` datetime NOT NULL,
  `status_id` int NOT NULL,
  `book_type_id` int NOT NULL,
  `author_id` int NOT NULL,
  PRIMARY KEY (`id` DESC),
  KEY `fk_product_user` (`user_id`),
  KEY `fk_condition` (`condition_id`),
  KEY `fk_product_status` (`status_id`),
  KEY `book_type_id` (`book_type_id`),
  KEY `fk_author_id` (`author_id`),
  CONSTRAINT `book_ibfk_2` FOREIGN KEY (`condition_id`) REFERENCES `book_condition` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `book_ibfk_5` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `book_type_id` FOREIGN KEY (`book_type_id`) REFERENCES `book_type` (`id`),
  CONSTRAINT `fk_author_id` FOREIGN KEY (`author_id`) REFERENCES `author` (`id`),
  CONSTRAINT `fk_product_status` FOREIGN KEY (`status_id`) REFERENCES `book_status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1017 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of book
-- ----------------------------
BEGIN;
INSERT INTO `book` (`id`, `title`, `description`, `condition_id`, `price`, `qty`, `year`, `user_id`, `date_time`, `status_id`, `book_type_id`, `author_id`) VALUES (1016, 'Clean Code_ A Handbook of Agile Software Craftsmanship', 'This book provides a guide for writing clean, readable, and maintainable code. Martin emphasizes the importance of good coding practices and offers actionable advice for improving code quality in any software project. The book is filled with examples and practical tips on how to make your code more efficient, modular, and easier to understand.', 1, 300, 10, '2024', 41, '2024-09-16 10:26:19', 1, 1, 3);
INSERT INTO `book` (`id`, `title`, `description`, `condition_id`, `price`, `qty`, `year`, `user_id`, `date_time`, `status_id`, `book_type_id`, `author_id`) VALUES (1012, 'Think and Grow Rich', 'One of the most famous personal development and self-help books, \"Think and Grow Rich\" offers a guide to achieving personal and financial success by following a set of principles, such as desire, faith, and persistence.', 1, 2450, 11, '1937', 38, '2024-09-10 10:10:58', 1, 24, 13);
INSERT INTO `book` (`id`, `title`, `description`, `condition_id`, `price`, `qty`, `year`, `user_id`, `date_time`, `status_id`, `book_type_id`, `author_id`) VALUES (1011, 'The Psychology of Selling', 'This sales guide teaches how to sell effectively using psychology-based principles. Tracy explains strategies for understanding customer behavior and closing deals with confidence.', 1, 1200, 10, '1985', 38, '2024-09-10 10:10:58', 1, 27, 12);
INSERT INTO `book` (`id`, `title`, `description`, `condition_id`, `price`, `qty`, `year`, `user_id`, `date_time`, `status_id`, `book_type_id`, `author_id`) VALUES (1010, 'The Millionaire Fastlane', 'This book challenges traditional financial advice and presents a new path to becoming wealthy — the Fastlane. DeMarco argues that entrepreneurship and creating value are the true keys to financial freedom.', 1, 2300, 11, '2010', 38, '2024-09-10 10:10:58', 1, 26, 11);
INSERT INTO `book` (`id`, `title`, `description`, `condition_id`, `price`, `qty`, `year`, `user_id`, `date_time`, `status_id`, `book_type_id`, `author_id`) VALUES (1009, 'The 5 AM Club', 'Sharma presents a system that involves waking up at 5 AM each day to improve productivity, focus, and overall quality of life. The book offers strategies for making the most of the early hours to set a strong foundation for the rest of the day.', 1, 4500, 10, '2018', 38, '2024-09-10 10:10:58', 1, 24, 10);
INSERT INTO `book` (`id`, `title`, `description`, `condition_id`, `price`, `qty`, `year`, `user_id`, `date_time`, `status_id`, `book_type_id`, `author_id`) VALUES (1008, 'The Laws of Human Nature', 'This book explores the underlying motivations of human behavior and offers advice on how to read and influence others effectively. Greene synthesizes historical examples and psychological theories to help readers understand the laws governing human nature.', 1, 5500, 8, '2018', 38, '2024-09-10 10:10:58', 1, 24, 9);
INSERT INTO `book` (`id`, `title`, `description`, `condition_id`, `price`, `qty`, `year`, `user_id`, `date_time`, `status_id`, `book_type_id`, `author_id`) VALUES (1007, 'Start with Why', 'Sinek explores why some leaders and organizations are more innovative, influential, and profitable than others. The core idea is that great leaders inspire action by focusing on \"why\" they do what they do.', 1, 3900, 23, '2009', 38, '2024-09-10 10:10:58', 1, 25, 8);
INSERT INTO `book` (`id`, `title`, `description`, `condition_id`, `price`, `qty`, `year`, `user_id`, `date_time`, `status_id`, `book_type_id`, `author_id`) VALUES (1006, 'Rich Dad Poor Dad', 'This personal finance classic contrasts the lessons that Kiyosaki learned from his two \"dads\" — his own and his best friend\'s wealthy father. The book emphasizes the importance of financial education and investing.', 1, 2450, 10, '1997', 38, '2024-09-10 10:10:58', 1, 26, 7);
INSERT INTO `book` (`id`, `title`, `description`, `condition_id`, `price`, `qty`, `year`, `user_id`, `date_time`, `status_id`, `book_type_id`, `author_id`) VALUES (1005, 'How Successful People Think', 'This book provides actionable advice on how to develop a mindset for success. Maxwell outlines the different types of thinking that successful people use to approach challenges and decisions.', 1, 1499, 30, '2009', 38, '2024-09-10 10:10:58', 1, 24, 6);
INSERT INTO `book` (`id`, `title`, `description`, `condition_id`, `price`, `qty`, `year`, `user_id`, `date_time`, `status_id`, `book_type_id`, `author_id`) VALUES (1004, 'Get Smart! How to Think and Act Like the Most Successful and Highest-Paid People in Every Field', 'Brian Tracy explains how successful people think differently and provides insights on how to develop a high-performance mindset, increase your productivity, and achieve financial independence.', 1, 1500, 20, '2016', 38, '2024-09-10 10:10:58', 1, 24, 5);
INSERT INTO `book` (`id`, `title`, `description`, `condition_id`, `price`, `qty`, `year`, `user_id`, `date_time`, `status_id`, `book_type_id`, `author_id`) VALUES (1003, 'Atomic Habits', 'This book provides a practical guide to breaking bad habits and forming new, better ones. The focus is on making small, incremental changes (atomic habits) that lead to significant long-term improvements.', 1, 2500, 20, '2018', 38, '2024-09-10 10:10:58', 1, 24, 4);
INSERT INTO `book` (`id`, `title`, `description`, `condition_id`, `price`, `qty`, `year`, `user_id`, `date_time`, `status_id`, `book_type_id`, `author_id`) VALUES (1002, 'Positioning: The Battle for Your Mind', 'This marketing classic explores how to \"position\" a product or service in the minds of potential customers. It introduces the concept of positioning in advertising and how to differentiate your brand in a competitive marketplace.', 1, 1650, 17, '1980', 38, '2024-09-10 10:14:58', 1, 28, 3);
INSERT INTO `book` (`id`, `title`, `description`, `condition_id`, `price`, `qty`, `year`, `user_id`, `date_time`, `status_id`, `book_type_id`, `author_id`) VALUES (1001, 'The 48 Laws of Power', 'This book offers 48 strategies to help readers master the game of power, based on historical figures and strategies. It covers the art of manipulation, control, and self-defense in various social and professional situations.', 1, 3500, 15, '1998', 38, '2024-09-10 10:13:33', 1, 24, 2);
INSERT INTO `book` (`id`, `title`, `description`, `condition_id`, `price`, `qty`, `year`, `user_id`, `date_time`, `status_id`, `book_type_id`, `author_id`) VALUES (1000, 'The 10X Rule', 'This book explains the principle of \"Massive Action,\" suggesting that one should set goals ten times higher than what they initially think and work ten times harder to achieve them. It encourages readers to exceed expectations to achieve extraordinary success.', 2, 2500, 23, '2011', 38, '2024-09-10 10:10:58', 1, 24, 1);
COMMIT;

-- ----------------------------
-- Table structure for book_condition
-- ----------------------------
DROP TABLE IF EXISTS `book_condition`;
CREATE TABLE `book_condition` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of book_condition
-- ----------------------------
BEGIN;
INSERT INTO `book_condition` (`id`, `name`) VALUES (2, 'Used');
INSERT INTO `book_condition` (`id`, `name`) VALUES (1, 'New');
COMMIT;

-- ----------------------------
-- Table structure for book_status
-- ----------------------------
DROP TABLE IF EXISTS `book_status`;
CREATE TABLE `book_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of book_status
-- ----------------------------
BEGIN;
INSERT INTO `book_status` (`id`, `name`) VALUES (2, 'Inactive');
INSERT INTO `book_status` (`id`, `name`) VALUES (1, 'Active');
COMMIT;

-- ----------------------------
-- Table structure for book_type
-- ----------------------------
DROP TABLE IF EXISTS `book_type`;
CREATE TABLE `book_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of book_type
-- ----------------------------
BEGIN;
INSERT INTO `book_type` (`id`, `name`) VALUES (29, 'Leadership');
INSERT INTO `book_type` (`id`, `name`) VALUES (28, 'Marketing');
INSERT INTO `book_type` (`id`, `name`) VALUES (27, 'Sales');
INSERT INTO `book_type` (`id`, `name`) VALUES (26, 'Personal Finance');
INSERT INTO `book_type` (`id`, `name`) VALUES (25, 'Business');
INSERT INTO `book_type` (`id`, `name`) VALUES (24, 'Self-help');
INSERT INTO `book_type` (`id`, `name`) VALUES (23, 'Non-fiction novel');
INSERT INTO `book_type` (`id`, `name`) VALUES (22, 'Essays');
INSERT INTO `book_type` (`id`, `name`) VALUES (21, 'Biography');
INSERT INTO `book_type` (`id`, `name`) VALUES (20, 'Autobiography and memoir');
INSERT INTO `book_type` (`id`, `name`) VALUES (19, 'Young adult');
INSERT INTO `book_type` (`id`, `name`) VALUES (18, 'Women’s fiction');
INSERT INTO `book_type` (`id`, `name`) VALUES (17, 'War');
INSERT INTO `book_type` (`id`, `name`) VALUES (16, 'Thrillers');
INSERT INTO `book_type` (`id`, `name`) VALUES (15, 'Short stories');
INSERT INTO `book_type` (`id`, `name`) VALUES (14, 'Science fiction');
INSERT INTO `book_type` (`id`, `name`) VALUES (13, 'Romance');
INSERT INTO `book_type` (`id`, `name`) VALUES (12, 'Plays');
INSERT INTO `book_type` (`id`, `name`) VALUES (11, 'Poetry');
INSERT INTO `book_type` (`id`, `name`) VALUES (10, 'Mystery');
INSERT INTO `book_type` (`id`, `name`) VALUES (9, 'Literary fiction');
INSERT INTO `book_type` (`id`, `name`) VALUES (8, 'Humour and satire');
INSERT INTO `book_type` (`id`, `name`) VALUES (7, 'Horror');
INSERT INTO `book_type` (`id`, `name`) VALUES (6, 'Historical fiction');
INSERT INTO `book_type` (`id`, `name`) VALUES (5, 'Fantasy');
INSERT INTO `book_type` (`id`, `name`) VALUES (4, 'Fairy tales, fables, and folk tales');
INSERT INTO `book_type` (`id`, `name`) VALUES (3, 'Crime');
INSERT INTO `book_type` (`id`, `name`) VALUES (2, 'Classics');
INSERT INTO `book_type` (`id`, `name`) VALUES (1, 'Adventure stories');
COMMIT;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `book_id` int NOT NULL,
  `qty` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_cart` (`user_id`),
  KEY `fk_product_cart` (`book_id`),
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of cart
-- ----------------------------
BEGIN;
INSERT INTO `cart` (`id`, `user_id`, `book_id`, `qty`) VALUES (38, 40, 1008, 1);
INSERT INTO `cart` (`id`, `user_id`, `book_id`, `qty`) VALUES (39, 38, 1011, 1);
INSERT INTO `cart` (`id`, `user_id`, `book_id`, `qty`) VALUES (40, 38, 1009, 1);
INSERT INTO `cart` (`id`, `user_id`, `book_id`, `qty`) VALUES (43, 41, 1016, 1);
INSERT INTO `cart` (`id`, `user_id`, `book_id`, `qty`) VALUES (44, 38, 1016, 1);
COMMIT;

-- ----------------------------
-- Table structure for city
-- ----------------------------
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of city
-- ----------------------------
BEGIN;
INSERT INTO `city` (`id`, `name`) VALUES (1, 'Colombo');
INSERT INTO `city` (`id`, `name`) VALUES (2, 'Gampaha');
COMMIT;

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `book_id` int NOT NULL,
  `qty` int NOT NULL,
  PRIMARY KEY (`id` DESC) USING BTREE,
  KEY `fk_product_order` (`book_id`),
  KEY `fk_order` (`order_id`),
  CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `order_item_ibfk_3` FOREIGN KEY (`book_id`) REFERENCES `book` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of order_item
-- ----------------------------
BEGIN;
INSERT INTO `order_item` (`id`, `order_id`, `book_id`, `qty`) VALUES (15, 7, 1010, 1);
INSERT INTO `order_item` (`id`, `order_id`, `book_id`, `qty`) VALUES (14, 6, 1011, 1);
INSERT INTO `order_item` (`id`, `order_id`, `book_id`, `qty`) VALUES (13, 5, 1010, 1);
INSERT INTO `order_item` (`id`, `order_id`, `book_id`, `qty`) VALUES (12, 5, 1000, 1);
INSERT INTO `order_item` (`id`, `order_id`, `book_id`, `qty`) VALUES (11, 5, 1012, 1);
INSERT INTO `order_item` (`id`, `order_id`, `book_id`, `qty`) VALUES (10, 4, 1011, 1);
INSERT INTO `order_item` (`id`, `order_id`, `book_id`, `qty`) VALUES (9, 3, 1008, 1);
INSERT INTO `order_item` (`id`, `order_id`, `book_id`, `qty`) VALUES (8, 3, 1009, 1);
INSERT INTO `order_item` (`id`, `order_id`, `book_id`, `qty`) VALUES (7, 3, 1012, 2);
INSERT INTO `order_item` (`id`, `order_id`, `book_id`, `qty`) VALUES (6, 2, 1008, 1);
INSERT INTO `order_item` (`id`, `order_id`, `book_id`, `qty`) VALUES (5, 2, 1009, 2);
INSERT INTO `order_item` (`id`, `order_id`, `book_id`, `qty`) VALUES (4, 2, 1012, 1);
COMMIT;

-- ----------------------------
-- Table structure for order_status
-- ----------------------------
DROP TABLE IF EXISTS `order_status`;
CREATE TABLE `order_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of order_status
-- ----------------------------
BEGIN;
INSERT INTO `order_status` (`id`, `name`) VALUES (5, 'Pending');
INSERT INTO `order_status` (`id`, `name`) VALUES (4, 'Shipped');
INSERT INTO `order_status` (`id`, `name`) VALUES (3, 'Deliverd');
INSERT INTO `order_status` (`id`, `name`) VALUES (2, 'Processing');
INSERT INTO `order_status` (`id`, `name`) VALUES (1, 'Paid');
COMMIT;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `date_time` datetime NOT NULL,
  `address_id` int NOT NULL,
  `order_status_id` int NOT NULL,
  PRIMARY KEY (`id` DESC),
  KEY `fk_user_order` (`user_id`),
  KEY `fk_user_address` (`address_id`),
  KEY `fk_order_ststus_id` (`order_status_id`),
  CONSTRAINT `fk_order_ststus_id` FOREIGN KEY (`order_status_id`) REFERENCES `order_status` (`id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of orders
-- ----------------------------
BEGIN;
INSERT INTO `orders` (`id`, `user_id`, `date_time`, `address_id`, `order_status_id`) VALUES (7, 41, '2024-09-16 10:24:51', 7, 5);
INSERT INTO `orders` (`id`, `user_id`, `date_time`, `address_id`, `order_status_id`) VALUES (6, 41, '2024-09-16 10:24:00', 7, 5);
INSERT INTO `orders` (`id`, `user_id`, `date_time`, `address_id`, `order_status_id`) VALUES (5, 40, '2024-09-15 19:44:43', 6, 5);
INSERT INTO `orders` (`id`, `user_id`, `date_time`, `address_id`, `order_status_id`) VALUES (4, 38, '2024-09-15 19:42:22', 1, 5);
INSERT INTO `orders` (`id`, `user_id`, `date_time`, `address_id`, `order_status_id`) VALUES (3, 38, '2024-09-15 18:03:50', 1, 5);
INSERT INTO `orders` (`id`, `user_id`, `date_time`, `address_id`, `order_status_id`) VALUES (2, 38, '2024-09-15 18:02:03', 1, 5);
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `mobile` varchar(12) NOT NULL,
  `password` varchar(12) NOT NULL,
  `verification` varchar(20) NOT NULL,
  `status_id` int DEFAULT '1',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_user_status` (`status_id`),
  CONSTRAINT `fk_user_status` FOREIGN KEY (`status_id`) REFERENCES `user_status` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`id`, `first_name`, `last_name`, `email`, `mobile`, `password`, `verification`, `status_id`, `create_at`) VALUES (38, 'Sajith', 'Jeewantha', 'example1@gmail.com', '0761111111', '12345678', 'verified', 1, '2024-09-06 23:10:32');
INSERT INTO `user` (`id`, `first_name`, `last_name`, `email`, `mobile`, `password`, `verification`, `status_id`, `create_at`) VALUES (40, 'fman1', 'lman1', 'example2@gmail.com', '0762222222', '12345678', 'verified', 1, '2024-09-15 18:22:10');
INSERT INTO `user` (`id`, `first_name`, `last_name`, `email`, `mobile`, `password`, `verification`, `status_id`, `create_at`) VALUES (41, 'fman2', 'lman2', 'example3@gmail.com', '0763333333', '12345678', 'verified', 1, '2024-09-16 10:22:12');
COMMIT;

-- ----------------------------
-- Table structure for user_status
-- ----------------------------
DROP TABLE IF EXISTS `user_status`;
CREATE TABLE `user_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(10) NOT NULL,
  PRIMARY KEY (`id` DESC)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user_status
-- ----------------------------
BEGIN;
INSERT INTO `user_status` (`id`, `name`) VALUES (2, 'Deactive');
INSERT INTO `user_status` (`id`, `name`) VALUES (1, 'Active');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
