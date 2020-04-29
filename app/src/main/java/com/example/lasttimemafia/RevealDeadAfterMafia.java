package com.example.lasttimemafia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.example.lasttimemafia.ReavealRole.receiveMessage;
import static com.example.lasttimemafia.joinedGame.sendMessage;
import static com.example.lasttimemafia.joinedGame.socket;

public class RevealDeadAfterMafia extends AppCompatActivity {
    String nextThing = "";
    String currentActivity = "";
    boolean canIMoveOn = true;
    boolean isTheGameGoing = true;
    boolean liveTheLowLife = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal_dead_after_mafia);
        TextView theKillerName = findViewById(R.id.theKillerName);
        nextThing = LifecycleTracker.returnNextActivity();
        currentActivity = LifecycleTracker.returnCurrentActivity();
        if (currentActivity.equals("villagedeath")) {
            theKillerName.setText("The Villagers Killed");
        }
        Log.d("heheblock", "We made it into RevealDead");
        TextView ripThem = findViewById(R.id.ripThem);
        if (currentActivity.equals("deadforever")) {
            ripThem.setText("No.Really.You are literally dead.");
            theKillerName.setText("You're Dead");
        } else if(currentActivity.equals("gameover")){
            theKillerName.setText("Game Over");
            sendMessage("whowonthegame");
            String whoWon = "";
            try {
                whoWon = receiveMessage(socket);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            ripThem.setText(whoWon + " won");

        }else {
            sendMessage("getdead");
            String personThatDied = "";
            try {
                personThatDied = receiveMessage(socket);
                if (joinedGame.name.equals(personThatDied)) {
                    canIMoveOn = false;
                }
                ripThem.setText(personThatDied);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            boolean afterVillagerKill = false;
            if (currentActivity.equals("villagedeath")) {
                afterVillagerKill = true;
            }
            sendMessage("isgamestillgoing " + afterVillagerKill);
            try {
                isTheGameGoing = Boolean.parseBoolean(receiveMessage(socket));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendMessage("startalarmandchecktime 5");
            long countdownTimer = 0;
            try {
                countdownTimer = Long.parseLong(receiveMessage(socket));
                Log.d("finalloop", "Countdown Timer:" + countdownTimer);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("finalloop", "Did we make it here");
            new CountDownTimer(countdownTimer, 500) {
                public void onTick(long millisUntilFinished) {
                    Log.d("finalloop", "Did we make it here 2");
                }

                public void onFinish() {
                    Log.d("finalloop", "Did we make it here 3");
                    //Log.d("faker", "value of nextThing:" + LifecycleTracker.getNumberOfActivity());
                    //Log.d("faker", "Called from ShowDeath");
                    //nextThing = LifecycleTracker.returnNextActivity();
                    //int valueOfCurrent = LifecycleTracker.getNumberOfActivity();
                    //Log.d("outbounds","value of currentActivity in Reveal Dead:" + valueOfCurrent);
                    //Log.d("days","does this even wrk!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    Log.d("finalloop", "NextThing:" + nextThing);
                    Log.d("finalloop", "Did we make it here 4");
                    if (!isTheGameGoing) {
                        openGameOver();
                    } else if (!canIMoveOn) {
                        openYourDead();
                    } else if (nextThing.equals("villagetalk")) {
                        openVillageTalk();
                    } else if (nextThing.equals("closeeyes")) {
                        openCloseEyes();
                    } else if(nextThing.equals("gameover")){
                        openGameOver();
                    }else {
                        Log.d("failure", "failure in Textmessages");
                    }
                }
            }.start();
            // int positionOfPlayerInArray = MafiaServerGame.players.indexOf(personThatDied);
            //MafiaServerGame.players.remove(personThatDied);
            // Log.d("removeplayers","Value of roles:" + MafiaServerGame.role);
//        MafiaServerGame.role.remove(positionOfPlayerInArray);
        }
    }

    public void openVillageTalk() {
        Intent intent = new Intent(this, VillageVoting.class);
        startActivity(intent);
    }

    public void openCloseEyes() {
        Intent intent = new Intent(this, CloseYourEyes.class);
        startActivity(intent);
    }

    public void openYourDead() {
        LifecycleTracker.setLifecycle(new ArrayList<>(Arrays.asList("deadforever")));
        Intent intent = new Intent(this, RevealDeadAfterMafia.class);
        startActivity(intent);
    }

    public void openGameOver() {
        LifecycleTracker.setLifecycle(new ArrayList<>(Arrays.asList("gameover")));
        Intent intent = new Intent(this, RevealDeadAfterMafia.class);
        startActivity(intent);
    }
}
