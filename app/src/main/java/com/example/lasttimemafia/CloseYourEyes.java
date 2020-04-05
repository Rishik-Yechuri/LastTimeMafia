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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_your_eyes);
       /* double time = getTime.returnTime();
        double timeLeft = 16 - time;
        double tempConversion = timeLeft * 1000;
        long countdownTimer = (int)tempConversion;
        Log.d("successtest","Time2:" + getTime.returnTime());*/
        sendMessage("startalarmandchecktime 4.0");
        long countdownTimer = 0;
        try {
            countdownTimer = Long.parseLong(receiveMessage(socket));
            Log.d("synccheck","Value of RevealRole countdowntimer:"+ countdownTimer);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        playAudio();
        new CountDownTimer(countdownTimer, 500) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                openOpenEyes();
            }
        }.start();
    }
    public void playAudio(){
        if(closeEyesAudio==null){
        }
        closeEyesAudio.start();
    }

    public void openOpenEyes(){
        OpenYourEyes.openEyesAudio = MediaPlayer.create(this,R.raw.openeyes);
        Intent intent = new Intent(this,OpenYourEyes.class);
        startActivity(intent);
    }
}
