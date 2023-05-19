<?php 
	include_once("../../config/connection.php");
	$success = false;
	$error = true;
	$message = "Invalid data";
	$data = [];

	if (isset($_POST['id_transaksi']) && !empty($_POST['id_transaksi']) 
	&& isset($_POST['id_kategori']) && !empty($_POST['id_kategori']) 
	&& isset($_POST['jumlah']) && !empty($_POST['jumlah']) 
	&& isset($_POST['tanggal']) && !empty($_POST['tanggal']) 
	&& isset($_POST['catatan']) && !empty($_POST['catatan'])) {
		$id_transaksi = addslashes($_POST['id_transaksi']);
		$id_kategori = addslashes($_POST['id_kategori']);
		$jumlah = addslashes($_POST['jumlah']);
		$tanggal = addslashes($_POST['tanggal']);
		$catatan = addslashes($_POST['catatan']);
		
		$query= "UPDATE transaksi_ SET _id_kategori= '$id_kategori', _jumlah= '$jumlah', _catatan= '$catatan', _tanggal= '$tanggal' WHERE _id_transaksi=$id_transaksi";
		$update = $db->query($query);
		if($update)
		{	
			$success = true;
			$error = false;
			$message = "Sukses Update Transaksi";
		}
		else
		{
			$message = "Gagal Update Transaksi";
		}
	}

	print_r(json_encode(
		array(
			"success"=>$success,
			"error"=>$error,
			"data"=>$data,
			"message"=>$message
		)
	));
?>