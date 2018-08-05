package com.qader.ahmed.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.qader.ahmed.popularmovies.BuildUrl;
import com.qader.ahmed.popularmovies.CheckInternetConnection;
import com.qader.ahmed.popularmovies.R;
import com.qader.ahmed.popularmovies.models.TrailerModel;

import java.util.ArrayList;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    Context mContext;
    int currentPosition;
    ArrayList<TrailerModel> trailerModelArrayList;
    public TrailerAdapter() {
    }
    public TrailerAdapter(Context mContext, ArrayList<TrailerModel> trailerModelArrayList) {
        this.mContext = mContext;
        this.trailerModelArrayList = trailerModelArrayList;
    }
    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_card,parent,false);
        TrailerViewHolder holder = new TrailerViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
    currentPosition = position;
        holder.bind();
    }
    @Override
    public int getItemCount() {
        if (trailerModelArrayList.size() != 0)
        return trailerModelArrayList.size();
        else return 0;
    }
    class TrailerViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public TrailerViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.trailer_image);
        }
        public void bind(){
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CheckInternetConnection.isConnected(mContext)) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BuildUrl.VIDEO_BASE_URL +
                                trailerModelArrayList.get(currentPosition).getKey()));
                        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                            mContext.startActivity(intent);

                        }
                    }else
                        Toast.makeText(mContext, R.string.no_internet, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
