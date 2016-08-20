package tech.rahulsriram.care;

import android.app.Dialog;
import android.os.Bundle;
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
    Button button1;
    TextView text,text1;
    String username = "admin";
    String password = "admin";
    //String[] a={"hi1","hi2","hi3"};
    ListView listView;
    TabHost tabHost;
    GestureDetector gestureDetector;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabview);
        gestureDetector=new GestureDetector(this);
        text = (TextView)findViewById(R.id.textView);
        button1=(Button) findViewById(R.id.view);
        button1.setOnClickListener(this);
        listView=(ListView)findViewById(R.id.listView);

        tabHost=(TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
//        int a=tabHost.getCurrentTab();

        TabHost.TabSpec tabSpec;

        tabSpec=tabHost.newTabSpec("newsfeed");
        tabSpec.setContent(R.id.linearLayout);
        tabSpec.setIndicator("Completed Donations");
        tabHost.addTab(tabSpec);

        tabSpec=tabHost.newTabSpec("notification");
        tabSpec.setContent(R.id.linearLayout2);
        tabSpec.setIndicator("Open Donations");
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTab(0);

        ArrayList<String> lst = new ArrayList<String>();
        lst.add("hi4");
        ArrayAdapter <String> adapter = new ArrayAdapter <String>( this, android.R.layout.simple_list_item_1, lst);
        listView.setAdapter(adapter);
    }
    public boolean onTouchEvent(MotionEvent m){
//        float lastX = 0;
        return gestureDetector.onTouchEvent(m);
    }
    public void onClick(View v){
        new FetchData(AllNotifications.this,text,listView).execute(username, password);//id number
    }
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
        text1=(TextView)dialog.findViewById(R.id.textView2);
        dialog.setContentView(R.layout.dialogbox);
        dialog.show();
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

