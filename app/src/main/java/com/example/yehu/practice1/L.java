package com.example.yehu.practice1;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by yehu on 7/9/16.
 */
public class L {

    public static void m(String message) {
        Log.d("VIVZ", "" + message);
    }

    public static void t(Context context, String message){
        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
        Log.d("VIVZ", "" + message);
    }

    public static void T(Context context, String message){
        Toast.makeText(context, message + "", Toast.LENGTH_LONG).show();
    }
}
