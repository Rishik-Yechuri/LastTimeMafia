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

public class MafiaServerGame extends AppCompatActivity {
    public static ArrayList players = new ArrayList();
    public static ArrayList role = new ArrayList<>();
    public static boolean sendRole = false;
    public static ArrayList<String> textMessageSender = new ArrayList();
    public static ArrayList<String> textMessages = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        Log.d("hostGame","Intent worked and made it!");
        setContentView(R.layout.activity_mafia_server_game);
        //Looper.prepare();
        players.add("Vihaan");
        textMessageSender.add("Prathad");
        textMessages.add("Sup!");
        boolean pauseForPlayers = true;
        while(pauseForPlayers){
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(players.size()==hostGame.totalNumOfPlayers){
                pauseForPlayers=false;
            }
        }
        Collections.shuffle(players);
        Log.d("random","Players: " + players);
        role.add("mafia");
        role.add("guardian angel");
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