package edu.upenn.cis350.donormatch;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class AccessWebTask extends AsyncTask<URL, String, String> {


    @Override
    protected String doInBackground(URL... urls) {

       try{
           URL url = urls[0];

           HttpURLConnection conn = (HttpURLConnection)url.openConnection();
           conn.setRequestMethod("GET");
           conn.connect();

           Scanner in = new Scanner(url.openStream());
           String msg = in.nextLine();

           System.out.println(msg);

           JSONObject jobj = new JSONObject(msg);
           String addr = jobj.getString("address");
          // System.out.println("address should be below this");
          // System.out.println(addr);
           return addr;
       }
       catch (Exception e){
           return e.toString();
       }

    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println(s);
        System.out.println("address should be printed above");
    }
}
