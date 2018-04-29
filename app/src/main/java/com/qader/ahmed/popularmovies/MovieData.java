package com.qader.ahmed.popularmovies;

import android.os.Parcel;

import java.io.Serializable;


public class MovieData  implements Serializable {
    private String poster_path;
    private String overview;
    private String release_date;
    private String title;
    private double popularity;
    private int vote_count;
    private double vote_average;

    protected MovieData(Parcel in) {
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        title = in.readString();
        popularity = in.readDouble();
        vote_count = in.readInt();
        vote_average = in.readDouble();
    }


    public MovieData() {

    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getPoster_path() {
        return "http://image.tmdb.org/t/p/w185/"+poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}