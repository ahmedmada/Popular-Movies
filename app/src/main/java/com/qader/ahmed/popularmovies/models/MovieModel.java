package com.qader.ahmed.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;



public class MovieModel  implements Serializable,Parcelable{
    private int movieId;
    private String movieTitle;
    private String movieOverview;
    private String moviePostarPath;
    private String movieReleaseDate;
    private String movieVoteAverage;

    public MovieModel() {
    }

    protected MovieModel(Parcel in) {
        movieId = in.readInt();
        movieTitle = in.readString();
        movieOverview = in.readString();
        moviePostarPath = in.readString();
        movieReleaseDate = in.readString();
        movieVoteAverage = in.readString();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public String getMoviePostarPath() {
        return moviePostarPath;
    }

    public void setMoviePostarPath(String moviePostarPath) {
        this.moviePostarPath = moviePostarPath;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMovieVoteAverage() {
        return movieVoteAverage;
    }

    public void setMovieVoteAverage(String movieVoteAverage) {
        this.movieVoteAverage = movieVoteAverage;
    }

    @Override
    public String toString() {
        return "Movie Title: "+getMovieTitle()+"\n"
                +"Movie Poster Path: "+getMoviePostarPath()+"\n"
                +"Movie Vote Average: "+getMovieVoteAverage()+"\n"
                +"Movie Release Date: "+getMovieReleaseDate()+"\n"
                +"Movie Overview: "+getMovieOverview()+"\n"
                ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(movieTitle);
        dest.writeString(movieOverview);
        dest.writeString(moviePostarPath);
        dest.writeString(movieReleaseDate);
        dest.writeString(movieVoteAverage);
    }
}
