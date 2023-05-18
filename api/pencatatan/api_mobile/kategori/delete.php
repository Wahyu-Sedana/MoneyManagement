<?php 
	include_once("../../config/connection.php");
	$success = false;
	$error = true;
	$message = "Invalid data";
	$data = [];

	if (isset($_POST['id_kategori']) && !empty($_POST['id_kategori'])) {
		$id_kategori = addslashes($_POST['id_kategori']);
		$dataqr = $db->query("SELECT _id_kategori as id_kategori FROM transaksi_ WHERE _id_kategori=$id_kategori");

		if ($dataqr->num_rows) {
			$update = $db->query("UPDATE kategori_ SET _status=0 WHERE _id_kategori=$id_kategori");
			if($update)
			{	
				$success = true;
				$error = false;
				$message = "Sukses Hapus Kategori.";
			}
			else
			{
				$message = "Gagal Hapus Kategori";
			}
		}
		else
		{
			$update = $db->query("DELETE FROM kategori_ WHERE _id_kategori=$id_kategori");
			if($update)
			{	
				$success = true;
				$error = false;
				$message = "Sukses Hapus Kategori";
			}
			else
			{
				$message = "Gagal Hapus Kategori";
			}
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