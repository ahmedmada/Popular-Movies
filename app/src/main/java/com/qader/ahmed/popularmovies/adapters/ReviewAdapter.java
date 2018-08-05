package com.qader.ahmed.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qader.ahmed.popularmovies.R;
import com.qader.ahmed.popularmovies.models.ReviewModel;

import java.util.ArrayList;




public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{
    Context mContext;
    ArrayList<ReviewModel> reviewModelArrayList;
    int currentPosition;
    public ReviewAdapter() {
    }
    public ReviewAdapter(Context mContext, ArrayList<ReviewModel> reviewModelArrayList) {
        this.reviewModelArrayList = reviewModelArrayList;
        this.mContext = mContext;
    }
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.review_card,parent,false);
        ReviewViewHolder holder = new ReviewViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        currentPosition = position;
        holder.bind();
    }
    @Override
    public int getItemCount() {
        if (reviewModelArrayList.size() != 0)
        return reviewModelArrayList.size();
        else return 0;
    }
    class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView author;
        TextView content;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author);
            content = itemView.findViewById(R.id.content);
        }
        public void bind(){
            author.setText(reviewModelArrayList.get(currentPosition).getAuthor());
            content.setText(reviewModelArrayList.get(currentPosition).getContent());
        }
    }
}