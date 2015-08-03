package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Madhu on 7/25/15.
 */
public class MovieDetails implements Parcelable {

    private MovieDetails(Parcel in){

        moviesTitle = in.readString();
        moviePosterPath = in.readString();
        movieOverview = in.readString();
        moviesVoteAverage = in.readString();
        moviesReleaseDate = in.readString();

    }

    public MovieDetails() {
        this.movieOverview = movieOverview;
        this.moviePosterPath = moviePosterPath;
        this.moviesReleaseDate = moviesReleaseDate;
        this.moviesTitle = moviesTitle;
        this.moviesVoteAverage = moviesVoteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(moviesTitle);
        parcel.writeString(moviePosterPath);
        parcel.writeString(movieOverview);
        parcel.writeString(moviesVoteAverage);
        parcel.writeString(moviesReleaseDate);
    }

    public final Parcelable.Creator<MovieDetails> CREATOR = new Parcelable.Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel parcel) {
            return new MovieDetails(parcel);
        }

        @Override
        public MovieDetails[] newArray(int i) {
            return new MovieDetails[i];
        }

    };

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
