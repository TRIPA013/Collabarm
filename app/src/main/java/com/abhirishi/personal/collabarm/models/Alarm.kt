package com.abhirishi.personal.collabarm.models

import java.text.SimpleDateFormat
import java.util.*


class Alarm : Comparable<Alarm> {
    var milliseconds: Long = 0L
    var by: String? = null

    fun getFormattedAlarmTime(): String {
        return getDate(milliseconds, "dd/MM/yyyy hh:mm")
    }

    override fun compareTo(compareAlarm: Alarm): Int {
        val l = this.milliseconds - compareAlarm.milliseconds
        return if (l == 0L) 0 else if (l > 0) 1 else -1
    }

    fun getDate(milliSeconds: Long, dateFormat: String): String {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds)
        return formatter.format(calendar.getTime())
    }

}