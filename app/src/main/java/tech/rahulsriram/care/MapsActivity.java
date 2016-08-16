package tech.rahulsriram.care;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.LocationListener;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, OnMyLocationButtonClickListener, ConnectionCallbacks, OnConnectionFailedListener, LocationListener, LocationSource {
    private static final String TAG = "care-logger";
    private static final String LOCATION_UNAVAILABLE_MSG = "Couldn't get location. Please check if your GPS is on";
    private static final String LOCATION_PERMISSION_UNAVAILABLE_MSG = "Application needs Location Permissions to work";
    //private boolean isFirstLocationUpdate = true;
    private GoogleMap map;
    private Location lastKnownLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationSource.OnLocationChangedListener mLocationListener = null;

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
        //mLocationRequest.setInterval(10*60*1000); //in milliseconds
        //mLocationRequest.setFastestInterval(5*60*1000); //in milliseconds
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
                Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_LONG).show();
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
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "fab.setOnClickListener()");
                String reply = LOCATION_UNAVAILABLE_MSG;
                if(lastKnownLocation != null) {
                    reply = lastKnownLocation.toString();
                }

                Toast.makeText(getApplicationContext(), reply, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart()");
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume()");
        super.onResume();
        checkPlayServices();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause()");
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged()");

        if (location != null) {
            if (location.hasAccuracy()/* && location.getAccuracy() < 30*/) {
                lastKnownLocation = location;
                if (mLocationListener != null) {
                    mLocationListener.onLocationChanged(lastKnownLocation);
                }

                Log.i(TAG, "Updated location: " + lastKnownLocation.toString());
                Toast.makeText(getApplicationContext(), "Updated: " + lastKnownLocation.toString(), Toast.LENGTH_SHORT).show();
            }/* else if(isFirstLocationUpdate) {
                lastKnownLocation = location;
                isFirstLocationUpdate = false;
                if (mLocationListener != null) {
                    mLocationListener.onLocationChanged(lastKnownLocation);
                }

                Log.i(TAG, "Initial location inaccurate: " + lastKnownLocation.toString());
                Toast.makeText(getApplicationContext(), "Initial: " + lastKnownLocation.toString(), Toast.LENGTH_SHORT).show();
            }*/ else {
                Log.i(TAG, "Inaccurate location: " + location.toString());
                Toast.makeText(getApplicationContext(), "Inaccurate: " + location.toString(), Toast.LENGTH_SHORT).show();
            }

            if (lastKnownLocation != null) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), (float) 17.5));
            }
        } else {
            Toast.makeText(getApplicationContext(), LOCATION_UNAVAILABLE_MSG, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "onMapReady()");
        map = googleMap;
        map.setLocationSource(this);
        map.setOnMyLocationButtonClickListener(this);
        try {
            map.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), LOCATION_PERMISSION_UNAVAILABLE_MSG, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Log.i(TAG, "onMyLocationButtonClick()");
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            onLocationChanged(location);
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), LOCATION_PERMISSION_UNAVAILABLE_MSG, Toast.LENGTH_LONG).show();
            finish();
        }

        return true;
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "onConnectionFailed()");
        Log.i(TAG, "Connection failed: " + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "onConnected()");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int arg) {
        Log.i(TAG, "onConnectionSuspended()");
        mGoogleApiClient.connect();
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        Log.i(TAG, "activate()");
        mLocationListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        Log.i(TAG, "deactivate()");
        mLocationListener = null;
    }
}