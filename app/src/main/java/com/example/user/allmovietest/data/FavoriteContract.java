package com.example.user.allmovietest.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URL;

/**
 * Created by Lavinia Dragunoi on 3/20/2018.
 */

public class FavoriteContract {


    public static final String AUTHORITY = "com.example.user.allmovietest";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoriteEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();
        //table name
        public static final String TABLE_NAME = "favorites";
        //tabels columns name
        public static final String COLUMN_MOVIE_POSTER = "moviePoster";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_VOUT_COUNT = "vouteCount";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
    }
}
