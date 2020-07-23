package com.example.lasttimemafia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class joinedGame extends AppCompatActivity {
    private static Object OnFailureListener;
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
    static Context context;
    private BroadcastReceiver _getMessages = new GetNewMessages();
    public static Handler handler;
    //Message mainThreadMessage;
    Message messageOfficial;
    static String host = "";
    public static SaveWANMessages saveWANMessages;
    public Handler mainThreadHandler;
    public static String gameCode;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_game);
        saveWANMessages = new SaveWANMessages();
        token = MyFirebaseMessagingService.getToken(getApplicationContext());
        IntentFilter filter = new IntentFilter("NEWMESSAGE");
        context = getApplicationContext();
        this.registerReceiver(_getMessages, filter);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String value = msg.obj.toString();
                if (value.equals("die")) {
                    finish();
                }
            }
        };
        Log.d("comethru", "Context set:" + context);
        //if (savedInstanceState == null) {
        String code = "";
        Log.d("formatting", "Made it to JoinedGame1");
        button2 = (Button) findViewById(R.id.button4);
        Log.d("formatting", "Made it to JoinedGame2");
        progressBar = findViewById(R.id.progressBar4);
        //createGame("test");
        //addToken("king","lol","hi");
        bandageFunction();
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
                    button2.setText("connecting");
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
        if (context.getSharedPreferences("_", MODE_PRIVATE).getString("gametype", "lan").equals("lan")) {
            Socket socket = new Socket(host, portNumber);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            Log.d("gamernation","Final connection for lan");
        }
        Log.d("gamernation","Final connection after lan");
        //setContentView(R.layout.activity_lobby);
        Thread.sleep(2000);
        Log.d("gamernation","prereceive");
        String receive = receiveMessage(realActivity);
        Log.d("gamernation","post receive");
        String[] splitMessage = receive.split(" ");
        totalNumOfPlayers = splitMessage[1];
        String kidsNames = "setnameङॠ" + name;
        Log.d("gamernation","presend");
        sendMessage(kidsNames);
        Log.d("gamernation","postsend");
        //sendMessage("trashInfo");
        start = true;

    }

    public static void runMainCode(String code, joinedGame realActivity) throws IOException, InterruptedException {
        // String[] codeSplit = code.split("\\.");
        if (code.length() > 4) {
            String totalistic = "";
            context.getSharedPreferences("_", MODE_PRIVATE).edit().putString("gametype", "lan").apply();
            long downed = ConvertBasesToCode.convertDown(code, 62);
            Log.d("downedvalue", "downed:" + downed);
            totalistic = ConvertBasesToCode.convertUp(downed, 11);
            totalistic = totalistic.replace("A", ".");
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
        } else if (code.length() == 4) {
            context.getSharedPreferences("_", MODE_PRIVATE).edit().putString("gametype", "wan").apply();
            Log.d("gamernation","value of pref:" + context.getSharedPreferences("_",MODE_PRIVATE).getString("gametype","lan"));
            Log.d("comethru", "Value of token:" + MyFirebaseMessagingService.getToken(/*getApplicationContext())*/context));
            addToken(MyFirebaseMessagingService.getToken(context/*getBaseContext()*/), code, name);
            sendMessageFirebase("none","registername");
            //String whyme = String.valueOf(createGame("poop"));
            finalConnection(host, 0, realActivity);
        }
    }

    public static void sendMessage(String message) {
        Log.d("howmanytimes","sendMessage");
        if (context.getSharedPreferences("_", MODE_PRIVATE).getString("gametype", "lan").equals("wan")) {
            sendMessageFirebase(message,"messagetohost");
        } else if (context.getSharedPreferences("_", MODE_PRIVATE).getString("gametype", "lan").equals("lan")) {
            out.println(message);
        }
    }

    /*public String receiveMessage() throws IOException {
        String receivedMessage = br.readLine();
        Log.d("conflict","The recieved message was: " + receivedMessage);
        return receivedMessage;
    }*/
    public static String receiveMessage(joinedGame activity) throws IOException, InterruptedException {
        WeakReference<joinedGame> activityWeakReference = new WeakReference<joinedGame>(activity);
        joinedGame activity2 = activityWeakReference.get();
        String receivedMessage = "";
        Log.d("gamernation", "received message on client");
        if (context.getSharedPreferences("_", MODE_PRIVATE).getString("gametype", "lan").equals("wan")) {
            Log.d("gamernation", "wan receive message");
            receivedMessage = saveWANMessages.returnMessage();
            Log.d("gamernation", "wan receive message after");
        } else if (context.getSharedPreferences("_", MODE_PRIVATE).getString("gametype", "lan").equals("lan")) {
            boolean loop = true;
            while (loop) {
                Thread.sleep(200);
                receivedMessage = br.readLine();
                if (receivedMessage != null) {
                    loop = false;
                }
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
                Log.d("gamernation", "inside async task");
                runMainCode(code, activity);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            return "finished";
        }
    }
    public static void addToken(String token, String gameID, String name) {
        // Create the arguments to the callable function.
        gameCode = gameID;
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("gameID", gameID);
        data.put("name", name);
        Log.d("comethru","addToken called");
        FirebaseFunctions.getInstance()
                .getHttpsCallable("addToken")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        HashMap result = (HashMap) task.getResult().getData();
                        JSONObject res = new JSONObject(result);
                        //String message = res.getString("lastName");
                        return null;
                    }
                });
        Log.d("comethru","addToken Finished");
    }
    public Task<String> createGame(String token) {
        Map<String, Object> data = new HashMap<>();
        //data.put("message", message);
        //data.put("token", MyFirebaseMessagingService.getToken(getApplicationContext()));
        data.put("token", token);
        return  FirebaseFunctions.getInstance()
                .getHttpsCallable("createGame")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        HashMap result = (HashMap) task.getResult().getData();
                        JSONObject res = new JSONObject(result);
                        return null;
                    }
                });
    }

    public class GetNewMessages extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getExtras().getString("message");
            Log.d("toucher","We have injected message");
            saveWANMessages.injectMessage(message);
        }
    }

    public static void sendMessageFirebase(String message,String purpose) {
        Log.d("howmanytimes","sendMessageFirebase");
        Log.d("conspiracytheories","Message Sent:" + message);
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        data.put("purpose", purpose);
        data.put("clientname",name);
        data.put("gameCode", gameCode);
        data.put("token", "host");
        FirebaseFunctions.getInstance()
                .getHttpsCallable("sendMessage")
                .call(data)
                .addOnFailureListener((com.google.android.gms.tasks.OnFailureListener) OnFailureListener)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        HashMap result = (HashMap) task.getResult().getData();
                        JSONObject res = new JSONObject(result);
                        //String message = res.getString("gameID");
                        //Log.d("serverresult","gameID:" + message);
                        //openShowCode(message);
                        return null;
                    }
                });
    }

    public static SharedPreferences getSharedPreferences(Context ctxt) {
        return ctxt.getSharedPreferences("FILE", 0);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this._getMessages);
    }
    public static void bandageFunction() {
        FirebaseFunctions.getInstance()
                .getHttpsCallable("bandageFunction")
                .call()
                .addOnFailureListener((com.google.android.gms.tasks.OnFailureListener) OnFailureListener)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        HashMap result = (HashMap) task.getResult().getData();
                        JSONObject res = new JSONObject(result);
                        //String message = res.getString("gameID");
                        //Log.d("serverresult","gameID:" + message);
                        //openShowCode(message);
                        return null;
                    }
                });
    }
}