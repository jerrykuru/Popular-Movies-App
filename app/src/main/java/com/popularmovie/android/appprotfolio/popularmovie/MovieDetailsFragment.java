package com.popularmovie.android.appprotfolio.popularmovie;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;


public class MovieDetailsFragment extends Fragment {
    private static final String MAIN_DETAIL_FRAGMENT_TAG = "DMFTAG";
    static final String DETAIL_URI = "URI";
    private static final String LOG_TAG = MovieDetailsActivityFragment.class.getSimpleName();
    private Uri mUri;
    private static final String MOVIE_FRAGMENT_TAG = "MTAG";
    private static final String REVIEW_FRAGMENT_TAG = "RMTAG";
    private static final String TRAILER_FRAGMENT_TAG = "TRTAG";


    public MovieDetailsFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_movie_details, menu);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_movie_details, container, false);
        return rootView;

    }


}
