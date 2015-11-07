package com.popularmovie.android.appprotfolio.popularmovie.service;


import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.popularmovie.android.appprotfolio.popularmovie.HttpUtility;
import com.popularmovie.android.appprotfolio.popularmovie.Movie;
import com.popularmovie.android.appprotfolio.popularmovie.MovieConstants;
import com.popularmovie.android.appprotfolio.popularmovie.MovieSelection;
import com.popularmovie.android.appprotfolio.popularmovie.SelectionType;
import com.popularmovie.android.appprotfolio.popularmovie.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class MovieService extends IntentService {

    public static final String MOVIE_SELECTION_TYPE = "mst";
    private final String LOG_TAG = MovieService.class.getSimpleName();
    private static final String DISCOVER_MOVIE_URL = "http://api.themoviedb.org/3/discover/movie?";
    private static final String SORT_BY = "sort_by";
    private static final String API_KEY = "api_key";
    private static final String PAGE = "page";
    private static final String IMAGE_URI_PREFIX = "http://image.tmdb.org/t/p/w185/";


    public MovieService() {
        super("MovieService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String movieSortSelection = intent.getStringExtra(MOVIE_SELECTION_TYPE);
        // Will contain the raw JSON response as a string.
        String listMovieJsonStr = null;
        MovieSelection movieSelection = new MovieSelection();

        if (movieSortSelection != null) {
            URL url = null;
            if (movieSortSelection.equalsIgnoreCase(SelectionType.HighestRated.getSortType())) {
                url = buildUrlHighestRatedMovies(movieSelection.getPage());
                movieSelection.setSelectionType(SelectionType.HighestRated);
            }
            if (movieSortSelection.equalsIgnoreCase(SelectionType.Popular.getSortType())) {
                url = buildUrlPopularMovies(movieSelection.getPage());
                movieSelection.setSelectionType(SelectionType.Popular);
            }
            listMovieJsonStr = new HttpUtility().talkToOutsideWorld(url);
            addMovieToDatabase(getMovieListingFromJson(listMovieJsonStr), movieSelection);
        }

    }


    /**
     * What are the most popular movies?
     * <p>
     * URL: /discover/movie?sort_by=popularity.desc
     *
     * @param page
     * @return
     */
    private URL buildUrlPopularMovies(String page) {

        final String POPULARITY_DESC = "popularity.desc";
        URL url = null;
        try {

            Uri builtUri = Uri.parse(DISCOVER_MOVIE_URL).buildUpon()
                    .appendQueryParameter(SORT_BY, POPULARITY_DESC)
                    .appendQueryParameter(PAGE, page)
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
     * What are the highest rated movies rated R?
     * <p>
     * URL: /discover/movie/?certification_country=US&certification=R&sort_by=vote_average.desc
     *
     * @param page
     * @return
     */
    private URL buildUrlHighestRatedMovies(String page) {

        final String HIGHEST_RATED = "vote_average.desc";
        final String CERTIFICATION_COUNTRY = "certification_country";
        final String CERTIFICATION_COUNTRY_US = "US";
        URL url = null;
        try {
            Uri builtUri = Uri.parse(DISCOVER_MOVIE_URL).buildUpon()
                    .appendQueryParameter(CERTIFICATION_COUNTRY, CERTIFICATION_COUNTRY_US)
                    .appendQueryParameter(SORT_BY, HIGHEST_RATED)
                    .appendQueryParameter(PAGE, page)
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
     * Take the String representing the complete Movie Listing in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     * <p>
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private List<Movie> getMovieListingFromJson(String listMovieJsonStr) {

        List<Movie> listOfMovie = new ArrayList<Movie>();
        try {
            if (listMovieJsonStr != null) {
                // These are the names of the JSON objects that need to be extracted.
                final String MOVIE_LIST = "results";
                final String MOVIE_ID = "id";
                final String MOVIE_OVERVIEW = "overview";
                final String MOVIE_POSTER_PATH = "poster_path";
                final String MOVIE_RELEASE_DATE = "release_date";
                final String MOVIE_VOTE_AVERAGE = "vote_average";
                final String MOVIE_TITLE = "title";

                JSONObject movieJson = new JSONObject(listMovieJsonStr);
                JSONArray movieArray = movieJson.getJSONArray(MOVIE_LIST);

                for (int i = 0; i < movieArray.length(); i++) {
                    // Get the JSON object representing a Movie
                    JSONObject movieJSONObject = movieArray.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setId(movieJSONObject.getString(MOVIE_ID));
                    movie.setOverview(movieJSONObject.getString(MOVIE_OVERVIEW));
                    movie.setPoster_path(IMAGE_URI_PREFIX + movieJSONObject.getString(MOVIE_POSTER_PATH));
                    try {
                        Date movieReleaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(movieJSONObject.getString(MOVIE_RELEASE_DATE));
                        movie.setRelease_date(movieReleaseDate);
                    } catch (ParseException e) {
                        Log.e(LOG_TAG, "Date Conversion Exception: " + e);
                    }
                    movie.setVote_average(movieJSONObject.getDouble(MOVIE_VOTE_AVERAGE));
                    movie.setTitle(movieJSONObject.getString(MOVIE_TITLE));
                    listOfMovie.add(movie);
                }

            } else {
                Log.e("Error No JSON", "listMovieJsonStr is Null");
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON Exception" + e);
        }
        return listOfMovie;

    }

    private void addMovieToDatabase(List<Movie> listOfMovie, MovieSelection movieSelection) {

        // Insert the new movie information into the database
        Vector<ContentValues> cVVector = new Vector<ContentValues>(listOfMovie.size());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        for (Movie s : listOfMovie) {

            ContentValues testValues = new ContentValues();
            testValues.put(MovieContract.MovieEntry._ID, s.getId());
            testValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE_FLAG, movieSelection.getSelectionType() == SelectionType.Favourite ? "true" : "false");
            testValues.put(MovieContract.MovieEntry.COLUMN_HIGHESTRATED, movieSelection.getSelectionType() == SelectionType.HighestRated ? "true" : "false");
            testValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, s.getOverview());
            testValues.put(MovieContract.MovieEntry.COLUMN_POPULAR, movieSelection.getSelectionType() == SelectionType.Popular ? "true" : "false");
            testValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, s.getPoster_path());
            testValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, df.format(s.getRelease_date()));
            testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, s.getTitle());
            testValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, new Double(s.getVote_average()).toString());
            cVVector.add(testValues);
        }


        int inserted = 0;
        // add to database
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
           int numberOfRecordsInserted =  this.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
            Log.d(LOG_TAG, "Movie Service Complete. " + numberOfRecordsInserted + " Inserted");
        }


    }

}
