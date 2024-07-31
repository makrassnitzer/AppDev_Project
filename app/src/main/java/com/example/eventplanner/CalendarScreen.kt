package com.example.eventplanner

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.CalendarContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventplanner.data.Event
import com.example.eventplanner.data.EventUtils
import com.example.eventplanner.ui.CalendarViewModel
import com.example.eventplanner.ui.DateUtil
import com.example.eventplanner.ui.getDisplayName
import com.example.eventplanner.ui.theme.Purple80
import java.time.YearMonth
import java.util.Calendar

// Enthält die Hauptkomponente und übergibt den Kontext der für die Eventselektion nötig ist
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(context: Context) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CalendarApp(context)
    }
}

// Hauptkomponente der Kalender-App
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
                )
            )
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

// Zeigt Details zum ausgewählten Datum an
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SelectedDateDetailsView(details: CalendarUiState.SelectedDateDetails) {
    ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.padding(10.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Selected Date: ",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Purple80
                )
                Text(
                    text = details.day + "." + details.month + "." + details.year,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Purple80
                )
            }
        }
    }
    ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.padding(10.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Events:",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Purple80
                )
                if(details.events.isEmpty()){
                    Text(
                        text = "Nothing to display",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Purple80
                    )
                }else {
                    LazyColumn {
                        items(details.events) { event ->
                            EventView(event)
                        }
                    }
                }
            }
        }
    }
}

// Zeigt ein einzelnes Event an und ermöglicht die Anzeige von Details in einem Dialog
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventView(event: Event) {
    var showDetail by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Purple80),
        modifier = Modifier
            .padding(10.dp)
            .width(300.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.bezeichnung,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Purple80
                )
            }
            IconButton(
                onClick = { showDetail = true },
                modifier = Modifier.background(
                    color = Purple80,
                    shape = RoundedCornerShape(10.dp)
                )
            ) {
                Icon(Icons.Default.Info, tint = Color.White, contentDescription = null)
            }
        }

        // info button handling
        if (showDetail) {
            AlertDialog(
                icon = {
                    Icon(Icons.Default.Info, tint = Purple80, contentDescription = null)
                },
                title = {
                    Text(
                        text = event.bezeichnung,
                        color = Purple80
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Date: ${event.datum.toLocalDate()}",
                            color = Purple80
                        )
                        Text(
                            text = "Time: ${event.datum.toLocalTime()}",
                            color = Purple80
                        )
                        Text(
                            text = "Type: ${event.eventart}",
                            color = Purple80
                        )
                        Text(
                            text = "Location: ${event.standort}",
                            color = Purple80
                        )
                        Text(
                            text = "Members: ${event.teilnehmer}",
                            color = Purple80
                        )
                        Text(
                            text = "Costs: ${event.ausgaben}",
                            color = Purple80
                        )
                        Text(
                            text = "Additional Info: ${event.additionalInfoPath}",
                            color = Purple80
                        )
                    }
                },
                onDismissRequest = {
                    showDetail = false
                },
                confirmButton = {
                    TextButton(
                        onClick = { showDetail = false }
                    ) {
                        Icon(Icons.Default.Close, tint = Color.Red, contentDescription = null)
                    }
                    TextButton(
                        onClick = {
                            val intent = createCalendarEventIntent(event)
                            context.startActivity(intent)
                            showDetail = false
                        }
                    ) {
                        Text("Add to Calendar")
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun createCalendarEventIntent(event: Event): Intent {
    val time = Calendar.getInstance().apply {
        set(event.datum.year, event.datum.monthValue, event.datum.dayOfMonth, event.datum.hour, event.datum.minute)
    }

    return Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, time.timeInMillis)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, time.timeInMillis)
        putExtra(CalendarContract.Events.TITLE, event.bezeichnung)
        putExtra(CalendarContract.Events.DESCRIPTION, event.additionalInfoPath)
        putExtra(CalendarContract.Events.EVENT_LOCATION, event.standort)
        putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
    }
}



// Kalender-Widget, das Tage, Monatsüberschrift und Navigationspfeile anzeigt
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
            days.forEach { day -> DayItem(day, modifier = Modifier.weight(1f)) }
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

// Überschrift des Kalenders mit Monatsnamen und Navigationstasten
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

// Anzeige eines Wochentags
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

// Inhalt des Kalenders, der die Tage des Monats anzeigt
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
                    if (item == CalendarUiState.Date.Empty) {
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

// Anzeige eines einzelnen Kalendertages
@Composable
fun ContentItem(
    date: CalendarUiState.Date,
    onClickListener: (CalendarUiState.Date) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = if (date.hasEvents) {// Andere Farbe wenn es an dem Tag ein Event gibt
                    Color.Yellow
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