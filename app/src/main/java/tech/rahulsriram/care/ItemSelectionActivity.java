package tech.rahulsriram.care;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by SREEVATHSA on 15-08-2016.
 */
public class ItemSelectionActivity  extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    ArrayList<String> selection = new ArrayList<>();
    String description, verify="", final_Donate;
    EditText itemDescription;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selection);

        sp = getSharedPreferences("Care", MODE_PRIVATE);

        itemDescription = (EditText)findViewById(R.id.itemDescription);
        Switch homeFoodSwitch = (Switch) findViewById(R.id.homeFoodSwitch);
        assert homeFoodSwitch != null;
        homeFoodSwitch.setOnCheckedChangeListener(this);

        Switch packedFoodSwitch = (Switch) findViewById(R.id.packedFoodSwitch);
        assert packedFoodSwitch != null;
        packedFoodSwitch.setOnCheckedChangeListener(this);

        Switch clothesSwitch = (Switch) findViewById(R.id.clothesSwitch);
        assert clothesSwitch != null;
        clothesSwitch.setOnCheckedChangeListener(this);

        Switch bookSwitch = (Switch) findViewById(R.id.bookSwitch);
        assert bookSwitch != null;
        bookSwitch.setOnCheckedChangeListener(this);

        FloatingActionButton donate = (FloatingActionButton) findViewById(R.id.donateButton);
        assert donate != null;
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String selections : selection) {   //your was String selection : selection two same variable i just changed it
                    final_Donate += selections;
                }
                description = itemDescription.getText().toString();
                 if(description.length()==0)
                 {
                     Snackbar snackbar = Snackbar
                             .make(v, "Please enter description", Snackbar.LENGTH_LONG);
                     snackbar.show();
                 }
                else{
                     new Donate(ItemSelectionActivity.this).execute(sp.getString("id", ""),sp.getString("number", ""),sp.getString("location", ""),description);
                     if(verify.equals("ok")){
                         Snackbar snackbar = Snackbar
                                 .make(v, "Done", Snackbar.LENGTH_LONG);
                         snackbar.show();
                     }
                     else{
                         Snackbar snackbar = Snackbar
                                 .make(v, "Please TRY AGAIN", Snackbar.LENGTH_LONG);
                         snackbar.show();
                     }
                 }

                Toast.makeText(getApplicationContext(), final_Donate, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            switch (buttonView.getText().toString()) {
                case "Home Made Food":
                    selection.add("0");
                    break;

                case "Packed Food":
                    selection.add("1");
                    break;

                case "Clothes":
                    selection.add("2");
                    break;

                case "Books":
                    selection.add("3");
                    break;
            }
        } else {
            switch (buttonView.getText().toString()) {
                case "Home Made Food":
                    selection.remove("0");
                    break;

                case "Packed Food":
                    selection.remove("1");
                    break;

                case "Clothes":
                    selection.remove("2");
                    break;

                case "Books":
                    selection.remove("3");
                    break;
            }
        }
    }

    class Donate extends AsyncTask<String,String,String> {
        private Context context;
        ArrayAdapter<String> adapter;
        public Donate(Context context) {
            this.context = context;
        }
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... arg0) {
            String line, data;
            StringBuilder sb=new StringBuilder();
            String link = "http://10.0.0.20:8000/donate";
            try {
                URL url = new URL(link);
                HttpURLConnection conn =(HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                data = "id=" + sp.getString("id", "") + "&number=" + sp.getString("number", "") + "&location=" + sp.getString("location", "")+ "&items=" + final_Donate + "&description=" + description;
                BufferedReader reader = new BufferedReader (new InputStreamReader(conn.getInputStream()));
                try{
                    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                    writer.write(data);
                    writer.flush();
                }catch (Exception e){
                    return new String("Exception: " + e.getMessage());
                }

                while((line=reader.readLine())!= null) {
                    sb.append(line);
                    publishProgress(line);
                }
                return line;
            } catch (IOException e) {
                e.printStackTrace();
                return new String("Exception: " + e.getMessage());
            }
        }
        protected void onProgressUpdate(String... values) {
            adapter.add(values[0]);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            verify+=result;
        }
    }

}
