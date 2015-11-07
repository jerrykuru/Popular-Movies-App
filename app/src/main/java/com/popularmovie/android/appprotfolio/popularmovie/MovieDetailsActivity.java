package com.popularmovie.android.appprotfolio.popularmovie;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MovieDetailsActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private static final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_movie_details);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri contentUri) {
        //// TODO: 11/6/15
//
//        Intent intent = new Intent(this, MovieDetailsActivity.class)
//                .setData(contentUri);
//        startActivity(intent);

    }


    // Favorite button click
    public void addMovieAsFavorite(View view) {
        // Do something in response to button click
        Log.d(LOG_TAG, "i did cilck and the movie id is="+ view.getTag(R.string.favorite_movieId));
        // Get the fragment and invoke a method on the framgement that will add the movie as favorite
        String movieID =  view.getTag(R.string.favorite_movieId).toString();
        MovieDetailsActivityFragment movieDetailsActivityFragment = (MovieDetailsActivityFragment)getSupportFragmentManager().findFragmentById(R.id.movie_details);
        movieDetailsActivityFragment.addMovieAsFavorite(movieID);

    }
}
