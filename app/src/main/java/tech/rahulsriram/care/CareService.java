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

public class CareService extends Service {
    private static final String TAG = "care-logger";

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

        return START_STICKY;
    }
}
