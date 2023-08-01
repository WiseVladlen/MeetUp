package com.example.meet_up.tools

import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

fun Calendar.getStartOfDayCalendar(): Calendar {
    return (clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }
}

fun Calendar.getEndOfDayCalendar(): Calendar {
    return (clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
    }
}

fun Calendar.copyFrom(calendar: Calendar) {
    apply {
        set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DATE),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND)
        )
    }
}

fun dateToLocalDate(date: Date): LocalDate {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}