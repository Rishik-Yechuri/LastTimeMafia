package com.example.lasttimemafia;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.example.lasttimemafia.SettingsMenu.GAME_PREFERENCES;
import static com.example.lasttimemafia.SettingsMenu.preferences;

public class hostGame extends AppCompatActivity {
    //Comtinue regular stuff
    public static int totalNumOfPlayers = 2;
    public static int currentNumOfPlayers = 0;
    TextView code;
    String token = "";
    long time = 0;
    long startTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Looper.prepare();
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);
        if (savedInstanceState == null) {
            preferences = getSharedPreferences(GAME_PREFERENCES, MODE_PRIVATE);
            token = MyFirebaseMessagingService.getToken(getApplicationContext());
            Log.d("firstorsecond", "HostGame");
            final ProgressBar progressBar = findViewById(R.id.progressBar2);
            NetwworkCaller c1 = new NetwworkCaller();
            String test = "";
            final MafiaNetworkCode giveUp = new MafiaNetworkCode();
            if (SettingsMenu.getDefaults("wan", "false").equals("true")) {
                NetwworkCaller.WANmode = true;
                MafiaNetworkCode.WANmode = true;
            }
            try {
                if (SettingsMenu.getDefaults("wan", "false").equals("true")) {
                    test = String.valueOf(createGame(MyFirebaseMessagingService.getToken(getApplicationContext())));
                    //KeepTrackOfPorts keepTrack = new KeepTrackOfPorts();
                } else {
                    test = giveUp.convertIP();
                }
                //Log.d("IP", "This is the IP" + test);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            try {
                c1.main(getApplicationContext());
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

            //preferences = getSharedPreferences(GAME_PREFERENCES, MODE_PRIVATE);
            int tempMafia = Integer.parseInt(SettingsMenu.getDefaults("mafia", "0"));
            int tempVillager = Integer.parseInt(SettingsMenu.getDefaults("villager", "0"));
            int tempAngel = Integer.parseInt(SettingsMenu.getDefaults("angel", "0"));
            Log.d("international", "Host First");
            MafiaServerGame.numOfAngels = tempAngel;
            MafiaServerGame.numOfMafia = tempMafia;
            MafiaServerGame.numOfVillagers = tempVillager;
            totalNumOfPlayers = tempAngel + tempMafia + tempVillager;
            MafiaNetworkCode.totalNumOfPlayers = totalNumOfPlayers;
            progressBar.setMax(Integer.valueOf(totalNumOfPlayers));
            //Log.d("hostGame","Hey: " + ipAdress);
            code = (TextView) findViewById(R.id.textView13);
            if (SettingsMenu.getDefaults("wan", "false").equals("false")) {
                code.setText(test);
            }
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
                            finish();
                            openMafiaGame();
                        }
                    }
                }
            }.start();
        }
    }

    public void openMafiaGame() {
        Intent intent = new Intent(this, MafiaServerGame.class);
        startActivity(intent);
    }


    public Task<String> createGame(String token) {
        Map<String, Object> data = new HashMap<>();
        //data.put("message", message);
        //data.put("token", MyFirebaseMessagingService.getToken(getApplicationContext()));
        Log.d("spectacle", "value of context:" + getApplicationContext());
        Log.d("spectacle", "value of token:" + MyFirebaseMessagingService.getToken(getApplicationContext()));
        data.put("token", token);
        return FirebaseFunctions.getInstance()
                .getHttpsCallable("createGame")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        HashMap result = (HashMap) task.getResult().getData();
                        JSONObject res = new JSONObject(result);
                        String message = res.getString("gameID");
                        Log.d("woo", "Message pre send:" + message);
                        code.setText(message);
                        return message;
                    }
                });
    }
}