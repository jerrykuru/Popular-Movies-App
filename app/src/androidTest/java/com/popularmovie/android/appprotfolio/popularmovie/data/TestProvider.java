package com.popularmovie.android.appprotfolio.popularmovie.data;


import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public class TestProvider extends AndroidTestCase {
    public static final String LOG_TAG = TestProvider.class.getSimpleName();


    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }


    /*
        This test checks to make sure that the content provider is registered correctly.
     */
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();
        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(), MovieProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: MovieProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(),
                    false);
        }


    }

    /*
            This test doesn't touch the database.  It verifies that the ContentProvider returns
            the correct type for each type of URI that it can handle.
         */
    public void testGetType() {

        // content://com.popularmovie.android.appprotfolio.popularmovie/movie/highestRated
        String type = mContext.getContentResolver().getType(
                MovieContract.MovieEntry.buildListOfHighestRatedMovies());
        // vnd.android.cursor.dir/com.popularmovie.android.appprotfolio.popularmovie/movie
        assertEquals("Error: the MovieEntry CONTENT_URI with highestRated should return  MovieContract.MovieEntry.CONTENT_TYPE",
                MovieContract.MovieEntry.CONTENT_TYPE, type);


        // content://com.popularmovie.android.appprotfolio.popularmovie/movie/popular
        type = mContext.getContentResolver().getType(
                MovieContract.MovieEntry.buildListOfPopularMovies());

        // vnd.android.cursor.dir/com.popularmovie.android.appprotfolio.popularmovie/movie
        assertEquals("Error: the MovieEntry CONTENT_URI with popular should return  MovieContract.MovieEntry.CONTENT_TYPE",
                MovieContract.MovieEntry.CONTENT_TYPE, type);


        long movie_id = 1234;
        // content://com.popularmovie.android.appprotfolio.popularmovie/movie/{id}
        type = mContext.getContentResolver().getType(
                MovieContract.MovieEntry.buildMovieUri(movie_id));
        // vnd.android.cursor.dir/com.popularmovie.android.appprotfolio.popularmovie/movie
        assertEquals("Error: the MovieEntry CONTENT_URI Movie Details should return  MovieContract.MovieEntry.CONTENT_ITEM_TYPE",
                MovieContract.MovieEntry.CONTENT_ITEM_TYPE, type);


        // content://com.popularmovie.android.appprotfolio.popularmovie/movie/favourite
        type = mContext.getContentResolver().getType(
                MovieContract.MovieEntry.buildListFavouriteMoviesUri());
        // vnd.android.cursor.dir/com.popularmovie.android.appprotfolio.popularmovie/movie
        assertEquals("Error: the MovieEntry CONTENT_URI with favourite list  should return  MovieContract.MovieEntry.CONTENT_TYPE",
                MovieContract.MovieEntry.CONTENT_TYPE, type);


        // content://com.popularmovie.android.appprotfolio.popularmovie/movie/favourite/add/{id}
        type = mContext.getContentResolver().getType(
                MovieContract.MovieEntry.buildAddFavouriteMoviesUri(movie_id));

        // vnd.android.cursor.dir/com.popularmovie.android.appprotfolio.popularmovie/movie
        assertEquals("Error: the MovieEntry CONTENT_URI with Add Favourite should return  MovieContract.MovieEntry.CONTENT_ITEM_TYPE",
                MovieContract.MovieEntry.CONTENT_ITEM_TYPE, type);


        // content://com.popularmovie.android.appprotfolio.popularmovie/review/{id}
        type = mContext.getContentResolver().getType(
                MovieContract.MovieReviewEntry.buildListMovieReviewURI(movie_id));

        // vnd.android.cursor.dir/com.popularmovie.android.appprotfolio.popularmovie/review
        assertEquals("Error: the MovieEntry CONTENT_URI with MOVIE review list  should return  MovieContract.MovieEntry.CONTENT_TYPE",
                MovieContract.MovieEntry.CONTENT_TYPE, type);


        // content://com.popularmovie.android.appprotfolio.popularmovie/trailer/{id}
        type = mContext.getContentResolver().getType(
                MovieContract.MovieTrailerEntry.buildMovieTrailerUri(movie_id));

        // vnd.android.cursor.dir/com.popularmovie.android.appprotfolio.popularmovie/trailer
        assertEquals("Error: the MovieEntry CONTENT_URI with MOVIE TRAILER list  should return  MovieContract.MovieEntry.CONTENT_TYPE",
                MovieContract.MovieEntry.CONTENT_TYPE, type);


    }

    /*
           This test uses the database directly to insert and then uses the ContentProvider to
           read out the data.
    */
    public void testHighestRatedMoviesMovieQuery() {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createMovieValues();
        String selection = null;
        if (null == selection) selection = "1";
        db.delete(
                MovieContract.MovieEntry.TABLE_NAME, selection, null);

        long movieID = insertMovieValues(mContext);

        assertTrue("Unable to Insert MovieEntry into the Database", movieID != -1);

        db.close();

        // Test the basic content provider query
        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.buildListOfHighestRatedMovies(),
                null,
                null,
                null,
                null
        );
        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testHighestRatedMoviesMovieQuery", movieCursor, testValues);
    }


    /*
      This test uses the database directly to insert and then uses the ContentProvider to
      read out the data.
*/
    public void testPopularMoviesQuery() {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createPopularMovieValues();
        String selection = null;
        if (null == selection) selection = "1";
        db.delete(
                MovieContract.MovieEntry.TABLE_NAME, selection, null);

        long movieID = insertPopularMovieValues(mContext);

        assertTrue("Unable to Insert MovieEntry into the Database", movieID != -1);

        db.close();

        // Test the basic content provider query
        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.buildListOfPopularMovies(),
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testPopularMoviesQuery", movieCursor, testValues);
    }


    /*
           This test uses the database directly to insert and then uses the ContentProvider to
           read out the data.
    */
    public void testBasicMovieReviewQuery() {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createMovieReviewValues();
        long movieReviewID = insertMovieReviewValues(mContext);

        assertTrue("Unable to Insert MovieReviewEntry into the Database", movieReviewID != -1);

        db.close();

        // Test the basic content provider query
        Cursor movieReviewCursor = mContext.getContentResolver().query(
                MovieContract.MovieReviewEntry.buildListMovieReviewURI(1),
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicMovieReviewQuery", movieReviewCursor, testValues);
    }

    /*
             This test uses the database directly to insert and then uses the ContentProvider to
             read out the data.
      */
    public void testBasicMovieTrailerQuery() {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createMovieTrailerValues();
        long movieTrailerID = insertMovieTrailerValues(mContext);

        assertTrue("Unable to Insert MovieTrailerEntry into the Database", movieTrailerID != -1);

        db.close();

        // Test the basic content provider query
        Cursor movieTrailerCursor = mContext.getContentResolver().query(
                MovieContract.MovieTrailerEntry.buildMovieTrailerUri(1),
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicMovieTrailerQuery", movieTrailerCursor, testValues);
    }

    static long insertMovieValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createMovieValues();

        long locationRowId;
        locationRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert MovieEntry Values", locationRowId != -1);
        db.close();
        return locationRowId;
    }

    static long insertPopularMovieValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createPopularMovieValues();

        long locationRowId;
        locationRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert MovieEntry Values", locationRowId != -1);
        db.close();
        return locationRowId;
    }


    static long insertMovieReviewValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createMovieReviewValues();
        ContentValues testMovieValues = TestUtilities.createMovieValues();

        long movieId;
        movieId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testMovieValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert MovieEntry  in nsertMovieReviewValues Values", movieId != -1);

        long locationRowId;
        locationRowId = db.insert(MovieContract.MovieReviewEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert MovieReviewEntry Values", locationRowId != -1);
        db.close();
        return locationRowId;
    }


    static long insertMovieTrailerValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createMovieTrailerValues();
        ContentValues testMovieValues = TestUtilities.createMovieValues();

        long movieId;
        String selection = null;
        if (null == selection) selection = "1";
        db.delete(
                MovieContract.MovieEntry.TABLE_NAME, selection, null);
        movieId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testMovieValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert MovieEntry  in insertMovieTrailerValues Values", movieId != -1);

        long trailerId;
        trailerId = db.insert(MovieContract.MovieTrailerEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert MovieTrailerEntry Values", trailerId != -1);
        db.close();
        return trailerId;
    }
}
