package com.example.lasttimemafia;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
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
    ArrayList messageSender = new ArrayList();
    ArrayList message = new ArrayList();
    LinearLayout rootView;
    String lastSender;
    int textsSent = 0;
    boolean boolLoop = true;
    public static Thread thread;
    static Handler handler;
    long lastTimeMessageSent = 0;
    public static boolean keepThreadRunning = true;
    View view;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle onSavedInstance) {
        /*View*/
        view = inflater.inflate(R.layout.frag2_layout, container, false);
        rootView = view.findViewById(R.id.realHoldsText);
        ///////////////////////////////////////////////////////////////////////////////
        //addTextToUI("stop","me");
        /////////////////////////////////////////////////////////////////////////////////
        View lowerView = view.findViewById(R.id.linearLayoutTextHolder);
        //View lowerView = view.findViewById(R.id.linearLayoutMain);
        Button votingButton = lowerView.findViewById(R.id.button7);
        votingButton.setOnClickListener(sendText);
        editText = view.findViewById(R.id.editText);
        //context = getActivity();

        if (onSavedInstance == null) {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Log.d("rotatecheck", "inside handlemessage");
                    Log.d("textvalue", "TextsSent:" + textsSent);
                    String value = msg.obj.toString();
                    Log.d("finalpackage", "Value:" + value);
                    String[] split = value.split("\uD800\uDF0C");
                    Log.d("finalpackage", "split[0]" + split[0]);
                    Log.d("finalpackage", "split[1]" + split[1]);
                    addTextToUI(split[0], split[1]);
                }
            };
            thread = new Thread(new MyThread());
            thread.start();
            Log.d("intstatus", "Status of Interuptted:" + thread.isInterrupted());
        }
        return view;
    }

    class MyThread implements Runnable {
        @Override
        public void run() {
            Message messageOfficial = Message.obtain();
            Object kring = "lel Akash";
            while (keepThreadRunning) {
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
                    Log.d("sizecheck", "Here");
                    messageOfficial = Message.obtain();
                    Log.d("sizecheck", "Here 2");
                    messageOfficial.obj = kring;
                    Log.d("sizecheck", "Here 3 ");
                    handler.sendMessage(messageOfficial);
                    Log.d("sizecheck", "Here 4");
                }
                /*Log.d("intstatus","Inside thread check:" + Thread.currentThread().isInterrupted());
                if(Thread.currentThread().isInterrupted()){
                    Log.d("intstatus","Thread is Interupted");
                    keepRunning = false;
                }*/
            }
        }
    }

    private View.OnClickListener sendText = new View.OnClickListener() {
        public void onClick(View v) {
            String thingToSend = "setmessage";
            if (editText.getText().toString().length() > 0) {
                thingToSend += " " + editText.getText().toString();
                Log.d("conflict", "TimeGap:" + (System.currentTimeMillis() - lastTimeMessageSent));
                // if (System.currentTimeMillis() - lastTimeMessageSent > 4000) {
                sendMessage(thingToSend);
                // lastTimeMessageSent = System.currentTimeMillis();
                //  }
            }
        }
    };

    public void addTextToUI(String messageToSend, String senderName) {
        if (lastSender == null) {
            lastSender = "";
        }
        Log.d("trueornot", "value of GetContext:" + context);
        if (context == null) {
            context = getActivity();
        }
        TextView textView = new TextView(context);

        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        textView.setBackgroundResource(R.drawable.circularbutton);

        int selectedColor = Color.rgb(237, 19, 74);

        textView.setTextSize(16);

        String finaltext = "";

        if (!lastSender.equals(senderName)) {
            finaltext = senderName + "\n" + messageToSend;
        } else {
            finaltext = messageToSend;
        }

        textView.setText(finaltext);
        rootView.addView(textView);
        Log.d("addembois", "Added");
        lastSender = senderName;

        textsSent++;
    }

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}