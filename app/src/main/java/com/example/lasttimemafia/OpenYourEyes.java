package com.example.lasttimemafia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

public class OpenYourEyes extends AppCompatActivity {
    public static MediaPlayer openEyesAudio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MafiaClientGame.role.equals("mafia")){
        setContentView(R.layout.activity_open_your_eyes);
        }else{
            setContentView(R.layout.activity_close_your_eyes);
        }
        playAudio();
        double time = getTime.returnTime();
        double timeLeft = 18.6 - time;
        double tempConversion = timeLeft * 1000;
        long countdownTimer = (int)tempConversion;
        new CountDownTimer(countdownTimer, 500) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                openTextMessages();
            }
        }.start();
    }
    public void playAudio(){
        if(openEyesAudio==null){
        }
        openEyesAudio.start();
    }
    public void openTextMessages(){
        Log.d("testtabs","openeyes 1");
        Intent intent = new Intent(this,TextMessages.class);
        Log.d("testtabs","openeyes 2");
        startActivity(intent);
    }
}
