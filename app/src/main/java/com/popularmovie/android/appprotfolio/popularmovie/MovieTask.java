package com.popularmovie.android.appprotfolio.popularmovie;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MovieTask extends AsyncTask<MovieSelection, Void, List<Movie>> {

    private static final String LOG_TAG = MovieTask.class.getSimpleName();
    private static final String DISCOVER_MOVIE_URL = "http://api.themoviedb.org/3/discover/movie?";
    private static final String SORT_BY = "sort_by";
    private static final String API_KEY = "api_key";
    private static final String PAGE = "page";
    private static final String IMAGE_URI_PREFIX = "http://image.tmdb.org/t/p/w185/";
    private ImageAdapter imageAdapter;

    public MovieTask(ImageAdapter adapter){
        this.imageAdapter =  adapter;
    }

    /**
     * @param params
     * @return
     */
    @Override
    protected List<Movie> doInBackground(MovieSelection... params) {

        // Will contain the raw JSON response as a string.
        String listMovieJsonStr = null;
        MovieSelection movieSelection = params[0];
        if (movieSelection != null) {
            URL url = null;
            if (SelectionType.HighestRated == movieSelection.getSelectionType()) {
                url = buildUrlHighestRatedMovies(movieSelection.getPage());
            }
            if (movieSelection.getSelectionType() == SelectionType.Popular) {
                url = buildUrlPopularMovies(movieSelection.getPage());
            }
            listMovieJsonStr = placeRequestToAPI(url, movieSelection);
        }

        return getMovieListingFromJson(listMovieJsonStr);

    }

    /**
     * What are the most popular movies?
     * <p/>
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
     * <p/>
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

    private String placeRequestToAPI(URL url, MovieSelection movieSelection) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String listMovieJsonStr = null;


        try {

            if (SelectionType.HighestRated == movieSelection.getSelectionType()) {
                url = buildUrlHighestRatedMovies(movieSelection.getPage());
            }
            if (movieSelection.getSelectionType() == SelectionType.Popular) {
                url = buildUrlPopularMovies(movieSelection.getPage());
            }
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            listMovieJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Movie JSON String: " + listMovieJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return listMovieJsonStr;
    }


    /**
     * Take the String representing the complete Movie Listing in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     * <p/>
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

                for (Movie s : listOfMovie) {
                    Log.v(LOG_TAG, "Movie Id: " + s.getId());
                    Log.v(LOG_TAG, "Movie Poster URI: " + s.getPoster_path());
                }
            } else {
                Log.e("Error No JSON", "listMovieJsonStr is Null");
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON Exception" + e);
        }
        return listOfMovie;

    }


    @Override
    protected void onPostExecute(List<Movie> results) {
        if (results != null) {
            if(imageAdapter  != null) {
                imageAdapter.addall(results);
            }
        }
    }

}
