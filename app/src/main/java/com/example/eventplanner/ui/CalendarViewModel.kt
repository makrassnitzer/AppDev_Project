package com.example.eventplanner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventplanner.CalendarDataSource
import com.example.eventplanner.CalendarUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.YearMonth

class CalendarViewModel : ViewModel() {
    private val dataSource by lazy { CalendarDataSource() }

    private val _uiState = MutableStateFlow(CalendarUiState.Init)
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
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

    fun onDateSelected(date: CalendarUiState.Date) {
        viewModelScope.launch {
            val events = dataSource.getEventsForDate(date.dayOfMonth.toInt(), _uiState.value.yearMonth)
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
                        events = events
                    )
                )
            }
        }
    }

    private fun handleMonthChange(monthChange: YearMonth) {
        viewModelScope.launch {
            val selectedDateDetails = _uiState.value.selectedDateDetails
            val events = dataSource.getEventsForMonth(monthChange)
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

    private fun fetchEventsForMonth(yearMonth: YearMonth) {
        viewModelScope.launch {
            val events = dataSource.getEventsForMonth(yearMonth)
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
