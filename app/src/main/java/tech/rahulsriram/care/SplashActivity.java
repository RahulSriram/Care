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
        setContentView(R.layout.activity_name_input);

        sp = getSharedPreferences("Care", MODE_PRIVATE);
        new SplashTask().execute();
    }

    class SplashTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String ...var) {
            if (!sp.getString("id", "").isEmpty() && !sp.getString("number", "").isEmpty()) {
                if (!sp.getString("name", "").isEmpty()) {
                    String link = "http://10.0.0.20:8000/login";
                    String data = "id=" + sp.getString("id", "") + "&number=" + sp.getString("number", "");
                    StringBuilder sb = new StringBuilder();

                    try {
                        URL url = new URL(link);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
                        writer.write(data);
                        writer.flush();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        String line;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                        httpURLConnection.disconnect();
                    } catch (Exception e) {
                        return "error";
                    }

                    return sb.toString();
                } else {
                    return "name-empty";
                }
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
            } else if (result.equals("name-empty")) {
                startActivity(new Intent(SplashActivity.this, NameInput.class));
                finish();
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }
    }
}
