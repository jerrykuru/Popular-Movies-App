package com.popularmovie.android.appprotfolio.popularmovie;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends Fragment {

    private static final String LOG_TAG = MovieDetailsActivityFragment.class.getSimpleName();

    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Movie movie;
        Intent b = getActivity().getIntent();
        if(b!=null) {
            movie = (Movie) b.getSerializableExtra(MovieConstants.CUSTOM_LISTING);
            Log.d(LOG_TAG, movie.getId());
        }

        return inflater.inflate(R.layout.fragment_movie_details, container, false);
    }
}
