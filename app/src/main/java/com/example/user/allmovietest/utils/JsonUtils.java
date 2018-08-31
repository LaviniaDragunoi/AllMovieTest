package com.example.user.allmovietest.utils;

import com.example.user.allmovietest.movies.MovieObject;
import com.example.user.allmovietest.reviews.ReviewObject;
import com.example.user.allmovietest.trailers.TrailerObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This class helps to manage all the Json's String changes, from the moment that is formed the
 * request until all the data are parsed and fetched from the Json String.
 */
public class JsonUtils {

    private static final String LOG_TAG = JsonUtils.class.getName();

    //Constants that will help to parse Json String
    private static final String ROOT_JSON = "results";
    private static final String RATE = "vote_average";
    private static final String VOTE_COUNT = "vote_count";
    private static final String IMAGE = "poster_path";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String POPULARITY = "popularity";
    private static final String MOVIE_ID = "id";
    private static final String REVIEW_AUTHOR = "author";
    private static final String REVIEW_CONTENT = "content";
    private static final String TRAILER_KEY = "key";
    private static final String TRAILER_NAME = "name";

    //Constants for creating the URL
    private static final String BASE_URL
            = "https://api.themoviedb.org/3/movie";
    private static final String PARAM_KEY = "api_key";
    private static final String API_KEY = "your API_KEY goes here";

    /**
     * This method will create the URL that will be send in the fetchData method
     *
     * @param selection is the parameter that will be put to sortBy user's preferences
     * @return the URL concatenated in this method
     */
    public static URL getUrlResponse(String selection) throws IOException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addPathSegment(selection)
                .addQueryParameter(PARAM_KEY, API_KEY)
                .build();

        return urlBuilder.build().url();

    }

    /**
     * This method will create the URL for each movie Id and it will concatenated with video or
     * reviews that will be send in the fetchData method
     *
     * @param selection is the parameter that will be put(video or reviews)
     * @return the URL concatenated in this method
     */
    public static URL getUrlResponseById(String id, String selection) throws IOException {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addPathSegment(id)
                .addEncodedPathSegment(selection)
                .addQueryParameter(PARAM_KEY, API_KEY)
                .build();

        return urlBuilder.build().url();

    }

    /**
     * This method will parse the Json String received from the fetchData method,
     * that has all movie's info.
     *
     * @param json is the string received from the fetchData that was an response converted
     *             in a string
     * @return a MovieObject list that has all needed param that will be used to populate the UI
     * later in the main layout.
     */
    public static List<MovieObject> parseMovieJson(String json) {
        //Create an empty arrayList of MovieObjects in which we can start adding object to.
        List<MovieObject> moviesList = new ArrayList<>();
        //The Json string parsing.
        try {
            //Create JsonObject from json String.
            JSONObject rootJsonObject = new JSONObject(json);
            //Extract "results" Array that has all information that we need to parse
            JSONArray rootArray = rootJsonObject.getJSONArray(ROOT_JSON);
            //Extract from each element of array information needed to create a movieObjectList and
            // for each movieObject all the details needed.
            for (int i = 0; i < rootArray.length(); i++) {
                JSONObject movieJsonObject = rootArray.getJSONObject(i);
                String originalTitle = movieJsonObject.getString(ORIGINAL_TITLE);

                int voteCount = movieJsonObject.getInt(VOTE_COUNT);

                String moviePoster = movieJsonObject.getString(IMAGE);

                String overview = movieJsonObject.getString(OVERVIEW);

                double rating = movieJsonObject.getDouble(RATE);

                double popularity = movieJsonObject.getDouble(POPULARITY);

                String releaseDate = movieJsonObject.getString(RELEASE_DATE);

                int movieId = movieJsonObject.getInt(MOVIE_ID);

                moviesList.add(new MovieObject(originalTitle, moviePoster, overview, rating,
                        popularity, releaseDate, voteCount, movieId));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return moviesList;
    }

    /**
     * This method will parse the Json String received from the fetchData method,
     * that has all reviews info.
     *
     * @param json is the string received from the fetchData that was a response converted
     *             in a string
     * @return a ReviewObject list that has all needed param that will be used to populate the UI
     * later in the detail layout.
     */
    public static List<ReviewObject> parseReviewJson(String json) {
        List<ReviewObject> reviewsList = new ArrayList<>();
        try {
            JSONObject rootReviewJsonObject = new JSONObject(json);
            JSONArray rootReviewsArray = rootReviewJsonObject.getJSONArray(ROOT_JSON);
            for (int i = 0; i < rootReviewsArray.length(); i++) {
                JSONObject reviewsJsonObject = rootReviewsArray.getJSONObject(i);
                String reviewsAuthor = reviewsJsonObject.getString(REVIEW_AUTHOR);
                String reviewsContent = reviewsJsonObject.getString(REVIEW_CONTENT);
                reviewsList.add(new ReviewObject(reviewsAuthor, reviewsContent));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewsList;
    }

    /**
     * This method will parse the Json String received from the fetchData method,
     * that has all trailer's info.
     *
     * @param json is the string received from the fetchData that was a response converted
     *             in a string
     * @return a TrailerObject list that has all needed param that will be used to populate the UI
     * later in the detail layout.
     */
    public static List<TrailerObject> parseTrailerJson(String json) {
        List<TrailerObject> trailersList = new ArrayList<>();
        try {
            JSONObject rootTrailerJsonObject = new JSONObject(json);
            JSONArray rootTrailersArray = rootTrailerJsonObject.getJSONArray(ROOT_JSON);
            for (int i = 0; i < rootTrailersArray.length(); i++) {
                JSONObject trailersJsonObject = rootTrailersArray.getJSONObject(i);

                String trailerKey = trailersJsonObject.getString(TRAILER_KEY);
                String trailerName = trailersJsonObject.getString(TRAILER_NAME);
                trailersList.add(new TrailerObject(trailerKey, trailerName));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailersList;
    }

    /**
     * This method helps to fetch data
     *
     * @param requestUrl
     * @return
     */
    public static Response fetchData(String requestUrl) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(requestUrl)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}