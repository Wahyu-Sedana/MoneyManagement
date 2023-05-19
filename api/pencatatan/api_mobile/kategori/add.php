<?php 
	include_once("../../config/connection.php");
	$success = false;
	$error = true;
	$message = "Invalid data";
	$data = [];

	if (isset($_POST['id_user']) && !empty($_POST['id_user']) && isset($_POST['kategori']) && !empty($_POST['kategori']) && isset($_POST['id_jenis']) && !empty($_POST['id_jenis'])) {
		$kategori = addslashes($_POST['kategori']);
		$id_jenis = addslashes($_POST['id_jenis']);
		$id_user = addslashes($_POST['id_user']);

		$dataqr = $db->query("SELECT _id_kategori as id_kategori FROM kategori_ WHERE _kategori='$kategori' AND _id_user='$id_user' AND _id_jenis='$id_jenis' AND _status=1");

		if ($dataqr->num_rows) {
			$message = "Kategori telah ada";
		}
		else
		{
			$insert = $db->query("INSERT INTO kategori_ (_id_jenis, _kategori, _status, _id_user) VALUES ('$id_jenis','$kategori','1','$id_user')");
			if($insert)
			{	
				$success = true;
				$error = false;
				$message = "Sukses Tambah Kategori";
			}
			else
			{
				$message = "Gagal Tambah Kategori";
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