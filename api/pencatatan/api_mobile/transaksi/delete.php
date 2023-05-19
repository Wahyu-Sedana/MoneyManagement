<?php 
	include_once("../../config/connection.php");
	$success = false;
	$error = true;
	$message = "Invalid data";
	$data = [];

	if (isset($_POST['id_transaksi']) && !empty($_POST['id_transaksi'])) {
		$id_transaksi = addslashes($_POST['id_transaksi']);
		$delete = $db->query("DELETE FROM transaksi_ WHERE _id_transaksi=$id_transaksi");
		if($delete)
		{	
			$success = true;
			$error = false;
			$message = "Sukses Hapus Transaksi";
		}
		else
		{
			$message = "Gagal Hapus Transaksi";
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