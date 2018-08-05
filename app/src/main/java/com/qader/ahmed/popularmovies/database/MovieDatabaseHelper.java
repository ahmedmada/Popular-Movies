package com.qader.ahmed.popularmovies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.qader.ahmed.popularmovies.models.MovieModel;

import java.util.ArrayList;


public class MovieDatabaseHelper extends SQLiteOpenHelper {
    Context context;
    public MovieDatabaseHelper(Context context) {
        super(context, "Movie Data Base", null, 1);
        this.context = context;
    }
    private static final String TAG = MovieDatabaseHelper.class.getSimpleName();
    private final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + MovieContruct.Favorite.TABLE_NAME + " (" +
            MovieContruct.Favorite.MOVIE_ID + " INTEGER PRIMARY KEY," +
            MovieContruct.Favorite.POSTER_PATH + " TEXT, " +
            MovieContruct.Favorite.OVERVIEW + " TEXT, " +
            MovieContruct.Favorite.RELEASE_DATE + " TEXT," +
            MovieContruct.Favorite.TITLE + " TEXT, " +
            MovieContruct.Favorite.VOTE_AVERAGE + " TEXT" +
            " );";
    private static final String SQL_DELETE_FAVORITE_TABLE=
            "DROP TABLE IF EXISTS " + MovieContruct.Favorite.TABLE_NAME;
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE_FAVORITE_TABLE);
        }catch (SQLException e){
            Log.e(TAG + " DATABASE CREATE",e.getMessage());
            Toast.makeText(context, "Sorry there is Error in create your database ", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(SQL_DELETE_FAVORITE_TABLE);
            onCreate(db);
        }catch (SQLException e){
            Log.e(TAG + " DATABASE DELETE",e.getMessage());
            Toast.makeText(context, "Sorry there is Error in delete your database ", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean insertFavorite(MovieModel movieModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("movie_id", movieModel.getMovieId());
        values.put("poster_path", movieModel.getMoviePostarPath());
        values.put("overview", movieModel.getMovieOverview());
        values.put("release_date", movieModel.getMovieReleaseDate());
        values.put("title", movieModel.getMovieTitle());
        values.put("vote_average", movieModel.getMovieVoteAverage());
        long insert = sqLiteDatabase.insert(MovieContruct.Favorite.TABLE_NAME, null, values);
        if (insert > -1)
            return true;
        else
            return false;
    }
    public long insertFavorite(ContentValues values) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long insert = sqLiteDatabase.insert(MovieContruct.Favorite.TABLE_NAME, null, values);
            return insert;
    }
    public int  deleteFavorite(String selection,String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int delete = sqLiteDatabase.delete(MovieContruct.Favorite.TABLE_NAME, selection, selectionArgs);
        return delete;
    }
    public ArrayList<MovieModel> getMoiveData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c=sqLiteDatabase.query(MovieContruct.Favorite.TABLE_NAME, null, null, null, null, null, null);
        ArrayList<MovieModel> movieModels = new ArrayList<MovieModel>();
        MovieModel model = new MovieModel();
        while (c.moveToFirst()){
            model = new MovieModel();
            model.setMovieId(c.getInt(0));
            model.setMoviePostarPath(c.getString(1));
            model.setMovieOverview(c.getString(2));
            model.setMovieReleaseDate(c.getString(3));
            model.setMovieTitle(c.getString(4));
            model.setMovieVoteAverage(c.getString(5));
            movieModels.add(model);
        }

    return movieModels;
    }
    public Cursor queryMoiveData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c=sqLiteDatabase.query(MovieContruct.Favorite.TABLE_NAME, null, null, null, null, null, null);
    return c;
    }
    public Cursor queryMoiveData(String selection, String[] selectionArgs){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c=sqLiteDatabase.query(MovieContruct.Favorite.TABLE_NAME, null, selection, selectionArgs, null, null, null);
    return c;
    }
    public boolean checkFavoritFilm(int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor c=sqLiteDatabase.query(MovieContruct.Favorite.TABLE_NAME,null,"movie_id = ?", new String[]{id + ""},null,null,null);
        return c.moveToFirst();
    }

}
