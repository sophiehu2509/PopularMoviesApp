package com.example.yehu.practice1;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;
import com.example.yehu.practice1.json.Endpoints;
import com.example.yehu.practice1.MyApplication;
import com.example.yehu.practice1.json.Parser;
import com.example.yehu.practice1.json.Requestor;
import com.example.yehu.practice1.Movie;

/**
 * Created by yehu on 7/14/16.
 */
public class MovieUtils {
    public static ArrayList<Movie> loadBoxOfficeMovies(RequestQueue requestQueue) {
        JSONObject response = Requestor.sendRequestBoxOfficeMovies(requestQueue, Endpoints.getRequestUrl(30));
        ArrayList<Movie> listMovies = Parser.parseJSONResponse(response);
        MyApplication.getWritableDatabase().insertMoviesBoxOffice(listMovies, true);
        return listMovies;
    }
}