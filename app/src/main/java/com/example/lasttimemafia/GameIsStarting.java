package com.example.lasttimemafia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

public class GameIsStarting extends AppCompatActivity {
    public int countdownTimer = 4;
    int numOfDots = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_is_starting);
        Log.d("formatting","Made it to GameIsStarting");
        final TextView tripleDot = findViewById(R.id.theGameIsStartingText);
        new CountDownTimer(countdownTimer*1000, 500) {
            public void onTick(long millisUntilFinished) {
                String tempText = "The Game Is Starting";
                for(int x=0;x<numOfDots;x++){
                    tempText+= ".";
                }
                tripleDot.setText(tempText);
                if(numOfDots<4){
                    numOfDots++;
                }else{
                    numOfDots = 0;
                }
            }
            public void onFinish() {
                openCountdownTimer();
            }
        }.start();
    }
    public void openCountdownTimer(){
        Intent intent = new Intent(this,ReavealRole.class);
        startActivity(intent);
    }
}
