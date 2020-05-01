package com.example.lasttimemafia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;

import static com.example.lasttimemafia.joinedGame.br;
import static com.example.lasttimemafia.joinedGame.out;

public class GameIsStarting extends AppCompatActivity {
    public long countdownTimer = 0;
    int numOfDots = 0;
    public static int countDownInterval = 0;
    Socket socket = joinedGame.socket;
    public static double time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_is_starting);
        if (savedInstanceState == null) {
       /* time = getTime.returnTime();
        double timeLeft = 8.5 - time;
        double tempConversion = timeLeft * 1000;
        countdownTimer = (int)tempConversion;*/
            //setContentView(R.layout.activity_game_is_starting);
            final TextView tripleDot = findViewById(R.id.theGameIsStartingText);
            //sendMessage("startalarm 4.0");
            //sendMessage("checkalarmtime");
            sendMessage("startalarmandchecktime 5.0");
            try {
                //Log.d("synccheck","Value of alarmTime:" + trash);
                countdownTimer = Long.valueOf(receiveMessage());
                Log.d("synccheck", "Value of alarmTime:" + countdownTimer);
                if (countdownTimer < 0) {
                    countdownTimer = 0;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            new CountDownTimer(countdownTimer, 500) {
                public void onTick(long millisUntilFinished) {
                    String tempText = "The Game Is Starting";
                    for (int x = 0; x < numOfDots; x++) {
                        tempText += ".";
                    }
                    tripleDot.setText(tempText);
                    if (numOfDots < 4) {
                        numOfDots++;
                    } else {
                        numOfDots = 0;
                    }

                }

                public void onFinish() {
                    openCountdownTimer();
                }
            }.start();
        }
    }

    public void openCountdownTimer() {
        Intent intent = new Intent(this, ReavealRole.class);
        startActivity(intent);
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String receiveMessage() throws IOException, InterruptedException {
        boolean loop = true;
        String receivedMessage = "";
        while (loop) {
            Thread.sleep(200);
            receivedMessage = br.readLine();
            if (receivedMessage != null) {
                loop = false;
            }
        }
        return receivedMessage;
    }
}
