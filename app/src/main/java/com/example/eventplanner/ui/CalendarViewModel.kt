package com.example.eventplanner.ui

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventplanner.CalendarDataSource
import com.example.eventplanner.CalendarUiState
import com.example.eventplanner.data.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
class CalendarViewModel : ViewModel() {
    private val dataSource by lazy { CalendarDataSource() }

    private val _uiState = MutableStateFlow(CalendarUiState.Init)
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()
    private var allEvents: List<Event> = emptyList()


    fun setEvents(events: List<Event>) {
        allEvents = events
        viewModelScope.launch {
            fetchEventsForMonth(_uiState.value.yearMonth)
        }
    }

    fun toNextMonth(nextMonth: YearMonth) {
        fetchEventsForMonth(nextMonth)
        handleMonthChange(nextMonth)
    }

    fun toPreviousMonth(prevMonth: YearMonth) {
        fetchEventsForMonth(prevMonth)
        handleMonthChange(prevMonth)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onDateSelected(date: CalendarUiState.Date) {
        viewModelScope.launch {
            val updatedDates = _uiState.value.dates.map {
                it.copy(isSelected = it == date)
            }
            _uiState.update {
                it.copy(
                    dates = updatedDates,
                    selectedDateDetails = CalendarUiState.SelectedDateDetails(
                        day = date.dayOfMonth,
                        month = it.yearMonth.monthValue,
                        year = it.yearMonth.year,
                        events = allEvents.filter { event ->
                            event.datum.dayOfMonth == date.dayOfMonth.toInt() &&
                            event.datum.monthValue == it.yearMonth.monthValue &&
                            event.datum.year == it.yearMonth.year
                        }
                    )
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleMonthChange(monthChange: YearMonth) {
        viewModelScope.launch {
            val selectedDateDetails = _uiState.value.selectedDateDetails
            val events = allEvents.filter {
                it.datum.monthValue == monthChange.monthValue &&
                it.datum.year == monthChange.year
            }
            val dates = dataSource.getDates(monthChange, events).map { date ->
                if (selectedDateDetails != null &&
                    selectedDateDetails.day == date.dayOfMonth &&
                    selectedDateDetails.month == monthChange.monthValue &&
                    selectedDateDetails.year == monthChange.year
                ) {
                    date.copy(isSelected = true)
                } else {
                    date
                }
            }
            _uiState.update { currentState ->
                currentState.copy(
                    yearMonth = monthChange,
                    dates = dates
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchEventsForMonth(yearMonth: YearMonth) {
        viewModelScope.launch {
            val events = allEvents.filter {
                it.datum.monthValue == yearMonth.monthValue &&
                it.datum.year == yearMonth.year
            }
            val dates = dataSource.getDates(yearMonth, events)
            _uiState.update { currentState ->
                currentState.copy(
                    yearMonth = yearMonth,
                    dates = dates
                )
            }
        }
    }
}
