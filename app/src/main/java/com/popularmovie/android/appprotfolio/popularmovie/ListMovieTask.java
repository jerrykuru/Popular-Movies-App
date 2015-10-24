package com.popularmovie.android.appprotfolio.popularmovie;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;



import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class ListMovieTask extends AsyncTask<MovieSelection, Void, List<Movie>> {

    private static final String LOG_TAG = ListMovieTask.class.getSimpleName();
    private static final String DISCOVER_MOVIE_URL = "http://api.themoviedb.org/3/discover/movie?";
    private static final String SORT_BY = "sort_by";
    private static final String API_KEY = "api_key";
    private static final String ApiKey = "7c5abefc53acec71067d4859c75dbd0f";
    private static final String PAGE = "page";


    /**
     * @param params
     * @return
     */
    @Override
    protected List<Movie> doInBackground(MovieSelection... params) {


        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String listMovieJsonStr = null;

        try {
            URL url = null;
            if (SelectionType.HighestRated == params[0].getSelectionType()) {
                url = buildUrlHighestRatedMovies(params[0].getPage());
            }
            if (params[0].getSelectionType() == SelectionType.Popular) {
                url = buildUrlPopularMovies(params[0].getPage());
            }
            // Create the request to OpenWeatherMap, and open the connection
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
            Log.v(LOG_TAG, "Forcast JSON String: " + listMovieJsonStr);
            return getWeatherDataFromJson(listMovieJsonStr, numDays);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error", e);
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
        return new String[0];
    }

    /**
     * What are the most popular movies?
     * <p/>
     * URL: /discover/movie?sort_by=popularity.desc
     *
     * @param page
     * @return
     * @throws MalformedURLException
     */
    private URL buildUrlPopularMovies(String page) throws MalformedURLException {

        final String POPULARITY_DESC = "popularity.desc";

        Uri builtUri = Uri.parse(DISCOVER_MOVIE_URL).buildUpon()
                .appendQueryParameter(SORT_BY, POPULARITY_DESC)
                .appendQueryParameter(PAGE, page)
                .appendQueryParameter(API_KEY, ApiKey)
                .build();
        URL url = new URL(builtUri.toString());
        Log.v(LOG_TAG, builtUri.toString());
        return url;
    }


    /**
     * What are the highest rated movies rated R?
     * <p/>
     * URL: /discover/movie/?certification_country=US&certification=R&sort_by=vote_average.desc
     * @param page
     * @return
     * @throws MalformedURLException
     */
    private URL buildUrlHighestRatedMovies(String page) throws MalformedURLException {

        final String HIGHEST_RATED = "vote_average.desc";
        final String CERTIFICATION_COUNTRY = "certification_country";
        final String CERTIFICATION_COUNTRY_US = "US";


        Uri builtUri = Uri.parse(DISCOVER_MOVIE_URL).buildUpon()
                .appendQueryParameter(CERTIFICATION_COUNTRY, CERTIFICATION_COUNTRY_US)
                .appendQueryParameter(SORT_BY, HIGHEST_RATED)
                .appendQueryParameter(PAGE, page)
                .appendQueryParameter(API_KEY, ApiKey)
                .build();
        URL url = new URL(builtUri.toString());
        Log.v(LOG_TAG, builtUri.toString());
        return url;
    }
}
