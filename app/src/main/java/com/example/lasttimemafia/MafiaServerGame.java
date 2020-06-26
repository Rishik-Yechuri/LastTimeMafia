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

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static com.example.lasttimemafia.SettingsMenu.GAME_PREFERENCES;
import static com.example.lasttimemafia.SettingsMenu.preferences;

public class MafiaServerGame extends AppCompatActivity {
    public static boolean addedRoles = false;
    public static int numOfConfirms = 0;
    public static int numOfMafia = 0;
    public static int numOfVillagers = 0;
    public static int numOfAngels = 0;
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
    public static HashMap<String, String> holdVotingInfo = new HashMap<>();

    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mafia_server_game);
        if (savedInstanceState == null) {
            //Looper.prepare();
            // players.add("Vihaan");

            //players.add("Vihaan");

            //players.add("Prathad");
            preferences = getSharedPreferences(GAME_PREFERENCES, MODE_PRIVATE);
            SettingsMenu.editor = preferences.edit();
        /*int tempMafia = Integer.parseInt(SettingsMenu.getDefaults("mafia", "0"));
        int tempVillager = Integer.parseInt(SettingsMenu.getDefaults("villager", "0"));
        int tempAngel = Integer.parseInt(SettingsMenu.getDefaults("angel", "0"));
        MafiaServerGame.numOfAngels = tempAngel;
        MafiaServerGame.numOfMafia = tempMafia;
        MafiaServerGame.numOfVillagers = tempVillager;*/
            //for(int x=0;x<numOfVillagers;x++){role.add("villager");}for(int x=0;x<numOfMafia;x++){role.add("mafia");}for(int x=0;x<numOfAngels;x++){role.add("guardian angel");}
            boolean pauseForPlayers = true;
            while (pauseForPlayers) {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (players.size() == hostGame.totalNumOfPlayers) {
                    MafiaNetworkCode.startTimeOfTimer = System.currentTimeMillis();
                    pauseForPlayers = false;
                }
            }
            Collections.shuffle(players);
            Log.d("random", "Players: " + players);
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

    public static void addRolesToList() {
        int tempMafia = Integer.parseInt(SettingsMenu.getDefaults("mafia", "0"));
        int tempVillager = Integer.parseInt(SettingsMenu.getDefaults("villager", "0"));
        int tempAngel = Integer.parseInt(SettingsMenu.getDefaults("angel", "0"));
        MafiaServerGame.numOfAngels = tempAngel;
        MafiaServerGame.numOfMafia = tempMafia;
        MafiaServerGame.numOfVillagers = tempVillager;
        Log.d("ilolveit", "Hehe");
        Log.d("international", "Server First First");
        if (!addedRoles) {
            Log.d("ilolveit", "numOfMafia:" + numOfMafia);
            for (int x = 0; x < numOfVillagers; x++) {
                Log.d("ilolveit", "villager added");
                role.add("villager");
            }
            for (int x = 0; x < numOfMafia; x++) {
                Log.d("ilolveit", "mafia added");
                role.add("mafia");
            }
            for (int x = 0; x < numOfAngels; x++) {
                Log.d("ilolveit", "Angel added");
                role.add("guardian angel");
            }
            addedRoles = true;
            Log.d("goingcrazy", "roles added");
        }
    }
    @Override
    public void onBackPressed() {
        openDialog();
    }

    public void openDialog() {
        ConfirmGoPackDialog goBack = new ConfirmGoPackDialog(MafiaServerGame.this);
        goBack.show(getSupportFragmentManager(), "litty");
    }
}