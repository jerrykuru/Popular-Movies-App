package com.popularmovie.android.appprotfolio.popularmovie;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popularmovie.android.appprotfolio.popularmovie.service.MovieDetailService;
import com.popularmovie.android.appprotfolio.popularmovie.service.MovieService;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MovieDetailsActivityFragment.class.getSimpleName();

    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        String voteAverage = "/10";
        Movie movie;
        Intent b = getActivity().getIntent();
        if (b != null) {
            movie = (Movie) b.getSerializableExtra(MovieConstants.CUSTOM_LISTING);
            ((TextView) rootView.findViewById(R.id.movie_title)).setText(movie.getTitle());
            ((TextView) rootView.findViewById(R.id.plot_synopsis)).setText(movie.getOverview());
            Calendar cal = Calendar.getInstance();
            if (movie.getRelease_date() != null) {
                cal.setTime(movie.getRelease_date());
            } else {
                cal.setTime(new Date());
            }
            ((TextView) rootView.findViewById(R.id.release_date)).setText(Integer.toString(cal.get(Calendar.YEAR)));
            ((TextView) rootView.findViewById(R.id.vote_average)).setText(Double.toString(movie.getVote_average())+voteAverage);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.poster);
            Picasso.with(getContext()).load(movie.getPoster_path()).into(imageView);
        }

        return rootView;
    }

    // Invoke the Movie Detail Service
    private void getMovieDetailByMovieId(String movieID) {
        Intent intent = new Intent(getActivity(), MovieService.class);
        intent.putExtra(MovieDetailService.MOVIE_DETAIL_ID,
                movieID);
        getActivity().startService(intent);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
