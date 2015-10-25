package com.popularmovie.android.appprotfolio.popularmovie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class ImageAdapter extends BaseAdapter {

    private static final String LOG_TAG = ImageAdapter.class.getSimpleName();

    private Context mContext;

    private List<Movie> movieList = null;
    private LayoutInflater layoutinflater;


    public ImageAdapter(Context c, List<Movie> movieListInput) {
        mContext = c;
        movieList = movieListInput;
        layoutinflater = LayoutInflater.from(mContext);

    }

    public int getCount() {
        return movieList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            convertView = layoutinflater.inflate(R.layout.grid_item, parent, false);
            imageView = (ImageView) convertView.findViewById(R.id.imageView);

        } else {
            imageView = (ImageView) convertView;
        }

        Movie movie = movieList.get(position);
        Picasso.with(mContext).load(movie.getPoster_path()).into(imageView);
        return imageView;
    }



    public void addall(List<Movie> listOfMovies){
        movieList.clear();
        this.movieList =  listOfMovies;
        this.notifyDataSetChanged();
    }
}
