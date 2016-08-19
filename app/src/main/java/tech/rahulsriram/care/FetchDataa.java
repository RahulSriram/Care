package tech.rahulsriram.care;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Jebin on 25-07-2016.
 */
public class FetchDataa extends AsyncTask<String,Void,String> {
    private Context context;
   /* public interface AsyncResponse{
        void processFinish(String output);
    }
    public AsyncResponse delegate=null;
    public FetchDataa(AsyncResponse asyncResponse){
     delegate=asyncResponse;
    }*/
    protected void onPreExecute() {
    }
    @Override
    protected String doInBackground(String... arg0) {
        String line=null,ip="192.168.1.104",avail=arg0[0];
        StringBuffer buffer=new StringBuffer();
            try {
                //String link = "http://"+ip+"/sample.php?available="+avail;
                String link = "http://192.168.1.104/sample.html";
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                BufferedReader reader = new BufferedReader (new InputStreamReader(conn.getInputStream()));
                while((line=reader.readLine())!=null);
                    buffer.append(line);
                    return line;
            } catch (IOException e) {
                e.printStackTrace();
                return new String("Exception: " + e.getMessage());
            }
    }
    protected void onPostExecute(String result) {
        //delegate.processFinish(result);
    }
}
