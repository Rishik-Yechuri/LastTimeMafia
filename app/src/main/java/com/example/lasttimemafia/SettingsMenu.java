package com.example.lasttimemafia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static com.example.lasttimemafia.joinedGame.sendMessage;

public class SettingsMenu extends AppCompatActivity {
    public static final String GAME_PREFERENCES = "GamePrefs";
    TextView villagerNumbers;
    TextView mafiaNumbers;
    TextView angelNumbers;
   public static  SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);
        preferences = getSharedPreferences(GAME_PREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();
        villagerNumbers = findViewById(R.id.villagerNumbers);
        mafiaNumbers = findViewById(R.id.mafiaNumbers);
        angelNumbers = findViewById(R.id.angelNumbers);
        SeekBar seekBarVillager = findViewById(R.id.seekBarVillager);
        SeekBar seekBarMafia = findViewById(R.id.seekBarMafia);
        SeekBar seekBarAngel = findViewById(R.id.seekBarAngel);
        seekBarVillager.setOnSeekBarChangeListener(new seekbarListener());
        seekBarMafia.setOnSeekBarChangeListener(new seekbarListener());
        seekBarAngel.setOnSeekBarChangeListener(new seekbarListener());
       /* try {

            FileInputStream fin = openFileInput("settings.txt");
            int c;
            String temp="";

            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }
            fin.close();
            Log.d("esketit","Stored Message:" + temp);
        }
        catch(Exception ignored){
        }*/
    }

    private class seekbarListener implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            TextView thingToChange = findViewById(R.id.villagerNumbers);
            if (seekBar.getId() == R.id.seekBarVillager) { thingToChange = findViewById(R.id.villagerNumbers); }
            if (seekBar.getId() == R.id.seekBarMafia) { thingToChange = findViewById(R.id.mafiaNumbers); }
            if (seekBar.getId() == R.id.seekBarAngel) { thingToChange = findViewById(R.id.angelNumbers); }
            Log.d("getitbois","Value of progress:" + progress);
            thingToChange.setText(String.valueOf(progress));
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
       /* String data="";
        data += villagerNumbers.getText() + " " + mafiaNumbers.getText() + " " + angelNumbers.getText();
        try {
            Log.d("h3h3","PLeezx");
            FileOutputStream fOut = openFileOutput("settings.txt",MODE_PRIVATE);
            fOut.flush();
            fOut.write(data.getBytes());
            fOut.close();
        }
        catch (Exception e) { e.printStackTrace();
        }*/
       editor.clear();
       editor.putString("villager", String.valueOf(villagerNumbers.getText()));
        editor.putString("mafia", String.valueOf(mafiaNumbers.getText()));
        editor.putString("angel",String.valueOf(angelNumbers.getText()));
        editor.apply();
        super.onBackPressed();
    }
    public static String getDefaults(String key, String defaultValue) {
        String gotit = preferences.getString(key, defaultValue);
        return gotit;
    }
}
