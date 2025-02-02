package com.example.lasttimemafia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.UnknownHostException;

import static android.content.Context.MODE_PRIVATE;
import static com.example.lasttimemafia.SettingsMenu.GAME_PREFERENCES;
import static com.example.lasttimemafia.SettingsMenu.preferences;

public class NetwworkCaller extends AppCompatActivity {
    public static boolean WANmode = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(GAME_PREFERENCES, MODE_PRIVATE);
    }

    public void main(final Context context) throws UnknownHostException {
        Log.d("international", "Networkcaller first");
        final MafiaNetworkCode c1 = new MafiaNetworkCode();
        //String[] totalistic = {"127.0.0.1"};
        //MafiaNetworkCode c1 = new MafiaNetworkCode();
        final int players = hostGame.totalNumOfPlayers;
        final int totaler = 4999;
        if (!SettingsMenu.getDefaults("wan", "false").equals("true")) {
            new Thread() {
                public void run() {
                    try {
                        c1.connectionCreater(totaler, players);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        for (int o = 0; o < players; o++) {
            final int totaler2 = 5000 + o;
            new Thread() {
                public void run() {
                    Looper.prepare();
                    try {
                        final MafiaNetworkCode c2 = new MafiaNetworkCode();
                        c2.permaConnection(totaler2,context);
                        //c1.permaConnection(totaler2,context);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}