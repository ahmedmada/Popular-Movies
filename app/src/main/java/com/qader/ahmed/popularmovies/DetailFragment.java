package com.qader.ahmed.popularmovies;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.squareup.picasso.Picasso;



public class DetailFragment extends Fragment {

    TextView dec, date, popularity, count, average;
    ImageView image;
    ToggleButton fav;
    MovieData m ;




    public DetailFragment() {
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m = (MovieData) getActivity().getIntent().getSerializableExtra("movie");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_detail, container, false);


        getActivity().setTitle(m.getTitle());
        getActivity().setTitleColor(Color.BLUE);

        fav = (ToggleButton) v.findViewById(R.id.fav);
        dec = (TextView) v.findViewById(R.id.textDescription);
        date = (TextView) v.findViewById(R.id.date);
        image = (ImageView) v.findViewById(R.id.image);
        popularity = (TextView) v.findViewById(R.id.popularity);
        count = (TextView) v.findViewById(R.id.vote_count);
        average = (TextView) v.findViewById(R.id.vote_average);

        dec.setText(m.getOverview());
        date.setText(m.getRelease_date());
        popularity.setText("Popularity = " + m.getPopularity());
        count.setText("Vote count = " + m.getVote_count());
        average.setText("Vote average = " + m.getVote_average());
        Picasso.with(getActivity()).load(m.getPoster_path()).into(image);


        return v;
    }



    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}