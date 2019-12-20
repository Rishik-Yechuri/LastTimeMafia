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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class MafiaNetworkCode extends AppCompatActivity {
    ProgressBar progressBar;
    int totalNumOfPlayers = hostGame.totalNumOfPlayers;
    int currentNumOfPlayers = 1;
    Socket socket;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setMax(Integer.valueOf(totalNumOfPlayers));
        progressBar.setProgress(2);
        Log.d("hostingGame", "It Works!");
    }

    BufferedReader br;
    public static PrintWriter out;

    public static void main(String args[]) throws IOException {
    }

    public String convertIP() throws UnknownHostException {
        String totalistic = "";

        String ipAddress = getIPAddress(true);
        Log.d("IP","This is What is returned from getIPAddress:" + ipAddress);
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
                loop = false;
            }
        }
        String playerName = receiveMessage(socket);
        MafiaServerGame.players.add(playerName);
        while (MafiaServerGame.sendRole == false) {
            Thread.sleep(250);
        }
        int placeOfRoleInList = MafiaServerGame.players.indexOf(playerName);
        Log.d("random", "PlaceOfRoleInList: " + placeOfRoleInList);
        String role = (String) MafiaServerGame.role.get(placeOfRoleInList);
        //sendMessage("Trash");
        Log.d("rolecheck","RoleSentPre");
        sendMessage(role);//Role
        Log.d("rolecheck","RoleSentPost");
        sendMessage(String.valueOf(MafiaServerGame.players.size()));//Number of players
        sendMessage("1");//Number of Mafia
        sendMessage("1");//Number of Guardian Angel
        sendMessage("0");//Number fo villager
        sendMessage("30");//Seconds for mafia to talk
        sendMessage("10");//Time for guardian angel
        sendMessage("30");//time for village to talk

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
        out.println(message);
        Log.d("hostGame2", "This is the Message:" + message);
    }

    public String receiveMessage(Socket socket) throws IOException, InterruptedException {
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
                Log.d("hostGame3","value of split[0]: " + split[0]);
                Log.d("hostGame3","value of split[1]: " + split[1]);
                //MafiaServerGame.textMessages.add(position, split[0]);
                //MafiaServerGame.textMessageSender.add(position, split[1]);
            }
            sendMessage(send);
        }
    }
}