package com.example.lasttimemafia;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import static com.example.lasttimemafia.joinedGame.sendMessage;
import static com.example.lasttimemafia.ReavealRole.receiveMessage;
import static com.example.lasttimemafia.joinedGame.socket;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lasttimemafia.R;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class Frag1 extends Fragment {
int endNumber;
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag1_layout, container, false);
        LinearLayout rootView = view.findViewById(R.id.rootViewMafiaVoting);
        endNumber = 0;
        ArrayList<Button> holdButtons = new ArrayList<Button>();
        ArrayList<Button> holdButtons2 = new ArrayList<Button>();
        Button btnTag;
        Space space2 = new Space(getContext());
        space2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        space2.setMinimumHeight(dpToPx(20));
        rootView.addView(space2);
        String[] playerNames = new String[Integer.parseInt(joinedGame.totalNumOfPlayers)];
        sendMessage("getplayers");
        String tempPlayerList = "";
        try {
            tempPlayerList = receiveMessage(socket);
            Log.d("textdebug", "tempPlayerList:" + tempPlayerList);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        String[] cutPlayers = tempPlayerList.split(" ");
        Log.d("textdebug", "cutplayers:" + Arrays.toString(cutPlayers));
        if (cutPlayers[0].equals("playerlist")) {
            for (int y = 1; y < cutPlayers.length; y++) {
                playerNames[y - 1] = cutPlayers[y];
            }
        }
        Log.d("textdebug", "playerNames:" + Arrays.toString(playerNames));
        /*for(int p=0;p<playerNames.length;p++){
            holdButtons.get(p).setText(playerNames[p]);
        }*/

        /*for(int p=0;p<cutPlayers.length;p++){
            Button thick = holdButtons.get(0);
            //thick.setText("BP");
        }*/
        for (int x = 0; x < Integer.parseInt(MafiaClientGame.numOfPeople); x++) {
            btnTag = new Button(getContext());
            btnTag.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            btnTag.setBackgroundResource(R.drawable.circularbutton);
            holdButtons.add(btnTag);
            rootView.addView(btnTag);
            Space space = new Space(getContext());
            //space.setMinimumHeight(500);
            space.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            space.setMinimumHeight(dpToPx(20));
            rootView.addView(space);
        }
        for (int p = 0; p < playerNames.length; p++) {
            Log.d("textdebug", "PlayName.length:" + playerNames.length);
            Log.d("textdebug", "playerName value:" + playerNames[p]);
            holdButtons.get(p).setText(playerNames[p]);
        }
        for (Button btn : holdButtons) {
            btn.setOnClickListener(sendVote);
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private View.OnClickListener sendVote = new View.OnClickListener() {
        public void onClick(View v) {
            Button b = (Button) v;
            Log.d("sender", "buttonString:" + b.getText().toString());
            sendMessage("setvote" + " " + b.getText().toString()+" " + endNumber);
            endNumber++;
        }
    };
}