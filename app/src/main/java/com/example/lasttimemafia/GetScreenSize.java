package com.example.lasttimemafia;

import android.content.Context;
import android.util.DisplayMetrics;

public class GetScreenSize {
    public void GetScreenSize() {
    }
    public static float getDimensionInDP(Context context, String dimension) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpValue = 0;
        if (dimension.equals("height")) {
            dpValue = displayMetrics.heightPixels / displayMetrics.density;
        }else if(dimension.equals("width")){
            dpValue = displayMetrics.widthPixels / displayMetrics.density;
        }
        return dpValue;
    }
}
