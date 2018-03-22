package com.example.user.allmovietest.movies;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.user.allmovietest.utils.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Lavinia Dragunoi on 2/25/2018.
 * this class helps to move on the background thread those actions that could cause malfunction
 * to the device
 */

public class MovieLoader extends AsyncTaskLoader<List<MovieObject>> {
    private String murl;

    public MovieLoader(Context context, String url) {
        super(context);
        murl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<MovieObject> loadInBackground() {
        if (murl == null) return null;
        List<MovieObject> movieObjectList = new ArrayList<>();
        try {
            Response response = JsonUtils.fetchData(murl);
            movieObjectList = JsonUtils.parseMovieJson(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movieObjectList;
    }
}

