package com.example.user.allmovietest;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.allmovietest.data.ManageFavoritesUtils;
import com.example.user.allmovietest.movies.MovieAdapter;
import com.example.user.allmovietest.movies.MovieObject;
import com.example.user.allmovietest.reviews.ReviewAdapter;
import com.example.user.allmovietest.reviews.ReviewObject;
import com.example.user.allmovietest.trailers.TrailerAdapter;
import com.example.user.allmovietest.trailers.TrailerObject;
import com.example.user.allmovietest.utils.JsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

import static java.lang.String.valueOf;

/**
 * This class will help to populate the details activity layout with each movie characteristics.
 */
public class DetailActivity extends AppCompatActivity {

    TextView releaseDateTV, ratingTV, overviewTV, voteCountTV, noReviewTV, noTrailerTV;
    ImageView posterMovie;
    RatingBar ratingBar;
    ReviewAdapter mReviewAdapter;
    TrailerAdapter mTrailerAdapter;
    MovieObject currentMovie;
    ProgressBar progressBarReview, progressBarTrailer;
    TrailerObject firstTrailer;
    FloatingActionButton favoriteViewDetailsContent;
    boolean isCheckedAsFavorite;
    private static final String REVIEW_URL_PATH = "reviews";
    private static final String VIDEO_URL_PATH = "videos";

    private final int TRAILER_LOADER = 8;
    private final int REVIEWS_LOADER = 4;
    private final int FAVORITE_LOADER = 6;

    public RecyclerView reviewsRecyclerView, trailerRecyclerView;

    LoaderManager.LoaderCallbacks<Cursor> favoritesLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };


    LoaderManager.LoaderCallbacks<List<TrailerObject>> trailerLoader = new LoaderManager.LoaderCallbacks<List<TrailerObject>>() {
        @SuppressLint("StaticFieldLeak")
        @Override
        public Loader<List<TrailerObject>> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<List<TrailerObject>>(DetailActivity.this) {

                List<TrailerObject> trailerObjectList;

                @Override
                protected void onStartLoading() {
                    if (trailerObjectList != null) {
                        deliverResult(trailerObjectList);
                    } else {
                        forceLoad();
                    }

                }

                @Override
                public List<TrailerObject> loadInBackground() {
                    List<TrailerObject> trailerObjectList = new ArrayList<>();
                    try {
                        URL requestUrl = JsonUtils.getUrlResponseById(valueOf(currentMovie.getMovieId()),VIDEO_URL_PATH);
                        Response response = JsonUtils.fetchData(requestUrl.toString());
                        trailerObjectList = JsonUtils.parseTrailerJson(response.body().string());

                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    return trailerObjectList;
                }
                @Override
                public void deliverResult(List<TrailerObject> data) {
                    trailerObjectList = data;
                    super.deliverResult(data);
                }
            };
        }


        @Override
        public void onLoadFinished(Loader<List<TrailerObject>> loader, List<TrailerObject> trailerObjectList) {
            progressBarTrailer.setVisibility(View.GONE);
            mTrailerAdapter.addAll(trailerObjectList);
            firstTrailer = trailerObjectList.get(0);
            if(trailerObjectList.isEmpty()) noTrailerTV.setVisibility(View.VISIBLE);

        }

        @Override
        public void onLoaderReset(Loader<List<TrailerObject>> loader) {
            mTrailerAdapter.clearAll();
        }
    };

    LoaderManager.LoaderCallbacks<List<ReviewObject>> reviewLoader = new LoaderManager.LoaderCallbacks<List<ReviewObject>>() {
        @SuppressLint("StaticFieldLeak")
        @Override
        public Loader<List<ReviewObject>> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<List<ReviewObject>>(DetailActivity.this) {

                List<ReviewObject> reviewObjectList;

                @Override
                protected void onStartLoading() {
                    if (reviewObjectList != null) {
                        deliverResult(reviewObjectList);
                    } else {
                        forceLoad();
                    }

                }

                @Override
                public List<ReviewObject> loadInBackground() {
                    List<ReviewObject> reviewObjectList = new ArrayList<>();
                    try {
                        URL requestUrl = JsonUtils.getUrlResponseById(valueOf(currentMovie.getMovieId()),REVIEW_URL_PATH);
                        Response response = JsonUtils.fetchData(requestUrl.toString());
                        reviewObjectList = JsonUtils.parseReviewJson(response.body().string());

                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    return reviewObjectList;
                }
                @Override
                public void deliverResult(List<ReviewObject> data) {
                    reviewObjectList = data;
                    super.deliverResult(data);
                }
            };
        }


        @Override
        public void onLoadFinished(Loader<List<ReviewObject>> loader, List<ReviewObject> reviewObjectList) {
            progressBarReview.setVisibility(View.GONE);
            mReviewAdapter.addAll(reviewObjectList);
            if(reviewObjectList.isEmpty()) noReviewTV.setVisibility(View.VISIBLE);

        }

        @Override
        public void onLoaderReset(Loader<List<ReviewObject>> loader) {
            mReviewAdapter.clearAll();
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        reviewsRecyclerView = findViewById(R.id.reviews_recycler);
        RecyclerView.LayoutManager layoutManagerReviews = new
                LinearLayoutManager(reviewsRecyclerView.getContext());
        reviewsRecyclerView.setLayoutManager(layoutManagerReviews);

        mReviewAdapter = new ReviewAdapter(this, new ArrayList<ReviewObject>());
        reviewsRecyclerView.setAdapter(mReviewAdapter);

        getLoaderManager().initLoader(REVIEWS_LOADER, null, reviewLoader);

        trailerRecyclerView = findViewById(R.id.trailer_recycler);
        RecyclerView.LayoutManager layoutManagerTrailers = new
                LinearLayoutManager(trailerRecyclerView.getContext());
        trailerRecyclerView.setLayoutManager(layoutManagerTrailers);

        mTrailerAdapter = new TrailerAdapter(this, new ArrayList<TrailerObject>());
        trailerRecyclerView.setAdapter(mTrailerAdapter);

        getLoaderManager().initLoader(TRAILER_LOADER, null, trailerLoader);

        progressBarReview = findViewById(R.id.progress_bar_reviews);
        noReviewTV = findViewById(R.id.no_review_tv);

        progressBarTrailer = findViewById(R.id.progress_bar_trailers);
        noTrailerTV = findViewById(R.id.no_trailer_tv);

        trailerRecyclerView = findViewById(R.id.trailer_recycler);

        posterMovie = findViewById(R.id.movie_poster_details_view);

        Intent intent = getIntent();
        currentMovie = intent.getParcelableExtra("Movie");
         isCheckedAsFavorite = intent.getBooleanExtra("IsFavorite", false);

        String moviePosterUrlString = MovieAdapter.buildPosterUrl(currentMovie.getMoviePoster());
        Picasso.with(this).load(moviePosterUrlString).into(posterMovie);
        displayMovieUI(currentMovie);

        setTitle(currentMovie.getOriginalTitle());

        favoriteViewDetailsContent = findViewById(R.id.included_fab_details);
        if(isCheckedAsFavorite){
         favoriteViewDetailsContent.setImageResource(R.drawable.ic_favorite_red);
        }else favoriteViewDetailsContent.setImageResource(R.drawable.ic_favorite);


    }

    /**
     * This method takes a movieObject and extracts from it each info needed to be displayed
     * in the activity_detail.xml layout
     * @param movie the MovieObject that contains all information needed
     */
    private void displayMovieUI(MovieObject movie) {
        releaseDateTV = findViewById(R.id.release_date_tv);
        releaseDateTV.setText(movie.getReleaseDate());

        ratingTV = findViewById(R.id.rating_value_detail_tv);
        ratingTV.setText(valueOf(movie.getRating()));

        overviewTV = findViewById(R.id.overview_tv);
        overviewTV.setText(movie.getOverview());

        voteCountTV = findViewById(R.id.vote_count);
        voteCountTV.setText(valueOf(movie.getVoteCount()));

        ratingBar = findViewById(R.id.rating_bar);
        ratingBar.setRating((float) movie.getRating());
    }
        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.share_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            int id = item.getItemId();

            if (id == R.id.share_action) {
                Intent shareIntent = ShareCompat.IntentBuilder
                        .from(this)
                        .setType("text/plain")
                        .setChooserTitle("Share trailer")
                        .setText(firstTrailer.getSiteTrailer())
                        .getIntent();
                if(shareIntent.resolveActivity(getPackageManager()) != null) startActivity(shareIntent);
                return true;
            } else return super.onOptionsItemSelected(item);
        }
    }
