package com.example.eventplanner

import com.example.eventplanner.ui.getDayOfMonthStartingFromMonday
import java.time.LocalDate
import java.time.YearMonth

class CalendarDataSource {
    // This method simulates fetching events for a given month
    fun getEventsForMonth(yearMonth: YearMonth): List<Event> {
        // Replace with your actual data source logic
        return listOf(
            Event(LocalDate.of(yearMonth.year, yearMonth.month, 5), "Konzert"),
            Event(LocalDate.of(yearMonth.year, yearMonth.month, 15), "Sportevent"),
            Event(LocalDate.of(yearMonth.year, yearMonth.month, 25), "Treffen mit Freunden")
        )
    }

    fun getEventsForDate(day: Int, yearMonth: YearMonth): List<String> {
        // Implementation to fetch events for the specific date
        return listOf("Konzert", "Sportevent") // Example events
    }

    fun getDates(yearMonth: YearMonth, events: List<Event>): List<CalendarUiState.Date> {
        val eventDates = events.groupBy { it.date }
        return yearMonth.getDayOfMonthStartingFromMonday()
            .map { date ->
                CalendarUiState.Date(
                    dayOfMonth = if (date.monthValue == yearMonth.monthValue) {
                        "${date.dayOfMonth}"
                    } else {
                        "" // Fill with empty string for days outside the current month
                    },
                    isSelected = false, // Initially no date is selected
                    hasEvents = eventDates.containsKey(date) // Check if the date has events
                )
            }
    }
}
data class Event(val date: LocalDate, val description: String)
