package tech.rahulsriram.care;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

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
    String description, verify = "", finalDonate;
    EditText itemDescription;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selection);

        sp = getSharedPreferences("Care", MODE_PRIVATE);

        itemDescription = (EditText) findViewById(R.id.itemDescription);

        Switch homeFoodSwitch = (Switch) findViewById(R.id.homeFoodSwitch);
        homeFoodSwitch.setOnCheckedChangeListener(this);

        Switch packedFoodSwitch = (Switch) findViewById(R.id.packedFoodSwitch);
        packedFoodSwitch.setOnCheckedChangeListener(this);

        Switch clothesSwitch = (Switch) findViewById(R.id.clothesSwitch);
        clothesSwitch.setOnCheckedChangeListener(this);

        Switch bookSwitch = (Switch) findViewById(R.id.bookSwitch);
        bookSwitch.setOnCheckedChangeListener(this);

        FloatingActionButton donate = (FloatingActionButton) findViewById(R.id.donateButton);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String selections : selection) {
                    finalDonate += selections;
                }

                description = itemDescription.getText().toString();
                 if(description.length() != 0 && finalDonate.length() != 0) {
                     new Donate().execute();

                     if(verify.equals("ok")) {
                         Snackbar.make(v, "Done", Snackbar.LENGTH_LONG).show();
                     } else {
                         Snackbar.make(v, "Please try again", Snackbar.LENGTH_LONG).show();
                     }
                 } else {
                     Snackbar.make(v, "Please enter description", Snackbar.LENGTH_LONG).show();
                 }
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton switchText, boolean isChecked) {
        if(isChecked) {
            switch (switchText.getText().toString()) {
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
            switch (switchText.getText().toString()) {
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

    class Donate extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String data;
            StringBuilder sb = new StringBuilder();

            try {
                URL url = new URL("http://10.0.0.20:8000/donate");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                data = "id=" + sp.getString("id", "") + "&number=" + sp.getString("number", "") + "&location=" + sp.getString("location", "")+ "&items=" + finalDonate + "&description=" + description;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                try {
                    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                    writer.write(data);
                    writer.flush();
                } catch (Exception e) {
                    return "error";
                }

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                return "error";
            }

            return sb.toString();
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            verify = result;
        }
    }

}
