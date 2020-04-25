package com.example.lasttimemafia;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import static com.example.lasttimemafia.joinedGame.sendMessage;
import static com.example.lasttimemafia.ReavealRole.receiveMessage;
import static com.example.lasttimemafia.joinedGame.socket;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.lasttimemafia.R;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class VillageVoting extends AppCompatActivity {
    String nextThing = "";
    int endNumber;
    boolean confirmed = false;
    Thread thread;
    Handler handler;
    boolean keepThreadRunning = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_villager_voting);
        nextThing = LifecycleTracker.returnNextActivity();
        Log.d("pleaseplease","NextThing:" + nextThing);
        LinearLayout rootView = findViewById(R.id.realHoldsText);
        endNumber = 0;
        ArrayList<Button> holdButtons = new ArrayList<Button>();
        ArrayList<Button> holdButtons2 = new ArrayList<Button>();
        Button btnTag;
        Space space2 = new Space(this);
        space2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        space2.setMinimumHeight(dpToPx(20));
        rootView.addView(space2);
        ArrayList<String> playerNames = new ArrayList<>();
        sendMessage("getplayers");
        String tempPlayerList = "";
        Button confirmButton = findViewById(R.id.button7);
        confirmButton.setOnClickListener(updateConfirmStatus);
        try {
            tempPlayerList = receiveMessage(socket);
            Log.d("textdebug", "tempPlayerList:" + tempPlayerList);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        String[] cutPlayers = tempPlayerList.split(" ");
        Log.d("textdebug", "cutplayers:" + Arrays.toString(cutPlayers));
        if (cutPlayers[0].equals("playerlist")) {
            for (int y = 1; y < cutPlayers.length; y++) {
                playerNames.add(cutPlayers[y]);
            }
        }
        Log.d("textdebug", "playerNames:" + playerNames);
        /*for(int p=0;p<playerNames.length;p++){
            holdButtons.get(p).setText(playerNames[p]);
        }*/

        /*for(int p=0;p<cutPlayers.length;p++){
            Button thick = holdButtons.get(0);
            //thick.setText("BP");
        }*/
        for (int x = 0; x < playerNames.size(); x++) {
            btnTag = new Button(this);
            btnTag.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            btnTag.setBackgroundResource(R.drawable.circularbutton);
            holdButtons.add(btnTag);
            rootView.addView(btnTag);
            Space space = new Space(this);
            //space.setMinimumHeight(500);
            space.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            space.setMinimumHeight(dpToPx(20));
            rootView.addView(space);
        }
        for (int p = 0; p < playerNames.size(); p++) {
            Log.d("textdebug", "PlayName.length:" + playerNames.size());
            Log.d("textdebug", "playerName value:" + playerNames.get(p));
            holdButtons.get(p).setText(playerNames.get(p));
        }
        for (Button btn : holdButtons) {
            btn.setOnClickListener(sendVote);
        }
        thread = new Thread(new MyThread());
        Log.d("booleanchecker", "Before thread start");
        sendMessage("resetnumofconfirms");
        thread.start();
        Log.d("booleanchecker", "After thread start call");
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String value = msg.obj.toString();
                Log.d("booleanchecker", "value:" + value);
                if (Boolean.parseBoolean(value) && keepThreadRunning) {
                    Log.d("nextactivity", "About to open");
                    keepThreadRunning = false;
                    if (nextThing.equals("villagedeath")) {
                        openVillageDeath();
                    }
                }
            }
        };
    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private View.OnClickListener sendVote = new View.OnClickListener() {
        public void onClick(View v) {
            Button b = (Button) v;
            Log.d("sender", "buttonString:" + b.getText().toString());
            sendMessage("setvote" + " " + b.getText().toString() + " " + endNumber);
            endNumber++;
        }
    };
    private View.OnClickListener updateConfirmStatus = new View.OnClickListener() {
        public void onClick(View v) {
            Button b = (Button) v;
            confirmed = !confirmed;
            if (confirmed) {
                b.setText("Unconfirm");
            } else {
                b.setText("Confirm");
            }
            sendMessage("confirmclick " + confirmed);
        }
    };

    class MyThread implements Runnable {
        @Override
        public void run() {
            Log.d("booleanchecker", "In thread");
            Message messageOfficial = Message.obtain();
            Object kring = "true";
            while (keepThreadRunning) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("booleanchecker", "Before receive");
                sendMessage("checkconfirmstatus");
                try {
                    kring = receiveMessage(socket);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("booleanchecker", "After receive");
                messageOfficial = Message.obtain();
                messageOfficial.obj = kring;
                handler.sendMessage(messageOfficial);
            }
        }
    }

    public void openVillageDeath() {
        Log.d("nextactivity", "In openvillagedeath");
        Intent intent = new Intent(this, RevealDeadAfterMafia.class);
        startActivity(intent);
    }
}