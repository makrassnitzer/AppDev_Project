package com.example.eventplanner

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.eventplanner.data.Event
import com.example.eventplanner.ui.getDayOfMonthStartingFromMonday
import java.time.YearMonth

class CalendarDataSource {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDates(yearMonth: YearMonth, events: List<Event>): List<CalendarUiState.Date> {
        val eventDates = events.groupBy { it.datum.toLocalDate() }
        return yearMonth.getDayOfMonthStartingFromMonday().map { datum ->
            CalendarUiState.Date(
                dayOfMonth = if (datum.monthValue == yearMonth.monthValue) {
                    "${datum.dayOfMonth}"
                } else {
                    "" // Tage auserhalb des Monats sind leer
                },
                isSelected = false, // Kein Tag ist am Begin selektiert
                hasEvents = eventDates.containsKey(datum) // Es wird geschaut ob ein Tag ein Event hat um es dann farblich zu markieren
            )
        }
    }
}
