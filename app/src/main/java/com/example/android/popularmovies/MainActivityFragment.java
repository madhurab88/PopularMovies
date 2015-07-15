package com.example.android.popularmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        ArrayList<ImageView> imageViews= new ArrayList<ImageView>();
//        for (int i = 0; i < 5; i++) {
//            ImageView imageView = new ImageView(getActivity());
//            imageView.setImageResource(R.drawable.sample_8);
//            imageViews.add(imageView);
//        }
//
//        ArrayAdapter<ImageView> loadImagesAdapter;
//
//        loadImagesAdapter = new ArrayAdapter<ImageView>(
//                getActivity(),
//                R.layout.grid_item_movies,
//                R.id.grid_item_movies_textview,
//                imageViews
//        );

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        /*GridView gridview = (GridView) rootView.findViewById(R.id.grid_item_movies);
        gridview.setAdapter(loadImagesAdapter);
*/
        return rootView;
    }
}
