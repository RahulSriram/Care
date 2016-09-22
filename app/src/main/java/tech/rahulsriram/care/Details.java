package tech.rahulsriram.care;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by jebineinstein on 29/8/16.
 */
public class Details extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sp;

    String la, lo, name1, item1, description1, number1, itemid1;

    int tempvalue;

    String secretnumber3;

    ProgressDialog progressDialog3;

    Dialog dialog3;

    AlertDialog.Builder alertDialogBuilder3;

    EditText editText3;

    Button button3;

    ArrayList<String> name3 = new ArrayList<>();
    ArrayList<String> description3 = new ArrayList<>();
    ArrayList<String> item3 = new ArrayList<>();
    ArrayList<String> number3 = new ArrayList<>();
    ArrayList<String> latitude3 = new ArrayList<>();
    ArrayList<String> longitude3 = new ArrayList<>();
    ArrayList<String> itemid3 = new ArrayList<>();

    TextView text1, text2, text3, text4, text5;

    Button button,callbutton,Verify;
    FloatingActionButton map;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        progressDialog3 = new ProgressDialog(this);
        progressDialog3.setMessage("Verifying");
        progressDialog3.setCanceledOnTouchOutside(false);

        dialog3 = new Dialog(this);
        dialog3.setContentView(R.layout.dialogbox);
        dialog3.setTitle("Verify...");
        button3=(Button)dialog3.findViewById(R.id.dialogbutton);
        editText3=(EditText)dialog3.findViewById(R.id.dialogedittext);

        alertDialogBuilder3 = new AlertDialog.Builder(this);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText3.getText().length() == 6) {
                    secretnumber3=sp.getString("tempuseritemid1","");
                    new CloseDonations().execute(secretnumber3, editText3.getText().toString());
                }
                else{
                    Snackbar.make(findViewById(R.id.TabView),"Check the code",Snackbar.LENGTH_LONG).show();
                }
            }
        });

        sp = getSharedPreferences("Care", MODE_PRIVATE);
        la = sp.getString("tempuserla", "");
        lo = sp.getString("tempuserlo", "");
        name1 = sp.getString("tempusername1", "");
        description1 = sp.getString("tempuserdescription1", "");
        item1 = sp.getString("tempuseritem1", "");
        number1 = sp.getString("tempusernumber1", "");
        itemid1 = sp.getString("tempuseritemid1", "");

        text1 = (TextView) findViewById(R.id.detailtext4);
        text2 = (TextView) findViewById(R.id.detailtext5);
        text3 = (TextView) findViewById(R.id.detailtext6);
        if (!number1.equals(sp.getString("number", ""))) {

            button = (Button) findViewById(R.id.detailsbutton);
            assert button != null;
            button.setEnabled(true);

            callbutton = (Button) findViewById(R.id.callbutton);
            assert callbutton != null;
//            callbutton.setEnabled(true);
            callbutton.setVisibility(View.GONE);
            callbutton.setEnabled(false);

            Verify = (Button) findViewById(R.id.verifybutton);
            assert Verify != null;
//            Verify.setEnabled(true);
            Verify.setVisibility(View.GONE);
            Verify.setEnabled(false);

            SharedPreferences.Editor edit=sp.edit();
            edit.putBoolean("acceptbutton",true);
            edit.apply();
        } else {
            button = (Button) findViewById(R.id.detailsbutton);
            assert button != null;
            button.setVisibility(View.GONE);
            button.setEnabled(false);

            callbutton = (Button) findViewById(R.id.callbutton);
            assert callbutton != null;
            callbutton.setVisibility(View.GONE);
            callbutton.setEnabled(false);

            Verify = (Button) findViewById(R.id.verifybutton);
            assert Verify != null;
            Verify.setVisibility(View.GONE);
            Verify.setEnabled(false);

            SharedPreferences.Editor edit=sp.edit();
            edit.putBoolean("acceptbutton",false);
            edit.apply();
        }

        map = (FloatingActionButton) findViewById(R.id.mapbutton);
        assert map != null;
        map.setClickable(false);
        map.setAlpha(.5f);

        button.setOnClickListener(this);
        callbutton.setOnClickListener(this);
        map.setOnClickListener(this);
        Verify.setOnClickListener(this);

        text1.setText(name1);
        text2.setText(description1);
        text3.setText(item1);

    }

    class confirm extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            StringBuilder sb = new StringBuilder();
            if (arg0[0].equals("accept")) {
                String link = "http://" + getString(R.string.website) + "/accept_donation", line0;
                try {
                    String data = "id=" + URLEncoder.encode(sp.getString("id", ""), "UTF-8") + "&number=" + URLEncoder.encode(sp.getString("number", ""), "UTF-8") + "&donationId=" + URLEncoder.encode(sp.getString("tempuseritemid1", ""), "UTF-8");
                    URL url = new URL(link);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                    writer.write(data);
                    writer.flush();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line0 = reader.readLine()) != null) {
                        sb.append(line0);
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    return "error";
                }
            }
            return arg0[0];
        }

        protected void onPostExecute(String result) {
            if (result.equals("path")) {//&&(result.substring(4,result.length()).equals("ok"))) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + la + "," + lo);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            } else if (result.equals("accept")) {//&&(result.substring(6,result.length()).equals("ok"))){
                Snackbar.make((findViewById(R.id.DetailsLayout)), "item Added", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make((findViewById(R.id.DetailsLayout)), "item not avaiable", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.onsetting, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(Details.this, AllNotifications.class));
                finish();
                break;
        }
        return false;
    }

    public void onBackPressed() {
        startActivity(new Intent(Details.this, AllNotifications.class));
        finish();
        super.onBackPressed();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detailsbutton:
                map.setAlpha((float) 1.0);

                map.setClickable(true);

                callbutton.setEnabled(true);
                callbutton.setVisibility(View.VISIBLE);

                Verify.setEnabled(true);
                Verify.setVisibility(View.GONE);

                new confirm().execute("accept");
                break;
            case R.id.mapbutton:
                new confirm().execute("path");
                break;
            case R.id.callbutton:
                if(sp.getBoolean("acceptbutton",false)) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    if(sp.getString("number","").length()>9) {
                        callIntent.setData(Uri.parse("tel:" + sp.getString("tempusernumber1", "")));
                    }
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(callIntent);
                    break;
                }
            case R.id.verifybutton:
                dialog3.show();
                break;
        }
    }

    class CloseDonations extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            progressDialog3.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            StringBuilder sb = new StringBuilder();
            Log.i("jebin",arg0[0]+arg0[1]);
            String link0 = "http://" + getString(R.string.website) + "/close_donation", line0;
            try {
                String data0 = "id=" + URLEncoder.encode(sp.getString("id", ""), "UTF-8")
                        + "&number=" + URLEncoder.encode(sp.getString("number", ""), "UTF-8")
                        + "&donationId=" + URLEncoder.encode(arg0[0],"UTF-8")
                        + "&code=" + URLEncoder.encode(arg0[1],"UTF-8");
                URL url = new URL(link0);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data0);
                writer.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line0 = reader.readLine()) != null) {
                    sb.append(line0);
                }
                conn.disconnect();
            } catch (Exception e) {
                return "error";
            }
            return sb.toString();
        }

        protected void onPostExecute(String result) {
            progressDialog3.dismiss();
            if(result.equals("ok")) {
                alertDialogBuilder3.setMessage("Verified");
                alertDialogBuilder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                alertDialogBuilder3.show();
            }
            else{
                alertDialogBuilder3.setMessage("Try Again");
                alertDialogBuilder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                alertDialogBuilder3.show();
            }
            dialog3.dismiss();
        }
    }
}
