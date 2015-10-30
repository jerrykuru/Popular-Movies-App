package com.popularmovie.android.appprotfolio.popularmovie.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;


public class TestUtilities extends AndroidTestCase {

    static ContentValues createMovieValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE_FLAG, "false");
        testValues.put(MovieContract.MovieEntry.COLUMN_HIGHESTRATED, "true");
        testValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "This is testing");
        testValues.put(MovieContract.MovieEntry.COLUMN_POPULAR, "false");
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "/d/d/d/");
        testValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "2012-01-17");
        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Jerry is going to comeback");
        testValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, "7/10");
        testValues.put(MovieContract.MovieEntry._ID, "1");

        return testValues;
    }


    static ContentValues createPopularMovieValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE_FLAG, "true");
        testValues.put(MovieContract.MovieEntry.COLUMN_HIGHESTRATED, "false");
        testValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "This is testing");
        testValues.put(MovieContract.MovieEntry.COLUMN_POPULAR, "true");
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "/c/c/c/");
        testValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "2012-06-17");
        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Binu is going to comeback");
        testValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, "7/10");
        testValues.put(MovieContract.MovieEntry._ID, "2");

        return testValues;
    }

    static ContentValues createMovieReviewValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieReviewEntry.COLUMN_AUTHOR, "Elise");
        testValues.put(MovieContract.MovieReviewEntry.COLUMN_CONTENT, "The movie was fine");
        testValues.put(MovieContract.MovieReviewEntry.COLUMN_MOVIE_KEY, "1");
        testValues.put(MovieContract.MovieReviewEntry._ID, "1");

        return testValues;
    }

    static ContentValues createMovieTrailerValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieTrailerEntry.COLUMN_MOVIE_KEY, "1");
        testValues.put(MovieContract.MovieTrailerEntry.COLUMN_ISO, "ISO");
        testValues.put(MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_KEY, "youTubeKey");
        testValues.put(MovieContract.MovieTrailerEntry._ID, "1");
        testValues.put(MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_NAME, "YOUTUBE NAME");
        testValues.put(MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_SITE, "YOUTUBE SITE");
        testValues.put(MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_TYPE, "YOUTUBE Type");
        testValues.put(MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_SIZE, "YOUTUBE Size");
        return testValues;
    }




    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }
}