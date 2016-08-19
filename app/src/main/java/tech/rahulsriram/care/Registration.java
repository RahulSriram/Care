package tech.rahulsriram.care;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jebineinstein on 19/8/16.
 */
public class Registration extends AsyncTask<String,Void,String>
{
        public TextView textView;
        String add_info_url,line=null,a;
        public Registration(TextView tc)
        {
                this.textView=tc;
        }
@Override
protected void onPreExecute() {
        add_info_url = "http://10.0.0.20:8000/register";
        }
@Override
protected String doInBackground(String ...var) {
        String data_string="id=&number=&name=&code=";
        StringBuilder sb=new StringBuilder();
        try {
        URL url= new URL(add_info_url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        OutputStreamWriter bufferedWriter =new OutputStreamWriter(httpURLConnection.getOutputStream());
        bufferedWriter.write("");
        bufferedWriter.flush();
        BufferedReader reader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while((line=reader.readLine())!=null) {
                        sb.append(line);
                        //publishProgress(data);
                        //Thread.sleep(1000);
                }
                httpURLConnection.disconnect();
        return sb.toString();
        } catch (MalformedURLException e) {
        e.printStackTrace();
                return new String("Exception: " + e.getMessage());
        } catch (IOException e) {
        e.printStackTrace();
                return new String("Exception: " + e.getMessage());
        }
        //return sb.toString();
        }
@Override
protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.i(a,"onpostexecute");
        this.textView.setText(result);
        }
}

