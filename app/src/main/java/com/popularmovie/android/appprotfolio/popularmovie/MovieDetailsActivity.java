package com.popularmovie.android.appprotfolio.popularmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

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


    // Favorite button click
    public void addMovieAsFavorite(View view) {
        // Get the fragment and invoke a method on the framgement that will add the movie as favorite
        String movieID = view.getTag(R.string.favorite_movieId).toString();
        MovieDetailsActivityFragment movieDetailsActivityFragment = (MovieDetailsActivityFragment) getSupportFragmentManager().findFragmentById(R.id.movie_poster_syno_details);
        movieDetailsActivityFragment.addMovieAsFavorite(movieID);

    }

    // Play Youtube Trailer
    public void playTrailer(View view) {
        String youTubeKey = view.getTag(R.string.youTube_key).toString();
        Intent intent = new Intent(this, WebViewActivity.class).putExtra(String.valueOf(R.string.youTube_key), youTubeKey);
        startActivity(intent);
    }
}
