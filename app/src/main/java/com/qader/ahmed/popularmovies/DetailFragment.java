package com.qader.ahmed.popularmovies;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;



public class DetailFragment extends Fragment {

    TextView dec, date, popularity, count, average;
    ImageView image;
    ToggleButton fav;
    MovieData m ;
    MovieDatabase movieDatabase;
    ListView vedio_data, review_data;

    ListAdapter adapter;
    ReviewAdapter reviewAdapter;

    int checkFlag;

    TrailerData trailerData = new TrailerData();
    ReviewData reviewData = new ReviewData();

    ArrayList<TrailerData> listData = new ArrayList();
    ArrayList<ReviewData> listReview = new ArrayList();

    boolean fav_check = false;

    public DetailFragment() {
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle =getArguments();
        if (bundle ==null) {
            m = (MovieData) this.getActivity().getIntent().getSerializableExtra("movie");
            checkFlag = this.getActivity().getIntent().getIntExtra("flag",0);
        }else{
            m = (MovieData) bundle.getSerializable("movie");
            checkFlag = bundle.getInt("flag");
        }


//        m = (MovieData) getActivity().getIntent().getSerializableExtra("movie");
//        checkFlag = (int) getActivity().getIntent().getSerializableExtra("flag");

        if (checkFlag == 0) {
            String VEDIO_BASE_URL = "http://api.themoviedb.org/3/movie/" + m.getId()
                    + "/videos?api_key=ef11fa4ee85a2ce3a4ec3bb228455eb4";
            new FetchMovieVedio(getActivity()).execute(VEDIO_BASE_URL);

            String REVIEW_BASE_URL = "http://api.themoviedb.org/3/movie/" + m.getId()
                    + "/reviews?api_key=ef11fa4ee85a2ce3a4ec3bb228455eb4";
            new FetchMovieReview(getActivity()).execute(REVIEW_BASE_URL);
        }
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
/////////////////////////////////////////////

        movieDatabase = new MovieDatabase(getActivity());

        ArrayList list = movieDatabase.getAllId();
        fav_check = check_fav_id(list);

        if (fav_check == true){
            fav.isChecked();
            fav.setBackgroundResource(R.drawable.abc_btn_rating_star_on_mtrl_alpha);
        }

/////////////////////////////////////////////
        dec.setText(m.getOverview());
        date.setText(m.getRelease_date());
        popularity.setText("Popularity = " + m.getPopularity());
        count.setText("Vote count = " + m.getVote_count());
        average.setText("Vote average = " + m.getVote_average());
        Picasso.with(getActivity()).load(m.getPoster_path()).into(image);


        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()&&fav_check==false) {


                    String poster_path = m.getPoster_path();
                    String overview = m.getOverview();
                    String release_date = m.getRelease_date();
                    String title = m.getTitle();
                    double popularity = m.getPopularity();
                    int vote_count = m.getVote_count();
                    double vote_average = m.getVote_average();
                    int id = m.getId();

                    boolean check = movieDatabase.insertData(poster_path,overview,release_date,title,popularity,vote_count,vote_average,id);

                    if (check) {
                        fav.setBackgroundResource(R.drawable.abc_btn_rating_star_on_mtrl_alpha);

                        Toast.makeText(getActivity(), "add to favourit", Toast.LENGTH_LONG).show();
                    }else
                        Toast.makeText(getActivity(), "Databade error", Toast.LENGTH_LONG).show();

                } else {

                    int i = m.getId();
                    Integer result = movieDatabase.deleteData(i);

                    if(result>0){
                        Toast.makeText(getActivity(),"Delete data",Toast.LENGTH_LONG).show();
                        fav.setBackgroundResource(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
                    }else
                        Toast.makeText(getActivity(),"Error : not delete data",Toast.LENGTH_LONG).show();


                }
            }
        });



///////////////////////////////////////////////////////////////////////////////////////////

        vedio_data = (ListView) v.findViewById(R.id.vedio_data);
        adapter = new ListAdapter(getActivity());
        review_data = (ListView) v.findViewById(R.id.review_data);
        reviewAdapter = new ReviewAdapter(getActivity());

        vedio_data.setAdapter(adapter);
        review_data.setAdapter(reviewAdapter);


        return v;
    }

    private boolean check_fav_id(ArrayList list) {

        for (int i = 0;i<list.size();i++) {
            if (Integer.parseInt(list.get(i)+"")==m.getId())
                return true;
        }

        return false;
    }


    public class FetchMovieVedio extends AsyncTask<String, Void, ArrayList<TrailerData>> {


        Context context;

        public FetchMovieVedio(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<TrailerData> doInBackground(String... params) {

            String dataVedio = null;

            if (params.length == 0) {
                return null;
            }

            try {
                HttpURLConnection conn = ConnectionManager.openConnection(params[0]);

                dataVedio = ConnectionManager.getResult(conn);

            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<TrailerData> listVedio = null;
            try {
                listVedio = jsonData(dataVedio);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return listVedio;
        }

        private ArrayList<TrailerData> jsonData(String data) throws JSONException {

            if (data == null || data.length() == 0)
                return null;

            JSONObject jObject = new JSONObject(data);
            JSONArray jArray = jObject.getJSONArray("results");

            for (int i = 0; i < jArray.length(); i++) {
                trailerData = new TrailerData();
                JSONObject finalObject = jArray.getJSONObject(i);
                trailerData.setKey(finalObject.getString("key"));
                trailerData.setName(finalObject.getString("name"));
                trailerData.setSite(finalObject.getString("site"));
                trailerData.setType(finalObject.getString("type"));
                listData.add(trailerData);
            }
            return listData;
        }

        @Override
        protected void onPostExecute(ArrayList<TrailerData> l) {
            adapter.clear();
            for (int i = 0; i < l.size(); i++) {
                adapter.add(l.get(i));
            }

            adapter.notifyDataSetChanged();

            if (listData == null) {
                return;
            }

        }
    }

    public class FetchMovieReview extends AsyncTask<String, Void, ArrayList<ReviewData>> {


        Context context;

        public FetchMovieReview(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<ReviewData> doInBackground(String... params) {

            String dataVedio = null;

            if (params.length == 0) {
                return null;

            }

            try {

                HttpURLConnection conn = ConnectionManager.openConnection(params[0]);

                dataVedio = ConnectionManager.getResult(conn);

            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<ReviewData> reviewDatas = null;
            try {
                reviewDatas = jsonData(dataVedio);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return reviewDatas;
        }

        private ArrayList<ReviewData> jsonData(String data) throws JSONException {

            if (data == null || data.length() == 0)
                return null;

            JSONObject jObject = new JSONObject(data);
            JSONArray jArray = jObject.getJSONArray("results");

            for (int i = 0; i < jArray.length(); i++) {
                reviewData = new ReviewData();
                JSONObject finalObject = jArray.getJSONObject(i);
                reviewData.setAuthor(finalObject.getString("author"));
                reviewData.setContent(finalObject.getString("content"));

                listReview.add(reviewData);
            }

            return listReview;

        }

        @Override
        protected void onPostExecute(ArrayList<ReviewData> l) {

            reviewAdapter.clear();
            for (int i = 0; i < l.size(); i++) {

                reviewAdapter.add(l.get(i));
            }
            review_data.setAdapter(reviewAdapter);

            if (listData == null)
                return;



        }
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