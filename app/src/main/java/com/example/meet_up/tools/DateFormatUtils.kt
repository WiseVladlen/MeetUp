package com.example.meet_up.tools

import java.text.DateFormat
import java.util.Date

fun Date.toFullFormat(): String {
    return DateFormat.getDateInstance(DateFormat.FULL).format(this)
}

fun Date.toShortFormat(): String {
    return DateFormat.getTimeInstance(DateFormat.SHORT).format(this)
}