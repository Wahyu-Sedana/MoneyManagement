<?php 
	include_once("../../config/connection.php");
	$success = false;
	$error = true;
	$message = "Invalid data";
	$data = [];

	if (isset($_POST['id_kategori']) && !empty($_POST['id_kategori']) 
	&& isset($_POST['jumlah']) && !empty($_POST['jumlah']) 
	&& isset($_POST['tanggal']) && !empty($_POST['tanggal']) 
	&& isset($_POST['catatan']) && !empty($_POST['catatan'])) {
		$id_kategori = addslashes($_POST['id_kategori']);
		$jumlah = addslashes($_POST['jumlah']);
		$tanggal = addslashes($_POST['tanggal']);
		$catatan = addslashes($_POST['catatan']);

		$insert = $db->query("INSERT INTO transaksi_ (_id_kategori, _jumlah, _catatan, _tanggal) VALUES ('$id_kategori','$jumlah','$catatan','$tanggal')");
		if($insert)
		{	
			$success = true;
			$error = false;
			$message = "Sukses Tambah Transaksi";
		}
		else
		{
			$message = "Gagal Tambah Transaksi";
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