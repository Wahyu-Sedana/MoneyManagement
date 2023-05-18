-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 18 Bulan Mei 2023 pada 16.45
-- Versi server: 10.4.22-MariaDB
-- Versi PHP: 7.4.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pencatatan`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `kategori_`
--

CREATE TABLE `kategori_` (
  `_id_kategori` int(5) NOT NULL,
  `_id_user` int(5) NOT NULL,
  `_id_jenis` tinyint(1) NOT NULL DEFAULT 1,
  `_kategori` varchar(100) NOT NULL,
  `_status` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `kategori_`
--

INSERT INTO `kategori_` (`_id_kategori`, `_id_user`, `_id_jenis`, `_kategori`, `_status`) VALUES
(1, 1, 1, 'Penjualan', 1),
(2, 1, 1, 'Sumbangan', 1),
(3, 2, 1, 'Sumbangan', 1),
(5, 1, 2, 'Retribusi', 1);

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksi_`
--

CREATE TABLE `transaksi_` (
  `_id_transaksi` int(9) NOT NULL,
  `_id_kategori` int(5) NOT NULL,
  `_jumlah` double NOT NULL,
  `_catatan` varchar(100) NOT NULL,
  `_tanggal` date NOT NULL,
  `_waktu_insert` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `transaksi_`
--

INSERT INTO `transaksi_` (`_id_transaksi`, `_id_kategori`, `_jumlah`, `_catatan`, `_tanggal`, `_waktu_insert`) VALUES
(2, 2, 3000, 'Bansos Pemerintah', '2023-05-18', '2023-05-18 00:17:19'),
(3, 5, 5000, 'Bayar Karcis', '2023-05-15', '2023-05-18 00:26:37');

-- --------------------------------------------------------

--
-- Struktur dari tabel `user_`
--

CREATE TABLE `user_` (
  `_id_user` int(5) NOT NULL,
  `_nama_usaha` varchar(100) NOT NULL,
  `_alamat` varchar(200) NOT NULL,
  `_email` varchar(100) NOT NULL,
  `_password` varchar(225) NOT NULL,
  `_tgl_register` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data untuk tabel `user_`
--

INSERT INTO `user_` (`_id_user`, `_nama_usaha`, `_alamat`, `_email`, `_password`, `_tgl_register`) VALUES
(1, 'Toko Sembako', 'Jl. Hayam Wuruk', 'satya@bamboomedia.net', 'd8578edf8458ce06fbc5bb76a58c5ca4', '2023-05-15'),
(3, 'Go Go', 'JL. Akasia', 'adam@bamboomedia.net', 'd8578edf8458ce06fbc5bb76a58c5ca4', '2023-05-15'),
(4, 'Go Go Ca', 'JL. Akasia', 'adamd@bamboomedia.net', 'd8578edf8458ce06fbc5bb76a58c5ca4', '2023-05-15');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `kategori_`
--
ALTER TABLE `kategori_`
  ADD PRIMARY KEY (`_id_kategori`);

--
-- Indeks untuk tabel `transaksi_`
--
ALTER TABLE `transaksi_`
  ADD PRIMARY KEY (`_id_transaksi`);

--
-- Indeks untuk tabel `user_`
--
ALTER TABLE `user_`
  ADD PRIMARY KEY (`_id_user`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `kategori_`
--
ALTER TABLE `kategori_`
  MODIFY `_id_kategori` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT untuk tabel `transaksi_`
--
ALTER TABLE `transaksi_`
  MODIFY `_id_transaksi` int(9) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT untuk tabel `user_`
--
ALTER TABLE `user_`
  MODIFY `_id_user` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
