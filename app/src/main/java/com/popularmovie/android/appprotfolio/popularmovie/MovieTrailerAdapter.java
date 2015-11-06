package com.popularmovie.android.appprotfolio.popularmovie;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieTrailerAdapter extends CursorAdapter {

    private static final String LOG_TAG = MovieTrailerAdapter.class.getSimpleName();

    public MovieTrailerAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_trailer, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView imageView = (TextView) view.findViewById(R.id.trailer_count);
        String posterPath = cursor.getString(MainActivityFragment.COL_MOVIE_POSTER_PATH);
        Picasso.with(context).load(posterPath).into(imageView);
    }

}
