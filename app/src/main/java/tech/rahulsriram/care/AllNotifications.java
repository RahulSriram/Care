package tech.rahulsriram.care;

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
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

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
    ArrayList<String> number0 = new ArrayList<>();
    ArrayList<String> latitude0 = new ArrayList<>();
    ArrayList<String> longitude0 = new ArrayList<>();

    ArrayList<String> name1 = new ArrayList<>();
    ArrayList<String> description1 = new ArrayList<>();
    ArrayList<String> item1 = new ArrayList<>();
    ArrayList<String> number1 = new ArrayList<>();
    ArrayList<String> latitude1 = new ArrayList<>();
    ArrayList<String> longitude1 = new ArrayList<>();

    TabHost tabHost;
    GestureDetector gestureDetector;

    SharedPreferences sp;

    String la,lo;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabview);

        sp = getSharedPreferences("Care", MODE_PRIVATE);

        gestureDetector = new GestureDetector(this);


        name0.add("name0");
        description0.add("descritpion0");
        item0.add("item0");
        number0.add("number0");
        latitude0.add("46.2276");
        longitude0.add("2.2137");
        name0.add("name0");
        description0.add("descritpion0");
        item0.add("item0");
        number0.add("number0");
        latitude0.add("46.2276");
        longitude0.add("2.2137");
        name0.add("name0");
        description0.add("descritpion0");
        item0.add("item0");
        number0.add("number0");
        latitude0.add("46.2276");
        longitude0.add("2.2137");
        name0.add("name0");
        description0.add("descritpion0");
        item0.add("item0");
        number0.add("number0");
        latitude0.add("46.2276");
        longitude0.add("2.2137");
        name0.add("name0");
        description0.add("descritpion0");
        item0.add("item0");
        number0.add("number0");
        latitude0.add("46.2276");
        longitude0.add("2.2137");
        name0.add("name0");
        description0.add("descritpion0");
        item0.add("item0");
        number0.add("number0");
        latitude0.add("46.2276");
        longitude0.add("2.2137");

        name1.add("name1");
        description1.add("descritpion1");
        item1.add("item1");
        number1.add("number1");
        latitude1.add("46.2276");
        longitude1.add("2.2137");
        name1.add("name1");
        description1.add("descritpion1");
        item1.add("item1");
        number1.add("number1");
        latitude1.add("46.2276");
        longitude1.add("2.2137");
        name1.add("name1");
        description1.add("descritpion1");
        item1.add("item1");
        number1.add("number1");
        latitude1.add("46.2276");
        longitude1.add("2.2137");
        name1.add("name1");
        description1.add("descritpion1");
        item1.add("item1");
        number1.add("number1");
        latitude1.add("46.2276");
        longitude1.add("2.2137");
        name1.add("name1");
        description1.add("descritpion1");
        item1.add("item1");
        number1.add("number1");
        latitude1.add("46.2276");
        longitude1.add("2.2137");
        name1.add("name1");
        description1.add("descritpion1");
        item1.add("item1");
        number1.add("number1");
        latitude1.add("46.2276");
        longitude1.add("2.2137");



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
            if((tabHost.getCurrentTab()==1)) {
                int a11=tabHost.getCurrentTab();
                int selectedItemPosition1 = recyclerView1.getChildPosition(v);
                RecyclerView.ViewHolder viewHolder1
                        = recyclerView1.findViewHolderForPosition(selectedItemPosition1);
                TextView textViewIcon1 = (TextView) viewHolder1.itemView.findViewById(R.id.textViewIcon);
                String selectedName1 = (String) textViewIcon1.getText();
                for(int i=0;i<description1.size();i++) {
                    if (selectedName1.equals(description1.get(i))) {
                        la = latitude1.get(i);
                        lo = longitude1.get(i);
                    }
                }
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+la+","+lo);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }

        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button0:
                new ClosedDonations().execute();
                break;
            case R.id.button1:
                new OpenDonations().execute();
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
        startActivity(new Intent(AllNotifications.this, SettingsActivity.class));
                return false;
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


    class OpenDonations extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            number1.clear();
            name1.clear();
            latitude1.clear();
            longitude1.clear();
            item1.clear();
            description1.clear();
        }

        @Override
        protected String doInBackground(String... arg0) {
            StringBuilder sb = new StringBuilder();
            String link = "http://10.0.0.20:8000/recent_history",line1;
            String data = "id=" + sp.getString("id","") + "&number=" + sp.getString("number","")+"&location="+sp.getString("location","")+"&radius="+sp.getString("radius","")+"&status=+open";//TODO:number,name,latitude,longitude,item,description
            try {
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10);
                conn.setReadTimeout(10);
                conn.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data);
                writer.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line1 = reader.readLine()) != null) {
                    String[] line11=line1.split(",");
                    sb.append(data);
                    number1.add(line11[0]);
                    name1.add(line11[1]);
                    latitude1.add(line11[2]);
                    longitude1.add(line11[3]);
                    item1.add(line11[4]);
                    description1.add(line11[5]);
                }
                conn.disconnect();
            } catch (Exception e) {
                return "error";
            }
            return sb.toString();
        }

        protected void onPostExecute(String result) {
            if(result!="error") {
                data1 = new ArrayList<>();
                for (int j = 0; j < name1.size(); j++) {
                    data1.add(new DataModel(number1.get(j), name1.get(j), latitude1.get(j), longitude1.get(j), item1.get(j), description1.get(j)));
                }

                adapter1 = new CustomAdapter(data1);
                recyclerView1.setAdapter(adapter1);
            }
        }
    }
    class ClosedDonations extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            number0.clear();
            name0.clear();
            latitude0.clear();
            longitude0.clear();
            item0.clear();
            description0.clear();
        }

        @Override
        protected String doInBackground(String... arg0) {
            StringBuilder sb = new StringBuilder();
            String link = "http://10.0.0.20:8000/recent_history",line0;
            String data = "id=" + sp.getString("id","") + "&number=" + sp.getString("number","")+"&location="+sp.getString("location","")+"&radius="+sp.getString("radius","")+"&status=closed";
            try {
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10);
                conn.setReadTimeout(10);
                conn.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data);
                writer.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line0 = reader.readLine()) != null) {
                    String[] line00=data.split(",");
                    sb.append(line0);
                    number0.add(line00[0]);
                    name0.add(line00[1]);
                    latitude0.add(line00[2]);
                    longitude0.add(line00[3]);
                    item0.add(line00[4]);
                    description0.add(line00[5]);
                }
                conn.disconnect();
            } catch (Exception e) {
                return "error";
            }
            return sb.toString();
        }

        protected void onPostExecute(String result) {
            if(result!="error") {
                data0 = new ArrayList<>();
                for (int j = 0; j < name0.size(); j++) {
                    data0.add(new DataModel(number0.get(j), name0.get(j), latitude0.get(j), longitude0.get(j), item0.get(j), description0.get(j)));
                }

                adapter0 = new CustomAdapter(data0);
                recyclerView0.setAdapter(adapter0);
            }
        }
    }
}

