package com.example.lasttimemafia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;

public class CloseYourEyes extends AppCompatActivity {
public static MediaPlayer closeEyesAudio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_your_eyes);
        playAudio();
        new CountDownTimer(4*1000, 500) {
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
