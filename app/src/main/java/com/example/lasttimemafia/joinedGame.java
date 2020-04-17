package com.example.lasttimemafia;

import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lasttimemafia.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class joinedGame extends AppCompatActivity {
    public static String name = "";
    public static Socket socket;
    Button button2;
    EditText nameText;
    public static PrintWriter out;
    public static BufferedReader br;
    public static String totalNumOfPlayers = "";
    boolean start = false;
    boolean loop2 = true;
    ProgressBar progressBar;
    String input = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String code = "";
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_game);
        Log.d("formatting","Made it to JoinedGame1");
        button2 = (Button) findViewById(R.id.button4);
        Log.d("formatting","Made it to JoinedGame2");
        //progressBar = findViewById(R.id.progressBar4);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button4) {
                    EditText textCode = findViewById(R.id.editText3);
                    nameText = findViewById(R.id.nameText);
                    Log.d("formatting","Made it to JoinedGame3");
                    String code = textCode.getText().toString();
                    name = nameText.getText().toString();
                    Log.d("formatting","Made it to JoinedGame4");
                    try {
                        runMainCode(code);
                        Log.d("formatting","Made it to JoinedGame5");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("formatting","Made it to JoinedGame6");
                    button2.setText("connected");
                }
                Log.d("formatting","Made it to JoinedGame7");
            }
        });
        new Thread() {
            public void run() {
                while(loop2){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(start){
                        loop2 = false;
                    }
                }
                boolean loop = true;
                 ProgressBar progressBar =findViewById(R.id.progressBar4);
                Log.d("formatting","Made it to JoinedGame8");
                 while(loop) {
                    progressBar.setMax(/*Integer.valueOf(totalNumOfPlayers)*/2);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                     try {
                         input = receiveMessage();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                     Log.d("clientGame","This is the Progress: " + input);
                    //Log.d("clientGame","We Made It Here!");
                    String currentNumOfPlayers = "";
                    String[] currentNumOfPlayers2 = {};
                    if (input != null) {
                        currentNumOfPlayers2 = input.split(" ");

                        currentNumOfPlayers = "";
                        if (currentNumOfPlayers2[0].equals("currentplayers")) {
                            currentNumOfPlayers = currentNumOfPlayers2[1];
                        }
                        if (currentNumOfPlayers.length() > 0) {
                            progressBar.setProgress(Integer.valueOf(currentNumOfPlayers));
                        }
                    }
                    Log.d("clientGame2","CurrentPlayers: "+ currentNumOfPlayers);
                    Log.d("clientGame2","TotalPlayers: "+ totalNumOfPlayers);
                    if(currentNumOfPlayers.equals(totalNumOfPlayers)){
                        if(loop){
                            Log.d("formatting","Made it to JoinedGameAboutToCallMafiaGame");
                            openMafiaGame();
                        }
                        loop=false;
                        Log.d("clientGame2","Mafia Game Has Been Opened");
                    }
                }
            }
        }.start();
    }
    public void finalConnection(String host, int portNumber) throws IOException, InterruptedException {
        Log.d("clientGame","FinalPort:" + portNumber);
        Log.d("formatting","Made it to JoinedGame11");
        Socket socket = new Socket(host, portNumber);
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        setContentView(R.layout.activity_lobby);
        Thread.sleep(2000);
        Log.d("formatting","Made it to JoinedGame12");
        String receive = receiveMessage();
        String[] splitMessage = receive.split(" ");
        totalNumOfPlayers = splitMessage[1];
        Log.d("formatting","Made it to JoinedGame13");
        sendMessage(name);
        Log.d("formatting","Made it to JoinedGame14");
        //sendMessage("trashInfo");
        start = true;
        Log.d("clientGame","Total Num Of Players:" + totalNumOfPlayers);

           /* new Thread() {
                public void run() {
                    boolean loop = true;
                    //while(loop){

                   // }
                }
            }.start();*/
    }
    public void runMainCode(String code) throws IOException, InterruptedException {
        String[] codeSplit = code.split("\\.");
        String totalistic = "";
        if(!(code.charAt(0)=='A'||code.charAt(0)=='C'||code.charAt(0)=='B')){
            //Final Code
            totalistic = "192.168" + "." + codeSplit[0]+ "." + codeSplit[1];

        }
        else if(code.charAt(0)=='A'){
            totalistic = "172.";
            for(int looper=1;looper<code.length();looper++){
                totalistic = totalistic + code.charAt(looper);
            }
        }
        else if(code.charAt(0)=='C'){
            totalistic = "10.";
            for(int looper=1;looper<code.length();looper++){
                totalistic = totalistic + code.charAt(looper);
            }
        }
        else{
            totalistic = "";
            for(int looper=1;looper<code.length();looper++){
                totalistic = totalistic + code.charAt(looper);
            }
        }
        final String host = totalistic;
        final int portNumber = 4999;
        boolean run = true;
        int portToUse = 0;
        Log.d("checkConnection","The Host:" + host);
        Log.d("checkConnection","Before Socket Connection");
        //socket = new Socket(host, portNumber);
        Log.d("formatting","Made it to JoinedGame9");
        socket = new Socket(host,4999);
        Log.d("checkConnection","After the Socket Connection");
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        portToUse = Integer.parseInt(br.readLine());
        Log.d("clientGame","value of final port:" + portToUse);
        socket.close();
        Log.d("formatting","Made it to JoinedGame10");
        finalConnection(host,portToUse);
    }
    public static void sendMessage(String message){
        Log.d("conflict","Message Sent:" + message);
        out.println(message);
    }

    public String receiveMessage() throws IOException {
        String receivedMessage = br.readLine();
        Log.d("clientGame","The recieved message was: " + receivedMessage);
        return receivedMessage;
    }
    public void openMafiaGame(){
        Intent intent = new Intent(this,MafiaClientGame.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}