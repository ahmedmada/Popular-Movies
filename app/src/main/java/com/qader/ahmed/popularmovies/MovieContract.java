package com.qader.ahmed.popularmovies;

import android.provider.BaseColumns;



public class MovieContract {
    public static final class DetailEntry implements BaseColumns{

        public static final String TABLE_NAME = "details";
        public static final String DESCRIPTION = "description";
        public static final String DATE = "date";
        public static final String TITLE = "title";
        public static final String POPULARITY = "popularity";
        public static final String VOTE_COUNT = "vote_count";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String IMAGE = "image";
        public static final String MY_ID = "my_id";

    }
}
