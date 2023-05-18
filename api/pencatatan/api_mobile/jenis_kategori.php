<?php 
	$success = true;
	$error = false;
	$message = "Data Jenis Kategori";
	$data = [];
	$data[0]['id_jenis']=1;
	$data[0]['jenis_kategori']='Pemasukan';
	$data[1]['id_jenis']=2;
	$data[1]['jenis_kategori']='Pengeluaran';

	print_r(json_encode(
		array(
			"success"=>$success,
			"error"=>$error,
			"data"=>$data,
			"message"=>$message
		)
	));
?>