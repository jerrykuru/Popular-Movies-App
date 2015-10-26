package com.popularmovie.android.appprotfolio.popularmovie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private List<Movie> movieList;
    private ImageAdapter adapter;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        getMovieListByPreference();
        adapter = new ImageAdapter(getActivity(), movieList);
        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Movie movie = (Movie) adapter.getItem(position);
                Log.d(LOG_TAG,movie.getId());
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                intent.putExtra(MovieConstants.CUSTOM_LISTING, movie);
                startActivity(intent);

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


    private void getMovieListByPreference() {
        MovieTask movieTask = new MovieTask(adapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String movieSortSelection = prefs.getString(getString(R.string.movie_sort_key), getString(R.string.movie_sort_key_default));
        MovieSelection movieSelection = new MovieSelection();
        if (movieSortSelection.equalsIgnoreCase(SelectionType.HighestRated.getSortType())) {
            movieSelection.setSelectionType(SelectionType.HighestRated);
        }
        if (movieSortSelection.equalsIgnoreCase(SelectionType.Popular.getSortType())) {
            movieSelection.setSelectionType(SelectionType.Popular);
        }
        try {
            movieList = movieTask.execute(movieSelection).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

}
