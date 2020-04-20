package com.example.lasttimemafia

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Space
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.util.*
import com.example.lasttimemafia.joinedGame.sendMessage
import com.example.lasttimemafia.ReavealRole.receiveMessage
import com.example.lasttimemafia.joinedGame.socket

class VillagerVoting : AppCompatActivity() {
    var endNumber = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frag1_layout)

        val rootView: LinearLayout = findViewById(R.id.rootViewMafiaVoting)
        endNumber = 0
        val holdButtons = ArrayList<Button>()
        val holdButtons2 = ArrayList<Button>()
        var btnTag: Button
        val space2 = Space(this)
        space2.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        space2.minimumHeight = dpToPx(20)
        rootView.addView(space2)
        val playerNames = arrayOfNulls<String>(joinedGame.totalNumOfPlayers.toInt())
        sendMessage("getplayers")
        var tempPlayerList = ""
        try {
            tempPlayerList = receiveMessage(socket)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val cutPlayers = tempPlayerList.split(" ").toTypedArray()
        if (cutPlayers[0] == "playerlist") {
            for (y in 1 until cutPlayers.size) {
                playerNames[y - 1] = cutPlayers[y]
            }
        }
        for (x in 0 until MafiaClientGame.numOfPeople.toInt()) {
            btnTag = Button(this)
            btnTag.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            btnTag.setBackgroundResource(R.drawable.circularbutton)
            holdButtons.add(btnTag)
            rootView.addView(btnTag)
            val space = Space(this)
            space.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            space.minimumHeight = dpToPx(20)
            rootView.addView(space)
        }
        for (p in playerNames.indices) {
            Log.d("textdebug", "PlayName.length:" + playerNames.size)
            Log.d("textdebug", "playerName value:" + playerNames[p])
            holdButtons[p].text = playerNames[p]
        }
        for (btn in holdButtons) {
            btn.setOnClickListener(sendVote)
        }
    }

    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    }

    private val sendVote = View.OnClickListener { v ->
        val b = v as Button
        Log.d("sender", "buttonString:" + b.text.toString())
        sendMessage("setvote" + " " + b.text.toString() + " " + endNumber)
        endNumber++
    }
}
