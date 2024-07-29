package com.example.eventplanner

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eventplanner.data.Event
import com.example.eventplanner.data.EventUtils
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEventScreen(navController: NavController) {
    var bezeichnung by remember { mutableStateOf(TextFieldValue()) }
    var eventart by remember { mutableStateOf(TextFieldValue()) }
    var date by remember { mutableStateOf<LocalDate?>(null) }
    var time by remember { mutableStateOf<LocalTime?>(null) }
    var standort by remember { mutableStateOf(TextFieldValue()) }
    var teilnehmer by remember { mutableStateOf(TextFieldValue()) }
    var ausgaben by remember { mutableStateOf(TextFieldValue()) }
    var additionalInfoPath by remember { mutableStateOf(TextFieldValue()) }

    val context = LocalContext.current
    var showSnackbar by remember { mutableStateOf(false) }
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

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
                value = date?.format(dateFormatter) ?: "",
                onValueChange = {},
                label = { Text("Datum") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    TextButton(onClick = {
                        val calendar = Calendar.getInstance()
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                date = LocalDate.of(year, month + 1, dayOfMonth)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }) {
                        Text("Select Date")
                    }
                }
            )
            OutlinedTextField(
                value = time?.format(timeFormatter) ?: "",
                onValueChange = {},
                label = { Text("Uhrzeit") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    TextButton(onClick = {
                        val calendar = Calendar.getInstance()
                        TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                time = LocalTime.of(hourOfDay, minute)
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    }) {
                        Text("Select Time")
                    }
                }
            )
            OutlinedTextField(
                value = standort,
                onValueChange = { standort = it },
                label = { Text("Standort") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    TextButton(onClick = {
                        // Show Google Maps dialog to pick location
                    }) {
                        Text("Select Location")
                    }
                }
            )
            Box(modifier = Modifier.height(300.dp).fillMaxWidth()) {
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 2f)
                }
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { latLng ->
                        selectedLocation = latLng
                        standort = TextFieldValue("${latLng.latitude}, ${latLng.longitude}")
                    }
                ) {
                    selectedLocation?.let {
                        Marker(
                            state = MarkerState(position = it),
                            title = "Selected Location"
                        )
                    }
                }
            }
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
                try {
                    if (date == null || time == null) {
                        throw IllegalArgumentException("Date and time are required")
                    }

                    val eventDateTime = LocalDateTime.of(date, time)

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
                } catch (e: Exception) {
                    Log.e("AddEventScreen", "Error saving event", e)
                }
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
