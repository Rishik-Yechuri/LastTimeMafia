package com.example.lasttimemafia;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.example.lasttimemafia.joinedGame.br;
import static com.example.lasttimemafia.joinedGame.sendMessage;
import static com.example.lasttimemafia.joinedGame.socket;

public class ReavealRole extends AppCompatActivity {
    String nextThing;
    public static Intent intentMain;
    static Handler handler;
    boolean killed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaveal_role);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String value = msg.obj.toString();
                if (value.equals("mainmenu")) {
                    killed = true;
                    Message killerMessage = Message.obtain();
                    killerMessage.obj = "die";
                    joinedGame.handler.sendMessage(killerMessage);
                    finish();
                }
            }
        };
        intentMain = new Intent(this, MainActivity.class);
        HashMap<String, String> roleToFormatted = new HashMap<>();
        roleToFormatted.put("mafia", "75");
        roleToFormatted.put("villager", "75");
        roleToFormatted.put("guardian angel", "75");

        //End of formatting names of positions

        String roleString = MafiaClientGame.role;
        roleString = toTitleCase(roleString);
        final TextView role = findViewById(R.id.roleText);
        role.setText(roleToFormatted.get(roleString));
        role.setText(roleString);
        if (savedInstanceState == null) {
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
                Log.d("synccheck", "Value of RevealRole countdowntimer:" + countdownTimer);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            ArrayList<String> playerLifecycle;
            if (role.getText().toString().equals("Mafia")) {
                //playerLifecycle = new ArrayList<>(Arrays.asList("closeeyes", "mafiaopeneyes", "mafiatextmessages", "closeeyes", "openeyes", "showdeath","villagetalk","villagedeath"));
                playerLifecycle = new ArrayList<>(Arrays.asList("closeeyes", "mafiaopeneyes", "mafiatextmessages", "closeeyes", "angelopeneyes", "closeeyes", "closeeyes", "openeyes", "showdeath", "villagetalk", "villagedeath"));
            } else if (role.getText().toString().equals("Guardian Angel")) {
                //playerLifecycle = new ArrayList<>(Arrays.asList("closeeyes", "mafiaopeneyes", "closeeyes", "closeeyes", "openeyes", "showdeath","villagetalk","villagedeath"));
                playerLifecycle = new ArrayList<>(Arrays.asList("closeeyes", "mafiaopeneyes", "closeeyes", "closeeyes", "angelopeneyes", "angelprotection", "closeeyes", "openeyes", "showdeath", "villagetalk", "villagedeath"));
            } else if (role.getText().toString().equals("Villager")) {
                //playerLifecycle = new ArrayList<>(Arrays.asList("closeeyes", "mafiaopeneyes", "closeeyes", "closeeyes", "openeyes", "showdeath","villagetalk","villagedeath"));
                playerLifecycle = new ArrayList<>(Arrays.asList("closeeyes", "mafiaopeneyes", "closeeyes", "closeeyes", "angelopeneyes", "closeeyes", "closeeyes", "openeyes", "showdeath", "villagetalk", "villagedeath"));
            } else {
                playerLifecycle = new ArrayList<>(Arrays.asList("failure"));
            }
            LifecycleTracker.setLifecycle(playerLifecycle);
            new CountDownTimer(countdownTimer, 500) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    //nextThing = LifecycleTracker.returnNextActivity();
                    // if(nextThing.equals("closeeyes")){
                    finish();
                    openCloseEyes();
                    // }else{
                    Log.d("failurecatcher", "role:" + role.getText().toString());
                    Log.d("failurecatcher", "failure in reveal role");
                    // }
                }
            }.start();
        }
    }

    public void openCloseEyes() {
        Log.d("reuse", "here 2");
        CloseYourEyes.closeEyesAudio = MediaPlayer.create(this, R.raw.closeeyes);
        Intent intent = new Intent(this, CloseYourEyes.class);
        Log.d("reuse", "here 3");
        finish();
        if (!killed) {
            startActivity(intent);
        }
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
        if (receivedMessage.startsWith("totalplayers")) {
            MafiaClientGame.progressBar.setMax(Integer.parseInt(receivedMessage.split(" ")[1]));
        } else if (receivedMessage.startsWith("currentplayers")) {
            MafiaClientGame.progressBar.setProgress(Integer.parseInt(receivedMessage.split(" ")[1]));
        }
        Log.d("conflict", "Message Received:" + receivedMessage);
        return receivedMessage;
    }

    @Override
    public void onBackPressed() {
        openDialog();
    }

    public void openDialog() {
        ConfirmGoPackDialog goBack = new ConfirmGoPackDialog(getApplicationContext());
        goBack.show(getSupportFragmentManager(), "litty");
    }
}
