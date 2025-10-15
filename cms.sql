-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: labs-mysql-freezing-deep-ad:3306
-- Generation Time: Jul 06, 2025 at 01:43 AM
-- Server version: 8.0.37
-- PHP Version: 8.2.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cms`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`%` PROCEDURE `GetDailyAppointmentReportByDoctor` (IN `report_date` DATE)   BEGIN
    SELECT 
        d.name AS doctor_name,
        a.appointment_time,
        a.status,
        p.name AS patient_name,
        p.phone AS patient_phone
    FROM 
        appointment a
    JOIN 
        doctor d ON a.doctor_id = d.id
    JOIN 
        patient p ON a.patient_id = p.id
    WHERE 
        DATE(a.appointment_time) = report_date
    ORDER BY 
        d.name, a.appointment_time;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `GetDoctorWithMostPatientsByMonth` (IN `input_month` INT, IN `input_year` INT)   BEGIN
    SELECT
        doctor_id, 
        COUNT(patient_id) AS patients_seen
    FROM
        appointment
    WHERE
        MONTH(appointment_time) = input_month 
        AND YEAR(appointment_time) = input_year
    GROUP BY
        doctor_id
    ORDER BY
        patients_seen DESC
    LIMIT 1;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `GetDoctorWithMostPatientsByYear` (IN `input_year` INT)   BEGIN
    SELECT
        doctor_id, 
        COUNT(patient_id) AS patients_seen
    FROM
        appointment
    WHERE
        YEAR(appointment_time) = input_year
    GROUP BY
        doctor_id
    ORDER BY
        patients_seen DESC
    LIMIT 1;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` bigint NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `password`, `username`) VALUES
(1, 'admin@1234', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `appointment`
--

CREATE TABLE `appointment` (
  `id` bigint NOT NULL,
  `appointment_time` datetime(6) NOT NULL,
  `notes` varchar(500) DEFAULT NULL,
  `reason_for_visit` varchar(255) DEFAULT NULL,
  `status` int NOT NULL,
  `doctor_id` bigint NOT NULL,
  `patient_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `appointment`
--

INSERT INTO `appointment` (`id`, `appointment_time`, `notes`, `reason_for_visit`, `status`, `doctor_id`, `patient_id`) VALUES
(1, '2025-05-01 09:00:00.000000', NULL, NULL, 0, 1, 1),
(2, '2025-05-02 10:00:00.000000', NULL, NULL, 0, 1, 2),
(3, '2025-05-03 11:00:00.000000', NULL, NULL, 0, 1, 3),
(4, '2025-05-04 14:00:00.000000', NULL, NULL, 0, 1, 4),
(5, '2025-05-05 15:00:00.000000', NULL, NULL, 0, 1, 5),
(6, '2025-05-06 13:00:00.000000', NULL, NULL, 0, 1, 6),
(7, '2025-05-07 09:00:00.000000', NULL, NULL, 0, 1, 7),
(8, '2025-05-08 16:00:00.000000', NULL, NULL, 0, 1, 8),
(9, '2025-05-09 11:00:00.000000', NULL, NULL, 0, 1, 9),
(10, '2025-05-10 10:00:00.000000', NULL, NULL, 0, 1, 10),
(11, '2025-05-11 12:00:00.000000', NULL, NULL, 0, 1, 11),
(12, '2025-05-12 15:00:00.000000', NULL, NULL, 0, 1, 12),
(13, '2025-05-13 13:00:00.000000', NULL, NULL, 0, 1, 13),
(14, '2025-05-14 10:00:00.000000', NULL, NULL, 0, 1, 14),
(15, '2025-05-15 11:00:00.000000', NULL, NULL, 0, 1, 15),
(16, '2025-05-16 14:00:00.000000', NULL, NULL, 0, 1, 16),
(17, '2025-05-17 09:00:00.000000', NULL, NULL, 0, 1, 17),
(18, '2025-05-18 12:00:00.000000', NULL, NULL, 0, 1, 18),
(19, '2025-05-19 13:00:00.000000', NULL, NULL, 0, 1, 19),
(20, '2025-05-20 16:00:00.000000', NULL, NULL, 0, 1, 20),
(21, '2025-05-21 14:00:00.000000', NULL, NULL, 0, 1, 21),
(22, '2025-05-22 10:00:00.000000', NULL, NULL, 0, 1, 22),
(23, '2025-05-23 11:00:00.000000', NULL, NULL, 0, 1, 23),
(24, '2025-05-24 15:00:00.000000', NULL, NULL, 0, 1, 24),
(25, '2025-05-25 09:00:00.000000', NULL, NULL, 0, 1, 25),
(26, '2025-05-01 10:00:00.000000', NULL, NULL, 0, 2, 1),
(27, '2025-05-02 11:00:00.000000', NULL, NULL, 0, 3, 2),
(28, '2025-05-03 14:00:00.000000', NULL, NULL, 0, 4, 3),
(29, '2025-05-04 15:00:00.000000', NULL, NULL, 0, 5, 4),
(30, '2025-05-05 10:00:00.000000', NULL, NULL, 0, 6, 5),
(31, '2025-05-06 11:00:00.000000', NULL, NULL, 0, 7, 6),
(32, '2025-05-07 14:00:00.000000', NULL, NULL, 0, 8, 7),
(33, '2025-05-08 15:00:00.000000', NULL, NULL, 0, 9, 8),
(34, '2025-05-09 10:00:00.000000', NULL, NULL, 0, 10, 9),
(35, '2025-05-10 14:00:00.000000', NULL, NULL, 0, 11, 10),
(36, '2025-05-11 13:00:00.000000', NULL, NULL, 0, 12, 11),
(37, '2025-05-12 14:00:00.000000', NULL, NULL, 0, 13, 12),
(38, '2025-05-13 15:00:00.000000', NULL, NULL, 0, 14, 13),
(39, '2025-05-14 10:00:00.000000', NULL, NULL, 0, 15, 14),
(40, '2025-05-15 11:00:00.000000', NULL, NULL, 0, 16, 15),
(41, '2025-05-16 14:00:00.000000', NULL, NULL, 0, 17, 16),
(42, '2025-05-17 10:00:00.000000', NULL, NULL, 0, 18, 17),
(43, '2025-05-18 13:00:00.000000', NULL, NULL, 0, 19, 18),
(44, '2025-05-19 14:00:00.000000', NULL, NULL, 0, 20, 19),
(45, '2025-05-20 11:00:00.000000', NULL, NULL, 0, 21, 20),
(46, '2025-05-21 13:00:00.000000', NULL, NULL, 0, 22, 21),
(47, '2025-05-22 14:00:00.000000', NULL, NULL, 0, 23, 22),
(48, '2025-05-23 10:00:00.000000', NULL, NULL, 0, 24, 23),
(49, '2025-05-24 15:00:00.000000', NULL, NULL, 0, 25, 24),
(50, '2025-05-25 13:00:00.000000', NULL, NULL, 0, 25, 25),
(51, '2025-04-01 10:00:00.000000', NULL, NULL, 1, 1, 2),
(52, '2025-04-02 11:00:00.000000', NULL, NULL, 1, 2, 3),
(53, '2025-04-03 14:00:00.000000', NULL, NULL, 1, 3, 4),
(54, '2025-04-04 15:00:00.000000', NULL, NULL, 1, 4, 5),
(55, '2025-04-05 10:00:00.000000', NULL, NULL, 1, 5, 6),
(56, '2025-04-06 11:00:00.000000', NULL, NULL, 1, 6, 7),
(57, '2025-04-07 14:00:00.000000', NULL, NULL, 1, 7, 8),
(58, '2025-04-08 15:00:00.000000', NULL, NULL, 1, 8, 9),
(59, '2025-04-09 10:00:00.000000', NULL, NULL, 1, 9, 10),
(60, '2025-04-10 14:00:00.000000', NULL, NULL, 1, 10, 11),
(61, '2025-04-11 13:00:00.000000', NULL, NULL, 1, 11, 12),
(62, '2025-04-12 14:00:00.000000', NULL, NULL, 1, 12, 13),
(63, '2025-04-13 15:00:00.000000', NULL, NULL, 1, 13, 14),
(64, '2025-04-14 10:00:00.000000', NULL, NULL, 1, 14, 15),
(65, '2025-04-15 11:00:00.000000', NULL, NULL, 1, 15, 16),
(66, '2025-04-16 14:00:00.000000', NULL, NULL, 1, 16, 17),
(67, '2025-04-17 10:00:00.000000', NULL, NULL, 1, 17, 18),
(68, '2025-04-18 13:00:00.000000', NULL, NULL, 1, 18, 19),
(69, '2025-04-19 14:00:00.000000', NULL, NULL, 1, 19, 20),
(70, '2025-04-20 11:00:00.000000', NULL, NULL, 1, 20, 21),
(71, '2025-04-21 13:00:00.000000', NULL, NULL, 1, 21, 22),
(72, '2025-04-22 14:00:00.000000', NULL, NULL, 1, 22, 23),
(73, '2025-04-23 10:00:00.000000', NULL, NULL, 1, 23, 24),
(74, '2025-04-24 15:00:00.000000', NULL, NULL, 1, 24, 25),
(75, '2025-04-25 13:00:00.000000', NULL, NULL, 1, 25, 25),
(76, '2025-04-01 09:00:00.000000', NULL, NULL, 1, 1, 1),
(77, '2025-04-02 10:00:00.000000', NULL, NULL, 1, 1, 2),
(78, '2025-04-03 11:00:00.000000', NULL, NULL, 1, 1, 3),
(79, '2025-04-04 14:00:00.000000', NULL, NULL, 1, 1, 4),
(80, '2025-04-05 10:00:00.000000', NULL, NULL, 1, 1, 5),
(81, '2025-04-10 10:00:00.000000', NULL, NULL, 1, 1, 6),
(82, '2025-04-11 09:00:00.000000', NULL, NULL, 1, 1, 7),
(83, '2025-04-14 13:00:00.000000', NULL, NULL, 1, 1, 8),
(84, '2025-04-01 10:00:00.000000', NULL, NULL, 1, 2, 1),
(85, '2025-04-01 11:00:00.000000', NULL, NULL, 1, 2, 2),
(86, '2025-04-02 09:00:00.000000', NULL, NULL, 1, 2, 3),
(87, '2025-04-02 10:00:00.000000', NULL, NULL, 1, 2, 4),
(88, '2025-04-03 11:00:00.000000', NULL, NULL, 1, 2, 5),
(89, '2025-04-03 12:00:00.000000', NULL, NULL, 1, 2, 6),
(90, '2025-04-04 14:00:00.000000', NULL, NULL, 1, 2, 7),
(91, '2025-04-04 15:00:00.000000', NULL, NULL, 1, 2, 8),
(92, '2025-04-05 10:00:00.000000', NULL, NULL, 1, 2, 9),
(93, '2025-04-05 11:00:00.000000', NULL, NULL, 1, 2, 10),
(94, '2025-04-06 13:00:00.000000', NULL, NULL, 1, 2, 11),
(95, '2025-04-06 14:00:00.000000', NULL, NULL, 1, 2, 12),
(96, '2025-04-07 09:00:00.000000', NULL, NULL, 1, 2, 13),
(97, '2025-04-07 10:00:00.000000', NULL, NULL, 1, 2, 14),
(98, '2025-04-08 11:00:00.000000', NULL, NULL, 1, 2, 15),
(99, '2025-04-08 12:00:00.000000', NULL, NULL, 1, 2, 16),
(100, '2025-04-09 13:00:00.000000', NULL, NULL, 1, 2, 17),
(101, '2025-04-09 14:00:00.000000', NULL, NULL, 1, 2, 18),
(102, '2025-04-10 11:00:00.000000', NULL, NULL, 1, 2, 19),
(103, '2025-04-10 12:00:00.000000', NULL, NULL, 1, 2, 20),
(104, '2025-04-11 14:00:00.000000', NULL, NULL, 1, 2, 21),
(105, '2025-04-11 15:00:00.000000', NULL, NULL, 1, 2, 22),
(106, '2025-04-12 10:00:00.000000', NULL, NULL, 1, 2, 23),
(107, '2025-04-12 11:00:00.000000', NULL, NULL, 1, 2, 24),
(108, '2025-04-13 13:00:00.000000', NULL, NULL, 1, 2, 25),
(109, '2025-04-13 14:00:00.000000', NULL, NULL, 1, 2, 1),
(110, '2025-04-14 09:00:00.000000', NULL, NULL, 1, 2, 2),
(111, '2025-04-14 10:00:00.000000', NULL, NULL, 1, 2, 3),
(112, '2025-04-15 12:00:00.000000', NULL, NULL, 1, 2, 4),
(113, '2025-04-15 13:00:00.000000', NULL, NULL, 1, 2, 5),
(114, '2025-04-01 12:00:00.000000', NULL, NULL, 1, 3, 1),
(115, '2025-04-02 11:00:00.000000', NULL, NULL, 1, 3, 2),
(116, '2025-04-03 13:00:00.000000', NULL, NULL, 1, 3, 3),
(117, '2025-04-04 15:00:00.000000', NULL, NULL, 1, 3, 4),
(118, '2025-04-05 12:00:00.000000', NULL, NULL, 1, 3, 5),
(119, '2025-04-08 13:00:00.000000', NULL, NULL, 1, 3, 6),
(120, '2025-04-09 10:00:00.000000', NULL, NULL, 1, 3, 7),
(121, '2025-04-10 14:00:00.000000', NULL, NULL, 1, 3, 8),
(122, '2025-04-11 13:00:00.000000', NULL, NULL, 1, 3, 9),
(123, '2025-04-12 09:00:00.000000', NULL, NULL, 1, 3, 10),
(124, '2025-04-01 14:00:00.000000', NULL, NULL, 1, 4, 1),
(125, '2025-04-02 12:00:00.000000', NULL, NULL, 1, 4, 2),
(126, '2025-04-03 14:00:00.000000', NULL, NULL, 1, 4, 3),
(127, '2025-04-04 16:00:00.000000', NULL, NULL, 1, 4, 4),
(128, '2025-04-05 14:00:00.000000', NULL, NULL, 1, 4, 5),
(129, '2025-04-09 11:00:00.000000', NULL, NULL, 1, 4, 6),
(130, '2025-04-10 13:00:00.000000', NULL, NULL, 1, 4, 7);

-- --------------------------------------------------------

--
-- Table structure for table `doctor`
--

CREATE TABLE `doctor` (
  `id` bigint NOT NULL,
  `clinic_address` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `rating` double DEFAULT NULL,
  `specialty` varchar(50) NOT NULL,
  `years_of_experience` int DEFAULT NULL
) ;

--
-- Dumping data for table `doctor`
--

INSERT INTO `doctor` (`id`, `clinic_address`, `email`, `name`, `password`, `phone`, `rating`, `specialty`, `years_of_experience`) VALUES
(1, NULL, 'dr.adams@example.com', 'Dr. Emily Adams', 'pass12345', '555-101-2020', NULL, 'Cardiologist', NULL),
(2, NULL, 'dr.johnson@example.com', 'Dr. Mark Johnson', 'secure4567', '555-202-3030', NULL, 'Neurologist', NULL),
(3, NULL, 'dr.lee@example.com', 'Dr. Sarah Lee', 'leePass987', '555-303-4040', NULL, 'Orthopedist', NULL),
(4, NULL, 'dr.wilson@example.com', 'Dr. Tom Wilson', 'w!ls0nPwd', '555-404-5050', NULL, 'Pediatrician', NULL),
(5, NULL, 'dr.brown@example.com', 'Dr. Alice Brown', 'brownie123', '555-505-6060', NULL, 'Dermatologist', NULL),
(6, NULL, 'dr.taylor@example.com', 'Dr. Taylor Grant', 'taylor321', '555-606-7070', NULL, 'Cardiologist', NULL),
(7, NULL, 'dr.white@example.com', 'Dr. Sam White', 'whiteSecure1', '555-707-8080', NULL, 'Neurologist', NULL),
(8, NULL, 'dr.clark@example.com', 'Dr. Emma Clark', 'clarkPass456', '555-808-9090', NULL, 'Orthopedist', NULL),
(9, NULL, 'dr.davis@example.com', 'Dr. Olivia Davis', 'davis789', '555-909-0101', NULL, 'Pediatrician', NULL),
(10, NULL, 'dr.miller@example.com', 'Dr. Henry Miller', 'millertime!', '555-010-1111', NULL, 'Dermatologist', NULL),
(11, NULL, 'dr.moore@example.com', 'Dr. Ella Moore', 'ellapass33', '555-111-2222', NULL, 'Cardiologist', NULL),
(12, NULL, 'dr.martin@example.com', 'Dr. Leo Martin', 'martinpass', '555-222-3333', NULL, 'Neurologist', NULL),
(13, NULL, 'dr.jackson@example.com', 'Dr. Ivy Jackson', 'jackson11', '555-333-4444', NULL, 'Orthopedist', NULL),
(14, NULL, 'dr.thomas@example.com', 'Dr. Owen Thomas', 'thomasPWD', '555-444-5555', NULL, 'Pediatrician', NULL),
(15, NULL, 'dr.hall@example.com', 'Dr. Ava Hall', 'hallhall', '555-555-6666', NULL, 'Dermatologist', NULL),
(16, NULL, 'dr.green@example.com', 'Dr. Mia Green', 'greenleaf', '555-666-7777', NULL, 'Cardiologist', NULL),
(17, NULL, 'dr.baker@example.com', 'Dr. Jack Baker', 'bakeitup', '555-777-8888', NULL, 'Neurologist', NULL),
(18, NULL, 'dr.walker@example.com', 'Dr. Nora Walker', 'walkpass12', '555-888-9999', NULL, 'Orthopedist', NULL),
(19, NULL, 'dr.young@example.com', 'Dr. Liam Young', 'young123', '555-999-0000', NULL, 'Pediatrician', NULL),
(20, NULL, 'dr.king@example.com', 'Dr. Zoe King', 'kingkong1', '555-000-1111', NULL, 'Dermatologist', NULL),
(21, NULL, 'dr.scott@example.com', 'Dr. Lily Scott', 'scottish', '555-111-2223', NULL, 'Cardiologist', NULL),
(22, NULL, 'dr.evans@example.com', 'Dr. Lucas Evans', 'evansEv1', '555-222-3334', NULL, 'Neurologist', NULL),
(23, NULL, 'dr.turner@example.com', 'Dr. Grace Turner', 'turnerBurner', '555-333-4445', NULL, 'Orthopedist', NULL),
(24, NULL, 'dr.hill@example.com', 'Dr. Ethan Hill', 'hillclimb', '555-444-5556', NULL, 'Pediatrician', NULL),
(25, NULL, 'dr.ward@example.com', 'Dr. Ruby Ward', 'wardWard', '555-555-6667', NULL, 'Dermatologist', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `doctor_available_times`
--

CREATE TABLE `doctor_available_times` (
  `doctor_id` bigint NOT NULL,
  `available_times` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `doctor_available_times`
--

INSERT INTO `doctor_available_times` (`doctor_id`, `available_times`) VALUES
(1, '09:00-10:00'),
(1, '10:00-11:00'),
(1, '11:00-12:00'),
(1, '14:00-15:00'),
(2, '10:00-11:00'),
(2, '11:00-12:00'),
(2, '14:00-15:00'),
(2, '15:00-16:00'),
(3, '09:00-10:00'),
(3, '11:00-12:00'),
(3, '14:00-15:00'),
(3, '16:00-17:00'),
(4, '09:00-10:00'),
(4, '10:00-11:00'),
(4, '15:00-16:00'),
(4, '16:00-17:00'),
(5, '09:00-10:00'),
(5, '10:00-11:00'),
(5, '14:00-15:00'),
(5, '15:00-16:00'),
(6, '09:00-10:00'),
(6, '10:00-11:00'),
(6, '11:00-12:00'),
(6, '14:00-15:00'),
(7, '09:00-10:00'),
(7, '10:00-11:00'),
(7, '15:00-16:00'),
(7, '16:00-17:00'),
(8, '10:00-11:00'),
(8, '11:00-12:00'),
(8, '14:00-15:00'),
(8, '15:00-16:00'),
(9, '09:00-10:00'),
(9, '11:00-12:00'),
(9, '13:00-14:00'),
(9, '14:00-15:00'),
(10, '10:00-11:00'),
(10, '11:00-12:00'),
(10, '14:00-15:00'),
(10, '16:00-17:00'),
(11, '09:00-10:00'),
(11, '12:00-13:00'),
(11, '14:00-15:00'),
(11, '15:00-16:00'),
(12, '10:00-11:00'),
(12, '11:00-12:00'),
(12, '13:00-14:00'),
(12, '14:00-15:00'),
(13, '13:00-14:00'),
(13, '14:00-15:00'),
(13, '15:00-16:00'),
(13, '16:00-17:00'),
(14, '09:00-10:00'),
(14, '10:00-11:00'),
(14, '14:00-15:00'),
(14, '16:00-17:00'),
(15, '10:00-11:00'),
(15, '11:00-12:00'),
(15, '13:00-14:00'),
(15, '14:00-15:00'),
(16, '09:00-10:00'),
(16, '11:00-12:00'),
(16, '14:00-15:00'),
(16, '16:00-17:00'),
(17, '09:00-10:00'),
(17, '10:00-11:00'),
(17, '11:00-12:00'),
(17, '12:00-13:00'),
(18, '09:00-10:00'),
(18, '10:00-11:00'),
(18, '11:00-12:00'),
(18, '15:00-16:00'),
(19, '13:00-14:00'),
(19, '14:00-15:00'),
(19, '15:00-16:00'),
(19, '16:00-17:00'),
(20, '10:00-11:00'),
(20, '13:00-14:00'),
(20, '14:00-15:00'),
(20, '15:00-16:00'),
(21, '09:00-10:00'),
(21, '10:00-11:00'),
(21, '14:00-15:00'),
(21, '15:00-16:00'),
(22, '10:00-11:00'),
(22, '11:00-12:00'),
(22, '14:00-15:00'),
(22, '16:00-17:00'),
(23, '11:00-12:00'),
(23, '13:00-14:00'),
(23, '15:00-16:00'),
(23, '16:00-17:00'),
(24, '12:00-13:00'),
(24, '13:00-14:00'),
(24, '14:00-15:00'),
(24, '15:00-16:00'),
(25, '09:00-10:00'),
(25, '10:00-11:00'),
(25, '14:00-15:00'),
(25, '15:00-16:00');

-- --------------------------------------------------------

--
-- Table structure for table `patient`
--

CREATE TABLE `patient` (
  `id` bigint NOT NULL,
  `address` varchar(255) NOT NULL,
  `date_of_birth` date DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `emergency_contact` varchar(255) DEFAULT NULL,
  `insurance_provider` varchar(100) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `patient`
--

INSERT INTO `patient` (`id`, `address`, `date_of_birth`, `email`, `emergency_contact`, `insurance_provider`, `name`, `password`, `phone`) VALUES
(1, '101 Oak St, Cityville', NULL, 'jane.doe@example.com', NULL, NULL, 'Jane Doe', 'passJane1', '888-111-1111'),
(2, '202 Maple Rd, Townsville', NULL, 'john.smith@example.com', NULL, NULL, 'John Smith', 'smithSecure', '888-222-2222'),
(3, '303 Pine Ave, Villageton', NULL, 'emily.rose@example.com', NULL, NULL, 'Emily Rose', 'emilyPass99', '888-333-3333'),
(4, '404 Birch Ln, Metropolis', NULL, 'michael.j@example.com', NULL, NULL, 'Michael Jordan', 'airmj23', '888-444-4444'),
(5, '505 Cedar Blvd, Springfield', NULL, 'olivia.m@example.com', NULL, NULL, 'Olivia Moon', 'moonshine12', '888-555-5555'),
(6, '606 Spruce Ct, Gotham', NULL, 'liam.k@example.com', NULL, NULL, 'Liam King', 'king321', '888-666-6666'),
(7, '707 Aspen Dr, Riverdale', NULL, 'sophia.l@example.com', NULL, NULL, 'Sophia Lane', 'sophieLane', '888-777-7777'),
(8, '808 Elm St, Newtown', NULL, 'noah.b@example.com', NULL, NULL, 'Noah Brooks', 'noahBest!', '888-888-8888'),
(9, '909 Willow Way, Star City', NULL, 'ava.d@example.com', NULL, NULL, 'Ava Daniels', 'avaSecure8', '888-999-9999'),
(10, '111 Chestnut Pl, Midvale', NULL, 'william.h@example.com', NULL, NULL, 'William Harris', 'willH2025', '888-000-0000'),
(11, '112 Redwood St, Fairview', NULL, 'mia.g@example.com', NULL, NULL, 'Mia Green', 'miagreen1', '889-111-1111'),
(12, '113 Cypress Rd, Edgewater', NULL, 'james.b@example.com', NULL, NULL, 'James Brown', 'jamiebrown', '889-222-2222'),
(13, '114 Poplar Ave, Crestwood', NULL, 'amelia.c@example.com', NULL, NULL, 'Amelia Clark', 'ameliacool', '889-333-3333'),
(14, '115 Sequoia Dr, Elmwood', NULL, 'ben.j@example.com', NULL, NULL, 'Ben Johnson', 'bennyJ', '889-444-4444'),
(15, '116 Palm Blvd, Harborview', NULL, 'ella.m@example.com', NULL, NULL, 'Ella Monroe', 'ellam123', '889-555-5555'),
(16, '117 Cottonwood Ct, Laketown', NULL, 'lucas.t@example.com', NULL, NULL, 'Lucas Turner', 'lucasTurn', '889-666-6666'),
(17, '118 Sycamore Ln, Hilltop', NULL, 'grace.s@example.com', NULL, NULL, 'Grace Scott', 'graceful', '889-777-7777'),
(18, '119 Magnolia Pl, Brookside', NULL, 'ethan.h@example.com', NULL, NULL, 'Ethan Hill', 'hill2025', '889-888-8888'),
(19, '120 Fir St, Woodland', NULL, 'ruby.w@example.com', NULL, NULL, 'Ruby Ward', 'rubypass', '889-999-9999'),
(20, '121 Beech Way, Lakeside', NULL, 'jack.b@example.com', NULL, NULL, 'Jack Baker', 'bakerjack', '889-000-0000'),
(21, '122 Alder Ave, Pinehill', NULL, 'mia.h@example.com', NULL, NULL, 'Mia Hall', 'hallMia', '890-111-1111'),
(22, '123 Hawthorn Blvd, Meadowbrook', NULL, 'owen.t@example.com', NULL, NULL, 'Owen Thomas', 'owen123', '890-222-2222'),
(23, '124 Dogwood Dr, Summit', NULL, 'ivy.j@example.com', NULL, NULL, 'Ivy Jackson', 'ivyIvy', '890-333-3333'),
(24, '125 Juniper Ct, Greenwood', NULL, 'leo.m@example.com', NULL, NULL, 'Leo Martin', 'leopass', '890-444-4444'),
(25, '126 Olive Rd, Ashville', NULL, 'ella.moore@example.com', NULL, NULL, 'Ella Moore', 'ellamoore', '890-555-5555');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `appointment`
--
ALTER TABLE `appointment`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKoeb98n82eph1dx43v3y2bcmsl` (`doctor_id`),
  ADD KEY `FK4apif2ewfyf14077ichee8g06` (`patient_id`);

--
-- Indexes for table `doctor`
--
ALTER TABLE `doctor`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `doctor_available_times`
--
ALTER TABLE `doctor_available_times`
  ADD KEY `FKdgs10srq75djpwnb9c22k3lmk` (`doctor_id`);

--
-- Indexes for table `patient`
--
ALTER TABLE `patient`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `appointment`
--
ALTER TABLE `appointment`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=131;

--
-- AUTO_INCREMENT for table `doctor`
--
ALTER TABLE `doctor`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `patient`
--
ALTER TABLE `patient`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `appointment`
--
ALTER TABLE `appointment`
  ADD CONSTRAINT `FK4apif2ewfyf14077ichee8g06` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`),
  ADD CONSTRAINT `FKoeb98n82eph1dx43v3y2bcmsl` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`);

--
-- Constraints for table `doctor_available_times`
--
ALTER TABLE `doctor_available_times`
  ADD CONSTRAINT `FKdgs10srq75djpwnb9c22k3lmk` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
