package com.example.lasttimemafia;

import android.util.Log;

import java.util.ArrayList;

public class SaveWANMessages {
    int counter = 0;
    ArrayList<String> holdMessages = new ArrayList<>();

    public SaveWANMessages() {
    }

    public void injectMessage(String message) {
        holdMessages.add(message);
    }

    public String returnMessage() throws InterruptedException {
        Log.d("gamernation", "In SaveWAN 1");
        int tempNum = counter;
        Log.d("gamernation", "In SaveWAN 2");
        Log.d("gamernation","Value of tempNum:" + tempNum);
        Log.d("gamernation","value of holdMessages:" + holdMessages.toString());
        //String thingToReturn = holdMessages.get(tempNum);
        while(tempNum >= holdMessages.size()){
            Thread.sleep(200);
        }
        return holdMessages.get(tempNum);
        /*if(tempNum < holdMessages.size()) {
            Log.d("gamernation", "In SaveWAN 4");
            counter++;
            Log.d("gamernation", "In SaveWAN 5");
            return holdMessages.get(tempNum);
        }else{
            Log.d("gamernation", "In SaveWAN 6");
            Thread.sleep(200);
            Log.d("gamernation", "In SaveWAN 7");
            return holdMessages.get(tempNum);
        }*/
        //return holdMessages.get(tempNum);
    }
}
