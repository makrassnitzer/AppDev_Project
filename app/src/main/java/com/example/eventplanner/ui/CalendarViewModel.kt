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
            _uiState.update { currentState ->
                currentState.copy(
                    dates = dataSource.getDates(currentState.yearMonth)
                )
            }
        }
    }

    fun toNextMonth(nextMonth: YearMonth) {
        viewModelScope.launch {
            val selectedDateDetails = _uiState.value.selectedDateDetails
            val dates = dataSource.getDates(nextMonth).map { date ->
                if (selectedDateDetails != null &&
                    selectedDateDetails.day == date.dayOfMonth &&
                    selectedDateDetails.month == nextMonth.monthValue &&
                    selectedDateDetails.year == nextMonth.year) {
                    date.copy(isSelected = true)
                } else {
                    date
                }
            }
            _uiState.update { currentState ->
                currentState.copy(
                    yearMonth = nextMonth,
                    dates = dates
                )
            }
        }
    }

    fun toPreviousMonth(prevMonth: YearMonth) {
        viewModelScope.launch {
            val selectedDateDetails = _uiState.value.selectedDateDetails
            val dates = dataSource.getDates(prevMonth).map { date ->
                if (selectedDateDetails != null &&
                    selectedDateDetails.day == date.dayOfMonth &&
                    selectedDateDetails.month == prevMonth.monthValue &&
                    selectedDateDetails.year == prevMonth.year) {
                    date.copy(isSelected = true)
                } else {
                    date
                }
            }
            _uiState.update { currentState ->
                currentState.copy(
                    yearMonth = prevMonth,
                    dates = dates
                )
            }
        }
    }

    fun onDateSelected(date: CalendarUiState.Date) {
        val updatedDates = _uiState.value.dates.map {
            if (it == date) {
                it.copy(isSelected = true)
            } else {
                it.copy(isSelected = false)
            }
        }
        _uiState.update {
            it.copy(
                dates = updatedDates,
                selectedDateDetails = CalendarUiState.SelectedDateDetails(
                    day = date.dayOfMonth,
                    month = it.yearMonth.monthValue,
                    year = it.yearMonth.year
                )
            )
        }
    }
}
