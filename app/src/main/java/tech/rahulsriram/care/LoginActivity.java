package tech.rahulsriram.care;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
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
    SharedPreferences sp;

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
        sp = getSharedPreferences("Care", MODE_PRIVATE);
        countryCode = (EditText) findViewById(R.id.countryCode);
        phoneNumber = (EditText) findViewById(R.id.mobileNumber);
        if(phoneNumber.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("id",deviceId);
        editor.commit();

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.next,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        code = countryCode.getText().toString();
        if (code.length() == 0) {
            code = getString(R.string.default_country_code);
        }

        number = code + phoneNumber.getText().toString();
        number = code + phoneNumber.getText().toString();
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("number",number);
        editor.apply();

        if (number.length() >= 12) {
            new Registration(LoginActivity.this).execute(deviceId, number, name, code);
            Log.i(deviceId, "on button click");
        }
        else{
            Toast.makeText(this,"error",Toast.LENGTH_LONG).show();
        }
//        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Had a snack at Snackbar", Snackbar.LENGTH_LONG);
//        View view = snack.getView();
//        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
//        params.gravity = Gravity.TOP;
//        view.setLayoutParams(params);
//        snack.show()
        return false;
    }

    class Registration extends AsyncTask<String,Void,String>
    {       Context context;
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
            dialog.setMessage("Registering");
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
        }
        @Override
        protected String doInBackground(String ...var) {
            String data_string="id=" + sp.getString("id", "") + "&number=" + sp.getString("number", "");//"id=&number=&code=";
            StringBuilder sb=new StringBuilder();
            try {
                URL url= new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(2000);
                httpURLConnection.setReadTimeout(2000);
                OutputStreamWriter bufferedWriter =new OutputStreamWriter(httpURLConnection.getOutputStream());
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                BufferedReader reader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                line=reader.readLine();
                httpURLConnection.disconnect();
                return line;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "auth_fail";
            } catch (IOException e) {
                e.printStackTrace();
                return "auth_fail";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(a,"postxecute");
            Toast.makeText(context,result,Toast.LENGTH_LONG).show();
            b=result;
            if (b.equals("auth_fail")) {
                Toast.makeText(context,"error",Toast.LENGTH_LONG).show();
            } else {
                startActivity(new Intent(LoginActivity.this, AllNotifications.class));
                startService(new Intent(LoginActivity.this, CareService.class));
            }
            dialog.dismiss();
        }
    }

}

