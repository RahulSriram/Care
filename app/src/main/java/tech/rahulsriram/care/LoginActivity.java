package tech.rahulsriram.care;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    String deviceId,a11,name,str_result="1",number,code, b="auth_fail";
    EditText phoneNumber, countryCode;
    TextView textView;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        actionBar=getSupportActionBar();
//        getSupportActionBar().setHomeButtonEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.a);
//        actionBar.setIcon(R.drawable.a);
        textView=(TextView)findViewById(R.id.textView2);
        countryCode = (EditText) findViewById(R.id.countryCode);
        phoneNumber = (EditText) findViewById(R.id.mobileNumber);
        if(phoneNumber.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Button login = (Button) findViewById(R.id.loginButton);
        assert login != null;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = countryCode.getText().toString();
                if (code.length() == 0) {
                    code = getString(R.string.default_country_code);
                }

                number = code + phoneNumber.getText().toString();

                if (number.length() >= 12) {
                    new Registration(LoginActivity.this).execute(deviceId, number, name, code);
                    Log.i(deviceId, "on button click");
                    if (b.equals("auth_fail")) {
                        Snackbar snackbar = Snackbar
                                .make(v, b, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    } else {
                        startActivity(new Intent(LoginActivity.this, AllNotifications.class));
                        startService(new Intent(LoginActivity.this, CareService.class));
                    }
                    //finish();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(v, "Aleardy Exists", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.next,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        new Registration(LoginActivity.this).execute(deviceId,number,name,code);
            if(b.equals("auth_fails")){
            Snackbar snackbar = Snackbar
                    .make(this.findViewById(android.R.id.content), b, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        else{
            startActivity(new Intent(LoginActivity.this, AllNotifications.class));
            startService(new Intent(LoginActivity.this, CareService.class));
        }

//        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Had a snack at Snackbar", Snackbar.LENGTH_LONG);
//        View view = snack.getView();
//        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
//        params.gravity = Gravity.TOP;
//        view.setLayoutParams(params);
//        snack.show();
        return false;
    }

    class Registration extends AsyncTask<String,Void,String>
    {       Context context;
        //        public interface AsyncResponse {
//                void processFinish(String output);
//        }
//
//        public AsyncResponse delegate = null;
//
//        public Registration(AsyncResponse delegate){
//                this.delegate = delegate;
//        }
        String add_info_url,line="0",a;
        ProgressDialog dialog=null;
        public Registration(Context cont)
        {
            context=cont;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(a,"preexecute");
            dialog=new ProgressDialog(context);
            add_info_url = "http://10.0.0.20:8000/register";
            dialog.setMessage("loading");
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
        }
        @Override
        protected String doInBackground(String ...var) {
            String data_string="id=&number=&code=";
            StringBuilder sb=new StringBuilder();
            try {
                URL url= new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                //httpURLConnection.setConnectTimeout(10000);
                OutputStreamWriter bufferedWriter =new OutputStreamWriter(httpURLConnection.getOutputStream());
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                BufferedReader reader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                line=reader.readLine();
                httpURLConnection.disconnect();
                return line;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return new String("Exception: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                return new String("Exception: " + e.getMessage());
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(a,"postxecute");
            dialog.dismiss();
            Toast.makeText(context,result,Toast.LENGTH_LONG).show();
            b=result;
        }
    }

}

