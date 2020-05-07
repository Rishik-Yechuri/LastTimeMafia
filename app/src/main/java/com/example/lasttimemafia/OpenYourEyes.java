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

public class OpenYourEyes extends AppCompatActivity {
    public static MediaPlayer openEyesAudio;
    String nextThing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_your_eyes);
        if (savedInstanceState == null) {
            Log.d("faker", "value of nextThing:" + LifecycleTracker.getNumberOfActivity());
            Log.d("faker", "Called from OpenYourEyes");
            nextThing = LifecycleTracker.returnNextActivity();
       /* if(MafiaClientGame.role.equals("mafia")){
        setContentView(R.layout.activity_open_your_eyes);
        }else{
            setContentView(R.layout.activity_close_your_eyes);
        }*/
            playAudio();
        /*double time = getTime.returnTime();
        double timeLeft = 18.6 - time;
        double tempConversion = timeLeft * 1000;
        long countdownTimer = (int)tempConversion;*/
            sendMessage("startalarmandchecktime 1.8");
            long countdownTimer = 0;
            try {
                countdownTimer = Long.parseLong(receiveMessage(socket));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("synccheck", "Value of RevealRole countdowntimer:" + countdownTimer);
            new CountDownTimer(countdownTimer, 500) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    finish();
                    if (nextThing.equals("mafiatextmessages")) {
                        openTextMessages();
                    } else if (nextThing.equals("closeeyes")) {
                        openCloseEyes();
                    } else if (nextThing.equals("showdeath")) {
                        openRevealDead();
                    } else if (nextThing.equals("angelprotection")) {
                        Log.d("lolmemes", "It actually worked");
                        openAngelProtection();
                    }
                }
            }.start();
        }
    }

    public void playAudio() {
        if (openEyesAudio != null) {
            openEyesAudio.start();
        }
    }

    public void openTextMessages() {
        Log.d("testtabs", "openeyes 1");
        Intent intent = new Intent(this, TextMessages.class);
        Log.d("testtabs", "openeyes 2");
        startActivity(intent);
    }

    public void openCloseEyes() {
        CloseYourEyes.closeEyesAudio = MediaPlayer.create(this, R.raw.closeeyes);
        Intent intent = new Intent(this, CloseYourEyes.class);
        finish();
        startActivity(intent);
    }

    public void openRevealDead() {
        Intent intent = new Intent(this, RevealDeadAfterMafia.class);
        startActivity(intent);
    }

    public void openAngelProtection() {
        Intent intent = new Intent(this, AngelVoting.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        openDialog();
    }

    public void openDialog() {
        ConfirmGoPackDialog goBack = new ConfirmGoPackDialog(getApplicationContext());
        goBack.show(getSupportFragmentManager(), "litty");
    }
}
