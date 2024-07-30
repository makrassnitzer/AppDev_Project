import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eventplanner.data.Event
import com.example.eventplanner.data.EventUtils
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEventScreen(navController: NavController) {
    var bezeichnung by remember { mutableStateOf(TextFieldValue()) }
    var eventart by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf<LocalDate?>(null) }
    var time by remember { mutableStateOf<LocalTime?>(null) }
    var standort by remember { mutableStateOf("") }
    var teilnehmer by remember { mutableStateOf(TextFieldValue()) }
    var ausgaben by remember { mutableStateOf(TextFieldValue()) }
    var additionalInfoPath by remember { mutableStateOf(TextFieldValue()) }

    val context = LocalContext.current
    var showSnackbar by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    val eventTypes = listOf("private", "work", "sports", "concert", "other")

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
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = eventart,
                    onValueChange = {},
                    label = { Text("Eventtype") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .clickable { expanded = true }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    eventTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                eventart = type
                                expanded = false
                            }
                        )
                    }
                }
            }
            OutlinedTextField(
                value = date?.format(dateFormatter) ?: "",
                onValueChange = {},
                label = { Text("Date") },
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
                label = { Text("Time") },
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
            AddressAutocomplete(
                address = standort,
                onAddressChange = { standort = it },
                onAddressSelected = { selectedAddress -> standort = selectedAddress }
            )
            OutlinedTextField(
                value = teilnehmer,
                onValueChange = { teilnehmer = it },
                label = { Text("Participants (Email)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = ausgaben,
                onValueChange = { ausgaben = it },
                label = { Text("Costs") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = additionalInfoPath,
                onValueChange = { additionalInfoPath = it },
                label = { Text("Additional Info") },
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
                        eventart = eventart,
                        datum = eventDateTime,
                        standort = standort,
                        teilnehmer = teilnehmer.text,
                        ausgaben = ausgaben.text.toDoubleOrNull() ?: 0.0,
                        additionalInfoPath = additionalInfoPath.text
                    )
                    EventUtils.saveEventToFile(event, context)

                    // Send email invitation
                    val emailIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "message/rfc822"
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(teilnehmer.text))
                        putExtra(Intent.EXTRA_SUBJECT, "Invitation to ${event.bezeichnung}")
                        putExtra(Intent.EXTRA_TEXT, """
                            You are invited to ${event.bezeichnung}.
                            
                            Event Type: ${event.eventart}
                            Date and Time: ${event.datum.format(dateFormatter)} at ${event.datum.format(timeFormatter)}
                            Location: ${event.standort}
                            Additional Info: ${event.additionalInfoPath}
                        """.trimIndent())
                    }
                    context.startActivity(Intent.createChooser(emailIntent, "Send Email"))

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
                Text(text = "Event created and email sent successfully")
            }
        }
    }
}

@Composable
fun AddressAutocomplete(
    address: String,
    onAddressChange: (String) -> Unit,
    onAddressSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val placesClient = remember { Places.createClient(context) }
    val coroutineScope = rememberCoroutineScope()

    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }

    Column {
        OutlinedTextField(
            value = address,
            onValueChange = { query ->
                onAddressChange(query)
                if (query.isNotEmpty()) {
                    val request = FindAutocompletePredictionsRequest.builder()
                        .setQuery(query)
                        .build()
                    coroutineScope.launch {
                        try {
                            val response = placesClient.findAutocompletePredictions(request).await()
                            predictions = response.autocompletePredictions
                        } catch (e: Exception) {
                            predictions = emptyList() // Handle error appropriately
                        }
                    }
                } else {
                    predictions = emptyList()
                }
            },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn {
            items(predictions) { prediction ->
                ListItem(
                    headlineContent = { Text(prediction.getPrimaryText(null).toString()) },
                    supportingContent = { Text(prediction.getSecondaryText(null).toString()) },
                    modifier = Modifier.clickable {
                        onAddressSelected(prediction.getFullText(null).toString())
                        predictions = emptyList()
                    }
                )
            }
        }
    }
}
