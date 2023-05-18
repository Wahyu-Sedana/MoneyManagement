<?php 
	include_once("../../config/connection.php");
	$success = false;
	$error = true;
	$message = "Invalid data";
	$data = [];

	if (isset($_GET['id_user']) && !empty($_GET['id_user'])) {
		$id_user = addslashes($_GET['id_user']);

		$cond='';
		if (isset($_GET['search']) && !empty($_GET['search'])) {
			$search = addslashes($_GET['search']);
			$cond.=" AND _kategori LIKE '%$search%'";
		}

		if (isset($_GET['id_jenis']) && !empty($_GET['id_jenis'])) {
			$id_jenis = addslashes($_GET['id_jenis']);
			$cond.=" AND _id_jenis = '$id_jenis'";
		}
	
		$dataqr = $db->query("SELECT _id_kategori as id_kategori, _id_jenis as id_jenis, _kategori as kategori FROM kategori_ WHERE _status=1 AND _id_user='$id_user' $cond");
	
		if ($dataqr->num_rows) {
			while ($row = $dataqr->fetch_object()) {
				$data[] = $row;
			}
			$success = true;
			$error = false;
			$message = "Sukses Load Data";
		}
		else
		{
			$message = "Data Kosong";
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