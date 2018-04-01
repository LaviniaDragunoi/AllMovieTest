package com.example.user.allmovietest.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;
import java.net.URL;

/**
 * Created by Lavinia Dragunoi on 3/20/2018.
 */

public class FavoriteContract {
    /*
    Through FavoriteContract are added constants that will be used in creating and using favorite
     movies database
     */

    //the authority, that is the scheme of the URI used to access database items.
    public static final String AUTHORITY = "com.example.user.allmovietest";

    //the base content URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    //this is the path for "favorites" movie list from database
    public static final String PATH_FAVORITES = "favorites";

    /*Here will be declared all constance of the database*/
    public static final class FavoriteEntry implements BaseColumns {

        //building the URI content = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();
        //table name
        public static final String TABLE_NAME = "favorites";
        //tables columns name
        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_MOVIE_POSTER = "moviePoster";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_VOTE_COUNT = "vouteCount";
        public static final String COLUMN_MOVIE_ID = "movieId";
    }
}
