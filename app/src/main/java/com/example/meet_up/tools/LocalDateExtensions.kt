package com.example.meet_up.tools

import java.util.Calendar
import java.time.LocalDate

fun LocalDate.toCalendar(): Calendar {
    return Calendar.getInstance().apply {
        set(year, month.value, dayOfMonth)
    }
}