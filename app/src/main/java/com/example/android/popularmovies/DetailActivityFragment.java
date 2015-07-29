package com.example.android.popularmovies;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View detailView = inflater.inflate(R.layout.fragment_detail, container, false);

        //Getting the array from PopularMoviesFragment using Bundle
        Bundle b = getActivity().getIntent().getExtras();
        String[] movieArray = b.getStringArray("MOVIE_DETAILS_ARRAY");

        ((TextView) detailView.findViewById(R.id.movieTitle_text))
                .setText(movieArray[0]);
        ((TextView) detailView.findViewById(R.id.movieOverview_text))
                .setText(movieArray[1]);

        ImageView iv = ((ImageView) detailView.findViewById(R.id.moviePosterURL_image));
        Picasso.with(getActivity()).load(movieArray[2]).into(iv);

        ((TextView) detailView.findViewById(R.id.movieRating_text))
                .setText("User Rating:" + movieArray[3]);

        ((TextView) detailView.findViewById(R.id.movieReleaseDate_text))
                .setText("Release Date:" + movieArray[4]);

        return detailView;
    }
}
