package com.popularmovie.android.appprotfolio.popularmovie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.popularmovie.android.appprotfolio.popularmovie.data.MovieContract;
import com.popularmovie.android.appprotfolio.popularmovie.service.MovieDetailService;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MovieDetailsActivityFragment.class.getSimpleName();

    private static final int MOVIE_LOADER = 0;
    private Uri mUri;
    private String mMovieId;

    // For the Movie view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_HIGHESTRATED,
            MovieContract.MovieEntry.COLUMN_POPULAR,
            MovieContract.MovieEntry.COLUMN_FAVORITE_FLAG
    };

    // These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_TITLE = 1;
    static final int COL_MOVIE_RELEASE_DATE = 2;
    static final int COL_MOVIE_POSTER_PATH = 3;
    static final int COL_MOVIE_VOTE_AVERAGE = 4;
    static final int COL_MOVIE_OVERVIEW = 5;
    static final int COL_MOVIE_HIGHESTRATED = 6;
    static final int COL_MOVIE_POPULAR = 7;
    static final int COL_MOVIE_FAVORITE_FLAG = 8;


    private ImageView mPosterView;
    private TextView mMovieTitleView;
    private TextView mPlotSynopsisView;
    private TextView mReleaseDateView;
    private TextView mVoteAverageView;
    private  Button mButton;

    public void addMovieAsFavorite(String movieID) {

        Uri uri = MovieContract.MovieEntry.buildMovieUri(new Long(movieID));
        Cursor movieCursor = getActivity().getContentResolver().query(
                uri,
                MOVIE_COLUMNS,
                null,
                new String[]{movieID},
                null
        );
        ContentValues testValues = new ContentValues();
        movieCursor.moveToFirst();
        DatabaseUtils.cursorRowToContentValues(movieCursor, testValues);
        uri = MovieContract.MovieEntry.CONTENT_URI;
        testValues.remove(MovieContract.MovieEntry.COLUMN_FAVORITE_FLAG);
        testValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE_FLAG, "true");
        int movieTrailerCursor = getActivity().getContentResolver().update(
                uri,
                testValues,
                null,
                new String[]{movieID}
        );
        movieCursor.close();
    }


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {


        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }


    public MovieDetailsActivityFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_movie_details, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        mUri = getActivity().getIntent().getData();
        Log.d(LOG_TAG, mUri.toString());
        mMovieId = MovieContract.MovieEntry.getMovieIdFromUri(mUri);
        getMovieDetailByMovieId(mMovieId);
        mPosterView = (ImageView) rootView.findViewById(R.id.poster);
        mMovieTitleView = (TextView) rootView.findViewById(R.id.movie_title);
        mPlotSynopsisView = (TextView) rootView.findViewById(R.id.plot_synopsis);
        mReleaseDateView = (TextView) rootView.findViewById(R.id.release_date);
        mVoteAverageView = (TextView) rootView.findViewById(R.id.vote_average);
        mButton = (Button) rootView.findViewById(R.id.addMovieAsFavorite);
        mButton.setTag(R.string.favorite_movieId,mMovieId);
        return rootView;
    }

    // Invoke the Movie Detail Service
    private void getMovieDetailByMovieId(String movieID) {
        Log.d(LOG_TAG, "Invoking Movie Detail Service");
        Intent intent = new Intent(getActivity(), MovieDetailService.class);
        intent.putExtra(MovieDetailService.MOVIE_DETAIL_ID,
                movieID);
        getActivity().startService(intent);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri) {

            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    MOVIE_COLUMNS,
                    null,
                    new String[]{mMovieId},
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {
           Picasso.with(getContext()).load(data.getString(COL_MOVIE_POSTER_PATH)).into(mPosterView);
            mMovieTitleView.setText(data.getString(COL_MOVIE_TITLE));
            mPlotSynopsisView.setText(data.getString(COL_MOVIE_OVERVIEW));
            String voteAverage = "/10";
            mVoteAverageView.setText(data.getString(COL_MOVIE_VOTE_AVERAGE) + voteAverage);
            Calendar cal = Calendar.getInstance();
            String releaseDate = data.getString(COL_MOVIE_RELEASE_DATE);
            if (releaseDate != null) {
                try {
                    cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(data.getString(COL_MOVIE_RELEASE_DATE)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                cal.setTime(new Date());
            }
            mReleaseDateView.setText(Integer.toString(cal.get(Calendar.YEAR)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


}
