<?php  
date_default_timezone_set("Asia/Makassar");

$host ="localhost";
$username = "root";
$password ="";
$database ="pencatatan";

$db = mysqli_connect($host,$username,$password,$database);
if ($db->error) {
	die("error : ".$db->error);
}
?>