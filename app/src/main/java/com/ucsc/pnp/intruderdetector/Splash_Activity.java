package com.ucsc.pnp.intruderdetector;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Splash_Activity extends AppCompatActivity {
    ProgressDialog progDailog;
    ImageView IntruderImage;
    IntruderReceiver intruderReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        IntruderImage=(ImageView)findViewById(R.id.IntruderImage);
        progDailog=ProgressDialog.show(this,"Wait", "Loading");

        startService(new Intent(this, NetLink.class));

        IntentFilter filter = new IntentFilter("com.ucsc.pnp.intruder.CUSTOM");

        intruderReceiver = new IntruderReceiver();
        registerReceiver(intruderReceiver, filter);


        new InitLoader().execute();




    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(intruderReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class InitLoader extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(4000,0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progDailog.hide();
        }
    }

    class IntruderReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String jsonmsg = intent.getStringExtra("data");
            Toast.makeText(getApplicationContext(),"Intruder Alert",Toast.LENGTH_LONG).show();
            try {
                JSONObject jsonObject = new JSONObject(jsonmsg);
                String immg = jsonObject.getString("img");
                byte[] decodedString = Base64.decode(immg, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                IntruderImage.setImageBitmap(decodedByte);

            } catch (JSONException e) {
                e.printStackTrace();

            }


        }
    }


}
