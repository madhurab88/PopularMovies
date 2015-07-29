package com.example.android.popularmovies;

/**
 * Created by Madhu on 7/25/15.
 */
public class MovieDetails {

    final String baseURLMovieImagesURL = "http://image.tmdb.org/t/p/";
    final String imageSize = "w185";
    private String moviesTitle = "title";
    private String moviePosterPath = "poster_path";
    private String movieOverview = "overview";
    private String moviesVoteAverage = "vote_average";
    private String moviesReleaseDate = "release_date";

    public String getPosterPathURL() {
        return baseURLMovieImagesURL + imageSize + getMoviePosterPath();
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public String getMoviePosterPath() {
        return moviePosterPath;
    }

    public void setMoviePosterPath(String moviePosterPath) {
        this.moviePosterPath = moviePosterPath;
    }

    public String getMoviesReleaseDate() {
        return moviesReleaseDate;
    }

    public void setMoviesReleaseDate(String moviesReleaseDate) {
        this.moviesReleaseDate = moviesReleaseDate;
    }

    public String getMoviesTitle() {
        return moviesTitle;
    }

    public void setMoviesTitle(String moviesTitle) {
        this.moviesTitle = moviesTitle;
    }

    public String getMoviesVoteAverage() {
        return moviesVoteAverage;
    }

    public void setMoviesVoteAverage(String moviesVoteAverage) {
        this.moviesVoteAverage = moviesVoteAverage;
    }
}
