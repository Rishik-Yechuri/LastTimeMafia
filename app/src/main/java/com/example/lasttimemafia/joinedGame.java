package com.example.lasttimemafia;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;

public class joinedGame extends AppCompatActivity {
    boolean loop = true;
    public static String name = "";
    public static Socket socket;
    Button button2;
    EditText nameText;
    public static PrintWriter out;
    public static BufferedReader br;
    public static String totalNumOfPlayers = "";
    static boolean start = false;
    boolean loop2 = true;
    ProgressBar progressBar;
    String input = "";
    EditText textCode;
    public static Handler handler;
    Thread thread;
    //Message mainThreadMessage;
    Message messageOfficial;
    static String host = "";
    public Handler mainThreadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_game);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String value = msg.obj.toString();
                if (value.equals("die")) {
                    finish();
                }
            }
        };
        //if (savedInstanceState == null) {
        String code = "";
        Log.d("formatting", "Made it to JoinedGame1");
        button2 = (Button) findViewById(R.id.button4);
        Log.d("formatting", "Made it to JoinedGame2");
        progressBar = findViewById(R.id.progressBar4);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button4) {
                    Log.d("progressbarfinal", "Value of progressBar:" + progressBar);
                    textCode = findViewById(R.id.editText3);
                    nameText = findViewById(R.id.nameText);
                    name = nameText.getText().toString();
                        /*try {
                            runMainCode(code);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                    networkProcess task = new networkProcess(joinedGame.this);
                    task.execute();
                    button2.setText("connected");
                }
            }
        });
        new Thread() {
            public void run() {
                start = false;
                Looper.prepare();
                while (loop2) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (start) {
                        loop2 = false;
                    }
                }

                Log.d("formatting", "Made it to JoinedGame8");
                while (loop) {
                    Log.d("clientnums", "TotalPlayers:" + totalNumOfPlayers);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        input = receiveMessage(joinedGame.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message mainThreadMessage;
                    mainThreadMessage = Message.obtain();
                    mainThreadMessage.obj = "updateinput";
                    Log.d("handleimport", "Pre Export");
                    Log.d("handleimport", "Post Export");
                    Log.d("clientGame", "This is the Progress: " + input);
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
                    if (currentNumOfPlayers.equals(totalNumOfPlayers)) {
                        if (loop) {
                            Log.d("formatting", "Made it to JoinedGameAboutToCallMafiaGame");
                            finish();
                            openMafiaGame();
                        }
                        loop = false;
                        Log.d("clientGame2", "Mafia Game Has Been Opened");
                    }
                }
            }
        }.start();
        //}
    }

    public static void finalConnection(String host, int portNumber, joinedGame realActivity) throws IOException, InterruptedException {
        Log.d("stronktrace", "Here 1");
        Socket socket = new Socket(host, portNumber);
        Log.d("stronktrace", "Here 2");
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Log.d("stronktrace", "Here 3");
        out = new PrintWriter(socket.getOutputStream(), true);
        Log.d("stronktrace", "Here 4");
        //setContentView(R.layout.activity_lobby);
        Thread.sleep(2000);
        Log.d("stronktrace", "Here 5");
        String receive = receiveMessage(realActivity);
        Log.d("stronktrace", "Here 6");
        String[] splitMessage = receive.split(" ");
        Log.d("stronktrace", "Here 7");
        totalNumOfPlayers = splitMessage[1];
        Log.d("stacktrace", "presend");
        String kidsNames = "setnameङॠ" + name;
        Log.d("textdebug","Name sent:" + kidsNames);
        sendMessage(kidsNames);
        Log.d("stacktrace", "postsend");
        //sendMessage("trashInfo");
        start = true;
    }

    public static void runMainCode(String code, joinedGame realActivity) throws IOException, InterruptedException {
       // String[] codeSplit = code.split("\\.");
        String totalistic = "";
        long downed = ConvertBasesToCode.convertDown(code,62);
        Log.d("downedvalue","downed:" + downed);
        totalistic = ConvertBasesToCode.convertUp(downed,11);
        totalistic = totalistic.replace("A",".");
        /*if (!(code.charAt(0) == 'A' || code.charAt(0) == 'C' || code.charAt(0) == 'B')) {
            //Final Code
            totalistic = "192.168" + "." + codeSplit[0] + "." + codeSplit[1];

        } else if (code.charAt(0) == 'A') {
            totalistic = "172.";
            for (int looper = 1; looper < code.length(); looper++) {
                totalistic = totalistic + code.charAt(looper);
            }
        } else if (code.charAt(0) == 'C') {
            totalistic = "10.";
            for (int looper = 1; looper < code.length(); looper++) {
                totalistic = totalistic + code.charAt(looper);
            }
        } else {
            totalistic = "";
            for (int looper = 1; looper < code.length(); looper++) {
                totalistic = totalistic + code.charAt(looper);
            }
        }*/
        host = totalistic;
        final int portNumber = 4999;
        boolean run = true;
        int portToUse = 0;
        socket = new Socket(host, 4999);
        Message mainThreadMessage = Message.obtain();
        mainThreadMessage.obj = "updatesocket";
        Log.d("handleimport", "Pre message send in mainCode");
        Log.d("handleimport", "Post message send in mainCode");
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        portToUse = Integer.parseInt(br.readLine());
        socket.close();
        finalConnection(host, portToUse, realActivity);
    }

    public static void sendMessage(String message) {
        Log.d("conflict", "Message Sent:" + message);
        out.println(message);
    }

    /*public String receiveMessage() throws IOException {
        String receivedMessage = br.readLine();
        Log.d("conflict","The recieved message was: " + receivedMessage);
        return receivedMessage;
    }*/
    public static String receiveMessage(joinedGame activity) throws IOException, InterruptedException {
        WeakReference<joinedGame> activityWeakReference = new WeakReference<joinedGame>(activity);
        joinedGame activity2 = activityWeakReference.get();
        boolean loop = true;
        String receivedMessage = "";
        while (loop) {
            Thread.sleep(200);
            receivedMessage = br.readLine();
            if (receivedMessage != null) {
                loop = false;
            }
        }
        if (receivedMessage.startsWith("currentplayers")) {
            Log.d("thingset", "please");
            activity2.progressBar.setProgress(Integer.parseInt(receivedMessage.split(" ")[1]));
        } else if (receivedMessage.startsWith("totalplayers")) {
            if (activity2.progressBar == null) {
                activity2.progressBar = activity2.findViewById(R.id.progressBar4);
            }
            activity2.progressBar = activity2.findViewById(R.id.progressBar4);
            activity2.progressBar.setMax(Integer.parseInt(receivedMessage.split(" ")[1]));
            totalNumOfPlayers = receivedMessage.split(" ")[1];
        }
        Log.d("callercheck", "Called Receive from joinedGame");
        Log.d("conflict", "Received Message: " + receivedMessage);
        return receivedMessage;
    }

    public void openMafiaGame() {
        Intent intent = new Intent(this, MafiaClientGame.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    private static class networkProcess extends AsyncTask<Integer, Integer, String> {
        WeakReference<joinedGame> activityWeakReference;

        networkProcess(joinedGame activity) {
            activityWeakReference = new WeakReference<joinedGame>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            joinedGame activity = activityWeakReference.get();
            activity.setContentView(R.layout.activity_lobby);
            activity.progressBar = activity.findViewById(R.id.progressBar4);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            joinedGame activity = activityWeakReference.get();
            String code = activity.textCode.getText().toString();
            try {
                runMainCode(code, activity);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "finished";
        }
    }
}