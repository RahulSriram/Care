package tech.rahulsriram.care;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;


/**
 * Created by Jebin on 13-08-2016.
 */
public class CheckConnectivity {
    public String a = "s", b = "nil", check=null;
    public String checkconnectivity(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] info = cm.getAllNetworkInfo();
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    //b = new FetchDataa().execute("yes").get();
                    //return b;
                    return "true";
                }
            }
            return "nil";
        } catch (Exception e) {
            return "nil";
        }
    }

    public void generateNotifications(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), CareService.class);
        Intent intentI = new Intent(context.getApplicationContext(), AllNotifications.class);
        NotificationManager mNotificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pi = PendingIntent.getActivity(context.getApplicationContext(), 0, intentI, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext());
        mBuilder.setSmallIcon(R.drawable.notification_icon);
        mBuilder.setContentTitle("Notification");
        if (!("nil".equals(a = checkconnectivity(context)))) {
            if(isMyServiceRunning(CareService.class, context))/*"myservice".equals(check=sharedPreferences.getString("servicename","n/a")))*/{
                mBuilder.setContentText(a + " Donations Available");
                mBuilder.setContentIntent(pi);
                mNotificationManager.notify(324255, mBuilder.build());
                //TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
                //taskStackBuilder.addParentStack(AllNotifications.class);
                //taskStackBuilder.addNextIntent(intentI);
            }
            else {
                context.startService(intent);
                mBuilder.setContentText(a + " Donations Available");
                mBuilder.setContentIntent(pi);
                mNotificationManager.notify(324255, mBuilder.build());
                //TaskStackBuilder taskStackBuilder=TaskStackBuilder.create(context);
            }
        } else {
            if(isMyServiceRunning(CareService.class,context)){
                context.stopService(intent);
            }
        }
    }
    public boolean isMyServiceRunning(Class ClassName,Context context) {
        ActivityManager manager = (ActivityManager)context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ClassName.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}