package tech.rahulsriram.care;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SREEVATHSA on 22-08-2016.
 */
public class SplashActivity extends AppCompatActivity{
    String TAG = "care-logger";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);
        sp = getSharedPreferences("Care", MODE_PRIVATE);

        new SplashTask().execute();
    }

    class SplashTask extends AsyncTask<String,Void,String> {
        String add_info_url,line="0";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "preexecute");

            add_info_url = "http://10.0.0.20:8000/login";
        }

        @Override
        protected String doInBackground(String ...var) {
            if(!sp.getString("id", "").isEmpty() && !sp.getString("number", "").isEmpty()) {
                String data_string = "id=" + sp.getString("id", "") + "&number=" + sp.getString("number", "");

                try {
                    URL url = new URL(add_info_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setConnectTimeout(2000);
                    httpURLConnection.setReadTimeout(2000);
                    OutputStreamWriter bufferedWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
                    bufferedWriter.write(data_string);
                    bufferedWriter.flush();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    line = reader.readLine();
                    httpURLConnection.disconnect();
                } catch (Exception e) {
                    return "error";
                }

                return line;
            } else {
                return "error";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG, "postexecute");

            if (result.equals("ok")) {
                startActivity(new Intent(SplashActivity.this, AllNotifications.class));
                finish();
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }
    }
}
