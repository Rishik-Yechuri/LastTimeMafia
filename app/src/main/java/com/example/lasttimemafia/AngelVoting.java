package com.example.lasttimemafia;


import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.example.lasttimemafia.ReavealRole.receiveMessage;
import static com.example.lasttimemafia.joinedGame.sendMessage;
import static com.example.lasttimemafia.joinedGame.socket;

public class AngelVoting extends AppCompatActivity {
    ArrayList<Button> holdButtons = new ArrayList<Button>();
    ArrayList<String> playerNames = new ArrayList<>();
    Button btnTag;
    boolean voted = false;
    TextView getRole;
    String nextThing = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angel_voting);
        LinearLayout holdVoting = findViewById(R.id.holdVotingButtons);
        getRole = findViewById(R.id.getrole);
        nextThing = LifecycleTracker.returnNextActivity();
        sendMessage("getplayers");
        String tempPlayerList = "";
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
        for (int x = 0; x < playerNames.size(); x++) {
            btnTag = new Button(this);
            btnTag.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            btnTag.setBackgroundResource(R.drawable.circularbutton);
            holdButtons.add(btnTag);
            holdVoting.addView(btnTag);
            Space space = new Space(this);
            //space.setMinimumHeight(500);
            space.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            space.setMinimumHeight(dpToPx(20));
            holdVoting.addView(space);
        }
        for (int p = 0; p < playerNames.size(); p++) {
            Log.d("textdebug", "PlayName.length:" + playerNames.size());
            Log.d("textdebug", "playerName value:" + playerNames.get(p));
            holdButtons.get(p).setText(playerNames.get(p));
        }
        for (Button btn : holdButtons) {
            btn.setOnClickListener(sendVote);
        }
        sendMessage("startalarmandchecktime 10");
        long countdownTimer = 0;
        try {
            countdownTimer = Long.parseLong(receiveMessage(socket));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        new CountDownTimer(countdownTimer, 500) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                openCloseEyes();
            }
        }.start();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private View.OnClickListener sendVote = new View.OnClickListener() {
        public void onClick(View v) {
            Button b = (Button) v;
            if (!voted) {
                sendMessage("getroleofperson" + " " + b.getText().toString());

                String theRole = "";
                try {
                    theRole = receiveMessage(socket);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                getRole.setText(theRole);
            }
        }
    };
    public void openCloseEyes() {
        Intent intent = new Intent(this, CloseYourEyes.class);
        startActivity(intent);
    }
}
