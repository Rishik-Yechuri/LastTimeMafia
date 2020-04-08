package com.example.lasttimemafia;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lasttimemafia.R;

import java.util.ArrayList;


public class Frag1 extends Fragment {

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag1_layout, container, false);
        LinearLayout rootView = view.findViewById(R.id.rootViewMafiaVoting);
        ArrayList<Button> holdButtons = new ArrayList<>();
        Button btnTag;
        for(int x = 0;x<Integer.parseInt(MafiaClientGame.numOfPeople);x++){
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
        /*Button[] holdButtons = new Button[20];
        holdButtons[0] = view.findViewById(R.id.votingButton1);
        holdButtons[1] = view.findViewById(R.id.votingButton2);
        holdButtons[2]  = view.findViewById(R.id.votingButton3);
        holdButtons[3] = view.findViewById(R.id.votingButton4);
        holdButtons[4]  = view.findViewById(R.id.votingButton5);
        holdButtons[5]  = view.findViewById(R.id.votingButton6);
        holdButtons[6]  = view.findViewById(R.id.votingButton7);
        holdButtons[7]  = view.findViewById(R.id.votingButton8);
        holdButtons[8]  = view.findViewById(R.id.votingButton9);
        holdButtons[9]  = view.findViewById(R.id.votingButton10);
        holdButtons[10]   = view.findViewById(R.id.votingButton11);
        holdButtons[11]  = view.findViewById(R.id.votingButton12);
        holdButtons[12]  = view.findViewById(R.id.votingButton13);
        holdButtons[13]  = view.findViewById(R.id.votingButton14);
        holdButtons[14]   = view.findViewById(R.id.votingButton15);
        holdButtons[15] = view.findViewById(R.id.votingButton16);
        holdButtons[16]  = view.findViewById(R.id.votingButton17);
        holdButtons[17]  = view.findViewById(R.id.votingButton18);
        holdButtons[18]  = view.findViewById(R.id.votingButton19);
        holdButtons[19] = view.findViewById(R.id.votingButton20);*/
        //votingButton1.setVisibility(View.GONE);
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
}