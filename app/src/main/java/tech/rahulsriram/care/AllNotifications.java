package tech.rahulsriram.care;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jebin on 24-07-2016.
 */
public class AllNotifications extends AppCompatActivity implements View.OnClickListener {
    Button button1;
    TextView text;
    String username = "admin";
    String password = "admin";
    //String[] a={"hi1","hi2","hi3"};
    ListView listView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_notifications);
        text = (TextView)findViewById(R.id.textView);
        button1=(Button) findViewById(R.id.view);
        button1.setOnClickListener(this);
        listView=(ListView)findViewById(R.id.listView);
        ArrayList<String> lst = new ArrayList<String>();
        lst.add("hi4");
        ArrayAdapter <String> adapter = new ArrayAdapter <String>( this, android.R.layout.simple_list_item_1, lst);
        listView.setAdapter(adapter);
    }
    public void onClick(View v){
        new FetchData(AllNotifications.this,text,listView).execute(username, password);//id number
    }
}


