package com.example.user.allmovietest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.user.allmovietest.data.FavoriteContract.FavoriteEntry;

/**
 * Created by Lavinia Dragunoi on 3/20/2018.
 */

public class FavoriteDBHelper extends SQLiteOpenHelper {

    //the name of the database
    private static final String DATABASE_NAME = "favorites.db";
    //the version number of the database schema
    private static final int DATABASE_VERSION = 1;

    /**
     * The databaseHelper constructor
     *
     * @param context
     */
    public FavoriteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This onCreate method is called when the database is first created
     *
     * @param db
     */
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " +
                FavoriteEntry.TABLE_NAME + " (" +
                FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteEntry.COLUMN_ORIGINAL_TITLE + " TEXT," +
                FavoriteEntry.COLUMN_MOVIE_ID + " INTEGER," +
                FavoriteEntry.COLUMN_MOVIE_POSTER + " TEXT," +
                FavoriteEntry.COLUMN_OVERVIEW + " TEXT," +
                FavoriteEntry.COLUMN_RATING + " REAL," +
                FavoriteEntry.COLUMN_POPULARITY + " REAL," +
                FavoriteEntry.COLUMN_VOTE_COUNT + " INTEGER," +
                FavoriteEntry.COLUMN_RELEASE_DATE + " TEXT);";

        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    /**
     * This method is called when the database has to be updated, in this case the old database
     * will be discards and a new one will be created.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
