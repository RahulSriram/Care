package tech.rahulsriram.care;

/**
 * Created by Jebin on 23-07-2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ConnectivityChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "care-logger";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "receiver");
        //intent=new Intent(context,CareService.class);
        //context.startService(intent);
        new CheckConnectivity().generateNotifications(context);
    }
}