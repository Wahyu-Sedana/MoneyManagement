<?php
// session_start();
header("Content-type: application/vnd.ms-excel");
header("Content-Disposition: attachment; filename=excel_labarugi.xls");
include_once("../config/connection.php");
?>
<style>
*
{
	font-size:14px;
}
table
{
	font-size:11px;
	border: thin solid;
}
th
{
	border-bottom: thin solid;
	font-size:6px;
}
td
{
	border-bottom: thin solid;
	font-size:11px;
}
.vertical-border
{
	border-right: thin solid;
}
.horizontal-border
{
	border-top: thin solid;
}
.horizontal-border-right
{
	border-top: thin solid;
	border-right: thin solid;
}
.horizontal-dotted
{
	border-top: thin dotted;
}
.horizontal-dotted-right
{
	border-top: thin dotted;
	border-right: thin solid;
}
.lampiran{
	border:1px solid #000;
	padding-top:2px;
	font-weight:bold;
	font-size:12px;
	width:80px;
	text-align:center;
}

.bersih{
	border:0px;
	font-size:12px;
}

h3
{
	font-size:18px;
}
</style>
<style type="text/css" media="print">
    @page {
        size: landscape;
    }
</style>

<?php
$total_pemasukan = 0;
$total_pengeluaran = 0;
$laba_rugi = 0;
$data_pemasukan = [];
$data_pengeluaran = [];
$cond="";

if (isset($_GET['id_user']) && !empty($_GET['id_user'])) {
    $id_user = addslashes($_GET['id_user']);

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
    }
}

?>

<h2><center> Laporan<br>
Laba/Rugi Sederhana <br>
<br><br>
<table width='100%' cellspacing = '0'>
	<tr>
		<th  width='5%' class = 'vertical-border'>No</th>
		<th  width = '50%' class = 'vertical-border'>Keterangan</th>
		<th  width = '20%' class = 'vertical-border'>Nominal</th>
		<th  width = '25%' class = 'vertical-border'>Total</th>
    </tr>

    <tr>
		<td class = 'vertical-border'></td>
		<td class = 'vertical-border'><b>Pemasukan</b></td>
		<td class = 'vertical-border'></td>
		<td class = 'vertical-border'></td>
	</tr>
<?php

if(count($data_pemasukan))
{
    for($im=0;$im<count($data_pemasukan); $im++)
    {
    ?>
        <tr>
            <td class = 'vertical-border'><?=$im+1?></td>
            <td class = 'vertical-border'><?=$data_pemasukan[$im]->catatan?></td>
            <td class = 'vertical-border'><?=$data_pengeluaran[$im]->jumlah?></td>
            <td class = 'vertical-border'></td>
        </tr>
    <?php
    }
}

    
?>
    <tr>
        <td class = 'vertical-border'></td>
        <td class = 'vertical-border'></td>
        <td class = 'vertical-border'></td>
        <td class = 'vertical-border'><b><?=$total_pemasukan?></b></td>
    </tr>
    <tr>
        <td class = 'vertical-border'></td>
        <td class = 'vertical-border'></td>
        <td class = 'vertical-border'></td>
        <td class = 'vertical-border'></td>
    </tr>

    <tr>
		<td class = 'vertical-border'></td>
		<td class = 'vertical-border'><b>Pengeluaran</b></td>
		<td class = 'vertical-border'></td>
		<td class = 'vertical-border'></td>
	</tr>
<?php
if(count($data_pengeluaran))
{
    for($ik=0;$ik<count($data_pengeluaran); $ik++)
    {
    ?>
        <tr>
            <td class = 'vertical-border'><?=$ik+1?></td>
            <td class = 'vertical-border'><?=$data_pengeluaran[$ik]->catatan?></td>
            <td class = 'vertical-border'><?=$data_pengeluaran[$ik]->jumlah?></td>
            <td class = 'vertical-border'></td>
        </tr>
    <?php
    }
}
?>
    <tr>
        <td class = 'vertical-border'></td>
        <td class = 'vertical-border'></td>
        <td class = 'vertical-border'></td>
        <td class = 'vertical-border'><b><?=$total_pengeluaran?></b></td>
    </tr>
    <tr>
        <td class = 'vertical-border'></td>
        <td class = 'vertical-border'></td>
        <td class = 'vertical-border'></td>
        <td class = 'vertical-border'></td>
    </tr>
    <tr>
        <td class = 'vertical-border'></td>
        <td class = 'vertical-border'><b>Laba/Rugi</b></td>
        <td class = 'vertical-border'></td>
        <td class = 'vertical-border'><b><?=$laba_rugi?></b></td>
    </tr>
</table>
