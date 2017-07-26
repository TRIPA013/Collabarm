package com.abhirishi.personal.collabarm

import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar


class AlarmActivity : AppCompatActivity() {

    var ringtone: Ringtone? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton

        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(applicationContext, notification)
        ringtone!!.play()

        fab.setOnClickListener { view ->
            ringtone?.stop()
            Snackbar.make(view, "Alarm has been stopped!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

    }
}
