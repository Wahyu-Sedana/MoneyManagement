<?php 
	include_once("../config/connection.php");
	$success = false;
	$error = true;
	$message = "Lengkapi Data Registrasi";
	$data = [];

	if (isset($_POST['email']) && !empty($_POST['email']) && isset($_POST['alamat']) && !empty($_POST['alamat']) && isset($_POST['password']) && !empty($_POST['password']) && isset($_POST['nama_usaha']) && !empty($_POST['nama_usaha'])) {

		$email = addslashes($_POST['email']);
		$password = md5(addslashes($_POST['password']));
		$nama_usaha = addslashes($_POST['nama_usaha']);
		$alamat = addslashes($_POST['alamat']);

		$dataqr = $db->query("SELECT _id_user as id_user FROM user_ WHERE _email='$email'");
		
		if ($dataqr->num_rows) {
			$message = "Email telah digunakan";
		}
		else
		{
			$tgl = date('Y-m-d');
			$dataInsert = $db->query("INSERT INTO user_ (_nama_usaha, _alamat, _email, _password, _tgl_register) VALUES ('$nama_usaha','$alamat','$email','$password', '$tgl')");
			if($dataInsert)
			{
				$success = true;
				$error = false;
				$message = "Registrasi Berhasil, silahkan login";
			}
			else
			{
				$message = "Gagal tambah data User";
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