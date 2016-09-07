package tech.rahulsriram.care;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ItemSelectionActivity  extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private ArrayList<String> selection = new ArrayList<>();
    private String description, finalDonate;
    private EditText itemDescription;
    private SharedPreferences sp;


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

        final FloatingActionButton donate = (FloatingActionButton) findViewById(R.id.donateButton);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDonate = "";
                for (String selections : selection) {
                    finalDonate += selections;
                }

                description = itemDescription.getText().toString();
                itemDescription.clearFocus();

                if (!description.isEmpty()) {
                    if (!finalDonate.isEmpty()) {
                        if (!sp.getString("location", "").isEmpty()) {
                            new DonateTask().execute();
                        } else {
                            Snackbar.make(v, "Couldn't fetch your location. Please check if your GPS is on", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        Snackbar.make(v, "Please select at least one type", Snackbar.LENGTH_LONG).show();
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

    class DonateTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            StringBuilder sb = new StringBuilder();

            try {
                String link = "http://10.0.0.20:8000/donate";
                String data = "id=" + URLEncoder.encode(sp.getString("id", ""), "UTF-8") + "&number=" + URLEncoder.encode(sp.getString("number", ""), "UTF-8") + "&location=" + URLEncoder.encode(sp.getString("location", ""), "UTF-8") + "&items=" + URLEncoder.encode(finalDonate, "UTF-8") + "&description=" + URLEncoder.encode(description, "UTF-8");
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data);
                writer.flush();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

            } catch (Exception e) {
                return "error";
            }

            return sb.toString();
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.equals("ok")) {
                Snackbar.make(findViewById(R.id.ItemSelectionLayout), "Done", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(findViewById(R.id.ItemSelectionLayout), "Please try again", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
