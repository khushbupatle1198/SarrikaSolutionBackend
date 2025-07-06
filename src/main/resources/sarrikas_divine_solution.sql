-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 06, 2025 at 07:34 PM
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
-- Database: `sarrikas_divine_solution`
--

-- --------------------------------------------------------

--
-- Table structure for table `banners`
--

CREATE TABLE `banners` (
  `id` bigint(20) NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `banners`
--

INSERT INTO `banners` (`id`, `image_url`, `name`) VALUES
(1, '1751034768057_1.png', '1'),
(2, '1751034783796_2.png', '2');

-- --------------------------------------------------------

--
-- Table structure for table `blog`
--

CREATE TABLE `blog` (
  `id` bigint(20) NOT NULL,
  `author` varchar(255) DEFAULT NULL,
  `content` text DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `featured` bit(1) NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `blog`
--

INSERT INTO `blog` (`id`, `author`, `content`, `date`, `featured`, `image_url`, `title`) VALUES
(1, 'Sarrika', 'Your Kundli, or birth chart, is the blueprint of your life. Created using the exact time, date, and place of your birth, it maps the positions of celestial bodies and reveals your natural inclinations, strengths, challenges, and purpose.\r\n\r\nWhat You’ll Discover:\r\nPersonality traits and life purpose\r\n\r\nCareer, health, and relationship patterns\r\n\r\nDoshas (like Mangal Dosha or Kaal Sarp) and remedies\r\n\r\nAuspicious periods for major decisions\r\n\r\nWhether you\'re at a crossroads or starting a new phase in life, a detailed Kundli analysis offers deep clarity.\r\n\r\n📌 Tip: Combine Kundli analysis with gemstone or mantra recommendations for better results.', '2025-06-29 08:21:02.000000', b'1', '1751165462405_kundli-analysis.png', ' Kundli Analysis – Your Vedic Roadmap to Destiny'),
(2, 'Sarrika', 'Tarot cards are a powerful tool for self-reflection and spiritual growth. Unlike astrology, Tarot taps into your present energy and provides intuitive guidance based on symbols and archetypes.\r\n\r\nIdeal For:\r\nLove and relationship clarity\r\n\r\nCareer or business decisions\r\n\r\nPersonal transformation\r\n\r\nUnblocking emotional energy\r\n\r\nEach reading is unique, offering answers and clarity to your most pressing questions. Tarot is not about predicting the future — it\'s about empowering your decisions.\r\n\r\n📌 Pro Tip: Focus on one specific question to receive the clearest insight.', '2025-06-29 08:21:56.000000', b'1', '1751165516052_tarot-reading.png', 'Tarot Card Reading – The Intuitive Guide to Your Inner World'),
(3, 'Sarrika', 'What if changing a letter in your name could change your life? According to Name Numerology, each letter emits a frequency that influences your personal energy and success.\r\n\r\nHow It Helps:\r\nAlign your name with favorable numbers\r\n\r\nUnlock confidence, leadership, and luck\r\n\r\nImprove relationships and career paths\r\n\r\nSuitable for personal and business names\r\n\r\nWe analyze your name based on Pythagorean or Chaldean numerology, suggest minor corrections, and balance your name with your birth number for maximum harmony.\r\n\r\n📌 Note: A balanced name can remove energy blockages and invite new opportunities.', '2025-06-29 08:23:02.000000', b'1', '1751165582814_Name Fixing.png', '🔢 Name Numerology – Your Name Holds Power'),
(4, 'Sarrika', 'Crystals are more than just beautiful stones — they are ancient tools for emotional and spiritual healing. When used correctly, they align your chakras, cleanse negative energy, and restore balance.\r\n\r\nPopular Crystals:\r\nAmethyst – For peace and protection\r\n\r\nRose Quartz – For love and emotional healing\r\n\r\nCitrine – For wealth and abundance\r\n\r\nBlack Tourmaline – For protection and grounding\r\n\r\nEach crystal is selected based on your birth chart or current life challenges. They can be worn, meditated with, or placed in your home or workplace.\r\n\r\n📌 Bonus: Energized and cleansed crystals work faster and deeper — always cleanse them with salt, sunlight, or sound.', '2025-06-29 08:23:40.000000', b'1', '1751165620245_crystal-healing.png', '💎 Crystal Healing – Balance Your Energy with the Earth’s Gifts');

-- --------------------------------------------------------

--
-- Table structure for table `consultations`
--

CREATE TABLE `consultations` (
  `id` bigint(20) NOT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `consultations`
--

INSERT INTO `consultations` (`id`, `description`, `image_url`, `price`, `title`) VALUES
(1, 'Detailed Vedic birth chart reading revealing planetary influences, life path, and remedies.', '1751166780973_kundli-analysis.png', '1100', 'Kundli Analysis'),
(2, 'Intuitive tarot reading for love, career, health & spiritual guidance.', '1751166833653_tarot-reading.png', '990', 'Tarot Card Reading'),
(3, 'Analyze mobile number energy with missing number remedies & personalized suggestions.', '1751166874421_mobile-number-numerology.png', '930', 'Mobile Number Numerology'),
(4, 'Decode your name vibrations and suggest name corrections for luck and alignment.', '1751166943601_name-numerology.png', '800', 'Name Numerology'),
(5, 'Traditional number reading based on birth date for personality, life events, and balance.', '1751166978136_vedic-numerology.png', '1000', 'Vedic Numerology'),
(6, 'Ancient Chinese grid reading for strengths, weaknesses, and remedies.', '1751167015582_loshu-grid.png', '750', 'Loshu Grid Mastery'),
(7, 'Design numerology-based logos to align business identity with success energy.', '1751167060844_logo-design.png', '1500', 'Logo Design (Numerology Based)'),
(8, 'Western numerology for brand or name using soft sounding word vibrations.', '1751167137804_switchwords.png', '700', 'Western Switch Word'),
(9, 'Personalized healing using energized crystals for chakra balance, positivity & peace.', '1751167188471_crystal-healing.png', '950', 'Crystal Healing'),
(10, 'Design your visiting card layout & numerology-based format for career attraction.', '1751167235917_business-card.png', '850', 'Business Card Mastery'),
(11, 'Ancient energy activation through yantras, mantras, and tantra practices.', '1751167273810_vedic-tantra.png', '1300', 'Vedic Tantra/Yantra Mastery'),
(12, 'Personalized remedies including gemstones, rituals, and mantras for life harmony.', '1751167312771_remedies.png', '1000', 'Remedies Expertise');

-- --------------------------------------------------------

--
-- Table structure for table `consultation_bookings`
--

CREATE TABLE `consultation_bookings` (
  `id` bigint(20) NOT NULL,
  `booking_date` datetime(6) DEFAULT NULL,
  `dob` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `payment_proof_url` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `place_of_birth` varchar(255) DEFAULT NULL,
  `questions` varchar(2000) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  `consultation_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `consultation_bookings`
--

INSERT INTO `consultation_bookings` (`id`, `booking_date`, `dob`, `email`, `name`, `payment_proof_url`, `phone`, `place_of_birth`, `questions`, `status`, `time`, `consultation_id`) VALUES
(1, '2025-07-02 22:14:15.000000', '2025-05-14', 'sujitmanapure2002@gmail.com', 'sujit manapure', '1751474655282_clg ID.jpg', '9988776655', 'nagpur', 'i know', 'REJECTED', '22:15', 1),
(2, '2025-07-02 22:27:38.000000', '2025-02-13', 'rajatmanapure09@gmail.com', 'Deep Tibude', '1751475458796_Sujit Profile.jpg', '1234567654', 'Jamb', 'For Demo', 'PENDING', '14:27', 3),
(3, '2025-07-02 22:31:37.000000', '2025-07-01', 'deeptibude@gmail.com', 'Deep Tibude', '1751475697387_business-card.png', '9876543212', 'Jamb', 'qwertyuioiuytre', 'PENDING', '22:34', 3),
(4, '2025-07-02 23:06:11.000000', '2019-10-15', 'sujitmanapure2002@gmail.com', 'sujit manapure', '1751477771666_mobile-number-numerology.png', '9876543212', 'Mumbai', 'Hihihihihih', 'APPROVED', '23:07', 5),
(5, '2025-07-06 08:56:15.000000', '2025-07-03', 'rajatmanapure09@gmail.com', 'Rajat Manapure', '1751772375307_clg ID.jpg', '9921442074', 'Jamb', 'demo', 'APPROVED', '08:57', 7);

-- --------------------------------------------------------

--
-- Table structure for table `courses`
--

CREATE TABLE `courses` (
  `id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `logo_url` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `courses`
--

INSERT INTO `courses` (`id`, `description`, `logo_url`, `price`, `title`) VALUES
(1, 'Discover your life\'s path through detailed Kundli readings based on Vedic astrology. Understand your birth chart, planetary positions, and doshas.', '1751038954460_kundli-analysis.png', '9999', 'Kundli Analysis'),
(2, ' Get intuitive guidance and answers with insightful Tarot card readings for love, career, and personal clarity.', '1751039364108_tarot-reading.png', '2999', 'Tarot Card Reading'),
(3, 'Decode the energy of your mobile number and align it with your destiny for better communication and success.', '1751164917971_mobile-number-numerology.png', '1111', 'Mobile Number Numerology'),
(4, 'Analyze your name to discover its hidden numerological power and make corrections for a more fortunate future.', '1751164964535_name-numerology.png', '222', 'Name Numerology'),
(5, 'Explore the ancient science of Vedic numbers to gain deep insight into your nature, karma, and life direction.', '1751165006383_vedic-numerology.png', '212', 'Vedic Numerology'),
(6, 'Master your destiny with the ancient Chinese Loshu Grid technique — decode your strengths, weaknesses, and life patterns.', '1751165044214_loshu-grid.png', '333', 'Loshu Grid Mastery'),
(7, 'Design impactful and vibration-aligned logos that boost brand energy and prosperity through numerological principles.', '1751165104659_logo-design.png', '1211', 'Logo Design (Numerology-Based)'),
(8, 'Heal and harmonize your energy using powerful crystals tailored to your birth chart and personal energy needs.', '1751165141729_crystal-healing.png', '499', 'Crystal Healing'),
(9, 'Create magnetic business cards using numerology, color, and design principles to attract opportunities and clients.', '1751165212837_business-card.png', '299', 'Business Card Mastery'),
(10, ' Harness divine energy through sacred Tantra and Yantra practices for protection, success, and spiritual growth.', '1751165254977_vedic-tantra.png', '399', 'Vedic Tantra/Yantra Mastery'),
(11, ' Get powerful and personalized Vedic remedies including gemstones, mantras, yantras, and rituals to balance your life.', '1751165298080_remedies.png', '3999', 'Remedies Expertise');

-- --------------------------------------------------------

--
-- Table structure for table `course_purchase`
--

CREATE TABLE `course_purchase` (
  `id` bigint(20) NOT NULL,
  `expiry_date` datetime(6) DEFAULT NULL,
  `payment_proof_path` varchar(255) DEFAULT NULL,
  `purchase_date` datetime(6) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `verified` bit(1) NOT NULL,
  `course_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `course_purchase`
--

INSERT INTO `course_purchase` (`id`, `expiry_date`, `payment_proof_path`, `purchase_date`, `status`, `verified`, `course_id`, `user_id`) VALUES
(1, '2026-06-30 10:22:16.000000', '1751259136846_library.jpg', '2025-06-30 10:22:16.000000', 'COMPLETED', b'0', 2, 1),
(2, '2026-06-30 18:52:37.000000', '1751289757223_kundli-analysis.png', '2025-06-30 18:52:37.000000', 'COMPLETED', b'0', 1, 1),
(3, '2026-06-30 20:00:31.000000', '1751293831704_mobile-number-numerology.png', '2025-06-30 19:57:57.000000', 'APPROVED', b'1', 3, 1),
(7, '2026-06-30 21:06:36.000000', '1751297741662_business-card.png', '2025-06-30 21:06:36.000000', 'PENDING', b'1', 2, 1),
(12, '2026-06-30 22:08:39.000000', '1751301470284_business-card.png', '2025-06-30 22:08:39.000000', 'COMPLETED', b'1', 4, 1),
(14, '2026-06-30 22:21:25.000000', '1751302218072_name-numerology.png', '2025-06-30 22:21:25.000000', 'COMPLETED', b'1', 11, 5),
(15, '2026-07-02 10:04:41.000000', '1751430731886_clg ID.jpg', '2025-07-05 10:04:41.000000', 'APPROVED', b'1', 1, 1),
(16, '2026-07-03 15:07:24.000000', '1751535396236_business-card.png', '2025-07-03 15:07:24.000000', 'COMPLETED', b'0', 6, 1);

-- --------------------------------------------------------

--
-- Table structure for table `ereport`
--

CREATE TABLE `ereport` (
  `id` bigint(20) NOT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ereport`
--

INSERT INTO `ereport` (`id`, `description`, `image_url`, `price`, `title`) VALUES
(1, 'Your mobile number is not just a set of digits – it carries a unique vibration that influences your energy, decisions, and destiny. With our Advance Mobile Numerology Report, you’ll discover how your number affects your life and how to realign it for maximum benefit.', '1751166388155_mobile-number-numerology.png', '598', 'Advance Mobile Numerology Report'),
(2, 'Step into the next five years with confidence and clarity! Our Destiny 5 Years Report offers a deep, personalized forecast based on numerology, designed to guide you in key areas of life including relationships, health, wealth, and more.', '1751166514077_Destiny.png', '930', 'Destiny 5 Years Report');

-- --------------------------------------------------------

--
-- Table structure for table `ereport_points`
--

CREATE TABLE `ereport_points` (
  `ereport_id` bigint(20) NOT NULL,
  `points` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ereport_points`
--

INSERT INTO `ereport_points` (`ereport_id`, `points`) VALUES
(1, 'Mobile Number Prediction'),
(1, 'Missing Number Remedies'),
(1, 'Recommended Mobile Wallpaper'),
(1, 'Affirmation of Health, Relationship, Career'),
(1, 'Affirmation of Money, Job'),
(1, 'Vedic Grid'),
(1, 'Dasha Chart'),
(1, 'and Much More'),
(2, '5 Years Future Forecast'),
(2, 'Lucky Unlucky Numbers'),
(2, 'Missing Numbers Remedies'),
(2, 'Moonlank Number'),
(2, 'Name Analysis'),
(2, 'Marriage and Relationship Analysis'),
(2, 'Finance and Health Analysis'),
(2, 'and Much More');

-- --------------------------------------------------------

--
-- Table structure for table `ereport_purchases`
--

CREATE TABLE `ereport_purchases` (
  `id` bigint(20) NOT NULL,
  `amount_paid` double NOT NULL,
  `birth_place` varchar(255) DEFAULT NULL,
  `birth_time` varchar(255) DEFAULT NULL,
  `date_of_birth` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `payment_proof_url` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `purchase_date` datetime(6) NOT NULL,
  `specific_questions` text DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `ereport_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ereport_purchases`
--

INSERT INTO `ereport_purchases` (`id`, `amount_paid`, `birth_place`, `birth_time`, `date_of_birth`, `email`, `name`, `payment_proof_url`, `phone`, `purchase_date`, `specific_questions`, `status`, `ereport_id`) VALUES
(1, 598, 'Nagpur', '22:22', '2025-01-16', 'sujitmanapure2002@gmail.com', 'sujit manapure', '1751561505242_business-card.png', '9988776655', '2025-07-03 22:21:45.000000', 'no', 'PENDING', 1),
(2, 598, 'Nagpur', '08:32', '2025-07-02', 'sujitmanapure20@gmail.com', 'sujit manapure', '1751770919035_clg ID.jpg', '9921442074', '2025-07-06 08:31:59.000000', 'demo', 'APPROVED', 1);

-- --------------------------------------------------------

--
-- Table structure for table `review`
--

CREATE TABLE `review` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `rating` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `review`
--

INSERT INTO `review` (`id`, `created_at`, `email`, `message`, `name`, `rating`) VALUES
(1, '2025-07-04 21:12:24.000000', 'sujitmanapure2002@gmail.com', 'nice', 'sujit manapure', 4),
(2, '2025-07-04 21:13:13.000000', 'deeptibude@gmail.com', 'good servicess', 'Deep Tibude', 5),
(3, '2025-07-04 21:13:50.000000', 'sujitmanapure20@gmail.com', 'Nice Knowlede', 'Sujit Manapure', 5),
(4, '2025-07-04 21:14:16.000000', 'Sujit@123.com', 'Good', 'Lovely Glaze Beuty Studio', 4);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `birth_place` varchar(255) DEFAULT NULL,
  `birth_time` varchar(255) DEFAULT NULL,
  `dob` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `temporary` bit(1) NOT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `otp_expiry` datetime(6) DEFAULT NULL,
  `profile_image` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `birth_place`, `birth_time`, `dob`, `email`, `full_name`, `password`, `phone`, `temporary`, `otp`, `otp_expiry`, `profile_image`, `created_at`, `role`) VALUES
(1, 'Jamb', '19:18', '2002-02-04 00:00:00.000000', 'sujitmanapure2002@gmail.com', 'Sujit Yadorao Manapure', '12345678', '9284389450', b'0', NULL, NULL, 'dfdf2625-f0bc-4def-ac1d-979a558fd5bc_Sujit Profile.jpg', '2025-07-04 21:23:54.000000', NULL),
(5, 'Jamb', '22:20', '2025-06-04 00:00:00.000000', 'rajatmanapure09@gmail.com', 'Rajat Yadorao manapure', '11223344', '1234567890', b'0', NULL, NULL, NULL, NULL, NULL),
(6, 'Nagpur', NULL, NULL, 'sarika.shrirao@gmail.com', 'Sarrika', 'Tashu@6523@', '9067690333', b'0', NULL, NULL, NULL, NULL, 'ADMIN');

-- --------------------------------------------------------

--
-- Table structure for table `video`
--

CREATE TABLE `video` (
  `id` bigint(20) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `video_url` varchar(255) DEFAULT NULL,
  `course_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `video`
--

INSERT INTO `video` (`id`, `title`, `video_url`, `course_id`) VALUES
(1, 'Kundli Analysis', '1751039004689_Video-446.mp4', 1),
(2, '2nd part', '1751041194776_Video-818.mp4', 1),
(3, '1part', '1751041254369_Video-818.mp4', 2),
(5, '2nd part', '1751042104007_Video-446.mp4', 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `banners`
--
ALTER TABLE `banners`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `blog`
--
ALTER TABLE `blog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `consultations`
--
ALTER TABLE `consultations`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `consultation_bookings`
--
ALTER TABLE `consultation_bookings`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKhmhjgps2gtgmpoe1g7i0m2fj2` (`consultation_id`);

--
-- Indexes for table `courses`
--
ALTER TABLE `courses`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `course_purchase`
--
ALTER TABLE `course_purchase`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK6o5tmp657johjkp856e1u3qko` (`course_id`),
  ADD KEY `FKj7yiyov0rk35n3n58jgsyjh63` (`user_id`);

--
-- Indexes for table `ereport`
--
ALTER TABLE `ereport`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `ereport_points`
--
ALTER TABLE `ereport_points`
  ADD KEY `FKsd6d9ch3xq5vd5he6a9vi0be8` (`ereport_id`);

--
-- Indexes for table `ereport_purchases`
--
ALTER TABLE `ereport_purchases`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKthwh47wn37t66muhs4701hdxa` (`ereport_id`);

--
-- Indexes for table `review`
--
ALTER TABLE `review`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `video`
--
ALTER TABLE `video`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK3dtlahhq51jq8rtid9hrglea5` (`course_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `banners`
--
ALTER TABLE `banners`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `blog`
--
ALTER TABLE `blog`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `consultations`
--
ALTER TABLE `consultations`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `consultation_bookings`
--
ALTER TABLE `consultation_bookings`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `courses`
--
ALTER TABLE `courses`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `course_purchase`
--
ALTER TABLE `course_purchase`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `ereport`
--
ALTER TABLE `ereport`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `ereport_purchases`
--
ALTER TABLE `ereport_purchases`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `review`
--
ALTER TABLE `review`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `video`
--
ALTER TABLE `video`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `consultation_bookings`
--
ALTER TABLE `consultation_bookings`
  ADD CONSTRAINT `FKhmhjgps2gtgmpoe1g7i0m2fj2` FOREIGN KEY (`consultation_id`) REFERENCES `consultations` (`id`);

--
-- Constraints for table `course_purchase`
--
ALTER TABLE `course_purchase`
  ADD CONSTRAINT `FK6o5tmp657johjkp856e1u3qko` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
  ADD CONSTRAINT `FKj7yiyov0rk35n3n58jgsyjh63` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `ereport_points`
--
ALTER TABLE `ereport_points`
  ADD CONSTRAINT `FKsd6d9ch3xq5vd5he6a9vi0be8` FOREIGN KEY (`ereport_id`) REFERENCES `ereport` (`id`);

--
-- Constraints for table `ereport_purchases`
--
ALTER TABLE `ereport_purchases`
  ADD CONSTRAINT `FKthwh47wn37t66muhs4701hdxa` FOREIGN KEY (`ereport_id`) REFERENCES `ereport` (`id`);

--
-- Constraints for table `video`
--
ALTER TABLE `video`
  ADD CONSTRAINT `FK3dtlahhq51jq8rtid9hrglea5` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
