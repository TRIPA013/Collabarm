package com.abhirishi.personal.collabarm.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.abhirishi.personal.collabarm.AlarmReceiver
import com.abhirishi.personal.collabarm.AlarmsListener
import com.abhirishi.personal.collabarm.FriendsListener
import com.abhirishi.personal.collabarm.models.Alarm
import com.abhirishi.personal.collabarm.models.Friend
import com.google.firebase.database.*
import java.util.*


object FirebaseUtil {

    val database = FirebaseDatabase.getInstance().reference
    val ALARMS = "alarms"
    val ALARMS_SET = "alarms_set"
    val USERS = "users"
    var availableFriends = ArrayList<Friend>()
    var alarms = ArrayList<Alarm>()
    var alarmsSet = ArrayList<Alarm>()

    @JvmStatic fun checkForAlarms(context: Context, userName: String, alarmsListener: AlarmsListener?) {
        if (valueEventListenerForAlarms != null) {
            database.child(ALARMS).child(userName).removeEventListener(valueEventListenerForAlarms)
        }

        val valueEventListenerForAlarms = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                alarms.clear()
                dataSnapshot.children.forEach { it ->

                    val alarmSetters = it.value as HashMap<String, Long>
                    alarmSetters.forEach { _, value ->
                        if(value >= System.currentTimeMillis()) {
                            val alarm = Alarm()
                            alarm.by = it.key as String
                            alarm.milliseconds = value
                            alarms.add(alarm)
                        }
                    }
                    Collections.sort(alarms)
                    setAlarms(context)

                    alarmsListener?.onAlarmsChange()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        database.child(ALARMS).child(userName).addValueEventListener(valueEventListenerForAlarms)
    }

    var valueEventListenerForAlarms: ValueEventListener? = null

    var valueEventListenerForAlarmsSet: ValueEventListener? = null

    @JvmStatic fun checkForAlarmsSet(context: Context, userName: String, alarmsListener: AlarmsListener?) {
        if (valueEventListenerForAlarmsSet != null) {
            database.child(ALARMS_SET).child(userName).removeEventListener(valueEventListenerForAlarmsSet)
        }
        valueEventListenerForAlarmsSet = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                alarmsSet.clear()
                dataSnapshot.children.forEach { it ->

                    val alarmSetters = it.value as HashMap<String, Long>
                    alarmSetters.forEach { _, value ->
                        if(value >= System.currentTimeMillis()) {
                            val alarm = Alarm()
                            alarm.by = it.key as String
                            alarm.milliseconds = value
                            alarmsSet.add(alarm)
                        }
                    }
                    Collections.sort(alarmsSet)
                    setAlarms(context)

                    alarmsListener?.onAlarmsChange()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        database.child(ALARMS_SET).child(userName).addValueEventListener(valueEventListenerForAlarmsSet)
    }

    private fun setAlarms(context: Context) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        for (alarm in alarms) {
            if (alarm.milliseconds >= System.currentTimeMillis()) {
                val intent = Intent(context, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.milliseconds, pendingIntent)
            }
        }
    }

    fun checkForFriends(friends: List<Friend>, friendsListener: FriendsListener?) {
        database.child(USERS).addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val users = dataSnapshot.value as HashMap<String, String>
                availableFriends.clear()
                friends.forEach { friend ->
                    if (users.containsKey(friend.phoneNumber?.replace(" ", ""))) {
                        availableFriends.add(friend)
                    }
                }
                friendsListener?.onFriendsChange()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }


    fun setAlarmFor(context: Context, userName: String, forUser: String, milliseconds: String) {
        database.child(ALARMS).child(forUser).runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                return Transaction.success(mutableData)
            }

            override fun onComplete(databaseError: DatabaseError?, b: Boolean,
                                    dataSnapshot: DataSnapshot?) {
            }
        })
    }


    @JvmStatic fun getUsername(): String {
        return "abhithaparian"
    }

}