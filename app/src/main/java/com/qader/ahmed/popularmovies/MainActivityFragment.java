package com.qader.ahmed.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;



public class MainActivityFragment extends Fragment {


    List<MovieData> listData = new ArrayList<>();

    ListAdapter adapter;

    RecyclerView mrecycle;
    ProgressBar progressBar;

    MovieData movieData = new MovieData();
    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recycle_grid, container, false);


        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mrecycle = (RecyclerView) rootView.findViewById(R.id.recycle);
        mrecycle.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new ListAdapter();
        mrecycle.setAdapter(adapter);


        return rootView;
    }

    public void firstStart() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String sortBy = sharedPreferences.getString("arrang", "popular");

        String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/" + sortBy + "?";

        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter("api_key", "ef11fa4ee85a2ce3a4ec3bb228455eb4")
                .build();


        new FetchMovieData(getActivity()).execute(builtUri.toString());

    }


    @Override
    public void onStart() {
        super.onStart();

//        progressBar.setVisibility(View.VISIBLE);
        firstStart();
     }



    public class FetchMovieData extends AsyncTask<String, Void, ArrayList> {

        Context context;

        public FetchMovieData(Context context){
            this.context = context;
        }

        @Override
        protected ArrayList doInBackground(String... params) {

            String data = null;

            if (params.length == 0) {
                return null;
            }

            try {
                HttpURLConnection conn = ConnectionManager.openConnection(params[0]);
                data = ConnectionManager.getResult(conn);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            List<MovieData> list = null;
            try {
                 jsonData(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return (ArrayList) listData;
        }

        private List jsonData(String data) throws JSONException {

            if (data == null || data.length() == 0)
                return null;

            JSONObject jObject = new JSONObject(data);
            JSONArray jArray = jObject.getJSONArray("results");

            if (listData == null)
                listData = new ArrayList<>();

            else
                listData.clear();
            for (int i=0 ; i<jArray.length() ; i++){

                JSONObject finalObject = jArray.getJSONObject(i);

                movieData = new MovieData();
                movieData.setPoster_path(finalObject.getString("poster_path"));
                movieData.setOverview(finalObject.getString("overview"));
                movieData.setRelease_date(finalObject.getString("release_date"));
                movieData.setTitle(finalObject.getString("title"));
                movieData.setPopularity(finalObject.getDouble("popularity"));
                movieData.setVote_count(finalObject.getInt("vote_count"));
                movieData.setVote_average(finalObject.getDouble("vote_average"));

                listData.add(movieData);
            }

            return listData;

        }

        @Override
        protected void onPostExecute(ArrayList l) {

//            progressBar.setVisibility(View.INVISIBLE);

            if (listData == null) {
                return;
            }
            adapter.notifyDataSetChanged();
        }
    }

    private class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image ;
        MovieData moviedata;

        public ImageHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            image.setOnClickListener(this);
        }

        public void bind(MovieData data){
            Picasso.with(getActivity()).load(data.getPoster_path()).into(image);
            moviedata = data;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(),DetailActivity.class);
            i.putExtra("movie",  moviedata);
            startActivity(i);
        }
    }

    private class ListAdapter extends RecyclerView.Adapter<ImageHolder>{

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.grid_item_layout, parent, false);
            return new ImageHolder(v);
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, int position) {
            holder.bind(listData.get(position));
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       if (id == R.id.action_settings) {
            Intent i = new Intent(getActivity(),SettingActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
