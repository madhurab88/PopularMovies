package com.example.android.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMoviesFragment extends Fragment {

    String[] moviePosterPathURLArray;


    public PopularMoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                // Add this line in order for this fragment to handle menu events.
        fetchMoviesTask moviestask = new fetchMoviesTask();
        moviestask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridview = (GridView) rootView.findViewById(R.id.grid_item_movies);
        gridview.setAdapter(new ImageAdapter(getActivity()));
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }



    public class fetchMoviesTask extends AsyncTask <Void, Void, Void> {

        private final String LOG_TAG = fetchMoviesTask.class.getSimpleName();

        private String[] getMoviesDataFromJson(String moviesDetailsJsonStr) throws JSONException {

            final String mdb_results = "results";
            JSONObject movieDetailsobj = new JSONObject(moviesDetailsJsonStr);
            JSONArray ResultsArray = movieDetailsobj.getJSONArray(mdb_results);


             moviePosterPathURLArray = new String[ResultsArray.length()];
            for(int i = 0; i < ResultsArray.length(); i++) {

                JSONObject resultsObj = ResultsArray.getJSONObject(i);

                String moviesTitle = "title";
                String moviePosterPath = "poster_path";
                String movieOverview = "overview";
                String moviesVoteAverage = "vote_average";
                String moviesReleaseDate = "release_date";
                final String baseURLMovieImagesURL = "http://image.tmdb.org/t/p/";
                final String imageSize = "w185";

                String Title = resultsObj.getString(moviesTitle);
                String PosterPath = resultsObj.getString(moviePosterPath);
                String PosterPathURL = baseURLMovieImagesURL+imageSize+PosterPath;
                String Overview = resultsObj.getString(movieOverview);
                String VoteAverage = resultsObj.getString(moviesVoteAverage);
                String ReleaseDate = resultsObj.getString(moviesReleaseDate);

                moviePosterPathURLArray[i] = PosterPathURL;

                    /*System.out.println( "moviesTitle: " + Title);
                    System.out.println( "moviePosterPathURL: " + PosterPathURL);
                    System.out.println( "arraycontent " + moviePosterPathURLArray[i]);
                    System.out.println( "movieOverview: " + Overview);
                    System.out.println( "moviesVoteAverage: " + VoteAverage);
                    System.out.println( "moviesReleaseDate: " + ReleaseDate);*/
            }
            return moviePosterPathURLArray;

        }


        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(Void... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesDetailsJsonStr = null;
            String query_string = "popularity.desc";
            String api_key_value = "ec68b0639a7fadca7767c9f77b4e6c0b";

            try {
                // Construct the URL for the themoviedb.org API for query

                //URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=ec68b0639a7fadca7767c9f77b4e6c0b");

                final String FORECAST_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String QUERY_PARAM = "sort_by";
                final String API_KEY = "api_key";
                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                                               .appendQueryParameter(QUERY_PARAM,query_string)
                                                .appendQueryParameter(API_KEY,api_key_value)
                                                .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                moviesDetailsJsonStr = buffer.toString();
                Log.v(LOG_TAG, "MoviepathJSONString" + moviesDetailsJsonStr.toString());

                try {
                    getMoviesDataFromJson(moviesDetailsJsonStr);
                } catch (JSONException e) {
                    Log.v(LOG_TAG, "JSONException");
                }




            } catch (IOException e) {
                Log.e("PopularMoviesFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            }  finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PopularMoviesFragment", "Error closing stream", e);
                    }
                }
            }


            return null;
        }
    }
}
