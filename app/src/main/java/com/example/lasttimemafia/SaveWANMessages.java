package com.example.lasttimemafia;

import java.util.ArrayList;

public class SaveWANMessages {
    int counter = 0;
    ArrayList<String> holdMessages = new ArrayList<>();
    public SaveWANMessages(){
    }
    public void injectMessage(String message){
        holdMessages.add(message);
    }
    public String returnMessage(){
        int tempNum = counter;
        counter++;
        return holdMessages.get(tempNum);
    }
}
