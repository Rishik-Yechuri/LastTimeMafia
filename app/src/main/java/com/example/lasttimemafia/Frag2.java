package com.example.lasttimemafia;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lasttimemafia.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static com.example.lasttimemafia.ReavealRole.receiveMessage;
import static com.example.lasttimemafia.joinedGame.socket;

import static com.example.lasttimemafia.joinedGame.sendMessage;


public class Frag2 extends Fragment {
    EditText editText;
    ArrayList messageSender;
    ArrayList message;
    LinearLayout rootView;
    String lastSender;
    int textsSent;
    boolean boolLoop;
    Thread thread;
    Handler handler;
    long lastTimeMessageSent = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag2_layout, container, false);
        rootView = view.findViewById(R.id.realHoldsText);
        View lowerView = view.findViewById(R.id.linearLayoutTextHolder);
        //View lowerView = view.findViewById(R.id.linearLayoutMain);
        Button votingButton = lowerView.findViewById(R.id.button7);
        thread = new Thread(new MyThread());
        thread.start();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d("receiverApp", "Message is being handled by main thread.");
                String value = msg.obj.toString();
                String[] split = value.split("\uD800\uDF0C");
                addTextToUI(split[0], split[1]);
            }
        };
        textsSent = 0;
        message = new ArrayList();
        messageSender = new ArrayList();
        votingButton.setOnClickListener(sendText);
        editText = view.findViewById(R.id.editText);
        /*addTextToUI("lel", "Akash");
        addTextToUI("Who though?", "Saahil");
        addTextToUI("I think we should vote out Saahas", "Saahil");
        addTextToUI("Cause I think he knows about us", "Saahil");
        addTextToUI("Guys lets vote ourselves out to trick them", "Bhaskar");
        addTextToUI("Very Smort Boscer", "Saahil");
        addTextToUI("lel", "Akash");
        addTextToUI("But is it smort??", "Saahil");
        addTextToUI("jk im trolling", "Saahil");
        addTextToUI("but once we get rid of him we can spread propaganda", "Saahil");
        addTextToUI("we can keep killing more inncoent people", "Bhaskar");
        addTextToUI("That is what i like to hear", "Saahil");
        addTextToUI("ye", "Akash");
        addTextToUI("tell me the strats though", "Saahil");
        addTextToUI("Big brain time,thats why", "Saahil");
        addTextToUI("but don't kill me", "Saahil");
        addTextToUI("ok lets vote out Akash cause he isn't helping us", "Bhaskar");
        addTextToUI("^Respecc", "Saahil");
        addTextToUI("lel", "Akash");
        addTextToUI("Stop saying that", "Saahil");
        addTextToUI("yeah I might just vote for Akash", "Saahil");
        addTextToUI("Cause I think he will rat us out", "Saahil");
        addTextToUI("You are indeed a absolute stonk", "Bhaskar");
        addTextToUI("Once again,you are very smort boscer", "Saahil");*/

      /* new Thread() {
            public void run() {

            }
        }.start();*/
        boolLoop = true;
        /*while (boolLoop) {
            try {
                Thread.sleep(100);
                sendMessage("updatemessages");
                String getTextHistory = receiveMessage(socket);
                Log.d("breakitdown", "gettexthistory:" + getTextHistory);
                String[] breakItDown = getTextHistory.split("\uD800\uDF0C");
                Log.d("breakitdown", "Breakitdown:" + Arrays.toString(breakItDown));
                if (breakItDown.length > 1) {
                    message = new ArrayList();
                    messageSender = new ArrayList();
                    for (int x = 0; x < breakItDown.length; x += 2) {
                        message.add(breakItDown[x]);
                        messageSender.add(breakItDown[x + 1]);
                    }
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            Log.d("receiver", "Hikring");
            Log.d("receiver", "TextSent size:" + textsSent);
            Log.d("receiver", "message list size:" + message.size());
            if(textsSent<message.size()){
                Log.d("receiver","Added to UI");
                addTextToUI((String)message.get(textsSent),(String)messageSender.get(textsSent));
            }
            sendMessage("checkalarm");
            String loopOrNot = "";
            try {
                loopOrNot = receiveMessage(socket);
                boolLoop = Boolean.parseBoolean(loopOrNot);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        return view;


        /*while(boolLoop){

            sendMessage("checkalarm");
            String loopOrNot = "";
            try {
                loopOrNot = receiveMessage(socket);
                boolLoop = Boolean.parseBoolean(loopOrNot);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        //return super.onCreateView(inflater,container,savedInstanceState);
    }

    class MyThread implements Runnable {
        @Override
        public void run() {
            Message messageOfficial = Message.obtain();
            Object kring = "lel Akash";
            while (true) {
                try {
                    Thread.sleep(100);
                    Log.d("receiverApp", "Text Message request sent");
                    sendMessage("updatemessages");
                    String getTextHistory = receiveMessage(socket);
                    Log.d("receiverApp", "Text Messages Have been received");
                    Log.d("breakitdown", "gettexthistory:" + getTextHistory);
                    String[] breakItDown = getTextHistory.split("\uD800\uDF0C");
                    Log.d("breakitdown", "Breakitdown:" + Arrays.toString(breakItDown));
                    if (breakItDown.length > 1) {
                        message = new ArrayList();
                        messageSender = new ArrayList();
                        for (int x = 0; x < breakItDown.length; x += 2) {
                            message.add(breakItDown[x]);
                            messageSender.add(breakItDown[x + 1]);
                        }
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
                Log.d("receiverApp", "Hikring");
                Log.d("receiverApp", "TextSent size:" + textsSent);
                Log.d("receiverApp", "message list size:" + message.size());
                if (textsSent < message.size()) {
                    kring = message.get(textsSent) + "\uD800\uDF0C" + messageSender.get(textsSent);
                    messageOfficial = Message.obtain();
                    messageOfficial.obj = kring;
                    Log.d("receiverApp", "MessageOfficial:" + messageOfficial.obj);
                    Log.d("receiverApp", "Message is in second thread");
                    handler.sendMessage(messageOfficial);
                }
            }
        }
    }

    private View.OnClickListener sendText = new View.OnClickListener() {
        public void onClick(View v) {
            String thingToSend = "setmessage";
            if (editText.getText().toString().length() > 0) {
                thingToSend += " " + editText.getText().toString();
                Log.d("conflict","TimeGap:" + (System.currentTimeMillis()-lastTimeMessageSent));
               // if (System.currentTimeMillis() - lastTimeMessageSent > 4000) {
                    sendMessage(thingToSend);
                   // lastTimeMessageSent = System.currentTimeMillis();
              //  }
            }
        }
    };

    public void addTextToUI(String messageToSend, String senderName) {
        Log.d("receiverApp", "Message is being sent to UI");
        if (lastSender == null) {
            lastSender = "";
        }
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setBackgroundResource(R.drawable.circularbutton);
        int selectedColor = Color.rgb(237, 19, 74);
        //textView.setTextColor(selectedColor);
        textView.setTextSize(16);
        String finaltext = "";
        if (!lastSender.equals(senderName)) {
            finaltext = senderName + "\n" + messageToSend;
        } else {
            finaltext = messageToSend;
        }
        textView.setText(finaltext);
        rootView.addView(textView);
        lastSender = senderName;
        textsSent++;
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

}