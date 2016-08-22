package tech.rahulsriram.care;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SREEVATHSA on 22-08-2016.
 */
public class NameInput extends AppCompatActivity {


    String name, TAG = "care-logger";
    EditText username;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        sp = getSharedPreferences("Care", MODE_PRIVATE);

        username = (EditText) findViewById(R.id.username);
        name = username.getText().toString();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name", name);
        editor.apply();

        if(username.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.next,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if(name.length()!=0) {
           new NameTask().execute();
        }

        return true;
    }

    class NameTask extends AsyncTask<String,Void,String> {
        Context context;
        String add_info_url,line="0";
        ProgressDialog dialog=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG,"preexecute");
            dialog=new ProgressDialog(context);
            add_info_url = "http://10.0.0.20:8000/set_name";
            dialog.setMessage("Setting name");
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected String doInBackground(String ...var) {
            String data_string="id=" + sp.getString("id", "") + "&number=" + sp.getString("number", "") + "&name=" + sp.getString("name", "");
            StringBuilder sb = new StringBuilder();
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
            } catch (Exception e) {
                return "error";
            }

            return line;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(TAG, "postexecute");

            if (result.equals("ok")) {
                startActivity(new Intent(NameInput.this, AllNotifications.class));
            } else {
                Snackbar.make(findViewById(R.id.usernameLayout), "Couldn't set name. Try again", Snackbar.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }
    }









}
