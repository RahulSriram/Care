package tech.rahulsriram.care;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;

/**
 * Created by Jebin on 20-07-2016.
 */
public class CareService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "care-logger";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intents,int flags,int startId) {
        Log.i(TAG, "CareService");
        Intent intent = new Intent(this, ConnectivityChangeReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        SharedPreferences sp = getSharedPreferences("Care", MODE_PRIVATE);
        Calendar cal = Calendar.getInstance(); //create calendar
        cal.add(Calendar.SECOND, sp.getInt("update_interval", 15) * 60); //add 5 seconds to calendar
        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sp.getInt("update_interval", 15) * 60 * 1000, pi);

        Log.i(TAG, "start location client");
        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();

            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();

                if (mGoogleApiClient.isConnected()) {
                    startLocationUpdates();
                }
            }
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
        stopLocationUpdates();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

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
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(10); //in meters
    }

    protected boolean checkPlayServices() {
        Log.i(TAG, "checkPlayServices()");
        int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (result != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(result)) {
                GooglePlayServicesUtil.getErrorDialog(result, null, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getApplicationContext(), "Application needs Location Permissions to work", Toast.LENGTH_LONG).show();
        }
    }

    protected void stopLocationUpdates() {
        Log.i(TAG, "stopLocationUpdates()");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            if (location.hasAccuracy()) {
                SharedPreferences sp = getSharedPreferences("Care", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                editor.putString("location", location.getLatitude() + "," + location.getLongitude());
                editor.apply();
            }
        }
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
}
