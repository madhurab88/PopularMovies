package com.example.android.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.android.popularmovies.MovieDetails;

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
import java.util.Arrays;


/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMoviesFragment extends Fragment {

    private final String LOG_TAG = PopularMoviesFragment.class.getSimpleName();
    ArrayList<MovieDetails> moviePosterPathURLArray;
    MovieDetails movieDetails = null;
    private ImageAdapter ia;
    private GridView gridview;

    public PopularMoviesFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchMoviesTask moviestask = new fetchMoviesTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOption = prefs.getString(getString(R.string.sort_by_key),
                getString(R.string.sort_by_default));
        moviestask.execute(sortOption);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            moviePosterPathURLArray = new ArrayList<MovieDetails>(Arrays.asList(movieDetails));
        } else {
            moviePosterPathURLArray = savedInstanceState.getParcelableArrayList("movies");
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", moviePosterPathURLArray);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridview = (GridView) rootView.findViewById(R.id.grid_item_movies);

        return rootView;
    }

    public boolean isNetworkOnline() {
        boolean status = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getNetworkInfo(0);
        if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
            status = true;
        } else {
            netInfo = cm.getNetworkInfo(1);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                status = true;
        }
        return status;
    }

    public class fetchMoviesTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = fetchMoviesTask.class.getSimpleName();

        private ArrayList<MovieDetails> getMoviesDataFromJson(String moviesDetailsJsonStr) throws JSONException {

            final String mdb_results = "results";
            JSONObject movieDetailsobj = new JSONObject(moviesDetailsJsonStr);
            JSONArray ResultsArray = movieDetailsobj.getJSONArray(mdb_results);

            moviePosterPathURLArray = new ArrayList<MovieDetails>();

            for (int i = 0; i < ResultsArray.length(); i++) {

                movieDetails = new MovieDetails();
                JSONObject resultsObj = ResultsArray.getJSONObject(i);

                String moviesTitle = "title";
                String moviePosterPath = "poster_path";
                String movieOverview = "overview";
                String moviesVoteAverage = "vote_average";
                String moviesReleaseDate = "release_date";

                movieDetails.setMoviesTitle(resultsObj.getString(moviesTitle));
                movieDetails.setMoviePosterPath(resultsObj.getString(moviePosterPath));
                movieDetails.setMovieOverview(resultsObj.getString(movieOverview));
                movieDetails.setMoviesVoteAverage(resultsObj.getString(moviesVoteAverage));
                movieDetails.setMoviesReleaseDate(resultsObj.getString(moviesReleaseDate));

                moviePosterPathURLArray.add(movieDetails);
            }
            return moviePosterPathURLArray;
        }

        @SuppressLint("LongLogTag")
        @Override
        protected Void doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesDetailsJsonStr = null;
            String api_key_value = "";

            try {
                // Construct the URL for the themoviedb.org API for query

                final String FORECAST_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String QUERY_PARAM = "sort_by";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(API_KEY, api_key_value)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                if (isNetworkOnline()) {
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
                        Log.v(LOG_TAG, "Error getting Data from JSON" + moviePosterPathURLArray.size());

                    } catch (JSONException e) {
                        Log.v(LOG_TAG, "JSONException");
                    }
                } else {

                    Log.d(LOG_TAG, "Internet Connection Not Available");
                }

            } catch (IOException e) {
                Log.e("PopularMoviesFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
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


        @Override
        protected void onPostExecute(Void aVoid) {
            ia = new ImageAdapter(getActivity().getBaseContext(), moviePosterPathURLArray);

            gridview.setAdapter(ia);

            gridview.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    MovieDetails md = moviePosterPathURLArray.get(position);

                    Bundle b = new Bundle();
                    b.putStringArray("MOVIE_DETAILS_ARRAY", new String[]{md.getMoviesTitle(),
                            md.getMovieOverview(),
                            md.getPosterPathURL(), md.getMoviesVoteAverage(), md.getMoviesReleaseDate()
                    });
                    Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                    detailIntent.putExtras(b);

                    startActivity(detailIntent);
                }
            });

        }


    }
}
