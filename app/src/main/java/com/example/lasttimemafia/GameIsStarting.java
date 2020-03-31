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
    public int countdownTimer = 4;
    int numOfDots = 0;
    public static int countDownInterval = 0;
    Socket socket = joinedGame.socket;
    public static double time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_is_starting);
        time = getTime.returnTime();
        double timeLeft = 8.5 - time;
        double tempConversion = timeLeft * 1000;
        countdownTimer = (int)tempConversion;
        //setContentView(R.layout.activity_game_is_starting);
        final TextView tripleDot = findViewById(R.id.theGameIsStartingText);
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
