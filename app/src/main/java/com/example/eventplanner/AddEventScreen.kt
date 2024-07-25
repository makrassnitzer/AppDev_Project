package com.example.eventplanner

import EventViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventplanner.data.Event

@Composable
fun AddEventScreen(eventViewModel: EventViewModel = viewModel()) {
    var id by remember { mutableStateOf("") }
    var bezeichnung by remember { mutableStateOf("") }
    var eventart by remember { mutableStateOf("") }
    var datum by remember { mutableStateOf("") }
    var uhrzeit by remember { mutableStateOf("") }
    var standort by remember { mutableStateOf("") }
    var teilnehmer by remember { mutableStateOf("") }
    var ausgaben by remember { mutableStateOf("") }
    var additionalInfoPath by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            Text(text = "Add Event", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(bottom = 16.dp))
        }

        item {
            OutlinedTextField(
                value = id,
                onValueChange = { id = it },
                label = { Text("ID") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
        }

        item {
            OutlinedTextField(
                value = bezeichnung,
                onValueChange = { bezeichnung = it },
                label = { Text("Bezeichnung") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
        }

        item {
            OutlinedTextField(
                value = eventart,
                onValueChange = { eventart = it },
                label = { Text("Eventart") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
        }

        item {
            OutlinedTextField(
                value = datum,
                onValueChange = { datum = it },
                label = { Text("Datum") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
        }

        item {
            OutlinedTextField(
                value = uhrzeit,
                onValueChange = { uhrzeit = it },
                label = { Text("Uhrzeit") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
        }

        item {
            OutlinedTextField(
                value = standort,
                onValueChange = { standort = it },
                label = { Text("Standort") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
        }

        item {
            OutlinedTextField(
                value = teilnehmer,
                onValueChange = { teilnehmer = it },
                label = { Text("Teilnehmer") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
        }

        item {
            OutlinedTextField(
                value = ausgaben,
                onValueChange = { ausgaben = it },
                label = { Text("Ausgaben") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
        }

        item {
            OutlinedTextField(
                value = additionalInfoPath,
                onValueChange = { additionalInfoPath = it },
                label = { Text("Additional Info Path") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
        }

        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        val event = Event(
                            id = 1,
                            bezeichnung = "1",
                            eventart = "1",
                            datum = "1",
                            uhrzeit = "1",
                            standort = "1",
                            teilnehmer = "1",
                            ausgaben = 1.00,
                            additionalInfoPath = "1"
                        )
                        eventViewModel.addEvent(event)
                    }
                ) {
                    Text("Save Event")
                }
            }
        }
    }
}
