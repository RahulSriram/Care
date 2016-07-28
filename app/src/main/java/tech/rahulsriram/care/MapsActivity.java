package tech.rahulsriram.care;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.location.LocationListener;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;

    private double lat, lon;

    @Override
    public void onLocationChanged(final Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        // Add a marker and move the camera
        LatLng currentLocation = new LatLng(lat, lon);
        Log.i("tech.rahulsriram.care.Location", currentLocation.toString());
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        //TODO Auto-generated method stub
    }

    @Override
    public void onProviderDisabled(String provider) {
        //TODO Auto-generated method stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        long LOCATION_REFRESH_TIME = 100;
        float LOCATION_REFRESH_DISTANCE = 1;
        Criteria criteria = new Criteria();
        String bestProvider = mLocationManager.getBestProvider(criteria, true);
        mLocationManager.requestLocationUpdates(bestProvider, LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap = googleMap;

        // Add a marker and move the camera
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
    }
}