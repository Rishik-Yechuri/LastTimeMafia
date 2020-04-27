package com.example.lasttimemafia;

import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.example.lasttimemafia.SettingsMenu.GAME_PREFERENCES;
import static com.example.lasttimemafia.SettingsMenu.preferences;

public class hostGame extends AppCompatActivity {
    //Comtinue regular stuff
    public static int totalNumOfPlayers = 2;
    public static int currentNumOfPlayers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Looper.prepare();
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);
        Log.d("firstorsecond","HostGame");
        final ProgressBar progressBar = findViewById(R.id.progressBar2);
        NetwworkCaller c1 = new NetwworkCaller();
        String test = "";
        final MafiaNetworkCode giveUp = new MafiaNetworkCode();
        try {
            test = giveUp.convertIP();
            Log.d("IP", "This is the IP" + test);
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
        String ipAdress = inetAddress.getHostAddress();
        /*try {
            Log.d("cuthardcoding","We're in");
            Log.d("firstplace","In hostGame");
            FileInputStream fin = openFileInput("settings.txt");
            int c;
            String temp = "";
            Log.d("cuthardcoding","We're in 2");
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            Log.d("cuthardcoding","We're in 3");
            String[] allValues = temp.split(" ");
            int total = 0;
            for (int x = 0; x < allValues.length; x++) {
                Log.d("cuthardcoding","Value of allValues:" + Arrays.toString(allValues));
                total += Integer.parseInt(allValues[x]);
            }
            Log.d("playerchecks","Toalnumofplayers:" + total);
            totalNumOfPlayers = total;
        } catch (Exception ignored) {
            Log.d("caughtit","In hostagame");
        }*/
        preferences = getSharedPreferences(GAME_PREFERENCES, MODE_PRIVATE);
        int tempMafia  = Integer.parseInt(SettingsMenu.getDefaults("mafia","0"));
        int tempVillager  = Integer.parseInt(SettingsMenu.getDefaults("villager","0"));
        int tempAngel  = Integer.parseInt(SettingsMenu.getDefaults("angel","0"));
        Log.d("international","Host First");
        MafiaServerGame.numOfAngels = tempAngel;
        MafiaServerGame.numOfMafia = tempMafia;
        MafiaServerGame.numOfVillagers = tempVillager;
        totalNumOfPlayers = tempAngel+tempMafia+tempVillager;
        MafiaNetworkCode.totalNumOfPlayers = totalNumOfPlayers;
        progressBar.setMax(Integer.valueOf(totalNumOfPlayers));
        //Log.d("hostGame","Hey: " + ipAdress);
        TextView code = (TextView) findViewById(R.id.textView13);
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
                        Log.d("hostGame", "OpenedMafiaGame");
                        openMafiaGame();
                    }
                }
            }
        }.start();
    }

    public void openMafiaGame() {
        Intent intent = new Intent(this, MafiaServerGame.class);
        startActivity(intent);
    }
}