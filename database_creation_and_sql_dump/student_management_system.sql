-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 04, 2025 at 03:34 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `student_management_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `email` varchar(255) NOT NULL,
  `fname` varchar(255) DEFAULT NULL,
  `lname` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`email`, `fname`, `lname`, `password`) VALUES
('rjames@mail.com', 'Roberto', 'James', '$2a$10$n/2R/iRKMk.zWZWnf8QAC.R230uGrU8RhFK0QgN9CAj.jEnDYcsPe');

-- --------------------------------------------------------

--
-- Table structure for table `grade`
--

CREATE TABLE `grade` (
  `moduleCode` varchar(20) NOT NULL,
  `studentID` int(11) NOT NULL,
  `grade` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `module`
--

CREATE TABLE `module` (
  `moduleCode` varchar(20) NOT NULL,
  `moduleName` varchar(255) DEFAULT NULL,
  `credits` int(11) DEFAULT NULL CHECK (`credits` in (1,2,3,4)),
  `addedBy` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

CREATE TABLE `student` (
  `studentID` int(11) NOT NULL,
  `fname` varchar(255) DEFAULT NULL,
  `lname` varchar(255) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`studentID`, `fname`, `lname`, `dob`, `email`, `password`) VALUES
(55, 'Tom', 'John', '2001-04-18', 'tj001@gmail.com', '$2a$10$ZvDsEAU7r6D2eZjCiI.2VO1XePMqohd0qhV8sLQ.phuc1y8jbQv8m'),
(56, 'Sara', 'Lee', '2000-07-23', 'sara.lee001@gmail.com', '$2a$10$ZmRi1NfJT4FwrON2779FGuJZIW.YM6rrNFptSeOk5rcmkAP5nPMne'),
(57, 'David', 'Wright', '1999-11-05', 'd.wright002@gmail.com', '$2a$10$A592XEzqsI82RdWzToKzcOXGeuNKun9AIzvxh60hRRLxqLQboE1TW'),
(58, 'Lana', 'Smith', '1995-01-10', 'lana.smith003@gmail.com', '$2a$10$9Jwq539RK61JFqfKVAnC5uKHyExV4.hqOesN3wFW0yJymVFMsgVqi'),
(59, 'Kevin', 'Brown', '2003-09-02', 'kevinb004@gmail.com', '$2a$10$2Xoux1wBroWp6x.jUJeDuu/53dyxcBrI.YU0Qs.flRcz5M8tzYtua'),
(60, 'Olivia', 'Green', '2002-03-28', 'oliviagreen005@gmail.com', '$2a$10$8wzgZH0aW5I.xuTt68CdXODXgsfcy6FUbsK/4g63rTMeCUZSOX6Zy'),
(61, 'Marcus', 'James', '1998-12-15', 'marcus.j006@gmail.com', '$2a$10$Q7uF5lQUPRxyTmewhvwQZ.x9uvggY7yOWDEpHuADmEyYitqJ/bX22'),
(62, 'Emma', 'Stone', '1997-06-20', 'emma.stone007@gmail.com', '$2a$10$I25jTa5asYjW1jllFoPc/udJBdYFFJdwADPeaQYu5W3Hk9IvnZuFi'),
(63, 'Noah', 'Clark', '2000-10-09', 'nclark008@gmail.com', '$2a$10$IpcSOaE4RiciJEB0KU1T0OcAN6zNbIJhWbtiH0InMhN7.fNthmMl.'),
(64, 'Ava', 'Baker', '1996-04-01', 'ava.baker009@gmail.com', '$2a$10$iuQwjE9G4YcyZ11W4MLyDelCbbloa9yk4HqsGLRW0l/li941cv7sC');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`email`);

--
-- Indexes for table `grade`
--
ALTER TABLE `grade`
  ADD PRIMARY KEY (`moduleCode`,`studentID`),
  ADD KEY `fk_grade_student` (`studentID`);

--
-- Indexes for table `module`
--
ALTER TABLE `module`
  ADD PRIMARY KEY (`moduleCode`),
  ADD KEY `fk_module_admin` (`addedBy`);

--
-- Indexes for table `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`studentID`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `student`
--
ALTER TABLE `student`
  MODIFY `studentID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=65;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `grade`
--
ALTER TABLE `grade`
  ADD CONSTRAINT `fk_grade_module` FOREIGN KEY (`moduleCode`) REFERENCES `module` (`moduleCode`),
  ADD CONSTRAINT `fk_grade_student` FOREIGN KEY (`studentID`) REFERENCES `student` (`studentID`) ON DELETE CASCADE;

--
-- Constraints for table `module`
--
ALTER TABLE `module`
  ADD CONSTRAINT `fk_module_admin` FOREIGN KEY (`addedBy`) REFERENCES `admin` (`email`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
