package tech.rahulsriram.care;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Jebin on 25-07-2016.
 */
public class FetchData extends AsyncTask<String,String,String> {
    private Context context;
    public TextView noti;
    ListView listView;
    ArrayAdapter<String> adapter;
    public FetchData(Context context, TextView text, ListView ls) {
        this.context = context;
        this.noti=text;
        this.listView=ls;
    }
    protected void onPreExecute() {
        adapter=(ArrayAdapter<String>)listView.getAdapter();
    }
    @Override
    protected String doInBackground(String... arg0) {
        StringBuilder sb= new StringBuilder();
        String username=arg0[0],password=arg0[1],ip="192.168.1.104",data;
            try {
                //String link = "http://" + ip + "/sample.php?username=" + username + "&password=" + password+"&available=false";
                String link = "http://192.168.1.104/sample.html";
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                //conn.setRequestMethod("POST");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while((data=reader.readLine())!=null) {
                    sb.append(data);
                    publishProgress(data);
                    Thread.sleep(1000);
                }
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return new String("Exception: " + e.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
                return new String("Exception: " + e.getMessage());
            }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        adapter.add(values[0]);
    }

    protected void onPostExecute(String result) {
        this.noti.setText(result);
    }
}



/*
ImageView imageView = (ImageView) this.findViewById(R.id.img);
                ((BitmapDrawable)imageView.getDrawable()).getBitmap().recycle();
                imageView.setImageResource(R.drawable.new_image);
                imageView.invalidate();
                ArrayList<Item> imageArry = new ArrayList<Item>();
*/