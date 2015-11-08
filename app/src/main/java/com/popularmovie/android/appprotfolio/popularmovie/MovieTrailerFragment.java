package com.popularmovie.android.appprotfolio.popularmovie;

import android.database.Cursor;
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
import android.widget.ListView;

import com.popularmovie.android.appprotfolio.popularmovie.data.MovieContract;


public class MovieTrailerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = MovieTrailerFragment.class.getSimpleName();
    private static final int MOVIE_TRAILER_LOADER = 0;
    private MovieTrailerAdapter mMovieTrailerAdapter;
    private Uri mUri;
    private String mMovieId;
    static final String DETAIL_URI = "URI";
    // For the Movie view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] MOVIE_TRAILER_COLUMNS = {
            MovieContract.MovieTrailerEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieTrailerEntry.COLUMN_ISO,
            MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_KEY,
            MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_NAME,
            MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_SITE,
            MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_SIZE,
            MovieContract.MovieTrailerEntry.COLUMN_YOUTUBE_TYPE,
            MovieContract.MovieTrailerEntry.COLUMN_MOVIE_KEY,
            MovieContract.MovieTrailerEntry.COLUMN_COUNT
    };

    // These indices are tied to MOVIE_TRAILER_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    static final int COL_ID = 0;
    static final int COL_ISO = 1;
    static final int COL_YOUTUBE_KEY = 2;
    static final int COL_YOUTUBE_NAME = 3;
    static final int COL_YOUTUBE_SITE = 4;
    static final int COL_YOUTUBE_SIZE = 5;
    static final int COL_YOUTUBE_TYPE = 6;
    static final int COL_MOVIE_KEY = 7;
    static final int COL_COUNT = 8;


    public MovieTrailerFragment() {
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
        void onItemSelected(Uri dateUri);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_movie_details, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_TRAILER_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMovieTrailerAdapter = new MovieTrailerAdapter(getActivity(), null, 0);
        resolveInstanceValues();
        mMovieId = MovieContract.MovieEntry.getMovieIdFromUri(mUri);
        mUri = MovieContract.MovieTrailerEntry.buildMovieTrailerUri(new Long(mMovieId));

        View rootView = inflater.inflate(R.layout.fragment_trailer_main, container, true);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_trailer);
        listView.setAdapter(mMovieTrailerAdapter);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (null != mUri) {

            Log.d(LOG_TAG, "Calling Cursor Movie Trailer with MoveId");
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    MOVIE_TRAILER_COLUMNS,
                    null,
                    new String[]{mMovieId},
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieTrailerAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieTrailerAdapter.swapCursor(null);
    }

    private void resolveInstanceValues() {
        Bundle arguments = null;
        if (getParentFragment() != null) {
            arguments = getParentFragment().getArguments();
        } else {
            arguments = getArguments();
        }
        if (arguments != null) {
            mUri = arguments.getParcelable(DETAIL_URI);
        } else {
            mUri = getActivity().getIntent().getData();
        }
        if (mUri == null) {
            //Build a default URI
            mUri = MovieContract.MovieEntry.buildMovieUri(new Long(10751));
        }
        Log.d(LOG_TAG, mUri.toString());
    }
}

