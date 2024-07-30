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
                    "" // Fill with empty string for days outside the current month
                },
                isSelected = false, // Initially no date is selected
                hasEvents = eventDates.containsKey(datum) // Check if the date has events
            )
        }
    }
}
