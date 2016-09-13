package tech.rahulsriram.care;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.EditText;
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

    static RecyclerView.Adapter adapter2;
    RecyclerView.LayoutManager layoutManager2;
    static RecyclerView recyclerView2;
    static ArrayList<DataModel> data2;

    static RecyclerView.Adapter adapter3;
    RecyclerView.LayoutManager layoutManager3;
    static RecyclerView recyclerView3;
    static ArrayList<DataModel> data3;

    ArrayList<String> name0 = new ArrayList<>();
    ArrayList<String> description0 = new ArrayList<>();
    ArrayList<String> item0 = new ArrayList<>();
    ArrayList<String> number0 = new ArrayList<>();
    ArrayList<String> latitude0 = new ArrayList<>();
    ArrayList<String> longitude0 = new ArrayList<>();
    ArrayList<String> itemid0 = new ArrayList<>();


    ArrayList<String> name1 = new ArrayList<>();
    ArrayList<String> description1 = new ArrayList<>();
    ArrayList<String> item1 = new ArrayList<>();
    ArrayList<String> number1 = new ArrayList<>();
    ArrayList<String> latitude1 = new ArrayList<>();
    ArrayList<String> longitude1 = new ArrayList<>();
    ArrayList<String> itemid1 = new ArrayList<>();

    ArrayList<String> name2 = new ArrayList<>();
    ArrayList<String> description2 = new ArrayList<>();
    ArrayList<String> item2 = new ArrayList<>();
    ArrayList<String> number2 = new ArrayList<>();
    ArrayList<String> latitude2 = new ArrayList<>();
    ArrayList<String> longitude2 = new ArrayList<>();
    ArrayList<String> itemid2 = new ArrayList<>();

    ArrayList<String> name3 = new ArrayList<>();
    ArrayList<String> description3 = new ArrayList<>();
    ArrayList<String> item3 = new ArrayList<>();
    ArrayList<String> number3 = new ArrayList<>();
    ArrayList<String> itemid3 = new ArrayList<>();

    TabHost tabHost;
    GestureDetector gestureDetector;

    SwipeRefreshLayout swipeRefreshLayout;

    SharedPreferences sp;

    String la,lo;

    String secretnumber3;

    ProgressDialog progressDialog3;

    Dialog dialog3;

    AlertDialog.Builder alertDialogBuilder3;

    EditText editText3;

    Button button3;

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

        recyclerView2 = (RecyclerView) findViewById(R.id.my_recycler_view2);
        recyclerView2.setHasFixedSize(true);
        layoutManager2 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());

        recyclerView3 = (RecyclerView) findViewById(R.id.my_recycler_view3);
        recyclerView3.setHasFixedSize(true);
        layoutManager3 = new LinearLayoutManager(this);
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView3.setItemAnimator(new DefaultItemAnimator());

        myOnClickListener = new MyOnClickListener();

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
                if(editText3.getText().length()==10){
                    progressDialog3.show();
                    if(editText3.getText().toString()==secretnumber3){
                        progressDialog3.dismiss();
                        alertDialogBuilder3.setMessage("Verified");
                        alertDialogBuilder3.show();
                        alertDialogBuilder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                    }
                    else{
                        progressDialog3.dismiss();
                        alertDialogBuilder3.setMessage("wrong pass");
                        alertDialogBuilder3.show();
                        alertDialogBuilder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                    }
                }
            }
        });

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

        tabSpec = tabHost.newTabSpec("accepteddonations");
        tabSpec.setContent(R.id.linearLayout2);
        tabSpec.setIndicator("Accepted Donations");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("mydonations");
        tabSpec.setContent(R.id.linearLayout3);
        tabSpec.setIndicator("My Donations");
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

        FloatingActionButton donate2 = (FloatingActionButton) findViewById(R.id.donateButton2);
        assert donate2 != null;
        donate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllNotifications.this, ItemSelectionActivity.class));
            }
        });

        FloatingActionButton donate3 = (FloatingActionButton) findViewById(R.id.donateButton3);
        assert donate3 != null;
        donate3.setOnClickListener(new View.OnClickListener() {
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
                else if(tabHost.getCurrentTab()==1){
                    new OpenDonations().execute();
                    swipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    },3000);
                }
                else if(tabHost.getCurrentTab()==2){
                    new AcceptedDonations().execute();
                    swipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    },3000);
                }
                else{
                    new MyDonations().execute();
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
                        editor.putString("tempuseritemid1",itemid1.get(i));
                        editor.apply();
                        break;
                    }
                }
                startActivity(new Intent(AllNotifications.this,Details.class));
                onResume();
            }
            else if(tabHost.getCurrentTab()==3){
                int selectedItemPosition3 = recyclerView3.getChildPosition(v);
                RecyclerView.ViewHolder viewHolder3 = recyclerView3.findViewHolderForPosition(selectedItemPosition3);
                TextView textDescription3 = (TextView) viewHolder3.itemView.findViewById(R.id.textDescription);
                String selectedName3 = (String) textDescription3.getText();
                for(int i=0;i<description3.size();i++) {
                    if (selectedName3.equals(description3.get(i))) {
                        secretnumber3=itemid3.get(i);
                        break;
                    }
                }
                dialog3.show();
                if(editText3.getText().length()==10){
                    progressDialog3.show();
                    if(editText3.getText().toString()==secretnumber3){
                        progressDialog3.dismiss();
                        alertDialogBuilder3.setMessage("Verified");
                        alertDialogBuilder3.show();
                        alertDialogBuilder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                    }
                    else{
                        progressDialog3.dismiss();
                        alertDialogBuilder3.setMessage("wrong pass");
                        alertDialogBuilder3.show();
                        alertDialogBuilder3.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                    }
                }
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


    class ClosedDonations extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            number0.clear();
            name0.clear();
            latitude0.clear();
            longitude0.clear();
            item0.clear();
            description0.clear();
            itemid0.clear();
        }

        @Override
        protected String doInBackground(String... arg0) {
            StringBuilder sb = new StringBuilder();
            String link0 = "http://" + getString(R.string.website) + "/recent_history", line0;
            try {
                String data0 = "id=" + URLEncoder.encode(sp.getString("id", ""), "UTF-8") + "&number=" + URLEncoder.encode(sp.getString("number", ""), "UTF-8") + "&location=" + URLEncoder.encode(sp.getString("location", ""), "UTF-8") + "&radius=" + URLEncoder.encode(sp.getString("radius", ""), "UTF-8") + "&status=closed";
                URL url = new URL(link0);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data0);
                writer.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line0 = reader.readLine()) != null) {
                    String[] line00=line0.split(",");
                    sb.append(line0);
                    number0.add(line00[0]);
                    name0.add(line00[1]);
                    latitude0.add(line00[2]);
                    longitude0.add(line00[3]);
                    item0.add(itemNameParser(line00[4]));
                    description0.add(line00[5]);
                    itemid0.add(line00[6]);
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
                    data0.add(new DataModel(number0.get(j), name0.get(j), latitude0.get(j), longitude0.get(j), item0.get(j), description0.get(j), itemid0.get(j)));
                }

                adapter0 = new CustomAdapter(data0);
                recyclerView0.setAdapter(adapter0);
            }
        }
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
            String link1 = "http://" + getString(R.string.website) + "/recent_history", line1;
            try {
                String data1 = "id=" + URLEncoder.encode(sp.getString("id", ""), "UTF-8") + "&number=" + URLEncoder.encode(sp.getString("number", ""), "UTF-8") + "&location=" + URLEncoder.encode(sp.getString("location", ""), "UTF-8") + "&radius=" + URLEncoder.encode(sp.getString("radius", ""), "UTF-8") + "&status=open";
                URL url = new URL(link1);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data1);
                writer.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line1 = reader.readLine()) != null) {
                    if(line1.equals("ok")) {
                        String[] lineString = line1.split(",");
                        number1.add(lineString[0]);
                        name1.add(lineString[1]);
                        latitude1.add(lineString[2]);
                        longitude1.add(lineString[3]);
                        item1.add(itemNameParser(lineString[4]));
                        description1.add(lineString[5]);
                        itemid1.add(lineString[6]);
                    } else {
                        return line1;
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
                    data1.add(new DataModel(number1.get(j), name1.get(j), latitude1.get(j), longitude1.get(j), item1.get(j), description1.get(j), itemid1.get(j)));
                }

                adapter1 = new CustomAdapter(data1);
                recyclerView1.setAdapter(adapter1);
            }
        }
    }

    class AcceptedDonations extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            number2.clear();
            name2.clear();
            latitude2.clear();
            longitude2.clear();
            item2.clear();
            description2.clear();
        }

        @Override
        protected String doInBackground(String... arg0) {
            String link2 = "http://" + getString(R.string.website) + "/recent_history", line2;
            try {
                String data2 = "id=" + URLEncoder.encode(sp.getString("id", ""), "UTF-8") + "&number=" + URLEncoder.encode(sp.getString("number", ""), "UTF-8") + "&location=" + URLEncoder.encode(sp.getString("location", ""), "UTF-8") + "&radius=" + URLEncoder.encode(sp.getString("radius", ""), "UTF-8") + "&status=open";
                URL url = new URL(link2);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data2);
                writer.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line2 = reader.readLine()) != null) {
                    if(line2.equals("ok")) {
                        String[] lineString = line2.split(",");
                        number2.add(lineString[0]);
                        name2.add(lineString[1]);
                        latitude2.add(lineString[2]);
                        longitude2.add(lineString[3]);
                        item2.add(itemNameParser(lineString[4]));
                        description2.add(lineString[5]);
                        itemid2.add(lineString[6]);
                    } else {
                        return line2;
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
                data2 = new ArrayList<>();
                for (int j = 0; j < name2.size(); j++) {
                    data2.add(new DataModel(number2.get(j), name2.get(j), latitude2.get(j), longitude2.get(j), item2.get(j), description2.get(j), itemid2.get(j)));
                }

                adapter2 = new CustomAdapter(data2);
                recyclerView2.setAdapter(adapter2);
            }
        }
    }

    class MyDonations extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            StringBuilder sb = new StringBuilder();
            String link0 = "http://" + getString(R.string.website) + "/my_donations", line0;
            try {
                String data0 = "id=" + URLEncoder.encode(sp.getString("id", ""), "UTF-8") + "&number=" + URLEncoder.encode(sp.getString("number", ""), "UTF-8");
                URL url = new URL(link0);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data0);
                writer.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line0 = reader.readLine()) != null) {
                    String[] line00=line0.split(",");
                    sb.append(line0);
                    name3.add(line00[1]);
                    item3.add(itemNameParser(line00[4]));
                    description3.add(line00[5]);
                    itemid3.add(line00[6]);
                }
                conn.disconnect();
            } catch (Exception e) {
                return "error";
            }
            return sb.toString();
        }

        protected void onPostExecute(String result) {
            if(!result.equals("error")) {
                data3 = new ArrayList<>();
                for (int j = 0; j < name3.size(); j++) {
                    data3.add(new DataModel("", name3.get(j), "", "", item3.get(j), description3.get(j), itemid3.get(j)));
                }

                adapter3 = new CustomAdapter(data3);
                recyclerView3.setAdapter(adapter3);
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

