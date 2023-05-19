<?php 
	include_once("../../config/connection.php");
	$success = false;
	$error = true;
	$message = "Invalid data";
	$data = [];

	if (isset($_POST['id_kategori']) && !empty($_POST['id_kategori']) 
	&& isset($_POST['kategori']) && !empty($_POST['kategori']) 
	&& isset($_POST['id_user']) && !empty($_POST['id_user']) 
	&& isset($_POST['id_jenis']) && !empty($_POST['id_jenis'])) {
		$id_kategori = addslashes($_POST['id_kategori']);
		$kategori = addslashes($_POST['kategori']);
		$id_jenis = addslashes($_POST['id_jenis']);
		$id_user = addslashes($_POST['id_user']);
		
		$dataqr = $db->query("SELECT _id_kategori as id_kategori FROM kategori_ WHERE _id_jenis='$id_jenis' AND _kategori='$kategori' AND _id_user='$id_user' AND _status='1' AND _id_kategori!='$id_kategori'");

		if ($dataqr->num_rows) {
			$message = "Kategori telah tersedia";
		}
		else
		{
			$query= "UPDATE kategori_ SET _id_jenis= '$id_jenis', _kategori= '$kategori' WHERE _id_kategori=$id_kategori";
			
			$update = $db->query($query);
			if($update)
			{	
				$success = true;
				$error = false;
				$message = "Sukses Update Kategori";
			}
			else
			{
				$message = "Gagal Update Kategori";
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