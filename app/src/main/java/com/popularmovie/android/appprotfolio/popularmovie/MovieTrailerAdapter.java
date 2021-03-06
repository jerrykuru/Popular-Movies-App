package com.popularmovie.android.appprotfolio.popularmovie;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MovieTrailerAdapter extends CursorAdapter {

    private static final String LOG_TAG = MovieTrailerAdapter.class.getSimpleName();
    private static final String heading = "Trailer ";

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
        TextView trailerCount = (TextView) view.findViewById(R.id.trailer_count);
        String count = cursor.getString(MovieTrailerFragment.COL_COUNT);
        trailerCount.setText(heading + count);
        Button mButton = (Button) view.findViewById(R.id.youtube_id);
        mButton.setTag(R.string.youTube_key, cursor.getString(MovieTrailerFragment.COL_YOUTUBE_KEY));

    }

}
