package com.example.eventplanner

import com.example.eventplanner.ui.getDayOfMonthStartingFromMonday
import java.time.YearMonth

class CalendarDataSource {
    fun getDates(yearMonth: YearMonth): List<CalendarUiState.Date> {
        return yearMonth.getDayOfMonthStartingFromMonday()
            .map { date ->
                CalendarUiState.Date(
                    dayOfMonth = if (date.monthValue == yearMonth.monthValue) {
                        "${date.dayOfMonth}"
                    } else {
                        "" // Fill with empty string for days outside the current month
                    },
                    isSelected = false // Initially no date is selected
                )
            }
    }
}
