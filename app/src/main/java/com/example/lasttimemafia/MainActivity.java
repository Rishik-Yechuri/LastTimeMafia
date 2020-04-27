package com.example.lasttimemafia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.lasttimemafia.SettingsMenu.GAME_PREFERENCES;
import static com.example.lasttimemafia.SettingsMenu.preferences;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //Wifi Stuff
        //A method for the wifi that discovers peers
        //Regular Loading stuff
        preferences = getSharedPreferences(GAME_PREFERENCES, MODE_PRIVATE);
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(this);
        Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(this);
    }





    //Button codes
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button2){
            openSetting();
        }
        else if(v.getId()==R.id.button){
            openhostGame();
        }

        else if(v.getId()==R.id.button3){
            //openCreateAccount();
        }

        else if (v.getId()==R.id.button5){
            openJoinedGame();
        }
    }


    /*public void openchangeThings(){
      Intent intent = new Intent(this,Settings.class);
      startActivity(intent);
    }*/
    public void openhostGame(){
        Intent intent = new Intent(this,hostGame.class);
        startActivity(intent);
    }

 /* public void openCreateAccount(){
    Intent intent = new Intent(this,CreateAccount.class);
    startActivity(intent);
  }*/

    public void openJoinedGame(){
        Intent intent = new Intent(this,joinedGame.class);
        startActivity(intent);
    }

    public void openSetting(){
        Intent intent = new Intent(this,SettingsMenu.class);
        startActivity(intent);
    }
}