package tech.rahulsriram.care;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SmsVerificationActivity extends AppCompatActivity {
    String TAG = "care-logger";
    SharedPreferences sp;
    EditText smsCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification);

        sp = getSharedPreferences("Care", MODE_PRIVATE);
        smsCode = (EditText) findViewById(R.id.sms_code);
        if (smsCode.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        Button resendButton = (Button) findViewById(R.id.resend_code_button);
        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RequestSms(SmsVerificationActivity.this).execute();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.next, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        String code = smsCode.getText().toString();

        if (smsCode.hasFocus()) {
            smsCode.clearFocus();
        }

        if (!code.isEmpty()) {
            new Registration(SmsVerificationActivity.this).execute();
        }

        return false;
    }

    class Registration extends AsyncTask<String,Void,String> {
        Context context;
        String link, code;
        ProgressDialog dialog = null;

        public Registration(Context c) {
            context = c;
            code = smsCode.getText().toString();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG, "preexecute");
            dialog = new ProgressDialog(context);
            link = "http://10.0.0.20:8000/register";
            dialog.setMessage("Verifying smsCode");
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String ...var) {
            String data = "id=" + sp.getString("id", "") + "&number=" + sp.getString("number", "") + "&smsCode=" + code;
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
                startActivity(new Intent(SmsVerificationActivity.this, NameInput.class));
                finish();
            } else {
                Snackbar.make(findViewById(R.id.SmsVerificationLayout), "Please try again", Snackbar.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }

    class RequestSms extends AsyncTask<Void, Void, String> {
        Context context;
        ProgressDialog dialog = null;

        RequestSms(Context c) {
            context = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setMessage("Waiting for verification sms");
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            String link = "10.0.0.20:8000/request_sms";
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equals("error")) {
                Snackbar.make(findViewById(R.id.SmsVerificationLayout), "Please try again", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
