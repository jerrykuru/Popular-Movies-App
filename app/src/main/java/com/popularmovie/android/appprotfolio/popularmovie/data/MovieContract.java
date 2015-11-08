package com.popularmovie.android.appprotfolio.popularmovie.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the Movie database.
 */
public class MovieContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.popularmovie.android.appprotfolio.popularmovie";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_TRAILER = "trailer";
    public static final String MOVIE_HIGHEST_RATED = "highestRated";
    public static final String MOVIE_MOST_POPULAR = "popular";
    public static final String MOVIE_FAVOURITE = "favourite";
    public static final String MOVIE_ADD_FAVOURITE = "add";

    /* Inner class that defines the table contents of the  movie  table */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";

        // movie tile
        public static final String COLUMN_TITLE = "title";

        // Date, stored as String [yyyy-mm-dd]  format
        public static final String COLUMN_RELEASE_DATE = "date";

        // movie poster path
        public static final String COLUMN_POSTER_PATH = "poster_path";

        // movie poster path
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        // movie overview
        public static final String COLUMN_OVERVIEW = "overview";

        // movie overview
        public static final String COLUMN_HIGHESTRATED = "isHighestRated";

        // movie overview
        public static final String COLUMN_POPULAR = "popular";

        // movie favorite flag
        public static final String COLUMN_FAVORITE_FLAG = "favorite_flag";

        public static Uri buildListOfPopularMovies() {
            return CONTENT_URI.buildUpon().appendPath(MOVIE_MOST_POPULAR).build();
        }

        public static Uri buildListOfHighestRatedMovies() {
            return CONTENT_URI.buildUpon().appendPath(MOVIE_HIGHEST_RATED).build();
        }



        public static Uri buildListFavouriteMoviesUri() {
            return CONTENT_URI.buildUpon().appendPath(MOVIE_FAVOURITE).build();
        }

        public static Uri buildAddFavouriteMoviesUri(long movieID) {
           return CONTENT_URI.buildUpon().appendPath(MOVIE_FAVOURITE).appendPath(MOVIE_ADD_FAVOURITE).appendPath(Math.round(movieID)+"").build();
        }

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    /* Inner class that defines the table contents of the  movie review table */
    public static final class MovieReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static final String TABLE_NAME = "review";

        // Column with the foreign key into the Movie table.
        public static final String COLUMN_MOVIE_KEY = "movie_id";

        // movie review Author
        public static final String COLUMN_AUTHOR = "author";

        // movie review content
        public static final String COLUMN_CONTENT = "content";

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildListMovieReviewURI(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the  movie review table */
    public static final class MovieTrailerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        public static final String TABLE_NAME = "trailer";

        // Column with the foreign key into the Movie table.
        public static final String COLUMN_MOVIE_KEY = "movie_id";

        // Trailer ISO
        public static final String COLUMN_ISO = "iso_639_1";

        // movie Trailer youtube key
        public static final String COLUMN_YOUTUBE_KEY = "key";
        public static final String COLUMN_YOUTUBE_NAME = "name";
        public static final String COLUMN_YOUTUBE_SITE = "site";
        public static final String COLUMN_YOUTUBE_SIZE = "size";
        public static final String COLUMN_YOUTUBE_TYPE = "type";
        public static final String COLUMN_COUNT = "count";


        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getYouTubeKeyFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildMovieTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieTrailerWithYouTube(String youTube){
           return  BASE_CONTENT_URI.buildUpon().appendPath(youTube).build();
        }

    }

}
