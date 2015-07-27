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

/*

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String mtitle = intent.getStringExtra(Intent.EXTRA_TEXT);

            ((TextView) deailView.findViewById(R.id.detail_text))
                    .setText(mtitle);


        }*/
        Bundle b=getActivity().getIntent().getExtras();
        String[] movieArray=b.getStringArray("MOVIE_DETAILS_ARRAY");
        for(int i=0;i<movieArray.length;i++) {
            System.out.println("****" + movieArray[i]);
            ((TextView) detailView.findViewById(R.id.movieTitle_text))
                    .setText(movieArray[0]);
            ((TextView) detailView.findViewById(R.id.movieOverview_text))
                    .setText(movieArray[1]);

            /*Float ratingValue = Float.valueOf(movieArray[3]);
            System.out.println("----" + ratingValue);
            ((RatingBar) detailView.findViewById(R.id.movieVoteAverage_rating))
                    .setRating(ratingValue);*/


           ImageView iv = ((ImageView) detailView.findViewById(R.id.moviePosterURL_image));
            //String url = movieArray[2];

            Picasso.with(getActivity()).load(movieArray[2]).into(iv);

                    ((TextView) detailView.findViewById(R.id.movieRating_text))
                    .setText("Vote_Average:" + movieArray[3]);

            ((TextView) detailView.findViewById(R.id.movieReleaseDate_text))
                    .setText("ReleaseDate:"+movieArray[4]);

                   /* ((ImageView) detailView.findViewById(R.id.moviePosterURL_image))
                    .setImageResource(Integer.parseInt(movieArray[2]));
*/
        }


        return detailView;


    }
}
