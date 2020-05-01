package com.example.lasttimemafia;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lasttimemafia.R;

import java.util.ArrayList;
import java.util.List;

public class ChangeBackground extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_game);
        if (savedInstanceState == null) {
            Button button = (Button) findViewById(R.id.button4);
            button.setText("Connected");
        }
    }
}