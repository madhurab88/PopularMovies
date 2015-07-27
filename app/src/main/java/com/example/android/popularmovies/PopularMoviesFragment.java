package com.example.android.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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


/**
 * A placeholder fragment containing a simple view.
 */
public class PopularMoviesFragment extends Fragment {

    private final String LOG_TAG = PopularMoviesFragment.class.getSimpleName();
    ArrayList<MovieDetails> moviePosterPathURLArray;
    private ImageAdapter ia;
    private GridView gridview;

    public PopularMoviesFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

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
        gridview = (GridView) rootView.findViewById(R.id.grid_item_movies);






        return rootView;
    }
    public class fetchMoviesTask extends AsyncTask <Void, Void, Void> {

        private final String LOG_TAG = fetchMoviesTask.class.getSimpleName();

        private ArrayList<MovieDetails> getMoviesDataFromJson(String moviesDetailsJsonStr) throws JSONException {

            final String mdb_results = "results";
            JSONObject movieDetailsobj = new JSONObject(moviesDetailsJsonStr);
            JSONArray ResultsArray = movieDetailsobj.getJSONArray(mdb_results);


            moviePosterPathURLArray = new ArrayList<MovieDetails>();
            MovieDetails movieDetails = null;
            for(int i = 0; i < ResultsArray.length(); i++) {

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
        protected Void doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
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
                    Log.v(LOG_TAG, "LogTest1 ----" + moviePosterPathURLArray.size());

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

        @Override
        protected void onPostExecute(Void aVoid) {
            ia = new ImageAdapter(getActivity().getBaseContext(), moviePosterPathURLArray);
            gridview.setAdapter(ia);
            gridview.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    //Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();

                    MovieDetails md = moviePosterPathURLArray.get(position);


                    //String mtitle = md.getMoviesTitle();

                    //Intent detailIntent = new Intent(getActivity(), DetailActivity.class)
                           // .putExtra(Intent.EXTRA_TEXT, (mtitle));
                    Bundle b = new Bundle();
                    b.putStringArray("MOVIE_DETAILS_ARRAY", new String[]{md.getMoviesTitle(),
                                                                         md.getMovieOverview(),
                            md.getPosterPathURL(),md.getMoviesVoteAverage(),md.getMoviesReleaseDate()
                    });
                    Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                    detailIntent.putExtras(b);

                    startActivity(detailIntent);
                }
            });

        }


    }
}
