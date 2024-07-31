package com.example.eventplanner

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventplanner.data.Event
import com.example.eventplanner.data.EventUtils
import com.example.eventplanner.ui.theme.Purple80

@SuppressLint("ResourceType")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyEventsScreen(context: Context) {
    // events aus dem json laden
    var events by remember { mutableStateOf(EventUtils.loadEventsFromFile(context)) }
    // selectedType fürs dropdown
    var selectedType by remember { mutableStateOf("all") }

    // set background img
    var modifier = Modifier
    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
    )

    // set header
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            stringResource(id = R.string.viewEvents),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            color = Purple80
        )
        Image(
            painter = painterResource(id = R.drawable.calendar),
            contentDescription = null,
            modifier = Modifier
                .size(130.dp)
                .padding(8.dp),
            contentScale = ContentScale.Crop
        )
    }

    // events column
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // events card
        Card(
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onTertiary
            ),
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CardHeader(
                        onFilterSelected = { filterType ->
                            selectedType = filterType
                            events = EventUtils.loadEventsFromFile(context).filter {
                                it.eventart == selectedType || selectedType == "all"
                            }
                        }
                    )
                }
                LazyColumn() {
                    items(events) { event ->
                        EventsCard(event)
                    }
                }
            }
        }
    }
}

// events card header
@Composable
private fun CardHeader(onFilterSelected: (String) -> Unit) {
    var showFilter by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf("all") }

    Text(
        text = "All Events",
        fontSize = 24.sp,
        color = Purple80,
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(6.dp)
    )
    IconButton(
        onClick = { showFilter = true },
        modifier = Modifier.background(
            color = Color.White,
            shape = RoundedCornerShape(10.dp)
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.filter),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }

    if (showFilter) {
        AlertDialog(
            title = {
                Text(
                    text = "Filter",
                    color = Purple80,
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Select type",
                        fontSize = 18.sp,
                        color = Purple80,
                    )
                    EventTypeDropdown(
                        selectedType = selectedType,
                        onSelected = { type ->
                            selectedType = type
                        }
                    )
                }
            },
            onDismissRequest = {
                showFilter = true
            },
            dismissButton = {
                TextButton(
                    onClick = { showFilter = false }
                ) {
                    Icon(Icons.Default.Close, tint = Color.Red, contentDescription = null)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showFilter = false
                        onFilterSelected(selectedType)
                    }
                ) {
                    Icon(Icons.Default.Check, tint = Color.Green, contentDescription = null)
                }
            }
        )
    }
}

// events card body
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun EventsCard(event: Event) {
    var showDetail by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Purple80),
        modifier = Modifier
            .padding(10.dp)
            .width(300.dp)
    ) {
        Row(modifier = Modifier.padding(18.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.bezeichnung,
                    fontSize = 18.sp,
                    color = Purple80,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = event.datum.toLocalDate().toString(),
                    color = Purple80,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light
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
            }
        )
    }
}

// filter-dropdown
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventTypeDropdown(selectedType: String, onSelected: (String) -> Unit) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = Modifier
            .width(170.dp)
            .height(45.dp)
    ) {
        TextField(
            value = selectedType,
            onValueChange = {},
            readOnly = true,
            textStyle = TextStyle().copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                color = Purple80
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedTrailingIconColor = Purple80,
                unfocusedTrailingIconColor = Purple80,
                focusedIndicatorColor = Purple80,
                unfocusedIndicatorColor = Purple80
            ),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "all",
                        color = Purple80,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Light
                    )
                },
                onClick = {
                    onSelected("all")
                    isExpanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.type_private),
                        color = Purple80,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Light
                    )
                },
                onClick = {
                    onSelected("private")
                    isExpanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.type_work),
                        color = Purple80,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Light
                    )
                },
                onClick = {
                    onSelected("work")
                    isExpanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.type_sports),
                        color = Purple80,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Light
                    )
                },
                onClick = {
                    onSelected("sports")
                    isExpanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.type_concert),
                        color = Purple80,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Light
                    )
                },
                onClick = {
                    onSelected("concert")
                    isExpanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.type_other),
                        color = Purple80,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Light
                    )
                },
                onClick = {
                    onSelected("other")
                    isExpanded = false
                }
            )
        }
    }
}