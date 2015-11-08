package com.popularmovie.android.appprotfolio.popularmovie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String MAIN_DETAIL_FRAGMENT_TAG = "DMFTAG";

    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_details) != null) {
            mTwoPane = true;
            Log.d(LOG_TAG, "tWO PANE");
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_details, new MovieDetailsFragment(), MAIN_DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(Uri contentUri) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailsFragment.DETAIL_URI, contentUri);
            MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
            movieDetailsFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_details, movieDetailsFragment, MAIN_DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailsActivity.class)
                    .setData(contentUri);
            startActivity(intent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MainActivityFragment mainActivityFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        mainActivityFragment.onMovieSelectionPreferenceChange();
    }

    // Favorite button click
    public void addMovieAsFavorite(View view) {
        // Get the fragment and invoke a method on the framgment that will add the movie as favorite
        String movieID = view.getTag(R.string.favorite_movieId).toString();

        MovieDetailsFragment movieDetailsFragment = (MovieDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.movie_details);
        MovieDetailsActivityFragment movieDetailsActivityFragment = (MovieDetailsActivityFragment) movieDetailsFragment.getChildFragmentManager().findFragmentById(R.id.movie_poster_syno_details);
        if (movieDetailsActivityFragment != null) {
            movieDetailsActivityFragment.addMovieAsFavorite(movieID);
        } else {
            Log.d(LOG_TAG, "movieDetailsActivityFragment is Null");
        }

    }

    // Play Youtube Trailer
    public void playTrailer(View view) {
        String youTubeKey = view.getTag(R.string.youTube_key).toString();
        Intent intent = new Intent(this, WebViewActivity.class).putExtra(String.valueOf(R.string.youTube_key), youTubeKey);
        startActivity(intent);
    }
}
