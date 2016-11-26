package nikhil.texttodb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "distance";
    Button alert;
    Context context;
    GPSTracker gpsTracker;
    ImageView image;
    AsyncTask<locationscan, Integer, Double> message;
    String message1;
    Button scan;

    /* renamed from: nikhil.texttodb.MainActivity.1 */
    class C01641 implements OnClickListener {
        C01641() {
        }

        public void onClick(View v) {
            MainActivity.this.startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), 0);
        }
    }

    /* renamed from: nikhil.texttodb.MainActivity.2 */
    class C01652 implements OnClickListener {
        C01652() {
        }

        public void onClick(View v) {
            locationscan params = new locationscan(MainActivity.this.gpsTracker.sendLongitude(), MainActivity.this.gpsTracker.sendLatitude());
            new GPStaskscan(null).execute(new locationscan[]{params});
        }
    }

    private class GPStask extends AsyncTask<location, Integer, Double> {
        private GPStask() {
        }

        protected Double doInBackground(location... params) {
            postData(params[0].longitude, params[0].latitude, params[0].image_str);
            return null;
        }

        protected void onPostExecute(Double result) {
            Toast.makeText(MainActivity.this.getApplicationContext(), "GPS Location of Wildness saved", 1).show();
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData(double longitude, double latitude, String image_str) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://aas.pe.hu/alert.php");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList(2);
                nameValuePairs.add(new BasicNameValuePair("longitude", Double.toString(longitude)));
                nameValuePairs.add(new BasicNameValuePair("latitude", Double.toString(latitude)));
                nameValuePairs.add(new BasicNameValuePair("image", image_str));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpclient.execute(httppost);
            } catch (ClientProtocolException e) {
            } catch (IOException e2) {
            }
        }
    }

    private class GPStaskscan extends AsyncTask<locationscan, Integer, Double> {
        private GPStaskscan() {
        }

        protected Double doInBackground(locationscan... params) {
            postData(params[0].longitude, params[0].latitude);
            return null;
        }

        protected void onPostExecute(Double result) {
            Toast.makeText(MainActivity.this.getApplicationContext(), "Scanned...", 1).show();
            Intent next = new Intent(MainActivity.this, Main2Activity.class);
            Log.d("here", "here : " + MainActivity.this.message1);
            next.putExtra(MainActivity.EXTRA_MESSAGE, MainActivity.this.message1);
            MainActivity.this.startActivity(next);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        public void postData(double longitude, double latitude) {
            try {
                String SetServerString = (String) new DefaultHttpClient().execute(new HttpGet("http://aas.pe.hu/scan.php?lat=" + Double.toString(latitude) + "&long=" + Double.toString(longitude)), new BasicResponseHandler());
                Log.d("tag", "output: " + SetServerString);
                Double dist = Double.valueOf(Double.parseDouble(SetServerString));
                int dot;
                if (dist.doubleValue() < 1.0d) {
                    dist = Double.valueOf(dist.doubleValue() * 1000.0d);
                    MainActivity.this.message1 = Double.toString(dist.doubleValue());
                    dot = MainActivity.this.message1.indexOf(46);
                    MainActivity.this.message1 = MainActivity.this.message1.substring(0, Math.min(dot + 3, MainActivity.this.message1.length())) + " Ms";
                    return;
                }
                MainActivity.this.message1 = SetServerString;
                dot = MainActivity.this.message1.indexOf(46);
                MainActivity.this.message1 = MainActivity.this.message1.substring(0, Math.min(dot + 3, MainActivity.this.message1.length())) + " KMs";
            } catch (ClientProtocolException e) {
                Log.d("exception", "exception 1");
            } catch (IOException e2) {
                Log.d("exception", "exception 2");
            }
        }
    }

    private static class location {
        String image_str;
        double latitude;
        double longitude;

        location(double longitude, double latitude, String image_str) {
            this.longitude = longitude;
            this.latitude = latitude;
            this.image_str = image_str;
        }
    }

    private static class locationscan {
        double latitude;
        double longitude;

        locationscan(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0166R.layout.activity_main);
        this.context = this;
        this.gpsTracker = new GPSTracker(this.context);
        this.image = (ImageView) findViewById(C0166R.id.imageView);
        this.alert = (Button) findViewById(C0166R.id.button1);
        this.alert.setOnClickListener(new C01641());
        this.scan = (Button) findViewById(C0166R.id.button2);
        this.scan.setOnClickListener(new C01652());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bp = (Bitmap) data.getExtras().get("data");
        this.image.setImageBitmap(bp);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bp.compress(CompressFormat.PNG, 90, stream);
        location params = new location(this.gpsTracker.sendLongitude(), this.gpsTracker.sendLatitude(), Base64.encodeToString(stream.toByteArray(), 0));
        new GPStask().execute(new location[]{params});
    }
}