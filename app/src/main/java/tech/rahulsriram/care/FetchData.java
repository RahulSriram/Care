package tech.rahulsriram.care;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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
        String username=arg0[0],password=arg0[1],ip="192.168.1.104";
        String link = "http://10.0.0.20:8000/login",id="437687",number="tdsjfgsdhf";
        String data="id=" + id + "&number="+number;//TODO:number,name,latitude,longitude,item,description
            try {
                URL url = new URL(link);
                HttpURLConnection conn =(HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                try{
                    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                    writer.write(data);
                    writer.flush();
                }catch (Exception e){
                    return new String("Exception: " + e.getMessage());
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while((data=reader.readLine())!=null) {
                    sb.append(data);
                    publishProgress(data);
                    Thread.sleep(1000);
                }
                conn.disconnect();
            } catch (Exception e) {
                return "error";
            }

        return sb.toString();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        adapter.add(values[0]);
    }

    protected void onPostExecute(String result) {
        noti.setText(result);
    }
}



/*
ImageView imageView = (ImageView) this.findViewById(R.id.img);
                ((BitmapDrawable)imageView.getDrawable()).getBitmap().recycle();
                imageView.setImageResource(R.drawable.new_image);
                imageView.invalidate();
                ArrayList<Item> imageArry = new ArrayList<Item>();
*/
