package com.example.lasttimemafia;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lasttimemafia.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MafiaServerGame extends AppCompatActivity {
    public static int numOfConfirms = 0;
    public static boolean personKilled = false;
    public static long startTimeOfTimer = System.currentTimeMillis();
    public static long totalTimePassed = 0;
    public static long alarmLength = 0;
    public static long alarmEndTime = 0;
    public static long lastAlarmEndTime = 0;
    ////////////////////////////////////////////
    public static int numOfPlayersAlive = 0;
    public static long startingTime = System.currentTimeMillis();
    public static ArrayList players = new ArrayList();
    public static ArrayList<String> role = new ArrayList();
    public static boolean sendRole = false;
    public static int lastActivityRestartCalledNumber = -1;
    public static ArrayList<String> textMessageSender = new ArrayList();
    public static ArrayList<String> textMessages = new ArrayList<>();
    public static HashMap<String,String> holdVotingInfo = new HashMap<>();
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        Log.d("hostGame","Intent worked and made it!");
        setContentView(R.layout.activity_mafia_server_game);
        //Looper.prepare();
        players.add("Vihaan");
        players.add("Prathad");
       /* players.add("kring");
        players.add("hehe");
        players.add("akash");*/
        role.add("mafia");
        role.add("guardian angel");
        //textMessageSender.add("Prathad");
        //textMessages.add("");
        boolean pauseForPlayers = true;
        while(pauseForPlayers){
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(players.size()==hostGame.totalNumOfPlayers){
                MafiaNetworkCode.startTimeOfTimer = System.currentTimeMillis();
                pauseForPlayers=false;
            }
        }
        //Collections.shuffle(players);
        Log.d("random","Players: " + players);
        sendRole = true;
        TextView textView5 = findViewById(R.id.textView5);
        String playerName = (String) players.get(0);


        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                    }
                },
                5000
        );
    }


}