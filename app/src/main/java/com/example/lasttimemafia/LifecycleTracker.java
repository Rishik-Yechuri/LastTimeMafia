package com.example.lasttimemafia;

import android.util.Log;

import java.util.ArrayList;

public class LifecycleTracker {
    public static ArrayList<String> lifeCycle = new ArrayList();
    public static int placeInLifecycle = 0;

    public static void main(String args[]) {

    }

    public static String returnNextActivity() {
        Log.d("plain3", "Called");
        Log.d("outbounds", "placeinlifecyclereal:" + placeInLifecycle);
       /* String returnThis = lifeCycle.get(placeInLifecycle);
        if (placeInLifecycle + 1 >= lifeCycle.size()) {
            placeInLifecycle = 0;

        } else {
            placeInLifecycle++;

        }
        return returnThis;*
        */
        String returnThis = "";
        if (placeInLifecycle + 1 >= lifeCycle.size()) {
            returnThis = lifeCycle.get(0);
        }else{
            returnThis = lifeCycle.get(placeInLifecycle+1);
        }
        placeInLifecycle++;
        return returnThis;
    }

    public static void setLifecycle(ArrayList lifecycle) {
        lifeCycle = lifecycle;
    }

    public static int getNumberOfActivity() {
        //Log.d("outbounds","placeinlifecycle:" + placeInLifecycle);
        int position = placeInLifecycle;//lifeCycle.indexOf(lifeCycle.get(placeInLifecycle/*-1*/));
        return position;
    }
}
