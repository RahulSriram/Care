package tech.rahulsriram.care;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jebin on 24-07-2016.
 */
public class AllNotifications extends AppCompatActivity implements View.OnClickListener,GestureDetector.OnGestureListener {
    Button button0,button1;
    TextView text0,text1;
    ListView listView0,listView1;
    String item,description;
    String device_id,number;

    TabHost tabHost;
    GestureDetector gestureDetector;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabview);
        gestureDetector=new GestureDetector(this);

        button0=(Button) findViewById(R.id.button0);
        button0.setOnClickListener(this);
        listView0=(ListView)findViewById(R.id.listView0);

        button1=(Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        text1 = (TextView)findViewById(R.id.textView1);
        listView1=(ListView)findViewById(R.id.listView1);

        tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec=tabHost.newTabSpec("newsfeed");
        tabSpec.setContent(R.id.linearLayout0);
        tabSpec.setIndicator("Completed Donations");
        tabHost.addTab(tabSpec);

        tabSpec=tabHost.newTabSpec("notification");
        tabSpec.setContent(R.id.linearLayout1);
        tabSpec.setIndicator("Open Donations");
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTab(0);
        //TODO:NewsFeed
        ArrayList<String> lst0 = new ArrayList<String>();
        lst0.add("hi4");
        ArrayAdapter <String> adapter0 = new ArrayAdapter <String>( this, android.R.layout.simple_list_item_1, lst0);
        listView0.setAdapter(adapter0);

        FloatingActionButton donate0 = (FloatingActionButton) findViewById(R.id.donateButton0);
        assert donate0 != null;
        donate0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllNotifications.this, ItemSelectionActivity.class));
            }
        });
        //TODO:Open Donations
        ArrayList<String> lst1 = new ArrayList<String>();
        lst1.add("hi4");
        ArrayAdapter <String> adapter1 = new ArrayAdapter <String>( this, android.R.layout.simple_list_item_1, lst1);
        listView1.setAdapter(adapter1);

        FloatingActionButton donate1 = (FloatingActionButton) findViewById(R.id.donateButton1);
        assert donate1 != null;
        donate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllNotifications.this, ItemSelectionActivity.class));
            }
        });
    }
    //TODO: end of onCreate

    //TODO:Onbutton click
    public void onClick(View v){
        switch(v.getId()){
            case R.id.button0:
                new FetchDataa(AllNotifications.this,listView0).execute(item, description);//id number
                break;
            case R.id.button1:
                new FetchData(AllNotifications.this,text1,listView1).execute(device_id,number);//id number
                break;

        }
    }
    //TODO: Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.setting,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.settings:
                settings();
                return true;
            default:
                return false;
        }
    }
    public void settings(){
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialogbox);
        dialog.show();
    }
    //TODO: Gesture
    public boolean onTouchEvent(MotionEvent m){
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
}

