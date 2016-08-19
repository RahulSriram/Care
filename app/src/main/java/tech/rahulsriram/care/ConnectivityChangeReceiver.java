package tech.rahulsriram.care;

/**
 * Created by Jebin on 23-07-2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ConnectivityChangeReceiver extends BroadcastReceiver {
    public String a;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(a,"receiver");
        //intent=new Intent(context,CareService.class);
        //context.startService(intent);
            new Checkconnectivity().generatenotifications(context);
    }
}