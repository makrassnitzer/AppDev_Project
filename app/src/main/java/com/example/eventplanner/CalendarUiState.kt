package com.example.eventplanner

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.eventplanner.data.Event
import java.time.YearMonth

// Zustand des Kalenders
data class CalendarUiState(
    val yearMonth: YearMonth,
    val dates: List<Date>,
    val selectedDateDetails: SelectedDateDetails? = null
) {
    companion object {
        // Initialzustand des Kalenders
        @RequiresApi(Build.VERSION_CODES.O)
        val Init = CalendarUiState(
            yearMonth = YearMonth.now(),
            dates = emptyList()
        )
    }
    // Datenklasse für Datum
    data class Date(
        val dayOfMonth: String,
        val isSelected: Boolean,
        val hasEvents: Boolean
    ) {
        companion object {
            val Empty = Date("", false, false)
        }
    }

    // Datenklasse für für die Details eine ausgewählten Datums
    data class SelectedDateDetails(
        val day: String,
        val month: Int,
        val year: Int,
        val events: List<Event>
    )
}