<?php 
	include_once("../../config/connection.php");
	$success = false;
	$error = true;
	$message = "Invalid data";
	$total_pemasukan = 0;
	$total_pengeluaran = 0;
	$saldo = 0;
	$data = [];

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

		$dataqr = $db->query("SELECT k._id_jenis as id_jenis, k._kategori as kategori, t._id_transaksi as id_transaksi, t._jumlah as jumlah, t._catatan as catatan, DATE_FORMAT(t._tanggal, '%d-%m-%Y') as tanggal, DATE_FORMAT(t._waktu_insert, '%d-%m-%Y %H:%I:%s') as waktu_input 
		FROM transaksi_ t 
		INNER JOIN kategori_ k ON t._id_kategori=k._id_kategori 
		WHERE k._id_user='$id_user' $cond");
	
		if ($dataqr->num_rows) {
			while ($row = $dataqr->fetch_object()) {
				$row->jenis = ($row->id_jenis=='1') ? 'Pemasukan' : 'Pengeluaran';
				if($row->id_jenis=='1')
				{
					$total_pemasukan = $total_pemasukan+$row->jumlah;
				}

				if($row->id_jenis=='2')
				{
					$total_pengeluaran = $total_pengeluaran+$row->jumlah;
				}

				$data[] = $row;
			}

			$saldo = $total_pemasukan-$total_pengeluaran;

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
			"total_pemasukan"=>$total_pemasukan,
			"total_pengeluaran"=>$total_pengeluaran,
			"saldo"=>$saldo,
			"message"=>$message
		)
	));
?>