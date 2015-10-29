package com.popularmovie.android.appprotfolio.popularmovie.data;

import android.content.ContentValues;
import android.test.AndroidTestCase;


public class TestUtilities extends AndroidTestCase {

    static ContentValues createMovieValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE_FLAG, "false");
        testValues.put(MovieContract.MovieEntry.COLUMN_HIGHESTRATED, "true");
        testValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "This is testing");
        testValues.put(MovieContract.MovieEntry.COLUMN_POPULAR, "false");
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "/d/d/d/");
        testValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "2012-05-17");
        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Jerry is going to comeback");
        testValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, "7/10");
        testValues.put(MovieContract.MovieEntry._ID, "1");

        return testValues;
    }



}