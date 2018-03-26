package com.example.user.allmovietest;


import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.allmovietest.data.FavoriteContract;
import com.example.user.allmovietest.data.FavoriteDBHelper;
import com.example.user.allmovietest.data.ManageFavoritesUtils;
import com.example.user.allmovietest.movies.MovieAdapter;
import com.example.user.allmovietest.movies.MovieObject;
import com.example.user.allmovietest.utils.JsonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

import static com.example.user.allmovietest.data.FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID;
import static com.example.user.allmovietest.data.FavoriteContract.FavoriteEntry.COLUMN_MOVIE_POSTER;
import static com.example.user.allmovietest.data.FavoriteContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE;
import static com.example.user.allmovietest.data.FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW;
import static com.example.user.allmovietest.data.FavoriteContract.FavoriteEntry.COLUMN_POPULARITY;
import static com.example.user.allmovietest.data.FavoriteContract.FavoriteEntry.COLUMN_RATING;
import static com.example.user.allmovietest.data.FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE;
import static com.example.user.allmovietest.data.FavoriteContract.FavoriteEntry.COLUMN_VOUT_COUNT;
import static java.lang.Integer.valueOf;

/**
 * Main class of the project
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MOVIE_LOADER_ID = 3;
    public String sortBy = "top_rated";
    private MovieAdapter mAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    public  ProgressBar progressBar;
    public  RecyclerView movieRV;
    private TextView noFavoriteTextView;
    public final int FAVORITE_LOADER = 6;
    SQLiteDatabase mDb;

    LoaderManager.LoaderCallbacks<List<MovieObject>> movieLoader = new LoaderManager
            .LoaderCallbacks<List<MovieObject>>() {

        @SuppressLint("StaticFieldLeak")
        @Override
        public Loader<List<MovieObject>> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<List<MovieObject>>(MainActivity.this){

                List<MovieObject> movieObjectList;

                @Override
                protected void onStartLoading() {
                    if (movieObjectList != null) {
                        deliverResult(movieObjectList);
                    } else {
                        forceLoad();
                    }
                }

                    @Override
                public List<MovieObject> loadInBackground() {
                        List<MovieObject> movieObjectList = new ArrayList<>();
                        URL requestUrl = null;
                        try {
                            requestUrl = JsonUtils.getUrlResponse(sortBy);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            Response response = JsonUtils.fetchData(requestUrl.toString());
                            movieObjectList = JsonUtils.parseMovieJson(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return movieObjectList;
                }
                @Override
                public void deliverResult(List<MovieObject> data) {
                    movieObjectList = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<List<MovieObject>> loader, List<MovieObject> movieObjectList) {
            progressBar.setVisibility(View.GONE);
            mAdapter.addAll(movieObjectList);
        }

        @Override
        public void onLoaderReset(Loader<List<MovieObject>> loader) {
            mAdapter.clearAll();
        }

    };

   public LoaderManager.LoaderCallbacks<Cursor> favoritesLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @SuppressLint("StaticFieldLeak")
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            return new AsyncTaskLoader<Cursor>(MainActivity.this) {

                Cursor mFavoriteData = null;
                @Override
                protected void onStartLoading() {
                    if (mFavoriteData != null) {
                        // Delivers any previously loaded data immediately
                        deliverResult(mFavoriteData);
                    } else {
                        // Force a new load
                        forceLoad();
                    }
                }
                @Override
                public Cursor loadInBackground() {
                    try {
                        return getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                FavoriteContract.FavoriteEntry.COLUMN_RATING);
                    }catch (Exception e){
                        e.printStackTrace();
                        return null;
                    }
                }
                public void deliverResult(Cursor data) {
                    mFavoriteData = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            List<MovieObject> movieObjectList = new ArrayList<>();
            while (data.moveToNext()) {
                int movieId = data.getInt(data.getColumnIndex(COLUMN_MOVIE_ID));
                String moviePoster = data.getString(data.getColumnIndex(COLUMN_MOVIE_POSTER));
                String overview = data.getString(data.getColumnIndex(COLUMN_OVERVIEW));
                double rating = data.getDouble(data.getColumnIndex(COLUMN_RATING));
                String releaseDate = data.getString(data.getColumnIndex(COLUMN_RELEASE_DATE));
                int voteCount = data.getInt(data.getColumnIndex(COLUMN_VOUT_COUNT));
                String originalTitle = data.getString(data.getColumnIndex(COLUMN_ORIGINAL_TITLE));
                double popularity = data.getDouble(data.getColumnIndex(COLUMN_POPULARITY));
                MovieObject movieObject = new MovieObject(originalTitle, moviePoster,
                        overview, rating, popularity, releaseDate, voteCount, movieId);
                movieObjectList.add(movieObject);
                mAdapter.addAll(movieObjectList);

            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.clearAll();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FavoriteDBHelper favoriteDBHelper = new FavoriteDBHelper(this);
        mDb = favoriteDBHelper.getWritableDatabase();

        progressBar = findViewById(R.id.progress_bar);

        //setting up the navigation drawer
        drawerLayout = findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //find the recycler view that will host the movie list
        movieRV = findViewById(R.id.movie_list);
        View noConnectionView = findViewById(R.id.no_connection_group);

        //set the movie list
        int gridRows = valueOf(getString(R.string.grid_rows));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(movieRV.getContext(),
                gridRows);
        movieRV.setLayoutManager(layoutManager);

        mAdapter = new MovieAdapter(this, new ArrayList<MovieObject>());
        movieRV.setAdapter(mAdapter);

        //if is no connection an error message will appear on the device

        if (isConnected()) {
            getLoaderManager().initLoader(MOVIE_LOADER_ID, null, movieLoader);
        } else {
            noConnectionView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This is a method that will verify if the device has internet connection
     * @return true if exists connection and false otherwise
     */
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        TextView selectedIconPopularity = findViewById(R.id.popularity_list_movie_title);
        TextView selectedIconRated = findViewById(R.id.rated_list_movie_title);
        TextView selectedIconFavorite = findViewById(R.id.favorite_list_movie_title);
        getLoaderManager().initLoader(FAVORITE_LOADER, null, favoritesLoader);

        int id = item.getItemId();
        if (id == R.id.most_popular) {
            // sets order for the movie list by the most popular
            sortBy = getString(R.string.popular);
            selectedIconRated.setVisibility(View.GONE);
            selectedIconPopularity.setVisibility(View.VISIBLE);
            drawerLayout.closeDrawer(GravityCompat.START);
            movieRV.setAdapter(mAdapter);
            getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, movieLoader);

        } else if (id == R.id.top_rated) {
            // sets order for the movie list by the top rated
            sortBy = getString(R.string.rated);
            selectedIconPopularity.setVisibility(View.GONE);
            selectedIconRated.setVisibility(View.VISIBLE);
            drawerLayout.closeDrawer(GravityCompat.START);
            movieRV.setAdapter(mAdapter);
            getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, movieLoader);

        } else if (id == R.id.favorite_movie) {
            // sets order for the movie list by
            selectedIconPopularity.setVisibility(View.GONE);
            selectedIconRated.setVisibility(View.GONE);
            selectedIconFavorite.setVisibility(View.VISIBLE);
            drawerLayout.closeDrawer(GravityCompat.START);
            movieRV.setAdapter(mAdapter);
            getLoaderManager().restartLoader(FAVORITE_LOADER, null, favoritesLoader);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * When the back button is clicked and the user has the navigationDrawer opened, this method
     * will sent on the main Activity layout
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
