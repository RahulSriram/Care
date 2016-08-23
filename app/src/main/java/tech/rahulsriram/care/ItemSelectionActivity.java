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

public class ItemSelectionActivity  extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "care-logger";
    private static final String LOCATION_PERMISSION_UNAVAILABLE_MSG = "Application needs Location Permissions to work";
    private ArrayList<String> selection = new ArrayList<>();
    private String description, finalDonate;
    private EditText itemDescription;
    private SharedPreferences sp;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location lastKnownLocation;

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "buildGoogleApiClient()");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        Log.i(TAG, "createLocationRequest()");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(60*1000); //in milliseconds
        mLocationRequest.setFastestInterval(60*1000); //in milliseconds
        mLocationRequest.setSmallestDisplacement(10); //in meters
    }

    protected boolean checkPlayServices() {
        Log.i(TAG, "checkPlayServices()");
        int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (result != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(result)) {
                GooglePlayServicesUtil.getErrorDialog(result, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported.", Toast.LENGTH_LONG).show();
                finish();
            }

            return false;
        }

        return true;
    }

    protected void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates()");
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), LOCATION_PERMISSION_UNAVAILABLE_MSG, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    protected void stopLocationUpdates() {
        Log.i(TAG, "stopLocationUpdates()");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selection);

        sp = getSharedPreferences("Care", MODE_PRIVATE);

        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }

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
                        if (lastKnownLocation != null) {
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
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
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

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged()");
        if (location != null) {
            if (location.hasAccuracy()) {
                lastKnownLocation = location;
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "onConnectionSuspended()");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //TODO: empty-stub method
    }

    class DonateTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            StringBuilder sb = new StringBuilder();

            try {
                String location = lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude();
                String link = "http://10.0.0.20:8000/donate";
                String data = "id=" + URLEncoder.encode(sp.getString("id", ""), "UTF-8") + "&number=" + URLEncoder.encode(sp.getString("number", ""), "UTF-8") + "&location=" + URLEncoder.encode(location, "UTF-8") + "&items=" + URLEncoder.encode(finalDonate, "UTF-8") + "&description=" + URLEncoder.encode(description, "UTF-8");
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
