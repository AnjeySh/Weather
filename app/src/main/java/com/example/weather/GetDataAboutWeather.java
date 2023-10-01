package com.example.weather;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GetDataAboutWeather extends AsyncTask <URL, Void, String>{
    public interface AsyncResponse{
        void proccessFinish(String output);
    }
    public AsyncResponse deligate;
    public GetDataAboutWeather(AsyncResponse deligate){
        this.deligate=deligate;
    }
    protected String getResponseFromHttpGetURL(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in =connection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput=scanner.hasNext();
            String result;
            if(hasInput){
                result=scanner.next();
                return result;
            } else return null;
        } finally {
            connection.disconnect();
        }

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(URL[] url) {
        String result=null;
        try {
            result=getResponseFromHttpGetURL(url[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(o);
        deligate.proccessFinish(result);
    }
}


