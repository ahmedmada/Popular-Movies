package com.qader.ahmed.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class MovieDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 2;

    public MovieDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_DETAILS_TABLE ="CREATE TABLE "+ MovieContract.DetailEntry.TABLE_NAME + " (" +
                MovieContract.DetailEntry._ID +" INTEGER PRIMARY KEY,"+
                MovieContract.DetailEntry.MY_ID +" INTEGER,"+
                MovieContract.DetailEntry.DESCRIPTION +" TEXT,"+
                MovieContract.DetailEntry.DATE +" TEXT,"+
                MovieContract.DetailEntry.IMAGE +" TEXT,"+
                MovieContract.DetailEntry.POPULARITY +" DOUBLE,"+
                MovieContract.DetailEntry.TITLE +" TEXT,"+
                MovieContract.DetailEntry.VOTE_AVERAGE +" DOUBLE,"+
                MovieContract.DetailEntry.VOTE_COUNT +" INTEGER"+
                " );";

        db.execSQL(SQL_CREATE_DETAILS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.DetailEntry.TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String poster_path , String overview , String release_date , String title , double popularity
            , int vote_count , double vote_average , int my_id){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.DetailEntry.MY_ID,my_id);
        contentValues.put(MovieContract.DetailEntry.DESCRIPTION,overview);
        contentValues.put(MovieContract.DetailEntry.DATE,release_date);
        contentValues.put(MovieContract.DetailEntry.IMAGE,poster_path);
        contentValues.put(MovieContract.DetailEntry.POPULARITY,popularity);
        contentValues.put(MovieContract.DetailEntry.TITLE,title);
        contentValues.put(MovieContract.DetailEntry.VOTE_AVERAGE,vote_average);
        contentValues.put(MovieContract.DetailEntry.VOTE_COUNT,vote_count);

        long result = db.insert(MovieContract.DetailEntry.TABLE_NAME,null,contentValues);

        Log.d("aaa",result+" ............................................ ");

        if(result==-1)
            return false;
        else
            return true;
    }

    public ArrayList getAllRecord(){
        ArrayList arrayList = new ArrayList<MovieData>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+MovieContract.DetailEntry.TABLE_NAME,null);

        MovieData movieData;
        res.moveToFirst();

        while (res.isAfterLast()==false){

            movieData = new MovieData();

            int s0 = res.getInt(1);
            String s1 = res.getString(2);
            String s2 = res.getString(3);
            String s3 = res.getString(4);
            double s4 = res.getDouble(5);
            String s5 = res.getString(6);
            double s6 = res.getDouble(7);
            int s7 = res.getInt(8);

            movieData.setId(s0);
            movieData.setOverview(s1);
            movieData.setRelease_date(s2);
            movieData.setPoster_path(s3);
            movieData.setPopularity(s4);
            movieData.setTitle(s5);
            movieData.setVote_average(s6);
            movieData.setVote_count(s7);

            arrayList.add(movieData);

            res.moveToNext();
        }


        return arrayList;

    }

    public ArrayList getAllId(){
        ArrayList arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+MovieContract.DetailEntry.TABLE_NAME,null);

        res.moveToFirst();

        while (res.isAfterLast()==false){

            int i = res.getInt(1);
            res.getString(2);
            res.getString(3);
            res.getString(4);
            res.getString(5);
            res.getString(6);
            res.getString(7);
            res.getString(8);

            arrayList.add(i);

            res.moveToNext();
        }


        return arrayList;

    }
    public Integer deleteData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MovieContract.DetailEntry.TABLE_NAME, "my_id= ?", new String[]{String.valueOf(id)});

    }

}