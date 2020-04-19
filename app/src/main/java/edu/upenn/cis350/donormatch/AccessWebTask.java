package edu.upenn.cis350.donormatch;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class AccessWebTask extends AsyncTask<URL, String, String> {

    String method = "";

    // absolutely no idea if this is right
    public AccessWebTask(String request_method) {
        method = request_method;
    }

    @Override
    protected String doInBackground(URL... urls) {

       try{
           URL url = urls[0];

           HttpURLConnection conn = (HttpURLConnection)url.openConnection();
           conn.setRequestMethod(method);
           conn.connect();

           Scanner in = new Scanner(url.openStream());
           String msg = in.nextLine();
           System.out.println("------------------------------------------------------------");

           System.out.println(msg);
           System.out.println("------------------------------------------------------------");

           JSONObject jobj = new JSONObject(msg);
           System.out.println("Address printed below");
           System.out.println(jobj.toString());
           String res = jobj.toString();

           return res;
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
