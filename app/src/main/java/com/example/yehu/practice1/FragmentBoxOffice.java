package com.example.yehu.practice1;


import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yehu.practice1.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.yehu.practice1.UrlEndpoints.URL_BOX_OFFICE;
import static com.example.yehu.practice1.UrlEndpoints.URL_CHAR_AMEPERSAND;
import static com.example.yehu.practice1.UrlEndpoints.URL_CHAR_QUESTION;
import static com.example.yehu.practice1.UrlEndpoints.URL_PARAM_API_KEY;
import static com.example.yehu.practice1.UrlEndpoints.URL_PARAM_LIMIT;

import static com.example.yehu.practice1.Keys.EndpointBoxOffice.*;




/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentBoxOffice#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBoxOffice extends Fragment implements SortListener, BoxOfficeMoviesLoadedListener, SwipeRefreshLayout.OnRefreshListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String STATE_MOVIES = "state_movies";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private ArrayList<Movie> listMovies = new ArrayList<>();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private AdapterBoxOffice adapterBoxOffice;
    private RecyclerView listMovieHits;
    private TextView textVolleyError;
    private MovieSorter movieSorter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public FragmentBoxOffice() {
        // Required empty public constructor
        movieSorter = new MovieSorter();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentBoxOffice.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentBoxOffice newInstance(String param1, String param2) {
        FragmentBoxOffice fragment = new FragmentBoxOffice();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onMoviesLoaded() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_box_office, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeMovieHits);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        textVolleyError = (TextView) view.findViewById(R.id.textVolleyError);
        listMovieHits = (RecyclerView) view.findViewById(R.id.listMovieHits);
        listMovieHits.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterBoxOffice = new AdapterBoxOffice(getActivity());
        listMovieHits.setAdapter(adapterBoxOffice);
        if (savedInstanceState != null) {
            listMovies = savedInstanceState.getParcelableArrayList(STATE_MOVIES);

        } else {
            L.m("HI");
            listMovies = MyApplication.getWritableDatabase().getAllMoviesBoxOffice();
            if (listMovies.isEmpty()) {
                L.t(getActivity(), "executing task from fragment");
                new TaskLoadMoviesBoxOffice(this).execute();
            }
        }
        adapterBoxOffice.setMovies(listMovies);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MOVIES, listMovies);
    }


    private void handleVolleyError(VolleyError error) {
        textVolleyError.setVisibility(View.VISIBLE);
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            textVolleyError.setText(R.string.error_timeout);

        } else if (error instanceof AuthFailureError) {
            textVolleyError.setText(R.string.error_auth_failure);
            //TODO
        } else if (error instanceof ServerError) {
            textVolleyError.setText(R.string.error_auth_failure);
            //TODO
        } else if (error instanceof NetworkError) {
            textVolleyError.setText(R.string.error_network);
            //TODO
        } else if (error instanceof ParseError) {
            textVolleyError.setText(R.string.error_parser);
            //TODO
        }
    }


    public void onSortByName() {
        movieSorter.sortMoviesByName(listMovies);
        adapterBoxOffice.notifyDataSetChanged();

    }

    @Override
    public void onSortByDate() {
        movieSorter.sortMoviesByDate(listMovies);
        adapterBoxOffice.notifyDataSetChanged();
    }

    @Override
    public void onSortByRating() {
        movieSorter.sortMoviesByRating(listMovies);
        adapterBoxOffice.notifyDataSetChanged();
    }


    @Override
    public void onBoxOfficeMoviesLoaded(ArrayList<Movie> listMovies) {
        if (mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(false);
        }
        adapterBoxOffice.setMovies(listMovies);
    }

    @Override
    public void onRefresh() {
        L.t(getActivity(), "onRefresh");
        new TaskLoadMoviesBoxOffice(this).execute();
    }
}


    /*public static String getRequestUrl(int limit){
        return URL_BOX_OFFICE + URL_CHAR_QUESTION +URL_PARAM_API_KEY +
                MyApplication.API_KEY_ROTTEN_TOMATOES+URL_CHAR_AMEPERSAND+URL_PARAM_LIMIT+limit;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();

    }



    private void sendJsonRequest(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                getRequestUrl(30), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                textVolleyError.setVisibility(View.GONE);
                listMovies = parseJSONResponse(response);
                adapterBoxOffice.setMovieList(listMovies);
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error);
            }

        });


        requestQueue.add(request);
    }


    private ArrayList<Movie> parseJSONResponse(JSONObject response){
        ArrayList<Movie> listMovies = new ArrayList<>();
       if (response != null && response.length() > 0) {

           try {

               JSONArray arrayMovies = response.getJSONArray(KEY_MOVIES);

               for (int i = 0; i < arrayMovies.length(); i++) {
                   long id = 0;
                   String title = Constants.NA;
                   String releaseDate = Constants.NA;
                   int audienceScore = -1;
                   String synopsis = Constants.NA;
                   String urlThumbnail = Constants.NA;

                   JSONObject currentMovie = arrayMovies.getJSONObject(i);

                   if (contains(currentMovie, KEY_ID)) {
                       id = currentMovie.getLong(KEY_ID);
                   }

                   if (contains(currentMovie, KEY_TITLE)) {
                       title = currentMovie.getString(KEY_TITLE);
                   }

                   if (contains(currentMovie, KEY_RELEASE_DATES)) {
                       JSONObject objectReleaseDates = currentMovie.getJSONObject(KEY_RELEASE_DATES);

                       if (contains(objectReleaseDates, KEY_THEATER)) {
                           releaseDate = objectReleaseDates.getString(KEY_THEATER);
                       }
                   }

                   JSONObject objectRating = currentMovie.getJSONObject(KEY_RATING);
                   if (contains(currentMovie, KEY_RATING)) {
                       if (contains(objectRating,KEY_AUDIENCE_SCORE)) {
                           audienceScore = objectRating.getInt(KEY_AUDIENCE_SCORE);
                       }
                   }

                   if (contains(currentMovie, KEY_POSTERS)) {

                       JSONObject objectPosters = currentMovie.getJSONObject(KEY_POSTERS);
                       if (contains(objectPosters, KEY_THUMBNAIL)) {
                           urlThumbnail = objectPosters.getString(KEY_THUMBNAIL);
                       }
                   }


                   if (contains(currentMovie, KEY_SYNOPSIS)) {
                       synopsis = currentMovie.getString(KEY_SYNOPSIS);
                   }

                   Movie movie = new Movie();
                   movie.setId(id);
                   movie.setTitle(title);
                   Date date = null;
                   try {
                       date = dateFormat.parse(releaseDate);
                   }catch (ParseException e) {

                   }
                   movie.setReleaseDateTheater(date);
                   movie.setSynopsis(synopsis);
                   movie.setAudienceScore(audienceScore);
                   movie.setUrlThumbnail(urlThumbnail);

                   if (id != -1 && !title.equals(Constants.NA)) {
                       listMovies.add(movie);
                   }

               }

               //L.T(getActivity(), listMovies.toString());

           } catch (JSONException e) {

           }
       }
        return listMovies;
    }


    public boolean contains(JSONObject jsonObject, String key){
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key)? true:false;
    }*/


