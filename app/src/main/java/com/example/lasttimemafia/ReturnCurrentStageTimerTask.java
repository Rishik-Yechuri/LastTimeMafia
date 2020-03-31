package com.example.lasttimemafia;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ReturnCurrentStageTimerTask{
public static long startingTime = System.currentTimeMillis();
public static long currentTime = 0;
public static long time = currentTime-startingTime;
boolean setOriginalTime = true;
    public void run() {
       currentTime = System.currentTimeMillis();
       time = currentTime - startingTime;
    }

    private String mainMethod() {
        return "To Be Finished...";
    }

    public static void main(String args[]){
    }

}