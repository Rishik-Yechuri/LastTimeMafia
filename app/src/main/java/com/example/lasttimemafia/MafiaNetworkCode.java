package com.example.lasttimemafia;

import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
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
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;

public class MafiaNetworkCode extends AppCompatActivity {
    ProgressBar progressBar;
    int totalNumOfPlayers = hostGame.totalNumOfPlayers;
    int currentNumOfPlayers = 1;
    Socket socket;
    public static long startTimeOfTimer = System.currentTimeMillis();
    public static long totalTimePassed = 0;
    public static long alarmLength = 0;
    public static long alarmEndTime = 0;
    public static long lastAlarmEndTime = 0;
    String playerName;
    //Code for storing text messages and info
    HashMap<String, String> holdVotingInfo; //= new HashMap();
    ArrayList textMessageHistory;
    ArrayList textMessageHistorySender;
    int numberOfTextMessages = 0;

    //End of Code for storing text messages and info
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setMax(Integer.valueOf(totalNumOfPlayers));
        progressBar.setProgress(2);
        holdVotingInfo = new HashMap<>();
        textMessageHistory = new ArrayList();
        textMessageHistorySender = new ArrayList();
        Log.d("hostingGame", "It Works!");
    }

    BufferedReader br;
    public static PrintWriter out;

    public static void main(String args[]) throws IOException {
    }

    public String convertIP() throws UnknownHostException {
        String totalistic = "";

        String ipAddress = getIPAddress(true);
        Log.d("IP", "This is What is returned from getIPAddress:" + ipAddress);
        String[] IPSplit = ipAddress.split("\\.");
        if (IPSplit[0].equals("192") && IPSplit[1].equals("168")) {
            String first = "";
            String second = "";
            String finalFirst = "";
            String finalSecond = "";
            int firstNum = Integer.valueOf(IPSplit[2]);
            int secondNum = Integer.valueOf(IPSplit[3]);
            first = Integer.toString(firstNum);
            second = Integer.toString(secondNum);
            //Final Code
            totalistic = String.valueOf(firstNum) + "." + secondNum;


        } else if (IPSplit[0].equals("10")) {
            totalistic = "C";
            for (int looper = 3; looper < ipAddress.length(); looper++) {
                totalistic = totalistic + ipAddress.charAt(looper);
            }
        } else if (IPSplit[0].equals("172")) {
            totalistic = "A";
            for (int looper = 4; looper < ipAddress.length(); looper++) {
                totalistic = totalistic + ipAddress.charAt(looper);
            }
        } else {
            totalistic = "B" + ipAddress;
        }
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

    public void permaConnection(int portNumber2) throws IOException, InterruptedException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int portNumber = portNumber2;
        boolean run = true;
        ServerSocket serverSocket = new ServerSocket(portNumber);
        Log.d("hostingGame", "PortNumber:" + portNumber);
        socket = serverSocket.accept();
        Log.d("hostingGame", "Connected!");
        hostGame.currentNumOfPlayers++;
        currentNumOfPlayers++;
        OutputStream os = socket.getOutputStream();
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(os, true);
        //ProgressBar progressBar = findViewById(R.id.progressBar2);
        //progressBar.setMax(Integer.valueOf(totalNumOfPlayers));
        sendMessage("totalplayers " + totalNumOfPlayers);
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
        }
        playerName = receiveMessage(socket);
        MafiaServerGame.players.add(playerName);
        while (MafiaServerGame.sendRole == false) {
            Thread.sleep(250);
        }
        int placeOfRoleInList = MafiaServerGame.players.indexOf(playerName);
        Log.d("random", "PlaceOfRoleInList: " + placeOfRoleInList);
        String role = (String) MafiaServerGame.role.get(placeOfRoleInList);
        //sendMessage("Trash");
        Log.d("rolecheck", "RoleSentPre");
        sendMessage(role);//Role
        Log.d("rolecheck", "RoleSentPost");
        sendMessage(String.valueOf(MafiaServerGame.players.size()));//Number of players
        sendMessage("1");//Number of Mafia
        sendMessage("1");//Number of Guardian Angel
        sendMessage("0");//Number fo villager
        sendMessage("30");//Seconds for mafia to talk
        sendMessage("10");//Time for guardian angel
        sendMessage("30");//time for village to talk
        boolean keepLoopRunning = true;
        while (keepLoopRunning) {
            Log.d("conflict", "receiveMessage called");
            Log.d("lifecycle", "in loop");
            receiveMessage(socket);
        }
    }

    public char convertIntToChar(int number) {
        char sendBack = (char) (number);
        return sendBack;
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

    public static void sendMessage(String message) {
        Log.d("conflict", "Sent Message:" + message);
        out.println(message);
    }

    public String receiveMessage(Socket socket) throws IOException, InterruptedException {
        Log.d("lifecycle", "in receive message");
        Log.d("insidereceiver", "1");
        if (br == null) {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        Log.d("insidereceiver", "2");
        boolean loopRecceiveMessage = true;
        String receivedMessage = "";
        Log.d("insidereceiver", "3");
        while (loopRecceiveMessage) {
            Log.d("insidereceiver", "4");
            Thread.sleep(200);
            Log.d("insidereceiver", "5");
            Log.d("insidereceiver", "Reader Check:" + br.ready());
            receivedMessage = br.readLine();
            Log.d("insidereceiver", "6");
            if (receivedMessage != null) {
                Log.d("insidereceiver", "7");
                loopRecceiveMessage = false;
                Log.d("insidereceiver", "8");
            }
            Log.d("insidereceiver", "9");
        }
        Log.d("conflict", "Received Message:" + receivedMessage);
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
            receivedMessage = "nothing";
        }
        Log.d("updatemessages", "before else statement");
        Log.d("updatemessages", "What it is:" + receivedMessage);
        Log.d("lifecycle", "pre if condition");
        /*else*/
        if (receivedMessage.startsWith("updatemessages")) {
            Log.d("lifecycle", "inside if");
            Log.d("updatemessages", "in method");
            String listOfMessages = updateMessages();
            Log.d("updatemessages", "pre send");
            Log.d("lifecycle", "pre send");
            sendMessage(listOfMessages);
            Log.d("lifecycle", "post send");
            Log.d("updatemessages", "post send");
            receivedMessage = "nothing";
        } else if (receivedMessage.startsWith("setvote")) {
            //setVote(String.valueOf(receivedMessage.split(" ")[1]));
        } else if (receivedMessage.startsWith("setmessage")) {
            Log.d("messagesmafia", "in else statement");
            String combinedMessage = receivedMessage.substring(11);
            Log.d("messagesmafia", "calling method");
            setMessage(combinedMessage);
            Log.d("messagesmafia", "called method");
        }
        return receivedMessage;
    }

    public String quickReceive() throws InterruptedException, IOException {
        boolean loopRecceiveMessage = true;
        int currentNumberInLoop = 0;
        String receivedMessage = "";
        while (loopRecceiveMessage && (currentNumberInLoop < 3)) {
            Log.d("hostGame2", "value of currentNumberInLoop: " + currentNumberInLoop);
            Thread.sleep(65);
            Log.d("hostGame2", "we got past the thread sleep");
            receivedMessage = br.readLine();
            Log.d("hostGame2", "we read the nextLine");
            Log.d("hostGame2", "value of the nextLine: " + receivedMessage);
            if (receivedMessage != null) {
                loopRecceiveMessage = false;
            }
            currentNumberInLoop++;
        }
        Log.d("hostGame2", "quickReceive has finished");
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
        Log.d("serversync", "alarmEndTime:" + alarmEndTime);
        Log.d("serversync", "totalTimePassed:" + totalTimePassed);
        return alarmEndTime - totalTimePassed;
    }

    public String returnPlayers() {
        Log.d("textdebug", "ServerGame Player list 0:" + MafiaServerGame.players.get(0));
        Log.d("textdebug", "ServerGame Player list 1:" + MafiaServerGame.players.get(1));
        String playerList = "playerlist";
        for (int x = 0; x < totalNumOfPlayers; x++) {
            playerList += " " + MafiaServerGame.players.get(x);
        }
        Log.d("textdebug", "Value of playerList:" + playerList);
        return playerList;
    }

    public String updateMessages() {
        String returnedMessage = "";
        if (textMessageHistory == null) {
            textMessageHistory = new ArrayList();
        }
        if (textMessageHistorySender == null) {
            textMessageHistorySender = new ArrayList();
        }
        for (int x = 0; x < textMessageHistory.size(); x++) {
            Log.d("breakitdown", "textMessageTriggered");
            returnedMessage += textMessageHistory.get(x) + "\uD800\uDF0C" + textMessageHistorySender.get(x) + "\uD800\uDF0C";
        }
        Log.d("breakitdown", "ReturnedMessage:" + returnedMessage);
        return returnedMessage;
    }

    public void setVote(String personToKill) {
        if (holdVotingInfo == null) {
            holdVotingInfo = new HashMap<>();
        }
        Log.d("sender", "PlayerName:" + playerName);
        if (playerName != null) {
            holdVotingInfo.put(playerName, personToKill);
        }
        Log.d("sender", "holdVotingInfo:" + holdVotingInfo);
    }

    public void setMessage(String message) {
        if (textMessageHistory == null) {
            textMessageHistory = new ArrayList();
        }
        if (textMessageHistorySender == null) {
            textMessageHistorySender = new ArrayList();
        }
        if (playerName != null) {
            textMessageHistory.add(message);
            textMessageHistorySender.add(playerName);
            Log.d("messagesmafia", "added to array");
            Log.d("sender", "textmessagessent:" + textMessageHistory);
            Log.d("sender", "textmessagesenders:" + textMessageHistorySender);
            numberOfTextMessages++;
            Log.d("messagesmafia", "end of method");
        }
    }
}