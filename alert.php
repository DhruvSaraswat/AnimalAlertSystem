<?php
	
$mysql_hostname = "mysql.hostinger.in";
$mysql_user = "u646198511_muthu";
$mysql_password = "goingwoman";
$mysql_database = "u646198511_aas";
$database_connect = new mysqli($mysql_hostname,$mysql_user,$mysql_password,$mysql_database);
if ($database_connect->connect_error) {
    die("Connection failed: " . $database_connect->connect_error);
} 


if($_SERVER["REQUEST_METHOD"]=="POST")
{
		$longitude = $_POST['longitude'];
		$latitude = $_POST['latitude'];
		$image = $_POST['image'];
		date_default_timezone_set('Asia/Kolkata');
		$date = date("Y-m-d h:i:sa");
		$query = "INSERT INTO location (intime,longitude,latitude,image) VALUES ('$date','$longitude','$latitude','$image')";

		if (mysqli_query($database_connect,$query) === TRUE) {
    		echo "New record created successfully";
		} else {
    		echo "Error: database submission<br>";
		}
		

	
}

?>