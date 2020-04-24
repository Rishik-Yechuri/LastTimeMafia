package com.example.lasttimemafia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import static com.example.lasttimemafia.ReavealRole.receiveMessage;
import static com.example.lasttimemafia.joinedGame.sendMessage;
import static com.example.lasttimemafia.joinedGame.socket;

public class RevealDeadAfterMafia extends AppCompatActivity {
String nextThing = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal_dead_after_mafia);
        Log.d("heheblock","We made it into RevealDead");
        TextView ripThem = findViewById(R.id.ripThem);
        sendMessage("getdead");
        String personThatDied = "";
        try {
            personThatDied = receiveMessage(socket);
            ripThem.setText(personThatDied);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        sendMessage("startalarmandchecktime 5");
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
                Log.d("faker","value of nextThing:" + LifecycleTracker.getNumberOfActivity());
                Log.d("faker","Called from ShowDeath");
                nextThing = LifecycleTracker.returnNextActivity();
                //int valueOfCurrent = LifecycleTracker.getNumberOfActivity();
                //Log.d("outbounds","value of currentActivity in Reveal Dead:" + valueOfCurrent);
                //Log.d("days","does this even wrk!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                if (nextThing.equals("villagetalk")) {
                    openVillageTalk();
                }else{
                    Log.d("failure","failure in Textmessages");
                }
            }
        }.start();
       // int positionOfPlayerInArray = MafiaServerGame.players.indexOf(personThatDied);
        //MafiaServerGame.players.remove(personThatDied);
       // Log.d("removeplayers","Value of roles:" + MafiaServerGame.role);
//        MafiaServerGame.role.remove(positionOfPlayerInArray);
    }
    public void openVillageTalk() {
        Intent intent = new Intent(this, VillageVoting.class);
        startActivity(intent);
    }
}
