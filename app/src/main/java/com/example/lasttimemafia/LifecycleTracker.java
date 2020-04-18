package com.example.lasttimemafia;

import java.util.ArrayList;

public class LifecycleTracker {
    public static ArrayList<String> lifeCycle = new ArrayList();
    public static int placeInLifecycle = 0;

    public static void main(String args[]) {

    }

    public static String returnNextActivity() {
        String returnThis = lifeCycle.get(placeInLifecycle);
        if (placeInLifecycle + 1 >= lifeCycle.size()) {
            placeInLifecycle = 0;
        } else {
            placeInLifecycle++;
        }
        return returnThis;
    }

    public static void setLifecycle(ArrayList lifecycle) {
        lifeCycle = lifecycle;
    }
}
