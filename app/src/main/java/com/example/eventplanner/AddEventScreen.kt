package com.example.eventplanner

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eventplanner.data.EventUtils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.eventplanner.data.Event

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEventScreen(navController: NavController) {
    var bezeichnung by remember { mutableStateOf(TextFieldValue()) }
    var eventart by remember { mutableStateOf(TextFieldValue()) }
    var datum by remember { mutableStateOf(TextFieldValue()) }
    var standort by remember { mutableStateOf(TextFieldValue()) }
    var teilnehmer by remember { mutableStateOf(TextFieldValue()) }
    var ausgaben by remember { mutableStateOf(TextFieldValue()) }
    var additionalInfoPath by remember { mutableStateOf(TextFieldValue()) }

    val context = LocalContext.current

    var showSnackbar by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = bezeichnung,
                onValueChange = { bezeichnung = it },
                label = { Text("Bezeichnung") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = eventart,
                onValueChange = { eventart = it },
                label = { Text("Eventart") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = datum,
                onValueChange = { datum = it },
                label = { Text("Datum (yyyy-MM-dd-HH-mm-ss)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = standort,
                onValueChange = { standort = it },
                label = { Text("Standort") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = teilnehmer,
                onValueChange = { teilnehmer = it },
                label = { Text("Teilnehmer") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = ausgaben,
                onValueChange = { ausgaben = it },
                label = { Text("Ausgaben") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = additionalInfoPath,
                onValueChange = { additionalInfoPath = it },
                label = { Text("Additional Info Path") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")
                val eventDateTime = LocalDateTime.parse(datum.text, formatter)

                val event = Event(
                    id = EventUtils.generateId(),
                    bezeichnung = bezeichnung.text,
                    eventart = eventart.text,
                    datum = eventDateTime,
                    standort = standort.text,
                    teilnehmer = teilnehmer.text,
                    ausgaben = ausgaben.text.toDoubleOrNull() ?: 0.0,
                    additionalInfoPath = additionalInfoPath.text
                )
                EventUtils.saveEventToFile(event, context)
                showSnackbar = true
            }) {
                Text("Save Event")
            }
        }

        if (showSnackbar) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp),
                action = {
                    Button(onClick = {
                        showSnackbar = false
                        navController.popBackStack()
                    }) {
                        Text("OK")
                    }
                }
            ) {
                Text(text = "Event created successfully")
            }
        }
    }
}
