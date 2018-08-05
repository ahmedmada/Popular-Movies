package com.qader.ahmed.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.qader.ahmed.popularmovies.adapters.ReviewAdapter;
import com.qader.ahmed.popularmovies.adapters.TrailerAdapter;
import com.qader.ahmed.popularmovies.contentprovider.MovieProvider;
import com.qader.ahmed.popularmovies.database.MovieContruct;
import com.qader.ahmed.popularmovies.json.JsonUtils;
import com.qader.ahmed.popularmovies.models.MovieModel;
import com.qader.ahmed.popularmovies.models.ReviewModel;
import com.qader.ahmed.popularmovies.models.TrailerModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DetailActivity extends AppCompatActivity {
    ImageView moviePosterImageView;
    TextView movieVoteAverageTextView,movieReleaseDateTextView,movieOverViewTextView;
    MovieModel movieDetail;
    RecyclerView trailersRecyclerView,reviewsRecyclerView;
    TrailerAdapter trailerAdapter;
    ReviewAdapter reviewAdapter;
    ToggleButton favoriteToggleButton;
    ScrollView scrollView;
    public static int scrollX = 0;
    public static int scrollY = -1;
    public static int lastFirstVisiblePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        movieDetail = (MovieModel) intent.getExtras().getSerializable("movieKey");
        initUI();
        polishUi();
        addMovieToFavorites();
        checkIfMovieFavorite();
    }
    private void initUI(){
    moviePosterImageView = findViewById(R.id.activity_detail_movie_image);
    movieVoteAverageTextView = findViewById(R.id.activity_detail_movie_vote_average);
    movieReleaseDateTextView = findViewById(R.id.activity_detail_movie_release_date);
    movieOverViewTextView = findViewById(R.id.activity_detail_movie_overview);
    favoriteToggleButton = findViewById(R.id.favorite_toggle_button);
    trailersRecyclerView = findViewById(R.id.trailer_recycler_view);
    scrollView = findViewById(R.id.activity_detail_container);
    LinearLayoutManager trailersLinearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
    trailersRecyclerView.setLayoutManager(trailersLinearLayoutManager);
    reviewsRecyclerView = findViewById(R.id.reviews_recycler_view);
    LinearLayoutManager reviewsLinearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
    reviewsRecyclerView.setLayoutManager(reviewsLinearLayoutManager);
}
    private void polishUi(){
        if (movieDetail.getMoviePostarPath()!=null)
            Picasso.with(this).load(BuildUrl.IMAGE_BASE_URL+movieDetail.getMoviePostarPath()).into(moviePosterImageView);
        if (movieDetail.getMovieTitle()!=null)
            setTitle(movieDetail.getMovieTitle());
        if (movieDetail.getMovieVoteAverage()!=null)
            movieVoteAverageTextView.setText(movieDetail.getMovieVoteAverage());
        if (movieDetail.getMovieReleaseDate()!=null)
            movieReleaseDateTextView.setText(movieDetail.getMovieReleaseDate());
        if (movieDetail.getMovieOverview()!=null)
            movieOverViewTextView.setText(movieDetail.getMovieOverview());
    }
    public void addMovieToFavorites(){
        favoriteToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoriteToggleButton.isChecked()){
                    // here add movie to database
                    ContentValues values =  new ContentValues();
                    values.put(MovieContruct.Favorite.MOVIE_ID, movieDetail.getMovieId());
                    values.put(MovieContruct.Favorite.POSTER_PATH, movieDetail.getMoviePostarPath());
                    values.put(MovieContruct.Favorite.OVERVIEW, movieDetail.getMovieOverview());
                    values.put(MovieContruct.Favorite.RELEASE_DATE, movieDetail.getMovieReleaseDate());
                    values.put(MovieContruct.Favorite.TITLE, movieDetail.getMovieTitle());
                    values.put(MovieContruct.Favorite.VOTE_AVERAGE, movieDetail.getMovieVoteAverage());
                    Uri uri = getContentResolver().insert(MovieProvider.CONTENT_URI,values);
                    Log.e(DetailActivity.class.getSimpleName(),uri+"");
                    Toast.makeText(DetailActivity.this, "Movie Added Successfully", Toast.LENGTH_SHORT).show();
                }else {
                    // here delete movie from database
                    String selection = "movie_id = ?";
                    String selectionArgs [] = new String[]{movieDetail.getMovieId() + ""};
                    int delete = getContentResolver().delete(MovieProvider.CONTENT_URI,selection,selectionArgs);
                    if (delete > -1)
                        Toast.makeText(DetailActivity.this, "Movie Deleted Successfully", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(DetailActivity.this, "Movie Deleted Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void checkIfMovieFavorite(){
    //this method to check if the movie in favorite list
        String URL = "content://movieprovider/movies/"+movieDetail.getMovieId();
        Uri uri = Uri.parse(URL);
        String selection = "movie_id = ?";
        String selectionArgs [] = new String[]{movieDetail.getMovieId() + ""};
        Cursor c = getContentResolver().query(uri,null,selection,selectionArgs,null,null);
        if (c.moveToFirst())
            favoriteToggleButton.setChecked(true);
        else
            favoriteToggleButton.setChecked(false);
    }
    @Override
    protected void onStart() {
        super.onStart();
        BuildUrl buildUrl = new BuildUrl(movieDetail.getMovieId());
        if (CheckInternetConnection.isConnected(this)) {
            new FetchTrailerData().execute(buildUrl.getMovieTrailersUlr());
            new FetchReviewData().execute(buildUrl.getMovieReviewsUlr());
        }else
            Toast.makeText(this, R.string.no_internet+" for Trailers and Reviews !!", Toast.LENGTH_SHORT).show();
    }
    private class FetchTrailerData extends AsyncTask<String,Void,ArrayList<TrailerModel>> {
        private final String LOG_TAG = FetchTrailerData.class.getSimpleName();

        public FetchTrailerData() {
        }
        @Override
        protected ArrayList<TrailerModel> doInBackground(String... strings) {
//        Log.e(TAG,"Here Is doInBackground Of MovieAsyncTask");
            if (strings.length < 1 || strings[0] == null)
            {return null;}

            JsonUtils movieUtils = new JsonUtils();
            ArrayList<TrailerModel> trailerModels = movieUtils.getTrailerList(strings[0]);
            return trailerModels;
        }
        @Override
        protected void onPostExecute(ArrayList<TrailerModel> trailers) {
//        Log.e(TAG,"Here Is onPostExecute Of MovieAsyncTask");
            if (trailers != null && !trailers.isEmpty()) {
                trailerAdapter = new TrailerAdapter(DetailActivity.this,trailers);
                trailersRecyclerView.setAdapter(trailerAdapter);
                trailerAdapter.notifyDataSetChanged();
            }


        }
    }
    private class FetchReviewData extends AsyncTask<String,Void,ArrayList<ReviewModel>> {
        private final String LOG_TAG = FetchReviewData.class.getSimpleName();
        public FetchReviewData() {
        }
        @Override
        protected ArrayList<ReviewModel> doInBackground(String... strings) {
//        Log.e(TAG,"Here Is doInBackground Of MovieAsyncTask");
            if (strings.length < 1 || strings[0] == null)
            {return null;}

            JsonUtils movieUtils = new JsonUtils();
            ArrayList<ReviewModel> reviewModels = movieUtils.getReviewList(strings[0]);
            return reviewModels;
        }
        @Override
        protected void onPostExecute(ArrayList<ReviewModel> reviews) {
//        Log.e(TAG,"Here Is onPostExecute Of MovieAsyncTask");
            if (reviews != null && !reviews.isEmpty()) {
                reviewAdapter = new ReviewAdapter(DetailActivity.this,reviews);
                reviewsRecyclerView.setAdapter(reviewAdapter);
                reviewAdapter.notifyDataSetChanged();
            }


        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        scrollX = scrollView.getScrollX();
        scrollY = scrollView.getScrollY();
        lastFirstVisiblePosition = ((LinearLayoutManager)reviewsRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scrollView.post(new Runnable()
        {
            @Override
            public void run()
            {
                scrollView.scrollTo(scrollX, scrollY);
            }
        });
        ((LinearLayoutManager) reviewsRecyclerView.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);
    }
}
