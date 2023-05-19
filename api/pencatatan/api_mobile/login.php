<?php 
	include_once("../config/connection.php");
	$success = false;
	$error = true;
	$message = "Invalid data";
	$data = [];

	if (isset($_POST['email']) && !empty($_POST['email']) && isset($_POST['password']) && !empty($_POST['password'])) {
		$email = addslashes($_POST['email']);
		$password = md5(addslashes($_POST['password']));

		$dataqr = $db->query("SELECT _id_user as id_user, _email as email, _nama_usaha as nama_usaha FROM user_ WHERE _email='$email' AND _password='$password'");
		
		if ($dataqr->num_rows) {
			$row = $dataqr->fetch_object();
			// if($row->status>0)
			// {
				$success = true;
				$error = false;
				$message = "Login Sukses";
				$data = $row;
			// }
			// else
			// {
			// 	$message = "Status User Anda tidak aktif";
			// }
		}
		else
		{
			$message = "Email dan Password tidak ditemukan";
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