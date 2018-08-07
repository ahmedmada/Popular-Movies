package com.qader.ahmed.popularmovies;

import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.qader.ahmed.popularmovies.adapters.MovieAdapter;
import com.qader.ahmed.popularmovies.contentprovider.MovieProvider;
import com.qader.ahmed.popularmovies.json.JsonUtils;
import com.qader.ahmed.popularmovies.models.MovieModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    GridView movieGridView;
    MovieAdapter movieAdapter;
    TextView emptyStateTextView;
    ProgressBar movieProgressBar;
    MovieProvider movieProvider;
    ArrayList<MovieModel> movies1;
    public static int scrollX = 0;
    public static int scrollY = -1;
    public static int lastFirstVisiblePosition = 0;

    boolean fav=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        movieGridView = findViewById(R.id.Movie_gridview);
        emptyStateTextView = findViewById(R.id.empty_state_textview);
        movieProgressBar = findViewById(R.id.moive_progressbar);
        movieGridView.setEmptyView(emptyStateTextView);
        movieAdapter = new MovieAdapter(this.getBaseContext(),new ArrayList<MovieModel>());
        movieGridView.setAdapter(movieAdapter);
        movieProvider = new MovieProvider();
        if (savedInstanceState==null){
            if (CheckInternetConnection.isConnected(this))
                new MovieAsyncTask().execute(BuildUrl.POPULAR_MOVIE_URL);
            else {
                emptyStateTextView.setText(R.string.no_internet);
            }
        }else {
            Parcelable[] parcelable = savedInstanceState.
                    getParcelableArray("movies");
            if (parcelable != null) {
                int numMovieObjects = parcelable.length;
//                Toast.makeText(this, ""+numMovieObjects, Toast.LENGTH_SHORT).show();

                ArrayList<MovieModel> movies = new ArrayList<MovieModel>();
                for (int i = 0; i < numMovieObjects; i++) {
                    movies.add((MovieModel) parcelable[i]);
                }
                // Load movie objects into view
                movieGridView.setAdapter(new MovieAdapter(this,movies));
                movieGridView.setScrollX(savedInstanceState.getInt("scrollX"));
                movieGridView.setScrollY(savedInstanceState.getInt("scrollY"));

            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (fav){
            Uri uri = Uri.parse("content://movieprovider/movies");
            Cursor c = getContentResolver().query(uri,null,null,null,null);
            ArrayList<MovieModel> movies = new ArrayList<MovieModel>();
            MovieModel model;
            while (c.moveToNext()){
                model = new MovieModel();
                model.setMovieId(c.getInt(0));
                model.setMoviePostarPath(c.getString(1));
                model.setMovieOverview(c.getString(2));
                model.setMovieReleaseDate(c.getString(3));
                model.setMovieTitle(c.getString(4));
                Log.e("favorite list Title",c.getString(4));
                model.setMovieVoteAverage(c.getString(5));
                movies.add(model);
            }
            if (c.moveToFirst()){
                movieProgressBar.setVisibility(View.GONE);
                movieAdapter.clear();
                movieAdapter.addAll(movies);
                movieAdapter.notifyDataSetChanged();
                movieGridView.setAdapter(movieAdapter);
            }else{
                movieProgressBar.setVisibility(View.GONE);
                Toast.makeText(this, "There is no movies in favorite list", Toast.LENGTH_SHORT).show();
                movieAdapter.clear();
                movieAdapter.notifyDataSetChanged();
            }
        }
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        int gridSize = movieGridView.getCount();
        if (gridSize > 0){
            MovieModel [] models = new MovieModel[gridSize];
            for (int i = 0;i < gridSize;i++){
                models[i] = (MovieModel) movieGridView.getItemAtPosition(i);
            }
            savedInstanceState.putParcelableArray("movies", models);
        }
        savedInstanceState.putInt("scrollX", movieGridView.getScrollX());
        savedInstanceState.putInt("scrollY", movieGridView.getScrollY());
        savedInstanceState.putBoolean("status", true);
        super.onSaveInstanceState(savedInstanceState);
    }
    Parcelable state;
    int index;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (CheckInternetConnection.isConnected(this)) {
            if (item.getItemId() == R.id.popular_menu){
                new MovieAsyncTask().execute(BuildUrl.POPULAR_MOVIE_URL);
                fav=false;
            }
            if (item.getItemId() == R.id.top_rated_menu) {
                new MovieAsyncTask().execute(BuildUrl.TOP_RATED_MOVIE_URL);
                fav=false;
            }
        }else
            emptyStateTextView.setText(R.string.no_internet);
        if (item.getItemId() == R.id.favorite){
            fav=true;
            Uri uri = Uri.parse("content://movieprovider/movies");
            Cursor c = getContentResolver().query(uri,null,null,null,null);
            ArrayList<MovieModel> movies = new ArrayList<MovieModel>();
            MovieModel model;
            while (c.moveToNext()){
                model = new MovieModel();
                model.setMovieId(c.getInt(0));
                model.setMoviePostarPath(c.getString(1));
                model.setMovieOverview(c.getString(2));
                model.setMovieReleaseDate(c.getString(3));
                model.setMovieTitle(c.getString(4));
                Log.e("favorite list Title",c.getString(4));
                model.setMovieVoteAverage(c.getString(5));
                movies.add(model);
            }
            if (c.moveToFirst()){
                movieProgressBar.setVisibility(View.GONE);
                    movieAdapter.clear();
                    movieAdapter.addAll(movies);
                    movieAdapter.notifyDataSetChanged();
                    movieGridView.setAdapter(movieAdapter);
            }else{
                movieProgressBar.setVisibility(View.GONE);
                Toast.makeText(this, "There is no movies in favorite list", Toast.LENGTH_SHORT).show();
                movieAdapter.clear();
                movieAdapter.notifyDataSetChanged();
            }
        }
        return true;
    }
    private class MovieAsyncTask extends AsyncTask<String,Void,ArrayList<MovieModel>> {
        @Override
        protected void onPreExecute() {
            movieProgressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected ArrayList<MovieModel> doInBackground(String... strings) {
            if (strings.length < 1 || strings[0] == null)
            {return null;}
            JsonUtils movieUtils = new JsonUtils();
            ArrayList<MovieModel> movieModels = movieUtils.getMoviesList(strings[0]);
            return movieModels;
        }
        @Override
        protected void onPostExecute(ArrayList<MovieModel> movies) {
            emptyStateTextView.setText(R.string.no_movies);
            movieProgressBar.setVisibility(View.GONE);
            if (movies != null && !movies.isEmpty()) {
                movieAdapter.clear();
                movieAdapter.addAll(movies);
                movieAdapter.notifyDataSetChanged();
                movieGridView.setAdapter(movieAdapter);
                movies1 = new ArrayList<MovieModel>();
                movies1 = movies;
            }
        }
    }
}