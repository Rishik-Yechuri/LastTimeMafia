package com.example.lasttimemafia;

import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;


public class getTime {
static BufferedReader br = joinedGame.br;
static PrintWriter out = joinedGame.out;
static Socket socket = joinedGame.socket;
    public static void main(String args[]){
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public static String receiveMessage(Socket socket) throws IOException, InterruptedException {
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.d("timegamestarting","Inside The method 2");
        boolean loopRecceiveMessage = true;
        Log.d("timegamestarting","Inside The method 3");
        String receivedMessage = "";
        Log.d("timegamestarting","Inside The method 4");
        while (loopRecceiveMessage) {
            Log.d("timegamestarting","Inside The method 5");
            Thread.sleep(200);
            Log.d("timegamestarting","Inside The method 6");
            receivedMessage = br.readLine();
            Log.d("timegamestarting","Inside The method:" + receivedMessage);
            if (receivedMessage != null) {
                loopRecceiveMessage = false;
            }
        }
        return receivedMessage;
    }

    public static double returnTime(){
        sendMessage("time");
        String timeTemp = "";
        try { timeTemp = receiveMessage(socket); } catch (IOException e) { e.printStackTrace(); } catch (InterruptedException e) { e.printStackTrace(); }
        Log.d("almostdone","Time2:" + timeTemp);
        double time = Double.valueOf(timeTemp);
        Log.d("almostdone","Time3:" + time);
        return  time;
    }

    public static void sendMessage(String message) {
        out.println(message);
        Log.d("hostGame2", "This is the Message:" + message);
    }
}
