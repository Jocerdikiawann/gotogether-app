package com.example.livetracking.utils

import android.annotation.SuppressLint
import org.joda.time.DateTime
import java.text.SimpleDateFormat

fun getTodayTimeStamp(): Long {
    return DateTime().millis
}

@SuppressLint("SimpleDateFormat")
fun Long.formatDate(pattern: String = ""): String {
    if (pattern.isBlank()) {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this)
    }
    return SimpleDateFormat(pattern).format(this)
}