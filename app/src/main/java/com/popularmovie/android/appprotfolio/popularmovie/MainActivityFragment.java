package com.popularmovie.android.appprotfolio.popularmovie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.popularmovie.android.appprotfolio.popularmovie.data.MovieContract;
import com.popularmovie.android.appprotfolio.popularmovie.service.MovieService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private MovieAdapter mMovieAdapter;

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final int MOVIE_LOADER = 0;

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


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMovieAdapter = new MovieAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
     //   getMovieListByPreference();
        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(mMovieAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    ((Callback) getActivity())
                            .onItemSelected(MovieContract.MovieEntry.buildMovieUri(new Long(cursor.getString(MainActivityFragment.COL_MOVIE_ID))
                            ));
                }
            }
        });
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getMovieListByPreference();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    // Invoke the Movie Service
    private void getMovieListByPreference() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String movieSortSelection = prefs.getString(getString(R.string.movie_sort_key), getString(R.string.movie_sort_key_default));
        Intent intent = new Intent(getActivity(), MovieService.class);
        intent.putExtra(MovieService.MOVIE_SELECTION_TYPE,
                movieSortSelection);
        getActivity().startService(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri movieSelectionUri = null;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String movieSortSelection = prefs.getString(getString(R.string.movie_sort_key), getString(R.string.movie_sort_key_default));

        if (movieSortSelection.equalsIgnoreCase(SelectionType.HighestRated.getSortType())) {
            movieSelectionUri =  MovieContract.MovieEntry.buildListOfHighestRatedMovies();
       }
        if (movieSortSelection.equalsIgnoreCase(SelectionType.Popular.getSortType())) {
            movieSelectionUri =  MovieContract.MovieEntry.buildListOfPopularMovies();
        }
        if (movieSortSelection.equalsIgnoreCase(SelectionType.Favourite.getSortType())) {
            movieSelectionUri =  MovieContract.MovieEntry.buildListFavouriteMoviesUri();
        }

        return new CursorLoader(getActivity(),
                movieSelectionUri,
                MOVIE_COLUMNS,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getMovieListByPreference();
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Called by Activity on Resume from back stack
     */
    void onMovieSelectionPreferenceChange() {
        getMovieListByPreference();
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }
}
