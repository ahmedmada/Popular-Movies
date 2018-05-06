package com.qader.ahmed.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class ListAdapter extends ArrayAdapter<TrailerData> {

//    TrailerData trailerData = new TrailerData();
    Context c;
    LayoutInflater layoutInflater=null;

    public ListAdapter(Context c) {
        super(c, 0);
        this.c=c;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) this.c.getSystemService(c.LAYOUT_INFLATER_SERVICE);

        convertView = layoutInflater.inflate(R.layout.list_item_movie,parent,false);

        Button watch = (Button) convertView.findViewById(R.id.watch_btn);
        //watch.setText(getItem(position).getKey());

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(getItem(position).getKey()));
                    c.startActivity(i);
                }catch (Exception e){
                    Toast.makeText(getContext(),"Sorry,you can't watch this vedio",Toast.LENGTH_LONG).show();
                }
            }
        });
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(getItem(position).getName());

        TextView site = (TextView) convertView.findViewById(R.id.site);
        site.setText(getItem(position).getSite());

        TextView type = (TextView) convertView.findViewById(R.id.type);
        type.setText(getItem(position).getType());

        return convertView;

    }
}
