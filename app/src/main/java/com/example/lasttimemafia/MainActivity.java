package com.example.lasttimemafia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.lasttimemafia.SettingsMenu.GAME_PREFERENCES;
import static com.example.lasttimemafia.SettingsMenu.preferences;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //if (savedInstanceState == null) {
            preferences = getSharedPreferences(GAME_PREFERENCES, MODE_PRIVATE);
            Log.d("reloaderror","Pre buttons creating");
            Button button2 = (Button) findViewById(R.id.button2);
            Log.d("reloaderror","Button Created");
            button2.setOnClickListener(this);
            Log.d("reloaderror","listener set");
            Button button = (Button) findViewById(R.id.button);
            button.setOnClickListener(this);
            Button button3 = (Button) findViewById(R.id.button3);
            button3.setOnClickListener(this);
            Button button5 = (Button) findViewById(R.id.button5);
            button5.setOnClickListener(this);
       // }
    }


    //Button codes
    @Override
    public void onClick(View v) {
        Log.d("reloaderror","onclick works");
        if (v.getId() == R.id.button2) {
            Log.d("reloaderror","settings button clicked");
            openSetting();
        } else if (v.getId() == R.id.button) {
            openhostGame();
        } else if (v.getId() == R.id.button3) {
            //openCreateAccount();
        } else if (v.getId() == R.id.button5) {
            Log.d("reloaderror","join game button clicked");
            openJoinedGame();
        }
    }


    /*public void openchangeThings(){
      Intent intent = new Intent(this,Settings.class);
      startActivity(intent);
    }*/
    public void openhostGame() {
        Intent intent = new Intent(this, hostGame.class);
        startActivity(intent);
    }

 /* public void openCreateAccount(){
    Intent intent = new Intent(this,CreateAccount.class);
    startActivity(intent);
  }*/

    public void openJoinedGame() {
        Intent intent = new Intent(this, joinedGame.class);
        startActivity(intent);
    }

    public void openSetting() {
        Intent intent = new Intent(this, SettingsMenu.class);
        startActivity(intent);
    }
}