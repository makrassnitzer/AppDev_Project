package com.example.eventplanner

import java.time.YearMonth

data class CalendarUiState(
    val yearMonth: YearMonth,
    val dates: List<Date>,
    val selectedDateDetails: SelectedDateDetails? = null
) {
    companion object {
        val Init = CalendarUiState(
            yearMonth = YearMonth.now(),
            dates = emptyList()
        )
    }
    data class Date(
        val dayOfMonth: String,
        val isSelected: Boolean,
        val hasEvents: Boolean
    ) {
        companion object {
            val Empty = Date("", false, false)
        }
    }

    data class SelectedDateDetails(
        val day: String,
        val month: Int,
        val year: Int,
        val events: List<String>
    )
}