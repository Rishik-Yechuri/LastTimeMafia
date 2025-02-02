package com.example.lasttimemafia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.lasttimemafia.SettingsMenu.GAME_PREFERENCES;
import static com.example.lasttimemafia.SettingsMenu.preferences;

public class MafiaNetworkCode extends AppCompatActivity {
    public static boolean WANmode = false;
    public static int currentPort = 5000;
    ProgressBar progressBar;
    Context context;
    public static int totalNumOfPlayers = 2;
    SaveWANMessages saveWANMessages;
    int currentNumOfPlayers = 0;
    Socket socket;
    private BroadcastReceiver _updateName = new RegisterNameReceiver();
    private BroadcastReceiver _getUpdates = new GetPlayerUpdates();
    boolean personKilled = false;
    public static long startTimeOfTimer = System.currentTimeMillis();
    public static long totalTimePassed = 0;
    public static long alarmLength = 0;
    public static long alarmEndTime = 0;
    public static long lastAlarmEndTime = 0;
    ArrayList<String> storeSends = new ArrayList<>();
    int locInStoreSends = 0;
    boolean sendMessage = false;
    String playerName;
    int portNumber;
    //Code for storing text messages and info
    HashMap<String, String> holdVotingInfo; //= new HashMap();
    // ArrayList textMessageHistory;
    //ArrayList textMessageHistorySender;
    int numberOfTextMessages = 0;

    //End of Code for storing text messages and info
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            preferences = getSharedPreferences(GAME_PREFERENCES, MODE_PRIVATE);
            SettingsMenu.editor = preferences.edit();
            int tempMafia = Integer.parseInt(SettingsMenu.getDefaults("mafia", "0"));
            int tempVillager = Integer.parseInt(SettingsMenu.getDefaults("villager", "0"));
            int tempAngel = Integer.parseInt(SettingsMenu.getDefaults("angel", "0"));
            MafiaServerGame.numOfAngels = tempAngel;
            MafiaServerGame.numOfMafia = tempMafia;
            MafiaServerGame.numOfVillagers = tempVillager;
            hostGame.totalNumOfPlayers = tempAngel + tempMafia + tempVillager;
            totalNumOfPlayers = hostGame.totalNumOfPlayers;
            ////////////////////////////////////////////////////////////
            progressBar = findViewById(R.id.progressBar2);
            progressBar.setMax(Integer.valueOf(totalNumOfPlayers));
            progressBar.setProgress(2);
            holdVotingInfo = new HashMap<>();
            joinedGame.bandageFunction();
            //textMessageHistory = new ArrayList();
            //textMessageHistorySender = new ArrayList();
            Log.d("hostingGame", "It Works!");
        }
    }

    BufferedReader br;
    public static PrintWriter out;

    public static void main(String args[]) throws IOException {
    }

    public String convertIP() throws UnknownHostException {
        String totalistic = "";
        if (SettingsMenu.getDefaults("wan", "false").equals("false")) {
            String ipAddress = getIPAddress(true);
            ipAddress = ipAddress.replace(".", "A");
            long downed = ConvertBasesToCode.convertDown(ipAddress, 11);
            totalistic = ConvertBasesToCode.convertUp(downed, 62);
        }/*else if(SettingsMenu.getDefaults("wan", "false").equals("true")){
            //totalistic = String.valueOf(token);
            String kring = String.valueOf(createGame(token));
        }*/
        Log.d("consolepeasants", "totalistic:" + totalistic);
        return totalistic;
        //return totalistic;
    }

    public void connectionCreater(int portNumber2, int numPeople) throws IOException {
        int portNumber = portNumber2;
        boolean run = true;
        ServerSocket serverSocket = new ServerSocket(/*portNumber*/4999);
        Socket socket;
        while (run) {
            socket = serverSocket.accept();
            int numOfPlayers = currentNumOfPlayers;
            Log.d("hostGame", "numOfPlayers++");
            numOfPlayers++;
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            int finalPlaceToConnect = portNumber + 1;
            pw.print(finalPlaceToConnect);
            pw.close();
            socket.close();
            portNumber++;
            if (!(portNumber + 1 < 5000 + numPeople)) {
                run = false;
            }
        }
    }

    public void permaConnection(int portNumber2, Context context) throws IOException, InterruptedException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        new Thread() {
            public void run() {
                hostGame.bandageHost();
                sendMessageFirebase("hurry");
                int counter = 0;
                while (counter < 1299999999) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (sendMessage) {
                        Log.d("conspiracytheories","send message true");
                        sendMessageFirebase("hurry");
                        sendMessageFirebase(storeSends.get(locInStoreSends));
                        locInStoreSends++;
                        sendMessage = false;
                    }
                    counter++;
                }
            }
        }.start();
        int tempMafia = Integer.parseInt(SettingsMenu.getDefaults("mafia", "0"));
        int tempVillager = Integer.parseInt(SettingsMenu.getDefaults("villager", "0"));
        int tempAngel = Integer.parseInt(SettingsMenu.getDefaults("angel", "0"));
        MafiaServerGame.numOfAngels = tempAngel;
        MafiaServerGame.numOfMafia = tempMafia;
        MafiaServerGame.numOfVillagers = tempVillager;
        MafiaServerGame.addRolesToList();
        portNumber = portNumber2;
        Log.d("bruh", "Connection creater set port number:" + portNumber);
        boolean run = true;
        if (SettingsMenu.getDefaults("wan", "false").equals("true")) {
            IntentFilter filter = new IntentFilter("REGISTERNAME");
            _updateName = new RegisterNameReceiver();
            saveWANMessages = new SaveWANMessages();
            this.context = context;
            context.registerReceiver(_updateName, filter);
        } else if (SettingsMenu.getDefaults("wan", "false").equals("false")) {
            Log.d("throwitup", "Do The Thing2");
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Log.d("throwitup", "Do The Thing2 2");
            socket = serverSocket.accept();
            Log.d("throwitup", "Do The Thing2 3");
            OutputStream os = socket.getOutputStream();
            Log.d("throwitup", "Do The Thing2 4");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Log.d("throwitup", "Do The Thing2 5");
            out = new PrintWriter(os, true);
            Log.d("throwitup", "Do The Thing2 6");
            doThingAfterNameConfirmed();
            Log.d("throwitup", "Do The Thing2 7");
            boolean keepLoopRunning = true;
            while (keepLoopRunning) {
                Log.d("throwitup", "Do The Thing2 8");
                receiveMessage(socket);
                Log.d("throwitup", "Do The Thing2 9");
            }
        }
        /*ServerSocket serverSocket = new ServerSocket(portNumber);
        socket = serverSocket.accept();*/
        /*hostGame.currentNumOfPlayers++;
        currentNumOfPlayers++;*/
        /*OutputStream os = socket.getOutputStream();
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(os, true);*/
        //ProgressBar progressBar = findViewById(R.id.progressBar2);
        //progressBar.setMax(Integer.valueOf(totalNumOfPlayers));
        Log.d("helpme", "totalnum:" + totalNumOfPlayers);
        /*sendMessage("totalplayers " + totalNumOfPlayers);
        boolean loop = true;
        while (loop) {
            Thread.sleep(200);
            Log.d("hostGame2", "sentAgain");
            Log.d("hostGame2", String.valueOf(loop));
            sendMessage("currentplayers " + String.valueOf(hostGame.currentNumOfPlayers));
            Log.d("hostGame2", "Current number of players: " + currentNumOfPlayers);
            Log.d("hostGame2", "Total number of players: " + totalNumOfPlayers);
            if (currentNumOfPlayers == totalNumOfPlayers) {
                Log.d("hostGame2", "Loop Is Set To False");
                // startTimeOfTimer = System.currentTimeMillis();
                Log.d("synccheck", "This is when the server timer is started");
                loop = false;
            }
        }*/
        /*String setPlayerName = receiveMessage(socket);
        MafiaServerGame.players.add(playerName);
        while (MafiaServerGame.sendRole == false) {
            Thread.sleep(250);
        }*/
        //Log.d("stealyokid", "playerName:" + playerName);
        /*int placeOfRoleInList = MafiaServerGame.players.indexOf(playerName);*/
        // Log.d("random", "PlaceOfRoleInList: " + placeOfRoleInList);
        //Log.d("removeplayers", "Value of Roles before death:" + MafiaServerGame.role);
       /* int tempMafia = Integer.parseInt(SettingsMenu.getDefaults("mafia", "0"));
        int tempVillager = Integer.parseInt(SettingsMenu.getDefaults("villager", "0"));
        int tempAngel = Integer.parseInt(SettingsMenu.getDefaults("angel", "0"));
        MafiaServerGame.numOfAngels = tempAngel;
        MafiaServerGame.numOfMafia = tempMafia;
        MafiaServerGame.numOfVillagers = tempVillager;*/
        //MafiaServerGame.addRolesToList();
        //Log.d("goingcrazy", "roles accessed");
        //String role = (String) MafiaServerGame.role.get(placeOfRoleInList);
        //sendMessage("Trash");
       /* Log.d("rolecheck", "RoleSentPre");
        sendMessage(role);//Role
        Log.d("rolecheck", "RoleSentPost");
        sendMessage(String.valueOf(MafiaServerGame.players.size()));//Number of players
        sendMessage("1");//Number of Mafia
        sendMessage("1");//Number of Guardian Angel
        sendMessage("0");//Number fo villager
        sendMessage("30");//Seconds for mafia to talk
        sendMessage("10");//Time for guardian angel
        sendMessage("30");//time for village to talk*/
        /*boolean keepLoopRunning = true;
        while (keepLoopRunning) {
            receiveMessage(socket);
        }*/
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    public String howManyTotalPeople() {
        return String.valueOf(totalNumOfPlayers);
    }

    public String howManyCurrentPeople() {
        return String.valueOf(currentNumOfPlayers);
    }

    public void sendMessage(String message) {
        Log.d("howmanytimes", "sendMesssage");
        if (SettingsMenu.getDefaults("wan", "false").equals("true")) {
            storeSends.add(message);
            sendMessage = true;
            //sendMessageFirebase(message);
        } else if (SettingsMenu.getDefaults("wan", "false").equals("false")) {
            out.println(message);
        }
    }

    public String receiveMessage(Socket socket) throws IOException, InterruptedException {
        String receivedMessage = "nothing";
        if (SettingsMenu.getDefaults("wan", "false").equals("true")) {
            receivedMessage = saveWANMessages.returnMessage();
        } else if (SettingsMenu.getDefaults("wan", "false").equals("false")) {
            if (br == null) {
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }
            boolean loopRecceiveMessage = true;
            Log.d("insidereceiver", "3");
            while (loopRecceiveMessage) {
                Thread.sleep(200);
                receivedMessage = br.readLine();
                if (receivedMessage != null) {
                    loopRecceiveMessage = false;
                }
                Log.d("insidereceiver", "9");
            }
        }
        //Log.d("conspiracytheories","Message Received:" + receivedMessage);
        if (receivedMessage.startsWith("time")) {
            sendMessage(String.valueOf(returnTime()));
            receivedMessage = "nothing";
        } else if (receivedMessage.startsWith("startalarmandchecktime")) {
            double input = Double.parseDouble(receivedMessage.split(" ")[1]);
            startAlarm(input);
            long time = getAlarmTimeRemaining();
            Log.d("synccheck", "Server side time value: " + time);
            if (time < 0) {
                time = 0;
            }
            sendMessage(String.valueOf(time));
            receivedMessage = "nothing";
        } else if (receivedMessage.startsWith("startalarm")) {
            double input = Double.parseDouble(receivedMessage.split(" ")[1]);
            startAlarm(input);
            receivedMessage = "nothing";
        } else if (receivedMessage.startsWith("checkalarmtime")) {
            long time = getAlarmTimeRemaining();
            sendMessage(String.valueOf(time));
            receivedMessage = "nothing";
        } else if (receivedMessage.startsWith("checkalarm")) {
            boolean alarmOnOrOff = checkAlarm();
            sendMessage(String.valueOf(alarmOnOrOff));
            receivedMessage = "nothing";
        } else if (receivedMessage.startsWith("getplayers")) {
            String listOfPlayers = returnPlayers();
            sendMessage(listOfPlayers);
        } else if (receivedMessage.startsWith("updatemessages")) {
            String listOfMessages = updateMessages();
            sendMessage(listOfMessages);
            receivedMessage = "nothing";
        } else if (receivedMessage.startsWith("setvote")) {
            setVote(String.valueOf(receivedMessage.split(" ")[1]));
            receivedMessage = "nothing";
        } else if (receivedMessage.startsWith("setmessage")) {
            String combinedMessage = receivedMessage.substring(11);
            setMessage(combinedMessage);
            receivedMessage = "nothing";
        } else if (receivedMessage.startsWith("getdead")) {
            Log.d("predeadthing", "votinginfo:" + MafiaServerGame.holdVotingInfo);
            String deadPerson = getDead(MafiaServerGame.holdVotingInfo);
            Log.d("players", "deadperson:" + deadPerson);
            sendMessage(deadPerson);
            if (!personKilled) {
                Log.d("textdebug", "In removing player");
                removePlayer(deadPerson);
                personKilled = true;
            }
            receivedMessage = "nothing";
        } else if (receivedMessage.startsWith("removeplayer")) {
            removePlayer(receivedMessage.split(" ")[1]);
        } else if (receivedMessage.startsWith("restartvoting")) {
            restartVoting(Integer.parseInt(receivedMessage.split(" ")[1]));
        } else if (receivedMessage.startsWith("confirmclick")) {
            Log.d("finallap", "Pre changeTimer call");
            changeTimerWhenConfirmed(Boolean.parseBoolean(receivedMessage.split(" ")[1]));
        } else if (receivedMessage.startsWith("checkconfirmstatus")) {
            Log.d("confirmcheck", "Pre isItConfirmed");
            String booleanToreturn = String.valueOf(isItConfirmed());
            Log.d("confirmcheck", "Value of Confirm Check:" + booleanToreturn);
            sendMessage(booleanToreturn);
        } else if (receivedMessage.startsWith("resetnumofconfirms")) {
            resetConfirms();
        } else if (receivedMessage.startsWith("amistillinthegame")) {
            sendMessage(String.valueOf(amIStillIn(playerName)));
        } else if (receivedMessage.startsWith("isgamestillgoing")) {
            boolean isItGoing = isGameStillGoing(Boolean.parseBoolean(receivedMessage.split(" ")[1]));
            sendMessage(String.valueOf(isItGoing));
        } else if (receivedMessage.startsWith("whowonthegame")) {
            sendMessage(whoWon());
        } else if (receivedMessage.startsWith("getroleofperson")) {
            Log.d("textdebug", "Received Message for getrole:" + receivedMessage);
            String role = getRoleOfPerson(receivedMessage.split("ङॠ")[1]);
            sendMessage(role);

        } else if (receivedMessage.startsWith("getnumberofpeople")) {
            sendMessage(String.valueOf(totalNumOfPlayers));

        } else if (receivedMessage.startsWith("setname")) {
            playerName = receivedMessage.split("ङॠ")[1];
            Log.d("textdebug", "Name received:" + playerName);
        }
        return receivedMessage;
    }

    public String quickReceive() throws InterruptedException, IOException {
        boolean loopRecceiveMessage = true;
        int currentNumberInLoop = 0;
        String receivedMessage = "";
        while (loopRecceiveMessage && (currentNumberInLoop < 3)) {
            Thread.sleep(65);
            receivedMessage = br.readLine();
            if (receivedMessage != null) {
                loopRecceiveMessage = false;
            }
            currentNumberInLoop++;
        }
        return receivedMessage;
    }

    public void textConversation() throws InterruptedException, IOException {
        long startTime = System.currentTimeMillis(); //fetch starting time
        while ((System.currentTimeMillis() - startTime) < (30000)) {
            Thread.sleep(200);
            String send = "";
            ArrayList<String> textList = MafiaServerGame.textMessages;
            ArrayList<String> textSenderList = MafiaServerGame.textMessageSender;
            for (int x = 0; x < textList.size(); x++) {
                send = send + textList.get(x) + "," + textSenderList.get(x) + " ";
            }
            sendMessage(send);
            Log.d("hostGame2", "We Made It here!");
            String message = /*quickReceive*/receiveMessage(socket);
            Log.d("hostGame2", "We Made It here2!");
            int position = MafiaServerGame.textMessages.size();
            if (message != null) {
                String[] split;
                split = message.split(",");
                Log.d("hostGame2", "Value of message: " + message);
                Log.d("hostGame3", "value of split[0]: " + split[0]);
                Log.d("hostGame3", "value of split[1]: " + split[1]);
                //MafiaServerGame.textMessages.add(position, split[0]);
                //MafiaServerGame.textMessageSender.add(position, split[1]);
            }
            sendMessage(send);
        }
    }

    public double returnTime() {
        long timeTemp = System.currentTimeMillis() - MafiaServerGame.startingTime;
        Log.d("almostdone", "timeTempInHost:" + timeTemp);
        timeTemp = timeTemp / 100;
        Log.d("almostdone", "timeTempInHost2:" + timeTemp);
        double actualTime = ((double) timeTemp);
        actualTime = actualTime / 10;
        Log.d("almostdone", "timeTempInHost3:" + actualTime);
        return actualTime;
    }

    public void startAlarm(double numOfSeconds) {
        totalTimePassed = System.currentTimeMillis() - startTimeOfTimer;
        alarmLength = (long) (numOfSeconds * 1000);
        alarmEndTime = alarmLength + lastAlarmEndTime;
        lastAlarmEndTime += alarmLength;
    }

    public boolean checkAlarm() {
        totalTimePassed = System.currentTimeMillis() - startTimeOfTimer;
        if (totalTimePassed <= alarmEndTime) {
            return true;
        } else {
            alarmLength = 0;
            return false;
        }
    }

    public long getAlarmTimeRemaining() {
        totalTimePassed = System.currentTimeMillis() - startTimeOfTimer;
        Log.d("inspectalarm", "totalTimePassed:" + totalTimePassed);
        return alarmEndTime - totalTimePassed;
    }

    public String returnPlayers() {
        //Log.d("textdebug", "ServerGame Player list 0:" + MafiaServerGame.players.get(0));
        //Log.d("textdebug", "ServerGame Player list 1:" + MafiaServerGame.players.get(1));
        String playerList = "playerlist";
        for (int x = 0; x < currentNumOfPlayers; x++) {
            playerList += "ङॠ" + MafiaServerGame.players.get(x);
        }
        Log.d("textdebug", "Value of playerList:" + playerList);
        return playerList;
    }

    public String updateMessages() {
        String returnedMessage = "";
        if (MafiaServerGame.textMessages == null) {
            MafiaServerGame.textMessages = new ArrayList();
        }
        if (MafiaServerGame.textMessageSender == null) {
            MafiaServerGame.textMessageSender = new ArrayList();
        }
        for (int x = 0; x < MafiaServerGame.textMessages.size(); x++) {
            Log.d("breakitdown", "textMessageTriggered");
            returnedMessage += MafiaServerGame.textMessages.get(x) + "\uD800\uDF0C" + MafiaServerGame.textMessageSender.get(x) + "\uD800\uDF0C";
        }
        Log.d("breakitdown", "ReturnedMessage:" + returnedMessage);
        return returnedMessage;
    }

    public void setVote(String personToKill) {
        if (MafiaServerGame.holdVotingInfo == null) {
            MafiaServerGame.holdVotingInfo = new HashMap<>();
        }
        Log.d("sender", "PlayerName:" + playerName);
        if (playerName != null) {
            MafiaServerGame.holdVotingInfo.put(playerName, personToKill);
        }
        Log.d("sender", "holdVotingInfo:" + MafiaServerGame.holdVotingInfo);
    }

    public void setMessage(String message) {
        if (MafiaServerGame.textMessages == null) {
            MafiaServerGame.textMessages = new ArrayList();
        }
        if (MafiaServerGame.textMessageSender == null) {
            MafiaServerGame.textMessageSender = new ArrayList();
        }
        if (playerName != null) {
            //textMessageHistory.add(message);
            // textMessageHistorySender.add(playerName);
            MafiaServerGame.textMessages.add(message);
            MafiaServerGame.textMessageSender.add(playerName);
            Log.d("messagesmafia", "added to array");
            Log.d("sender", "textmessagessent:" + MafiaServerGame.textMessages);
            Log.d("sender", "textmessagesenders:" + MafiaServerGame.textMessageSender);
            numberOfTextMessages++;
            Log.d("messagesmafia", "end of method");
        }
    }

    public static String getDead(Map mp) {
        ArrayList<String> personGettingVotedFor = new ArrayList<>();
        ArrayList<String> numberOfVotes = new ArrayList<>();
        if (mp == null) {
            Log.d("dead", "Its RANDOM 1");
            ArrayList<String> playerList = new ArrayList<>();
            for (int x = 0; x < MafiaServerGame.players.size(); x++) {
                playerList.add(x, (String) MafiaServerGame.players.get(x));
            }
            Collections.shuffle(playerList);
            String randomThing = playerList.get(0);
            return randomThing;
        }
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Log.d("deadvoting", "has next");
            Map.Entry pair = (Map.Entry) it.next();
            Log.d("deadvoting", "Pair Value:" + pair.getValue());
            if (personGettingVotedFor.contains(pair.getValue())) {
                Log.d("deadvoting", "in if statement");
                int index = personGettingVotedFor.indexOf(pair.getValue());
                numberOfVotes.add(index, String.valueOf(Integer.parseInt(numberOfVotes.get(index)) + 1));
            } else {
                Log.d("deadvoting", "in else statement");
                personGettingVotedFor.add((String) pair.getValue());
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        int highestVotes = 0;
        int locationOfHighestVote = 0;
        for (int x = 0; x < numberOfVotes.size(); x++) {
            if (Integer.parseInt(numberOfVotes.get(x)) > highestVotes) {
                highestVotes = Integer.parseInt(numberOfVotes.get(x));
                locationOfHighestVote = x;
            }
        }
        String deadPerson = "";
        if (personGettingVotedFor.size() == 0) {
            Log.d("deadvoting", "Its RANDOM 2");
            ArrayList<String> playerList = new ArrayList<>();
            for (int x = 0; x < MafiaServerGame.players.size(); x++) {
                playerList.add(x, (String) MafiaServerGame.players.get(x));
            }
            Collections.shuffle(playerList);
            String randomThing = playerList.get(0);
            return randomThing;
        }
        Log.d("deadvoting", "locationOfHighestVote:" + locationOfHighestVote);
        deadPerson = personGettingVotedFor.get(locationOfHighestVote);
        return deadPerson;
    }

    public void removePlayer(String player) {
        Log.d("players", "Player being killed" + player);
        Log.d("players", "We here");
        int positionOfPlayerInArray = MafiaServerGame.players.indexOf(player);
        Log.d("players", "positionOfPlayerInArray:" + positionOfPlayerInArray);
        if (positionOfPlayerInArray >= 0) {
            Log.d("players", "We here 2");
            Log.d("players", "Players Left Before removing:" + MafiaServerGame.players);
            MafiaServerGame.players.remove(player);
            Log.d("players", "We here 3");
            MafiaServerGame.role.remove(positionOfPlayerInArray);
            currentNumOfPlayers--;
            Log.d("players", "Players Left After removing:" + MafiaServerGame.players);
        }
        Log.d("players", "We here 4");
    }

    public void restartVoting(int numOfActivity) {
        if (!(MafiaServerGame.lastActivityRestartCalledNumber == numOfActivity)) {
            MafiaServerGame.holdVotingInfo = new HashMap<>();
        }
        MafiaServerGame.lastActivityRestartCalledNumber = numOfActivity;
    }

    public void changeTimerWhenConfirmed(boolean confirmed) {
        Log.d("finallap", "value of confirmed:" + confirmed);
        if (confirmed) {
            MafiaServerGame.numOfConfirms++;
        } else {
            MafiaServerGame.numOfConfirms--;
        }
        if (MafiaServerGame.numOfConfirms == /*totalNumOfPlayers*/1) {
            totalTimePassed = System.currentTimeMillis() - startTimeOfTimer;
            lastAlarmEndTime = totalTimePassed;
        }
    }

    public boolean isItConfirmed() {
        Log.d("confirmcheck", "isItConfirmedCalled");
        Log.d("numberconfirm", "value Of numOfConfirms::" + MafiaServerGame.numOfConfirms);
        if (MafiaServerGame.numOfConfirms == /*totalNumOfPlayers*/1) {
            return true;
        }
        return false;
    }

    public void resetConfirms() {
        MafiaServerGame.numOfConfirms = 0;
    }

    public boolean amIStillIn(String name) {
        return MafiaServerGame.players.contains(name);
    }

    public boolean isGameStillGoing(boolean afterVillageKill) {
        int numOfMafia = 0;
        int numOfInnocent = 0;
        boolean keepPlaying = true;
        for (int x = 0; x < MafiaServerGame.role.size(); x++) {
            if (MafiaServerGame.role.get(x).equals("mafia")) {
                numOfMafia++;
            } else {
                numOfInnocent++;
            }
        }
        if (afterVillageKill && numOfMafia >= numOfInnocent) {
            keepPlaying = false;
        } else if (numOfMafia > numOfInnocent) {
            keepPlaying = false;
        }
        if (numOfInnocent == 0 || numOfMafia == 0) {
            keepPlaying = false;
        }
        return keepPlaying;
    }

    public String whoWon() {
        int numOfMafia = 0;
        int numOfInnocent = 0;
        String whoWon = "";
        for (int x = 0; x < MafiaServerGame.role.size(); x++) {
            if (MafiaServerGame.role.get(x).equals("mafia")) {
                numOfMafia++;
            } else {
                numOfInnocent++;
            }
        }
        if (numOfMafia > numOfInnocent) {
            whoWon = "the mafia";
        } else {
            whoWon = "the village";
        }
        return whoWon;
    }

    public String getRoleOfPerson(String personToGetRoleOf) {
        Log.d("textdebug", "personTogetRoleOf:" + personToGetRoleOf);
        int place = MafiaServerGame.players.indexOf(personToGetRoleOf);
        String role = MafiaServerGame.role.get(place);
        return role;
    }

    public class RegisterNameReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("bruh", "we got the message!");
            String name = intent.getStringExtra("name");
            String port = intent.getStringExtra("port");
            try {
                Log.d("bruh", "Pre register");
                checkToRegister(name, port);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            Log.d("bruh", "Post register");
        }
    }

    public class GetPlayerUpdates extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getExtras().getString("message");
            saveWANMessages.injectMessage(message);
        }
    }

    public void checkToRegister(String name, String port) throws InterruptedException, IOException {
        Log.d("bruh", "Inside register");
        Log.d("bruh", "Value of portNumber:" + portNumber);
        Log.d("bruh", "value of port:" + port);
        if (portNumber == Integer.parseInt(port)) {
            Log.d("bruh", "portNumber is port");
            playerName = name;
            IntentFilter filter = new IntentFilter(name);
            _getUpdates = new GetPlayerUpdates();
            context.registerReceiver(_getUpdates, filter);
            doThingAfterNameConfirmed();
            Log.d("bruh", "do thing after name confirmed finished");
        }
    }

    public void sendMessageFirebase(String message) {
        Log.d("howmanytimes", "sendMessageFirebase");
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        data.put("purpose", "message");
        data.put("clientname", "none");
        data.put("gameCode", hostGame.gameCode);
        if (message.equals("hurry")) {
            data.put("token", "ङॠ");
        } else {
            data.put("token", playerName);
        }
        FirebaseFunctions.getInstance()
                .getHttpsCallable("sendMessage")
                .call(data)
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
        Log.d("conspiracytheories", "Sent message:" + message);
    }

    public void doThingAfterNameConfirmed() throws IOException, InterruptedException {
        Log.d("throwitup", "Do The Thing2 10");
        hostGame.currentNumOfPlayers++;
        currentNumOfPlayers++;
        sendMessage("totalplayers " + totalNumOfPlayers);
        Log.d("throwitup", "Do The Thing2 11");
        boolean loop = true;
        while (loop) {
            Log.d("throwitup", "Do The Thing2 12");
            Thread.sleep(200);
            sendMessage("currentplayers " + String.valueOf(hostGame.currentNumOfPlayers));
            Log.d("throwitup", "Do The Thing2 13");
            Log.d("petrolkids", "currentNum:" + currentNumOfPlayers);
            Log.d("petrolkids", "totalNum:" + totalNumOfPlayers);
            if (currentNumOfPlayers == totalNumOfPlayers) {
                Log.d("throwitup", "Do The Thing2 14");
                Log.d("hostGame2", "Loop Is Set To False");
                startTimeOfTimer = System.currentTimeMillis();
                Log.d("synccheck", "This is when the server timer is started");
                loop = false;
            }
            Log.d("throwitup", "Do The Thing2 15");
        }
        String setPlayerName = receiveMessage(socket);
        MafiaServerGame.players.add(playerName);
        Log.d("throwitup", "Do The Thing2 16");
        while (MafiaServerGame.sendRole == false) {
            Thread.sleep(250);
        }
        Log.d("throwitup", "Do The Thing2 17");
        int placeOfRoleInList = MafiaServerGame.players.indexOf(playerName);
        String role = (String) MafiaServerGame.role.get(placeOfRoleInList);
        Log.d("throwitup", "Do The Thing2 18");
    }

}