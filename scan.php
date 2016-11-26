<?php
	
$mysql_hostname = "mysql.hostinger.in";
$mysql_user = "u646198511_muthu";
$mysql_password = "goingwoman";
$mysql_database = "u646198511_aas";
$database_connect = new mysqli($mysql_hostname,$mysql_user,$mysql_password,$mysql_database);
if ($database_connect->connect_error) {
    die("Connection failed: " . $database_connect->connect_error);
} 


function distanceGeoPoints ($lat1, $lng1, $lat2, $lng2) {

    $earthRadius = 3958.75;

    $dLat = deg2rad($lat2-$lat1);
    $dLng = deg2rad($lng2-$lng1);


    $a = sin($dLat/2) * sin($dLat/2) +
       cos(deg2rad($lat1)) * cos(deg2rad($lat2)) *
       sin($dLng/2) * sin($dLng/2);
    $c = 2 * atan2(sqrt($a), sqrt(1-$a));
    $dist = $earthRadius * $c;

    // from miles
    $meterConversion = 1609;
    $geopointDistance = $dist * $meterConversion;
    $geopointDistance = $geopointDistance/1000;

    return $geopointDistance;
}

if($_SERVER["REQUEST_METHOD"]=="GET")
{
		$user_lat = doubleval($_GET["lat"]);
		$user_long = doubleval($_GET["long"]);

		$query = "select * from location ";
		$result = mysqli_query($database_connect,$query);
		$min_dist = PHP_INT_MAX;

		while($assoc=mysqli_fetch_assoc($result)) {

			$time = $assoc['intime'];
			$longitude = $assoc['longitude'];
			$latitude = $assoc['latitude'];
		
			$dist = distanceGeoPoints($user_lat, $user_long, $latitude, $longitude);
			if($dist < $min_dist) {
				$min_dist = $dist;
			}
                        
		}	
		
		echo $min_dist;	
}
return "HI";
?>	