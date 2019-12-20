package com.example.lasttimemafia;

import android.content.Intent;
import android.os.StrictMode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MafiaClientGame extends AppCompatActivity implements View.OnClickListener {
    String numOfPeople="";
    int numOfMafia;
    int numOfAngels;
    int numOfVillagers;
    int mafiaTime=30;
    int angelTime;
    int villagerTime;
    public static String role = "";
    boolean sendSomething = false;
    BufferedReader br;
    PrintWriter out;
    ArrayList players = new ArrayList();
    public void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.d("formatting","Made it to MafiaClientGameStart");
        final EditText editText4 = findViewById(R.id.editText4);
        super.onCreate(savedInstanceState);
        Log.d("clientGame2","Set the game view!");
        setContentView(R.layout.activity_mafia_client_game);
        Button button6 = findViewById(R.id.button6);
        button6.setOnClickListener(this);
        br = joinedGame.br;
        out = joinedGame.out;
        //String trash = receiveMessage();
        Log.d("receiveCheck","Pre");
        try {
            role=receiveMessage();
            numOfMafia = Integer.parseInt(receiveMessage());
            numOfAngels = Integer.parseInt(receiveMessage());
            numOfVillagers = Integer.parseInt(receiveMessage());
            mafiaTime = Integer.parseInt(receiveMessage());
            angelTime = Integer.parseInt(receiveMessage());
            villagerTime = Integer.parseInt(receiveMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("receiveCheck","Post");
        Log.d("recInfo","At The Top Line");
        //numOfPeople = receiveMessage();
        numOfPeople = "2";
        Log.d("recInfo","numOfPeople:" + numOfPeople);
        //numOfMafia = Integer.parseInt(receiveMessage());
        //numOfAngels = Integer.parseInt(receiveMessage());
        // numOfVillagers = Integer.parseInt(receiveMessage());
        // mafiaTime = Integer.parseInt(receiveMessage());
        //angelTime = Integer.parseInt(receiveMessage());
        //villagerTime = Integer.parseInt(receiveMessage());
        TextView textMessage = findViewById(R.id.textMessage);
        textMessage.setText(role);
        CountdownTimer.countdownTime = mafiaTime;
        Log.d("formatting","Made it to MafiaClientGameEnd");
        openGameStarting();
       /* new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // if(role.equals("mafia")){
                        try {
                            textConversation(editText4,joinedGame.name);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //}
                    }
                },
                5000
        );*/
    }

    public void sendMessage(String message){
        out.println(message);
        Log.d("clientGsame2","This is the sent message: " + message);
}

    public String receiveMessage() throws IOException, InterruptedException {
        boolean loop = true;
        String receivedMessage = "";
        while(loop) {
            Thread.sleep(200);
            Log.d("messageforrole","Message: " + receivedMessage);
            receivedMessage = br.readLine();
            Log.d("messageforrole","Message2: " + receivedMessage);
            if(receivedMessage!=null){
                loop = false;
            }
        }
        return receivedMessage;
    }
    public String quickReceive() throws IOException, InterruptedException {
        boolean loop = true;
        int repeat = 0;
        String receivedMessage = "";
        while(loop&&repeat<3) {
            Thread.sleep(65);
            Log.d("clientGame2","Message: " + receivedMessage);
            receivedMessage = br.readLine();
            Log.d("clientGame2","Message2: " + receivedMessage);
            if(receivedMessage!=null){
                loop = false;
            }
            repeat++;
        }
        return receivedMessage;
    }
    /*public void textConversation(EditText editText4,String playerName) throws InterruptedException, IOException {
        long startTime = System.currentTimeMillis(); //fetch starting time
        while((System.currentTimeMillis()-startTime)<(mafiaTime+2)){
            Thread.sleep(200);
            String received = receiveMessage();
            String[] textList = received.split(" ");
            String[] sender = new String[textList.length];
            String[] message = new String[textList.length];
            for(int y=0;y<textList.length;y++){
                String[] pieces = textList[y].split(",");
                sender[y] = pieces[1];
                message[y] = pieces[0];
            }
            if(sendSomething) {
                sendMessage(editText4.getText().toString()+ " " + playerName);
                sendSomething = false;
            }
            for(int x=0;x<textList.length;x++){
                Log.d("clientGame2","TextMessage: " + message[x]);
                Log.d("clientGame2","Sender: " + sender[x]);
            }
        }
    }*/

    @Override
    public void onClick(View view) {
        sendSomething = true;
    }
    public void openGameStarting(){
        Intent intent = new Intent(this,GameIsStarting.class);
        startActivity(intent);
    }
}