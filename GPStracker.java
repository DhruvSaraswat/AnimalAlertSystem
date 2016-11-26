package nikhil.texttodb;

import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 60000;
    boolean canGetLocation;
    boolean isGPSEnabled;
    boolean isNetworkEnabled;
    double latitude;
    Location location;
    protected LocationManager locationManager;
    double longitude;
    private Context mContext;

    /* renamed from: nikhil.texttodb.GPSTracker.1 */
    class C01621 implements OnClickListener {
        C01621() {
        }

        public void onClick(DialogInterface dialog, int which) {
            GPSTracker.this.mContext.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
        }
    }

    /* renamed from: nikhil.texttodb.GPSTracker.2 */
    class C01632 implements OnClickListener {
        C01632() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    public GPSTracker(Context context) {
        this.isGPSEnabled = false;
        this.isNetworkEnabled = false;
        this.canGetLocation = false;
        this.location = null;
        this.mContext = context;
        getLocation();
        Log.d("lat n log", "latitude : " + Double.toString(this.latitude) + " and longitude : " + Double.toString(this.longitude));
    }

    public Location getLocation() {
        try {
            this.locationManager = (LocationManager) this.mContext.getSystemService("location");
            this.isGPSEnabled = this.locationManager.isProviderEnabled("gps");
            this.isNetworkEnabled = this.locationManager.isProviderEnabled("network");
            if (this.isGPSEnabled || this.isNetworkEnabled) {
                this.canGetLocation = true;
                if (this.isNetworkEnabled) {
                    this.locationManager.requestLocationUpdates("network", MIN_TIME_BW_UPDATES, 10.0f, this);
                    Log.d("Network", "Network Enabled");
                    if (this.locationManager != null) {
                        this.location = this.locationManager.getLastKnownLocation("network");
                        if (this.location != null) {
                            this.latitude = this.location.getLatitude();
                            this.longitude = this.location.getLongitude();
                        }
                    }
                }
                if (this.isGPSEnabled && this.location == null) {
                    this.locationManager.requestLocationUpdates("gps", MIN_TIME_BW_UPDATES, 10.0f, this);
                    Log.d("GPS", "GPS Enabled");
                    if (this.locationManager != null) {
                        this.location = this.locationManager.getLastKnownLocation("gps");
                        if (this.location != null) {
                            this.latitude = this.location.getLatitude();
                            this.longitude = this.location.getLongitude();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.location;
    }

    public void stopUsingGPS() {
        if (this.locationManager != null) {
            this.locationManager.removeUpdates(this);
        }
    }

    public double getLatitude() {
        if (this.location != null) {
            this.latitude = this.location.getLatitude();
        }
        return this.latitude;
    }

    public double getLongitude() {
        if (this.location != null) {
            this.longitude = this.location.getLongitude();
        }
        return this.longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        Builder alertDialog = new Builder(this.mContext);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new C01621());
        alertDialog.setNegativeButton("Cancel", new C01632());
        alertDialog.show();
    }

    public void onLocationChanged(Location location) {
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

    public double sendLongitude() {
        return this.longitude;
    }

    public double sendLatitude() {
        return this.latitude;
    }
}