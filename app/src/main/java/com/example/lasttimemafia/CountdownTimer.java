package com.example.lasttimemafia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class CountdownTimer extends AppCompatActivity {
    public static int countdownTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_timer);
        if (savedInstanceState == null) {
            final TextView timeCounter = findViewById(R.id.timeLeftLabel);
            new CountDownTimer(countdownTime * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    timeCounter.setText(String.valueOf((int) millisUntilFinished / 1000 + 1));
                }

                public void onFinish() {
                    timeCounter.setText(String.valueOf(0));
                }
            }.start();
        }
    }
}
