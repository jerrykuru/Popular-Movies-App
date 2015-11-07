package com.popularmovie.android.appprotfolio.popularmovie;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MovieReviewAdapter extends CursorAdapter {

    private static final String LOG_TAG = MovieReviewAdapter.class.getSimpleName();

    public MovieReviewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_reviews, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView authorView = (TextView) view.findViewById(R.id.review_author);
        String author = cursor.getString(MovieReviewFragment.COL_AUTHOR);
        authorView.setText(author);
        Log.d(LOG_TAG,author);
        TextView reviewComments = (TextView) view.findViewById(R.id.review_comments);
        String comments = cursor.getString(MovieReviewFragment.COL_CONTENT);
        reviewComments.setText(comments);

    }

}