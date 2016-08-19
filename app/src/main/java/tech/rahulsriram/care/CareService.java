package tech.rahulsriram.care;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Calendar;

/**
 * Created by Jebin on 20-07-2016.
 */
public class CareService extends Service {
    public String a="hi1";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public int onStartCommand(Intent intents,int flags,int startid) {
        Intent intent=new Intent(this,ConnectivityChangeReceiver.class);
        PendingIntent pi=PendingIntent.getBroadcast(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar cal=Calendar.getInstance();//create calendar
        cal.add(Calendar.SECOND, 5);//add 5 seconds to calendar
        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),1000*5,pi);
            /*new FetchDataa(
                    new FetchDataa.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                a=output;
                }
            }).execute(available);*/
            return START_STICKY;
    }
    public void onDestroy(){
        super.onDestroy();
    }

}
