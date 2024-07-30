package com.example.eventplanner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventplanner.data.EventUtils
import com.example.eventplanner.ui.theme.Purple80
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.eventplanner.data.Event

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyEventsScreen(context: Context) {
    // events aus dem json laden
    val events = EventUtils.loadEventsFromFile(context);

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
    }

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
                    Text(
                        text = "All Events",
                        fontSize = 24.sp,
                        color = Purple80,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(6.dp)
                    )
                }

                LazyRow() {
                    items(events) { event ->
                        EventsCard(event)
                    }
                }
            }
        }
    }
}

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