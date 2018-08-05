package com.qader.ahmed.popularmovies;



public class BuildUrl {
    public static final String IMAGE_BASE_URL ="http://image.tmdb.org/t/p/w185/";
    public static final String VIDEO_BASE_URL ="https://www.youtube.com/watch?v=";
    public static final String POPULAR_MOVIE_URL = "http://api.themoviedb.org/3/movie/popular?api_key=ef11fa4ee85a2ce3a4ec3bb228455eb4";
    public static final String TOP_RATED_MOVIE_URL = "http://api.themoviedb.org/3/movie/top_rated?api_key=ef11fa4ee85a2ce3a4ec3bb228455eb4";
    private int movieId;
    public BuildUrl() {
    }
    public BuildUrl(int movieId) {
    this.movieId = movieId;
    }
    public String getMovieTrailersUlr(){
     return "http://api.themoviedb.org/3/movie/"+movieId+"/videos?api_key=ef11fa4ee85a2ce3a4ec3bb228455eb4";
    }
    public String getMovieReviewsUlr(){
     return "http://api.themoviedb.org/3/movie/"+movieId+"/reviews?api_key=ef11fa4ee85a2ce3a4ec3bb228455eb4";
    }
}
