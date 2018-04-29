package com.qader.ahmed.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class ConnectionManager {

    //check internet
    public static boolean isInternetConnected(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isAvailable())
            //internet is work
            return true;
        //internet not work
        return false;
    }

    //open connection
    public static HttpURLConnection openConnection(String link) throws IOException {

        //url link
        URL url = new URL(link);
        //Returns a new connection to the resource referred to by this URL.
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setRequestMethod("GET");
        //the maximum time in milliseconds to wait while connecting
        httpURLConnection.setConnectTimeout(15000);
        //the maximum time to wait for an input stream read to complete before giving up
        httpURLConnection.setReadTimeout(15000);
        httpURLConnection.setDoInput(true);
        //Opens a connection to the resource
        httpURLConnection.connect();

        return httpURLConnection;

    }

    //Read Data
    public static String getResult(HttpURLConnection httpURLConnection) throws IOException{

        InputStream inputStream = httpURLConnection.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = bufferedReader.readLine()) != null)
            stringBuilder.append(line);

        return stringBuilder.toString();
    }

}
