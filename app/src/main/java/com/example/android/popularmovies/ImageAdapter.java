package com.example.android.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Madhu on 7/15/15.
 */
public class ImageAdapter extends BaseAdapter {
    private final String LOG_TAG = ImageAdapter.class.getSimpleName();

    private Context imageContext;
    private ArrayList<MovieDetails> movieImages;

    public ImageAdapter(Context imageContext, ArrayList<MovieDetails> movieImages) {
        this.imageContext = imageContext;
        this.movieImages = movieImages;
    }

    public int getCount() {
        return this.movieImages.size();
    }

    public Object getItem(int position) {
        return movieImages.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {


        ImageView imageView;
        //check to see if we have a view
        if (convertView == null) {
            imageView = new ImageView(this.imageContext);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(this.imageContext).load(this.movieImages.get(position).getPosterPathURL())
                .noFade().resize(185,277)
                .centerCrop()
                .into(imageView);
        return imageView;
    }
}
