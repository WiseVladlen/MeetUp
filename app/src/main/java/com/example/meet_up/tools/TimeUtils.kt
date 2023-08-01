package com.example.meet_up.tools

import java.time.Duration

private const val SECONDS_OF_DAY = (60 * 60 * 24) - 1

fun Int.daysToSeconds(): Int {
    return this * (60 * 60 * 24) - 1
}

fun Duration.isAllDay(): Boolean {
    return this.seconds.compareTo(SECONDS_OF_DAY) == 0
}