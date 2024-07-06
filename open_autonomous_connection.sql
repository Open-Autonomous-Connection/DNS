-- phpMyAdmin SQL Dump
-- version 5.2.1deb1ubuntu0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Erstellungszeit: 01. Jul 2024 um 17:51
-- Server-Version: 10.11.2-MariaDB-1
-- PHP-Version: 8.1.12-1ubuntu4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `open_autonomous_connection`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `accounts`
--

CREATE TABLE `accounts` (
  `id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `apikeys`
--

CREATE TABLE `apikeys` (
  `id` int(11) NOT NULL,
  `application` varchar(255) NOT NULL,
  `keyapi` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `config`
--

CREATE TABLE `config` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `config`
--

INSERT INTO `config` (`id`, `name`, `value`) VALUES
(1, 'allow_register_tld', '0'),
(2, 'allow_register_domain', '1'),
(3, 'allow_register_account', '1'),
(4, 'max_apikeys', '5');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `domains`
--

CREATE TABLE `domains` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `topleveldomain` varchar(255) NOT NULL,
  `destination` varchar(255) NOT NULL,
  `accesskey` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `domains`
--



-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `topleveldomains`
--

CREATE TABLE `topleveldomains` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `accesskey` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `topleveldomains`
--

INSERT INTO `topleveldomains` (`id`, `name`, `accesskey`) VALUES
(1, 'root', '1cf5e6c49913ac8a21cee452ca64bf03599e3e24be41779754c5d4e795ef22cd'),
(2, 'dev', '181ce64fe28c3a960356e31056544faa534f91587faa34802f994a81eae088e3'),
(3, 'chat', '1903ed16fadec59ea0b56d7d79c68714d940d9ce7c13e2ce4f420ea3b0197d8f'),
(4, 'info', '89652e1621555b06dcfde89ca1396a599145e49acbba2157445a83f48d8f1cd0'),
(5, 'ngo', 'cb2143278688d32570f1a5f7f22fa04cb239e5e10379c7f0e72079a15c77a0e1'),
(6, 'int', 'cd3545e0566e6d30401032fbe277eb173feee17822755349e56b47c2bcf92792'),
(7, 'world', '8d67358d90113af39c0268b5fd7b9a51ebb1f97626f1b8ba7c776e6e650ba450'),
(8, 'org', '06b855eef817be988d47903836ee06a5f6842bbee3f971a02d638382063c1005'),
(9, 'net', 'b3183a6b1db2681c1e31eadbe4281aff3ddc8841bba75541be5e631bb8d12c63'),
(10, 'admin', '6880e992d7104681e3ee729d2560c9cfce5743f91f688dd98671b6f7bb297554'),
(11, 'web', 'd155f6203c2cd27dd0efc4ccd9543c43b7c757c098ce9c3170458c2eab848940'),
(12, 'it', 'bcf7be434aae76586dabdc0526616afb24184dbe4b82627f249a5f79575bf128'),
(13, 'now', 'c06a46ca32c43637678f425ea8bed3c60d8785fefaefab0d889a6aec0a4a2257'),
(14, 'edu', '51ad136b4c54a174d6e0d8d51e0e3003aea929359fd6b74becebac47e5b038e9');

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `apikeys`
--
ALTER TABLE `apikeys`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `config`
--
ALTER TABLE `config`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `domains`
--
ALTER TABLE `domains`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `topleveldomains`
--
ALTER TABLE `topleveldomains`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `accounts`
--
ALTER TABLE `accounts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `apikeys`
--
ALTER TABLE `apikeys`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `config`
--
ALTER TABLE `config`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT für Tabelle `domains`
--
ALTER TABLE `domains`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT für Tabelle `topleveldomains`
--
ALTER TABLE `topleveldomains`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
