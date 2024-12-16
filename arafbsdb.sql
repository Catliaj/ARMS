-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 16, 2024 at 09:06 AM
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
-- Database: `arafbsdb`
--

-- --------------------------------------------------------

--
-- Table structure for table `apartment`
--

CREATE TABLE `apartment` (
  `unitID` int(11) NOT NULL,
  `unitCode` varchar(100) NOT NULL,
  `unitType` enum('Solo','Couple','Family') NOT NULL,
  `description` text NOT NULL,
  `rentAmount` double NOT NULL,
  `occupants` int(11) DEFAULT NULL,
  `status` enum('Available','Occupied') NOT NULL,
  `BedSpace` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `apartment`
--

INSERT INTO `apartment` (`unitID`, `unitCode`, `unitType`, `description`, `rentAmount`, `occupants`, `status`, `BedSpace`) VALUES
(1, 'SOL-101', 'Solo', 'A cozy studio designed for individuals looking for comfort and privacy.', 4800, 1, 'Occupied', 1),
(2, 'SOL-102', 'Solo', 'Modern studio with a touch of elegance, featuring ample natural light.', 5200, 2, 'Occupied', 1),
(3, 'SOL-103', 'Solo', 'Affordable and practical, with all essentials for solo living.', 4500, 1, 'Occupied', 1),
(4, 'SOL-104', 'Solo', 'A compact and functional unit for those seeking a minimalist lifestyle.', 5000, 1, 'Occupied', 1),
(5, 'SOL-105', 'Solo', 'A sleek, newly renovated studio perfect for individuals working or studying from home.', 5500, 1, 'Occupied', 1),
(6, 'SOL-106', 'Solo', 'Economical unit with a convenient layout and basic furnishings.', 4000, 1, 'Occupied', 1),
(7, 'SOL-107', 'Solo', 'Stylish single unit with modern finishes and a small private balcony.', 5800, 1, 'Occupied', 1),
(8, 'SOL-108', 'Solo', 'Fully furnished studio with a clean and contemporary design.', 6000, NULL, 'Available', 1),
(9, 'COP-101', 'Couple', 'One-bedroom unit with a spacious living area, perfect for a couple.', 7500, 2, 'Occupied', 2),
(10, 'COP-102', 'Couple', 'Bright unit with modern finishes and a private outdoor space.', 7800, NULL, 'Available', 2),
(11, 'COP-103', 'Couple', 'Charming unit with an open-plan living and dining area for couples.', 8000, NULL, 'Available', 2),
(12, 'COP-104', 'Couple', 'Affordable unit with a warm atmosphere and shared amenities.', 7200, NULL, 'Available', 2),
(13, 'COP-105', 'Couple', 'Premium unit featuring a stylish design, ideal for a couple seeking comfort.', 8500, NULL, 'Available', 2),
(14, 'COP-106', 'Couple', 'Comfortable and spacious unit with access to shared amenities.', 7000, NULL, 'Available', 2),
(15, 'COP-107', 'Couple', 'Modern, well-equipped unit with an open-plan design and fresh interiors.', 8800, NULL, 'Available', 2),
(16, 'COP-108', 'Couple', 'Luxurious unit offering a peaceful escape with contemporary features.', 9500, NULL, 'Available', 2),
(17, 'FAM-101', 'Family', 'A spacious family unit featuring a generous living space and a warm atmosphere.', 12500, 5, 'Occupied', 8),
(18, 'FAM-102', 'Family', 'Family-friendly unit with ample storage and a cozy living area for bonding.', 13000, NULL, 'Available', 8),
(19, 'FAM-103', 'Family', 'Modern apartment with a versatile layout, suitable for various family needs.', 14000, NULL, 'Available', 8),
(20, 'FAM-104', 'Family', 'Affordable unit for small families, offering practical space and comfort.', 12000, NULL, 'Available', 8),
(21, 'FAM-105', 'Family', 'Premium family apartment featuring high-end finishes and extra living space.', 15000, NULL, 'Available', 8),
(22, 'FAM-106', 'Family', 'Spacious apartment with a functional layout and child-friendly features.', 13500, NULL, 'Available', 8),
(23, 'FAM-107', 'Family', 'Large, well-maintained family unit with generous living areas for all members.', 16000, NULL, 'Available', 8),
(24, 'FAM-108', 'Family', 'Luxury apartment with top-tier finishes, ideal for a growing family.', 18000, NULL, 'Available', 8);

-- --------------------------------------------------------

--
-- Table structure for table `bills`
--

CREATE TABLE `bills` (
  `billID` int(11) NOT NULL,
  `totalAmount` double NOT NULL,
  `totalBalance` double DEFAULT NULL,
  `dueDate` date NOT NULL,
  `electricityBill` double DEFAULT NULL,
  `waterBill` double DEFAULT NULL,
  `status` enum('Paid','Unpaid','Partially Paid') NOT NULL,
  `facilityID` int(11) DEFAULT NULL,
  `tenantID` int(11) NOT NULL,
  `unitID` int(11) NOT NULL,
  `AdvancePayment` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bills`
--

INSERT INTO `bills` (`billID`, `totalAmount`, `totalBalance`, `dueDate`, `electricityBill`, `waterBill`, `status`, `facilityID`, `tenantID`, `unitID`, `AdvancePayment`) VALUES
(1, 7050, 0, '2025-01-05', 1500, 250, 'Paid', 4, 1, 1, 100),
(2, 9300, 0, '2025-01-05', 1300, 300, 'Paid', 14, 2, 9, 0),
(3, 12500, 12500, '2025-01-09', NULL, NULL, 'Unpaid', NULL, 3, 17, 0),
(8, 6400, 5400, '2025-01-14', 1000, 100, 'Partially Paid', 7, 8, 2, 3000),
(9, 6600, 6600, '2025-01-16', 1000, 1000, 'Unpaid', 8, 14, 3, 1000),
(10, 7100, 7100, '2025-01-16', 1000, 100, 'Unpaid', 9, 15, 4, 500),
(11, 9000, 9000, '2025-01-16', 2000, 500, 'Unpaid', 11, 16, 5, 400),
(12, 8600, 8600, '2025-01-16', 3000, 600, 'Unpaid', 13, 17, 6, 400),
(13, 5800, 5800, '2025-01-16', NULL, NULL, 'Unpaid', NULL, 18, 7, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `facility`
--

CREATE TABLE `facility` (
  `facilityID` int(11) NOT NULL,
  `facilityName` varchar(255) NOT NULL,
  `facilityBill` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `facility`
--

INSERT INTO `facility` (`facilityID`, `facilityName`, `facilityBill`) VALUES
(1, 'Wifi', 400),
(2, 'Wifi', 500),
(3, 'Parking', 200),
(4, '', 500),
(5, 'parking', 100),
(6, '', 100),
(7, '', 100),
(8, 'Solo Room', 100),
(9, 'Solo', 1000),
(10, 'solo', 1000),
(11, '', 1000),
(12, 'idk', 1000),
(13, '', 1000),
(14, '', 200);

-- --------------------------------------------------------

--
-- Table structure for table `ledger`
--

CREATE TABLE `ledger` (
  `ledgerID` int(11) NOT NULL,
  `tenantID` int(11) NOT NULL,
  `paymentID` int(11) NOT NULL,
  `billID` int(11) NOT NULL,
  `balanceAfterPayment` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ledger`
--

INSERT INTO `ledger` (`ledgerID`, `tenantID`, `paymentID`, `billID`, `balanceAfterPayment`) VALUES
(1, 1, 1, 1, 2050),
(2, 2, 2, 2, 3300),
(3, 1, 3, 1, 1050),
(4, 1, 4, 1, 0),
(5, 8, 5, 8, 4400),
(6, 2, 6, 2, 0);

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE `payment` (
  `paymentID` int(11) NOT NULL,
  `paymentAmount` double NOT NULL,
  `paymentDate` date NOT NULL,
  `billID` int(11) NOT NULL,
  `tenantID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payment`
--

INSERT INTO `payment` (`paymentID`, `paymentAmount`, `paymentDate`, `billID`, `tenantID`) VALUES
(1, 5000, '2024-12-08', 1, 1),
(2, 6000, '2024-12-09', 2, 2),
(3, 1000, '2024-12-10', 1, 1),
(4, 1050, '2024-12-13', 1, 1),
(5, 1000, '2024-12-15', 8, 8),
(6, 3300, '2024-12-16', 2, 2);

-- --------------------------------------------------------

--
-- Table structure for table `rentalcontract`
--

CREATE TABLE `rentalcontract` (
  `rentalContractID` int(11) NOT NULL,
  `rentStart` date NOT NULL,
  `rentEnd` date NOT NULL,
  `unitID` int(11) NOT NULL,
  `tenantID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rentalcontract`
--

INSERT INTO `rentalcontract` (`rentalContractID`, `rentStart`, `rentEnd`, `unitID`, `tenantID`) VALUES
(1, '2024-12-05', '2024-12-25', 1, 1),
(2, '2024-12-05', '2024-12-12', 9, 2),
(3, '2024-12-09', '2024-12-26', 17, 3),
(8, '2024-12-14', '2024-12-22', 2, 8),
(14, '2024-12-16', '2024-12-11', 3, 14),
(15, '2024-12-16', '2024-12-06', 4, 15),
(16, '2024-12-16', '2024-12-18', 5, 16),
(17, '2024-12-16', '2024-12-20', 6, 17),
(18, '2024-12-16', '2024-12-17', 7, 18);

-- --------------------------------------------------------

--
-- Table structure for table `rentalhistory`
--

CREATE TABLE `rentalhistory` (
  `historyID` int(11) NOT NULL,
  `unitID` int(11) NOT NULL,
  `tenantID` int(11) NOT NULL,
  `rentalContractID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rentalhistory`
--

INSERT INTO `rentalhistory` (`historyID`, `unitID`, `tenantID`, `rentalContractID`) VALUES
(1, 1, 1, 1),
(2, 9, 2, 2),
(3, 17, 3, 3),
(8, 2, 8, 8),
(14, 3, 14, 14),
(15, 4, 15, 15),
(16, 5, 16, 16),
(17, 6, 17, 17),
(18, 7, 18, 18);

-- --------------------------------------------------------

--
-- Table structure for table `tenant`
--

CREATE TABLE `tenant` (
  `tenantID` int(11) NOT NULL,
  `firstName` varchar(100) NOT NULL,
  `lastName` varchar(100) NOT NULL,
  `contactNum` varchar(100) NOT NULL,
  `email` varchar(255) NOT NULL,
  `additionalInfo` text NOT NULL,
  `unitID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tenant`
--

INSERT INTO `tenant` (`tenantID`, `firstName`, `lastName`, `contactNum`, `email`, `additionalInfo`, `unitID`) VALUES
(1, 'Naicel', 'Apolona', '09064064647', 'apolona.naicel.p@gmail.com', 'None', 1),
(2, 'Yeoj', 'Valdez', '09266783411', 'valdezyeoj', 'Joyce Corpuz \nFriend', 9),
(3, 'Clarence', 'Adrias', '09059074831', 'adriasclarence@gmail.com', 'Mian\nNicole\nSam\nKirby', 17),
(8, 'jhon paulo', 'boiser', '0912432323', 'boiser@gmail.com', 'awdaw', 2),
(14, 'sharon', 'konet', '94823', 'aljhf', 'awdqw', 3),
(15, 'naicel', 'koneta', '0912445', 'kajhfj', '2134	', 4),
(16, 'asam-le', 'samplrq', '827359827', 'sflj', 'sjfh	', 5),
(17, 'dsmaoke', 'ajhfq', 'jkfhask', 'kjsahef', 'akw	', 6),
(18, 'Moymoy', 'hakdog', '0914732', 'moymoy@gmail.com', 'hadghjw	', 7);

-- --------------------------------------------------------

--
-- Table structure for table `tenanthistory`
--

CREATE TABLE `tenanthistory` (
  `tenantID` int(11) NOT NULL,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `contactNum` varchar(15) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `additionalInfo` text DEFAULT NULL,
  `unitID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tenanthistory`
--

INSERT INTO `tenanthistory` (`tenantID`, `firstName`, `lastName`, `contactNum`, `email`, `additionalInfo`, `unitID`) VALUES
(1, 'Naicel', 'Apolona', '09064064647', 'apolona.naicel.p@gmail.com', 'None', 1),
(2, 'Yeoj', 'Valdez', '09266783411', 'valdezyeoj', 'Joyce Corpuz, Friend', 9),
(3, 'Clarence', 'Adrias', '09059074831', 'adriasclarence@gmail.com', 'Mian, Nicole, Sam, Kirby', 17),
(4, 'Irish', 'Cuenca', '09661435915', 'cuencairish@gmail.com', 'Sofia Tapongco, Jake Estano, Friends', 18),
(5, 'Sofia', 'Tapongco', '09064847409', 'tapongcouzi@gmail.com', 'None', 2);

-- --------------------------------------------------------

--
-- Table structure for table `tenants`
--

CREATE TABLE `tenants` (
  `tenantID` int(11) NOT NULL,
  `firstName` varchar(50) DEFAULT NULL,
  `lastName` varchar(50) DEFAULT NULL,
  `contactNum` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `additionalInfo` text DEFAULT NULL,
  `unitID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tenants`
--

INSERT INTO `tenants` (`tenantID`, `firstName`, `lastName`, `contactNum`, `email`, `additionalInfo`, `unitID`) VALUES
(1, 'Naicel', 'Apolona', '09064064647', 'apolona.naicel.p@gmail.com', 'None', 1),
(2, 'Yeoj', 'Valdez', '09266783411', 'valdezyeoj', 'Joyce Corpuz, Friend', 9),
(3, 'Clarence', 'Adrias', '09059074831', 'adriasclarence@gmail.com', 'Mian, Nicole, Sam, Kirby', 17),
(4, 'Irish', 'Cuenca', '09661435915', 'cuencairish@gmail.com', 'Sofia Tapongco, Jake Estano, Friends', 18);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `apartment`
--
ALTER TABLE `apartment`
  ADD PRIMARY KEY (`unitID`);

--
-- Indexes for table `bills`
--
ALTER TABLE `bills`
  ADD PRIMARY KEY (`billID`),
  ADD KEY `fk_bill_tenant` (`tenantID`),
  ADD KEY `fk_bill_unit` (`unitID`),
  ADD KEY `fk_bill_facility` (`facilityID`);

--
-- Indexes for table `facility`
--
ALTER TABLE `facility`
  ADD PRIMARY KEY (`facilityID`);

--
-- Indexes for table `ledger`
--
ALTER TABLE `ledger`
  ADD PRIMARY KEY (`ledgerID`),
  ADD KEY `fk_ledger_bill` (`billID`),
  ADD KEY `fk_ledger_payment` (`paymentID`),
  ADD KEY `fk_ledger_tenant` (`tenantID`);

--
-- Indexes for table `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`paymentID`),
  ADD KEY `fk_payment_bill` (`billID`),
  ADD KEY `fk_payment_tenant` (`tenantID`);

--
-- Indexes for table `rentalcontract`
--
ALTER TABLE `rentalcontract`
  ADD PRIMARY KEY (`rentalContractID`),
  ADD KEY `fk_rentalContract_tenant` (`tenantID`),
  ADD KEY `fk_rentalContract_unit` (`unitID`);

--
-- Indexes for table `rentalhistory`
--
ALTER TABLE `rentalhistory`
  ADD PRIMARY KEY (`historyID`),
  ADD KEY `fk_rentalHistory_unit` (`unitID`),
  ADD KEY `fk_rentalHistory_rentalContract` (`rentalContractID`),
  ADD KEY `fk_rentalHistory_tenant` (`tenantID`);

--
-- Indexes for table `tenant`
--
ALTER TABLE `tenant`
  ADD PRIMARY KEY (`tenantID`),
  ADD KEY `fk_tenant_unit` (`unitID`);

--
-- Indexes for table `tenanthistory`
--
ALTER TABLE `tenanthistory`
  ADD PRIMARY KEY (`tenantID`),
  ADD KEY `unitID` (`unitID`);

--
-- Indexes for table `tenants`
--
ALTER TABLE `tenants`
  ADD PRIMARY KEY (`tenantID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `apartment`
--
ALTER TABLE `apartment`
  MODIFY `unitID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `bills`
--
ALTER TABLE `bills`
  MODIFY `billID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `facility`
--
ALTER TABLE `facility`
  MODIFY `facilityID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `ledger`
--
ALTER TABLE `ledger`
  MODIFY `ledgerID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `payment`
--
ALTER TABLE `payment`
  MODIFY `paymentID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `rentalcontract`
--
ALTER TABLE `rentalcontract`
  MODIFY `rentalContractID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `rentalhistory`
--
ALTER TABLE `rentalhistory`
  MODIFY `historyID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `tenant`
--
ALTER TABLE `tenant`
  MODIFY `tenantID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `tenanthistory`
--
ALTER TABLE `tenanthistory`
  MODIFY `tenantID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `bills`
--
ALTER TABLE `bills`
  ADD CONSTRAINT `fk_bill_facility` FOREIGN KEY (`facilityID`) REFERENCES `facility` (`facilityID`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_bill_tenant` FOREIGN KEY (`tenantID`) REFERENCES `tenant` (`tenantID`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_bill_unit` FOREIGN KEY (`unitID`) REFERENCES `apartment` (`unitID`) ON DELETE CASCADE;

--
-- Constraints for table `ledger`
--
ALTER TABLE `ledger`
  ADD CONSTRAINT `fk_ledger_bill` FOREIGN KEY (`billID`) REFERENCES `bills` (`billID`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_ledger_payment` FOREIGN KEY (`paymentID`) REFERENCES `payment` (`paymentID`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_ledger_tenant` FOREIGN KEY (`tenantID`) REFERENCES `tenant` (`tenantID`) ON DELETE CASCADE;

--
-- Constraints for table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `fk_payment_bill` FOREIGN KEY (`billID`) REFERENCES `bills` (`billID`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_payment_tenant` FOREIGN KEY (`tenantID`) REFERENCES `tenant` (`tenantID`) ON DELETE CASCADE;

--
-- Constraints for table `rentalcontract`
--
ALTER TABLE `rentalcontract`
  ADD CONSTRAINT `fk_rentalContract_tenant` FOREIGN KEY (`tenantID`) REFERENCES `tenant` (`tenantID`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_rentalContract_unit` FOREIGN KEY (`unitID`) REFERENCES `apartment` (`unitID`) ON DELETE CASCADE;

--
-- Constraints for table `rentalhistory`
--
ALTER TABLE `rentalhistory`
  ADD CONSTRAINT `fk_rentalHistory_rentalContract` FOREIGN KEY (`rentalContractID`) REFERENCES `rentalcontract` (`rentalContractID`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_rentalHistory_tenant` FOREIGN KEY (`tenantID`) REFERENCES `tenant` (`tenantID`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_rentalHistory_unit` FOREIGN KEY (`unitID`) REFERENCES `apartment` (`unitID`) ON DELETE CASCADE;

--
-- Constraints for table `tenant`
--
ALTER TABLE `tenant`
  ADD CONSTRAINT `fk_tenant_unit` FOREIGN KEY (`unitID`) REFERENCES `apartment` (`unitID`) ON DELETE CASCADE;

--
-- Constraints for table `tenanthistory`
--
ALTER TABLE `tenanthistory`
  ADD CONSTRAINT `tenanthistory_ibfk_1` FOREIGN KEY (`unitID`) REFERENCES `apartment` (`unitID`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
