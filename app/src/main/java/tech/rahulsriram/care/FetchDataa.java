package tech.rahulsriram.care;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jebin on 25-07-2016.
 */
public class FetchDataa extends AsyncTask<String,String,String> {
    private Context context;
    ListView listView0;
    ArrayAdapter<String> adapter;
    public FetchDataa(Context context, ListView ls) {
        this.context = context;
        this.listView0=ls;
    }
    protected void onPreExecute() {
        adapter=(ArrayAdapter<String>)listView0.getAdapter();
    }
    @Override
    protected String doInBackground(String... arg0) {
        String line=null,ip="192.168.1.104",avail=arg0[0];
        StringBuilder sb=new StringBuilder();
        String link = "http://10.0.0.20:8000/recent_history";
            try {
                URL url = new URL(link);
                HttpURLConnection conn =(HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                BufferedReader reader = new BufferedReader (new InputStreamReader(conn.getInputStream()));
                while((line=reader.readLine())!=null) {
                    sb.append(line);
                    publishProgress(line);
                    Thread.sleep(1000);
                }
                    return line;
            } catch (IOException e) {
                e.printStackTrace();
                return new String("Exception: " + e.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
                return new String("Exception: " + e.getMessage());
            }
    }
    protected void onProgressUpdate(String... values) {
        adapter.add(values[0]);
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        // this.newsfeed.setText(result);
    }
}
