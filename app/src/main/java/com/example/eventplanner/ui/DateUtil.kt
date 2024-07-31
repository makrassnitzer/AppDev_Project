package com.example.eventplanner.ui

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.format.TextStyle
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters
import java.util.Locale

object DateUtil {

    // Gibt die Wochentage als Array von Strings zurück, beginnend mit Montag
    val daysOfWeek: Array<String>
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            val daysOfWeek = Array(7) { "" }

            for (dayOfWeek in DayOfWeek.entries) {
                val localizedDayName = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                daysOfWeek[dayOfWeek.value - 1] = localizedDayName
            }

            return daysOfWeek
        }
}

@RequiresApi(Build.VERSION_CODES.O)
fun YearMonth.getDayOfMonthStartingFromMonday(): List<LocalDate> {
    val firstDayOfMonth = this.atDay(1)
    val lastDayOfMonth = this.atEndOfMonth()

    // Bestimmt den ersten Montag vor oder am ersten Tag des Monats
    val firstMonday = firstDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    // Bestimmt den letzten Sonntag nach oder am letzten Tag des Monats
    val lastSunday = lastDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

    // Generiert eine Liste von Daten vom ersten Montag bis zum letzten Sonntag
    return (0..lastSunday.toEpochDay() - firstMonday.toEpochDay()).map {
        firstMonday.plusDays(it)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun YearMonth.getDisplayName(): String {
    return "${month.getDisplayName(TextStyle.FULL, Locale.getDefault())} $year"
}