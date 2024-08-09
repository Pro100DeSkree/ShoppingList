package com.deskree.shoppinglist.utils

import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

object TimeManager {
    private const val DEFAULT_TIME_FORMAT = "HH:mm:ss - dd.MM.yy"
    fun getCurrentTime(): String{
        val formatter = SimpleDateFormat(DEFAULT_TIME_FORMAT, Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }
    fun getTimeFormat(time: String, defPreferences: SharedPreferences): String{
        val defFormatter = SimpleDateFormat(DEFAULT_TIME_FORMAT, Locale.getDefault())
        val defDate = defFormatter.parse(time)
        val newFormat = defPreferences.getString("time_format_key", DEFAULT_TIME_FORMAT)
        val newFormatter = SimpleDateFormat(newFormat, Locale.getDefault())
        return if(defDate != null)
            newFormatter.format(defDate)
        else
            time
    }

    fun getCurrentTimeAd(): Int{
        var second = 0
        val formatterHH = SimpleDateFormat("HH", Locale.getDefault())
        val formatterMM = SimpleDateFormat("mm", Locale.getDefault())
        val formatterSS = SimpleDateFormat("ss", Locale.getDefault())
        second = formatterHH.format(Calendar.getInstance().time).toInt() * 60 * 60
        second += formatterMM.format(Calendar.getInstance().time).toInt() * 60
        second += formatterSS.format(Calendar.getInstance().time).toInt()
        return second
    }
}