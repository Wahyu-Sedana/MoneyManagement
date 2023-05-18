<?php 
	include_once("../config/connection.php");
	$success = false;
	$error = true;
	$message = "Invalid data";
	$total_pemasukan = 0;
	$total_pengeluaran = 0;
	$laba_rugi = 0;
	$data_pemasukan = [];
	$data_pengeluaran = [];

	if (isset($_GET['id_user']) && !empty($_GET['id_user'])) {
		$id_user = addslashes($_GET['id_user']);

		$cond='';
		if (isset($_GET['search']) && !empty($_GET['search'])) {
			$search = addslashes($_GET['search']);
			$cond.=" AND _kategori LIKE '%$search%'";
		}

		$tgl_dari = (isset($_GET['tgl_dari']) && !empty($_GET['tgl_dari'])) ? addslashes($_GET['tgl_dari']) : '';
		$tgl_sampai = (isset($_GET['tgl_sampai']) && !empty($_GET['tgl_sampai'])) ? addslashes($_GET['tgl_sampai']) : '';

		if($tgl_dari=='' && $tgl_sampai=='')
		{
			$cond.=" AND MONTH(t._tanggal)= '".date('m')."' AND YEAR(t._tanggal)= '".date('Y')."'";
		}
		else if($tgl_dari!='' && $tgl_sampai!='')
		{
			$cond.=" AND (t._tanggal>='$tgl_dari' && t._tanggal<='$tgl_sampai')";
		}
		else if($tgl_dari!='')
		{
			$cond.=" AND t._tanggal>= '$tgl_dari'";
		}
		else if($tgl_sampai!='')
		{
			$cond.=" AND t._tanggal<= '$tgl_sampai'";
		}

		$dataqr = $db->query("SELECT k._id_jenis as id_jenis, t._jumlah as jumlah, t._catatan as catatan, DATE_FORMAT(t._tanggal, '%d-%m-%Y') as tanggal 
		FROM transaksi_ t 
		INNER JOIN kategori_ k ON t._id_kategori=k._id_kategori 
		WHERE k._id_user='$id_user' $cond");
	
		if ($dataqr->num_rows) {
			while ($row = $dataqr->fetch_object()) {
				if($row->id_jenis=='1')
				{
					$total_pemasukan = $total_pemasukan+$row->jumlah;
					$data_pemasukan[] = $row;
				}

				if($row->id_jenis=='2')
				{
					$total_pengeluaran = $total_pengeluaran+$row->jumlah;
					$data_pengeluaran[] = $row;
				}
			}

			$laba_rugi = $total_pemasukan-$total_pengeluaran;

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
			"data_pemasukan"=>$data_pemasukan,
			"data_pengeluaran"=>$data_pengeluaran,
			"total_pemasukan"=>$total_pemasukan,
			"total_pengeluaran"=>$total_pengeluaran,
			"saldo"=>$laba_rugi,
			"message"=>$message
		)
	));
?>