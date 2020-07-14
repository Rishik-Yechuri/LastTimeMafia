package com.example.lasttimemafia;

public class KeepTrackOfPorts {
    int currentPort;
    public KeepTrackOfPorts(){
        currentPort = 5001;
    }
    public int returnCurrentPort(){
        int tempCurrentPort = currentPort;
        currentPort++;
        return tempCurrentPort;
    }
}
