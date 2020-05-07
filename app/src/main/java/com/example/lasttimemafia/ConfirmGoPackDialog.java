package com.example.lasttimemafia;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

public class ConfirmGoPackDialog extends AppCompatDialogFragment {
    Context mContext;

    public ConfirmGoPackDialog(Context context) {
        this.mContext = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to leave?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Message tempMessage = Message.obtain();
                tempMessage.obj = "mainmenu";
                ((Activity) mContext).finish();
                //ReavealRole.handler.sendMessage(tempMessage);
            }
        });
        return builder.create();
    }
}
