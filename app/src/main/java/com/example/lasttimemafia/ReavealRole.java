package com.example.lasttimemafia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;

public class ReavealRole extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaveal_role);

        //Store role name to what they should be formatted to in a Hashmap

        HashMap<String,String> roleToFormatted = new HashMap<>();
        roleToFormatted.put("mafia","75");
        roleToFormatted.put("villager","75");
        roleToFormatted.put("guardian angel","75");

        //End of formatting names of positions

        String roleString = MafiaClientGame.role;
        roleString = toTitleCase(roleString);
        TextView role = findViewById(R.id.roleText);
        role.setText(roleToFormatted.get(roleString));
        role.setText(roleString);
        new CountDownTimer(3*1000, 500) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                openCloseEyes();
            }
        }.start();
    }
    public void openCloseEyes(){
        CloseYourEyes.closeEyesAudio = MediaPlayer.create(this,R.raw.closeeyes);
        Intent intent = new Intent(this,CloseYourEyes.class);
        startActivity(intent);
    }

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
