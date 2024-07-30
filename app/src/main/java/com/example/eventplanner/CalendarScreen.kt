package com.example.eventplanner

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventplanner.data.Event
import com.example.eventplanner.data.EventUtils
import com.example.eventplanner.ui.CalendarViewModel
import com.example.eventplanner.ui.DateUtil
import com.example.eventplanner.ui.getDisplayName
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(context: Context) {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CalendarApp(context)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarApp(
    context: Context,
    viewModel: CalendarViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val events by remember { mutableStateOf(EventUtils.loadEventsFromFile(context)) }
    LaunchedEffect(events) {
        viewModel.setEvents(events)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ))
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Surface(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                CalendarWidget(
                    days = DateUtil.daysOfWeek,
                    yearMonth = uiState.yearMonth,
                    dates = uiState.dates,
                    onPreviousMonthButtonClicked = { prevMonth ->
                        viewModel.toPreviousMonth(prevMonth)
                    },
                    onNextMonthButtonClicked = { nextMonth ->
                        viewModel.toNextMonth(nextMonth)
                    },
                    onDateClickListener = { date ->
                        viewModel.onDateSelected(date)
                    }
                )
            }
            uiState.selectedDateDetails?.let {
                SelectedDateDetailsView(it)
            }
        }
    }
}

@Composable
fun SelectedDateDetailsView(details: CalendarUiState.SelectedDateDetails) {
    ElevatedCard (
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.padding(10.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Selected Date Details:",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Day: ${details.day}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Month: ${details.month}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Year: ${details.year}",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
    ElevatedCard (
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.padding(10.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Events:",
                    style = MaterialTheme.typography.headlineSmall
                )
                details.events.forEach { event ->
                    EventView(event)
                }
            }
        }
    }
}

@Composable
fun EventView(event: Event) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = event.eventart,  // Assuming Event has a title property
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
        )
        Text(
            text = event.bezeichnung,  // Assuming Event has a description property
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarWidget(
    days: Array<String>,
    yearMonth: YearMonth,
    dates: List<CalendarUiState.Date>,
    onPreviousMonthButtonClicked: (YearMonth) -> Unit,
    onNextMonthButtonClicked: (YearMonth) -> Unit,
    onDateClickListener: (CalendarUiState.Date) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            repeat(days.size) {
                val item = days[it]
                DayItem(item, modifier = Modifier.weight(1f))
            }
        }
        Header(
            yearMonth = yearMonth,
            onPreviousMonthButtonClicked = onPreviousMonthButtonClicked,
            onNextMonthButtonClicked = onNextMonthButtonClicked
        )
        Content(
            dates = dates,
            onDateClickListener = onDateClickListener
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Header(
    yearMonth: YearMonth,
    onPreviousMonthButtonClicked: (YearMonth) -> Unit,
    onNextMonthButtonClicked: (YearMonth) -> Unit,
) {
    Row {
        IconButton(onClick = {
            onPreviousMonthButtonClicked.invoke(yearMonth.minusMonths(1))
        }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(id = R.string.back)
            )
        }
        Text(
            text = yearMonth.getDisplayName(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        IconButton(onClick = {
            onNextMonthButtonClicked.invoke(yearMonth.plusMonths(1))
        }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.next)
            )
        }
    }
}

@Composable
fun DayItem(day: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = day,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(10.dp)
        )
    }
}

@Composable
fun Content(
    dates: List<CalendarUiState.Date>,
    onDateClickListener: (CalendarUiState.Date) -> Unit,
) {
    Column {
        var index = 0
        repeat(6) {
            if (index >= dates.size) return@repeat
            Row {
                repeat(7) {
                    val item = if (index < dates.size) dates[index] else CalendarUiState.Date.Empty
                    if(item == CalendarUiState.Date.Empty){
                        ContentItem(
                            date = item,
                            onClickListener = {},
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        ContentItem(
                            date = item,
                            onClickListener = onDateClickListener,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    index++
                }
            }
        }
    }
}

@Composable
fun ContentItem(
    date: CalendarUiState.Date,
    onClickListener: (CalendarUiState.Date) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = if (date.hasEvents) {// Change color if the date has events
                    //TODO Different Colors for Events
                    MaterialTheme.colorScheme.error
                } else if (date.isSelected) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    Color.Transparent
                }
            )
            .clickable {
                onClickListener(date)
            }
    ) {
        Text(
            text = date.dayOfMonth,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(10.dp)
        )
    }
}