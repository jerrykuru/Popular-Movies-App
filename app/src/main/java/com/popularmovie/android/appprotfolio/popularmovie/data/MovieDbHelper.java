package com.popularmovie.android.appprotfolio.popularmovie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 3;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID + " TEXT NOT NULL PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_FAVORITE_FLAG + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_HIGHESTRATED + " TEXT, " +
                MovieContract.MovieEntry.COLUMN_POPULAR + " TEXT " +
                " );";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + MovieContract.MovieReviewEntry.TABLE_NAME + " (" +

                MovieContract.MovieReviewEntry._ID + " TEXT NOT NULL PRIMARY KEY, " +
                MovieContract.MovieReviewEntry.COLUMN_AUTHOR + " TEXT, " +
                MovieContract.MovieReviewEntry.COLUMN_CONTENT + " TEXT, " +
                MovieContract.MovieReviewEntry.COLUMN_MOVIE_KEY + " TEXT," +
                // Set up the movie id column as a foreign key to movie table.
                " FOREIGN KEY (" + MovieContract.MovieReviewEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry._ID + "));";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + MovieContract.MovieTrailerEntry.TABLE_NAME + " (" +

                MovieContract.MovieTrailerEntry._ID + " TEXT NOT NULL PRIMARY KEY, " +
                MovieContract.MovieTrailerEntry.COLUMN_ISO + " TEXT, " +
                MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_KEY + " TEXT, " +
                MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_NAME + " TEXT, " +
                MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_SITE + " TEXT, " +
                MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_SIZE + " TEXT, " +
                MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_TYPE + " TEXT, " +
                MovieContract.MovieTrailerEntry.COLUMN_MOVIE_KEY + " TEXT," +
                MovieContract.MovieTrailerEntry.COLUMN_COUNT + " TEXT, " +
                // Set up the movie id column as a foreign key to movie table.
                " FOREIGN KEY (" + MovieContract.MovieTrailerEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry._ID + "));";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
        db.execSQL(SQL_CREATE_TRAILER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieTrailerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);

    }
}
