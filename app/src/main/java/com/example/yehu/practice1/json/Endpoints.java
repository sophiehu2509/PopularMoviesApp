package com.example.yehu.practice1.json;

/**
 * Created by yehu on 7/14/16.
 */
import com.example.yehu.practice1.MyApplication;

import static com.example.yehu.practice1.UrlEndpoints.URL_BOX_OFFICE;
import static com.example.yehu.practice1.UrlEndpoints.URL_CHAR_AMEPERSAND;
import static com.example.yehu.practice1.UrlEndpoints.URL_CHAR_QUESTION;
import static com.example.yehu.practice1.UrlEndpoints.URL_PARAM_API_KEY;
import static com.example.yehu.practice1.UrlEndpoints.URL_PARAM_LIMIT;


public class Endpoints {
    public static String getRequestUrl(int limit) {

        return URL_BOX_OFFICE
                + URL_CHAR_QUESTION
                + URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES
                + URL_CHAR_AMEPERSAND
                + URL_PARAM_LIMIT + limit;
    }
}

