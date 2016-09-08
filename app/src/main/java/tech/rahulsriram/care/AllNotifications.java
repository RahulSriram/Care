package tech.rahulsriram.care;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.TabHost;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AllNotifications extends AppCompatActivity implements GestureDetector.OnGestureListener {
    public static View.OnClickListener myOnClickListener;

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

    SwipeRefreshLayout swipeRefreshLayout;

    SharedPreferences sp;

    String la,lo;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabview);

        sp = getSharedPreferences("Care", MODE_PRIVATE);

        gestureDetector = new GestureDetector(this);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);

        recyclerView0 = (RecyclerView) findViewById(R.id.my_recycler_view0);
        recyclerView0.setHasFixedSize(true);
        layoutManager0 = new LinearLayoutManager(this);
        recyclerView0.setLayoutManager(layoutManager0);
        recyclerView0.setItemAnimator(new DefaultItemAnimator());

        recyclerView1 = (RecyclerView) findViewById(R.id.my_recycler_view1);
        recyclerView1.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());

        myOnClickListener = new MyOnClickListener();

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
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorAccent));

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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(tabHost.getCurrentTab()==0){
                    new ClosedDonations().execute();
                    swipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    },3000);
                }
                else{
                    new OpenDonations().execute();
                    swipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    },3000);
                }
            }
        });
    }

    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if((tabHost.getCurrentTab()==1)) {
                int selectedItemPosition1 = recyclerView1.getChildPosition(v);
                RecyclerView.ViewHolder viewHolder1 = recyclerView1.findViewHolderForPosition(selectedItemPosition1);
                TextView textDescription1 = (TextView) viewHolder1.itemView.findViewById(R.id.textDescription);
                String selectedName1 = (String) textDescription1.getText();
                for(int i=0;i<description1.size();i++) {
                    if (selectedName1.equals(description1.get(i))) {
                        la = latitude1.get(i);
                        lo = longitude1.get(i);
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putString("tempuserla",la);
                        editor.putString("tempuserlo",lo);
                        editor.putString("tempusername1",name1.get(i));
                        editor.putString("tempuserdescription1",description1.get(i));
                        editor.putString("tempuseritem1",item1.get(i));
                        editor.putString("tempusernumber1",number1.get(i));
                        editor.apply();
                        break;
                    }
                }
                startActivity(new Intent(AllNotifications.this,Details.class));
                onResume();
            }

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.setting, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(AllNotifications.this, SettingsActivity.class));
        return false;
    }

    public boolean onTouchEvent(MotionEvent m) {
        return gestureDetector.onTouchEvent(m);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //  Toast.makeText(this,"down swipe",Toast.LENGTH_LONG).show();
        return false;
    }

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
            String link = "http://" + getString(R.string.website) + "/recent_history", line;
            try {
                String data = "id=" + URLEncoder.encode(sp.getString("id", ""), "UTF-8") + "&number=" + URLEncoder.encode(sp.getString("number", ""), "UTF-8") + "&location=" + URLEncoder.encode(sp.getString("location", ""), "UTF-8") + "&radius=" + URLEncoder.encode(sp.getString("radius", ""), "UTF-8") + "&status=open";
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data);
                writer.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    if(line.equals("ok")) {
                        String[] lineString = line.split(",");
                        number1.add(lineString[0]);
                        name1.add(lineString[1]);
                        latitude1.add(lineString[2]);
                        longitude1.add(lineString[3]);
                        item1.add(itemNameParser(lineString[4]));
                        description1.add(lineString[5]);
                    } else {
                        return line;
                    }
                }
                conn.disconnect();
            } catch (Exception e) {
                return "error";
            }

            return "ok";
        }

        protected void onPostExecute(String result) {
            if(!result.equals("error")) {
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
            String link = "http://" + getString(R.string.website) + "/recent_history", line0;
            try {
                String data = "id=" + URLEncoder.encode(sp.getString("id", ""), "UTF-8") + "&number=" + URLEncoder.encode(sp.getString("number", ""), "UTF-8") + "&location=" + URLEncoder.encode(sp.getString("location", ""), "UTF-8") + "&radius=" + URLEncoder.encode(sp.getString("radius", ""), "UTF-8") + "&status=closed";
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
                    item0.add(itemNameParser(line00[4]));
                    description0.add(line00[5]);
                }
                conn.disconnect();
            } catch (Exception e) {
                return "error";
            }
            return sb.toString();
        }

        protected void onPostExecute(String result) {
            if(!result.equals("error")) {
                data0 = new ArrayList<>();
                for (int j = 0; j < name0.size(); j++) {
                    data0.add(new DataModel(number0.get(j), name0.get(j), latitude0.get(j), longitude0.get(j), item0.get(j), description0.get(j)));
                }

                adapter0 = new CustomAdapter(data0);
                recyclerView0.setAdapter(adapter0);
            }
        }
    }

    private String itemNameParser(String items) {
        String reply = "error";

        if (Integer.parseInt(items) >= 0) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < 4; i++) {
                switch (items.charAt(i)) {
                    case '0':
                        sb.append("Home Made Food, ");
                        break;

                    case '1':
                        sb.append("Packed Food, ");
                        break;

                    case '2':
                        sb.append("Clothes, ");
                        break;

                    case '3':
                        sb.append("Books, ");
                        break;
                }
            }

            reply = sb.substring(0, sb.length() - 2);
        }

        return reply;
    }
}

