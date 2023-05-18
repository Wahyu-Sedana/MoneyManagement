<?php 
	include_once("../../config/connection.php");
	$success = false;
	$error = true;
	$message = "Invalid data";
	$data = [];

	if (isset($_POST['id_user']) && !empty($_POST['id_user']) 
	&& isset($_POST['nama_usaha']) && !empty($_POST['nama_usaha']) 
	&& isset($_POST['alamat']) && !empty($_POST['alamat']) 
	&& isset($_POST['email']) && !empty($_POST['email'])) {
		$nama_usaha = addslashes($_POST['nama_usaha']);
		$alamat = addslashes($_POST['alamat']);
		$email = addslashes($_POST['email']);
		$id_user = addslashes($_POST['id_user']);
		$password_lama = (isset($_POST['password_lama']) && !empty($_POST['password_lama'])) ? addslashes($_POST['password_lama']) : '';
		$password_baru = (isset($_POST['password_baru']) && !empty($_POST['password_baru'])) ? addslashes($_POST['password_baru']) : '';
		
		if($password_lama!='' && $password_baru=='')
		{
			$message = "Password baru belum terisi";
		}
		else if($password_baru!='' && $password_lama=='')
		{
			$message = "Password lama belum terisi";
		}
		else
		{
			$pass_valid = 1;
			if($password_lama!='' && $password_baru!='')
			{
				$password_lama_enc=md5($password_lama);
				$dataqr = $db->query("SELECT _id_user FROM user_ WHERE _email='$email' AND _password='$password_lama_enc'");
				$pass_valid = ($dataqr->num_rows) ? 1 : 0;
			}

			if($password_baru!='' && $pass_valid == 0)
			{
				$message = "Password lama tidak valid";
			}
			else
			{
				if($password_baru!='')
				{
					$password_baru=md5($password_baru);
					$query= "UPDATE user_ SET _nama_usaha= '$nama_usaha', _alamat= '$alamat', _password='$password_baru' WHERE _id_user=$id_user";
				}
				else
				{
					$query= "UPDATE user_ SET _nama_usaha= '$nama_usaha', _alamat= '$alamat' WHERE _id_user=$id_user";
				}
				
				$update = $db->query($query);
				if($update)
				{	
					$success = true;
					$error = false;
					$message = "Sukses Update Profile";
				}
				else
				{
					$message = "Gagal Update Profile";
				}
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