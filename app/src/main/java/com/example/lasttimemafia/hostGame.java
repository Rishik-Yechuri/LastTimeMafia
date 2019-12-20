package com.example.lasttimemafia;
import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class hostGame extends AppCompatActivity {
    //Comtinue regular stuff
    public static int totalNumOfPlayers = 2;
    public static int currentNumOfPlayers = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Looper.prepare();
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);
        final ProgressBar progressBar =findViewById(R.id.progressBar2);
        progressBar.setMax(Integer.valueOf(totalNumOfPlayers));
        NetwworkCaller c1 = new NetwworkCaller();
        String test = "";
        final MafiaNetworkCode giveUp = new MafiaNetworkCode();
        try {
            test = giveUp.convertIP();
            Log.d("IP","This is the IP" + test);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            c1.main();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String ipAdress = inetAddress. getHostAddress();
        //Log.d("hostGame","Hey: " + ipAdress);
        TextView code = (TextView)findViewById(R.id.textView13);
        code.setText(test);
        new Thread() {
            public void run() {
                //Looper.prepare();
                boolean loop = true;
                while (loop) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBar.setProgress(currentNumOfPlayers);
                    if (currentNumOfPlayers == totalNumOfPlayers) {
                        loop = false;
                        Log.d("hostGame","OpenedMafiaGame");
                        openMafiaGame();
                    }
                }
            }
        }.start();
    }
    public void openMafiaGame(){
        Intent intent = new Intent(this, MafiaServerGame.class);
        startActivity(intent);
    }
}