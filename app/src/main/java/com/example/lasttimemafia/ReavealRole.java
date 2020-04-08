package com.example.lasttimemafia;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import static com.example.lasttimemafia.joinedGame.br;
import static com.example.lasttimemafia.joinedGame.sendMessage;
import static com.example.lasttimemafia.joinedGame.socket;

public class ReavealRole extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaveal_role);
        View tempView = findViewById(R.id.revealRoleRoot);
        /*double time = getTime.returnTime();
        Log.d("successtest","Time:" + time);
        double timeLeft = 12.4 - time;
        double tempConversion = timeLeft * 1000;
        long countdownTimer = (int)tempConversion;*/
        //Store role name to what they should be formatted to in a Hashmap
        sendMessage("startalarmandchecktime 4.0");
        long countdownTimer = 0;
        try {
            countdownTimer = Long.parseLong(receiveMessage(socket));
            Log.d("synccheck","Value of RevealRole countdowntimer:"+ countdownTimer);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        HashMap<String, String> roleToFormatted = new HashMap<>();
        roleToFormatted.put("mafia", "75");
        roleToFormatted.put("villager", "75");
        roleToFormatted.put("guardian angel", "75");

        //End of formatting names of positions

        String roleString = MafiaClientGame.role;
        roleString = toTitleCase(roleString);
        TextView role = findViewById(R.id.roleText);
        role.setText(roleToFormatted.get(roleString));
        role.setText(roleString);
        new CountDownTimer(countdownTimer, 500) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                openCloseEyes();
            }
        }.start();
    }

    public void openCloseEyes() {
        CloseYourEyes.closeEyesAudio = MediaPlayer.create(this, R.raw.closeeyes);
        Intent intent = new Intent(this, CloseYourEyes.class);
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

    public static String receiveMessage(Socket socket) throws IOException, InterruptedException {
       // br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        boolean loopRecceiveMessage = true;
        String receivedMessage = "";
        while (loopRecceiveMessage) {
            Thread.sleep(200);
            receivedMessage = br.readLine();
            if (receivedMessage != null) {
                loopRecceiveMessage = false;
            }
        }
        return receivedMessage;
    }
}
