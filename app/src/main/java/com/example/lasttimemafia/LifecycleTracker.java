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
        String returnThis = "";
        if (placeInLifecycle + 1 >= lifeCycle.size()) {
            returnThis = lifeCycle.get(0);
            placeInLifecycle = 0;
        }else{
            returnThis = lifeCycle.get(placeInLifecycle+1);
            placeInLifecycle++;
        }
        Log.d("loopflex","ReturnThis:" + returnThis);
        return returnThis;
    }

    public static void setLifecycle(ArrayList lifecycle) {
        lifeCycle = lifecycle;
        placeInLifecycle = 0;
    }

    public static int getNumberOfActivity() {
        int position = placeInLifecycle;//lifeCycle.indexOf(lifeCycle.get(placeInLifecycle/*-1*/));
        return position;
    }
    public static String returnCurrentActivity(){
        int pos = getNumberOfActivity();
        return lifeCycle.get(pos);
    }
    public static String returnSpecificActivity(int place){
       return lifeCycle.get(place);
    }
}
