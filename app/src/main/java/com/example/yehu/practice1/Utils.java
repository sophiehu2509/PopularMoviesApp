package com.example.yehu.practice1;

import org.json.JSONObject;

/**
 * Created by yehu on 7/14/16.
 */
public class Utils {
    public static boolean contains(JSONObject jsonObject, String key) {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key) ? true : false;
    }
}
