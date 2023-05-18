<?php 
	include_once("../../config/connection.php");
	$success = false;
	$error = true;
	$message = "Invalid data";
	$data = [];

	if (isset($_GET['id_user']) && !empty($_GET['id_user'])) {
		$id_user = addslashes($_GET['id_user']);

		$cond='';
		$dataqr = $db->query("SELECT _id_user as id_user, _nama_usaha as nama_usaha, _alamat as alamat, _email as email, _tgl_register as tgl_register FROM user_ WHERE _id_user='$id_user' $cond");
	
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