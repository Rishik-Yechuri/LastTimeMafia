package com.example.lasttimemafia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import java.io.IOException;

import static com.example.lasttimemafia.ReavealRole.receiveMessage;
import static com.example.lasttimemafia.joinedGame.sendMessage;
import static com.example.lasttimemafia.joinedGame.socket;

public class CloseYourEyes extends AppCompatActivity {
    public static MediaPlayer closeEyesAudio;
    String nextThing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_your_eyes);
       /* double time = getTime.returnTime();
        double timeLeft = 16 - time;
        double tempConversion = timeLeft * 1000;
        long countdownTimer = (int)tempConversion;
        Log.d("successtest","Time2:" + getTime.returnTime());*/
        nextThing = LifecycleTracker.returnNextActivity();
        if (nextThing.equals("openeyes")) {
            sendMessage("startalarmandchecktime 4.0");
        } else if (nextThing.equals("closeeyes")) {
            sendMessage("startalarmandchecktime 35.0");
        }
        else if (nextThing.equals("mafiaopeneyes")) {
            sendMessage("startalarmandchecktime 4.0");
        }
        long countdownTimer = 0;
        try {
            countdownTimer = Long.parseLong(receiveMessage(socket));
            Log.d("synccheck", "Value of RevealRole countdowntimer:" + countdownTimer);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        if (nextThing.equals("mafiaopeneyes")) {
            playAudio();
        } else if(nextThing.equals("closeeyes")){
            //Do nothing
        }else if(nextThing.equals("openeyes")){
            playAudio();
        }
        //playAudio();
        new CountDownTimer(countdownTimer, 500) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (nextThing.equals("openeyes")) {
                    openOpenEyes();
                } else if (nextThing.equals("closeeyes")) {
                    openCloseEyes();
                }else if(nextThing.equals("mafiaopeneyes")){
                    openOpenEyes();
                }
            }
        }.start();
    }

    public void playAudio() {
        if (closeEyesAudio == null) {
        }
        closeEyesAudio.start();
    }

    public void openOpenEyes() {
        if (nextThing.equals("openeyes")) {
            OpenYourEyes.openEyesAudio = MediaPlayer.create(this, R.raw.everyoneopeneyes);
        } else if (nextThing.equals("mafiaopeneyes")) {
            OpenYourEyes.openEyesAudio = MediaPlayer.create(this, R.raw.openeyes);
        }
        Intent intent = new Intent(this, OpenYourEyes.class);
        startActivity(intent);
    }

    public void openCloseEyes() {
        CloseYourEyes.closeEyesAudio = MediaPlayer.create(this, R.raw.closeeyes);
        Intent intent = new Intent(this, CloseYourEyes.class);
        startActivity(intent);
    }
}
