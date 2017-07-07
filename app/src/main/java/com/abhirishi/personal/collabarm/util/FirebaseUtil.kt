package com.abhirishi.personal.collabarm.util

import android.content.Context
import com.abhirishi.personal.collabarm.AlarmsListener
import com.abhirishi.personal.collabarm.FriendsListener
import com.abhirishi.personal.collabarm.models.Alarm
import com.abhirishi.personal.collabarm.models.Friend
import com.google.firebase.database.*
import java.util.*

class FirebaseUtil {

    companion object {
        val database = FirebaseDatabase.getInstance().reference
        val ALARMS = "alarms"
        val USERS = "users"
        var availableFriends = ArrayList<Friend>()
        var alarms = ArrayList<Alarm>()

        fun checkForAlarms(userName: String, alarmsListener: AlarmsListener?) {
            database.child(ALARMS).child(userName).addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    alarms.clear()
                    dataSnapshot.children.forEach { it ->

                        val alarmSetters = it.value as HashMap<String, Long>
                        alarmSetters.forEach { _, value ->
                            val alarm = Alarm()
                            alarm.by = it.key as String
                            alarm.milliseconds = value
                            alarms.add(alarm)
                        }
                        Collections.sort(alarms)
                        alarmsListener?.onAlarmsChange()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
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


        fun getUsername(): String {
            return "abhithaparian"
        }

    }


}