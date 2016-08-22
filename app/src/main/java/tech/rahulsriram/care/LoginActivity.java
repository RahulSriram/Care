package tech.rahulsriram.care;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    String verify, smsCode, TAG = "care-logger";
    EditText phoneNumber, countryCode;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getSharedPreferences("Care", MODE_PRIVATE);
        countryCode = (EditText) findViewById(R.id.countryCode);
        phoneNumber = (EditText) findViewById(R.id.mobileNumber);
        if(phoneNumber.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        String deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("id", deviceId);
        editor.apply();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.next, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        String code = countryCode.getText().toString();

        if (phoneNumber.hasFocus()) {
            phoneNumber.clearFocus();
        }

        if (countryCode.hasFocus()) {
            countryCode.clearFocus();
        }

        if (code.length() == 0) {
            code = getString(R.string.default_country_code);
        }

        String number = code + phoneNumber.getText().toString();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("number", number);
        editor.apply();

        if (number.length() >= 12) {
            new Registration(LoginActivity.this).execute();
            Log.i(TAG, "on button click");
        } else {
            Snackbar.make(findViewById(R.id.loginLayout), "Please type your number", Snackbar.LENGTH_LONG).show();
        }
        return false;
    }

    class RequestSms extends AsyncTask<Void, Void, String> {
        Context context;
        String link = "10.0.0.20:8000/request_sms";
        ProgressDialog dialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setMessage("Registering");
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            String data = "number=" + sp.getString("number", "");
            StringBuilder sb = new StringBuilder();
            try {
                URL url= new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
                writer.write(data);
                writer.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                httpURLConnection.disconnect();
            } catch (Exception e) {
                return "error";
            }

            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            verify = s;
        }
    }

    class Registration extends AsyncTask<String,Void,String> {
        Context context;
        String link;
        ProgressDialog dialog=null;

        public Registration(Context cont)
        {
            context=cont;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "preexecute");
            dialog = new ProgressDialog(context);
            link = "http://10.0.0.20:8000/register";
            dialog.setMessage("Registering");
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String ...var) {
            String data_string = "id=" + sp.getString("id", "") + "&number=" + sp.getString("number", "") + "&code=" + smsCode;
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                OutputStreamWriter bufferedWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String line;
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                httpURLConnection.disconnect();
            } catch (Exception e) {
                return "error";
            }

            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG,"postexecute");
            Toast.makeText(context,result,Toast.LENGTH_LONG).show();

            if (result.equals("ok")) {
                startActivity(new Intent(LoginActivity.this, AllNotifications.class));
                startService(new Intent(LoginActivity.this, CareService.class));
            } else {
                Toast.makeText(context, "Please try again.",Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }

}