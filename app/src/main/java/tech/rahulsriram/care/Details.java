package tech.rahulsriram.care;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by jebineinstein on 29/8/16.
 */
public class Details extends AppCompatActivity {

    SharedPreferences sp;

    String la,lo,name1,item1,description1,number1,itemid1;

    int tempvalue;

    TextView text1,text2,text3,text4,text5;

    Button button;
    FloatingActionButton map;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        sp = getSharedPreferences("Care", MODE_PRIVATE);
        la = sp.getString("tempuserla", "");
        lo = sp.getString("tempuserlo", "");
        name1 = sp.getString("tempusername1", "");
        description1 = sp.getString("tempuserdescription1", "");
        item1 = sp.getString("tempuseritem1", "");
        number1 = sp.getString("tempusernumber1", "");
        itemid1 = sp.getString("tempuseritemid1", "");

        text1 = (TextView) findViewById(R.id.detailtext1);
        text2 = (TextView) findViewById(R.id.detailtext2);
        text3 = (TextView) findViewById(R.id.detailtext3);

        button = (Button) findViewById(R.id.detailsbutton);
        assert button != null;

        map = (FloatingActionButton) findViewById(R.id.mapbutton);
        assert map != null;
        map.setClickable(false);
        map.setAlpha(.5f);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.setAlpha((float)1.0);
                map.setClickable(true);
                new confirm().execute("accept");
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new confirm().execute("path");
            }
        });

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
            if(arg0[0]=="accept") {
                String link = "http://" + getString(R.string.website) + "/accept_donation", line0;
                try {
                    String data = "id=" + URLEncoder.encode(sp.getString("id", ""), "UTF-8") + "&number=" + URLEncoder.encode(sp.getString("number", ""), "UTF-8") + "&donarnumber=" + URLEncoder.encode(sp.getString("tempusernumber1", ""), "UTF-8") + "&donationId=" + URLEncoder.encode(sp.getString("tempuseritemid1", ""), "UTF-8");
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
            return arg0[0]+sb.toString();
        }

        protected void onPostExecute(String result) {
            if((result.substring(0,3)=="path")&&(result.substring(4,result.length())=="ok")) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+la+","+lo);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
            else if((result.substring(0,5)=="accept")&&(result.substring(6,result.length())=="ok")){
                Snackbar.make((findViewById(R.id.DetailsLayout)),"item Added",Snackbar.LENGTH_LONG).show();
            }
            else{
                Snackbar.make((findViewById(R.id.DetailsLayout)),"item not avaiable",Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
