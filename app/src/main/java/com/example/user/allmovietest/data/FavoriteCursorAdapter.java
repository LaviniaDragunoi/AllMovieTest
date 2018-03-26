package com.example.user.allmovietest.data;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.allmovietest.DetailActivity;
import com.example.user.allmovietest.R;
import com.example.user.allmovietest.movies.MovieAdapter;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import static java.lang.String.valueOf;

/**
 * Created by Lavinia Dragunoi on 3/21/2018.
 */

public class FavoriteCursorAdapter extends RecyclerView.Adapter<FavoriteCursorAdapter
        .FavoriteViewHolder> implements Parcelable{
    private static final String LOG_TAG = FavoriteCursorAdapter.class.getName();
    private Context mContext;
    private Cursor mCursor;

    public FavoriteCursorAdapter(Context context, Cursor cursor){
        this.mContext = context;
        this.mCursor = cursor;
    }

    protected FavoriteCursorAdapter(Parcel in) {
    }

    public static final Creator<FavoriteCursorAdapter> CREATOR = new Creator<FavoriteCursorAdapter>() {
        @Override
        public FavoriteCursorAdapter createFromParcel(Parcel in) {
            return new FavoriteCursorAdapter(in);
        }

        @Override
        public FavoriteCursorAdapter[] newArray(int size) {
            return new FavoriteCursorAdapter[size];
        }
    };

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item, parent, false);
        view.setBackgroundColor(R.drawable.color_background);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {

        if(!mCursor.moveToPosition(position)) return;

        ImageView moviePosterImageView = holder.moviePoster;
        String moviePosterUrlString = MovieAdapter.buildPosterUrl(mCursor.getString(
                mCursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_POSTER)));

        final TextView ratingValueTextView = holder.ratingValue;
        ratingValueTextView.setVisibility(View.INVISIBLE);

        final String ratingString = valueOf(mCursor.getDouble(mCursor.getColumnIndex(
                FavoriteContract.FavoriteEntry.COLUMN_RATING)));

        final FloatingActionButton favoriteView = holder.favoriteView;
        favoriteView.setVisibility(View.INVISIBLE);

        Picasso.with(mContext).load(moviePosterUrlString).into(moviePosterImageView,
                new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        ratingValueTextView.setText(ratingString);
                        ratingValueTextView.setVisibility(View.VISIBLE);
                        favoriteView.setVisibility(View.VISIBLE);
                        favoriteView.setImageResource(R.drawable.ic_favorite_red);
                    }

                    @Override
                    public void onError() {
                        Log.e(LOG_TAG, String.valueOf(R.string.error_loading_movie));

                    }
                });

        long id = mCursor.getLong(mCursor.getColumnIndex(FavoriteContract.FavoriteEntry._ID));
        holder.itemView.setTag(id);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("FavoriteMovie", mCursor.getExtras());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder{

        TextView ratingValue;
        ImageView moviePoster;
        FloatingActionButton favoriteView;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            //Find Views that will display each item
            ratingValue = itemView.findViewById(R.id.rating_value_tv);
            moviePoster = itemView.findViewById(R.id.movie_image_List);
            favoriteView = itemView.findViewById(R.id.favorite_list_item);
        }
    }

    public void swapCursor(Cursor newCursor){
        if(mCursor != null) mCursor.close();
        mCursor = newCursor;
        if(newCursor != null){
            this.notifyDataSetChanged();
        }
    }

}
