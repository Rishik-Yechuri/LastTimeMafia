package com.example.lasttimemafia;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.lasttimemafia.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;

import static com.example.lasttimemafia.ReavealRole.receiveMessage;
import static com.example.lasttimemafia.joinedGame.sendMessage;
import static com.example.lasttimemafia.joinedGame.socket;

public class TextMessages extends AppCompatActivity {
    String nextThing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main2);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setText("Voting");
        tabs.getTabAt(1).setText("Messages");
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
                //Frag2.thread.interrupt();
                Frag2.keepThreadRunning = false;
                Log.d("faker","value of nextThing:" + LifecycleTracker.getNumberOfActivity());
                Log.d("faker","Called from TextMessages");
                nextThing = LifecycleTracker.returnNextActivity();
                if (nextThing.equals("closeeyes")) {
                    closeEyes();
                }else{
                    Log.d("failure","failure in Textmessages");
                }
            }
        }.start();
    }

    public void closeEyes() {
        Log.d("testtabs", "openeyes 1");
        Intent intent = new Intent(this, CloseYourEyes.class);
        Log.d("testtabs", "openeyes 2");
        startActivity(intent);
    }
}