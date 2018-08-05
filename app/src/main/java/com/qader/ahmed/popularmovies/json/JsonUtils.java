package com.qader.ahmed.popularmovies.json;

import android.util.Log;


import com.qader.ahmed.popularmovies.models.MovieModel;
import com.qader.ahmed.popularmovies.models.ReviewModel;
import com.qader.ahmed.popularmovies.models.TrailerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;



public class JsonUtils {
    private static final String TAG = JsonUtils.class.getSimpleName();
    private static final String MOVIE_ID = "id";
    private static final String MOVIE_TITLE = "title";
    private static final String MOVIE_POSTER_PATH = "poster_path";
    private static final String MOVIE_VOTE_AVERAGE = "vote_average";
    private static final String MOVIE_RELEASE_DATE = "release_date";
    private static final String MOVIE_OVERVIEW = "overview";
    private static final String JSONARRAY_RESULT = "results";
    private static MovieModel movieModel;
    public static ArrayList<MovieModel> parseMovieJson(String json){
        ArrayList<MovieModel> movieModelArrayList = new ArrayList<MovieModel>();
    try {
        JSONObject jsonObject1 = new JSONObject(json);
        JSONArray jsonArray = jsonObject1.getJSONArray(JSONARRAY_RESULT);
        for (int i = 0;i < jsonArray.length();i++){
            movieModel = new MovieModel();
            movieModel.setMovieId(jsonArray.optJSONObject(i).optInt(MOVIE_ID));
//            Log.e(TAG,"Movie id: "+movieModel.getMovieId());
            movieModel.setMovieTitle(jsonArray.optJSONObject(i).optString(MOVIE_TITLE));
//            Log.e(TAG,"Movie Title: "+movieModel.getMovieTitle());
            movieModel.setMoviePostarPath(jsonArray.optJSONObject(i).optString(MOVIE_POSTER_PATH));
            movieModel.setMovieVoteAverage(jsonArray.optJSONObject(i).optString(MOVIE_VOTE_AVERAGE));
            movieModel.setMovieReleaseDate(jsonArray.optJSONObject(i).optString(MOVIE_RELEASE_DATE));
            movieModel.setMovieOverview(jsonArray.optJSONObject(i).optString(MOVIE_OVERVIEW));
            movieModelArrayList.add(movieModel);
        }
    } catch (JSONException e) {
        e.printStackTrace();
        Log.e(TAG,e.getMessage());
    }
    return movieModelArrayList;
}
    public ArrayList<TrailerModel> parseTrailerJson(String data){
        ArrayList<TrailerModel> arrayList = new ArrayList<>();
        try {
            JSONObject dataObject = new JSONObject(data);
            JSONArray dataArray = dataObject.getJSONArray("results");
            String[] result = new String[dataArray.length()];
            String name,key;
            JSONObject object1;
            TrailerModel ob;
            for (int i = 0; i < dataArray.length(); i++) {
                object1 = dataArray.getJSONObject(i);
                name = object1.optString("name");
                key = object1.optString("key");
                ob = new TrailerModel();
                ob.setName(name);
                ob.setKey(key);
                arrayList.add(ob);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }
        return arrayList;
    }
    public ArrayList<ReviewModel> parseReviewJson(String data){
        ArrayList<ReviewModel> arrayList = new ArrayList<>();
        try {
            JSONObject dataObject = new JSONObject(data);
            JSONArray dataArray = dataObject.getJSONArray("results");
            String[] result = new String[dataArray.length()];
            String author, content;
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject object1 = dataArray.getJSONObject(i);
                author = object1.optString("author");
                content = object1.optString("content");
                ReviewModel ob = new ReviewModel();
                ob.setAuthor(author);
                ob.setContent(content);
                arrayList.add(ob);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }
        return arrayList;
    }
    private URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG,"Error With Creating Url"+e.getMessage());
        }
        return url;
    }
    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else
                Log.e(TAG,"HttpResponseCode"+urlConnection.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"Error With MakeHttpReqquest"+e.getMessage());
        }finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }


        return jsonResponse;
    }
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output =new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        return output.toString();
    }
    public ArrayList<MovieModel> getMoviesList(String urlString){
        URL url = createUrl(urlString);
        String jsonString = "";
            try {
                jsonString = makeHttpRequest(url);
                return parseMovieJson(jsonString);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,e.getMessage());
                return null;
            }
    }
    public ArrayList<TrailerModel> getTrailerList(String urlString){
        URL url = createUrl(urlString);
        String jsonString = "";
            try {
                jsonString = makeHttpRequest(url);
                return parseTrailerJson(jsonString);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,e.getMessage());
                return null;
            }
    }
    public ArrayList<ReviewModel> getReviewList(String urlString){
        URL url = createUrl(urlString);
        String jsonString = "";
            try {
                jsonString = makeHttpRequest(url);
                return parseReviewJson(jsonString);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,e.getMessage());
                return null;
            }
    }
}