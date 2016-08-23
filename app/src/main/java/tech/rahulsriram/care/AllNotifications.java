package tech.rahulsriram.care;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jebin on 24-07-2016.
 */
public class AllNotifications extends AppCompatActivity implements View.OnClickListener,GestureDetector.OnGestureListener {
    public static View.OnClickListener myOnClickListener;
    Button button0, button1;
    String device_id,Jebin="careLog";

    static RecyclerView.Adapter adapter0;
    RecyclerView.LayoutManager layoutManager0;
    static RecyclerView recyclerView0;
    static ArrayList<DataModel> data0;

    static RecyclerView.Adapter adapter1;
    RecyclerView.LayoutManager layoutManager1;
    static RecyclerView recyclerView1;
    static ArrayList<DataModel> data1;

    ArrayList<String> name0 = new ArrayList<>();
    ArrayList<String> description0 = new ArrayList<>();
    ArrayList<String> item0 = new ArrayList<>();

    ArrayList<String> name1 = new ArrayList<>();
    ArrayList<String> description1 = new ArrayList<>();
    ArrayList<String> item1 = new ArrayList<>();

    TabHost tabHost;
    GestureDetector gestureDetector;

    SharedPreferences sp;
    String location1;
    String[] location2;

    String[] separated10[]=new String[20][20];
    String[] separated0;

    String[] separated11[]=new String[20][20];
    String[] separated1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabview);

        sp = getSharedPreferences("Care", MODE_PRIVATE);
        location1=sp.getString("location", "");
        location2=location1.split(",");

        gestureDetector = new GestureDetector(this);

        button0 = (Button) findViewById(R.id.button0);
        button0.setOnClickListener(this);
        recyclerView0 = (RecyclerView) findViewById(R.id.my_recycler_view0);
        recyclerView0.setHasFixedSize(true);
        layoutManager0 = new LinearLayoutManager(this);
        recyclerView0.setLayoutManager(layoutManager0);
        recyclerView0.setItemAnimator(new DefaultItemAnimator());

        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        recyclerView1 = (RecyclerView) findViewById(R.id.my_recycler_view1);
        recyclerView1.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        myOnClickListener = new MyOnClickListener(this);

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("newsfeed");
        tabSpec.setContent(R.id.linearLayout0);
        tabSpec.setIndicator("Completed Donations");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("notification");
        tabSpec.setContent(R.id.linearLayout1);
        tabSpec.setIndicator("Open Donations");
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTab(0);

        FloatingActionButton donate0 = (FloatingActionButton) findViewById(R.id.donateButton0);
        assert donate0 != null;
        donate0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllNotifications.this, ItemSelectionActivity.class));
            }
        });

        FloatingActionButton donate1 = (FloatingActionButton) findViewById(R.id.donateButton1);
        assert donate1 != null;
        donate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllNotifications.this, ItemSelectionActivity.class));
            }
        });
    }
    class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Log.i(Jebin,"mapp");
            Uri gmmIntentUri = Uri.parse("google.navigation:q="+String.valueOf(separated1[2])+","+String.valueOf(separated1[3]));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);

        }
    }
    //TODO: end of onCreate

    //TODO:Onbutton click
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button0:
                new FetchDataa().execute();
                break;
            case R.id.button1:
                new FetchData().execute();
                break;

        }
    }

    //TODO: Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.setting, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                settings();
                return true;
            default:
                return false;
        }
    }

    public void settings() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialogbox);
        dialog.show();
    }

    //TODO: Gesture
    public boolean onTouchEvent(MotionEvent m) {
//        float lastX = 0;
        return gestureDetector.onTouchEvent(m);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //  Toast.makeText(this,"down swipe",Toast.LENGTH_LONG).show();
        return false;
    }
    // public boolean onLeft  (MotionEvent e) {
    //   Toast.makeText(this,"down swipe",Toast.LENGTH_LONG).show();
    // return false;
    //}

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        Toast.makeText(this,String.valueOf(distanceX),Toast.LENGTH_LONG).show();


        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


    class FetchData extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            StringBuilder sb = new StringBuilder();
            sb.append("aaa");
            String link = "http://10.0.0.20:8000/donation";
            String data = "id=" + sp.getString("id","") + "&number=" + sp.getString("number","")+"&location="+sp.getString("location","")+"&radius"+sp.getString("radius","")+"&status+"+sp.getString("status","");//TODO:number,name,latitude,longitude,item,description
            try {
                Log.i(Jebin, "doInBackground");
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10);
                conn.setReadTimeout(10);
                conn.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data);
                writer.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((data = reader.readLine()) != null) {
                    Log.i(Jebin, "while");
                    String[] data1=data.split(",");
                    sb.append(data);
                    name1.add(data1[1]);
                    item1.add(data1[4]);
                    description1.add(data1[5]);
                }
                conn.disconnect();
            } catch (Exception e) {
                return "error";
            }
            return sb.toString();
        }

        protected void onPostExecute(String result) {
            data1=new ArrayList<>();
            for(int j = 0; j<2;j++)
            {
                Log.i(Jebin, "datamodel");
                data1.add(new DataModel(name1.get(j), item1.get(j), description1.get(j)));
            }

            adapter1=new CustomAdapter(data1);
            recyclerView1.setAdapter(adapter1);
            Log.i(Jebin,"call to adapter");
        }
    }
    class FetchDataa extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            StringBuilder sb = new StringBuilder();
            String link = "http://10.0.0.20:8000/recent_history",line;
            String data = "id=" + sp.getString("id","") + "&number=" + sp.getString("number","")+"&location="+sp.getString("location","")+"&radius"+sp.getString("radius","")+"&status+"+sp.getString("status","");
            try {
                Log.i(Jebin, "doInBackground");
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10);
                conn.setReadTimeout(10);
                conn.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data);
                writer.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    Log.i(Jebin, "while");
                    String[] line0=data.split(",");
                    sb.append(line);
                    name0.add(line0[1]);
                    item0.add(line0[4]);
                    description0.add(line0[5]);
                }
                conn.disconnect();
            } catch (Exception e) {
                return "error";
            }
            return sb.toString();
        }

        protected void onPostExecute(String result) {
            data0=new ArrayList<>();
            for(int j = 0; j<2;j++)
            {
                Log.i(Jebin, "datamodel");
                data0.add(new DataModel(name0.get(j), item0.get(j), description0.get(j)));
            }

            adapter0=new CustomAdapter(data0);
            recyclerView0.setAdapter(adapter0);
            Log.i(Jebin,"call to adapter");
        }
    }
}

