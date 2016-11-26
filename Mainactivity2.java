package nikhil.texttodb;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Main2Activity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0166R.layout.activity_main2);
        String mess = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = (TextView) findViewById(C0166R.id.message);
        Log.d("passed", "passed : " + mess);
        textView.setText(mess);
    }
}