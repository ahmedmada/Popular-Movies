package com.qader.ahmed.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.qader.ahmed.popularmovies.BuildUrl;
import com.qader.ahmed.popularmovies.DetailActivity;
import com.qader.ahmed.popularmovies.R;
import com.qader.ahmed.popularmovies.models.MovieModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;



public class MovieAdapter extends ArrayAdapter<MovieModel> {
    private static final String TAG = MovieAdapter.class.getSimpleName();
    public MovieAdapter(@NonNull Context context, @NonNull List<MovieModel> objects) {
        super(context, 0,objects);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final MovieModel movieModel = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_card,parent,false);
        }
        ImageView moviePoster = convertView.findViewById(R.id.activity_detail_movie_image);
        if (movieModel.getMoviePostarPath() != null)
            Picasso.with(getContext()).load(BuildUrl.IMAGE_BASE_URL +movieModel.getMoviePostarPath()).into(moviePoster);
        moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getContext(), DetailActivity.class);
                intent.putExtra("movieKey", (Serializable) movieModel);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        TextView movieTitle = convertView.findViewById(R.id.activity_detail_movie_title);
        if (movieModel.getMovieTitle() != null)
        movieTitle.setText(movieModel.getMovieTitle());
        TextView movieVoteAverage = convertView.findViewById(R.id.activity_main_movie_vote_average);
        if (movieModel.getMovieVoteAverage() != null)
        movieVoteAverage.setText(movieModel.getMovieVoteAverage());
        return convertView;
    }
}
