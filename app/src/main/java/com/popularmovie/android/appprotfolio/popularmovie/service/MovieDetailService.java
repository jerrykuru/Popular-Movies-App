package com.popularmovie.android.appprotfolio.popularmovie.service;


import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.popularmovie.android.appprotfolio.popularmovie.HttpUtility;
import com.popularmovie.android.appprotfolio.popularmovie.MovieConstants;
import com.popularmovie.android.appprotfolio.popularmovie.MovieReviews;
import com.popularmovie.android.appprotfolio.popularmovie.MovieTrailer;
import com.popularmovie.android.appprotfolio.popularmovie.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MovieDetailService extends IntentService {

    public MovieDetailService() {
        super("MovieDetailService");
    }

    public static final String MOVIE_DETAIL_ID = "mdi";
    private final String LOG_TAG = MovieDetailService.class.getSimpleName();
    private static final String MOVIE_TRAILER_REVIEWS_URL = "http://api.themoviedb.org/3/movie/";
    private static final String MOVIE_TRAILER_URI_VIDEOS = "videos?";
    private static final String MOVIE_TRAILER_URI_REVIEWS = "reviews?";
    private static final String API_KEY = "api_key";


    @Override
    protected void onHandleIntent(Intent intent) {
        String movieID = intent.getStringExtra(MOVIE_DETAIL_ID);
    }

    //    Trailer - http://api.themoviedb.org/3/movie/10751/videos?api_key=
   //    Reviews - http://api.themoviedb.org/3/movie/135397/reviews?api_key=
    private void getMovieTrailerForMovieId(String movieId) {
        String jsonStr = new HttpUtility().talkToOutsideWorld(buildUrlMoviesTrailer(movieId));
        addMovieTrailerToDatabase(getMovieTrailerFromJson(jsonStr, movieId));
    }

    private void getMovieReviewsForMovieId(String movieId) {
        String jsonStr = new HttpUtility().talkToOutsideWorld(buildUrlMoviesReviews(movieId));
        addMovieReviewToDatabase(getMovieReviewsFromJson(jsonStr, movieId));
    }

    /**
     * movie Reviews
     * <p>
     * URL:  http://api.themoviedb.org/3/movie/10751/reviews?api_key=
     *
     * @param movieId
     * @return
     */
    private URL buildUrlMoviesReviews(String movieId) {

        URL url = null;
        try {

            Uri builtUri = Uri.parse(MOVIE_TRAILER_REVIEWS_URL).buildUpon()
                    .appendPath(movieId)
                    .appendPath(MOVIE_TRAILER_URI_REVIEWS)
                    .appendQueryParameter(API_KEY, MovieConstants.ApiKey)
                    .build();
            url = new URL(builtUri.toString());
            Log.v(LOG_TAG, builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        }
        return url;
    }


    /**
     * movie Trailer
     * <p>
     * URL:  http://api.themoviedb.org/3/movie/10751/videos?api_key=
     *
     * @param movieId
     * @return
     */
    private URL buildUrlMoviesTrailer(String movieId) {

        URL url = null;
        try {

            Uri builtUri = Uri.parse(MOVIE_TRAILER_REVIEWS_URL).buildUpon()
                    .appendPath(movieId)
                    .appendPath(MOVIE_TRAILER_URI_VIDEOS)
                    .appendQueryParameter(API_KEY, MovieConstants.ApiKey)
                    .build();
            url = new URL(builtUri.toString());
            Log.v(LOG_TAG, builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        }
        return url;
    }


    /**
     * Take the String representing the complete Movie Trailer in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     * <p>
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private List<MovieTrailer> getMovieTrailerFromJson(String jsonStr, String movieId) {

        List<MovieTrailer> listOfMovie = new ArrayList<MovieTrailer>();
        try {
            if (jsonStr != null) {
                // These are the names of the JSON objects that need to be extracted.
                final String MOVIE_TRAILER_LIST = "results";
                final String MOVIE_TRAILER_ISO = "iso_639_1";
                final String MOVIE_TRAILER_KEY = "key";
                final String MOVIE_TRAILER_NAME = "name";
                final String MOVIE_TRAILER_SITE = "site";
                final String MOVIE_TRAILER_SIZE = "size";
                final String MOVIE_TRAILER_TYPE = "type";
                final String MOVIE_TRAILER_ID = "id";


                /**
                 * Sample Response
                 * --------------------
                 * "id": "54282ba2c3a3680b1b002905",
                 "iso_639_1": "en",
                 "key": "NuUnJ5WL1g",
                 "name": "Trailer",
                 "site": "YouTube",
                 "size": 360,
                 "type": "Trailer"
                 */

                JSONObject movieJson = new JSONObject(jsonStr);
                JSONArray movieArray = movieJson.getJSONArray(MOVIE_TRAILER_LIST);

                for (int i = 0; i < movieArray.length(); i++) {
                    // Get the JSON object representing a Movie
                    JSONObject movieJSONObject = movieArray.getJSONObject(i);
                    MovieTrailer movieTrailer = new MovieTrailer();
                    movieTrailer.setMovieTrailerID(movieJSONObject.getString(MOVIE_TRAILER_ID));
                    movieTrailer.setIso_639_1(movieJSONObject.getString(MOVIE_TRAILER_ISO));
                    movieTrailer.setKey(movieJSONObject.getString(MOVIE_TRAILER_KEY));
                    movieTrailer.setName(movieJSONObject.getString(MOVIE_TRAILER_NAME));
                    movieTrailer.setSite(movieJSONObject.getString(MOVIE_TRAILER_SITE));
                    movieTrailer.setSize(movieJSONObject.getInt(MOVIE_TRAILER_SIZE) + "");
                    movieTrailer.setType(movieJSONObject.getString(MOVIE_TRAILER_TYPE));
                    movieTrailer.setMovieId(movieId);
                    listOfMovie.add(movieTrailer);
                }

                for (MovieTrailer s : listOfMovie) {
                    Log.v(LOG_TAG, "Movie Trailer Id: " + s.getMovieTrailerID());
                    Log.v(LOG_TAG, "Movie Id: " + s.getMovieId());
                    Log.v(LOG_TAG, "Movie Trailer Key: " + s.getKey());
                }
            } else {
                Log.e("Error No JSON", "listMovie Trailer JsonStr is Null");
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON Exception" + e);
        }
        return listOfMovie;

    }

    /**
     * Take the String representing the complete Movie Reviews in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     * <p>
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private List<MovieReviews> getMovieReviewsFromJson(String jsonStr, String movieId) {

        List<MovieReviews> listOfMovie = new ArrayList<MovieReviews>();
        try {
            if (jsonStr != null) {
                // These are the names of the JSON objects that need to be extracted.
                final String MOVIE_REVIEWS_LIST = "results";
                final String MOVIE_REVIEWS_AUTHOR = "author";
                final String MOVIE_REVIEWS_CONTENT = "content";
                final String MOVIE_REVIEWS_URL = "url";
                final String MOVIE_REVIEWS_ID = "id";


                /**
                 * Sample Response
                 * --------------------
                 *  "id": "55910381c3a36807f900065d",
                 "author": "jonlikesmoviesthatdontsuck",
                 "content": "I was a huge fan of the original 3 movies
                 "url": "http://j.mp/1GHgSxi"
                 */

                JSONObject movieJson = new JSONObject(jsonStr);
                JSONArray movieArray = movieJson.getJSONArray(MOVIE_REVIEWS_LIST);

                for (int i = 0; i < movieArray.length(); i++) {
                    // Get the JSON object representing a Movie
                    JSONObject movieJSONObject = movieArray.getJSONObject(i);
                    MovieReviews movieReviews = new MovieReviews();
                    movieReviews.setMovieReviewId(movieJSONObject.getString(MOVIE_REVIEWS_ID));
                    movieReviews.setAuthor(movieJSONObject.getString(MOVIE_REVIEWS_AUTHOR));
                    movieReviews.setContent(movieJSONObject.getString(MOVIE_REVIEWS_CONTENT));
                    movieReviews.setMovieReviewUri(movieJSONObject.getString(MOVIE_REVIEWS_URL));
                    movieReviews.setMovieId(movieId);
                    listOfMovie.add(movieReviews);
                }

                for (MovieReviews s : listOfMovie) {
                    Log.v(LOG_TAG, "Movie Trailer Id: " + s.getMovieReviewId());
                    Log.v(LOG_TAG, "Movie Id: " + s.getMovieId());
                    Log.v(LOG_TAG, "Movie Review Author Key: " + s.getAuthor());
                }
            } else {
                Log.e("Error No JSON", "listMovie Review JsonStr is Null");
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON Exception" + e);
        }
        return listOfMovie;

    }


    private void addMovieReviewToDatabase(List<MovieReviews> listOfMovie) {

        // Insert the new movie review information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(listOfMovie.size());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        for (MovieReviews s : listOfMovie) {
            Log.e(LOG_TAG, s.getMovieReviewId());
            Log.e(LOG_TAG, s.getMovieId());
            Log.e(LOG_TAG, s.getAuthor());
            Log.e(LOG_TAG, s.getContent());
            ContentValues testValues = new ContentValues();
            testValues.put(MovieContract.MovieReviewEntry._ID, s.getMovieReviewId());
            testValues.put(MovieContract.MovieReviewEntry.COLUMN_MOVIE_KEY, s.getMovieId());
            testValues.put(MovieContract.MovieReviewEntry.COLUMN_AUTHOR, s.getAuthor());
            testValues.put(MovieContract.MovieReviewEntry.COLUMN_CONTENT, s.getContent());
            cVVector.add(testValues);
        }


        int inserted = 0;
        // add to database
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            this.getContentResolver().bulkInsert(MovieContract.MovieReviewEntry.CONTENT_URI, cvArray);
        }

        Log.d(LOG_TAG, "Movie Detail Review Service Complete. " + cVVector.size() + " Inserted");
    }

    private void addMovieTrailerToDatabase(List<MovieTrailer> listOfMovie) {

        // Insert the new movie review information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(listOfMovie.size());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");


        for (MovieTrailer s : listOfMovie) {
            Log.e(LOG_TAG, s.getMovieTrailerID());
            Log.e(LOG_TAG, s.getMovieId());
            Log.e(LOG_TAG, s.getKey());
            Log.e(LOG_TAG, s.getSite());
            ContentValues testValues = new ContentValues();
            testValues.put(MovieContract.MovieTrailerEntry._ID, s.getMovieTrailerID());
            testValues.put(MovieContract.MovieTrailerEntry.COLUMN_MOVIE_KEY, s.getMovieId());
            testValues.put(MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_KEY, s.getKey());
            testValues.put(MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_SITE, s.getSite());
            testValues.put(MovieContract.MovieTrailerEntry.COLUMN_ISO, s.getIso_639_1());
            testValues.put(MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_NAME, s.getName());
            testValues.put(MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_SIZE, s.getSize());
            testValues.put(MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_TYPE, s.getType());
            cVVector.add(testValues);
        }


        int inserted = 0;
        // add to database
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            this.getContentResolver().bulkInsert(MovieContract.MovieTrailerEntry.CONTENT_URI, cvArray);
        }

        Log.d(LOG_TAG, "Movie MovieTrailerEntry Service Complete. " + cVVector.size() + " Inserted");
    }

}
