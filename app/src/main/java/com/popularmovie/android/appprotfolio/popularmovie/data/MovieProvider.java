package com.popularmovie.android.appprotfolio.popularmovie.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;


public class MovieProvider extends ContentProvider {
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;
    private static final String LOG_TAG = MovieProvider.class.getSimpleName();

    static final int MOVIE = 100;
    static final int MOVIE_HIGHEST_RATED = 101;
    static final int MOVIE_MOST_POPULAR = 102;
    static final int MOVIE_DETAILS = 103;
    static final int MOVIE_LIST_FAVOURITE = 104;
    static final int MOVIE_ADD_FAVOURITE = 105;
    static final int MOVIE_REVIEW = 106;
    static final int MOVIE_TRAILER = 107;
    static final String CONSTANT_MOVIE_FAVOURITE = "true";
    static final String CONSTANT_MOVIE_POPULAR = "true";
    static final String CONSTANT_MOVIE_HIGHESTRATED = "true";

    private static final SQLiteQueryBuilder sMovieQueryBuilder;
    private static final SQLiteQueryBuilder sMovieReviewQueryBuilder;
    private static final SQLiteQueryBuilder sMovieTrailerQueryBuilder;


    static {
        sMovieQueryBuilder = new SQLiteQueryBuilder();
        sMovieReviewQueryBuilder = new SQLiteQueryBuilder();
        sMovieTrailerQueryBuilder = new SQLiteQueryBuilder();
        sMovieQueryBuilder.setTables(MovieContract.MovieEntry.TABLE_NAME);
        sMovieTrailerQueryBuilder.setTables(MovieContract.MovieTrailerEntry.TABLE_NAME + " INNER JOIN " +
                MovieContract.MovieEntry.TABLE_NAME +
                " ON " + MovieContract.MovieTrailerEntry.TABLE_NAME +
                "." + MovieContract.MovieTrailerEntry.COLUMN_MOVIE_KEY +
                " = " + MovieContract.MovieEntry.TABLE_NAME +
                "." + MovieContract.MovieEntry._ID +
                " And " + MovieContract.MovieTrailerEntry.TABLE_NAME +
                "." + MovieContract.MovieTrailerEntry.COLUMN_MOVIE_KEY + " = ?");
        sMovieReviewQueryBuilder.setTables(MovieContract.MovieReviewEntry.TABLE_NAME + " INNER JOIN " +
                MovieContract.MovieEntry.TABLE_NAME +
                " ON " + MovieContract.MovieReviewEntry.TABLE_NAME +
                "." + MovieContract.MovieReviewEntry.COLUMN_MOVIE_KEY +
                " = " + MovieContract.MovieEntry.TABLE_NAME +
                "." + MovieContract.MovieEntry._ID +
                " And " + MovieContract.MovieReviewEntry.TABLE_NAME +
                "." + MovieContract.MovieReviewEntry.COLUMN_MOVIE_KEY + " = ?");
    }

    //select Popular Movies
    private static final String sMovieSelectionByPopular =
            MovieContract.MovieEntry.TABLE_NAME +
                    "." + MovieContract.MovieEntry.COLUMN_POPULAR + " = ? ";

    //select Highest Rated Movies
    private static final String sMovieSelectionByHighestRated =
            MovieContract.MovieEntry.TABLE_NAME +
                    "." + MovieContract.MovieEntry.COLUMN_HIGHESTRATED + " = ? ";

    //select Favourite  Movies
    private static final String sMovieSelectionByFavouriteMovies =
            MovieContract.MovieEntry.TABLE_NAME +
                    "." + MovieContract.MovieEntry.COLUMN_FAVORITE_FLAG + " = ? ";

    //select by Movie ID
    private static final String sMovieSelectionByMovieId =
            MovieContract.MovieEntry.TABLE_NAME +
                    "." + MovieContract.MovieEntry._ID + " = ? ";

    //select by REVIEW_ID
    private static final String sMovieReviewSelectionByReviewId =
            MovieContract.MovieReviewEntry.TABLE_NAME +
                    "." +MovieContract.MovieReviewEntry._ID + " = ? ";

    //select by TRAILER_ID
    private static final String sMovieSelectionByTrailerId =
            MovieContract.MovieTrailerEntry.TABLE_NAME +
                    "." + MovieContract.MovieTrailerEntry._ID + " = ? ";


    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_HIGHEST_RATED: {
                retCursor = getHighestRatedMovies(uri, projection, sortOrder);
                break;
            }
            case MOVIE_MOST_POPULAR: {
                retCursor = getPopularMovies(uri, projection, sortOrder);
                break;
            }
            case MOVIE_DETAILS: {
                Log.d(LOG_TAG, "Movie Details");
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        sMovieSelectionByMovieId,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                Log.d(LOG_TAG, "Count =" + retCursor.getCount());
                break;
            }
            case MOVIE_LIST_FAVOURITE: {
                retCursor = getFavouriteMovies(uri, projection, sortOrder);
                break;
            }
            case MOVIE_REVIEW: {
                retCursor = getMovieReviewsByMovieId(uri, projection, sortOrder);
                break;
            }
            case MOVIE_TRAILER: {
                retCursor = getMovieTrailersByMovieId(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

    }


    private Cursor getHighestRatedMovies(Uri uri, String[] projection, String sortOrder) {

        return sMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMovieSelectionByHighestRated,
                new String[]{CONSTANT_MOVIE_HIGHESTRATED},
                null,
                null,
                sortOrder
        );
    }


    private Cursor getFavouriteMovies(Uri uri, String[] projection, String sortOrder) {

        return sMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMovieSelectionByFavouriteMovies,
                new String[]{CONSTANT_MOVIE_FAVOURITE},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getPopularMovies(Uri uri, String[] projection, String sortOrder) {

        return sMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMovieSelectionByPopular,
                new String[]{CONSTANT_MOVIE_POPULAR},
                null,
                null,
                sortOrder
        );
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE_HIGHEST_RATED:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_MOST_POPULAR:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_DETAILS:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case MOVIE_LIST_FAVOURITE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_ADD_FAVOURITE:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case MOVIE_REVIEW:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_TRAILER:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    // content://com.popularmovie.android.appprotfolio.popularmovie/review/{id}
    private Cursor getMovieReviewsByMovieId(Uri uri, String[] projection, String sortOrder) {
        String movieId = MovieContract.MovieReviewEntry.getMovieIdFromUri(uri);
        String[] listOfArg = new String[]{movieId};

        return sMovieReviewQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                null,
                listOfArg,
                null,
                null,
                sortOrder
        );
    }


    // content://com.popularmovie.android.appprotfolio.popularmovie/review/{id}
    private Cursor getMovieTrailersByMovieId(Uri uri, String[] projection, String sortOrder) {
        String movieId = MovieContract.MovieTrailerEntry.getMovieIdFromUri(uri);

        return sMovieTrailerQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                null,
                new String[]{movieId},
                null,
                null,
                sortOrder
        );
    }


    static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        // content://com.popularmovie.android.appprotfolio.popularmovie/movie/highestRated
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/highestRated", MOVIE_HIGHEST_RATED);
        // content://com.popularmovie.android.appprotfolio.popularmovie/movie/popular
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/popular", MOVIE_MOST_POPULAR);
        // content://com.popularmovie.android.appprotfolio.popularmovie/movie/{id}
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", MOVIE_DETAILS);
        // content://com.popularmovie.android.appprotfolio.popularmovie/movie/favourite
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/favourite", MOVIE_LIST_FAVOURITE);
        // content://com.popularmovie.android.appprotfolio.popularmovie/review/{id}
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/#", MOVIE_REVIEW);
        // content://com.popularmovie.android.appprotfolio.popularmovie/trailer/{id}
        matcher.addURI(authority, MovieContract.PATH_TRAILER + "/#", MOVIE_TRAILER);
        // content://com.popularmovie.android.appprotfolio.popularmovie/movie/favourite/add/{id}
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/favourite/add/#", MOVIE_ADD_FAVOURITE);
        // content://com.popularmovie.android.appprotfolio.popularmovie/movie/highestRated
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_TRAILER, MOVIE_TRAILER);
        matcher.addURI(authority, MovieContract.PATH_REVIEW, MOVIE_REVIEW);

        return matcher;
    }


    /*
    Student: Add the ability to insert Locations to the implementation of this function.
 */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOVIE_REVIEW: {
                long _id = db.insert(MovieContract.MovieReviewEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.MovieReviewEntry.buildListMovieReviewURI(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOVIE_TRAILER: {
                long _id = db.insert(MovieContract.MovieTrailerEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.MovieTrailerEntry.buildMovieTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_REVIEW:
                rowsDeleted = db.delete(
                        MovieContract.MovieReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_TRAILER:
                rowsDeleted = db.delete(
                        MovieContract.MovieTrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MOVIE_REVIEW:
                rowsUpdated = db.update(MovieContract.MovieReviewEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MOVIE_TRAILER:
                rowsUpdated = db.update(MovieContract.MovieTrailerEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;

        switch (match) {
            case MOVIE:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        String movieId = value.getAsString(MovieContract.MovieEntry._ID);
                        String[] selection = new String[]{movieId};
                        Cursor isMoviePresentCusor = mOpenHelper.getReadableDatabase().query(
                                MovieContract.MovieEntry.TABLE_NAME,
                                null,
                                sMovieSelectionByMovieId,
                                selection,
                                null,
                                null,
                                null
                        );
                        if (isMoviePresentCusor.getCount() == 0) {
                            long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }

                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case MOVIE_REVIEW:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        String reviewId = value.getAsString(MovieContract.MovieReviewEntry._ID);
                        String[] selection = new String[]{reviewId};
                        Cursor isMovieReviewPresentCusor = mOpenHelper.getReadableDatabase().query(
                                MovieContract.MovieReviewEntry.TABLE_NAME,
                                null,
                                sMovieReviewSelectionByReviewId,
                                selection,
                                null,
                                null,
                                null
                        );
                        if (isMovieReviewPresentCusor.getCount() == 0) {
                            long _id = db.insert(MovieContract.MovieReviewEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case MOVIE_TRAILER:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        String trailerId = value.getAsString(MovieContract.MovieTrailerEntry._ID);
                        String[] selection = new String[]{trailerId};
                        Cursor isMovieTrailerPresentCusor = mOpenHelper.getReadableDatabase().query(
                                MovieContract.MovieTrailerEntry.TABLE_NAME,
                                null,
                                sMovieSelectionByTrailerId,
                                selection,
                                null,
                                null,
                                null
                        );
                        if (isMovieTrailerPresentCusor.getCount() == 0) {

                            long _id = db.insert(MovieContract.MovieTrailerEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }


    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}
