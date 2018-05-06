package com.qader.ahmed.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ReviewAdapter extends ArrayAdapter<ReviewData> {

    Context context;
    LayoutInflater layoutInflater=null;

    public ReviewAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        convertView = layoutInflater.inflate(R.layout.review_item_list,parent,false);

        TextView author = (TextView) convertView.findViewById(R.id.author);
        author.setText(getItem(position).getAuthor());

        TextView content = (TextView) convertView.findViewById(R.id.content);
        content.setText(getItem(position).getContent());

        return convertView;

    }
}
